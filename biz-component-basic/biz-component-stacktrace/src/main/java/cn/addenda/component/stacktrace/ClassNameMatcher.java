package cn.addenda.component.stacktrace;

import java.util.Objects;

public class ClassNameMatcher implements IdentifierMatcher {

  private final String className;

  public ClassNameMatcher(String className) {
    if (className == null || className.isEmpty()) {
      throw new StackTraceException("className can not be null or empty.");
    }
    this.className = className;
  }

  @Override
  public boolean match(StackTraceElement stackTraceElement) {
    return className.equals(stackTraceElement.getClassName());
  }

  @Override
  public String toString() {
    return "ClassNameMatcher{" +
            "className='" + className + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClassNameMatcher that = (ClassNameMatcher) o;
    return Objects.equals(className, that.className);
  }

  @Override
  public int hashCode() {
    return Objects.hash(className);
  }
}
