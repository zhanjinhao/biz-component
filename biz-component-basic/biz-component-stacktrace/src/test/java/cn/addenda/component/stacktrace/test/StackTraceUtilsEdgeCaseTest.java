package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.IdentifierMatcherFactory;
import cn.addenda.component.stacktrace.StackTraceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StackTraceUtilsEdgeCaseTest {

  @Test
  void testExtractSimpleClassName() {
    // 通过 getCallerInfo(false, false, false) 验证全限定名
    String result = fullNameTest();
    Assertions.assertTrue(result.startsWith("cn.addenda.component.stacktrace.test.StackTraceUtilsEdgeCaseTest#"));
  }

  private String fullNameTest() {
    return StackTraceUtils.getCallerInfo(false, false, false);
  }

  @Test
  void testCallerInfoWithCustomExcludesConvenienceMethod() {
    String result = convenienceHelper();
    Assertions.assertTrue(result.startsWith("StackTraceUtilsEdgeCaseTest"));
    Assertions.assertFalse(result.contains("convenienceInner"));
  }

  private String convenienceHelper() {
    return convenienceInner();
  }

  private String convenienceInner() {
    return StackTraceUtils.getCallerInfo(true, false, false,
            IdentifierMatcherFactory.withHash(StackTraceUtilsEdgeCaseTest.class, "convenienceInner"));
  }

  @Test
  void testGetCallerInfoConvenienceOverloads() {
    // via no-arg -> useSimpleClassName=true, exclLambda=false, exclAnon=false
    Assertions.assertTrue(StackTraceUtils.getCallerInfo().contains("#"));

    // via String... excludes
    Assertions.assertTrue(StackTraceUtils.getCallerInfo("cn.dummy.X").contains("#"));

    // via detailed caller info no-arg
    Assertions.assertTrue(StackTraceUtils.getDetailedCallerInfo().contains(" of "));
    Assertions.assertTrue(StackTraceUtils.getDetailedCallerInfo().contains(":"));

    // via detailed caller info with excludes
    Assertions.assertTrue(StackTraceUtils.getDetailedCallerInfo("cn.dummy.X").contains(" of "));
  }

  @Test
  void testAnonymousInnerClassExcluded() {
    String result = new Object() {
      String invoke() {
        return StackTraceUtils.getCallerInfo(true, false, true);
      }
    }.invoke();
    Assertions.assertFalse(result.contains("$"));
  }
}
