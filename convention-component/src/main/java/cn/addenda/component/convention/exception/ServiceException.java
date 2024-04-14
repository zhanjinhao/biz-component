package cn.addenda.component.convention.exception;

/**
 * @author addenda
 * @since 2023/6/3 19:09
 */
public class ServiceException extends RuntimeException implements IErrorCode {

  private final IErrorCode iErrorCode;

  public ServiceException(String message) {
    super(message);
    this.iErrorCode = new ServiceErrorCode(message);
  }

  public ServiceException(IErrorCode iErrorCode) {
    super(iErrorCode.message());
    this.iErrorCode = iErrorCode;
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
    this.iErrorCode = new ServiceErrorCode(message);
  }

  public ServiceException(IErrorCode iErrorCode, Throwable cause) {
    super(iErrorCode.message(), cause);
    this.iErrorCode = iErrorCode;
  }

  public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.iErrorCode = new ServiceErrorCode(message);
  }

  public ServiceException(IErrorCode iErrorCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(iErrorCode.message(), cause, enableSuppression, writableStackTrace);
    this.iErrorCode = iErrorCode;
  }

  @Override
  public String code() {
    return iErrorCode.code();
  }

  @Override
  public String message() {
    return iErrorCode.message();
  }

  @Override
  public String toString() {
    return "errorCode: " + code() + ". " + super.toString();
  }

}
