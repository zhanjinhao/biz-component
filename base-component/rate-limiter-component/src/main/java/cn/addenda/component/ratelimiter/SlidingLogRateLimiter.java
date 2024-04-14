package cn.addenda.component.ratelimiter;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 滑动日志限流：
 * <p>
 * 统计每毫秒请求的数量并以此限制总时间窗口内请求的总数量，
 * <p>
 * 当前时间距离本毫秒开始时间不足一毫秒按一毫秒计算。
 *
 * @author addenda
 * @since 2022/12/28 14:02
 */
@Slf4j
public class SlidingLogRateLimiter implements RateLimiter {

    private final SlidingWindowRateLimiter slidingWindowRateLimiter;

    /**
     * 总的计数阈值
     */
    private final long permits;
    /**
     * 日志的总时长（ms）
     */
    private final long duration;

    public SlidingLogRateLimiter(long permits, long duration) {
        this.permits = permits;
        this.duration = duration;
        slidingWindowRateLimiter = new SlidingWindowRateLimiter(permits, duration, 1L, true);
    }

    @Override
    public void acquire() {
        slidingWindowRateLimiter.acquire();
    }

    @Override
    public void acquire(TimeUnit timeUnit, long timeout) {
        slidingWindowRateLimiter.acquire(timeUnit, timeout);
    }

    @Override
    public boolean tryAcquire() {
        return slidingWindowRateLimiter.tryAcquire();
    }

    @Override
    public boolean tryAcquire(TimeUnit timeUnit, long timeout) {
        return slidingWindowRateLimiter.tryAcquire(timeUnit, timeout);
    }

    @Override
    public String toString() {
        return "SlidingLogRateLimiter{" +
            "slidingWindowRateLimiter=" + slidingWindowRateLimiter +
            ", permits=" + permits +
            ", duration=" + duration +
            '}';
    }
}
