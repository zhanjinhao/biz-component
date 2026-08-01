package cn.addenda.component.common.exception;

import cn.addenda.component.common.util.string.Slf4jUtils;
import lombok.Getter;

/**
 * 系统异常是不应该出现的异常。
 *
 * @author addenda
 * @since 2023/5/29 22:13
 */
@Getter
public class SystemException extends RuntimeException {

  private final String name;

  public SystemException() {
    super();
    this.name = name();
  }

  public SystemException(String message) {
    super(message);
    this.name = name();
  }

  public SystemException(String message, Throwable cause) {
    super(message, cause);
    this.name = name();
  }

  public SystemException(Throwable cause) {
    super(cause);
    this.name = name();
  }

  public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.name = name();
  }

  public String moduleName() {
    return "system";
  }

  public String componentName() {
    return "system";
  }

  private String name() {
    return moduleName() + "#" + componentName();
  }

  public static SystemException unExpectedException() {
    return new SystemException("unExpected exception!");
  }

  public static SystemException unExpectedException(String message) {
    return new SystemException(Slf4jUtils.format("unExpected exception: [{}].", message));
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
            "name='" + name + '\'' +
            "} " + super.toString();
  }
}
