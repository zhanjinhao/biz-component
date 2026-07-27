package cn.addenda.component.common.test.lambda.executeonce;

import cn.addenda.component.common.lambda.executeonce.ExecuteOnceBiFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteOnceBiFunctionTest {

  @Test
  void testSameKeyPair_ExecutedOnce() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiFunction<String, String, Integer> f = ExecuteOnceBiFunction.of((a, b) -> counter.incrementAndGet());
    Assertions.assertEquals(1, f.apply("a", "b"));
    Assertions.assertEquals(1, f.apply("a", "b"));
    Assertions.assertEquals(1, f.apply("a", "b"));
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testDifferentKeyPairs_ExecutedEach() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiFunction<String, String, Integer> f = ExecuteOnceBiFunction.of((a, b) -> counter.incrementAndGet());
    Assertions.assertEquals(1, f.apply("a", "b"));
    Assertions.assertEquals(2, f.apply("a", "c"));
    Assertions.assertEquals(3, f.apply("d", "e"));
  }

  @Test
  void testSameT_DifferentU_ExecutedEach() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiFunction<String, String, Integer> f = ExecuteOnceBiFunction.of((a, b) -> counter.incrementAndGet());
    Assertions.assertEquals(1, f.apply("a", "b"));
    Assertions.assertEquals(2, f.apply("a", "c"));
    Assertions.assertEquals(1, f.apply("a", "b"));
  }

  @Test
  void testNullResult_IsCached() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiFunction<String, String, Object> f = ExecuteOnceBiFunction.of((a, b) -> {
      counter.incrementAndGet();
      return null;
    });
    Assertions.assertNull(f.apply("a", "b"));
    Assertions.assertNull(f.apply("a", "b"));
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testNullResult_CacheNullFalse_NotCached() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiFunction<String, String, Object> f = ExecuteOnceBiFunction.of((a, b) -> {
      counter.incrementAndGet();
      return null;
    }, false);
    Assertions.assertNull(f.apply("a", "b"));
    Assertions.assertNull(f.apply("a", "b"));
    Assertions.assertEquals(2, counter.get());
  }

  @Test
  void testNullKey() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiFunction<String, String, Integer> f = ExecuteOnceBiFunction.of((a, b) -> counter.incrementAndGet());
    Assertions.assertEquals(1, f.apply(null, null));
    Assertions.assertEquals(1, f.apply(null, null));
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testThrows_NotCached_Retried() {
    AtomicInteger counter = new AtomicInteger(0);
    RuntimeException ex = new RuntimeException("test");
    ExecuteOnceBiFunction<String, String, String> f = ExecuteOnceBiFunction.of((a, b) -> {
      counter.incrementAndGet();
      throw ex;
    });
    Assertions.assertThrows(RuntimeException.class, () -> f.apply("x", "y"));
    Assertions.assertThrows(RuntimeException.class, () -> f.apply("x", "y"));
    Assertions.assertEquals(2, counter.get());
  }

  @Test
  void testOf() {
    ExecuteOnceBiFunction<String, String, String> f = ExecuteOnceBiFunction.of((a, b) -> a + b);
    Assertions.assertNotNull(f);
  }

  @Test
  void testOf_CacheNull() {
    ExecuteOnceBiFunction<String, String, String> f = ExecuteOnceBiFunction.of((a, b) -> a + b, false);
    Assertions.assertNotNull(f);
  }

  @Test
  void testOf_CacheNullAndJackson() {
    ExecuteOnceBiFunction<String, String, String> f = ExecuteOnceBiFunction.of((a, b) -> a + b, true, true);
    Assertions.assertNotNull(f);
  }

  @Test
  void testToString() {
    ExecuteOnceBiFunction<String, String, String> f = ExecuteOnceBiFunction.of((a, b) -> a + b);
    String s = f.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceBiFunction{biFunction=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceBiFunctionTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", cacheNull=true, executedCount=0}"));
  }

  @Test
  void testToString_AfterExecute() {
    ExecuteOnceBiFunction<String, String, Integer> f = ExecuteOnceBiFunction.of((a, b) -> a.length() + b.length());
    f.apply("hello", "world");
    f.apply("a", "b");
    String s = f.toString();
    Assertions.assertTrue(s.contains(", executedCount=2}"));
  }

  @Test
  void testToString_KeyPair_NotComputed() {
    ExecuteOnceBiFunction<String, String, Integer> f = ExecuteOnceBiFunction.of((a, b) -> a.length() + b.length());
    String s = f.toString("hello", "world");
    Assertions.assertTrue(s.contains("biFunction=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceBiFunctionTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", contained=false}"));
  }

  @Test
  void testToString_KeyPair_Computed() {
    ExecuteOnceBiFunction<String, String, Integer> f = ExecuteOnceBiFunction.of((a, b) -> a.length() + b.length());
    f.apply("hello", "world");
    String s = f.toString("hello", "world");
    Assertions.assertTrue(s.contains("biFunction=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceBiFunctionTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", contained=true, keyT=hello, keyU=world, result=10}"));
  }

  @Test
  void testToString_KeyPair_JacksonTrue() {
    ExecuteOnceBiFunction<String, String, String> f = ExecuteOnceBiFunction.of((a, b) -> a + b, true, true);
    f.apply("hello", "world");
    String s = f.toString("hello", "world");
    Assertions.assertTrue(s.contains(", keyT=\"hello\", keyU=\"world\", result=\"helloworld\"}"));
  }

  @Test
  void testConcurrency_SameKeyPair_ExecutedOnce() throws Exception {
    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);
    AtomicInteger executionCount = new AtomicInteger(0);

    ExecuteOnceBiFunction<String, String, Integer> f = ExecuteOnceBiFunction.of((a, b) -> executionCount.incrementAndGet());

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          f.apply("sameA", "sameB");
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

    ExecuteOnceBiFunction<String, String, Integer> f = ExecuteOnceBiFunction.of((a, b) -> executionCount.incrementAndGet());

    for (int k = 0; k < keys; k++) {
      String key1 = "a" + k;
      String key2 = "b" + k;
      for (int t = 0; t < threadsPerKey; t++) {
        executor.submit(() -> {
          try {
            startLatch.await();
            f.apply(key1, key2);
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
