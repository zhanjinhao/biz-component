package cn.addenda.component.common.test.lambda.costed;

public class LogEvent {

  final String level;
  final String message;
  final Throwable throwable;

  LogEvent(String level, String message, Throwable throwable) {
    this.level = level;
    this.message = message;
    this.throwable = throwable;
  }

  public static LogEvent debug(String message) { return new LogEvent("DEBUG", message, null); }
  public static LogEvent error(String message) { return new LogEvent("ERROR", message, null); }
  public static LogEvent error(String message, Throwable t) { return new LogEvent("ERROR", message, t); }
}
