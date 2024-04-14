/*
 * copy from mybatis project.
 */
package cn.addenda.component.convention.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author Clinton Begin
 */
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

  public static RuntimeException wrapAsRuntimeException(Throwable throwable, Class<? extends RuntimeException> exception) {
    throwable = ExceptionUtils.unwrapThrowable(throwable);
    if (throwable != null && !(exception.isAssignableFrom(throwable.getClass()))) {
      if (throwable instanceof RuntimeException) {
        return (RuntimeException) throwable;
      } else {
        return new UndeclaredThrowableException(throwable);
      }
    }

    return exception.cast(throwable);
  }

}
