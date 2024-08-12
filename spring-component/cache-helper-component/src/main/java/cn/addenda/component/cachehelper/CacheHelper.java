package cn.addenda.component.cachehelper;

import cn.addenda.component.cache.CacheException;
import cn.addenda.component.cache.ExpiredKVCache;
import cn.addenda.component.jackson.util.TypeFactoryUtils;
import cn.addenda.component.jdk.allocator.lock.LockAllocator;
import cn.addenda.component.jdk.allocator.lock.ReentrantLockAllocator;
import cn.addenda.component.jdk.concurrent.SimpleNamedThreadFactory;
import cn.addenda.component.jdk.exception.component.ComponentServiceException;
import cn.addenda.component.jdk.util.CompletableFutureUtils;
import cn.addenda.component.jdk.util.DateUtils;
import cn.addenda.component.jdk.util.RetryUtils;
import cn.addenda.component.jdk.util.SleepUtils;
import cn.addenda.component.jackson.util.JacksonUtils;
import cn.addenda.component.ratelimiter.RateLimiter;
import cn.addenda.component.ratelimiter.RequestIntervalRateLimiter;
import cn.addenda.component.ratelimiter.allcator.RateLimiterAllocator;
import cn.addenda.component.ratelimiter.allcator.TokenBucketRateLimiterAllocator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author addenda
 * @since 2023/05/30
 */
@Slf4j
public class CacheHelper implements DisposableBean {

  public static final String NULL_OBJECT = "_NIL";

  /**
   * ppf: performance first
   */
  public static final String PERFORMANCE_FIRST_PREFIX = "pff:";
  /**
   * rdf: realtime data first
   */
  public static final String REALTIME_DATA_FIRST_PREFIX = "rdf:";

  private static final String CACHE_EXPIRED_OR_NOT_MSG = "获取到 [{}] 的数据 [{}] [{}]。";

  private static final String BUILD_CACHE_SUCCESS_MSG = "构建缓存 [{}] [成功]，获取到数据 [{}]，缓存到期时间 [{}]。";
  private static final String PPF_BUILD_CACHE_MSG = "异步构建缓存 [{}] [{}]，提交时间[{}]，最大执行耗时[{}]ms，开始执行时间[{}]，最大完成时间[{}]，完成时间[{}]。";
  private static final String PPF_SUBMIT_BUILD_CACHE_TASK_SUCCESS_MSG = "获取锁 [{}] [成功]，提交了缓存重建任务，返回过期数据 [{}]。";
  private static final String PPF_SUBMIT_BUILD_CACHE_TASK_FAILED_MSG = "获取锁 [{}] [失败]，未提交缓存重建任务，返回过期数据 [{}]。";

  private static final String RDF_TRY_LOCK_FAIL_TERMINAL_MSG = "第 [{}] 次未获取到锁 [{}]，终止获取锁";
  private static final String RDF_TRY_LOCK_FAIL_WAIT_MSG = "第 [{}] 次未获取到锁 [{}]，休眠 [{}]ms";

  private static final String CLEAR_CACHE_MSG = "清理缓存 [{}] [{}]。xId [{}]。预计 [{}] 执行 [{}]。";
  private static final String DELAY_CLEAR_CACHE_MSG = "延迟清理缓存 [%s] [%s]。xId[%s]。出生时间[%s]，提交时间[%s]，延迟[%s]ms，预期开始执行时间[%s]，实际开始执行时间[%s]，最大完成时间[%s]，当前时间[%s]。";

  /**
   * ms <p/>
   * 空 缓存多久
   */
  @Setter
  @Getter
  private Long cacheNullTtl = 5 * 60 * 1000L;

  /**
   * ms <p/>
   * ppf: 提交异步任务后等待多久 <p/>
   * rdf: 获取不到锁时休眠多久
   */
  @Setter
  @Getter
  private long lockWaitTime = 50L;

  @Setter
  @Getter
  private int rdfBusyLoop = 3;

  /**
   * key是prefix
   */
  private final Map<String, RequestIntervalRateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

  /**
   * 锁的管理器，防止查询相同数据的多个线程拿到不同的锁，导致加锁失败
   */
  private final LockAllocator<?> lockAllocator;

