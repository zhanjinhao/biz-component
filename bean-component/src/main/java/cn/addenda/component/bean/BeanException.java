package cn.addenda.component.bean;

import cn.addenda.component.convention.exception.SystemException;

public class BeanException extends SystemException {

  public BeanException() {
    super();
  }

  public BeanException(String message) {
    super(message);
  }

  public BeanException(String message, Throwable cause) {
    super(message, cause);
  }

  public BeanException(Throwable cause) {
    super(cause);
  }

  public BeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "bean";
  }

  @Override
  public String getComponentName() {
    return "bean";
  }
}
