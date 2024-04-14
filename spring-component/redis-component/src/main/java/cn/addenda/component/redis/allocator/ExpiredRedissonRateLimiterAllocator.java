package cn.addenda.component.redis.allocator;

import cn.addenda.component.allocator.AllocatorException;
import cn.addenda.component.allocator.ExpiredAllocator;
import cn.addenda.component.redis.ratelimiter.ExpiredRedissonRateLimiter;
import cn.addenda.component.redis.ratelimiter.RRateLimiterWrapper;
import org.redisson.Redisson;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.redisson.command.CommandAsyncExecutor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/9/16 11:53
 */
public class ExpiredRedissonRateLimiterAllocator implements ExpiredAllocator<RRateLimiterWrapper> {

  private final RateType mode;
  private final long rate;
  private final long rateInterval;
  private final RateIntervalUnit rateIntervalUnit;
  private final RedissonClient redissonClient;
  private final CommandAsyncExecutor executor;

  public ExpiredRedissonRateLimiterAllocator(
          RateType mode, long rate, long rateInterval,
          RateIntervalUnit rateIntervalUnit, RedissonClient redissonClient) {
    this.mode = mode;
    this.rate = rate;
    this.rateInterval = rateInterval;
    this.rateIntervalUnit = rateIntervalUnit;
    if (!(redissonClient instanceof Redisson)) {
      throw new AllocatorException("ExpiredRedissonRateLimiterAllocator 只支持 org.redisson.Redisson！");
    }
    this.redissonClient = redissonClient;
    this.executor = extractCommandAsyncExecutor();
  }

  @Override
  public RRateLimiterWrapper allocate(String name) {
    return allocate(name, TimeUnit.DAYS, 3650);
  }

  @Override
  public void release(String name) {
    new ExpiredRedissonRateLimiter(
            executor, name, mode, rate, rateInterval, rateIntervalUnit).delete();
  }

  /**
   * 默认过期时间是 interval * 2
   */
  @Override
  public RRateLimiterWrapper allocateWithDefaultTtl(String name) {
    return allocate(name, TimeUnit.MILLISECONDS, rateIntervalUnit.toMillis(rateInterval) * 2);
  }

  @Override
  public RRateLimiterWrapper allocate(String name, TimeUnit timeUnit, long ttl) {
    ExpiredRedissonRateLimiter rateLimiter = new ExpiredRedissonRateLimiter(
            executor, name, mode, rate, rateInterval, rateIntervalUnit, timeUnit, ttl);
    return new RRateLimiterWrapper(rateLimiter);
  }

  private CommandAsyncExecutor extractCommandAsyncExecutor() {
    Field commandExecutorField = ReflectionUtils.findField(Redisson.class, "commandExecutor");
    ReflectionUtils.makeAccessible(commandExecutorField);
    return (CommandAsyncExecutor) ReflectionUtils.getField(commandExecutorField, redissonClient);
  }

  @Override
  public String toString() {
    return "ExpiredRedissonRateLimiterAllocator{" +
            "mode=" + mode +
            ", rate=" + rate +
            ", rateInterval=" + rateInterval +
            ", rateIntervalUnit=" + rateIntervalUnit +
            '}';
  }
}
