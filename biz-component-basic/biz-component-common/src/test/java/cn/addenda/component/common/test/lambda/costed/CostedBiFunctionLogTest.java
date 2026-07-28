package cn.addenda.component.common.test.lambda.costed;

import cn.addenda.component.common.lambda.costed.CostedBiFunction;
import cn.addenda.component.common.lambda.named.NamedBiFunction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

class CostedBiFunctionLogTest {

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

  static class TestableCostedBiFunction<T, U, R> extends CostedBiFunction<T, U, R> {

    final TestLogger testLogger;

    public TestableCostedBiFunction(LocalDateTime createDateTime, Long threshold,
                                    BiFunction<T, U, R> biFunction, TestLogger testLogger) {
      super(createDateTime, threshold, biFunction);
      this.testLogger = testLogger;
    }

    public TestableCostedBiFunction(LocalDateTime createDateTime, Long threshold,
                                    BiFunction<T, U, R> biFunction,
                                    ThreadPoolExecutor threadPoolExecutor, TestLogger testLogger) {
      super(createDateTime, threshold, biFunction, threadPoolExecutor);
      this.testLogger = testLogger;
    }

    @Override
    protected Logger getLogger() {
      return testLogger;
    }

    public static <T, U, R> TestableCostedBiFunction<T, U, R> newInstance(Long threshold, BiFunction<T, U, R> biFunction) {
      TestLogger logger = new TestLogger("TestableCostedBiFunction");
      return new TestableCostedBiFunction<>(LocalDateTime.now(), threshold, biFunction, logger);
    }

    public static <T, U, R> TestableCostedBiFunction<T, U, R> newInstance(Long threshold, BiFunction<T, U, R> biFunction,
                                                                          ThreadPoolExecutor tpe) {
      TestLogger logger = new TestLogger("TestableCostedBiFunction");
      return new TestableCostedBiFunction<>(LocalDateTime.now(), threshold, biFunction, tpe, logger);
    }
  }

  // ==================== 非线程池：正常执行，高阈值 → DEBUG ====================

