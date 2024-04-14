package cn.addenda.component.idgenerator;

import cn.addenda.component.convention.exception.SystemException;

/**
 * @author addenda
 * @since 2022/2/5 22:45
 */
public class IdFillingException extends SystemException {

  public IdFillingException() {
  }

  public IdFillingException(String message) {
    super(message);
  }

  public IdFillingException(String message, Throwable cause) {
    super(message, cause);
  }

  public IdFillingException(Throwable cause) {
    super(cause);
  }

  public IdFillingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "mybatis";
  }

  @Override
  public String getComponentName() {
    return "id-generator";
  }
}
