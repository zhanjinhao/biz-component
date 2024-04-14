package cn.addenda.component.jdk.util.sql;

import cn.addenda.component.jdk.JdkException;

public class ConnectionException extends JdkException {

  public ConnectionException() {
    super();
  }

  public ConnectionException(String message) {
    super(message);
  }

  public ConnectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConnectionException(Throwable cause) {
    super(cause);
  }

  public ConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "jdk";
  }

  @Override
  public String getComponentName() {
    return "jdk-connection";
  }
}
