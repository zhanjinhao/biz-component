package cn.addenda.component.stacktrace;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static cn.addenda.component.stacktrace.IdentifierMatcher.HASH;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdentifierMatcherFactory {


  private static final Map<String, IdentifierMatcher> identifierMatcherMap = new ConcurrentHashMap<>();

  public static boolean match(Set<IdentifierMatcher> identifierMatcherSet, StackTraceElement stackTraceElement) {
    for (IdentifierMatcher identifierMatcher : identifierMatcherSet) {
      if (identifierMatcher.match(stackTraceElement)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 获取IdentifierMatcher
   */
  public static IdentifierMatcher getIdentifierMatcher(String identifier) {
    return identifierMatcherMap.computeIfAbsent(identifier,
            s -> {
              if (!identifier.startsWith(HASH)) {
                return new ClassNamePrefixMatcher(identifier);
              }

              String[] split = identifier.substring(1).split(HASH);
              if (split.length == 1) {
                return new ClassNameMatcher(split[0]);
              }
              return new ClassNameAndMethodNameMatcher(split[0], split[1]);
            });
  }

  /**
   * 获取IdentifierMatcher
   */
  public static IdentifierMatcher getIdentifierMatcher(Class<?> clazz) {
    return getIdentifierMatcher(withHash(clazz.getName()));
  }

  /**
   * 获取IdentifierMatcher
   */
  public static IdentifierMatcher getIdentifierMatcher(Class<?> clazz, boolean ifPrefix) {
    if (ifPrefix) {
      return getIdentifierMatcher(clazz.getName());
    }
    return getIdentifierMatcher(withHash(clazz.getName()));
  }

  /**
   * 给类名称加上#
   */
  public static String withHash(String className) {
    return HASH + className;
  }

  /**
   * 给类名称和方法名称加上#
   */
  public static String withHash(String className, String methodName) {
    return HASH + className + HASH + methodName;
  }

  /**
   * 给类加上#
   */
  public static String withHash(Class<?> clazz) {
    return withHash(clazz.getName());
  }

  /**
   * 给类和方法加上#
   */
  public static String withHash(Class<?> clazz, String methodName) {
    return withHash(clazz.getName(), methodName);
  }

  /**
   * 给类和方法加上#
   */
  public static String withHash(Method method) {
    return withHash(method.getDeclaringClass(), method.getName());
  }

}
