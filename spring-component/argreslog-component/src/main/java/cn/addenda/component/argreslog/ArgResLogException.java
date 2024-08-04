package cn.addenda.component.argreslog;

import cn.addenda.component.jdk.exception.SystemException;

/**
 * @author addenda
 * @since 2023/3/9 11:21
 */
public class ArgResLogException extends SystemException {

  public ArgResLogException() {
  }

  public ArgResLogException(String message) {
    super(message);
  }

  public ArgResLogException(String message, Throwable cause) {
    super(message, cause);
  }

  public ArgResLogException(Throwable cause) {
    super(cause);
  }

  public ArgResLogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "spring";
  }

  @Override
  public String getComponentName() {
    return "argreslog";
  }
}
