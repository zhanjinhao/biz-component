package cn.addenda.component.test.ratelimiter.timeout;

import cn.addenda.component.ratelimiter.LeakyBucketRateLimiter;
import org.junit.Test;

/**
 * @author addenda
 * @since 2022/12/28 17:13
 */
public class LeakyBucketRateLimiterTimeoutTest {

  @Test
  public void test1() throws Exception {
    LeakyBucketRateLimiter leakyBucketRateLimiter = new LeakyBucketRateLimiter(1000L, 10);
    new RateLimiterTimeoutBaseTest(leakyBucketRateLimiter).test(true);
  }

}
