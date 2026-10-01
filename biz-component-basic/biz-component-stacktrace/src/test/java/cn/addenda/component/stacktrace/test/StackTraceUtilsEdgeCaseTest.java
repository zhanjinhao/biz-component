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

  /**
   * 局部类（javac 命名 {@code Outer$1Local}）也会被 {@code ifExcludeAnonymousOrLocalClass} 跳过。
   * <p>该开关覆盖"局部类 + 匿名类"两类：它们都是编译器生成的类名，调用方要的是外层方法。
   * 这里把行为固定下来，避免以后被"修正"成只看 {@code $1}（那样局部类的帧会冒出来）。
   */
  @Test
  void testLocalClassIsExcludedToo() {
    class Local {
      String withExclusion() {
        return StackTraceUtils.getCallerInfo(true, false, true);
      }

      String withoutExclusion() {
        return StackTraceUtils.getCallerInfo(true, false, false);
      }
    }
    Local local = new Local();

    // 开：跳过局部类帧，返回外层方法
    Assertions.assertEquals("StackTraceUtilsEdgeCaseTest#testLocalClassIsExcludedToo", local.withExclusion());

    // 关：如实返回局部类帧，类名形如 StackTraceUtilsEdgeCaseTest$1Local
    String withoutExclusion = local.withoutExclusion();
    Assertions.assertTrue(withoutExclusion.contains("$"), withoutExclusion);
    Assertions.assertTrue(withoutExclusion.endsWith("Local#withoutExclusion"), withoutExclusion);
  }

  /**
   * {@code $} 是合法的 Java 标识符字符，用户完全可能写出 {@code notlambda$method} 这种方法名。
   * lambda 判定必须用 {@code startsWith("lambda$")}（javac 生成的 lambda 方法体一律以它开头），
   * 用 {@code contains} 会把这种用户方法当成 lambda 帧一起跳过。
   */
  @Test
  void testMethodNameContainingLambdaIsNotExcluded() {
    Assertions.assertEquals("StackTraceUtilsEdgeCaseTest#notlambda$method", notlambda$method());
  }

  private String notlambda$method() {
    return StackTraceUtils.getCallerInfo(true, true, false);
  }
}
