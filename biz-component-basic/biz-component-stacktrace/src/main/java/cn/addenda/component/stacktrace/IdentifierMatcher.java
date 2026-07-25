package cn.addenda.component.stacktrace;

public interface IdentifierMatcher {
  String HASH = "#";

  boolean match(StackTraceElement stackTraceElement);

}
