package cn.addenda.component.stacktrace;

public class StackTraceException extends RuntimeException {

  public StackTraceException() {
    super();
  }

  public StackTraceException(String message) {
    super(message);
  }

  public StackTraceException(String message, Throwable cause) {
    super(message, cause);
  }

  public StackTraceException(Throwable cause) {
    super(cause);
  }

  protected StackTraceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
