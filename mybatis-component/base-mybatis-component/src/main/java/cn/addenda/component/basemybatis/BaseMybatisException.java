package cn.addenda.component.basemybatis;

import cn.addenda.component.jdk.exception.SystemException;

/**
 * @author addenda
 * @since 2022/2/1 17:33
 */
public class BaseMybatisException extends SystemException {
  public BaseMybatisException() {
  }

  public BaseMybatisException(String message) {
    super(message);
  }

  public BaseMybatisException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaseMybatisException(Throwable cause) {
    super(cause);
  }

  public BaseMybatisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "mybatis";
  }

  @Override
  public String getComponentName() {
    return "basemybatis";
  }

}
