package cn.addenda.component.dynamicdatasource;

import cn.addenda.component.convention.exception.SystemException;

/**
 * @author addenda
 * @since 2022/3/3 17:26
 */
public class MultiDataSourceException extends SystemException {

  public MultiDataSourceException() {
  }

  public MultiDataSourceException(String message) {
    super(message);
  }

  public MultiDataSourceException(String message, Throwable cause) {
    super(message, cause);
  }

  public MultiDataSourceException(Throwable cause) {
    super(cause);
  }

  public MultiDataSourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "spring";
  }

  @Override
  public String getComponentName() {
    return "dynamic-datasource";
  }
}
