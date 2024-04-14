package cn.addenda.component.ratelimiter;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 漏桶限流：限制请求的时间间隔 & 时间间隔不到且等待的请求不超过桶容量时在桶里等待
 *
 * @author addenda
 * @since 2022/12/28 14:15
 */
@Slf4j
public class LeakyBucketRateLimiter implements RateLimiter {

  /**
   * 最大等待时间（ms）
   */
  private final long maxQueueingTime;
  /**
   * 每秒允许通过的请求数量
   */
  private final double permitsPerSecond;
  /**
   * 每两次请求之间的间隔（ms）
   */
  private final long interval;
  /**
   * 桶的总容量
   */
  private final long capacity;
  /**
   * 上次请求通过的时间
   */
  private long latestPassedTime = -1;

  private final Object lock = new Object();

  public LeakyBucketRateLimiter(long maxQueueingTime, double permitsPerSecond) {
    this.maxQueueingTime = maxQueueingTime;
    this.permitsPerSecond = permitsPerSecond;
    this.interval = Math.round(1000 / permitsPerSecond);
    this.capacity = Math.round(maxQueueingTime * permitsPerSecond / 1000);
    attributeCheck();
  }

  private void attributeCheck() {
    if (interval * permitsPerSecond != 1000) {
      log.warn("interval * permitsPerSecond != 1000, interval: {}, permitsSecond: {}. ", interval, permitsPerSecond);
    }
    if (capacity * 1000 != maxQueueingTime * permitsPerSecond) {
      log.warn("capacity * 1000 != maxQueueingTime * permitsPerSecond, capacity: {}, maxQueueingTime: {}, permitsPerSecond: {}. ",
              capacity, maxQueueingTime, permitsPerSecond);
    }
  }

  @Override
  public void acquire() {
    tryAcquire(TimeUnit.MILLISECONDS, Long.MAX_VALUE);
  }

  @Override
  public void acquire(TimeUnit timeUnit, long timeout) {
    long now = System.currentTimeMillis();
    int doTryAcquire = doTryAcquire(timeUnit, timeout);
    if (doTryAcquire == -1) {
      throw RateLimiterException.exceed(now, 1, maxQueueingTime);
    }
    if (doTryAcquire == -2) {
      throw RateLimiterException.timeout(now, 1, timeUnit, timeout);
    }
  }

  /**
   * 能通过的请求是匀速的。
   */
  @Override
  public boolean tryAcquire() {
    return tryAcquire(null, -1);
  }

  @Override
  public boolean tryAcquire(TimeUnit timeUnit, long timeout) {
    return doTryAcquire(timeUnit, timeout) == 0;
  }

  private int doTryAcquire(TimeUnit timeUnit, long timeout) {
    long waitMills;
    long now = System.currentTimeMillis();
    long remainingWaitMills = waitMills(timeUnit, timeout);

    synchronized (lock) {
      long expectedTime = interval + latestPassedTime;
      if (expectedTime <= now) {
        latestPassedTime = now;
        return 0;
      } else {
        waitMills = expectedTime - now;
        if (maxQueueingTime < waitMills) {
          return -1;
        }
        if (remainingWaitMills < waitMills) {
          return -2;
        }
        latestPassedTime = expectedTime;
      }
    }
    sleep(waitMills);
    return 0;
  }

  @Override
  public String toString() {
    return "LeakyBucketRateLimiter{" +
            "maxQueueingTime=" + maxQueueingTime +
            ", permitsPerSecond=" + permitsPerSecond +
            ", interval=" + interval +
            ", capacity=" + capacity +
            ", latestPassedTime=" + latestPassedTime +
            ", lock=" + lock +
            '}';
  }
}
