package cn.addenda.component.jdk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.concurrent.*;
import java.util.function.BiConsumer;

/**
 * CompletableFuture 扩展工具
 *
 * @author zhangtianci7, addenda
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompletableFutureUtils {

  /**
   * 如果在给定超时之前未完成，则异常完成此 CompletableFuture 并抛出 {@link TimeoutException} 。
   *
   * @param timeout 在出现 TimeoutException 异常完成之前等待多长时间，以 {@code unit} 为单位
   * @param unit    一个 {@link TimeUnit}，结合 {@code timeout} 参数，表示给定粒度单位的持续时间
   * @return 与入参CompletableFuture具有相同返回类型的future
   */
  public static <T> CompletableFuture<T> orTimeout(CompletableFuture<T> future, long timeout, TimeUnit unit) {
    Assert.notNull(future, "`future` can not be null!");
    Assert.notNull(future, "`unit` can not be null!");
    Assert.isTrue(timeout > -1, "`timeout` should be greater than -1!");
    if (future.isDone()) {
      return future;
    }

    return future.whenComplete(new Canceller(Delayer.delay(new Timeout(future), timeout, unit)));
  }


  /**
   * 如果在给定超时之前未完成，则使用默认值完成此 CompletableFuture。
   *
   * @param value   默认值
   * @param timeout 在出现 TimeoutException 异常完成之前等待多长时间，以 {@code unit} 为单位
   * @param unit    一个 {@link TimeUnit}，结合 {@code timeout} 参数，表示给定粒度单位的持续时间
   * @return 与入参CompletableFuture具有相同返回类型的future
   */
  public static <T> CompletableFuture<T> completeOnTimeout(CompletableFuture<T> future, T value, long timeout, TimeUnit unit) {
    Assert.notNull(future, "`future` can not be null!");
    Assert.notNull(future, "`unit` can not be null!");
    Assert.isTrue(timeout > -1, "`timeout` should be greater than -1!");
    if (future.isDone()) {
      return future;
    }
    return future.whenComplete(new Canceller(Delayer.delay(new DelayedCompleter<T>(future, value), timeout, unit)));
  }


  /**
   * 超时时异常完成的操作
   */
  static final class Timeout implements Runnable {
    final CompletableFuture<?> future;

    Timeout(CompletableFuture<?> future) {
      this.future = future;
    }

    public void run() {
      if (null != future && !future.isDone()) {
        future.completeExceptionally(new TimeoutException());
      }
    }
  }

  static final class DelayedCompleter<U> implements Runnable {
    final CompletableFuture<U> f;
    final U u;

    DelayedCompleter(CompletableFuture<U> f, U u) {
      this.f = f;
      this.u = u;
    }

    public void run() {
      if (f != null)
        f.complete(u);
    }
  }

  /**
   * 取消不需要的超时的操作
   */
  static final class Canceller implements BiConsumer<Object, Throwable> {
    final Future<?> future;

    Canceller(Future<?> future) {
      this.future = future;
    }

    public void accept(Object ignore, Throwable ex) {
      if (null == ex && null != future && !future.isDone()) {
        future.cancel(false);
      }
    }
  }

  /**
   * 单例延迟调度器，仅用于启动和取消任务，一个线程就足够
   */
  private static final class Delayer {
    static ScheduledFuture<?> delay(Runnable command, long delay, TimeUnit unit) {
      return delayer.schedule(command, delay, unit);
    }

    static final class DaemonThreadFactory implements ThreadFactory {
      public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("CompletableFutureUtils-Delayer");
        return t;
      }
    }

    static final ScheduledThreadPoolExecutor delayer;

    static {
      delayer = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
      delayer.setRemoveOnCancelPolicy(true);
    }
  }

}
