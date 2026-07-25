package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.IdentifierMatcherFactory;
import cn.addenda.component.stacktrace.StackTraceException;
import cn.addenda.component.stacktrace.StackTraceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StackTraceUtilsMethodTest {

  @Test
  void test1() {
    Assertions.assertEquals("StackTraceUtilsMethodTest#test2 of StackTraceUtilsMethodTest.java:17", test2());
  }

  private String test2() {
    return test3();
  }

  @Test
  void testGetDetailedCallerInfoWithoutExcludes() {
    String s = directHelper();
    Assertions.assertEquals("StackTraceUtilsMethodTest#directHelper of StackTraceUtilsMethodTest.java:27", s);
  }

  private String directHelper() {
    return StackTraceUtils.getDetailedCallerInfo(true, false, false);
  }

  @Test
  void testWithMultipleExcludes() {
    String s = helperA();
    Assertions.assertEquals("StackTraceUtilsMethodTest#helperA of StackTraceUtilsMethodTest.java:37", s);
  }

  private String helperA() {
    return helperB();
  }

  private String helperB() {
    return StackTraceUtils.getDetailedCallerInfo(
            IdentifierMatcherFactory.withHash(StackTraceUtilsMethodTest.class, "helperB"));
  }

  @Test
  void testWithNullInExcludes() {
    String s = helperC();
    Assertions.assertEquals("StackTraceUtilsMethodTest#helperC of StackTraceUtilsMethodTest.java:52", s);
  }

  private String helperC() {
    return helperD();
  }

  private String helperD() {
    return StackTraceUtils.getDetailedCallerInfo(true, false, false,
            null, IdentifierMatcherFactory.withHash(StackTraceUtilsMethodTest.class, "helperD"));
  }

  @Test
  void testGetCallerInfoWithExcludes() {
    String result = callerHelper();
    Assertions.assertTrue(result.startsWith("StackTraceUtilsMethodTest"));
    Assertions.assertTrue(result.contains("#callerHelper"));
    Assertions.assertFalse(result.contains("callerInner"));
  }

  private String callerHelper() {
    return callerInner();
  }

  private String callerInner() {
    return StackTraceUtils.getCallerInfo(
            IdentifierMatcherFactory.withHash(StackTraceUtilsMethodTest.class, "callerInner"));
  }

  @Test
  void testGetCallerInfoFullClassName() {
    String result = fullNameHelper();
    Assertions.assertTrue(result.startsWith("cn.addenda.component.stacktrace.test.StackTraceUtilsMethodTest"));
    Assertions.assertFalse(result.startsWith("StackTraceUtilsMethodTest"));
  }

  private String fullNameHelper() {
    return StackTraceUtils.getCallerInfo(false, false, false);
  }

  @Test
  void testAllElementsExcludedThrowsException() {
    Assertions.assertThrows(StackTraceException.class, this::allExcludedHelper);
  }

  private String allExcludedHelper() {
    return StackTraceUtils.getCallerInfo("java.", "org.", "sun.", "cn.addenda", "jdk.");
  }

  private String test3() {
    System.out.println(IdentifierMatcherFactory.withHash(StackTraceUtilsMethodTest.class, "test3"));
    return StackTraceUtils.getDetailedCallerInfo(IdentifierMatcherFactory.withHash(StackTraceUtilsMethodTest.class, "test3"));
  }

}
