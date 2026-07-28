package cn.addenda.component.common.test.lambda.costed;

import cn.addenda.component.common.lambda.costed.CostedBiConsumer;
import cn.addenda.component.common.lambda.named.NamedBiConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

class CostedBiConsumerLogTest {

  private ThreadPoolExecutor threadPoolExecutor;

  @BeforeEach
  void setUp() {
    threadPoolExecutor = new ThreadPoolExecutor(
            1, 2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5));
  }

  @AfterEach
  void tearDown() {
    if (threadPoolExecutor != null) {
      threadPoolExecutor.shutdownNow();
    }
  }

  // ==================== 非线程池：正常执行，高阈值 → DEBUG ====================

  @Test
  void testNonPool_NormalExecution_LogsDebug() {
    AtomicReference<String> captured = new AtomicReference<>();
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            60_000L, NamedBiConsumer.of("concat", (a, b) -> captured.set(a + b)));
    c.accept("hello", "world");

    Assertions.assertEquals("helloworld", captured.get());

    Assertions.assertEquals(1, c.testLogger.events.size());
    LogEvent event = c.testLogger.events.get(0);
    Assertions.assertEquals("DEBUG", event.level);
    Assertions.assertNull(event.throwable);

    String msg = event.message;
    Assertions.assertTrue(msg.contains("]: createDateTime["));
    Assertions.assertTrue(msg.contains("], endDateTime["));
    Assertions.assertTrue(msg.contains("], totalCost["));
    Assertions.assertTrue(msg.contains("ms]."));
    Assertions.assertFalse(msg.contains("startDateTime"));
    Assertions.assertFalse(msg.contains("runCost"));
    Assertions.assertFalse(msg.contains("queueSize"));
    Assertions.assertTrue(msg.contains("concat"));
    Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
  }

  // ==================== 非线程池：低阈值 → ERROR ====================

  @Test
  void testNonPool_LowThreshold_LogsError() throws Exception {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            0L, NamedBiConsumer.of("noop", (a, b) -> {
            }));
    Thread.sleep(1);
    c.accept("a", "b");

    Assertions.assertEquals(1, c.testLogger.events.size());
    LogEvent event = c.testLogger.events.get(0);
    Assertions.assertEquals("ERROR", event.level);
    Assertions.assertNull(event.throwable);

    String msg = event.message;
    Assertions.assertTrue(msg.contains("]: createDateTime["));
    Assertions.assertTrue(msg.contains("], endDateTime["));
    Assertions.assertTrue(msg.contains("], totalCost["));
    Assertions.assertTrue(msg.contains("ms]."));
    Assertions.assertFalse(msg.contains("startDateTime"));
    Assertions.assertFalse(msg.contains("runCost"));
    Assertions.assertFalse(msg.contains("queueSize"));
    Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
  }

  // ==================== 非线程池：抛出异常 → ERROR ====================

  @Test
  void testNonPool_Throws_LogsErrorWithThrowable() {
    RuntimeException ex = new RuntimeException("test error");
    AtomicReference<String> lastArg = new AtomicReference<>();
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            60_000L, NamedBiConsumer.of("throws", (a, b) -> {
              lastArg.set(a);
              throw ex;
            }));

    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> c.accept("x", "y"));
    Assertions.assertSame(ex, thrown);
    Assertions.assertEquals("x", lastArg.get());

    Assertions.assertEquals(1, c.testLogger.events.size());
    LogEvent event = c.testLogger.events.get(0);
    Assertions.assertEquals("ERROR", event.level);
    Assertions.assertSame(ex, event.throwable);

    String msg = event.message;
    Assertions.assertTrue(msg.contains("]: createDateTime["));
    Assertions.assertTrue(msg.contains("], endDateTime["));
    Assertions.assertTrue(msg.contains("], totalCost["));
    Assertions.assertTrue(msg.contains("ms]."));
    Assertions.assertFalse(msg.contains("startDateTime"));
    Assertions.assertFalse(msg.contains("runCost"));
    Assertions.assertFalse(msg.contains("queueSize"));
    Assertions.assertTrue(msg.contains("throws"));
    Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
  }

  // ==================== 非线程池：runCost == 0，totalCost <= threshold → isDebugEnabled=false → 无日志 ====================

  @Test
  void testNonPool_DebugDisabled_NoLog() {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            60_000L, NamedBiConsumer.of("noop", (a, b) -> {
            }));
    c.testLogger.debugEnabled = false;
    c.accept("a", "b");

    Assertions.assertEquals(0, c.testLogger.events.size());
  }

  // ==================== 非线程池：runCost > 0，totalCost > 2 * runCost → ERROR ====================

  @Test
  void testNonPool_RunCostPositive_WaitExceedsTwiceRunTime_LogsError() throws Exception {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            0L, NamedBiConsumer.of("run1ms", (a, b) -> {
              try {
                Thread.sleep(1);
              } catch (InterruptedException ignored) {
              }
            }));
    Thread.sleep(50);
    c.accept("a", "b");

    Assertions.assertEquals(1, c.testLogger.events.size());
    LogEvent event = c.testLogger.events.get(0);
    Assertions.assertEquals("ERROR", event.level);
    Assertions.assertNull(event.throwable);

    String msg = event.message;
    Assertions.assertTrue(msg.contains("]: createDateTime["));
    Assertions.assertTrue(msg.contains("], endDateTime["));
    Assertions.assertTrue(msg.contains("], totalCost["));
    Assertions.assertTrue(msg.contains("ms]."));
    Assertions.assertFalse(msg.contains("startDateTime"));
    Assertions.assertFalse(msg.contains("runCost"));
    Assertions.assertFalse(msg.contains("queueSize"));
    Assertions.assertTrue(msg.contains("run1ms"));
    Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
  }

  // ==================== 非线程池：runCost > 0，totalCost - runCost > threshold → ERROR ====================

  @Test
  void testNonPool_RunCostPositive_QueueTimeExceedsThreshold_LogsError() throws Exception {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            1L, NamedBiConsumer.of("sleep50", (a, b) -> {
              try {
                Thread.sleep(50);
              } catch (InterruptedException ignored) {
              }
            }));
    Thread.sleep(10);
    c.accept("a", "b");

    Assertions.assertEquals(1, c.testLogger.events.size());
    LogEvent event = c.testLogger.events.get(0);
    Assertions.assertEquals("ERROR", event.level);
    Assertions.assertNull(event.throwable);

    String msg = event.message;
    Assertions.assertTrue(msg.contains("]: createDateTime["));
    Assertions.assertTrue(msg.contains("], endDateTime["));
    Assertions.assertTrue(msg.contains("], totalCost["));
    Assertions.assertTrue(msg.contains("ms]."));
    Assertions.assertFalse(msg.contains("startDateTime"));
    Assertions.assertFalse(msg.contains("runCost"));
    Assertions.assertFalse(msg.contains("queueSize"));
    Assertions.assertTrue(msg.contains("sleep50"));
    Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
  }

  // ==================== 非线程池：runCost > 0，正常执行不触发 ERROR → DEBUG ====================

  @Test
  void testNonPool_RunCostPositive_NoConditionTriggers_LogsDebug() throws Exception {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            60_000L, NamedBiConsumer.of("run1ms", (a, b) -> {
              try {
                Thread.sleep(1);
              } catch (InterruptedException ignored) {
              }
            }));
    c.accept("a", "b");

    Assertions.assertEquals(1, c.testLogger.events.size());
    LogEvent event = c.testLogger.events.get(0);
    Assertions.assertEquals("DEBUG", event.level);
    Assertions.assertNull(event.throwable);

    String msg = event.message;
    Assertions.assertTrue(msg.contains("]: createDateTime["));
    Assertions.assertTrue(msg.contains("], endDateTime["));
    Assertions.assertTrue(msg.contains("], totalCost["));
    Assertions.assertTrue(msg.contains("ms]."));
    Assertions.assertTrue(msg.contains("run1ms"));
    Assertions.assertFalse(msg.contains("startDateTime"));
    Assertions.assertFalse(msg.contains("runCost"));
    Assertions.assertFalse(msg.contains("queueSize"));
    Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
  }

  // ==================== 非线程池：runCost > 0，isDebugEnabled=false → 无日志 ====================

  @Test
  void testNonPool_RunCostPositive_DebugDisabled_NoLog() throws Exception {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            60_000L, NamedBiConsumer.of("run1ms", (a, b) -> {
              try {
                Thread.sleep(1);
              } catch (InterruptedException ignored) {
              }
            }));
    c.testLogger.debugEnabled = false;
    c.accept("a", "b");

    Assertions.assertEquals(0, c.testLogger.events.size());
  }

  // ==================== 线程池：正常执行 → DEBUG + 线程池信息 ====================

  @Test
  void testPool_NormalExecution_LogsDebugWithPoolInfo() {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    tpe.submit(() -> { /* occupy one thread */ });
    try {
      TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
              60_000L, NamedBiConsumer.of("noop", (a, b) -> {
              }), tpe);
      c.accept("a", "b");

      Assertions.assertEquals(1, c.testLogger.events.size());
      LogEvent event = c.testLogger.events.get(0);
      Assertions.assertEquals("DEBUG", event.level);
      Assertions.assertNull(event.throwable);

      String msg = event.message;
      Assertions.assertTrue(msg.contains("]: createDateTime["));
      Assertions.assertTrue(msg.contains("], startDateTime["));
      Assertions.assertTrue(msg.contains("], endDateTime["));
      Assertions.assertTrue(msg.contains("], totalCost["));
      Assertions.assertTrue(msg.contains("ms], runCost["));
      Assertions.assertTrue(msg.contains("The state of the thread pool"));
      Assertions.assertTrue(msg.contains("queueSize["));
      Assertions.assertTrue(msg.contains("], poolSize["));
      Assertions.assertTrue(msg.contains("], activeCount["));
      Assertions.assertTrue(msg.contains("noop"));
      Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
    } finally {
      tpe.shutdownNow();
    }
  }

  // ==================== 线程池：低阈值 → ERROR + 线程池信息 ====================

  @Test
  void testPool_LowThreshold_LogsErrorWithPoolInfo() throws Exception {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            0L, NamedBiConsumer.of("noop", (a, b) -> {
            }), threadPoolExecutor);
    Thread.sleep(1);
    c.accept("a", "b");

    Assertions.assertEquals(1, c.testLogger.events.size());
    LogEvent event = c.testLogger.events.get(0);
    Assertions.assertEquals("ERROR", event.level);
    Assertions.assertNull(event.throwable);

    String msg = event.message;
    Assertions.assertTrue(msg.contains("]: createDateTime["));
    Assertions.assertTrue(msg.contains("], startDateTime["));
    Assertions.assertTrue(msg.contains("], endDateTime["));
    Assertions.assertTrue(msg.contains("], totalCost["));
    Assertions.assertTrue(msg.contains("ms], runCost["));
    Assertions.assertTrue(msg.contains("The state of the thread pool"));
    Assertions.assertTrue(msg.contains("queueSize["));
    Assertions.assertTrue(msg.contains("], poolSize["));
    Assertions.assertTrue(msg.contains("], activeCount["));
    Assertions.assertTrue(msg.contains("noop"));
    Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
  }

  // ==================== 线程池：抛出异常 → ERROR + throwable + 线程池信息 ====================

  @Test
  void testPool_Throws_LogsErrorWithThrowableAndPoolInfo() {
    RuntimeException ex = new RuntimeException("pool error");
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            60_000L, NamedBiConsumer.of("throws", (a, b) -> {
              throw ex;
            }), threadPoolExecutor);

    Assertions.assertThrows(RuntimeException.class, () -> c.accept("x", "y"));

    Assertions.assertEquals(1, c.testLogger.events.size());
    LogEvent event = c.testLogger.events.get(0);
    Assertions.assertEquals("ERROR", event.level);
    Assertions.assertSame(ex, event.throwable);

    String msg = event.message;
    Assertions.assertTrue(msg.contains("]: createDateTime["));
    Assertions.assertTrue(msg.contains("], startDateTime["));
    Assertions.assertTrue(msg.contains("], endDateTime["));
    Assertions.assertTrue(msg.contains("], totalCost["));
    Assertions.assertTrue(msg.contains("ms], runCost["));
    Assertions.assertTrue(msg.contains("The state of the thread pool"));
    Assertions.assertTrue(msg.contains("queueSize["));
    Assertions.assertTrue(msg.contains("], poolSize["));
    Assertions.assertTrue(msg.contains("], activeCount["));
    Assertions.assertTrue(msg.contains("throws"));
    Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
  }

  // ==================== 线程池：runCost == 0，isDebugEnabled=false → 无日志 ====================

  @Test
  void testPool_DebugDisabled_NoLog() {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            60_000L, NamedBiConsumer.of("noop", (a, b) -> {
            }), threadPoolExecutor);
    c.testLogger.debugEnabled = false;
    c.accept("a", "b");

    Assertions.assertEquals(0, c.testLogger.events.size());
  }

  // ==================== 线程池：runCost > 0，totalCost > 2 * runCost → ERROR ====================

  @Test
  void testPool_RunCostPositive_WaitExceedsTwiceRunTime_LogsError() throws Exception {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    try {
      TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
              0L, NamedBiConsumer.of("run1ms", (a, b) -> {
                try {
                  Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
              }), tpe);
      Thread.sleep(50);
      c.accept("a", "b");

      Assertions.assertEquals(1, c.testLogger.events.size());
      LogEvent event = c.testLogger.events.get(0);
      Assertions.assertEquals("ERROR", event.level);
      Assertions.assertNull(event.throwable);

      String msg = event.message;
      Assertions.assertTrue(msg.contains("]: createDateTime["));
      Assertions.assertTrue(msg.contains("], startDateTime["));
      Assertions.assertTrue(msg.contains("], endDateTime["));
      Assertions.assertTrue(msg.contains("], totalCost["));
      Assertions.assertTrue(msg.contains("ms], runCost["));
      Assertions.assertTrue(msg.contains("The state of the thread pool"));
      Assertions.assertTrue(msg.contains("queueSize["));
      Assertions.assertTrue(msg.contains("], poolSize["));
      Assertions.assertTrue(msg.contains("], activeCount["));
      Assertions.assertTrue(msg.contains("run1ms"));
      Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
    } finally {
      tpe.shutdownNow();
    }
  }

  // ==================== 线程池：runCost > 0，totalCost - runCost > threshold → ERROR ====================

  @Test
  void testPool_RunCostPositive_QueueTimeExceedsThreshold_LogsError() throws Exception {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    try {
      TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
              1L, NamedBiConsumer.of("sleep50", (a, b) -> {
                try {
                  Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
              }), tpe);
      Thread.sleep(10);
      c.accept("a", "b");

      Assertions.assertEquals(1, c.testLogger.events.size());
      LogEvent event = c.testLogger.events.get(0);
      Assertions.assertEquals("ERROR", event.level);
      Assertions.assertNull(event.throwable);

      String msg = event.message;
      Assertions.assertTrue(msg.contains("]: createDateTime["));
      Assertions.assertTrue(msg.contains("], startDateTime["));
      Assertions.assertTrue(msg.contains("], endDateTime["));
      Assertions.assertTrue(msg.contains("], totalCost["));
      Assertions.assertTrue(msg.contains("ms], runCost["));
      Assertions.assertTrue(msg.contains("The state of the thread pool"));
      Assertions.assertTrue(msg.contains("queueSize["));
      Assertions.assertTrue(msg.contains("], poolSize["));
      Assertions.assertTrue(msg.contains("], activeCount["));
      Assertions.assertTrue(msg.contains("sleep50"));
      Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
    } finally {
      tpe.shutdownNow();
    }
  }

  // ==================== 线程池：runCost > 0，正常执行不触发 ERROR → DEBUG ====================

  @Test
  void testPool_RunCostPositive_NoConditionTriggers_LogsDebug() throws Exception {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    try {
      TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
              60_000L, NamedBiConsumer.of("run1ms", (a, b) -> {
                try {
                  Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
              }), tpe);
      c.accept("a", "b");

      Assertions.assertEquals(1, c.testLogger.events.size());
      LogEvent event = c.testLogger.events.get(0);
      Assertions.assertEquals("DEBUG", event.level);
      Assertions.assertNull(event.throwable);

      String msg = event.message;
      Assertions.assertTrue(msg.contains("]: createDateTime["));
      Assertions.assertTrue(msg.contains("], startDateTime["));
      Assertions.assertTrue(msg.contains("], endDateTime["));
      Assertions.assertTrue(msg.contains("], totalCost["));
      Assertions.assertTrue(msg.contains("ms], runCost["));
      Assertions.assertTrue(msg.contains("The state of the thread pool"));
      Assertions.assertTrue(msg.contains("queueSize["));
      Assertions.assertTrue(msg.contains("], poolSize["));
      Assertions.assertTrue(msg.contains("], activeCount["));
      Assertions.assertTrue(msg.contains("run1ms"));
      Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
    } finally {
      tpe.shutdownNow();
    }
  }

  // ==================== 线程池：runCost > 0，isDebugEnabled=false → 无日志 ====================

  @Test
  void testPool_RunCostPositive_DebugDisabled_NoLog() throws Exception {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    try {
      TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
              60_000L, NamedBiConsumer.of("run1ms", (a, b) -> {
                try {
                  Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
              }), tpe);
      c.testLogger.debugEnabled = false;
      c.accept("a", "b");

      Assertions.assertEquals(0, c.testLogger.events.size());
    } finally {
      tpe.shutdownNow();
    }
  }

  // ==================== 消息验证：严格格式匹配 ====================

  @Test
  void testMessageFormat_NonPool_FullMatch() {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            0L, NamedBiConsumer.of("fmt-test", (a, b) -> {
            }));
    c.accept("x", "y");

    String msg = c.testLogger.events.get(0).message;
    Assertions.assertTrue(msg.matches(
            "cn\\.addenda\\.component\\.common\\.lambda\\.named\\.NamedBiConsumer" +
                    "\\[NamedBiConsumer\\{.*, name=fmt-test\\}\\]: " +
                    "createDateTime\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\], " +
                    "endDateTime\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\], " +
                    "totalCost\\[\\d+ms\\]\\. .*"));
  }

  @Test
  void testMessageFormat_Pool_FullMatch() {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            0L, NamedBiConsumer.of("fmt-test", (a, b) -> {
            }), threadPoolExecutor);
    c.accept("a", "b");

    String msg = c.testLogger.events.get(0).message;
    Assertions.assertTrue(msg.matches(
            "cn\\.addenda\\.component\\.common\\.lambda\\.named\\.NamedBiConsumer" +
                    "\\[NamedBiConsumer\\{.*, name=fmt-test\\}\\]: " +
                    "createDateTime\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\], " +
                    "startDateTime\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\], " +
                    "endDateTime\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\], " +
                    "totalCost\\[\\d+ms\\], runCost\\[\\d+ms\\]\\. " +
                    "The state of the thread pool at the moment the task is submitted: " +
                    "queueSize\\[\\d+\\], poolSize\\[\\d+\\], activeCount\\[\\d+\\]\\."));
  }

  // ==================== 消息验证：字段顺序 ====================

  @Test
  void testMessageFormat_ThreadPoolOrder() {
    TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
            0L, NamedBiConsumer.of("noop", (a, b) -> {
            }), threadPoolExecutor);
    c.accept("a", "b");

    String msg = c.testLogger.events.get(0).message;
    int idxCreate = msg.indexOf("createDateTime[");
    int idxStart = msg.indexOf("startDateTime[");
    int idxEnd = msg.indexOf("endDateTime[");
    int idxTotal = msg.indexOf("totalCost[");
    int idxRun = msg.indexOf("runCost[");
    int idxQueue = msg.indexOf("queueSize[");

    Assertions.assertTrue(idxCreate < idxEnd);
    Assertions.assertTrue(idxStart < idxEnd);
    Assertions.assertTrue(idxEnd < idxTotal);
    Assertions.assertTrue(idxTotal < idxRun);
    Assertions.assertTrue(idxRun < idxQueue);
  }

  // ==================== 线程池：mock executor 的具体数值 ====================

  @Test
  void testPool_ExactPoolNumbers() throws Exception {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    try {
      tpe.submit(() -> {
        try {
          Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
      });
      Thread.sleep(10);

      TestableCostedBiConsumer<String, String> c = TestableCostedBiConsumer.newInstance(
              0L, NamedBiConsumer.of("noop", (a, b) -> {
              }), tpe);
      c.accept("a", "b");

      Assertions.assertEquals(1, c.testLogger.events.size());
      LogEvent event = c.testLogger.events.get(0);
      Assertions.assertNull(event.throwable);

      String msg = event.message;
      Assertions.assertTrue(msg.contains("]: createDateTime["));
      Assertions.assertTrue(msg.contains("], startDateTime["));
      Assertions.assertTrue(msg.contains("], endDateTime["));
      Assertions.assertTrue(msg.contains("], totalCost["));
      Assertions.assertTrue(msg.contains("ms], runCost["));
      Assertions.assertTrue(msg.contains("The state of the thread pool"));
      Assertions.assertTrue(msg.contains("queueSize[0]"));
      Assertions.assertTrue(msg.contains("], poolSize["));
      Assertions.assertTrue(msg.contains("activeCount[1]"));
      Assertions.assertTrue(msg.contains("noop"));
      Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
    } finally {
      tpe.shutdownNow();
    }
  }


  static class TestableCostedBiConsumer<T, U> extends CostedBiConsumer<T, U> {

    final TestLogger testLogger;

    public TestableCostedBiConsumer(LocalDateTime createDateTime, Long threshold,
                                    BiConsumer<T, U> biConsumer, TestLogger testLogger) {
      super(createDateTime, threshold, biConsumer);
      this.testLogger = testLogger;
    }

    public TestableCostedBiConsumer(LocalDateTime createDateTime, Long threshold,
                                    BiConsumer<T, U> biConsumer,
                                    ThreadPoolExecutor threadPoolExecutor, TestLogger testLogger) {
      super(createDateTime, threshold, biConsumer, threadPoolExecutor);
      this.testLogger = testLogger;
    }

    @Override
    protected org.slf4j.Logger getLogger() {
      return testLogger;
    }

    public static <T, U> TestableCostedBiConsumer<T, U> newInstance(Long threshold, BiConsumer<T, U> biConsumer) {
      TestLogger logger = new TestLogger("TestableCostedBiConsumer");
      return new TestableCostedBiConsumer<>(LocalDateTime.now(), threshold, biConsumer, logger);
    }

    public static <T, U> TestableCostedBiConsumer<T, U> newInstance(Long threshold, BiConsumer<T, U> biConsumer,
                                                                    ThreadPoolExecutor tpe) {
      TestLogger logger = new TestLogger("TestableCostedBiConsumer");
      return new TestableCostedBiConsumer<>(LocalDateTime.now(), threshold, biConsumer, tpe, logger);
    }
  }

}
