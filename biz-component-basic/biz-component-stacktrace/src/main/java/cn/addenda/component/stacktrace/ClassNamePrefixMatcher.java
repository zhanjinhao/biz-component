package cn.addenda.component.stacktrace;

import java.util.Objects;

public class ClassNamePrefixMatcher implements IdentifierMatcher {

  private final String classNamePrefix;

  public ClassNamePrefixMatcher(String classNamePrefix) {
    if (classNamePrefix == null || classNamePrefix.isEmpty()) {
      throw new StackTraceException("classNamePrefix can not be null or empty.");
    }
    this.classNamePrefix = classNamePrefix;
  }

  @Override
  public boolean match(StackTraceElement stackTraceElement) {
    return stackTraceElement.getClassName().startsWith(classNamePrefix);
  }

  @Override
  public String toString() {
    return "ClassNamePrefixMatcher{" +
            "classNamePrefix='" + classNamePrefix + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClassNamePrefixMatcher that = (ClassNamePrefixMatcher) o;
    return Objects.equals(classNamePrefix, that.classNamePrefix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classNamePrefix);
  }
}
