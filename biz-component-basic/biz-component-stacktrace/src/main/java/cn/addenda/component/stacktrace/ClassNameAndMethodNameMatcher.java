package cn.addenda.component.stacktrace;

import java.util.Objects;

public class ClassNameAndMethodNameMatcher implements IdentifierMatcher {

  private final String className;

  private final String methodName;

  public ClassNameAndMethodNameMatcher(String className, String methodName) {
    if (className == null || className.isEmpty()) {
      throw new StackTraceException("className can not be null or empty.");
    }
    if (methodName == null || methodName.isEmpty()) {
      throw new StackTraceException("methodName can not be null or empty.");
    }
    this.className = className;
    this.methodName = methodName;
  }

  @Override
  public boolean match(StackTraceElement stackTraceElement) {
    return className.equals(stackTraceElement.getClassName()) && methodName.equals(stackTraceElement.getMethodName());
  }

  @Override
  public String toString() {
    return "ClassNameAndMethodNameMatcher{" +
            "className='" + className + '\'' +
            ", methodName='" + methodName + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClassNameAndMethodNameMatcher that = (ClassNameAndMethodNameMatcher) o;
    return Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(className, methodName);
  }
}
