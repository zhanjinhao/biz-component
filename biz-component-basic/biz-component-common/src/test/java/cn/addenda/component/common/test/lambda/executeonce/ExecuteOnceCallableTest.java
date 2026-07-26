package cn.addenda.component.common.test.lambda.executeonce;

import cn.addenda.component.common.lambda.executeonce.ExecuteOnceCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteOnceCallableTest {

  @Test
  void testExecuteOnce() throws Exception {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceCallable<Integer> callable = ExecuteOnceCallable.of(counter::incrementAndGet);
    Assertions.assertEquals(1, callable.call());
    Assertions.assertEquals(1, callable.call());
    Assertions.assertEquals(1, callable.call());
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testExecuteOnce_NullResult() throws Exception {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceCallable<Object> callable = ExecuteOnceCallable.of(() -> {
      counter.incrementAndGet();
      return null;
    });
    Assertions.assertNull(callable.call());
    Assertions.assertNull(callable.call());
    Assertions.assertNull(callable.call());
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testCallable_Throws() {
    Exception ex = new Exception("test");
    ExecuteOnceCallable<Object> callable = ExecuteOnceCallable.of(() -> { throw ex; });
    Exception thrown = Assertions.assertThrows(Exception.class, callable::call);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testOf() throws Exception {
    ExecuteOnceCallable<String> callable = ExecuteOnceCallable.of(() -> "hello");
    Assertions.assertNotNull(callable);
    Assertions.assertEquals("hello", callable.call());
  }

  @Test
  void testToString() {
    ExecuteOnceCallable<String> callable = ExecuteOnceCallable.of(() -> "x");
    String str = callable.toString();
    Assertions.assertTrue(str.contains("ExecuteOnceCallable{callable=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceCallableTest$$Lambda"));
    Assertions.assertTrue(str.contains(", executed=false}"));
  }

  @Test
  void testToString_NotExecuted() {
    ExecuteOnceCallable<String> callable = ExecuteOnceCallable.of(() -> "x");
    String s = callable.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceCallable{callable=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceCallableTest$$Lambda"));
    Assertions.assertTrue(s.contains(", executed=false}"));
  }

  @Test
  void testToString_Executed() throws Exception {
    ExecuteOnceCallable<String> callable = ExecuteOnceCallable.of(() -> "hello");
    callable.call();
    String s = callable.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceCallable{callable=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceCallableTest$$Lambda"));
    Assertions.assertTrue(s.contains(", executed=true"));
    Assertions.assertTrue(s.contains(", result=hello}"));
  }

  @Test
  void testToString_Executed_NullResult() throws Exception {
    ExecuteOnceCallable<Object> callable = ExecuteOnceCallable.of(() -> null);
    callable.call();
    String s = callable.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceCallable{callable=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceCallableTest$$Lambda"));
    Assertions.assertTrue(s.contains(", executed=true"));
    Assertions.assertTrue(s.contains(", result=null}"));
  }

  @Test
  void testToString_JacksonTrue() throws Exception {
    ExecuteOnceCallable<String> callable = ExecuteOnceCallable.of(() -> "hello", true);
    callable.call();
    String s = callable.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceCallable{callable=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceCallableTest$$Lambda"));
    Assertions.assertTrue(s.contains(", executed=true"));
    Assertions.assertTrue(s.contains(", result=\"hello\"}"));
  }

  @Test
  void testToString_JacksonFalse() throws Exception {
    ExecuteOnceCallable<String> callable = ExecuteOnceCallable.of(() -> "hello", false);
    callable.call();
    String s = callable.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceCallable{callable=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceCallableTest$$Lambda"));
    Assertions.assertTrue(s.contains(", executed=true"));
    Assertions.assertTrue(s.contains(", result=hello}"));
  }

  @Test
  void testOf_JacksonToString() {
    ExecuteOnceCallable<String> callable = ExecuteOnceCallable.of(() -> "x", true);
    Assertions.assertNotNull(callable);
  }

  @Test
  void testConcurrency_OnlyExecutedOnce() throws Exception {
    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);
    AtomicInteger executionCount = new AtomicInteger(0);

    ExecuteOnceCallable<Integer> callable = ExecuteOnceCallable.of(executionCount::incrementAndGet);

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          callable.call();
        } catch (Exception ignored) {
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

    ExecuteOnceCallable<Object> callable = ExecuteOnceCallable.of(() -> {
      executionCount.incrementAndGet();
      return null;
    });

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          Assertions.assertNull(callable.call());
        } catch (Exception ignored) {
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
