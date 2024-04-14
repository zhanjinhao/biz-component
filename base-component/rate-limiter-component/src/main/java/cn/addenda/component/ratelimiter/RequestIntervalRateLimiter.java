package cn.addenda.component.ratelimiter;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求间隔限流：限制请求的时间间隔 & 没有桶存放请求
 *
 * @author addenda
 * @since 2022/12/28 16:57
 */
@Slf4j
public class RequestIntervalRateLimiter implements RateLimiter {

  /**
   * 每秒允许通过的请求数量
   */
  private final double permitsPerSecond;
  /**
   * 每两次请求之间的间隔（ms）
   */
  private final long interval;
  /**
   * 上次请求通过的时间
   */
  private final AtomicLong latestPassedTime = new AtomicLong(-1L);

  public RequestIntervalRateLimiter(double permitsPerSecond) {
    this.interval = Math.round(1000 / permitsPerSecond);
    this.permitsPerSecond = permitsPerSecond;
    attributeCheck();
  }

  public RequestIntervalRateLimiter(long interval) {
    this.interval = interval;
    this.permitsPerSecond = 1000d / interval;
    attributeCheck();
  }

  private void attributeCheck() {
    if (interval * permitsPerSecond != 1000) {
      log.warn("interval * permitsPerSecond != 1000, interval: {}, permitsSecond: {}. ", interval, permitsPerSecond);
    }
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
    boolean fg = false;
    long remainingWaitMills = waitMills(timeUnit, timeout);

    while (!fg) {
      long now = System.currentTimeMillis();
      long latest = latestPassedTime.get();
      // 需要等待多久才有获取请求的机会
      long expectedTime = latest + interval;
      if (expectedTime <= now) {
        fg = latestPassedTime.compareAndSet(latest, now);
      } else {
        long waitMills = expectedTime - now;
        if (remainingWaitMills < waitMills) {
          return false;
        } else {
          sleep(waitMills);
          remainingWaitMills = remainingWaitMills - waitMills;
        }
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return "RequestIntervalRateLimiter{" +
            "permitsPerSecond=" + permitsPerSecond +
            ", interval=" + interval +
            ", latestPassedTime=" + latestPassedTime +
            '}';
  }
}
