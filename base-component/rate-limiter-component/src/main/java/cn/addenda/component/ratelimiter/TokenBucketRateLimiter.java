package cn.addenda.component.ratelimiter;

import java.util.concurrent.TimeUnit;

/**
 * 令牌桶限流：限制请求的时间间隔 & 在闲时存一批令牌 & 没有桶存等待中的请求
 *
 * @author addenda
 * @since 2022/12/29 18:42
 */
public class TokenBucketRateLimiter implements RateLimiter {

  /**
   * 令牌桶的容量
   */
  private final long capacity;
  /**
   * 每秒产生的令牌的数量
   */
  private final long permitsPerSecond;
  /**
   * 上次请求通过的时间
   */
  private long latestPassedTime;
  /**
   * 当前令牌数量
   */
  private long tokens;

  private final Object lock = new Object();

  public TokenBucketRateLimiter(long capacity, long permitsPerSecond) {
    this.capacity = capacity;
    this.permitsPerSecond = permitsPerSecond;
  }

  @Override
  public void acquire() {
    tryAcquire(TimeUnit.MILLISECONDS, Long.MAX_VALUE);
  }

  @Override
  public void acquire(TimeUnit timeUnit, long timeout) {
    long now = System.currentTimeMillis();
    if (!tryAcquire(timeUnit, timeout)) {
      throw RateLimiterException.timeout(now, 1, timeUnit, timeout);
    }
  }

  @Override
  public boolean tryAcquire() {
    return tryAcquire(null, -1);
  }

  @Override
  public boolean tryAcquire(TimeUnit timeUnit, long timeout) {
    long waitMills;
    long now = System.currentTimeMillis();
    long remainingWaitMills = waitMills(timeUnit, timeout);

    synchronized (lock) {
      // 如果permitsPerSecond是double类型。必须使用取整，不能使用Math.round()
      long newPermits = (now - latestPassedTime) * permitsPerSecond / 1000;
      tokens = Math.max(0, Math.min(tokens + newPermits, capacity));

      if (tokens > 0) {
        latestPassedTime = now;
        tokens--;
        return true;
      } else {
        long interval = 1000 / permitsPerSecond;
        waitMills = interval + latestPassedTime - now;
        if (remainingWaitMills < waitMills) {
          return false;
        } else {
          latestPassedTime = latestPassedTime + interval;
        }
      }
    }
    sleep(waitMills);
    return true;
  }

}
