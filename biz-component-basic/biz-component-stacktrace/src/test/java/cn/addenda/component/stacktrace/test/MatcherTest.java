package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.ClassNameAndMethodNameMatcher;
import cn.addenda.component.stacktrace.ClassNameMatcher;
import cn.addenda.component.stacktrace.ClassNamePrefixMatcher;
import cn.addenda.component.stacktrace.IdentifierMatcher;
import cn.addenda.component.stacktrace.IdentifierMatcherFactory;
import cn.addenda.component.stacktrace.StackTraceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MatcherTest {

  @Test
  void testClassNameMatcher() {
    IdentifierMatcher matcher = new ClassNameMatcher("cn.a.MyClass");

    Assertions.assertTrue(matcher.match(element("cn.a.MyClass", "m1")));
    Assertions.assertFalse(matcher.match(element("cn.a.Other", "m1")));
    Assertions.assertFalse(matcher.match(element("cn.a.MyClass2", "m1")));
  }

  @Test
  void testClassNamePrefixMatcher() {
    IdentifierMatcher matcher = new ClassNamePrefixMatcher("cn.a.");

    Assertions.assertTrue(matcher.match(element("cn.a.MyClass", "m1")));
    Assertions.assertTrue(matcher.match(element("cn.a.sub.Cls", "m1")));
    Assertions.assertFalse(matcher.match(element("cn.b.MyClass", "m1")));
  }

  @Test
  void testClassNameAndMethodNameMatcher() {
    IdentifierMatcher matcher = new ClassNameAndMethodNameMatcher("cn.a.MyClass", "myMethod");

    Assertions.assertTrue(matcher.match(element("cn.a.MyClass", "myMethod")));
    Assertions.assertFalse(matcher.match(element("cn.a.MyClass", "other")));
    Assertions.assertFalse(matcher.match(element("cn.a.Other", "myMethod")));
  }

  @Test
  void testConstructorValidation() {
    Assertions.assertThrows(StackTraceException.class, () -> new ClassNameMatcher(null));
    Assertions.assertThrows(StackTraceException.class, () -> new ClassNameMatcher(""));
    Assertions.assertThrows(StackTraceException.class, () -> new ClassNamePrefixMatcher(null));
    Assertions.assertThrows(StackTraceException.class, () -> new ClassNamePrefixMatcher(""));
    Assertions.assertThrows(StackTraceException.class, () -> new ClassNameAndMethodNameMatcher(null, "m"));
    Assertions.assertThrows(StackTraceException.class, () -> new ClassNameAndMethodNameMatcher("c", null));
  }

  @Test
  void testIdentifierMatcherFactoryEdgeCases() {
    Assertions.assertThrows(StackTraceException.class, () ->
            IdentifierMatcherFactory.getIdentifierMatcher("#"));

    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () ->
            IdentifierMatcherFactory.getIdentifierMatcher("##"));
  }

  @Test
  void testGetIdentifierMatcherWithPrefix() {
    IdentifierMatcher m = IdentifierMatcherFactory.getIdentifierMatcher("cn.a.prefix");
    Assertions.assertEquals(ClassNamePrefixMatcher.class, m.getClass());
    Assertions.assertTrue(m.match(element("cn.a.prefix.MyClass", "m")));
    Assertions.assertTrue(m.match(element("cn.a.prefix.sub.Cls", "m")));
    Assertions.assertFalse(m.match(element("cn.b.Other", "m")));
  }

  @Test
  void testMatcherCacheReturnsSameInstance() {
    IdentifierMatcher m1 = IdentifierMatcherFactory.getIdentifierMatcher(
            IdentifierMatcherFactory.withHash(MatcherTest.class));
    IdentifierMatcher m2 = IdentifierMatcherFactory.getIdentifierMatcher(
            IdentifierMatcherFactory.withHash(MatcherTest.class));
    Assertions.assertSame(m1, m2);
  }

  private static StackTraceElement element(String className, String methodName) {
    return new StackTraceElement(className, methodName, null, -1);
  }
}
