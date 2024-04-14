package cn.addenda.component.lockhelper;

import cn.addenda.component.convention.exception.SystemException;
import lombok.Getter;
import lombok.Setter;

/**
 * @author addenda
 * @since 2022/11/30 19:16
 */
public class LockHelperException extends SystemException {

  @Setter
  @Getter
  private String mode;

  public static final String BUSY = "BUSY";
  public static final String ERROR = "ERROR";

  public LockHelperException() {
    this.mode = ERROR;
  }

  public LockHelperException(String message) {
    super(message);
    this.mode = ERROR;
  }

  public LockHelperException(String message, Throwable cause) {
    super(message, cause);
    this.mode = ERROR;
  }

  public LockHelperException(Throwable cause) {
    super(cause);
    this.mode = ERROR;
  }

  public LockHelperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.mode = ERROR;
  }

  @Override
  public String getModuleName() {
    return "spring";
  }

  @Override
  public String getComponentName() {
    return "lock-helper";
  }

}
