package cn.addenda.component.common.exception;

/**
 * 使用场景：客户端参数不对（包括url参数、body参数、header参数）
 */
public class ClientException extends ServiceException {

  public ClientException() {
    super();
  }

  public ClientException(String message) {
    super(message);
  }

  public ClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClientException(Throwable cause) {
    super(cause);
  }

  protected ClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
