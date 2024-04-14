package cn.addenda.component.ratelimitationhelper;

import cn.addenda.component.convention.exception.SystemException;

/**
 * @author addenda
 * @since 2023/9/28 20:04
 */
public class RateLimitationHelperException extends SystemException {
  public RateLimitationHelperException() {
  }

  public RateLimitationHelperException(String message) {
    super(message);
  }

  public RateLimitationHelperException(String message, Throwable cause) {
    super(message, cause);
  }

  public RateLimitationHelperException(Throwable cause) {
    super(cause);
  }

  public RateLimitationHelperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String getModuleName() {
    return "spring";
  }

  @Override
  public String getComponentName() {
    return "rate-limitation-helper";
  }
}
