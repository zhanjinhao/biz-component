package cn.addenda.component.common.test.lambda.costed;

import org.slf4j.helpers.MarkerIgnoringBase;

import java.util.ArrayList;
import java.util.List;

public class TestLogger extends MarkerIgnoringBase {

  boolean debugEnabled = true;
  final List<LogEvent> events = new ArrayList<>();

  public TestLogger(String name) {
    this.name = name;
  }

  @Override public boolean isDebugEnabled() { return debugEnabled; }
  @Override public boolean isErrorEnabled() { return true; }
  @Override public boolean isWarnEnabled() { return true; }
  @Override public boolean isInfoEnabled() { return true; }
  @Override public boolean isTraceEnabled() { return false; }

  @Override public void debug(String msg) { events.add(LogEvent.debug(msg)); }
  @Override public void error(String msg) { events.add(LogEvent.error(msg)); }
  @Override public void error(String msg, Throwable t) { events.add(LogEvent.error(msg, t)); }

  @Override public void debug(String f, Object a) { }
  @Override public void debug(String f, Object a1, Object a2) { }
  @Override public void debug(String f, Object... a) { }
  @Override public void debug(String m, Throwable t) { }
  @Override public void error(String f, Object a) { }
  @Override public void error(String f, Object a1, Object a2) { }
  @Override public void error(String f, Object... a) { }
  @Override public void info(String msg) { }
  @Override public void info(String f, Object a) { }
  @Override public void info(String f, Object a1, Object a2) { }
  @Override public void info(String f, Object... a) { }
  @Override public void info(String m, Throwable t) { }
  @Override public void warn(String msg) { }
  @Override public void warn(String f, Object a) { }
  @Override public void warn(String f, Object a1, Object a2) { }
  @Override public void warn(String f, Object... a) { }
  @Override public void warn(String m, Throwable t) { }
  @Override public void trace(String msg) { }
  @Override public void trace(String f, Object a) { }
  @Override public void trace(String f, Object a1, Object a2) { }
  @Override public void trace(String f, Object... a) { }
  @Override public void trace(String m, Throwable t) { }
}
