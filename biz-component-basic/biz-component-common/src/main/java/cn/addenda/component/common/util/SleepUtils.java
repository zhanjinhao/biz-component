package cn.addenda.component.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/3/9 19:42
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SleepUtils {

  public static void sleep(Duration duration) {
    AssertUtils.notNull(duration, "`duration` 不能为 null。");
    sleep(TimeUnit.MILLISECONDS, duration.toMillis(), false);
  }

  public static void sleep(Duration duration, boolean eatInterruptedFg) {
    AssertUtils.notNull(duration, "`duration` 不能为 null。");
    sleep(TimeUnit.MILLISECONDS, duration.toMillis(), eatInterruptedFg);
  }

  public static void sleep(TimeUnit timeUnit, long timeout) {
    AssertUtils.notNull(timeUnit, "`timeUnit` 不能为 null。");
    sleep(timeUnit, timeout, false);
  }

  /**
   * 尝试睡眠，被中断则立即退出，不补足剩余时长。
   * <p>
   * 与 {@link #sleep(TimeUnit, long, boolean)} 的区别在于：{@code trySleep} 只调用一次
   * {@link Thread#sleep(long)}，被中断后直接退出，不循环补足；{@code sleep} 会循环补足直到
   * 总睡眠时长至少达到 {@code timeout}。
   * <p>
   * 通过 {@code eatInterruptedFg} 控制退出后是否恢复中断标记：
   * <ul>
   *   <li>{@code false} — 调用 {@link Thread#interrupt()} 恢复标记，上游可感知中断</li>
   *   <li>{@code true} — 吃掉中断，上游无感知</li>
   * </ul>
   *
   * @param timeUnit         时间单位
   * @param timeout          期望睡眠时长
   * @param eatInterruptedFg 是否吃掉中断标记
   */
  public static void trySleep(TimeUnit timeUnit, long timeout, boolean eatInterruptedFg) {
    AssertUtils.notNull(timeUnit, "`timeUnit` 不能为 null。");
    try {
      Thread.sleep(timeUnit.toMillis(timeout));
    } catch (InterruptedException e) {
      if (log.isDebugEnabled()) {
        log.debug("trySleep 被打断，期望睡眠 [{}ms]。", timeUnit.toMillis(timeout));
      }
      if (!eatInterruptedFg) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public static void trySleep(TimeUnit timeUnit, long timeout) {
    trySleep(timeUnit, timeout, false);
  }

  public static void trySleep(Duration duration, boolean eatInterruptedFg) {
    AssertUtils.notNull(duration, "`duration` 不能为 null。");
    trySleep(TimeUnit.MILLISECONDS, duration.toMillis(), eatInterruptedFg);
  }

  public static void trySleep(Duration duration) {
    AssertUtils.notNull(duration, "`duration` 不能为 null。");
    trySleep(TimeUnit.MILLISECONDS, duration.toMillis(), false);
  }

  /**
   * 保证至少睡满 {@code timeout} 时长。
   * <p>
   * 若睡眠期间被中断（{@link InterruptedException}），不会提前退出，
   * 而是继续补足剩余时间。退出时根据 {@code eatInterruptedFg} 决定是否恢复中断标记：
   * <ul>
   *   <li>{@code false} — 调用 {@link Thread#interrupt()} 恢复标记，上游可感知中断</li>
   *   <li>{@code true} — 吃掉中断，上游无感知</li>
   * </ul>
   *
   * @param timeUnit         时间单位
   * @param timeout          期望睡眠时长。{@code 0} 直接返回，不做任何事。
   * @param eatInterruptedFg 是否吃掉中断标记
   */
  public static void sleep(TimeUnit timeUnit, long timeout, boolean eatInterruptedFg) {
    AssertUtils.notNull(timeUnit, "`timeUnit` 不能为 null。");
    long start = System.currentTimeMillis();
    long timeoutMillis = timeUnit.toMillis(timeout);
    if (timeoutMillis == 0) {
      return;
    }
    long duration = 0;
    boolean interruptedFg = false;
    // 循环睡眠，确保至少睡满 timeoutMillis，中断不会减少总睡眠时长
    while (true) {
      long delay = timeoutMillis - duration;
      if (delay <= 0) {
        break;
      }
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        // 记录当前已睡时长（通过反推得出本次实际睡了多久）
        if (log.isDebugEnabled()) {
          log.debug("睡眠期间被打断，本次睡眠 [{}ms]，总睡眠 [{}ms]，期望睡眠 [{}ms]。",
                  delay - (timeoutMillis - (System.currentTimeMillis() - start)), System.currentTimeMillis() - start, timeoutMillis);
        }
        interruptedFg = true;
      }
      // 重新计算已过时长，若不足则继续补足
      duration = System.currentTimeMillis() - start;
    }
    // 如果发生过中断且不用吃掉标记，恢复中断标记让上游感知
    if (interruptedFg && !eatInterruptedFg) {
      Thread.currentThread().interrupt();
    }
  }

}
