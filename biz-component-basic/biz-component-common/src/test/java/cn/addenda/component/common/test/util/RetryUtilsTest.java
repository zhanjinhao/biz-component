package cn.addenda.component.common.test.util;

import cn.addenda.component.common.lambda.ExceptionFunction;
import cn.addenda.component.common.util.ExceptionUtils;
import cn.addenda.component.common.util.RetryUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class RetryUtilsTest {

  @BeforeEach
  void setUp() {
    UUID.randomUUID();
  }

  @Test
  void testSuccess_NoRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    Integer result = RetryUtils.retry(() -> {
      count.incrementAndGet();
      return 42;
    }, null, 3);
    Assertions.assertEquals(42, result);
    Assertions.assertEquals(1, count.get());
  }

  @Test
  void testSuccess_AfterRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    Integer result = RetryUtils.retry(() -> {
      if (count.incrementAndGet() < 2) {
        throw new RuntimeException("fail");
      }
      return 42;
    }, null, 3);
    Assertions.assertEquals(42, result);
    Assertions.assertEquals(2, count.get());
  }

  @Test
  void testSuccess_AfterRetry_WithDuration() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    long start = System.currentTimeMillis();
    Integer result = RetryUtils.retry(() -> {
      if (count.incrementAndGet() < 2) {
        throw new RuntimeException("fail");
      }
      return 42;
    }, null, Duration.ofMillis(100), 3);
    long elapsed = System.currentTimeMillis() - start;
    Assertions.assertEquals(42, result);
    Assertions.assertEquals(2, count.get());
    Assertions.assertTrue(elapsed >= 100, "elapsed=" + elapsed);
  }

  @Test
  void testSuccess_AfterRetry_WithTimeUnit() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    long start = System.currentTimeMillis();
    Integer result = RetryUtils.retry(() -> {
      if (count.incrementAndGet() < 2) {
        throw new RuntimeException("fail");
      }
      return 42;
    }, null, TimeUnit.MILLISECONDS, 100, 3);
    long elapsed = System.currentTimeMillis() - start;
    Assertions.assertEquals(42, result);
    Assertions.assertEquals(2, count.get());
    Assertions.assertTrue(elapsed >= 100, "elapsed=" + elapsed);
  }

  @Test
  void testExhausted_ThrowsLastException() {
    RuntimeException ex1 = new RuntimeException("first");
    RuntimeException ex2 = new RuntimeException("last");
    AtomicInteger count = new AtomicInteger(0);
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
            RetryUtils.retry(() -> {
              if (count.incrementAndGet() == 1) throw ex1;
              throw ex2;
            }, null, 2));
    Assertions.assertSame(ex2, thrown);
    Assertions.assertEquals(1, thrown.getSuppressed().length);
    Assertions.assertSame(ex1, thrown.getSuppressed()[0]);
    String throwableStr = ExceptionUtils.getThrowableStr(thrown);
    System.out.println(throwableStr);
  }

  @Test
  void testExhausted_SuppressedExceptions() {
    RuntimeException first = new RuntimeException("first");
    RuntimeException second = new RuntimeException("second");
    RuntimeException third = new RuntimeException("third");
    AtomicInteger count = new AtomicInteger(0);

    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
            RetryUtils.retry(() -> {
              int i = count.incrementAndGet();
              if (i == 1) throw first;
              if (i == 2) throw second;
              throw third;
            }, null, 2));

    Assertions.assertSame(third, thrown);
    Throwable[] suppressed = thrown.getSuppressed();
    Assertions.assertEquals(2, suppressed.length);
    Assertions.assertSame(first, suppressed[0]);
    Assertions.assertSame(second, suppressed[1]);
  }

  @Test
  void testExhausted_MaxRetriesZero() {
    RuntimeException ex = new RuntimeException("fail");
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
            RetryUtils.retry(() -> {
              throw ex;
            }, null, 0));
    Assertions.assertSame(ex, thrown);
    Assertions.assertEquals(0, thrown.getSuppressed().length);
  }

  @Test
  void testExhausted_MaxRetriesZero_NoSuppressedOnSingleFailure() {
    RuntimeException ex1 = new RuntimeException("first");
    RuntimeException ex2 = new RuntimeException("second");
    AtomicInteger count = new AtomicInteger(0);
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
            RetryUtils.retry(() -> {
              if (count.incrementAndGet() == 1) throw ex1;
              throw ex2;
            }, "att", 1));
    Assertions.assertSame(ex2, thrown);
    Assertions.assertEquals(1, thrown.getSuppressed().length);
    Assertions.assertSame(ex1, thrown.getSuppressed()[0]);
  }

  @Test
  void testSuccess_AttachmentNull() throws Exception {
    Integer result = RetryUtils.retry(() -> 42, null, 1);
    Assertions.assertEquals(42, result);
  }

  @Test
  void testRunnable_Success_NoRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    RetryUtils.retry(count::incrementAndGet, null, 3);
    Assertions.assertEquals(1, count.get());
  }

  @Test
  void testRunnable_Success_AfterRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    RetryUtils.retry(() -> {
      if (count.incrementAndGet() < 2) {
        throw new RuntimeException("fail");
      }
    }, null, 3);
    Assertions.assertEquals(2, count.get());
  }

  @Test
  void testRunnable_Exhausted_Suppressed() {
    AtomicInteger count = new AtomicInteger(0);
    RuntimeException first = new RuntimeException("first");
    RuntimeException third = new RuntimeException("third");

    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
            RetryUtils.retry(() -> {
              int i = count.incrementAndGet();
              if (i == 1) throw first;
              if (i == 2) throw new RuntimeException("second");
              throw third;
            }, null, 2));

    Assertions.assertSame(third, thrown);
    Assertions.assertEquals(2, thrown.getSuppressed().length);
  }

  @Test
  void testFunction_Success_NoRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    String result = RetryUtils.retry(t -> {
      count.incrementAndGet();
      return t.toUpperCase();
    }, "hello", null, 3);
    Assertions.assertEquals("HELLO", result);
    Assertions.assertEquals(1, count.get());
  }

  @Test
  void testFunction_Success_AfterRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    String result = RetryUtils.retry(t -> {
      if (count.incrementAndGet() < 2) {
        throw new RuntimeException("fail");
      }
      return t.toUpperCase();
    }, "hello", null, 3);
    Assertions.assertEquals("HELLO", result);
    Assertions.assertEquals(2, count.get());
  }

  @Test
  void testFunction_Exhausted_Suppressed() {
    AtomicInteger count = new AtomicInteger(0);
    RuntimeException first = new RuntimeException("first");
    RuntimeException third = new RuntimeException("third");

    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
            RetryUtils.retry((ExceptionFunction<String, Object>) t -> {
              int i = count.incrementAndGet();
              if (i == 1) throw first;
              if (i == 2) throw new RuntimeException("second");
              throw third;
            }, "test", null, 2));

    Assertions.assertSame(third, thrown);
    Assertions.assertEquals(2, thrown.getSuppressed().length);
    String throwableStr = ExceptionUtils.getThrowableStr(thrown);
    System.out.println(throwableStr);
  }

  @Test
  void testInterval_TimeUnit_SleepBeforeRetry() {
    long start = System.currentTimeMillis();
    AtomicInteger count = new AtomicInteger(0);
    try {
      RetryUtils.retry(() -> {
        if (count.incrementAndGet() < 3) {
          throw new RuntimeException("fail");
        }
        return null;
      }, null, TimeUnit.MILLISECONDS, 50, 3);
    } catch (Exception ignore) {
    }
    long elapsed = System.currentTimeMillis() - start;
    Assertions.assertTrue(elapsed >= 100, "elapsed=" + elapsed);
  }

  @Test
  void testInterval_Duration_SleepBeforeRetry() {
    long start = System.currentTimeMillis();
    AtomicInteger count = new AtomicInteger(0);
    try {
      RetryUtils.retry(() -> {
        if (count.incrementAndGet() < 3) {
          throw new RuntimeException("fail");
        }
        return null;
      }, null, Duration.ofMillis(50), 3);
    } catch (Exception ignore) {
    }
    long elapsed = System.currentTimeMillis() - start;
    Assertions.assertTrue(elapsed >= 100, "elapsed=" + elapsed);
  }

  // ==================== ExceptionConsumer ====================

  @Test
  void testConsumer_Success_NoRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    RetryUtils.retry((String s) -> count.incrementAndGet(), "test", null, 3);
    Assertions.assertEquals(1, count.get());
  }

  @Test
  void testConsumer_Success_AfterRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    RetryUtils.retry((String s) -> {
      if (count.incrementAndGet() < 2) {
        throw new RuntimeException("fail");
      }
    }, "test", null, 3);
    Assertions.assertEquals(2, count.get());
  }

  @Test
  void testConsumer_Exhausted_Suppressed() {
    AtomicInteger count = new AtomicInteger(0);
    RuntimeException first = new RuntimeException("first");
    RuntimeException third = new RuntimeException("third");

    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
            RetryUtils.retry((String s) -> {
              int i = count.incrementAndGet();
              if (i == 1) throw first;
              if (i == 2) throw new RuntimeException("second");
              throw third;
            }, "test", null, 2));

    Assertions.assertSame(third, thrown);
    Assertions.assertEquals(2, thrown.getSuppressed().length);
  }

  // ==================== ExceptionBiFunction ====================

  @Test
  void testBiFunction_Success_NoRetry() throws Exception {
    Integer result = RetryUtils.retry((a, b) -> a + b, 3, 4, null, 3);
    Assertions.assertEquals(7, result);
  }

  @Test
  void testBiFunction_Success_AfterRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    Integer result = RetryUtils.retry((Integer a, Integer b) -> {
      if (count.incrementAndGet() < 2) {
        throw new RuntimeException("fail");
      }
      return a + b;
    }, 3, 4, null, 3);
    Assertions.assertEquals(7, result);
    Assertions.assertEquals(2, count.get());
  }

  @Test
  void testBiFunction_Exhausted_Suppressed() {
    AtomicInteger count = new AtomicInteger(0);
    RuntimeException first = new RuntimeException("first");
    RuntimeException third = new RuntimeException("third");

    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
            RetryUtils.retry((Integer a, Integer b) -> {
              int i = count.incrementAndGet();
              if (i == 1) throw first;
              if (i == 2) throw new RuntimeException("second");
              throw third;
            }, 1, 2, null, 2));

    Assertions.assertSame(third, thrown);
    Assertions.assertEquals(2, thrown.getSuppressed().length);
  }

  // ==================== ExceptionBiConsumer ====================

  @Test
  void testBiConsumer_Success_NoRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    RetryUtils.retry((String a, String b) -> count.incrementAndGet(), "x", "y", null, 3);
    Assertions.assertEquals(1, count.get());
  }

  @Test
  void testBiConsumer_Success_AfterRetry() throws Exception {
    AtomicInteger count = new AtomicInteger(0);
    RetryUtils.retry((String a, String b) -> {
      if (count.incrementAndGet() < 2) {
        throw new RuntimeException("fail");
      }
    }, "x", "y", null, 3);
    Assertions.assertEquals(2, count.get());
  }

  @Test
  void testBiConsumer_Exhausted_Suppressed() {
    AtomicInteger count = new AtomicInteger(0);
    RuntimeException first = new RuntimeException("first");
    RuntimeException third = new RuntimeException("third");

    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
            RetryUtils.retry((String a, String b) -> {
              int i = count.incrementAndGet();
              if (i == 1) throw first;
              if (i == 2) throw new RuntimeException("second");
              throw third;
            }, "x", "y", null, 2));

    Assertions.assertSame(third, thrown);
    Assertions.assertEquals(2, thrown.getSuppressed().length);
  }

}
