package cn.addenda.component.common.test.exception;

import cn.addenda.component.common.exception.RemoteException;
import cn.addenda.component.common.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RemoteExceptionTest {

  @Test
  void testNoArg() {
    RemoteException e = new RemoteException();
    Assertions.assertNull(e.getMessage());
    Assertions.assertNull(e.getCause());
  }

  @Test
  void testMessage() {
    RemoteException e = new RemoteException("test msg");
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertNull(e.getCause());
  }

  @Test
  void testMessageAndCause() {
    Throwable cause = new RuntimeException("root cause");
    RemoteException e = new RemoteException("test msg", cause);
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertSame(cause, e.getCause());
  }

  @Test
  void testCause() {
    Throwable cause = new RuntimeException("root cause");
    RemoteException e = new RemoteException(cause);
    Assertions.assertSame(cause, e.getCause());
  }

  @Test
  void testInheritance() {
    Assertions.assertTrue(new RemoteException() instanceof ServiceException);
  }
}
