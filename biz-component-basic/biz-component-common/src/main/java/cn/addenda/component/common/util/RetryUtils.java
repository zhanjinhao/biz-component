package cn.addenda.component.common.util;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import cn.addenda.component.common.lambda.ExceptionBiConsumer;
import cn.addenda.component.common.lambda.ExceptionBiFunction;
import cn.addenda.component.common.lambda.ExceptionConsumer;
import cn.addenda.component.common.lambda.ExceptionFunction;
import cn.addenda.component.common.lambda.ExceptionRunnable;
import cn.addenda.component.common.lambda.ExceptionSupplier;
import cn.addenda.component.stacktrace.StackTraceUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 只有传入的行为是幂等行为才能使用此工具。
 *
 * @author addenda
 * @since 2023/10/5 23:29
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetryUtils {

  private static final String LOG_MSG = "Caller:[{}], Attachment:[{}], Retry Interval:[{}], Execution:[{}/{}], retryId:[{}].";

  public static <R> R retry(
          ExceptionSupplier<R> supplier, Object attachment, int maxRetries) throws Exception {
    return doRetry(supplier, attachment, null, maxRetries);
  }

  public static void retry(
          ExceptionRunnable runnable, Object attachment, int maxRetries) throws Exception {
    doRetry(() -> { runnable.run(); return null; }, attachment, null, maxRetries);
  }

  public static <T, R> R retry(
          ExceptionFunction<T, R> function, T t, Object attachment, int maxRetries) throws Exception {
    return doRetry(() -> function.apply(t), attachment, null, maxRetries);
  }

  public static <R> R retry(
          ExceptionSupplier<R> supplier, Object attachment, Duration interval, int maxRetries) throws Exception {
    return doRetry(supplier, attachment, interval, maxRetries);
  }

  public static void retry(
          ExceptionRunnable runnable, Object attachment, Duration interval, int maxRetries) throws Exception {
    doRetry(() -> { runnable.run(); return null; }, attachment, interval, maxRetries);
  }

  public static <T, R> R retry(
          ExceptionFunction<T, R> function, T t, Object attachment, Duration interval, int maxRetries) throws Exception {
    return doRetry(() -> function.apply(t), attachment, interval, maxRetries);
  }

  public static <R> R retry(
          ExceptionSupplier<R> supplier, Object attachment, TimeUnit timeUnit, long interval, int maxRetries) throws Exception {
    return doRetry(supplier, attachment, Duration.ofMillis(timeUnit.toMillis(interval)), maxRetries);
  }

  public static void retry(
          ExceptionRunnable runnable, Object attachment, TimeUnit timeUnit, long interval, int maxRetries) throws Exception {
    doRetry(() -> { runnable.run(); return null; }, attachment, Duration.ofMillis(timeUnit.toMillis(interval)), maxRetries);
  }

  public static <T, R> R retry(
          ExceptionFunction<T, R> function, T t, Object attachment, TimeUnit timeUnit, long interval, int maxRetries) throws Exception {
    return doRetry(() -> function.apply(t), attachment, Duration.ofMillis(timeUnit.toMillis(interval)), maxRetries);
  }

  // ==================== ExceptionConsumer ====================

  public static <T> void retry(
          ExceptionConsumer<T> consumer, T t, Object attachment, int maxRetries) throws Exception {
    doRetry(() -> { consumer.accept(t); return null; }, attachment, null, maxRetries);
  }

  public static <T> void retry(
          ExceptionConsumer<T> consumer, T t, Object attachment, Duration interval, int maxRetries) throws Exception {
    doRetry(() -> { consumer.accept(t); return null; }, attachment, interval, maxRetries);
  }

  public static <T> void retry(
          ExceptionConsumer<T> consumer, T t, Object attachment, TimeUnit timeUnit, long interval, int maxRetries) throws Exception {
    doRetry(() -> { consumer.accept(t); return null; }, attachment, Duration.ofMillis(timeUnit.toMillis(interval)), maxRetries);
  }

  // ==================== ExceptionBiFunction ====================

  public static <T, U, R> R retry(
          ExceptionBiFunction<T, U, R> function, T t, U u, Object attachment, int maxRetries) throws Exception {
    return doRetry(() -> function.apply(t, u), attachment, null, maxRetries);
  }

  public static <T, U, R> R retry(
          ExceptionBiFunction<T, U, R> function, T t, U u, Object attachment, Duration interval, int maxRetries) throws Exception {
    return doRetry(() -> function.apply(t, u), attachment, interval, maxRetries);
  }

  public static <T, U, R> R retry(
          ExceptionBiFunction<T, U, R> function, T t, U u, Object attachment, TimeUnit timeUnit, long interval, int maxRetries) throws Exception {
    return doRetry(() -> function.apply(t, u), attachment, Duration.ofMillis(timeUnit.toMillis(interval)), maxRetries);
  }

  // ==================== ExceptionBiConsumer ====================

  public static <T, U> void retry(
          ExceptionBiConsumer<T, U> consumer, T t, U u, Object attachment, int maxRetries) throws Exception {
    doRetry(() -> { consumer.accept(t, u); return null; }, attachment, null, maxRetries);
  }

  public static <T, U> void retry(
          ExceptionBiConsumer<T, U> consumer, T t, U u, Object attachment, Duration interval, int maxRetries) throws Exception {
    doRetry(() -> { consumer.accept(t, u); return null; }, attachment, interval, maxRetries);
  }

  public static <T, U> void retry(
          ExceptionBiConsumer<T, U> consumer, T t, U u, Object attachment, TimeUnit timeUnit, long interval, int maxRetries) throws Exception {
    doRetry(() -> { consumer.accept(t, u); return null; }, attachment, Duration.ofMillis(timeUnit.toMillis(interval)), maxRetries);
  }

  private static <R> R doRetry(
          ExceptionSupplier<R> action, Object attachment, Duration duration, int maxRetries) throws Exception {
    int retriesDone = 0;
    String retryId = UUID.randomUUID().toString().replace("-", "");
    List<Exception> exceptions = new ArrayList<>();
    while (true) {
      try {
        return action.get();
      } catch (Exception e) {
        if (retriesDone >= maxRetries) {
          log.error(LOG_MSG, StackTraceUtils.getDetailedCallerInfo(), format(attachment),
                  duration == null ? "PT0S" : duration, retriesDone + 1, maxRetries + 1, retryId, e);
          for (Exception ee : exceptions) {
            if (ee != e) {
              e.addSuppressed(ee);
            }
          }
          throw e;
        }
        retriesDone++;
        exceptions.add(e);
        log.error(LOG_MSG, StackTraceUtils.getDetailedCallerInfo(), format(attachment),
                duration == null ? "PT0S" : duration, retriesDone, maxRetries + 1, retryId, e);
        if (duration != null) {
          SleepUtils.sleep(duration);
        }
      }
    }
  }

  private static String format(Object attachment) {
    if (attachment == null) {
      return null;
    }
    if (attachment instanceof CharSequence) {
      return String.valueOf(attachment);
    }
    return JacksonUtils.toStr(attachment);
  }

}
