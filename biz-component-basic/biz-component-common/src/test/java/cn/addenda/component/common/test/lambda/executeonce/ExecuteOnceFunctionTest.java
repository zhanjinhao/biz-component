package cn.addenda.component.common.test.lambda.executeonce;

import cn.addenda.component.common.lambda.executeonce.ExecuteOnceFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteOnceFunctionTest {

  @Test
  void testApply_SameKey_ExecutedOnce() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceFunction<String, Integer> f = ExecuteOnceFunction.of(s -> counter.incrementAndGet());
    Assertions.assertEquals(1, f.apply("a"));
    Assertions.assertEquals(1, f.apply("a"));
    Assertions.assertEquals(1, f.apply("a"));
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testApply_DifferentKeys_ExecutedEach() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceFunction<String, Integer> f = ExecuteOnceFunction.of(s -> counter.incrementAndGet());
    Assertions.assertEquals(1, f.apply("a"));
    Assertions.assertEquals(2, f.apply("b"));
    Assertions.assertEquals(3, f.apply("c"));
  }

  @Test
  void testApply_NullResult_IsCached() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceFunction<String, Object> f = ExecuteOnceFunction.of(s -> {
      counter.incrementAndGet();
      return null;
    });
    Assertions.assertNull(f.apply("a"));
    Assertions.assertNull(f.apply("a"));
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testApply_NullResult_CacheNullFalse_NotCached() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceFunction<String, Object> f = ExecuteOnceFunction.of(s -> {
      counter.incrementAndGet();
      return null;
    }, false);
    Assertions.assertNull(f.apply("a"));
    Assertions.assertNull(f.apply("a"));
    Assertions.assertEquals(2, counter.get());
  }

  @Test
  void testApply_NullKey() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceFunction<String, Integer> f = ExecuteOnceFunction.of(s -> counter.incrementAndGet());
    Assertions.assertEquals(1, f.apply(null));
    Assertions.assertEquals(1, f.apply(null));
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testApply_Throws() {
    RuntimeException ex = new RuntimeException("test");
    ExecuteOnceFunction<String, String> f = ExecuteOnceFunction.of(s -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> f.apply("x"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testApply_Throws_ExceptionNotCached_Retried() {
    AtomicInteger counter = new AtomicInteger(0);
    RuntimeException ex = new RuntimeException("test");
    ExecuteOnceFunction<String, String> f = ExecuteOnceFunction.of(s -> {
      counter.incrementAndGet();
      throw ex;
    });
    Assertions.assertThrows(RuntimeException.class, () -> f.apply("x"));
    Assertions.assertThrows(RuntimeException.class, () -> f.apply("x"));
    Assertions.assertEquals(2, counter.get());
  }

  @Test
  void testOf() {
    ExecuteOnceFunction<String, String> f = ExecuteOnceFunction.of(s -> s + "!");
    Assertions.assertNotNull(f);
  }

  @Test
  void testOf_CacheNull() {
    ExecuteOnceFunction<String, String> f = ExecuteOnceFunction.of(s -> s, false);
    Assertions.assertNotNull(f);
  }

  @Test
  void testOf_CacheNullAndJackson() {
    ExecuteOnceFunction<String, String> f = ExecuteOnceFunction.of(s -> s, true, true);
    Assertions.assertNotNull(f);
  }

  @Test
  void testToString() {
    ExecuteOnceFunction<String, String> f = ExecuteOnceFunction.of(s -> s);
    String s = f.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceFunction{function=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceFunctionTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", cacheNull=true}"));
  }

  @Test
  void testToString_Key_NotComputed() {
    ExecuteOnceFunction<String, Integer> f = ExecuteOnceFunction.of(s -> s.length());
    String s = f.toString("hello");
    Assertions.assertTrue(s.contains("ExecuteOnceFunction{function=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceFunctionTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", key=hello, contained=false}"));
  }

  @Test
  void testToString_Key_Computed() {
    ExecuteOnceFunction<String, Integer> f = ExecuteOnceFunction.of(String::length);
    f.apply("hello");
    String s = f.toString("hello");
    Assertions.assertTrue(s.contains("ExecuteOnceFunction{function=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceFunctionTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", key=hello, contained=true, result=5}"));
  }

  @Test
  void testToString_Key_NullKey() {
    ExecuteOnceFunction<String, Integer> f = ExecuteOnceFunction.of(s -> 1);
    f.apply(null);
    String s = f.toString(null);
    Assertions.assertTrue(s.contains("ExecuteOnceFunction{function=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceFunctionTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", key=null, contained=true, result=1}"));
  }

  @Test
  void testToString_Key_JacksonTrue() {
    ExecuteOnceFunction<String, String> f = ExecuteOnceFunction.of(s -> s, true, true);
    f.apply("hello");
    String s = f.toString("hello");
    Assertions.assertTrue(s.contains("ExecuteOnceFunction{function=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceFunctionTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", key=\"hello\", contained=true, result=\"hello\"}"));
  }

  @Test
  void testToString_Key_JacksonFalse() {
    ExecuteOnceFunction<String, String> f = ExecuteOnceFunction.of(s -> s, true, false);
    f.apply("hello");
    String s = f.toString("hello");
    Assertions.assertTrue(s.contains("ExecuteOnceFunction{function=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceFunctionTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", key=hello, contained=true, result=hello}"));
  }

  @Test
  void testConcurrency_SameKey_ExecutedOnce() throws Exception {
    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);
    AtomicInteger executionCount = new AtomicInteger(0);

    ExecuteOnceFunction<String, Integer> f = ExecuteOnceFunction.of(s -> executionCount.incrementAndGet());

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          f.apply("sameKey");
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
  void testConcurrency_MultipleKeys_EachExecutedOnce() throws Exception {
    int keys = 10;
    int threadsPerKey = 10;
    ExecutorService executor = Executors.newFixedThreadPool(keys * threadsPerKey);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(keys * threadsPerKey);
    AtomicInteger executionCount = new AtomicInteger(0);

    ExecuteOnceFunction<String, Integer> f = ExecuteOnceFunction.of(s -> executionCount.incrementAndGet());

    for (int k = 0; k < keys; k++) {
      String key = "key" + k;
      for (int t = 0; t < threadsPerKey; t++) {
        executor.submit(() -> {
          try {
            startLatch.await();
            f.apply(key);
          } catch (InterruptedException ignored) {
          } finally {
            doneLatch.countDown();
          }
        });
      }
    }

    startLatch.countDown();
    Assertions.assertTrue(doneLatch.await(5, TimeUnit.SECONDS));
    executor.shutdown();

    Assertions.assertEquals(keys, executionCount.get());
  }
}
