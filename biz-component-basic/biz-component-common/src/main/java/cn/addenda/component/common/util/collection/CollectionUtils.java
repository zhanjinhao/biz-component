package cn.addenda.component.common.util.collection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author addenda
 * @since 2022/2/7 12:37
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

  // ==================== isEmpty ====================

  /**
   * 集合是否为空（{@code null} 或 size == 0）。
   *
   * @param collection 集合，可为 {@code null}
   * @return 是否为 {@code null} 或空集合
   */
  public static boolean isEmpty(final Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  // ==================== orEmpty ====================

  /**
   * {@code null} 转为空集合，非 null 原样返回。
   *
   * @param collection 集合，可为 {@code null}
   * @param <T>        元素类型
   * @return 非{@code null} 集合，原集合为 {@code null} 时返回空 {@link ArrayList}
   */
  public static <T> Collection<T> orEmpty(final Collection<T> collection) {
    return collection == null ? new ArrayList<>() : collection;
  }

  // ==================== concat ====================

  /**
   * 拼接多个集合，返回新集合，不修改原集合。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个集合，可为 {@code null}
   * @param rest  其余集合，可为 {@code null}
   * @param <T>   元素类型
   * @return 拼接后的新 {@link ArrayList}；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  @SafeVarargs
  public static <T> Collection<T> concat(Collection<T> first, Collection<T>... rest) {
    boolean hasNonNull = first != null;
    ArrayList<T> result = new ArrayList<>();
    if (first != null) {
      result.addAll(first);
    }
    for (Collection<T> c : rest) {
      if (c != null) {
        hasNonNull = true;
        result.addAll(c);
      }
    }
    return hasNonNull ? result : null;
  }

  // ==================== subCollection ====================

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原集合。
   *
   * @param list 原列表，{@code null} 返回 {@code null}
   * @param from 起始索引（含）
   * @param to   结束索引（不含）
   * @param <T>  元素类型
   * @return 截取后的子列表；原列表为 {@code null} 时返回 {@code null}
   */
  public static <T> List<T> subCollection(List<T> list, int from, int to) {
    if (list == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, list.size());
    if (from >= to) return new ArrayList<>();
    return new ArrayList<>(list.subList(from, to));
  }

  // ==================== withoutAt ====================

  /**
   * 返回不含指定索引元素的新列表，不修改原列表。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原列表副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code list} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param list    原列表，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @param <T>     元素类型
   * @return 不含指定索引元素的新列表；原列表为 {@code null} 时返回 {@code null}
   */
  public static <T> List<T> withoutAt(List<T> list, int... indices) {
    if (list == null) return null;
    if (indices == null || indices.length == 0) {
      return new ArrayList<>(list);
    }
    boolean[] mask = new boolean[list.size()];
    for (int idx : indices) {
      if (idx >= 0 && idx < list.size()) {
        mask[idx] = true;
      }
    }
    List<T> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      if (!mask[i]) {
        result.add(list.get(i));
      }
    }
    return result;
  }

  // ==================== withoutElement ====================

  /**
   * 返回不含指定值元素的新集合（各值仅删首次匹配），不修改原集合。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原集合副本</li>
   *   <li>未命中不报错，返回原集合副本</li>
   *   <li>{@code null} 值通过 {@code Objects.equals} 匹配</li>
   *   <li>{@code collection} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param collection 原集合，可为 {@code null}
   * @param elements   要排除的值，可为 {@code null}
   * @param <T>        元素类型
   * @return 不含指定值元素的新集合；原集合为 {@code null} 时返回 {@code null}
   */
  @SafeVarargs
  public static <T> Collection<T> withoutElement(Collection<T> collection, T... elements) {
    if (collection == null) return null;
    if (elements == null || elements.length == 0) {
      return new ArrayList<>(collection);
    }
    List<T> list = new ArrayList<>(collection);
    boolean[] mask = new boolean[list.size()];
    for (T elem : elements) {
      for (int i = 0; i < list.size(); i++) {
        if (!mask[i] && Objects.equals(elem, list.get(i))) {
          mask[i] = true;
          break;
        }
      }
    }
    List<T> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      if (!mask[i]) {
        result.add(list.get(i));
      }
    }
    return result;
  }

  // ==================== elementsAt：返回指定索引的元素 ====================

  /**
   * 返回指定索引处的元素组成的新列表，不修改原列表。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空列表</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code list} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param list    原列表，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @param <T>     元素类型
   * @return 指定索引处的元素组成的新列表；原列表为 {@code null} 时返回 {@code null}
   */
  public static <T> List<T> elementsAt(List<T> list, int... indices) {
    if (list == null) return null;
    if (indices == null || indices.length == 0) return new ArrayList<>();
    boolean[] mask = new boolean[list.size()];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < list.size() && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    List<T> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      if (mask[i]) result.add(list.get(i));
    }
    return result;
  }

  // ==================== elementsIn：返回匹配指定值的元素 ====================

  /**
   * 返回匹配指定值的元素组成的新集合（各值仅取首次匹配），不修改原集合。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空列表</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code null} 值通过 {@code Objects.equals} 匹配</li>
   *   <li>{@code collection} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param collection 原集合，可为 {@code null}
   * @param values     要匹配的值，可为 {@code null}
   * @param <T>        元素类型
   * @return 匹配的元素组成的新集合；原集合为 {@code null} 时返回 {@code null}
   */
  @SafeVarargs
  public static <T> Collection<T> elementsIn(Collection<T> collection, T... values) {
    if (collection == null) return null;
    if (values == null || values.length == 0) return new ArrayList<>();
    boolean[] mask = new boolean[values.length];
    List<T> result = new ArrayList<>();
    for (T item : collection) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && Objects.equals(values[j], item)) {
          mask[j] = true;
          result.add(item);
          break;
        }
      }
    }
    return result;
  }

  // ==================== reverse ====================

  /**
   * 反转返回新列表。不修改原列表。
   *
   * @param list 原列表，{@code null} 返回 {@code null}
   * @param <T>  元素类型
   * @return 反转后的新列表；原列表为 {@code null} 时返回 {@code null}
   */
  public static <T> List<T> reverse(List<T> list) {
    if (list == null) return null;
    List<T> result = new ArrayList<>(list);
    Collections.reverse(result);
    return result;
  }

  // ==================== reverseSelf ====================

  /**
   * 原地反转，修改原列表。null 安全。
   *
   * @param list 要反转的列表，{@code null} 不处理
   * @param <T>  元素类型
   */
  public static <T> void reverseSelf(List<T> list) {
    if (list == null) return;
    Collections.reverse(list);
  }

  // ==================== join ====================

  /**
   * 用分隔符串联集合元素。
   * <ul>
   *   <li>{@code collection} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code collection} 为空集合 → 返回 {@code ""}</li>
   *   <li>{@code collection} 中 {@code null} 元素 → 输出 {@code "null"}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter  分隔符，可为 {@code null}
   * @param collection 原集合，可为 {@code null}
   * @return 串联后的字符串；原集合为 {@code null} 时返回 {@code null}，空集合返回 {@code ""}
   */
  public static String join(String delimiter, Collection<?> collection) {
    if (collection == null) return null;
    if (collection.isEmpty()) return "";
    StringBuilder sb = new StringBuilder();
    int i = 0;
    for (Object item : collection) {
      if (i > 0) sb.append(delimiter);
      sb.append(item);
      i++;
    }
    return sb.toString();
  }

  // ==================== firstIndexOf ====================

  /**
   * 查找首次出现下标（null 安全）。使用 Objects.equals 匹配。
   *
   * @param list  列表，{@code null} 返回 -1
   * @param value 要查找的值
   * @param <T>   元素类型
   * @return 首次出现的下标，列表为 {@code null} 或未找到时返回 {@code -1}
   */
  public static <T> int firstIndexOf(List<T> list, T value) {
    if (list == null) return -1;
    int i = 0;
    for (T item : list) {
      if (Objects.equals(value, item)) return i;
      i++;
    }
    return -1;
  }

  // ==================== lastIndexOf ====================

  /**
   * 查找末次出现下标（null 安全）。使用 Objects.equals 匹配。
   *
   * @param list  列表，{@code null} 返回 -1
   * @param value 要查找的值
   * @param <T>   元素类型
   * @return 末次出现的下标，列表为 {@code null} 或未找到时返回 {@code -1}
   */
  public static <T> int lastIndexOf(List<T> list, T value) {
    if (list == null) return -1;
    for (int i = list.size() - 1; i >= 0; i--) {
      if (Objects.equals(value, list.get(i))) return i;
    }
    return -1;
  }

  // ==================== removeNulls ====================

  /**
   * 移除集合中所有 {@code null} 元素，返回新集合，不修改原集合。
   * <ul>
   *   <li>原集合为 {@code null} → 返回 {@code null}</li>
   *   <li>原集合无 null → 返回原集合副本</li>
   * </ul>
   *
   * @param collection 原集合，可为 {@code null}
   * @param <T>        元素类型
   * @return 不含 {@code null} 的新集合；原集合为 {@code null} 时返回 {@code null}
   */
  public static <T> Collection<T> removeNulls(Collection<T> collection) {
    if (collection == null) return null;
    List<T> result = new ArrayList<>();
    for (T item : collection) {
      if (item != null) result.add(item);
    }
    return result;
  }

  // ==================== distinct ====================

  /**
   * 集合去重（保留首次出现顺序），返回新集合，不修改原集合。
   * <ul>
   *   <li>原集合为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code null} 元素视为有效值</li>
   * </ul>
   *
   * @param collection 原集合，可为 {@code null}
   * @param <T>        元素类型
   * @return 去重后的新集合；原集合为 {@code null} 时返回 {@code null}
   */
  public static <T> Collection<T> distinct(Collection<T> collection) {
    if (collection == null) return null;
    Set<T> seen = new HashSet<>();
    List<T> result = new ArrayList<>();
    for (T item : collection) {
      if (seen.add(item)) result.add(item);
    }
    return result;
  }

  // ==================== collectionToString ====================

  /**
   * 将集合转为字符串，格式为 {@code [a, b, c]}。
   * <ul>
   *   <li>{@code collection} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code collection} 为空 → 返回 {@code "[]"}</li>
   * </ul>
   *
   * @param collection 集合，可为 {@code null}
   * @return 集合的字符串表示；参数为 {@code null} 时返回 {@code null}
   */
  public static String collectionToString(Collection<?> collection) {
    if (collection == null) return null;
    if (collection.isEmpty()) return "[]";
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    int i = 0;
    for (Object item : collection) {
      if (i > 0) sb.append(", ");
      sb.append(item);
      i++;
    }
    sb.append(']');
    return sb.toString();
  }

  // ==================== countOf ====================

  /**
   * 统计指定值在集合中出现的次数。
   *
   * @param collection 原集合，{@code null} 返回 0
   * @param value      要统计的值
   * @param <T>        元素类型
   * @return 出现次数，集合为 {@code null} 时返回 0
   */
  public static <T> int countOf(Collection<T> collection, T value) {
    if (collection == null) return 0;
    int count = 0;
    for (T item : collection) {
      if (Objects.equals(value, item)) count++;
    }
    return count;
  }

}
