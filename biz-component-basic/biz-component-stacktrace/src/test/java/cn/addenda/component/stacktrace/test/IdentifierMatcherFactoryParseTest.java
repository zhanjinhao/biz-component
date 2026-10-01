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
    // 前缀是字符串前缀：cn.a. 不会匹配 cn.ab.MyClass
    Assertions.assertFalse(matcher.match(element("cn.ab.MyClass", "m")));
  }

  /**
   * {@code *} 没有任何匹配含义（类名、方法名都不含它），出现只会生成"永远匹配不上"的 matcher，因此直接报错。
   * 前缀匹配直接写类名前缀，如 {@code cn.a.}。
   */
  @Test
  void testWildcardIsRejected() {
    String[] invalidIdentifiers = {"cn.a.*", "cn.*.MyClass", "*", "cn.a.**", "#cn.a.MyClass*"};

    for (String invalidIdentifier : invalidIdentifiers) {
      StackTraceException e = Assertions.assertThrows(StackTraceException.class, () ->
              IdentifierMatcherFactory.getIdentifierMatcher(invalidIdentifier));
      Assertions.assertTrue(e.getMessage().contains("[" + invalidIdentifier + "]"), "message=" + e.getMessage());
      Assertions.assertTrue(e.getMessage().contains("[cn.a.]"), "message 里要给出前缀写法：" + e.getMessage());
    }
  }

  /**
   * 含空白字符的标识永远匹配不上（类名/方法名不含空白），必须报错而不是静默接受。
   */
  @Test
  void testWhitespaceIdentifierIsRejected() {
    String[] invalidIdentifiers = {"#cn.a.MyClass ", " #cn.a.MyClass", "#cn.a.My Class", "cn.a. MyClass*"};

    for (String invalidIdentifier : invalidIdentifiers) {
      Assertions.assertThrows(StackTraceException.class, () ->
              IdentifierMatcherFactory.getIdentifierMatcher(invalidIdentifier));
    }
  }

  /**
   * 显式工厂方法：三种语义各有专用入口，不依赖任何"默认语义"。
   */
  @Test
  void testExplicitFactoryMethods() {
    IdentifierMatcher classMatcher = IdentifierMatcherFactory.classMatcher("cn.a.MyClass");
    Assertions.assertEquals(ClassNameMatcher.class, classMatcher.getClass());
    Assertions.assertTrue(classMatcher.match(element("cn.a.MyClass", "m")));
    Assertions.assertFalse(classMatcher.match(element("cn.a.MyClass2", "m")));

    IdentifierMatcher classAndMethodMatcher = IdentifierMatcherFactory.classAndMethodMatcher("cn.a.MyClass", "myMethod");
    Assertions.assertEquals(ClassNameAndMethodNameMatcher.class, classAndMethodMatcher.getClass());
    Assertions.assertTrue(classAndMethodMatcher.match(element("cn.a.MyClass", "myMethod")));
    Assertions.assertFalse(classAndMethodMatcher.match(element("cn.a.MyClass", "other")));

    IdentifierMatcher prefixMatcher = IdentifierMatcherFactory.prefixMatcher("cn.a.");
    Assertions.assertEquals(ClassNamePrefixMatcher.class, prefixMatcher.getClass());
    Assertions.assertTrue(prefixMatcher.match(element("cn.a.sub.Cls", "m")));
    Assertions.assertFalse(prefixMatcher.match(element("cn.ab.Cls", "m")));
  }

  /**
   * 显式工厂方法的 Class 重载与对应的 String 重载等价——这是原来"两个入口语义不一致"的地方。
   */
  @Test
  void testExplicitFactoryMethodsWithClass() {
    Class<?> clazz = IdentifierMatcherFactoryParseTest.class;

    IdentifierMatcher classMatcher = IdentifierMatcherFactory.classMatcher(clazz);
    Assertions.assertEquals(ClassNameMatcher.class, classMatcher.getClass());
    Assertions.assertTrue(classMatcher.match(element(clazz.getName(), "anyMethod")));
    Assertions.assertFalse(classMatcher.match(element(clazz.getName() + "2", "anyMethod")));

    IdentifierMatcher classAndMethodMatcher = IdentifierMatcherFactory.classAndMethodMatcher(clazz, "m");
    Assertions.assertEquals(ClassNameAndMethodNameMatcher.class, classAndMethodMatcher.getClass());
    Assertions.assertTrue(classAndMethodMatcher.match(element(clazz.getName(), "m")));
    Assertions.assertFalse(classAndMethodMatcher.match(element(clazz.getName(), "other")));

    // prefixMatcher(Class) 覆盖该类及其内部类
    IdentifierMatcher prefixMatcher = IdentifierMatcherFactory.prefixMatcher(clazz);
    Assertions.assertEquals(ClassNamePrefixMatcher.class, prefixMatcher.getClass());
    Assertions.assertTrue(prefixMatcher.match(element(clazz.getName(), "anyMethod")));
    Assertions.assertTrue(prefixMatcher.match(element(clazz.getName() + "$Inner", "anyMethod")));
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
