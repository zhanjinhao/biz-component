package cn.addenda.component.test.ratelimiter.timeout;

import cn.addenda.component.ratelimiter.SlidingWindowRateLimiter;

/**
 * @author addenda
 * @since 2022/12/28 14:14
 */
public class SlidingWindowRateLimiterTimeoutTest {

  public static void main(String[] args) throws Exception {
    SlidingWindowRateLimiter slidingWindowRateLimiter = new SlidingWindowRateLimiter(10, 1000, 100, false);
    new RateLimiterTimeoutBaseTest(slidingWindowRateLimiter).test(true);
  }

}
