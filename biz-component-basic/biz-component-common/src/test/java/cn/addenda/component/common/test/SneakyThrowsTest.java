package cn.addenda.component.common.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SneakyThrowsTest {

  @SneakyThrows
  private static void throwIOException() {
    throw new IOException("I am a checked exception!");
  }

  @SneakyThrows
  private static String throwAndReturn() {
    throw new IOException("checked in non-void method");
  }

  @SneakyThrows
  private static void normalReturn() {
  }

  @Test
  void sneakyThrowsHidesCheckedException() {
    // @SneakyThrows 不包装异常！运行时类型仍是 IOException
    // 所以 catch(RuntimeException) 抓不住，必须 catch(Exception)
    try {
      throwIOException();
      Assertions.fail("should have thrown");
    } catch (Exception e) {
      Assertions.assertEquals(IOException.class, e.getClass());
      Assertions.assertEquals("I am a checked exception!", e.getMessage());
    }
  }

  @Test
  void runtimeTypeIsStillIOException() {
    try {
      throwIOException();
    } catch (Throwable e) {
      Assertions.assertEquals(IOException.class, e.getClass());
    }
  }

  @Test
  void cannotCatchAsRuntimeException() {
    // catch(RuntimeException) 抓不住 —— IOException 不是 RuntimeException 的子类
    // 这验证了 @SneakyThrows 没有把异常包装成 RuntimeException
    Assertions.assertThrows(IOException.class, () -> {
      try {
        throwIOException();
      } catch (RuntimeException e) {
        Assertions.fail("should NOT be caught by RuntimeException");
      }
    });
  }

  @Test
  void cannotCatchAsCheckedException() {
    // catch(IOException) 编译失败 —— 编译器认为不可达
    Assertions.assertTrue(true);
  }

  @Test
  void nonVoidMethodAlsoWorks() {
    try {
      throwAndReturn();
      Assertions.fail("should have thrown");
    } catch (Exception e) {
      Assertions.assertEquals(IOException.class, e.getClass());
    }
  }

  @Test
  void normalReturnWorks() {
    Assertions.assertDoesNotThrow(SneakyThrowsTest::normalReturn);
  }
}
