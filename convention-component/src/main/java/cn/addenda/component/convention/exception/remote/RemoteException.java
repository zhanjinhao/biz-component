package cn.addenda.component.convention.exception.remote;

import cn.addenda.component.convention.exception.ServiceException;

/**
 * 使用场景：调用外部接口失败
 */
public class RemoteException extends ServiceException {
  public RemoteException(String message) {
    super(new RemoteErrorCode(message));
  }

  public RemoteException(IRemoteErrorCode iErrorCode) {
    super(iErrorCode);
  }

  public RemoteException(String message, Throwable cause) {
    super(new RemoteErrorCode(message), cause);
  }

  public RemoteException(IRemoteErrorCode iErrorCode, Throwable cause) {
    super(iErrorCode, cause);
  }

  public RemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(new RemoteErrorCode(message), cause, enableSuppression, writableStackTrace);
  }

  public RemoteException(IRemoteErrorCode iErrorCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(iErrorCode, cause, enableSuppression, writableStackTrace);
  }
}