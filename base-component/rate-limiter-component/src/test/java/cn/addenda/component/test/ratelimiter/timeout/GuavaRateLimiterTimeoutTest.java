package cn.addenda.component.test.ratelimiter.timeout;

import cn.addenda.component.ratelimiter.TokenBucketRateLimiter;

/**
 * @author addenda
 * @since 2022/12/29 19:14
 */
public class GuavaRateLimiterTimeoutTest {

  public static void main(String[] args) throws Exception {
    TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(10, 10);
    new RateLimiterTimeoutBaseTest(tokenBucketRateLimiter).test(true);
  }

}
