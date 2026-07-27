package cn.addenda.component.common.test.lambda.executeonce;

import cn.addenda.component.common.lambda.executeonce.ExecuteOnceConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteOnceConsumerTest {

  @Test
  void testSameKey_ExecutedOnce() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> counter.incrementAndGet());
    c.accept("a");
    c.accept("a");
    c.accept("a");
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testDifferentKeys_ExecutedEach() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> counter.incrementAndGet());
    c.accept("a");
    c.accept("b");
    c.accept("c");
    Assertions.assertEquals(3, counter.get());
  }

  @Test
  void testNullKey() {
    AtomicInteger counter = new AtomicInteger(0);
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> counter.incrementAndGet());
    c.accept(null);
    c.accept(null);
    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testThrows_NotCached_Retried() {
    AtomicInteger counter = new AtomicInteger(0);
    RuntimeException ex = new RuntimeException("test");
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
      counter.incrementAndGet();
      throw ex;
    });
    Assertions.assertThrows(RuntimeException.class, () -> c.accept("x"));
    Assertions.assertThrows(RuntimeException.class, () -> c.accept("x"));
    Assertions.assertEquals(2, counter.get());
  }

  @Test
  void testOf() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    });
    Assertions.assertNotNull(c);
  }

  @Test
  void testToString() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    });
    String s = c.toString();
    Assertions.assertTrue(s.contains("ExecuteOnceConsumer{consumer=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceConsumerTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", executedCount=0}"));
  }

  @Test
  void testToString_AfterExecute() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    });
    c.accept("x");
    String s = c.toString();
    Assertions.assertTrue(s.contains(", executedCount=1}"));
  }

  @Test
  void testToString_Key_NotContained() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    });
    String s = c.toString("key");
    Assertions.assertTrue(s.contains("ExecuteOnceConsumer{consumer=cn.addenda.component.common.test.lambda.executeonce.ExecuteOnceConsumerTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", key=key, contained=false}"));
  }

  @Test
  void testToString_Key_Contained() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    });
    c.accept("hello");
    String s = c.toString("hello");
    Assertions.assertTrue(s.contains(", key=hello, contained=true}"));
  }

  @Test
  void testToString_Key_NullKey() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    });
    c.accept(null);
    String s = c.toString(null);
    Assertions.assertTrue(s.contains(", key=null, contained=true}"));
  }

  @Test
  void testToString_Key_NullKey_NotExecuted() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    });
    String s = c.toString(null);
    Assertions.assertTrue(s.contains(", key=null, contained=false}"));
  }

  @Test
  void testToString_Key_JacksonTrue() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    }, true);
    c.accept("hello");
    String s = c.toString("hello");
    Assertions.assertTrue(s.contains(", key=\"hello\", contained=true}"));
  }

  @Test
  void testToString_Key_JacksonFalse() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    }, false);
    c.accept("hello");
    String s = c.toString("hello");
    Assertions.assertTrue(s.contains(", key=hello, contained=true}"));
  }

  @Test
  void testOf_JacksonToString() {
    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> {
    }, true);
    Assertions.assertNotNull(c);
  }

  @Test
  void testConcurrency_SameKey_ExecutedOnce() throws Exception {
    int threadCount = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);
    AtomicInteger executionCount = new AtomicInteger(0);

    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> executionCount.incrementAndGet());

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          c.accept("sameKey");
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

    ExecuteOnceConsumer<String> c = ExecuteOnceConsumer.of(s -> executionCount.incrementAndGet());

    for (int k = 0; k < keys; k++) {
      String key = "key" + k;
      for (int t = 0; t < threadsPerKey; t++) {
        executor.submit(() -> {
          try {
            startLatch.await();
            c.accept(key);
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
