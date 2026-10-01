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

  /**
   * 通配符。标识里出现它只可能生成"永远匹配不上"的 matcher（类名、方法名都不含 {@code *}），直接报错。
   */
  private static final char WILDCARD = '*';

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
   * 解析标识文本，得到对应的 {@link IdentifierMatcher}。
   * <p>支持的标识格式：
   * <ul>
   *   <li>{@code classNamePrefix} —— 不含 {@code #}，按类名<b>文本前缀</b>匹配，例：{@code cn.addenda.}，
   *       可以一次排除整个包及其子包；注意它匹配的是字符串前缀，{@code cn.addenda.} 能匹配 {@code cn.addenda.Foo}
   *       和 {@code cn.addenda.sub.Cls}，不会匹配 {@code cn.addendaX.Foo}；</li>
   *   <li>{@code #className} —— 按类的全限定名精确匹配，例：{@code #cn.addenda.MyClass}；</li>
   *   <li>{@code #className#methodName} —— 按类的全限定名 + 方法名精确匹配，例：{@code #cn.addenda.MyClass#doSomething}。</li>
   * </ul>
   * 其它写法（{@code #}/{@code ##}/{@code #a#}/{@code #a#b#c}、含空白字符、含 {@code *}）一律抛
   * {@link StackTraceException}——这类标识只会"永远匹配不上"，静默失效比报错难查得多。
   * <p>需要按 Class 直接构造时用 {@link #classMatcher(Class)}、{@link #classAndMethodMatcher(Class, String)}、
   * {@link #prefixMatcher(Class)}。
   *
   * @param identifier 标识文本，不能为 null 或空
   * @throws StackTraceException identifier 为 null、为空或格式非法时抛出
   */
  public static IdentifierMatcher getIdentifierMatcher(String identifier) {
    if (identifier == null || identifier.isEmpty()) {
      throw invalidIdentifier(identifier);
    }
    return identifierMatcherMap.computeIfAbsent(identifier, IdentifierMatcherFactory::createIdentifierMatcher);
  }

  private static IdentifierMatcher createIdentifierMatcher(String identifier) {
    // 类名、方法名都不可能含空白字符或 *，出现它们只会生成"永远匹配不上"的 matcher
    if (containsWhitespace(identifier) || identifier.indexOf(WILDCARD) >= 0) {
      throw invalidIdentifier(identifier);
    }

    if (!identifier.startsWith(HASH)) {
      // 不带 # 就是类名前缀匹配，例如 cn.addenda. 能匹配 cn.addenda.MyClass 和 cn.addenda.sub.Cls
      return prefixMatcher(identifier);
    }

    int methodHash = identifier.indexOf(HASH, HASH.length());
    if (methodHash < 0) {
      // #className
      String className = identifier.substring(HASH.length());
      if (className.isEmpty()) {
        throw invalidIdentifier(identifier);
      }
      return classMatcher(className);
    }

    // #className#methodName
    String className = identifier.substring(HASH.length(), methodHash);
    String methodName = identifier.substring(methodHash + HASH.length());
    if (className.isEmpty() || methodName.isEmpty() || methodName.indexOf(HASH) >= 0) {
      throw invalidIdentifier(identifier);
    }
    return classAndMethodMatcher(className, methodName);
  }

  private static StackTraceException invalidIdentifier(String identifier) {
    return new StackTraceException(String.format("Invalid identifier: [%s]. The supported formats are "
            + "[classNamePrefix] (prefix, e.g. [cn.a.]), [#className] (exact) and [#className#methodName] (exact).",
            identifier));
  }

  private static boolean containsWhitespace(String identifier) {
    for (int i = 0; i < identifier.length(); i++) {
      if (Character.isWhitespace(identifier.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * 按类的全限定名精确匹配。
   */
  public static IdentifierMatcher classMatcher(String className) {
    return new ClassNameMatcher(className);
  }

  /**
   * 按类的全限定名精确匹配。
   */
  public static IdentifierMatcher classMatcher(Class<?> clazz) {
    return classMatcher(clazz.getName());
  }

  /**
   * 按类的全限定名 + 方法名精确匹配。
   */
  public static IdentifierMatcher classAndMethodMatcher(String className, String methodName) {
    return new ClassNameAndMethodNameMatcher(className, methodName);
  }

  /**
   * 按类的全限定名 + 方法名精确匹配。
   */
  public static IdentifierMatcher classAndMethodMatcher(Class<?> clazz, String methodName) {
    return classAndMethodMatcher(clazz.getName(), methodName);
  }

  /**
   * 按类名文本前缀匹配。
   * <p>注意是字符串前缀，不是"包"语义：{@code cn.a.} 能匹配 {@code cn.a.Foo}，不会匹配 {@code cn.ab.Foo}。
   */
  public static IdentifierMatcher prefixMatcher(String classNamePrefix) {
    return new ClassNamePrefixMatcher(classNamePrefix);
  }

  /**
   * 按类名文本前缀匹配，即该类本身以及同前缀的类（含内部类，如 {@code Outer$Inner}）。
   */
  public static IdentifierMatcher prefixMatcher(Class<?> clazz) {
    return prefixMatcher(clazz.getName());
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
