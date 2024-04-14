package cn.addenda.component.convention.exception.component;

import cn.addenda.component.convention.exception.ServiceException;

/**
 * 使用场景：内部组件
 */
public class ComponentServiceException extends ServiceException {

  public ComponentServiceException(String message) {
    super(new ComponentServiceErrorCode(message));
  }

  public ComponentServiceException(IComponentServiceErrorCode iErrorCode) {
    super(iErrorCode);
  }

  public ComponentServiceException(String message, Throwable cause) {
    super(new ComponentServiceErrorCode(message), cause);
  }

  public ComponentServiceException(IComponentServiceErrorCode iErrorCode, Throwable cause) {
    super(iErrorCode, cause);
  }

  public ComponentServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(new ComponentServiceErrorCode(message), cause, enableSuppression, writableStackTrace);
  }

  public ComponentServiceException(IComponentServiceErrorCode iErrorCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(iErrorCode, cause, enableSuppression, writableStackTrace);
  }
}
