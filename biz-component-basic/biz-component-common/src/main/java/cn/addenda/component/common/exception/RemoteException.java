package cn.addenda.component.common.exception;

/**
 * 使用场景：调用外部接口失败
 */
public class RemoteException extends ServiceException {
  public RemoteException() {
    super();
  }

  public RemoteException(String message) {
    super(message);
  }

  public RemoteException(String message, Throwable cause) {
    super(message, cause);
  }

  public RemoteException(Throwable cause) {
    super(cause);
  }

  protected RemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}