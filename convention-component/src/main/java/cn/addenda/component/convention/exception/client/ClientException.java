package cn.addenda.component.convention.exception.client;

import cn.addenda.component.convention.exception.ServiceException;

/**
 * 使用场景：客户端参数不对（包括url参数、body参数、header参数）
 */
public class ClientException extends ServiceException {

  public ClientException(String message) {
    super(new ClientErrorCode(message));
  }

  public ClientException(IClientErrorCode iClientErrorCode) {
    super(iClientErrorCode);
  }

  public ClientException(String message, Throwable cause) {
    super(new ClientErrorCode(message), cause);
  }

  public ClientException(IClientErrorCode iClientErrorCode, Throwable cause) {
    super(iClientErrorCode, cause);
  }

  public ClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(new ClientErrorCode(message), cause, enableSuppression, writableStackTrace);
  }

  public ClientException(IClientErrorCode iClientErrorCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(iClientErrorCode, cause, enableSuppression, writableStackTrace);
  }

}
