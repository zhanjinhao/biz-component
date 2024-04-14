package cn.addenda.component.test.ratelimiter.tryacquire;

import cn.addenda.component.ratelimiter.TokenBucketRateLimiter;

/**
 * @author addenda
 * @since 2022/12/29 19:14
 */
public class TokenBucketRateLimiterTest {

  public static void main(String[] args) throws Exception {
    TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(10, 10);
    new RateLimiterBaseTest(tokenBucketRateLimiter).test(true);
  }

}
