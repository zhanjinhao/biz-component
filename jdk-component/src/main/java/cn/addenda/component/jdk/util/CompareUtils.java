package cn.addenda.component.jdk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author addenda
 * @since 2024/1/9 10:38
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompareUtils {

  public static <T extends Comparable<? super T>> int nullMinCompare(T o1, T o2) {
    if (o1 == null && o2 == null) {
      return 0;
    }
    if (o1 == null) {
      return -1;
    }
    if (o2 == null) {
      return 1;
    }
    return o1.compareTo(o2);
  }

  public static <T extends Comparable<? super T>> int nullMaxCompare(T o1, T o2) {
    if (o1 == null && o2 == null) {
      return 0;
    }
    if (o1 == null) {
      return 1;
    }
    if (o2 == null) {
      return -1;
    }
    return o1.compareTo(o2);
  }

}