  /**
   * rdf模式下，查询数据库的限流器，解决缓存击穿问题
   */
  private final RateLimiterAllocator<?> realQueryRateLimiterAllocator;

  /**
   * 缓存异步构建使用的线程池
   */
  private final ExecutorService cacheBuildEs;

  /**
   * 真正存储数据的缓存
   */
  private final ExpiredKVCache<String, String> expiredKVCache;

  /**
   * ppf模式下过期检测间隔（ms）
   */
  private final long ppfExpirationDetectionInterval;

  /**
   * 在可以使用ServiceException的地方抛ServiceException
   */
  private final boolean useServiceException;

  public static final long DEFAULT_PPF_EXPIRATION_DETECTION_INTERVAL = 100L;

  private DelayQueue<DeleteTask> deleteTaskQueue;

  private Thread deleteTaskConsumer;

  public CacheHelper(ExpiredKVCache<String, String> expiredKVCache, long ppfExpirationDetectionInterval, LockAllocator<?> lockAllocator,
                     ExecutorService cacheBuildEs, RateLimiterAllocator<?> realQueryRateLimiterAllocator, boolean useServiceException) {
    this.expiredKVCache = expiredKVCache;
    this.ppfExpirationDetectionInterval = ppfExpirationDetectionInterval;
    this.lockAllocator = lockAllocator;
    this.cacheBuildEs = cacheBuildEs;
    this.realQueryRateLimiterAllocator = realQueryRateLimiterAllocator;
    this.useServiceException = useServiceException;
    this.initDeleteTaskConsumer();
  }

  public CacheHelper(ExpiredKVCache<String, String> expiredKVCache, long ppfExpirationDetectionInterval, LockAllocator<?> lockAllocator) {
    this.expiredKVCache = expiredKVCache;
    this.ppfExpirationDetectionInterval = ppfExpirationDetectionInterval;
    this.lockAllocator = lockAllocator;
    this.cacheBuildEs = defaultCacheBuildEs();
    this.realQueryRateLimiterAllocator = new TokenBucketRateLimiterAllocator(10, 10);
    this.useServiceException = false;
    this.initDeleteTaskConsumer();
  }

  public CacheHelper(ExpiredKVCache<String, String> expiredKVCache, long ppfExpirationDetectionInterval) {
    this.expiredKVCache = expiredKVCache;
    this.ppfExpirationDetectionInterval = ppfExpirationDetectionInterval;
    this.lockAllocator = new ReentrantLockAllocator();
    this.cacheBuildEs = defaultCacheBuildEs();
    this.realQueryRateLimiterAllocator = new TokenBucketRateLimiterAllocator(10, 10);
    this.useServiceException = false;
    this.initDeleteTaskConsumer();
  }

  public CacheHelper(ExpiredKVCache<String, String> expiredKVCache) {
    this.expiredKVCache = expiredKVCache;
    this.ppfExpirationDetectionInterval = DEFAULT_PPF_EXPIRATION_DETECTION_INTERVAL;
    this.lockAllocator = new ReentrantLockAllocator();
    this.cacheBuildEs = defaultCacheBuildEs();
    this.realQueryRateLimiterAllocator = new TokenBucketRateLimiterAllocator(10, 10);
    this.useServiceException = false;
    this.initDeleteTaskConsumer();
  }

