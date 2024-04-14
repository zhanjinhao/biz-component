package cn.addenda.component.test.ratelimiter.tryacquire;

import cn.addenda.component.ratelimiter.RequestIntervalRateLimiter;
import org.junit.Test;

/**
 * @author addenda
 * @since 2022/12/28 17:13
 */
public class RequestIntervalRateLimiterTest {

  @Test
  public void test1() throws Exception {
    RequestIntervalRateLimiter requestIntervalRateLimiter = new RequestIntervalRateLimiter(2d);
    new RateLimiterBaseTest(requestIntervalRateLimiter).test(true);
  }

}
