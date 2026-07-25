package cn.addenda.component.stacktrace.test;

import org.junit.jupiter.api.Test;

class ThreadTest {

  @Test
  void test() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    for (StackTraceElement stackTraceElement : stackTrace) {
      System.out.println(stackTraceElement);
    }
  }

}
