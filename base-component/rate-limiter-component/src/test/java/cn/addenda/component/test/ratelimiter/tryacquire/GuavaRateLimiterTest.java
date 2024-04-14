package cn.addenda.component.test.ratelimiter.tryacquire;

import cn.addenda.component.ratelimiter.GuavaRateLimiterWrapper;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2022/12/29 19:14
 */
public class GuavaRateLimiterTest {

  public static void main(String[] args) throws Exception {
    GuavaRateLimiterWrapper guavaRateLimiterWrapper = new GuavaRateLimiterWrapper(10, 1, TimeUnit.SECONDS);
    new RateLimiterBaseTest(guavaRateLimiterWrapper).test(true);
  }

  @Test
  public void test1() {
    RateLimiter r = RateLimiter.create(2, 3, TimeUnit.SECONDS);
    while (true) {
      System.out.println(String.format("Get 10 tokens spend %f s", r.acquire(6)));
      System.out.println(String.format("Get 10 tokens spend %f s", r.acquire(6)));
      System.out.println(String.format("Get 10 tokens spend %f s", r.acquire(6)));
      System.out.println(String.format("Get 10 tokens spend %f s", r.acquire(6)));
      System.out.println("end");
    }
  }

}
