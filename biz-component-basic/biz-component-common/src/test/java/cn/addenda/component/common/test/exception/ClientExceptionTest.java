package cn.addenda.component.common.test.exception;

import cn.addenda.component.common.exception.ClientException;
import cn.addenda.component.common.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClientExceptionTest {

  @Test
  void testNoArg() {
    ClientException e = new ClientException();
    Assertions.assertNull(e.getMessage());
    Assertions.assertNull(e.getCause());
  }

  @Test
  void testMessage() {
    ClientException e = new ClientException("test msg");
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertNull(e.getCause());
  }

  @Test
  void testMessageAndCause() {
    Throwable cause = new RuntimeException("root cause");
    ClientException e = new ClientException("test msg", cause);
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertSame(cause, e.getCause());
  }

  @Test
  void testCause() {
    Throwable cause = new RuntimeException("root cause");
    ClientException e = new ClientException(cause);
    Assertions.assertSame(cause, e.getCause());
  }

  @Test
  void testInheritance() {
    Assertions.assertTrue(new ClientException() instanceof ServiceException);
  }
}