  protected ExecutorService defaultCacheBuildEs() {
    return new ThreadPoolExecutor(
            2,
            2,
            30,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100000),
            new SimpleNamedThreadFactory("CacheHelper-Rebuild"));
  }

  protected void initDeleteTaskConsumer() {
    deleteTaskQueue = new DelayQueue<>();
    deleteTaskConsumer = new Thread(() -> {
      while (true) {
        DeleteTask t = null;
        try {
          t = deleteTaskQueue.take();
          final DeleteTask take = t;
          String key = take.getKey();
          CompletableFutureUtils
                  .orTimeout(CompletableFuture.runAsync(() -> {
                    take.setRealExecutionTime(System.currentTimeMillis());
                    expiredKVCache.delete(key);
                    log.info(getDelayDeleteMsg(key, take, "成功"));
                  }, cacheBuildEs), take.getTimeout(), TimeUnit.MILLISECONDS)
                  .exceptionally(
                          throwable -> {
                            if (throwable instanceof CompletionException && throwable.getCause() instanceof TimeoutException) {
                              log.error(getDelayDeleteMsg(key, take, "超时"));
                            } else {
                              deleteTaskQueue.put(new DeleteTask(take.getXId(), take.getKey(), take.getSince(), take.getDelay()));
                              log.error(getDelayDeleteMsg(key, take, "异常"), throwable);
                            }
                            return null;
                          });
        } catch (RejectedExecutionException e) {
          if (t != null) {
            String key = t.getKey();
            log.error(getDelayDeleteMsg(key, t, "线程池触发拒绝策略"), e);
          }
        } catch (InterruptedException e) {
          break;
        } catch (Throwable e) {
          if (t != null) {
            String key = t.getKey();
            log.error(getDelayDeleteMsg(key, t, "未知异常"), e);
          }
        }
      }
    });
    deleteTaskConsumer.setDaemon(true);
    deleteTaskConsumer.setName("DeleteTaskThread");
    deleteTaskConsumer.start();
  }

  private String getDelayDeleteMsg(String key, DeleteTask take, String prefix) {
    return String.format(DELAY_CLEAR_CACHE_MSG, key, prefix, take.getXId(), toDateTimeStr(take.getSince()),
            toDateTimeStr(take.getStart()), take.getDelay(),
            toDateTimeStr(take.getExpectedExecutionTime()),
            take.getRealExecutionTime() == null ? "未执行" : toDateTimeStr(take.getRealExecutionTime()),
            toDateTimeStr(take.getExpectedExecutionTime() + take.getTimeout()),
            toDateTimeStr(System.currentTimeMillis()));
  }

  public <I> void acceptWithPpf(String keyPrefix, I id, Consumer<I> consumer) {
    doAccept(keyPrefix, id, consumer, PERFORMANCE_FIRST_PREFIX);
  }

  public <I> void acceptWithRdf(String keyPrefix, I id, Consumer<I> consumer) {
    doAccept(keyPrefix, id, consumer, REALTIME_DATA_FIRST_PREFIX);
  }

  public <I> void doAccept(String keyPrefix, I id, Consumer<I> consumer, String mode) {
    String key = keyPrefix + mode + id;
    long l = System.currentTimeMillis();
    consumer.accept(id);
    doDelete(key, l);
  }

  public <I, R> R applyWithPpf(String keyPrefix, I id, Function<I, R> function) {
    return doApply(keyPrefix, id, function, PERFORMANCE_FIRST_PREFIX);
  }

  public <I, R> R applyWithRdf(String keyPrefix, I id, Function<I, R> function) {
    return doApply(keyPrefix, id, function, REALTIME_DATA_FIRST_PREFIX);
  }

  private <I, R> R doApply(String keyPrefix, I id, Function<I, R> function, String mode) {
    String key = keyPrefix + mode + id;
    long l = System.currentTimeMillis();
    R apply = function.apply(id);
    doDelete(key, l);
    return apply;
  }

  private void doDelete(String key, long l) {
    String xId = UUID.randomUUID().toString();
    try {
      RetryUtils.retryWhenException(() -> expiredKVCache.delete(key), key);
      DeleteTask deleteTask = new DeleteTask(xId, key, 2 * (System.currentTimeMillis() - l));
      log.info(CLEAR_CACHE_MSG, key, "成功", xId, toDateTimeStr(deleteTask.getExpectedExecutionTime()), "延迟删除");
      if (cacheBuildEs != null && !cacheBuildEs.isShutdown()) {
        deleteTaskQueue.put(deleteTask);
      }
    } catch (Throwable e) {
      DeleteTask deleteTask = new DeleteTask(xId, key, (System.currentTimeMillis() - l));
      log.error(CLEAR_CACHE_MSG, key, "异常", xId, toDateTimeStr(deleteTask.getExpectedExecutionTime()), "重试", e);
      if (cacheBuildEs != null && !cacheBuildEs.isShutdown()) {
        deleteTaskQueue.put(deleteTask);
      }
    }
  }

  /**
   * 性能优先的缓存查询方法，基于逻辑过期实现。
   *
   * @param keyPrefix 与id一起构成完整的键
   * @param id        键值
   * @param rType     返回值类型
   * @param rtQuery   查询实时数据
   * @param ttl       过期时间
   * @param <R>       返回值类型
   * @param <I>       键值类型
   */
  public <R, I> R queryWithPpf(
          String keyPrefix, I id, Class<R> rType, Function<I, R> rtQuery, Long ttl) {
    return queryWithPpf(keyPrefix, id, TypeFactoryUtils.construct(rType), rtQuery, ttl);
  }

  /**
   * 性能优先的缓存查询方法，基于逻辑过期实现。
   *
   * @param keyPrefix 与id一起构成完整的键
   * @param id        键值
   * @param rType     返回值类型
   * @param rtQuery   查询实时数据
   * @param ttl       过期时间
   * @param <R>       返回值类型
   * @param <I>       键值类型
   */
  public <R, I> R queryWithPpf(
          String keyPrefix, I id, TypeReference<R> rType, Function<I, R> rtQuery, Long ttl) {
    return queryWithPpf(keyPrefix, id, TypeFactoryUtils.construct(rType), rtQuery, ttl);
  }

  /**
   * 性能优先的缓存查询方法，基于逻辑过期实现。
   *
   * @param keyPrefix 与id一起构成完整的键
   * @param id        键值
   * @param rType     返回值类型
   * @param rtQuery   查询实时数据
   * @param ttl       过期时间
   * @param <R>       返回值类型
   * @param <I>       键值类型
   */
  public <R, I> R queryWithPpf(
          String keyPrefix, I id, JavaType rType, Function<I, R> rtQuery, Long ttl) {
    String key = keyPrefix + PERFORMANCE_FIRST_PREFIX + id;
    // 1 查询缓存
    String cachedJson = expiredKVCache.get(key);
    // 2.1 缓存不存在则基于互斥锁构建缓存
    if (cachedJson == null) {
      // 查询数据库
      R r = queryWithRdf(keyPrefix, id, rType, rtQuery, ttl, false);
      // 存在缓存里
      setCacheData(key, r, ttl);
      return r;
    }
    // 2.2 缓存存在则进入逻辑过期的判断
    else {
      String lockKey = getLockKey(key);
      // 3.1 命中，需要先把json反序列化为对象
      CacheData<R> cacheData = JacksonUtils.toObj(cachedJson, TypeFactoryUtils.constructParametricType(CacheData.class, rType));
      LocalDateTime expireTime = cacheData.getExpireTime();
      R data = cacheData.getData();
      // 4.1 判断是否过期，未过期，直接返回
      if (expireTime.isAfter(LocalDateTime.now())) {
        log.debug(CACHE_EXPIRED_OR_NOT_MSG, key, data, "未过期");
      }
      // 4.2 判断是否过期，已过期，需要缓存重建
      else {
        // 5.1 获取互斥锁，成功，开启独立线程，进行缓存重建
        Lock lock = lockAllocator.allocate(lockKey);
        AtomicBoolean newDataReady = new AtomicBoolean(false);
        AtomicReference<R> newData = new AtomicReference<>(null);
        if (lock.tryLock()) {
          try {
            long l = System.currentTimeMillis();
            cacheBuildEs.submit(() -> {
              long start = System.currentTimeMillis();
              long maxCost = 2 * lockWaitTime;
              try {
                // 查询数据库
                R r = rtQuery.apply(id);
                // 存在缓存里
                newData.set(r);
                newDataReady.set(true);
                setCacheData(key, r, ttl);
                if (System.currentTimeMillis() - l > maxCost) {
                  log.error(PPF_BUILD_CACHE_MSG, key, "超时", toDateTimeStr(l), maxCost, toDateTimeStr(start),
                          toDateTimeStr(l + maxCost), toDateTimeStr(System.currentTimeMillis()));
                }
              } catch (Throwable e) {
                log.error(PPF_BUILD_CACHE_MSG, key, "异常", toDateTimeStr(l), maxCost, toDateTimeStr(start),
                        toDateTimeStr(l + maxCost), toDateTimeStr(System.currentTimeMillis()), e);
              }
            });
            // 提交完缓存构建任务后休息一段时间，防止其他线程提交缓存构建任务
            SleepUtils.sleep(TimeUnit.MILLISECONDS, lockWaitTime);
            if (newDataReady.get()) {
              return newData.get();
            } else {
              log.info(PPF_SUBMIT_BUILD_CACHE_TASK_SUCCESS_MSG, lockKey, data);
            }
          } finally {
            try {
              lock.unlock();
            } finally {
              lockAllocator.release(lockKey);
            }
          }
        }
        // 5.2 获取互斥锁，未成功不进行缓存重建
        else {
          lockAllocator.release(lockKey);
          log.info(PPF_SUBMIT_BUILD_CACHE_TASK_FAILED_MSG, lockKey, data);

          // -----------------------------------------------------------
          // 提交重建的线程如果没有在等待时间内获取到新的数据，不会走下面的告警。
          // 这是为了防止低并发下输出不必要的日志。
          // -----------------------------------------------------------

          // 如果过期了，输出告警信息。
          // 使用限流器防止高并发下大量打印日志。
          RequestIntervalRateLimiter rateLimiter = rateLimiterMap.computeIfAbsent(
                  keyPrefix + PERFORMANCE_FIRST_PREFIX, s -> new RequestIntervalRateLimiter(ppfExpirationDetectionInterval));
          if (rateLimiter.tryAcquire()) {
            log.error(CACHE_EXPIRED_OR_NOT_MSG, key, data, "已过期");
          }
        }
      }
      return data;
    }
  }

  private <R> void setCacheData(String key, R r, long ttl) {
    // 设置逻辑过期
    CacheData<R> newCacheData = new CacheData<>(r);
    if (r == null) {
      newCacheData.setExpireTime(LocalDateTime.now().plus(Math.min(ttl, cacheNullTtl), ChronoUnit.MILLIS));
    } else {
      newCacheData.setExpireTime(LocalDateTime.now().plus(ttl, ChronoUnit.MILLIS));
    }
    // 写缓存
    expiredKVCache.set(key, JacksonUtils.toStr(newCacheData), ttl * 2, TimeUnit.MILLISECONDS);
    log.info(BUILD_CACHE_SUCCESS_MSG, key, r, toDateTimeStr(DateUtils.localDateTimeToTimestamp(newCacheData.getExpireTime())));
  }


  /**
   * 实时数据优先的缓存查询方法，基于互斥锁实现。
   *
   * @param keyPrefix 与id一起构成完整的键
   * @param id        键值
   * @param rType     返回值类型
   * @param rtQuery   查询实时数据
   * @param ttl       过期时间
   * @param <R>       返回值类型
   * @param <I>       键值类型
   */
  public <R, I> R queryWithRdf(
          String keyPrefix, I id, JavaType rType, Function<I, R> rtQuery, Long ttl) {
    return queryWithRdf(keyPrefix, id, rType, rtQuery, ttl, true);
  }

  /**
   * 实时数据优先的缓存查询方法，基于互持锁实现。
   *
   * @param keyPrefix 与id一起构成完整的键
   * @param id        键值
   * @param rType     返回值类型
   * @param rtQuery   查询实时数据
   * @param ttl       过期时间
   * @param <R>       返回值类型
   * @param <I>       键值类型
   */
  public <R, I> R queryWithRdf(
          String keyPrefix, I id, Class<R> rType, Function<I, R> rtQuery, Long ttl) {
    return queryWithRdf(keyPrefix, id, rType, rtQuery, ttl, true);
  }

  /**
   * 实时数据优先的缓存查询方法，基于互持锁实现。
   *
   * @param keyPrefix 与id一起构成完整的键
   * @param id        键值
   * @param rType     返回值类型
   * @param rtQuery   查询实时数据
   * @param ttl       过期时间
   * @param <R>       返回值类型
   * @param <I>       键值类型
   */
  public <R, I> R queryWithRdf(
          String keyPrefix, I id, TypeReference<R> rType, Function<I, R> rtQuery, Long ttl) {
    return queryWithRdf(keyPrefix, id, TypeFactoryUtils.construct(rType), rtQuery, ttl, true);
  }

  /**
   * 实时数据优先的缓存查询方法，基于互持锁实现。
   *
   * @param keyPrefix 与id一起构成完整的键
   * @param id        键值
   * @param rType     返回值类型
   * @param rtQuery   查询实时数据
   * @param ttl       过期时间
   * @param cache     是否将实时查询的数据缓存
   * @param <R>       返回值类型
   * @param <I>       键值类型
   */
  private <R, I> R queryWithRdf(
          String keyPrefix, I id, JavaType rType, Function<I, R> rtQuery, Long ttl, boolean cache) {
    return doQueryWithRdf(keyPrefix, id, rType, rtQuery, ttl, 0, cache);
  }

  /**
   * 实时数据优先的缓存查询方法，基于互持锁实现。
   *
   * @param keyPrefix 与id一起构成完整的键
   * @param id        键值
   * @param rType     返回值类型
   * @param rtQuery   查询实时数据
   * @param ttl       过期时间
   * @param cache     是否将实时查询的数据缓存
   * @param <R>       返回值类型
   * @param <I>       键值类型
   */
  private <R, I> R queryWithRdf(
          String keyPrefix, I id, Class<R> rType, Function<I, R> rtQuery, Long ttl, boolean cache) {
    return doQueryWithRdf(keyPrefix, id, TypeFactoryUtils.construct(rType), rtQuery, ttl, 0, cache);
  }

  /**
   * 实时数据优先的缓存查询方法，基于互持锁实现。
   *
   * @param keyPrefix 与id一起构成完整的键
   * @param id        键值
   * @param rType     返回值类型
   * @param rtQuery   查询实时数据
   * @param ttl       过期时间
   * @param itr       第几次尝试
   * @param cache     是否将实时查询的数据缓存
   * @param <R>       返回值类型
   * @param <I>       键值类型
   */
  private <R, I> R doQueryWithRdf(
          String keyPrefix, I id, JavaType rType, Function<I, R> rtQuery, Long ttl, int itr, boolean cache) {
    String key = keyPrefix + REALTIME_DATA_FIRST_PREFIX + id;
    // 1.查询缓存
    String resultJson = expiredKVCache.get(key);
    // 2.如果返回的是占位的空值，返回null
    if (NULL_OBJECT.equals(resultJson)) {
      log.debug("获取到 [{}] 的数据为空占位。", key);
      return null;
    }
    // 3.1如果字符串不为空，返回对象
    if (resultJson != null) {
      log.debug(CACHE_EXPIRED_OR_NOT_MSG, key, resultJson, "未过期");
      return JacksonUtils.toObj(resultJson, rType);
    }
    // 3.2如果字符串为空，进行缓存构建
    else {
      Supplier<R> supplier = () -> {
        R r = rtQuery.apply(id);
        if (cache) {
          LocalDateTime expireTime = LocalDateTime.now();
          if (r == null) {
            long realTtl = Math.min(cacheNullTtl, ttl);
            expireTime = expireTime.plus(realTtl, ChronoUnit.MILLIS);
            expiredKVCache.set(key, NULL_OBJECT, realTtl, TimeUnit.MILLISECONDS);
          } else {
            expireTime = expireTime.plus(ttl, ChronoUnit.MILLIS);
            expiredKVCache.set(key, JacksonUtils.toStr(r), ttl, TimeUnit.MILLISECONDS);
          }
          log.info(BUILD_CACHE_SUCCESS_MSG, key, r, toDateTimeStr(DateUtils.localDateTimeToTimestamp(expireTime)));
        }
        return r;
      };

      String lockKey = getLockKey(key);
      RateLimiter rateLimiter = realQueryRateLimiterAllocator.allocate(lockKey);
      if (rateLimiter.tryAcquire()) {
        try {
          return supplier.get();
        } finally {
          realQueryRateLimiterAllocator.release(lockKey);
        }
      } else {
        realQueryRateLimiterAllocator.release(lockKey);

        // 4.1获取互斥锁，获取到进行缓存构建
        Lock lock = lockAllocator.allocate(lockKey);
        if (lock.tryLock()) {
          try {
            return supplier.get();
          } finally {
            try {
              lock.unlock();
            } finally {
              lockAllocator.release(lockKey);
            }
          }
        }
        // 4.2获取互斥锁，获取不到就休眠直至抛出异常
        else {
          lockAllocator.release(lockKey);
          itr++;
          if (itr >= rdfBusyLoop) {
            log.error(RDF_TRY_LOCK_FAIL_TERMINAL_MSG, itr, lockKey);
            if (useServiceException) {
              throw new ComponentServiceException("系统繁忙，请稍后再试！");
            } else {
              throw new CacheException("系统繁忙，请稍后再试！");
            }
          } else {
            log.info(RDF_TRY_LOCK_FAIL_WAIT_MSG, itr, lockKey, lockWaitTime);
            SleepUtils.sleep(TimeUnit.MILLISECONDS, lockWaitTime);
            // 递归进入的时候，当前线程的tryLock是失败的，所以当前线程不持有锁，即递归进入的状态和初次进入的状态一致
            return doQueryWithRdf(keyPrefix, id, rType, rtQuery, ttl, itr, cache);
          }
        }
      }
    }
  }

  private String getLockKey(String key) {
    return key + ":lock";
  }

  @Override
  public void destroy() throws Exception {
    if (deleteTaskQueue != null) {
      deleteTaskConsumer.interrupt();
    }
    if (cacheBuildEs != null) {
      try {
        log.info("CacheHelper-Rebuild 开始关闭。");
        cacheBuildEs.shutdown();
        if (!cacheBuildEs.awaitTermination(30, TimeUnit.SECONDS)) {
          log.error("CacheHelper-Rebuild 关闭后等待超过30秒未终止：{}。", cacheBuildEs);
          cacheBuildEs.awaitTermination(30, TimeUnit.SECONDS);
        }
        log.info("CacheHelper-Rebuild 正常关闭。");
      } catch (Exception e) {
        log.error("CacheHelper-Rebuild 异常关闭：{}！", cacheBuildEs, e);
        if (e instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
      }
    }
    executeRemainingDeleteTask();
  }

  private void executeRemainingDeleteTask() {
    if (deleteTaskQueue == null) {
      return;
    }
    DeleteTask[] array = deleteTaskQueue.toArray(new DeleteTask[]{});
    if (array.length > 0) {
      log.error("CacheHelper-Rebuild 已关闭，还有{}个任务未被执行！", array.length);
      for (DeleteTask deleteTask : array) {
        try {
          RetryUtils.retryWhenException(() -> expiredKVCache.delete(deleteTask.getKey()), deleteTask.getKey());
        } catch (Throwable e) {
          log.error(getDelayDeleteMsg(deleteTask.getKey(), deleteTask, "服务关闭时延迟任务未执行！"), e);
        }
      }
    }
  }

  @Getter
  @ToString
  private static class DeleteTask implements Delayed {

    /**
     * 每一次写操作都会生成一个xId。一个xId表示一次更新数据库参数，多次缓存删除重试具备相同的xId。
     */
    private final String xId;

    private final String key;

    /**
     * 出生时间
     */
    private final long since;

    /**
     * 提交时间。由于存在重试场景，所以提交时间和出生时间不一致。
     */
    private final long start;

    /**
     * 更新数据库 + 删除缓存
     */
    private final long delay;

    /**
     * 预计执行时间
     */
    private final long expectedExecutionTime;

    @Setter
    private Long realExecutionTime;

    private DeleteTask(String xId, String key, long delay) {
      this.xId = xId;
      this.key = key;
      this.delay = delay;
      this.since = System.currentTimeMillis();
      this.start = since;
      this.expectedExecutionTime = start + delay;
    }

    private DeleteTask(String xId, String key, long since, long delay) {
      this.xId = xId;
      this.key = key;
      this.delay = delay;
      this.since = since;
      this.start = System.currentTimeMillis();
      this.expectedExecutionTime = start + delay;
    }

    @Override
    public long getDelay(TimeUnit unit) {
      return unit.convert(expectedExecutionTime - System.currentTimeMillis(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
      if (o == null) {
        throw new NullPointerException("arg can not be null!");
      }
      DeleteTask t = (DeleteTask) o;
      if (this.expectedExecutionTime - t.expectedExecutionTime < 0) {
        return -1;
      } else if (this.expectedExecutionTime == t.expectedExecutionTime) {
        return 0;
      } else {
        return 1;
      }
    }

    public long getTimeout() {
      return delay * 2;
    }

  }

  private String toDateTimeStr(long ts) {
    return DateUtils.format(DateUtils.timestampToLocalDateTime(ts), DateUtils.yMdHmsS_FORMATTER);
  }

}
