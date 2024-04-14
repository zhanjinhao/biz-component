package cn.addenda.component.convention.exception;

import lombok.Getter;

/**
 * 系统异常是不应该出现的异常。
 *
 * @author addenda
 * @since 2023/5/29 22:13
 */
@Getter
public class SystemException extends RuntimeException implements IErrorCode {

  private final String name;

  public SystemException() {
    super();
    this.name = fullName();
  }

  public SystemException(String message) {
    super(message);
    this.name = fullName();
  }

  public SystemException(String message, Throwable cause) {
    super(message, cause);
    this.name = fullName();
  }

  public SystemException(Throwable cause) {
    super(cause);
    this.name = fullName();
  }

  public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.name = fullName();
  }

  public String getModuleName() {
    return "system";
  }

  public String getComponentName() {
    return "system";
  }

  private String fullName() {
    return getModuleName() + ": " + getComponentName();
  }

  public static SystemException unExpectedException() {
    return new SystemException("unExpected exception!");
  }

  @Override
  public String code() {
    return fullName();
  }

  @Override
  public String message() {
    return getMessage();
  }
}
