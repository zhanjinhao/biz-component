package cn.addenda.component.common.test.util;

import cn.addenda.component.common.util.ExceptionUtils;
import cn.addenda.component.common.exception.SystemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author addenda
 * @since 2026/8/1
 */
class ExceptionUtilsTest {

  // ==================== unwrapThrowable ====================

  @Test
  void unwrap_NullInput() {
    Assertions.assertNull(ExceptionUtils.unwrapThrowable(null));
  }

  @Test
  void unwrap_PlainException() {
    RuntimeException ex = new RuntimeException("test");
    Assertions.assertSame(ex, ExceptionUtils.unwrapThrowable(ex));
  }

  @Test
  void unwrap_SingleInvocationTargetException() {
    RuntimeException real = new RuntimeException("real");
    InvocationTargetException wrapper = new InvocationTargetException(real);
    Assertions.assertSame(real, ExceptionUtils.unwrapThrowable(wrapper));
  }

  @Test
  void unwrap_SingleUndeclaredThrowableException() {
    Exception real = new Exception("real");
    UndeclaredThrowableException wrapper = new UndeclaredThrowableException(real);
    Assertions.assertSame(real, ExceptionUtils.unwrapThrowable(wrapper));
  }

  @Test
  void unwrap_NestedWrapping() {
    RuntimeException real = new RuntimeException("real");
    UndeclaredThrowableException inner = new UndeclaredThrowableException(real);
    InvocationTargetException outer = new InvocationTargetException(inner);
    Assertions.assertSame(real, ExceptionUtils.unwrapThrowable(outer));
  }

  @Test
  void unwrap_DeepNested() {
    RuntimeException real = new RuntimeException("real");
    Throwable wrapped = new InvocationTargetException(
            new UndeclaredThrowableException(
                    new InvocationTargetException(real)));
    Assertions.assertSame(real, ExceptionUtils.unwrapThrowable(wrapped));
  }

  @Test
  void unwrap_InvocationTargetExceptionWithNullTarget() {
    InvocationTargetException wrapper = new InvocationTargetException(null, "no target");
    Assertions.assertNull(ExceptionUtils.unwrapThrowable(wrapper));
  }

  @Test
  void unwrap_UndeclaredThrowableExceptionWithNullTarget() {
    UndeclaredThrowableException wrapper = new UndeclaredThrowableException(null);
    Assertions.assertNull(ExceptionUtils.unwrapThrowable(wrapper));
  }

  @Test
  void unwrap_CheckedException() {
    IOException real = new IOException("io error");
    Assertions.assertSame(real, ExceptionUtils.unwrapThrowable(real));
  }

  // ==================== wrapAsRuntimeException ====================

  @Test
  void wrap_NullThrowable() {
    Assertions.assertNull(ExceptionUtils.wrapAsRuntimeException(null, RuntimeException.class));
  }

  @Test
  void wrap_UnwrapReturnsNull() {
    InvocationTargetException wrapper = new InvocationTargetException(null, "no target");
    Assertions.assertNull(ExceptionUtils.wrapAsRuntimeException(wrapper, RuntimeException.class));
  }

  @Test
  void wrap_AlreadyTargetType() {
    SystemException ex = new SystemException("test");
    RuntimeException result = ExceptionUtils.wrapAsRuntimeException(ex, SystemException.class);
    Assertions.assertSame(ex, result);
  }

  @Test
  void wrap_DifferentRuntimeException() {
    IllegalArgumentException ex = new IllegalArgumentException("illegal arg");
    RuntimeException result = ExceptionUtils.wrapAsRuntimeException(ex, SystemException.class);
    Assertions.assertSame(ex, result);
  }

  @Test
  void wrap_CheckedException() {
    IOException ex = new IOException("io error");
    RuntimeException result = ExceptionUtils.wrapAsRuntimeException(ex, RuntimeException.class);
    Assertions.assertTrue(result instanceof UndeclaredThrowableException);
    Assertions.assertSame(ex, ((UndeclaredThrowableException) result).getUndeclaredThrowable());
  }

  @Test
  void wrap_UnwrapToMatchedTarget() {
    SystemException real = new SystemException("real");
    InvocationTargetException wrapper = new InvocationTargetException(real);
    RuntimeException result = ExceptionUtils.wrapAsRuntimeException(wrapper, SystemException.class);
    Assertions.assertSame(real, result);
  }

