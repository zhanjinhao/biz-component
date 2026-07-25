package cn.addenda.component.stacktrace.test;

import org.junit.jupiter.api.Test;

class RegexTest {

  @Test
  void test1() {
    System.out.println("StackTraceUtilsTest#lambda$new$4".matches(".*#lambda\\$.*"));
    System.out.println("StackTraceUtilsTest#lamda$new$4".matches(".*#lambda\\$.*"));

    System.out.println("StackTraceUtilsTest$3#get".matches(".*\\$\\d+.*"));
    System.out.println("StackTraceUtilsTest$a3#get".matches(".*\\$\\d+.*"));
  }

}
