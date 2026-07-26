package cn.addenda.component.common.test.lambda.executeonce;

import cn.addenda.component.common.lambda.executeonce.ExecuteOnceRunnable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteOnceRunnableTest {

  @Test
  void testExecuteOnce() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(counter::incrementAndGet);
    r.run();
    r.run();
    r.run();
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testRun_Throws() {
    RuntimeException ex = new RuntimeException("test");
    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(() -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, r::run);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testThrows_NotExecuted_Retried() {
    AtomicInteger counter = new AtomicInteger(0);
    RuntimeException ex = new RuntimeException("test");
    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(() -> {
      counter.incrementAndGet();
      throw ex;
    });
    Assertions.assertThrows(RuntimeException.class, r::run);
    Assertions.assertThrows(RuntimeException.class, r::run);
    Assertions.assertThrows(RuntimeException.class, r::run);
    Assertions.assertEquals(3, counter.get());
  }

  @Test
  void testOf() {
    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(() -> {});
    Assertions.assertNotNull(r);
  }

  @Test
  void testToString() {
    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(() -> {});
    String s = r.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceRunnable"));
  }

  @Test
  void testToString_NotExecuted() {
    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(() -> {});
    Assertions.assertTrue(r.toString().contains("executed=false"));
  }

  @Test
  void testToString_Executed() {
    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(() -> {});
    r.run();
    Assertions.assertTrue(r.toString().contains("executed=true"));
  }

  @Test
  void testToString_ExecutedAfterThrow() {
    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(() -> { throw new RuntimeException("test"); });
    Assertions.assertThrows(RuntimeException.class, r::run);
    Assertions.assertTrue(r.toString().contains("executed=false"));
  }

  @Test
  void testConcurrency_OnlyExecutedOnce() throws Exception {
    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);
    AtomicInteger executionCount = new AtomicInteger(0);
    AtomicBoolean passed = new AtomicBoolean(false);

    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(() -> {
      executionCount.incrementAndGet();
      passed.set(true);
    });

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          r.run();
        } catch (InterruptedException ignored) {
        } finally {
          doneLatch.countDown();
        }
      });
    }

    startLatch.countDown();
    Assertions.assertTrue(doneLatch.await(5, TimeUnit.SECONDS));
    executor.shutdown();

    Assertions.assertEquals(1, executionCount.get());
    Assertions.assertTrue(passed.get());
  }

  @Test
  void testConcurrency_AllThreadsSeeState() throws Exception {
    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);
    AtomicInteger executionCount = new AtomicInteger(0);
    AtomicInteger threadsCompleted = new AtomicInteger(0);

    ExecuteOnceRunnable r = ExecuteOnceRunnable.of(executionCount::incrementAndGet);

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          r.run();
          threadsCompleted.incrementAndGet();
        } catch (InterruptedException ignored) {
        } finally {
          doneLatch.countDown();
        }
      });
    }

    startLatch.countDown();
    Assertions.assertTrue(doneLatch.await(5, TimeUnit.SECONDS));
    executor.shutdown();

    Assertions.assertEquals(1, executionCount.get());
    Assertions.assertEquals(threadCount, threadsCompleted.get());
  }
}
