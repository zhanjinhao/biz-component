package cn.addenda.component.ratelimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/9/8 20:35
 */
public interface MultiPermitsRateLimiter extends RateLimiter {

  /**
   * 获取permits个许可，在获取到许可之前线程阻塞。
   */
  void acquire(int permits);

  /**
   * 在指定时间之内获取permits个许可。
   * <ul><li>获取到：线程继续运行，</li>
   * <li>获取不到：抛出{@link RateLimiterException}</li></ul>
   * 在获取不到许可的情况下，线程不会等待指定的时间才抛出异常，而是判断在当前等待时间内获取不到许可时立即抛出异常。
   *
   * @param timeUnit 单位
   * @param timeout  <=0时表示不等待
   */
  void acquire(int permits, TimeUnit timeUnit, long timeout);

  /**
   * 尝试获取permits个许可，获取到返回true，获取不到返回false。线程永远不会等待。
   */
  boolean tryAcquire(int permits);

  /**
   * 在指定时间内尝试获取permits个许可，获取到返回true，获取不到返回false。<br/>
   * 在获取不到许可的情况下，线程不会等待指定的时间才抛出异常，而是判断在当前等待时间内获取不到许可时立即返回false。
   *
   * @param timeUnit 单位
   * @param timeout  <=0时表示不等待
   */
  boolean tryAcquire(int permits, TimeUnit timeUnit, long timeout);

}
