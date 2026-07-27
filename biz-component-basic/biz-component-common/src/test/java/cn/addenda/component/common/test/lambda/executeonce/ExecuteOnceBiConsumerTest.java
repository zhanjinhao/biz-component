package cn.addenda.component.common.test.lambda.executeonce;

import cn.addenda.component.common.lambda.executeonce.ExecuteOnceBiConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteOnceBiConsumerTest {

  @Test
  void testSameKeyPair_ExecutedOnce() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> counter.incrementAndGet());
    c.accept("a", "b");
    c.accept("a", "b");
    c.accept("a", "b");
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testDifferentKeyPairs_ExecutedEach() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> counter.incrementAndGet());
    c.accept("a", "b");
    c.accept("a", "c");
    c.accept("d", "e");
    Assertions.assertEquals(3, counter.get());
  }

  @Test
  void testSameT_DifferentU_ExecutedEach() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> counter.incrementAndGet());
    c.accept("a", "b");
    c.accept("a", "c");
    Assertions.assertEquals(2, counter.get());
    c.accept("a", "b");
    Assertions.assertEquals(2, counter.get());
  }

  @Test
  void testNullKey() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> counter.incrementAndGet());
    c.accept(null, null);
    c.accept(null, null);
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testThrows_NotCached_Retried() {
    AtomicInteger counter = new AtomicInteger(0);
    RuntimeException ex = new RuntimeException("test");
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {
      counter.incrementAndGet();
      throw ex;
    });
    Assertions.assertThrows(RuntimeException.class, () -> c.accept("x", "y"));
    Assertions.assertThrows(RuntimeException.class, () -> c.accept("x", "y"));
    Assertions.assertEquals(2, counter.get());
  }

  @Test
  void testOf() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {});
    Assertions.assertNotNull(c);
  }

  @Test
  void testToString() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {});
    String s = c.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceBiConsumer{biConsumer=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceBiConsumerTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", executedCount=0}"));
  }

  @Test
  void testToString_AfterExecute() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {});
    c.accept("a", "b");
    c.accept("c", "d");
    String s = c.toString();
    Assertions.assertTrue(s.contains(", executedCount=2}"));
  }

  @Test
  void testToString_KeyPair_NotContained() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {});
    String s = c.toString("a", "b");
    Assertions.assertTrue(s.contains("ExecuteOnceBiConsumer{biConsumer=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceBiConsumerTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", keyT=a, keyU=b, contained=false}"));
  }

  @Test
  void testToString_KeyPair_Contained() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {});
    c.accept("hello", "world");
    String s = c.toString("hello", "world");
    Assertions.assertTrue(s.contains(", keyT=hello, keyU=world, contained=true}"));
  }

  @Test
  void testToString_KeyPair_NullKeys() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {});
    c.accept(null, null);
    String s = c.toString(null, null);
    Assertions.assertTrue(s.contains(", keyT=null, keyU=null, contained=true}"));
  }

  @Test
  void testToString_KeyPair_NullKeys_NotExecuted() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {});
    String s = c.toString(null, null);
    Assertions.assertTrue(s.contains(", keyT=null, keyU=null, contained=false}"));
  }

  @Test
  void testToString_KeyPair_JacksonTrue() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {}, true);
    c.accept("hello", "world");
    String s = c.toString("hello", "world");
    Assertions.assertTrue(s.contains(", keyT=\"hello\", keyU=\"world\", contained=true}"));
  }

  @Test
  void testToString_KeyPair_JacksonFalse() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {}, false);
    c.accept("hello", "world");
    String s = c.toString("hello", "world");
    Assertions.assertTrue(s.contains(", keyT=hello, keyU=world, contained=true}"));
  }

  @Test
  void testOf_JacksonToString() {
    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> {}, true);
    Assertions.assertNotNull(c);
  }

  @Test
  void testConcurrency_SameKeyPair_ExecutedOnce() throws Exception {
    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);
    AtomicInteger executionCount = new AtomicInteger(0);

    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> executionCount.incrementAndGet());

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          c.accept("sameA", "sameB");
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

    ExecuteOnceBiConsumer<String, String> c = ExecuteOnceBiConsumer.of((a, b) -> executionCount.incrementAndGet());

    for (int k = 0; k < keys; k++) {
      String key1 = "a" + k;
      String key2 = "b" + k;
      for (int t = 0; t < threadsPerKey; t++) {
        executor.submit(() -> {
          try {
            startLatch.await();
            c.accept(key1, key2);
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
