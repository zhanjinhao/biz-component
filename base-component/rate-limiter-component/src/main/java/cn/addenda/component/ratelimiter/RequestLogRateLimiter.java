package cn.addenda.component.ratelimiter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

/**
 * 请求日志限流：限制总时间窗口内请求的总数量 & 同时记录所有请求的时间点
 *
 * @author addenda
 * @since 2022/12/28 14:15
 */
public class RequestLogRateLimiter implements RateLimiter {
  /**
   * 日志的记录器
   */
  private final Deque<Long> requestLogger = new ArrayDeque<>();
  /**
   * 计数阈值
   */
  private final long permits;
  /**
   * 日志的总时长（ms）
   */
  private final long duration;

  private final Object lock = new Object();

  public RequestLogRateLimiter(long permits, long duration) {
    this.permits = permits;
    this.duration = duration;
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
    long remainingWaitMills = waitMills(timeUnit, timeout);
    long waitMills;

    while (true) {
      long now = System.currentTimeMillis();
      synchronized (lock) {
        clearExpiredLog(now);
        // 如果请求的数量没到阈值，成功
        if (requestLogger.size() < permits) {
          requestLogger.addLast(now);
          return true;
        } else {
          Long first = requestLogger.getFirst();
          waitMills = duration + first - now;
          if (remainingWaitMills < waitMills) {
            return false;
          } else {
            remainingWaitMills = remainingWaitMills - waitMills;
          }
        }
      }
      sleep(waitMills);
    }
  }

  /**
   * 清理所有的过期节点
   */
  private void clearExpiredLog(long now) {
    Long first;
    while (!requestLogger.isEmpty() && (first = requestLogger.getFirst()) != null) {
      if (now - first > duration) {
        requestLogger.removeFirst();
      } else {
        break;
      }
    }
  }

  @Override
  public String toString() {
    return "RequestLogRateLimiter{" +
            "requestLogger=" + requestLogger +
            ", permits=" + permits +
            ", duration=" + duration +
            ", lock=" + lock +
            '}';
  }
}
