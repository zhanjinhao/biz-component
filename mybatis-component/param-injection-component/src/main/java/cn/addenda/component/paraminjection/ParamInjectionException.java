package cn.addenda.component.paraminjection;

import cn.addenda.component.convention.exception.SystemException;

/**
 * @author addenda
 * @since 2023/7/6 10:13
 */
public class ParamInjectionException extends SystemException {

  public ParamInjectionException() {
  }

  public ParamInjectionException(String message) {
    super(message);
  }

  public ParamInjectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ParamInjectionException(Throwable cause) {
    super(cause);
  }

  public ParamInjectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }


  @Override
  public String getModuleName() {
    return "mybatis";
  }

  @Override
  public String getComponentName() {
    return "param-injection";
  }
}
