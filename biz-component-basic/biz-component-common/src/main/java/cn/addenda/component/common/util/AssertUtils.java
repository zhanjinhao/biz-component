package cn.addenda.component.common.util;

import cn.addenda.component.common.util.collection.ArrayUtils;
import cn.addenda.component.common.util.collection.CollectionUtils;
import cn.addenda.component.common.util.string.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 断言工具类，辅助验证方法参数。
 * <p>
 * 对标 Spring 的 {@code org.springframework.util.Assert}，
 * 用于在运行时尽早、清晰地暴露编程错误。
 * <p>
 * 典型用法：
 * <pre>{@code
 * AssertUtils.notNull(clazz, "clazz 不能为 null");
 * AssertUtils.isTrue(i > 0, "i 必须大于 0");
 * }</pre>
 *
 * @author addenda
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssertUtils {

  // ==================== state ====================

  /**
   * 断言布尔表达式为 {@code true}，失败时抛出 {@link IllegalStateException}。
   * <p>若期望抛出 {@link IllegalArgumentException}，请使用 {@link #isTrue(boolean, String)}。
   * <pre>{@code
   * AssertUtils.state(id == null, "id 属性必须尚未初始化");
   * }</pre>
   *
   * @param expression 布尔表达式
   * @param message    断言失败时的异常消息
   * @throws IllegalStateException 表达式为 {@code false} 时抛出
   */
  public static void state(boolean expression, String message) {
    if (!expression) {
      throw new IllegalStateException(message);
    }
  }

  /**
   * 断言布尔表达式为 {@code true}，失败时抛出 {@link IllegalStateException}。
   * <p>消息通过 {@link Supplier} 延迟构建，避免不必要的字符串拼接。
   *
   * @param expression      布尔表达式
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalStateException 表达式为 {@code false} 时抛出
   */
  public static void state(boolean expression, Supplier<String> messageSupplier) {
    if (!expression) {
      throw new IllegalStateException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== isTrue ====================

  /**
   * 断言布尔表达式为 {@code true}，失败时抛出 {@link IllegalArgumentException}。
   * <pre>{@code
   * AssertUtils.isTrue(i > 0, "i 必须大于 0");
   * }</pre>
   *
   * @param expression 布尔表达式
   * @param message    断言失败时的异常消息
   * @throws IllegalArgumentException 表达式为 {@code false} 时抛出
   */
  public static void isTrue(boolean expression, String message) {
    if (!expression) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 断言布尔表达式为 {@code true}，失败时抛出 {@link IllegalArgumentException}。
   * <p>消息通过 {@link Supplier} 延迟构建。
   * <pre>{@code
   * AssertUtils.isTrue(i > 0, () -> "值 " + i + " 必须大于 0");
   * }</pre>
   *
   * @param expression      布尔表达式
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 表达式为 {@code false} 时抛出
   */
  public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
    if (!expression) {
      throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== isNull ====================

  /**
   * 断言对象为 {@code null}。
   * <pre>{@code
   * AssertUtils.isNull(value, "value 必须为 null");
   * }</pre>
   *
   * @param object  待检查的对象
   * @param message 断言失败时的异常消息
   * @throws IllegalArgumentException 对象不为 {@code null} 时抛出
   */
  public static void isNull(Object object, String message) {
    if (object != null) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 断言对象为 {@code null}。
   * <p>消息通过 {@link Supplier} 延迟构建。
   *
   * @param object          待检查的对象
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 对象不为 {@code null} 时抛出
   */
  public static void isNull(Object object, Supplier<String> messageSupplier) {
    if (object != null) {
      throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== notNull ====================

  /**
   * 断言对象不为 {@code null}。
   * <pre>{@code
   * AssertUtils.notNull(clazz, "clazz 不能为 null");
   * }</pre>
   *
   * @param object  待检查的对象
   * @param message 断言失败时的异常消息
   * @throws IllegalArgumentException 对象为 {@code null} 时抛出
   */
  public static void notNull(Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 断言对象不为 {@code null}。
   * <p>消息通过 {@link Supplier} 延迟构建。
   * <pre>{@code
   * AssertUtils.notNull(entity.getId(), () -> "实体 " + entity.getName() + " 的 ID 不能为 null");
   * }</pre>
   *
   * @param object          待检查的对象
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 对象为 {@code null} 时抛出
   */
  public static void notNull(Object object, Supplier<String> messageSupplier) {
    if (object == null) {
      throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== hasLength ====================

  /**
   * 断言字符串不为空，即不能为 {@code null} 且不能为空字符串 {@code ""}。
   * <pre>{@code
   * AssertUtils.hasLength(name, "name 不能为空");
   * }</pre>
   *
   * @param text    待检查的字符串
   * @param message 断言失败时的异常消息
   * @throws IllegalArgumentException 字符串为 {@code null} 或空字符串时抛出
   * @see StringUtils#hasLength(CharSequence)
   */
  public static void hasLength(String text, String message) {
    if (!StringUtils.hasLength(text)) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 断言字符串不为空，即不能为 {@code null} 且不能为空字符串 {@code ""}。
   * <p>消息通过 {@link Supplier} 延迟构建。
   *
   * @param text            待检查的字符串
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 字符串为 {@code null} 或空字符串时抛出
   * @see StringUtils#hasLength(CharSequence)
   */
  public static void hasLength(String text, Supplier<String> messageSupplier) {
    if (!StringUtils.hasLength(text)) {
      throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== hasText ====================

  /**
   * 断言字符串包含有效文本内容，即不能为 {@code null} 且必须至少包含一个非空白字符。
   * <pre>{@code
   * AssertUtils.hasText(name, "name 不能为空");
   * }</pre>
   *
   * @param text    待检查的字符串
   * @param message 断言失败时的异常消息
   * @throws IllegalArgumentException 字符串不包含有效文本内容时抛出
   * @see StringUtils#hasText(CharSequence)
   */
  public static void hasText(String text, String message) {
    if (!StringUtils.hasText(text)) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 断言字符串包含有效文本内容，即不能为 {@code null} 且必须至少包含一个非空白字符。
   * <p>消息通过 {@link Supplier} 延迟构建。
   *
   * @param text            待检查的字符串
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 字符串不包含有效文本内容时抛出
   * @see StringUtils#hasText(CharSequence)
   */
  public static void hasText(String text, Supplier<String> messageSupplier) {
    if (!StringUtils.hasText(text)) {
      throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== doesNotContain ====================

  /**
   * 断言文本中不包含指定的子串。
   * <pre>{@code
   * AssertUtils.doesNotContain(name, "rod", "name 不能包含 'rod'");
   * }</pre>
   *
   * @param textToSearch 待搜索的文本
   * @param substring    要查找的子串
   * @param message      断言失败时的异常消息
   * @throws IllegalArgumentException 文本包含指定子串时抛出
   */
  public static void doesNotContain(String textToSearch, String substring, String message) {
    if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
            && textToSearch.contains(substring)) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 断言文本中不包含指定的子串。
   * <p>消息通过 {@link Supplier} 延迟构建。
   *
   * @param textToSearch    待搜索的文本
   * @param substring       要查找的子串
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 文本包含指定子串时抛出
   */
  public static void doesNotContain(String textToSearch, String substring, Supplier<String> messageSupplier) {
    if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
            && textToSearch.contains(substring)) {
      throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== notEmpty (数组) ====================

  /**
   * 断言数组包含元素，即不能为 {@code null} 且长度必须大于 0。
   * <pre>{@code
   * AssertUtils.notEmpty(array, "array 必须包含元素");
   * }</pre>
   *
   * @param array   待检查的数组
   * @param message 断言失败时的异常消息
   * @throws IllegalArgumentException 数组为 {@code null} 或不包含元素时抛出
   */
  public static void notEmpty(Object[] array, String message) {
    if (ArrayUtils.isEmpty(array)) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 断言数组包含元素，即不能为 {@code null} 且长度必须大于 0。
   * <p>消息通过 {@link Supplier} 延迟构建。
   *
   * @param array           待检查的数组
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 数组为 {@code null} 或不包含元素时抛出
   */
  public static void notEmpty(Object[] array, Supplier<String> messageSupplier) {
    if (ArrayUtils.isEmpty(array)) {
      throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== noNullElements (数组) ====================

  /**
   * 断言数组中不包含 {@code null} 元素。
   * <p>注意：数组为空时不会抛出异常！
   * <pre>{@code
   * AssertUtils.noNullElements(array, "array 不能包含 null 元素");
   * }</pre>
   *
   * @param array   待检查的数组
   * @param message 断言失败时的异常消息
   * @throws IllegalArgumentException 数组包含 {@code null} 元素时抛出
   */
  public static void noNullElements(Object[] array, String message) {
    if (array != null) {
      for (Object element : array) {
        if (element == null) {
          throw new IllegalArgumentException(message);
        }
      }
    }
  }

  /**
   * 断言数组中不包含 {@code null} 元素。
   * <p>注意：数组为空时不会抛出异常！消息通过 {@link Supplier} 延迟构建。
   *
   * @param array           待检查的数组
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 数组包含 {@code null} 元素时抛出
   */
  public static void noNullElements(Object[] array, Supplier<String> messageSupplier) {
    if (array != null) {
      for (Object element : array) {
        if (element == null) {
          throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
      }
    }
  }

  // ==================== notEmpty (集合) ====================

  /**
   * 断言集合包含元素，即不能为 {@code null} 且必须至少包含一个元素。
   * <pre>{@code
   * AssertUtils.notEmpty(collection, "collection 必须包含元素");
   * }</pre>
   *
   * @param collection 待检查的集合
   * @param message    断言失败时的异常消息
   * @throws IllegalArgumentException 集合为 {@code null} 或不包含元素时抛出
   */
  public static void notEmpty(Collection<?> collection, String message) {
    if (CollectionUtils.isEmpty(collection)) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 断言集合包含元素，即不能为 {@code null} 且必须至少包含一个元素。
   * <p>消息通过 {@link Supplier} 延迟构建。
   *
   * @param collection      待检查的集合
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 集合为 {@code null} 或不包含元素时抛出
   */
  public static void notEmpty(Collection<?> collection, Supplier<String> messageSupplier) {
    if (CollectionUtils.isEmpty(collection)) {
      throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== noNullElements (集合) ====================

  /**
   * 断言集合中不包含 {@code null} 元素。
   * <p>注意：集合为空时不会抛出异常！
   * <pre>{@code
   * AssertUtils.noNullElements(collection, "collection 不能包含 null 元素");
   * }</pre>
   *
   * @param collection 待检查的集合
   * @param message    断言失败时的异常消息
   * @throws IllegalArgumentException 集合包含 {@code null} 元素时抛出
   */
  public static void noNullElements(Collection<?> collection, String message) {
    if (collection != null) {
      for (Object element : collection) {
        if (element == null) {
          throw new IllegalArgumentException(message);
        }
      }
    }
  }

  /**
   * 断言集合中不包含 {@code null} 元素。
   * <p>注意：集合为空时不会抛出异常！消息通过 {@link Supplier} 延迟构建。
   *
   * @param collection      待检查的集合
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 集合包含 {@code null} 元素时抛出
   */
  public static void noNullElements(Collection<?> collection, Supplier<String> messageSupplier) {
    if (collection != null) {
      for (Object element : collection) {
        if (element == null) {
          throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
      }
    }
  }

  // ==================== notEmpty (Map) ====================

  /**
   * 断言 Map 包含条目，即不能为 {@code null} 且必须至少包含一个条目。
   * <pre>{@code
   * AssertUtils.notEmpty(map, "map 必须包含条目");
   * }</pre>
   *
   * @param map     待检查的 Map
   * @param message 断言失败时的异常消息
   * @throws IllegalArgumentException Map 为 {@code null} 或不包含条目时抛出
   */
  public static void notEmpty(Map<?, ?> map, String message) {
    if (map == null || map.isEmpty()) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 断言 Map 包含条目，即不能为 {@code null} 且必须至少包含一个条目。
   * <p>消息通过 {@link Supplier} 延迟构建。
   *
   * @param map             待检查的 Map
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException Map 为 {@code null} 或不包含条目时抛出
   */
  public static void notEmpty(Map<?, ?> map, Supplier<String> messageSupplier) {
    if (map == null || map.isEmpty()) {
      throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }
  }

  // ==================== isInstanceOf ====================

  /**
   * 断言对象是给定类型的实例。
   * <pre>{@code
   * AssertUtils.isInstanceOf(Foo.class, foo, "期望 Foo 类型");
   * }</pre>
   *
   * @param type 目标类型
   * @param obj  待检查的对象
   * @throws IllegalArgumentException 对象不是目标类型的实例时抛出
   */
  public static void isInstanceOf(Class<?> type, Object obj) {
    isInstanceOf(type, obj, "");
  }

  /**
   * 断言对象是给定类型的实例。
   * <pre>{@code
   * AssertUtils.isInstanceOf(Foo.class, foo, "期望 Foo 类型");
   * }</pre>
   *
   * @param type    目标类型
   * @param obj     待检查的对象
   * @param message 断言失败时的异常消息前缀。
   *                若为空或以 ":" ";" "," "." 结尾，则自动追加完整异常信息；
   *                若以空格结尾，则追加异常对象的类型名；
   *                其他情况，追加 ": " 和异常对象的类型名。
   * @throws IllegalArgumentException 对象不是目标类型的实例时抛出
   */
  public static void isInstanceOf(Class<?> type, Object obj, String message) {
    notNull(type, "type 不能为 null");
    if (!type.isInstance(obj)) {
      instanceCheckFailed(type, obj, message);
    }
  }

  /**
   * 断言对象是给定类型的实例。
   * <p>消息通过 {@link Supplier} 延迟构建。
   *
   * @param type            目标类型
   * @param obj             待检查的对象
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 对象不是目标类型的实例时抛出
   */
  public static void isInstanceOf(Class<?> type, Object obj, Supplier<String> messageSupplier) {
    notNull(type, "type 不能为 null");
    if (!type.isInstance(obj)) {
      instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
    }
  }

  // ==================== isAssignable ====================

  /**
   * 断言 {@code superType.isAssignableFrom(subType)} 为 {@code true}。
   * <pre>{@code
   * AssertUtils.isAssignable(Number.class, myClass);
   * }</pre>
   *
   * @param superType 父类型
   * @param subType   子类型
   * @throws IllegalArgumentException 类型不可赋值时抛出
   */
  public static void isAssignable(Class<?> superType, Class<?> subType) {
    isAssignable(superType, subType, "");
  }

  /**
   * 断言 {@code superType.isAssignableFrom(subType)} 为 {@code true}。
   * <pre>{@code
   * AssertUtils.isAssignable(Number.class, myClass, "期望 Number 类型");
   * }</pre>
   *
   * @param superType 父类型
   * @param subType   子类型
   * @param message   断言失败时的异常消息前缀。
   *                  若为空或以 ":" ";" "," "." 结尾，则自动追加完整异常信息；
   *                  若以空格结尾，则追加异常子类型的类名；
   *                  其他情况，追加 ": " 和异常子类型的类名。
   * @throws IllegalArgumentException 类型不可赋值时抛出
   */
  public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
    notNull(superType, "superType 不能为 null");
    if (subType == null || !superType.isAssignableFrom(subType)) {
      assignableCheckFailed(superType, subType, message);
    }
  }

  /**
   * 断言 {@code superType.isAssignableFrom(subType)} 为 {@code true}。
   * <p>消息通过 {@link Supplier} 延迟构建。
   *
   * @param superType       父类型
   * @param subType         子类型
   * @param messageSupplier 断言失败时异常消息的提供者
   * @throws IllegalArgumentException 类型不可赋值时抛出
   */
  public static void isAssignable(Class<?> superType, Class<?> subType, Supplier<String> messageSupplier) {
    notNull(superType, "superType 不能为 null");
    if (subType == null || !superType.isAssignableFrom(subType)) {
      assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
    }
  }

  // ==================== 私有辅助方法 ====================

  private static String nullSafeGet(Supplier<String> messageSupplier) {
    return (messageSupplier != null ? messageSupplier.get() : null);
  }

  private static void instanceCheckFailed(Class<?> type, Object obj, String message) {
    String className = (obj != null ? obj.getClass().getName() : "null");
    String result = "";
    boolean defaultMessage = true;
    if (StringUtils.hasLength(message)) {
      if (endsWithSeparator(message)) {
        result = message + " ";
      } else {
        result = messageWithTypeName(message, className);
        defaultMessage = false;
      }
    }
    if (defaultMessage) {
      result = result + ("期望类型 [" + type.getName() + "] 的对象，但实际类型是 [" + className + "]");
    }
    throw new IllegalArgumentException(result);
  }

  private static void assignableCheckFailed(Class<?> superType, Class<?> subType, String message) {
    String subTypeName = (subType != null ? subType.getName() : "null");
    String result = "";
    boolean defaultMessage = true;
    if (StringUtils.hasLength(message)) {
      if (endsWithSeparator(message)) {
        result = message + " ";
      } else {
        result = messageWithTypeName(message, subTypeName);
        defaultMessage = false;
      }
    }
    if (defaultMessage) {
      result = result + ("期望 [" + (subType != null ? subType.getName() : "null")
              + "] 是 [" + superType.getName() + "] 的子类");
    }
    throw new IllegalArgumentException(result);
  }

  private static boolean endsWithSeparator(String msg) {
    return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
  }

  private static String messageWithTypeName(String msg, String typeName) {
    return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
  }
}
