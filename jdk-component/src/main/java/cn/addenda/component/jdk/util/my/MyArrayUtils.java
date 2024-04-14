package cn.addenda.component.jdk.util.my;

import cn.addenda.component.jdk.util.CompareUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author addenda
 * @since 2022/2/7 12:37
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyArrayUtils {

  public static <T> ArrayList<T> asArrayList(T... objs) {
    ArrayList<T> list = new ArrayList<>();
    Collections.addAll(list, objs);
    return list;
  }

  public static <T> LinkedList<T> asLinkedList(T... objs) {
    LinkedList<T> list = new LinkedList<>();
    Collections.addAll(list, objs);
    return list;
  }

  public static <T> HashSet<T> asHashSet(T... objs) {
    HashSet<T> set = new HashSet<>();
    Collections.addAll(set, objs);
    return set;
  }

  /**
   * 使用{@link CompareUtils#nullMaxCompare(Comparable, Comparable)}或{@link CompareUtils#nullMinCompare(Comparable, Comparable)}构造comparator可以避免NPE
   */
  public static <T> TreeSet<T> asTreeSet(Comparator<T> comparator, T... objs) {
    TreeSet<T> set = new TreeSet<>(comparator);
    Collections.addAll(set, objs);
    return set;
  }

  public static <T extends Comparable<? super T>> TreeSet<T> asTreeSet(T... objs) {
    TreeSet<T> set = new TreeSet<>();
    Collections.addAll(set, objs);
    return set;
  }

  public static boolean isEmpty(final Object[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isArray(Object obj) {
    return null != obj && obj.getClass().isArray();
  }

  public static String arrayToString(Object obj) {
    if (null == obj) {
      return null;
    }

    if (obj instanceof long[]) {
      return Arrays.toString((long[]) obj);
    } else if (obj instanceof int[]) {
      return Arrays.toString((int[]) obj);
    } else if (obj instanceof short[]) {
      return Arrays.toString((short[]) obj);
    } else if (obj instanceof char[]) {
      return Arrays.toString((char[]) obj);
    } else if (obj instanceof byte[]) {
      return Arrays.toString((byte[]) obj);
    } else if (obj instanceof boolean[]) {
      return Arrays.toString((boolean[]) obj);
    } else if (obj instanceof float[]) {
      return Arrays.toString((float[]) obj);
    } else if (obj instanceof double[]) {
      return Arrays.toString((double[]) obj);
    } else if (isArray(obj)) {
      // 对象数组
      try {
        return Arrays.deepToString((Object[]) obj);
      } catch (Exception ignore) {
        //ignore
      }
    }

    return obj.toString();
  }

}