  @Test
  void wrap_NestedUnwrapToCheckedException() {
    IOException real = new IOException("io error");
    UndeclaredThrowableException inner = new UndeclaredThrowableException(real);
    InvocationTargetException outer = new InvocationTargetException(inner);
    RuntimeException result = ExceptionUtils.wrapAsRuntimeException(outer, SystemException.class);
    Assertions.assertTrue(result instanceof UndeclaredThrowableException);
    Assertions.assertSame(real, ((UndeclaredThrowableException) result).getUndeclaredThrowable());
  }

  @Test
  void wrap_TargetTypeIsSuperclass() {
    SystemException ex = new SystemException("test");
    RuntimeException result = ExceptionUtils.wrapAsRuntimeException(ex, RuntimeException.class);
    Assertions.assertSame(ex, result);
  }

  // ==================== getThrowableStr ====================

  @Test
  void getThrowableStr_NullInput() {
    Assertions.assertNull(ExceptionUtils.getThrowableStr(null));
  }

  @Test
  void getThrowableStr_ContainsClassName() {
    RuntimeException ex = new RuntimeException("test message");
    String str = ExceptionUtils.getThrowableStr(ex);
    Assertions.assertNotNull(str);
    Assertions.assertTrue(str.contains("RuntimeException"));
  }

  @Test
  void getThrowableStr_ContainsMessage() {
    RuntimeException ex = new RuntimeException("test message");
    String str = ExceptionUtils.getThrowableStr(ex);
    Assertions.assertTrue(str.contains("test message"));
  }

  @Test
  void getThrowableStr_ContainsCauseChain() {
    IOException cause = new IOException("root cause");
    RuntimeException ex = new RuntimeException("wrapper", cause);
    String str = ExceptionUtils.getThrowableStr(ex);
    Assertions.assertTrue(str.contains("Caused by:"));
    Assertions.assertTrue(str.contains("IOException"));
    Assertions.assertTrue(str.contains("root cause"));
  }

  @Test
  void getThrowableStr_NoMessageException() {
    RuntimeException ex = new RuntimeException();
    String str = ExceptionUtils.getThrowableStr(ex);
    Assertions.assertNotNull(str);
    Assertions.assertTrue(str.contains("RuntimeException"));
  }

  @Test
  void getThrowableStr_ContainsStackTrace() {
    RuntimeException ex = new RuntimeException();
    String str = ExceptionUtils.getThrowableStr(ex);
    Assertions.assertTrue(str.contains("\tat "));
  }

  // ==================== getThrowableFullStr ====================

  @Test
  void getThrowableFullStr_NullInput() {
    Assertions.assertNull(ExceptionUtils.getThrowableFullStr(null));
  }

  @Test
  void getThrowableFullStr_ContainsClassName() {
    RuntimeException ex = new RuntimeException("test");
    String str = ExceptionUtils.getThrowableFullStr(ex);
    Assertions.assertTrue(str.startsWith("java.lang.RuntimeException: test"));
  }

  @Test
  void getThrowableFullStr_ContainsCauseChain() {
    IOException cause = new IOException("root");
    RuntimeException ex = new RuntimeException("wrapper", cause);
    String str = ExceptionUtils.getThrowableFullStr(ex);
    Assertions.assertTrue(str.contains("Caused by: java.io.IOException: root"));
    Assertions.assertFalse(str.contains("more"));
  }

  @Test
  void getThrowableFullStr_ContainsSuppressed() {
    RuntimeException ex = new RuntimeException("main", new RuntimeException("cause"));
    ex.addSuppressed(new RuntimeException("suppressed1"));
    ex.addSuppressed(new RuntimeException("suppressed2"));
    String fullStr = ExceptionUtils.getThrowableFullStr(ex);
    System.out.println(fullStr);
    Assertions.assertTrue(fullStr.contains("Suppressed: java.lang.RuntimeException: suppressed1"));
    Assertions.assertTrue(fullStr.contains("Suppressed: java.lang.RuntimeException: suppressed2"));

    String str = ExceptionUtils.getThrowableStr(ex);
    System.out.println(str);
  }

  @Test
  void getThrowableFullStr_NoMoreTruncation() {
    IOException cause = new IOException("root");
    RuntimeException ex = new RuntimeException("wrapper", cause);
    String str = ExceptionUtils.getThrowableFullStr(ex);
    Assertions.assertFalse(str.contains("... "));
  }

}