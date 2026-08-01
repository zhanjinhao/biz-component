package cn.addenda.component.common.test.exception;

import cn.addenda.component.common.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ServiceExceptionTest {

  @Test
  void testNoArg() {
    ServiceException e = new ServiceException();
    Assertions.assertNull(e.getMessage());
    Assertions.assertNull(e.getCause());
  }

  @Test
  void testMessage() {
    ServiceException e = new ServiceException("test msg");
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertNull(e.getCause());
  }

  @Test
  void testMessageAndCause() {
    Throwable cause = new RuntimeException("root cause");
    ServiceException e = new ServiceException("test msg", cause);
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertSame(cause, e.getCause());
  }

  @Test
  void testCause() {
    Throwable cause = new RuntimeException("root cause");
    ServiceException e = new ServiceException(cause);
    Assertions.assertSame(cause, e.getCause());
  }

}
