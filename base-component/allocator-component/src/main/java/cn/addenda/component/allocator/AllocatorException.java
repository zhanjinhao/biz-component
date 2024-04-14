package cn.addenda.component.allocator;

import cn.addenda.component.convention.exception.SystemException;

/**
 * @author addenda
 * @since 2023/9/16 12:24
 */
public class AllocatorException extends SystemException {

  public AllocatorException() {
    super();
  }

  public AllocatorException(String message) {
    super(message);
  }

  public AllocatorException(String message, Throwable cause) {
    super(message, cause);
  }

  public AllocatorException(Throwable cause) {
    super(cause);
  }

  public AllocatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "base";
  }

  @Override
  public String getComponentName() {
    return "allocator";
  }
}
