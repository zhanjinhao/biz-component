package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.IdentifierMatcherFactory;
import cn.addenda.component.stacktrace.StackTraceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StackTraceUtilsInnerClassTest {

  @Test
  void test1() {
    Assertions.assertEquals("StackTraceUtilsInnerClassTest#test1 of StackTraceUtilsInnerClassTest.java:12", new InnerClass().testInnerClass());
    Assertions.assertEquals("StackTraceUtilsInnerClassTest#test1 of StackTraceUtilsInnerClassTest.java:13", new StaticInnerClass().testStaticInnerClass());
  }

  class InnerClass {
    public String testInnerClass() {
      return StackTraceUtils.getDetailedCallerInfo(IdentifierMatcherFactory.withHash(InnerClass.class));
    }
  }

  static class StaticInnerClass {
    public String testStaticInnerClass() {
      return StackTraceUtils.getDetailedCallerInfo(IdentifierMatcherFactory.withHash(StaticInnerClass.class));
    }
  }


}
