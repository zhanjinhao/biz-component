package cn.addenda.component.common.test.lambda.executeonce;

import cn.addenda.component.common.lambda.executeonce.ExecuteOnceSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteOnceSupplierTest {

  @Test
  void testExecuteOnce() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceSupplier<Integer> supplier = ExecuteOnceSupplier.of(counter::incrementAndGet);
    Assertions.assertEquals(1, supplier.get());
    Assertions.assertEquals(1, supplier.get());
    Assertions.assertEquals(1, supplier.get());
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testExecuteOnce_NullResult() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceSupplier<Object> supplier = ExecuteOnceSupplier.of(() -> {
      counter.incrementAndGet();
      return null;
    });
    Assertions.assertNull(supplier.get());
    Assertions.assertNull(supplier.get());
    Assertions.assertNull(supplier.get());
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testOf() {
    ExecuteOnceSupplier<String> supplier = ExecuteOnceSupplier.of(() -> "hello");
    Assertions.assertNotNull(supplier);
    Assertions.assertEquals("hello", supplier.get());
  }

  @Test
  void testToString() {
    ExecuteOnceSupplier<String> supplier = ExecuteOnceSupplier.of(() -> "x");
    String str = supplier.toString();
    Assertions.assertTrue(str.contains("ExecuteOnceSupplier{supplier=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceSupplierTest$$Lambda$"));
    Assertions.assertTrue(str.contains(", executed=false}"));
  }

  @Test
  void testToString_NotExecuted() {
    ExecuteOnceSupplier<String> supplier = ExecuteOnceSupplier.of(() -> "x");
    String str = supplier.toString();
    Assertions.assertTrue(str.contains("ExecuteOnceSupplier{supplier=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceSupplierTest$$Lambda$"));
    Assertions.assertTrue(str.contains(", executed=false}"));
  }

  @Test
  void testToString_Executed() {
    ExecuteOnceSupplier<String> supplier = ExecuteOnceSupplier.of(() -> "hello");
    supplier.get();
    String str = supplier.toString();
    Assertions.assertTrue(str.contains("ExecuteOnceSupplier{supplier=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceSupplierTest$$Lambda$"));
    Assertions.assertTrue(str.contains(", executed=true, result=hello}"));
  }

  @Test
  void testToString_Executed_NullResult() {
    ExecuteOnceSupplier<Object> supplier = ExecuteOnceSupplier.of(() -> null);
    supplier.get();
    String str = supplier.toString();
    Assertions.assertTrue(str.contains("ExecuteOnceSupplier{supplier=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceSupplierTest$$Lambda$"));
    Assertions.assertTrue(str.contains(", executed=true, result=null}"));
  }

  @Test
  void testToString_JacksonTrue() {
    ExecuteOnceSupplier<String> supplier = ExecuteOnceSupplier.of(() -> "hello", true);
    supplier.get();
    String str = supplier.toString();
    Assertions.assertTrue(str.contains("ExecuteOnceSupplier{supplier=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceSupplierTest$$Lambda$"));
    Assertions.assertTrue(str.contains(", executed=true, result=\"hello\"}"));
  }

  @Test
  void testToString_JacksonFalse() {
    ExecuteOnceSupplier<String> supplier = ExecuteOnceSupplier.of(() -> "hello", false);
    supplier.get();
    String str = supplier.toString();
    Assertions.assertTrue(str.contains("ExecuteOnceSupplier{supplier=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceSupplierTest$$Lambda$"));
    Assertions.assertTrue(str.contains(", executed=true, result=hello}"));
  }

  @Test
  void testOf_JacksonToString() {
    ExecuteOnceSupplier<String> supplier = ExecuteOnceSupplier.of(() -> "x", true);
    supplier.get();
    String str = supplier.toString();
    Assertions.assertTrue(str.contains("ExecuteOnceSupplier{supplier=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceSupplierTest$$Lambda$"));
    Assertions.assertTrue(str.contains(", executed=true, result=\"x\"}"));
  }

  @Test
  void testConcurrency_OnlyExecutedOnce() throws Exception {
    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);
    AtomicInteger executionCount = new AtomicInteger(0);

    ExecuteOnceSupplier<Integer> supplier = ExecuteOnceSupplier.of(executionCount::incrementAndGet);

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          supplier.get();
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
  }

  @Test
  void testConcurrency_NullResult_NotRecomputed() throws Exception {
    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);
    AtomicInteger executionCount = new AtomicInteger(0);

    ExecuteOnceSupplier<Object> supplier = ExecuteOnceSupplier.of(() -> {
      executionCount.incrementAndGet();
      return null;
    });

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          Assertions.assertNull(supplier.get());
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
  }
}
