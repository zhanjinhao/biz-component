package cn.addenda.component.idempotenct;

import cn.addenda.component.convention.exception.SystemException;
import lombok.Getter;

/**
 * @author addenda
 * @since 2023/7/29 19:07
 */
public class IdempotenceException extends SystemException {

  @Getter
  private ConsumeStage consumeStage;

  public IdempotenceException() {
  }

  public IdempotenceException(String message) {
    super(message);
  }

  public IdempotenceException(String message, ConsumeStage consumeStage) {
    super(message);
    this.consumeStage = consumeStage;
  }

  public IdempotenceException(String message, Throwable cause) {
    super(message, cause);
  }

  public IdempotenceException(String message, ConsumeStage consumeStage, Throwable cause) {
    super(message, cause);
    this.consumeStage = consumeStage;
  }

  public IdempotenceException(Throwable cause) {
    super(cause);
  }

  public IdempotenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "spring";
  }

  @Override
  public String getComponentName() {
    return "idempotence";
  }

}
