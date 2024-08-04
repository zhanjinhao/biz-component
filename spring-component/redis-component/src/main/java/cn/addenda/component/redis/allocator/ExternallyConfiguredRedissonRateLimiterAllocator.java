package cn.addenda.component.redis.allocator;

import cn.addenda.component.jdk.allocator.AbstractNamedExpiredAllocator;
import cn.addenda.component.jdk.allocator.AllocatorException;
import cn.addenda.component.redis.ratelimiter.ExternallyConfiguredRedissonRateLimiter;
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
 * @since 2023/9/27 18:43
 */
public class ExternallyConfiguredRedissonRateLimiterAllocator
        extends AbstractNamedExpiredAllocator<RRateLimiterWrapper> {

  private final RedissonClient redissonClient;
  private final CommandAsyncExecutor executor;

  public ExternallyConfiguredRedissonRateLimiterAllocator(String configureName, RedissonClient redissonClient) {
    super(configureName);
    if (!(redissonClient instanceof Redisson)) {
      throw new AllocatorException("ExpiredRedissonRateLimiterAllocator 只支持 org.redisson.Redisson。");
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
    new ExternallyConfiguredRedissonRateLimiter(executor, getName(), name).delete();
  }

  @Override
  public RRateLimiterWrapper allocateWithDefaultTtl(String name) {
    return allocate(name, TimeUnit.MILLISECONDS, -2);
  }

  @Override
  public RRateLimiterWrapper allocate(String name, TimeUnit timeUnit, long timeout) {
    ExternallyConfiguredRedissonRateLimiter rateLimiter = new ExternallyConfiguredRedissonRateLimiter(
            executor, getName(), name, timeUnit.toMillis(timeout));
    return new RRateLimiterWrapper(rateLimiter);
  }

  public void setRate(RateType type, long rate, long interval) {
    new ExternallyConfiguredRedissonRateLimiter(
            executor, getName(), "").setRate(type, rate, interval, RateIntervalUnit.MILLISECONDS);
  }

  private CommandAsyncExecutor extractCommandAsyncExecutor() {
    Field commandExecutorField = ReflectionUtils.findField(Redisson.class, "commandExecutor");
    ReflectionUtils.makeAccessible(commandExecutorField);
    return (CommandAsyncExecutor) ReflectionUtils.getField(commandExecutorField, redissonClient);
  }

}
