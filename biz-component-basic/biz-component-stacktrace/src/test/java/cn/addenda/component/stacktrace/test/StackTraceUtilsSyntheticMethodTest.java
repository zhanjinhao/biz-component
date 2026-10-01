package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.StackTraceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link StackTraceUtils#isSyntheticMethodName(String)} 的用例。
 * <p>{@code $} 是合法的 Java 标识符字符，所以判定必须严格按 javac 的命名规则来，
 * 不能退化成"包含 lambda$"这种宽松判断——否则用户写的 {@code notlambda$method} 会被误伤。
 */
class StackTraceUtilsSyntheticMethodTest {

  @Test
  void testLambdaMethodBody() {
    Assertions.assertTrue(StackTraceUtils.isSyntheticMethodName("lambda$test2$6"));
    Assertions.assertTrue(StackTraceUtils.isSyntheticMethodName("lambda$new$0"));
    Assertions.assertTrue(StackTraceUtils.isSyntheticMethodName("lambda$static$1"));
  }

  @Test
  void testSyntheticAccessor() {
    Assertions.assertTrue(StackTraceUtils.isSyntheticMethodName("access$000"));
    Assertions.assertTrue(StackTraceUtils.isSyntheticMethodName("access$100"));
    Assertions.assertTrue(StackTraceUtils.isSyntheticMethodName("access$12"));
  }

  @Test
  void testDeserializeLambda() {
    Assertions.assertTrue(StackTraceUtils.isSyntheticMethodName("$deserializeLambda$"));
  }

  @Test
  void testNormalMethod() {
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("run"));
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("getCallerInfo"));
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("<init>"));
  }

  /**
   * 用户方法名里可以含 {@code $}，含 {@code lambda$} 但不是 lambda 方法体、含 {@code access$} 但不是纯数字结尾，
   * 都必须按正常方法对待。
   */
  @Test
  void testUserMethodWithDollarIsNotSynthetic() {
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("notlambda$method"));
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("computeLambda$Value"));
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("my$lambda$x"));
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("access$"));
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("access$control"));
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("access$000a"));
    Assertions.assertFalse(StackTraceUtils.isSyntheticMethodName("lambda"));
  }
}
