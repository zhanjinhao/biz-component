package cn.addenda.component.common.util.collection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;

/**
 * @author addenda
 * @since 2024/1/9 10:38
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompareUtils {

  public static <T extends Comparable<? super T>> int nullFirstCompare(T o1, T o2) {
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

  public static <T extends Comparable<? super T>> int nullLastCompare(T o1, T o2) {
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

  public static <T> int nullFirstCompare(T o1, T o2, Comparator<? super T> comparator) {
    if (o1 == null && o2 == null) {
      return 0;
    }
    if (o1 == null) {
      return -1;
    }
    if (o2 == null) {
      return 1;
    }
    return comparator.compare(o1, o2);
  }

  public static <T> int nullLastCompare(T o1, T o2, Comparator<? super T> comparator) {
    if (o1 == null && o2 == null) {
      return 0;
    }
    if (o1 == null) {
      return 1;
    }
    if (o2 == null) {
      return -1;
    }
    return comparator.compare(o1, o2);
  }

  public static <T> Comparator<T> nullFirstComparator(Comparator<? super T> comparator) {
    return new NullFirstComparator<>(comparator);
  }

  public static <T> Comparator<T> nullLastComparator(Comparator<? super T> comparator) {
    return new NullLastComparator<>(comparator);
  }

  public static class NullFirstComparator<T> implements Comparator<T> {

    private final Comparator<? super T> comparator;

    NullFirstComparator(Comparator<? super T> comparator) {
      this.comparator = comparator;
    }

    @Override
    public int compare(T o1, T o2) {
      return nullFirstCompare(o1, o2, comparator);
    }

    @Override
    public String toString() {
      return "NullFirstComparator{comparator=" + comparator + '}';
    }

  }

  public static class NullLastComparator<T> implements Comparator<T> {

    private final Comparator<? super T> comparator;

    NullLastComparator(Comparator<? super T> comparator) {
      this.comparator = comparator;
    }

    @Override
    public int compare(T o1, T o2) {
      return nullLastCompare(o1, o2, comparator);
    }

    @Override
    public String toString() {
      return "NullLastComparator{comparator=" + comparator + '}';
    }

  }

}
