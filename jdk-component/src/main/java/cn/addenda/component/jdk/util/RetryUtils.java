package cn.addenda.component.jdk.util;

import cn.addenda.component.lambda.TFunction;
import cn.addenda.component.lambda.TRunnable;
import cn.addenda.component.lambda.TSupplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 只有传入的行为是幂等行为才能使用此工具。
 *
 * @author addenda
 * @since 2023/10/5 23:29
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetryUtils {

  public static <R> R retryWhenException(TSupplier<R> supplier, Object attachment) throws Throwable {
    try {
      return supplier.get();
    } catch (Throwable throwable) {
      log.error("RetryWhenException. Attachment: [{}].", attachment, throwable);
      return supplier.get();
    }
  }

  public static void retryWhenException(TRunnable runnable, Object attachment) throws Throwable {
    try {
      runnable.run();
    } catch (Throwable throwable) {
      log.error("RetryWhenException. Attachment: [{}].", attachment, throwable);
      runnable.run();
    }
  }

  public static <T, R> R retryWhenException(TFunction<T, R> function, T t, Object attachment) throws Throwable {
    try {
      return function.apply(t);
    } catch (Throwable throwable) {
      log.error("RetryWhenException. Attachment: [{}].", attachment, throwable);
      return function.apply(t);
    }
  }

}
