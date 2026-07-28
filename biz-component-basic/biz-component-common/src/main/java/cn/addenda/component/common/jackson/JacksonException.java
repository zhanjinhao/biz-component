package cn.addenda.component.common.jackson;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class JacksonException extends RuntimeException {

  public JacksonException() {
    super();
  }

  public JacksonException(String message) {
    super(message);
  }

  public JacksonException(String message, Throwable cause) {
    super(message, cause);
  }

  public JacksonException(Throwable cause) {
    super(cause);
  }

}
