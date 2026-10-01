package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.ClassNameAndMethodNameMatcher;
import cn.addenda.component.stacktrace.ClassNameMatcher;
import cn.addenda.component.stacktrace.ClassNamePrefixMatcher;
import cn.addenda.component.stacktrace.IdentifierMatcher;
import cn.addenda.component.stacktrace.IdentifierMatcherFactory;
import cn.addenda.component.stacktrace.StackTraceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link IdentifierMatcherFactory#getIdentifierMatcher(String)} 的标识解析用例。
 * <p>覆盖三类合法格式，以及非法格式必须抛出 {@link StackTraceException}（不能抛 AIOOBE、
 * 也不能静默降级成另一种 matcher）。
 */
class IdentifierMatcherFactoryParseTest {

  @Test
  void testPrefixFormat() {
    IdentifierMatcher matcher = IdentifierMatcherFactory.getIdentifierMatcher("cn.a.");
    Assertions.assertEquals(ClassNamePrefixMatcher.class, matcher.getClass());
    Assertions.assertTrue(matcher.match(element("cn.a.MyClass", "m")));
    Assertions.assertTrue(matcher.match(element("cn.a.sub.Cls", "m")));
    Assertions.assertFalse(matcher.match(element("cn.b.MyClass", "m")));
  }

  @Test
  void testClassNameFormat() {
    IdentifierMatcher matcher = IdentifierMatcherFactory.getIdentifierMatcher("#cn.a.MyClass");
    Assertions.assertEquals(ClassNameMatcher.class, matcher.getClass());
    Assertions.assertTrue(matcher.match(element("cn.a.MyClass", "m")));
    Assertions.assertFalse(matcher.match(element("cn.a.Other", "m")));
  }

  @Test
  void testClassNameAndMethodNameFormat() {
    IdentifierMatcher matcher = IdentifierMatcherFactory.getIdentifierMatcher("#cn.a.MyClass#myMethod");
    Assertions.assertEquals(ClassNameAndMethodNameMatcher.class, matcher.getClass());
    Assertions.assertTrue(matcher.match(element("cn.a.MyClass", "myMethod")));
    Assertions.assertFalse(matcher.match(element("cn.a.MyClass", "other")));
    Assertions.assertFalse(matcher.match(element("cn.a.Other", "myMethod")));
  }

  @Test
  void testNullAndEmpty() {
    Assertions.assertThrows(StackTraceException.class, () -> IdentifierMatcherFactory.getIdentifierMatcher((String) null));
    Assertions.assertThrows(StackTraceException.class, () -> IdentifierMatcherFactory.getIdentifierMatcher(""));
  }

  @Test
  void testSingleHashWithoutClassName() {
    StackTraceException e = Assertions.assertThrows(StackTraceException.class, () ->
            IdentifierMatcherFactory.getIdentifierMatcher("#"));
    Assertions.assertTrue(e.getMessage().contains("[#]"));
  }

  /**
   * {@code "##"} 曾经抛 ArrayIndexOutOfBoundsException（split 结果为空数组后取 [0]），
   * 现在必须是带上下文的 StackTraceException。
   */
  @Test
  void testEmptyClassNameAndMethodName() {
    StackTraceException e = Assertions.assertThrows(StackTraceException.class, () ->
            IdentifierMatcherFactory.getIdentifierMatcher("##"));
    Assertions.assertTrue(e.getMessage().contains("[##]"));
  }

  /**
   * {@code "#a#"} 曾经被 split 丢弃尾部空串、静默降级为 ClassNameMatcher("a")，现在必须报错。
   */
  @Test
  void testTrailingHashIsRejected() {
    StackTraceException e = Assertions.assertThrows(StackTraceException.class, () ->
            IdentifierMatcherFactory.getIdentifierMatcher("#cn.a.MyClass#"));
    Assertions.assertTrue(e.getMessage().contains("#cn.a.MyClass#"));
  }

  /**
   * {@code "#a#b#c"} 曾经静默丢弃第三段，现在必须报错。
   */
  @Test
  void testTooManySegmentsIsRejected() {
    StackTraceException e = Assertions.assertThrows(StackTraceException.class, () ->
            IdentifierMatcherFactory.getIdentifierMatcher("#cn.a.MyClass#myMethod#extra"));
    Assertions.assertTrue(e.getMessage().contains("#cn.a.MyClass#myMethod#extra"));
  }

  /**
   * withHash 系列构造出来的标识必须能被解析回等价语义的 matcher，这是最常用的调用方式。
   */
  @Test
  void testWithHashRoundTrip() {
    IdentifierMatcher classMatcher = IdentifierMatcherFactory.getIdentifierMatcher(
            IdentifierMatcherFactory.withHash(IdentifierMatcherFactoryParseTest.class));
    Assertions.assertEquals(ClassNameMatcher.class, classMatcher.getClass());
    Assertions.assertTrue(classMatcher.match(element(IdentifierMatcherFactoryParseTest.class.getName(), "anyMethod")));

    IdentifierMatcher classMethodMatcher = IdentifierMatcherFactory.getIdentifierMatcher(
            IdentifierMatcherFactory.withHash(IdentifierMatcherFactoryParseTest.class, "testWithHashRoundTrip"));
    Assertions.assertEquals(ClassNameAndMethodNameMatcher.class, classMethodMatcher.getClass());
    Assertions.assertTrue(classMethodMatcher.match(
            element(IdentifierMatcherFactoryParseTest.class.getName(), "testWithHashRoundTrip")));
    Assertions.assertFalse(classMethodMatcher.match(
            element(IdentifierMatcherFactoryParseTest.class.getName(), "otherMethod")));
  }

  /**
   * 解析失败不能污染缓存：同一个非法标识重复解析，每次都要抛异常。
   */
  @Test
  void testInvalidIdentifierIsNotCached() {
    for (int i = 0; i < 3; i++) {
      Assertions.assertThrows(StackTraceException.class, () ->
              IdentifierMatcherFactory.getIdentifierMatcher("#invalid#"));
    }
  }

  private static StackTraceElement element(String className, String methodName) {
    return new StackTraceElement(className, methodName, null, -1);
  }
}
