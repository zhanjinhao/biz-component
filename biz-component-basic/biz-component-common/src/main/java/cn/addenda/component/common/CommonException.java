package cn.addenda.component.common;

import cn.addenda.component.common.exception.SystemException;

/**
 * 这个异常正常情况下是不应该出现的，因为工具类的实现定义了确定的输入输出。
 * 在业务系统，由于依赖用户的输入，确定的输入往往很难达到。
 *
 * @author addenda
 * @since 2022/2/14 19:16
 */
public abstract class CommonException extends SystemException {
  protected CommonException() {
    super();
  }

  protected CommonException(String message) {
    super(message);
  }

  protected CommonException(String message, Throwable cause) {
    super(message, cause);
  }

  protected CommonException(Throwable cause) {
    super(cause);
  }

  protected CommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String moduleName() {
    return "common";
  }

  @Override
  public String componentName() {
    return "common";
  }

}
