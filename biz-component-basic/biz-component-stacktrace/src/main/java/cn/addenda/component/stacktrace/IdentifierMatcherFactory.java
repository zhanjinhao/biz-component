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
   * <p>支持的标识格式：
   * <ul>
   *   <li>{@code classNamePrefix} —— 不含 {@code #}，按类名前缀匹配；</li>
   *   <li>{@code #className} —— 按类的全限定名精确匹配；</li>
   *   <li>{@code #className#methodName} —— 按类的全限定名 + 方法名精确匹配。</li>
   * </ul>
   *
   * @param identifier 标识，不能为 null 或空
   * @throws StackTraceException identifier 为 null、为空或格式非法时抛出
   */
  public static IdentifierMatcher getIdentifierMatcher(String identifier) {
    if (identifier == null || identifier.isEmpty()) {
      throw new StackTraceException("identifier can not be null or empty.");
    }
    return identifierMatcherMap.computeIfAbsent(identifier, IdentifierMatcherFactory::createIdentifierMatcher);
  }

  private static IdentifierMatcher createIdentifierMatcher(String identifier) {
    if (!identifier.startsWith(HASH)) {
      return new ClassNamePrefixMatcher(identifier);
    }

    int methodHash = identifier.indexOf(HASH, HASH.length());
    if (methodHash < 0) {
      // #className
      String className = identifier.substring(HASH.length());
      if (className.isEmpty()) {
        throw invalidIdentifier(identifier);
      }
      return new ClassNameMatcher(className);
    }

    // #className#methodName
    String className = identifier.substring(HASH.length(), methodHash);
    String methodName = identifier.substring(methodHash + HASH.length());
    if (className.isEmpty() || methodName.isEmpty() || methodName.indexOf(HASH) >= 0) {
      throw invalidIdentifier(identifier);
    }
    return new ClassNameAndMethodNameMatcher(className, methodName);
  }

  private static StackTraceException invalidIdentifier(String identifier) {
    return new StackTraceException(String.format("Invalid identifier: [%s]. "
            + "The supported formats are [classNamePrefix], [#className] and [#className#methodName].", identifier));
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
