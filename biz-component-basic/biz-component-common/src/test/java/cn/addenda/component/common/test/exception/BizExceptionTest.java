package cn.addenda.component.common.test.exception;

import cn.addenda.component.common.exception.BizException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BizExceptionTest {

  @Test
  void testNoArg() {
    BizException e = new BizException();
    Assertions.assertNull(e.getMessage());
    Assertions.assertNull(e.getCause());
  }

  @Test
  void testMessage() {
    BizException e = new BizException("test msg");
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertNull(e.getCause());
  }

  @Test
  void testMessageAndCause() {
    Throwable cause = new RuntimeException("root cause");
    BizException e = new BizException("test msg", cause);
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertSame(cause, e.getCause());
  }

  @Test
  void testCause() {
    Throwable cause = new RuntimeException("root cause");
    BizException e = new BizException(cause);
    Assertions.assertSame(cause, e.getCause());
  }

}
