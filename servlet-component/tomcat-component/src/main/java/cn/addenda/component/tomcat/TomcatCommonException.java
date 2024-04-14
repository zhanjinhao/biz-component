package cn.addenda.component.tomcat;

import cn.addenda.component.convention.exception.SystemException;

/**
 * @author addenda
 * @since 2023/8/19 12:58
 */
public class TomcatCommonException extends SystemException {

  public TomcatCommonException() {
  }

  public TomcatCommonException(String message) {
    super(message);
  }

  public TomcatCommonException(String message, Throwable cause) {
    super(message, cause);
  }

  public TomcatCommonException(Throwable cause) {
    super(cause);
  }

  public TomcatCommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "servlet";
  }

  @Override
  public String getComponentName() {
    return "tomcat";
  }
}
