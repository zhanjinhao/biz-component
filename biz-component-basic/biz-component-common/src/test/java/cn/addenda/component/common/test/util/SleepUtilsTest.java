package cn.addenda.component.common.test.util;

import cn.addenda.component.common.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author addenda
 * @since 2023/3/9 19:45
 */
@Slf4j
class SleepUtilsTest {

  @Test
  void test1() {
    AtomicLong atomicLong = new AtomicLong(0);
    Thread thread = new Thread(() -> {
      long start = System.currentTimeMillis();
      SleepUtils.sleep(TimeUnit.SECONDS, 1, false);
      long end = System.currentTimeMillis();
      if (Thread.currentThread().isInterrupted()) {
        atomicLong.set(end - start);
      }
    });
    thread.start();

    while (thread.isAlive()) {
      SleepUtils.sleep(TimeUnit.MILLISECONDS, 300, false);
      thread.interrupt();
    }

    Assertions.assertTrue(atomicLong.get() >= 900);
  }

  @Test
  void test2() {
    AtomicLong atomicLong = new AtomicLong(0);
    Thread thread = new Thread(() -> {
      long start = System.currentTimeMillis();
      SleepUtils.sleep(Duration.ofSeconds(1), false);
      long end = System.currentTimeMillis();
      if (Thread.currentThread().isInterrupted()) {
        atomicLong.set(end - start);
      }
    });
    thread.start();

    while (thread.isAlive()) {
      SleepUtils.sleep(Duration.ofMillis(300), false);
      thread.interrupt();
    }

    Assertions.assertTrue(atomicLong.get() >= 900);
  }

  @Test
  void test3() {
    long start = System.currentTimeMillis();
    SleepUtils.sleep(TimeUnit.MILLISECONDS, 200, false);
    long elapsed = System.currentTimeMillis() - start;
    Assertions.assertTrue(elapsed >= 180, "elapsed=" + elapsed);
  }

  @Test
  void test4() {
    long start = System.currentTimeMillis();
    SleepUtils.sleep(Duration.ofMillis(200));
    long elapsed = System.currentTimeMillis() - start;
    Assertions.assertTrue(elapsed >= 180, "elapsed=" + elapsed);
  }

  @Test
  void testEatInterruptedTrue() throws InterruptedException {
    AtomicLong elapsed = new AtomicLong(0);
    AtomicBoolean interruptedAfter = new AtomicBoolean(false);
    Thread thread = new Thread(() -> {
      long start = System.currentTimeMillis();
      SleepUtils.sleep(TimeUnit.MILLISECONDS, 500, true);
      elapsed.set(System.currentTimeMillis() - start);
      interruptedAfter.set(Thread.interrupted());
    });
    thread.start();
    Thread.sleep(80);
    thread.interrupt();
    thread.join(2000);
    Assertions.assertTrue(elapsed.get() >= 450, "elapsed=" + elapsed.get());
    Assertions.assertFalse(interruptedAfter.get(), "sleep(..., true) 应吃掉中断标记");
  }

  @Test
  void testEatInterruptedFalse() throws InterruptedException {
    AtomicBoolean interruptedAfter = new AtomicBoolean(false);
    Thread thread = new Thread(() -> {
      SleepUtils.sleep(TimeUnit.MILLISECONDS, 500, false);
      interruptedAfter.set(Thread.interrupted());
    });
    thread.start();
    Thread.sleep(80);
    thread.interrupt();
    thread.join(2000);
    Assertions.assertTrue(interruptedAfter.get(), "sleep(..., false) 应恢复中断标记");
  }

  @Test
  void testNullDuration() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> SleepUtils.sleep((Duration) null));
  }

  @Test
  void testNullDurationWithFlag() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> SleepUtils.sleep((Duration) null, false));
  }

  @Test
  void testNullTimeUnit() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> SleepUtils.sleep(null, 100));
  }

  @Test
  void testNullTimeUnitWithFlag() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> SleepUtils.sleep(null, 100, false));
  }

  @Test
  void testZeroTimeout() {
    long start = System.currentTimeMillis();
    SleepUtils.sleep(TimeUnit.MILLISECONDS, 0, false);
    Assertions.assertTrue(System.currentTimeMillis() - start < 50, "0 超时应立即返回");
  }

  // ==================== trySleep ====================

  @Test
  void testTrySleepBasic() {
    long start = System.currentTimeMillis();
    SleepUtils.trySleep(TimeUnit.MILLISECONDS, 200);
    Assertions.assertTrue(System.currentTimeMillis() - start >= 180);
  }

  @Test
  void testTrySleepDuration() {
    long start = System.currentTimeMillis();
    SleepUtils.trySleep(Duration.ofMillis(200));
    Assertions.assertTrue(System.currentTimeMillis() - start >= 180);
  }

  @Test
  void testTrySleepInterrupted_EatTrue() throws InterruptedException {
    AtomicLong elapsed = new AtomicLong(0);
    AtomicBoolean interruptedAfter = new AtomicBoolean(true);
    Thread thread = new Thread(() -> {
      long start = System.currentTimeMillis();
      SleepUtils.trySleep(TimeUnit.MILLISECONDS, 1000, true);
      elapsed.set(System.currentTimeMillis() - start);
      interruptedAfter.set(Thread.interrupted());
    });
    thread.start();
    Thread.sleep(50);
    thread.interrupt();
    thread.join(2000);
    Assertions.assertTrue(elapsed.get() < 300, "被打断应提前退出，elapsed=" + elapsed.get());
    Assertions.assertFalse(interruptedAfter.get(), "trySleep(..., true) 应吃掉中断标记");
  }

  @Test
  void testTrySleepInterrupted_EatFalse() throws InterruptedException {
    AtomicLong elapsed = new AtomicLong(0);
    AtomicBoolean interruptedAfter = new AtomicBoolean(false);
    Thread thread = new Thread(() -> {
      long start = System.currentTimeMillis();
      SleepUtils.trySleep(TimeUnit.MILLISECONDS, 1000, false);
      elapsed.set(System.currentTimeMillis() - start);
      interruptedAfter.set(Thread.interrupted());
    });
    thread.start();
    Thread.sleep(50);
    thread.interrupt();
    thread.join(2000);
    Assertions.assertTrue(elapsed.get() < 300, "被打断应提前退出，elapsed=" + elapsed.get());
    Assertions.assertTrue(interruptedAfter.get(), "trySleep(..., false) 应恢复中断标记");
  }

  @Test
  void testTrySleepNullDuration() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> SleepUtils.trySleep((Duration) null));
  }

  @Test
  void testTrySleepNullDurationWithFlag() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> SleepUtils.trySleep((Duration) null, false));
  }

  @Test
  void testTrySleepNullTimeUnit() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> SleepUtils.trySleep(null, 100));
  }

  @Test
  void testTrySleepNullTimeUnitWithFlag() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> SleepUtils.trySleep(null, 100, false));
  }

}
