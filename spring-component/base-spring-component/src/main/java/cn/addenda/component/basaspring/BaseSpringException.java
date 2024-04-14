package cn.addenda.component.basaspring;

import cn.addenda.component.convention.exception.SystemException;

/**
 * @author addenda
 * @since 2022/8/7 12:06
 */
public class BaseSpringException extends SystemException {

  public BaseSpringException() {
    super();
  }

  public BaseSpringException(String message) {
    super(message);
  }

  public BaseSpringException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaseSpringException(Throwable cause) {
    super(cause);
  }

  public BaseSpringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "spring";
  }

  @Override
  public String getComponentName() {
    return super.getComponentName() + "base-spring";
  }
}
