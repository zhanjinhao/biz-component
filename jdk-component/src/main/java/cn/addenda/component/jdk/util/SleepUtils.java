package cn.addenda.component.jdk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/3/9 19:42
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SleepUtils {

  public static void sleep(TimeUnit timeUnit, long timeout) {
    sleep(timeUnit, timeout, false);
  }

  public static void sleep(TimeUnit timeUnit, long timeout, boolean eatInterruptedFg) {
    long start = System.currentTimeMillis();
    long timeoutMillis = timeUnit.toMillis(timeout);
    if (timeoutMillis == 0) {
      return;
    }
    long duration = 0;
    boolean interruptedFg = false;
    while (true) {
      long delay = timeoutMillis - duration;
      if (delay <= 0) {
        break;
      }
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        log.debug("睡眠期间被打断，本次睡眠 [{}ms]，总睡眠 [{}ms]，期望睡眠 [{}ms]。",
                delay - (timeoutMillis - (System.currentTimeMillis() - start)), System.currentTimeMillis() - start, timeoutMillis);
        interruptedFg = true;
      }
      duration = System.currentTimeMillis() - start;
    }
    if (interruptedFg && !eatInterruptedFg) {
      Thread.currentThread().interrupt();
    }
  }

}
