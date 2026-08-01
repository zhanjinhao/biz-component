package cn.addenda.component.common.exception;

/**
 * 业务异常，用于快速中断请求
 *
 * @author addenda
 * @since 2023/6/3 19:09
 */
public class BizException extends RuntimeException {

  public BizException() {
    super();
  }

  public BizException(String message) {
    super(message);
  }

  public BizException(String message, Throwable cause) {
    super(message, cause);
  }

  public BizException(Throwable cause) {
    super(cause);
  }

  protected BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
