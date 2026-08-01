package cn.addenda.component.common.test.exception;

import cn.addenda.component.common.CommonException;
import cn.addenda.component.common.exception.SystemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SystemExceptionTest {

  @Test
  void testNoArg() {
    SystemException e = new SystemException();
    Assertions.assertNull(e.getMessage());
    Assertions.assertNull(e.getCause());
    Assertions.assertEquals("system#system", e.getName());
  }

  @Test
  void testMessage() {
    SystemException e = new SystemException("test msg");
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertNull(e.getCause());
    Assertions.assertEquals("system#system", e.getName());
  }

  @Test
  void testMessageAndCause() {
    Throwable cause = new RuntimeException("root cause");
    SystemException e = new SystemException("test msg", cause);
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertSame(cause, e.getCause());
    Assertions.assertEquals("system#system", e.getName());
  }

  @Test
  void testCause() {
    Throwable cause = new RuntimeException("root cause");
    SystemException e = new SystemException(cause);
    Assertions.assertSame(cause, e.getCause());
    Assertions.assertEquals("system#system", e.getName());
  }

  @Test
  void testFullCtor() {
    Throwable cause = new RuntimeException("root cause");
    SystemException e = new SystemException("test msg", cause, true, true);
    Assertions.assertEquals("test msg", e.getMessage());
    Assertions.assertSame(cause, e.getCause());
    Assertions.assertEquals("system#system", e.getName());
  }

  @Test
  void testModuleName() {
    Assertions.assertEquals("system", new SystemException().moduleName());
  }

  @Test
  void testComponentName() {
    Assertions.assertEquals("system", new SystemException().componentName());
  }

  @Test
  void testUnExpectedException() {
    SystemException e = SystemException.unExpectedException();
    Assertions.assertEquals("unExpected exception!", e.getMessage());
    Assertions.assertEquals("system#system", e.getName());
  }

  @Test
  void testUnExpectedExceptionWithMessage() {
    SystemException e = SystemException.unExpectedException("something wrong");
    Assertions.assertTrue(e.getMessage().contains("something wrong"));
    Assertions.assertEquals("system#system", e.getName());
  }

  @Test
  void testToString() {
    SystemException e = new SystemException("test");
    String s = e.toString();
    Assertions.assertTrue(s.contains("SystemException"));
    Assertions.assertTrue(s.contains("name='system#system'"));
  }

  @Test
  void testCommonException() {
    CommonException e = new CommonException() {
    };
    Assertions.assertEquals("common", e.moduleName());
    Assertions.assertEquals("common", e.componentName());
    Assertions.assertEquals("common#common", e.getName());
  }

  @Test
  void testCommonExceptionWithMessage() {
    CommonException e = new CommonException("test") {
    };
    Assertions.assertEquals("test", e.getMessage());
    Assertions.assertEquals("common#common", e.getName());
  }
}
