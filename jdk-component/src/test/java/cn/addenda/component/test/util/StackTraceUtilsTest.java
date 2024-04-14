package cn.addenda.component.test.util;

import cn.addenda.component.jdk.util.StackTraceUtils;
import org.junit.Test;

/**
 * @author addenda
 * @since 2023/3/2 19:29
 */
public class StackTraceUtilsTest {

  @Test
  public void test1() {

    System.out.println(StackTraceUtils.getCallerInfo());
    System.out.println(StackTraceUtils.getDetailedCallerInfo());
  }

}
