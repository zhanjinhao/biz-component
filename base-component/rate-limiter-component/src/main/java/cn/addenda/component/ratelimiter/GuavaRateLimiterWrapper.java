package cn.addenda.component.ratelimiter;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/9/8 20:31
 */
public class GuavaRateLimiterWrapper implements MultiPermitsRateLimiter {

  private final com.google.common.util.concurrent.RateLimiter rateLimiter;

  public GuavaRateLimiterWrapper(com.google.common.util.concurrent.RateLimiter rateLimiter) {
    this.rateLimiter = rateLimiter;
  }

  public GuavaRateLimiterWrapper(double permitsPerSecond) {
    this.rateLimiter = com.google.common.util.concurrent.RateLimiter.create(permitsPerSecond);
  }

  public GuavaRateLimiterWrapper(double permitsPerSecond, Duration warmupPeriod) {
    this.rateLimiter = com.google.common.util.concurrent.RateLimiter.create(permitsPerSecond, warmupPeriod);
  }

  public GuavaRateLimiterWrapper(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
    this.rateLimiter = com.google.common.util.concurrent.RateLimiter.create(permitsPerSecond, warmupPeriod, unit);
  }

  @Override
  public void acquire() {
    rateLimiter.acquire();
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
    return rateLimiter.tryAcquire();
  }

  @Override
  public boolean tryAcquire(TimeUnit timeUnit, long timeout) {
    return rateLimiter.tryAcquire(timeout, timeUnit);
  }

  @Override
  public void acquire(int permits) {
    rateLimiter.acquire(permits);
  }

  @Override
  public void acquire(int permits, TimeUnit timeUnit, long timeout) {
    long now = System.currentTimeMillis();
    if (!tryAcquire(permits, timeUnit, timeout)) {
      throw RateLimiterException.timeout(now, permits, timeUnit, timeout);
    }
  }

  @Override
  public boolean tryAcquire(int permits) {
    return rateLimiter.tryAcquire(permits);
  }

  @Override
  public boolean tryAcquire(int permits, TimeUnit timeUnit, long timeout) {
    return rateLimiter.tryAcquire(permits, timeout, timeUnit);
  }

}
