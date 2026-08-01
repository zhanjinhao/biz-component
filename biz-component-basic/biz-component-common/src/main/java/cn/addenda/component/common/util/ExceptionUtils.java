/*
 * copy from mybatis project.
 */
package cn.addenda.component.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionUtils {

  public static Throwable unwrapThrowable(Throwable wrapped) {
    if (wrapped == null) {
      return null;
    }
    Throwable unwrapped = wrapped;
    while (true) {
      if (unwrapped instanceof InvocationTargetException) {
        unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
      } else if (unwrapped instanceof UndeclaredThrowableException) {
        unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
      } else {
        return unwrapped;
      }
    }
  }

  /**
   * 将任意 {@link Throwable} 包装为目标类型的 {@link RuntimeException}。
   * <p>
   * 处理逻辑：
   * <ol>
   *   <li>先通过 {@link #unwrapThrowable(Throwable)} 解包，剥离 {@link InvocationTargetException} / {@link UndeclaredThrowableException} 等包装器，获取真实异常。</li>
   *   <li>若解包后为 {@code null}，说明原始异常已被完全剥离（例如原始包装器的 target 就是 null），直接返回 {@code null}，调用方需自行判空。</li>
   *   <li>若解包后的异常已经可赋值给目标类型（{@code exception.isAssignableFrom(...)} 为 true），说明无需包装，直接强制转换后返回。</li>
   *   <li>否则进入不匹配分支：
   *     <ul>
   *       <li>若已经是 {@link RuntimeException}（但类型不匹配），直接返回，不做额外包装。</li>
   *       <li>若为受检异常（非 RuntimeException），用 {@link UndeclaredThrowableException} 包装后返回，以便在不需要声明受检异常的方法中抛出。</li>
   *     </ul>
   *   </li>
   * </ol>
   *
   * @param throwable 原始异常（可为 null）
   * @param exception 目标运行时异常类型，用于判断是否需要额外包装
   * @return 包装后的运行时异常；若解包后为 null 则返回 null
   */
  public static RuntimeException wrapAsRuntimeException(Throwable throwable, Class<? extends RuntimeException> exception) {
    // 1. 解包，移除 InvocationTargetException / UndeclaredThrowableException 等包装器，获取真实异常
    throwable = ExceptionUtils.unwrapThrowable(throwable);
    // 2. 解包后为 null：原始包装器内部无真实异常，返回 null 由调用方处理
    if (throwable == null) {
      return null;
    }
    // 3. 已匹配目标类型：无需包装，直接返回
    if (!exception.isAssignableFrom(throwable.getClass())) {
      // 4a. 不匹配但已是 RuntimeException：保留原类型直接返回
      if (throwable instanceof RuntimeException) {
        return (RuntimeException) throwable;
      } else {
        // 4b. 受检异常：用 UndeclaredThrowableException 包装，使其可在不声明 throws 的方法中抛出
        return new UndeclaredThrowableException(throwable);
      }
    }

    // 5. 类型完全匹配：强制转换返回
    return exception.cast(throwable);
  }

  public static String getThrowableStr(Throwable throwable) {
    if (throwable == null) {
      return null;
    }
    StringWriter error = new StringWriter();
    try (PrintWriter printWriter = new PrintWriter(error)) {
      throwable.printStackTrace(printWriter);
    }
    return error.toString();
  }

}
