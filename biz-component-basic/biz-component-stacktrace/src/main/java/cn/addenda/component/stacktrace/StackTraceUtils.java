package cn.addenda.component.stacktrace;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author addenda
 * @since 2023/2/17 9:51
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StackTraceUtils {

  @Getter
  private static final Set<IdentifierMatcher> defaultExcludeSet;

  private static final Pattern ANONYMOUS_INNER_CLASS_PATTERN = Pattern.compile("\\$\\d+");

  private static final String EXCLUDED_PATH = "META-INF/biz-component-stacktrace.excluded";

  static {
    Set<IdentifierMatcher> lines = new HashSet<>();
    try {
      ClassLoader classLoader = StackTraceUtils.class.getClassLoader();
      Enumeration<URL> urls = classLoader.getResources(EXCLUDED_PATH);
      while (urls.hasMoreElements()) {
        URL url = urls.nextElement();
        URLConnection urlConnection = url.openConnection();
        urlConnection.setUseCaches(false);
        try (InputStream inputStream = urlConnection.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
          String line;
          while ((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty()) {
              continue;
            }
            lines.add(IdentifierMatcherFactory.getIdentifierMatcher(line));
          }
        }
      }
    } catch (Exception e) {
      throw new StackTraceException(String.format("Can not open file: [%s].", EXCLUDED_PATH), e);
    }
    defaultExcludeSet = Collections.unmodifiableSet(lines);
  }

  /**
   * 获取调用者信息，返回格式：{@code ClassName#MethodName}
   * <p>如果 {@code useSimpleClassName} 为 {@code true}，则类名不含包名，只输出简写的类名。
   *
   * @param useSimpleClassName           true 表示简写类名（仅类名，不含包路径），false 表示全限定名
   * @param ifExcludeLambda              true 则跳过 lambda 表达式生成的栈帧
   * @param ifExcludeAnonymousInnerClass true 则跳过匿名内部类的栈帧
   * @param excludes                     额外需要排除的类/方法标识（格式见 {@link IdentifierMatcherFactory}）
   * @return 调用者信息，格式：{@code ClassName#MethodName}
   * @see #getDetailedCallerInfo(boolean, boolean, boolean, String...)
   */
  public static String getCallerInfo(
          boolean useSimpleClassName, boolean ifExcludeLambda, boolean ifExcludeAnonymousInnerClass, String... excludes) {
    StackTraceElement stackTraceElement = determineStackTraceElement(ifExcludeLambda, ifExcludeAnonymousInnerClass, excludes);
    String className = stackTraceElement.getClassName();
    String methodName = stackTraceElement.getMethodName();
    if (useSimpleClassName) {
      className = extractSimpleClassName(className);
    }
    return className + "#" + methodName;
  }

  public static String getCallerInfo(boolean useSimpleClassName, boolean ifExcludeLambda, boolean ifExcludeAnonymousInnerClass) {
    return getCallerInfo(useSimpleClassName, ifExcludeLambda, ifExcludeAnonymousInnerClass, (String[]) null);
  }

  public static String getCallerInfo(boolean ifExcludeLambda, boolean ifExcludeAnonymousInnerClass) {
    return getCallerInfo(true, ifExcludeLambda, ifExcludeAnonymousInnerClass);
  }

  public static String getCallerInfo() {
    return getCallerInfo(true, false, false);
  }

  public static String getCallerInfo(String... excludes) {
    return getCallerInfo(true, false, false, excludes);
  }

  /**
   * 获取详细的调用者信息，返回格式：{@code ClassName#MethodName of FileName:LineNumber}
   * <p>相比 {@link #getCallerInfo}，额外包含文件名和行号，可精确到代码行。
   * <p>如果 {@code useSimpleClassName} 为 {@code true}，则类名不含包名，只输出简写的类名。
   *
   * @param useSimpleClassName           true 表示简写类名（仅类名，不含包路径），false 表示全限定名
   * @param ifExcludeLambda              true 则跳过 lambda 表达式生成的栈帧
   * @param ifExcludeAnonymousInnerClass true 则跳过匿名内部类的栈帧
   * @param excludes                     额外需要排除的类/方法标识（格式见 {@link IdentifierMatcherFactory}）
   * @return 详细的调用者信息，格式：{@code ClassName#MethodName of FileName:LineNumber}
   * @see #getCallerInfo(boolean, boolean, boolean, String...)
   */
  public static String getDetailedCallerInfo(
          boolean useSimpleClassName, boolean ifExcludeLambda, boolean ifExcludeAnonymousInnerClass, String... excludes) {
    StackTraceElement stackTraceElement = determineStackTraceElement(ifExcludeLambda, ifExcludeAnonymousInnerClass, excludes);
    String className = stackTraceElement.getClassName();
    String fileName = stackTraceElement.getFileName();
    int lineNumber = stackTraceElement.getLineNumber();
    String methodName = stackTraceElement.getMethodName();
    if (useSimpleClassName) {
      className = extractSimpleClassName(className);
    }
    return String.format("%s of %s:%s", className + "#" + methodName, fileName, lineNumber);
  }

  public static String getDetailedCallerInfo(
          boolean useSimpleClassName, boolean ifExcludeLambda, boolean ifExcludeAnonymousInnerClass) {
    return getDetailedCallerInfo(useSimpleClassName, ifExcludeLambda, ifExcludeAnonymousInnerClass, (String[]) null);
  }

  public static String getDetailedCallerInfo(boolean ifExcludeLambda, boolean ifExcludeAnonymousInnerClass) {
    return getDetailedCallerInfo(true, ifExcludeLambda, ifExcludeAnonymousInnerClass, (String[]) null);
  }

  public static String getDetailedCallerInfo() {
    return getDetailedCallerInfo(true, false, false);
  }

  public static String getDetailedCallerInfo(String... excludes) {
    return getDetailedCallerInfo(true, false, false, excludes);
  }

  private static StackTraceElement determineStackTraceElement(
          boolean ifExcludeLambda, boolean ifExcludeAnonymousInnerClass, String... excludes) {
    Set<IdentifierMatcher> excludeSet = defaultExcludeSet;
    if (excludes != null) {
      excludeSet = new HashSet<>(defaultExcludeSet);
      for (String exclude : excludes) {
        if (exclude != null) {
          excludeSet.add(IdentifierMatcherFactory.getIdentifierMatcher(exclude));
        }
      }
    }
    return determineStackTraceElement(new Throwable().getStackTrace(), excludeSet, ifExcludeLambda, ifExcludeAnonymousInnerClass);
  }

  private static StackTraceElement determineStackTraceElement(
          StackTraceElement[] stackTraceElements, Set<IdentifierMatcher> excludeSet,
          boolean ifExcludeLambda, boolean ifExcludeAnonymousInnerClass) {
    if (stackTraceElements == null || stackTraceElements.length == 0) {
      throw new IllegalArgumentException("The arg `stackTraceElements` can not null and can not be empty.");
    }
    for (StackTraceElement stackTraceElement : stackTraceElements) {
      String methodName = stackTraceElement.getMethodName();
      if (ifExcludeLambda && methodName.contains("lambda$")) {
        continue;
      }
      String className = stackTraceElement.getClassName();
      if (ifExcludeAnonymousInnerClass && ANONYMOUS_INNER_CLASS_PATTERN.matcher(className).find()) {
        continue;
      }
      boolean flag = IdentifierMatcherFactory.match(excludeSet, stackTraceElement);
      if (!flag) {
        return stackTraceElement;
      }
    }
    String a = "All elements of `stackTraceElements` are excluded. The `stackTraceElements` are [%s]. The `excludeSet` are [%s].";
    throw new StackTraceException(String.format(a, toString(stackTraceElements), toString(excludeSet)));
  }

  private static String toString(StackTraceElement[] stackTraceElements) {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < stackTraceElements.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(stackTraceElements[i].toString());
    }
    sb.append("]");
    return sb.toString();
  }

  private static String toString(Set<IdentifierMatcher> excludeSet) {
    StringBuilder sb = new StringBuilder("[");
    boolean first = true;
    for (IdentifierMatcher matcher : excludeSet) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      sb.append(matcher.toString());
    }
    sb.append("]");
    return sb.toString();
  }

  private static String extractSimpleClassName(String className) {
    return className.substring(className.lastIndexOf('.') + 1);
  }

}