  @Test
  void testNonPool_NormalExecution_LogsDebug() {
    TestableCostedBiFunction<Integer, Integer, Integer> c = TestableCostedBiFunction.newInstance(
            60_000L, NamedBiFunction.of("sum", (Integer a, Integer b) -> a + b));
    Assertions.assertEquals(Integer.valueOf(7), c.apply(3, 4));

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
    Assertions.assertTrue(msg.contains("sum"));
    Assertions.assertTrue(msg.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*"));
  }

  // ==================== 非线程池：低阈值 → ERROR ====================

  @Test
  void testNonPool_LowThreshold_LogsError() throws Exception {
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            0L, NamedBiFunction.of("noop", (String a, String b) -> a + b));
    Thread.sleep(1);
    c.apply("a", "b");

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
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            60_000L, NamedBiFunction.of("throws", (String a, String b) -> {
              lastArg.set(a);
              throw ex;
            }));

    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> c.apply("x", "y"));
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
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            60_000L, NamedBiFunction.of("noop", (String a, String b) -> a + b));
    c.testLogger.debugEnabled = false;
    c.apply("a", "b");

    Assertions.assertEquals(0, c.testLogger.events.size());
  }

  // ==================== 非线程池：runCost > 0，totalCost > 2 * runCost → ERROR ====================

  @Test
  void testNonPool_RunCostPositive_WaitExceedsTwiceRunTime_LogsError() throws Exception {
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            0L, NamedBiFunction.of("run1ms", (String a, String b) -> {
              try {
                Thread.sleep(1);
              } catch (InterruptedException ignored) {
              }
              return a + b;
            }));
    Thread.sleep(50);
    c.apply("a", "b");

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
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            1L, NamedBiFunction.of("sleep50", (String a, String b) -> {
              try {
                Thread.sleep(50);
              } catch (InterruptedException ignored) {
              }
              return a + b;
            }));
    Thread.sleep(10);
    c.apply("a", "b");

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
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            60_000L, NamedBiFunction.of("run1ms", (String a, String b) -> {
              try {
                Thread.sleep(1);
              } catch (InterruptedException ignored) {
              }
              return a + b;
            }));
    c.apply("a", "b");

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
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            60_000L, NamedBiFunction.of("run1ms", (String a, String b) -> {
              try {
                Thread.sleep(1);
              } catch (InterruptedException ignored) {
              }
              return a + b;
            }));
    c.testLogger.debugEnabled = false;
    c.apply("a", "b");

    Assertions.assertEquals(0, c.testLogger.events.size());
  }

  // ==================== 线程池：正常执行 → DEBUG + 线程池信息 ====================

  @Test
  void testPool_NormalExecution_LogsDebugWithPoolInfo() {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    tpe.submit(() -> { /* occupy one thread */ });
    try {
      TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
              60_000L, NamedBiFunction.of("noop", (String a, String b) -> a + b), tpe);
      c.apply("a", "b");

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
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            0L, NamedBiFunction.of("noop", (String a, String b) -> a + b), threadPoolExecutor);
    Thread.sleep(1);
    c.apply("a", "b");

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
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            60_000L, NamedBiFunction.of("throws", (String a, String b) -> {
              throw ex;
            }), threadPoolExecutor);

    Assertions.assertThrows(RuntimeException.class, () -> c.apply("x", "y"));

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
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            60_000L, NamedBiFunction.of("noop", (String a, String b) -> a + b), threadPoolExecutor);
    c.testLogger.debugEnabled = false;
    c.apply("a", "b");

    Assertions.assertEquals(0, c.testLogger.events.size());
  }

  // ==================== 线程池：runCost > 0，totalCost > 2 * runCost → ERROR ====================

  @Test
  void testPool_RunCostPositive_WaitExceedsTwiceRunTime_LogsError() throws Exception {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    try {
      TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
              0L, NamedBiFunction.of("run1ms", (String a, String b) -> {
                try {
                  Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                return a + b;
              }), tpe);
      Thread.sleep(50);
      c.apply("a", "b");

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
      TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
              1L, NamedBiFunction.of("sleep50", (String a, String b) -> {
                try {
                  Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
                return a + b;
              }), tpe);
      Thread.sleep(10);
      c.apply("a", "b");

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
      TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
              60_000L, NamedBiFunction.of("run1ms", (String a, String b) -> {
                try {
                  Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                return a + b;
              }), tpe);
      c.apply("a", "b");

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
      TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
              60_000L, NamedBiFunction.of("run1ms", (String a, String b) -> {
                try {
                  Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                return a + b;
              }), tpe);
      c.testLogger.debugEnabled = false;
      c.apply("a", "b");

      Assertions.assertEquals(0, c.testLogger.events.size());
    } finally {
      tpe.shutdownNow();
    }
  }

  // ==================== 消息验证：严格格式匹配 ====================

  @Test
  void testMessageFormat_NonPool_FullMatch() {
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            0L, NamedBiFunction.of("fmt-test", (String a, String b) -> a + b));
    c.apply("x", "y");

    String msg = c.testLogger.events.get(0).message;
    Assertions.assertTrue(msg.matches(
            "cn\\.addenda\\.component\\.common\\.lambda\\.named\\.NamedBiFunction" +
                    "\\[NamedBiFunction\\{.*, name=fmt-test\\}\\]: " +
                    "createDateTime\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\], " +
                    "endDateTime\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\], " +
                    "totalCost\\[\\d+ms\\]\\. .*"));
  }

  @Test
  void testMessageFormat_Pool_FullMatch() {
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            0L, NamedBiFunction.of("fmt-test", (String a, String b) -> a + b), threadPoolExecutor);
    c.apply("a", "b");

    String msg = c.testLogger.events.get(0).message;
    Assertions.assertTrue(msg.matches(
            "cn\\.addenda\\.component\\.common\\.lambda\\.named\\.NamedBiFunction" +
                    "\\[NamedBiFunction\\{.*, name=fmt-test\\}\\]: " +
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
    TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
            0L, NamedBiFunction.of("noop", (String a, String b) -> a + b), threadPoolExecutor);
    c.apply("a", "b");

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

      TestableCostedBiFunction<String, String, String> c = TestableCostedBiFunction.newInstance(
              0L, NamedBiFunction.of("noop", (String a, String b) -> a + b), tpe);
      c.apply("a", "b");

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
}
