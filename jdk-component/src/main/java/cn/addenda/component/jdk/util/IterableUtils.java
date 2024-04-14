package cn.addenda.component.jdk.util;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import cn.addenda.component.bean.pojo.Ternary;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/6/5 21:27
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IterableUtils {

  /**
   * @return <pre>inAButNotInB, inAAndB, notInAButInB</pre>
   */
  public static <T> Ternary<List<T>, List<T>, List<T>> separateToList(Iterable<T> a, Iterable<T> b) {
    if (a == null && b == null) {
      return new Ternary<>();
    }
    if (a == null) {
      return new Ternary<>(new ArrayList<>(), new ArrayList<>(), toArrayList(b));
    }
    if (b == null) {
      return new Ternary<>(toArrayList(a), new ArrayList<>(), new ArrayList<>());
    }
    a = toArrayList(a);
    b = toArrayList(b);
    List<T> inAButNotInB = new ArrayList<>();
    List<T> inAAndB = new ArrayList<>();

    for (T t : a) {
      Iterator<T> iterator = b.iterator();
      boolean fg = false;
      while (iterator.hasNext()) {
        T next = iterator.next();
        if (t.equals(next)) {
          inAAndB.add(t);
          iterator.remove();
          fg = true;
          break;
        }
      }
      if (!fg) {
        inAButNotInB.add(t);
      }
    }

    List<T> notInAButInB = toArrayList(b);

    return new Ternary<>(inAButNotInB, inAAndB, notInAButInB);
  }

  /**
   * @return <pre>inAButNotInB, inAAndB, notInAButInB</pre>
   */
  public static <T> Ternary<Iterable<T>, Iterable<T>, Iterable<T>> separate(Iterable<T> a, Iterable<T> b) {
    Ternary<List<T>, List<T>, List<T>> ternary = separateToList(a, b);
    return new Ternary<>(ternary.getF1(), ternary.getF2(), ternary.getF3());
  }

  /**
   * 集合做拆分
   */
  public static <T> List<List<T>> splitToList(Iterable<T> iterable, int quantity) {
    if (iterable == null) {
      return null;
    }
    List<List<T>> listList = new ArrayList<>();
    List<T> seg = null;
    int i = 0;
    for (T t : iterable) {
      if (i % quantity == 0) {
        if (seg != null) {
          listList.add(seg);
        }
        seg = new ArrayList<>();
      }
      seg.add(t);
      i++;
    }
    if (seg != null && !seg.isEmpty()) {
      listList.add(seg);
    }
    return listList;
  }

  /**
   * 集合做拆分
   */
  public static <T> Iterable<Iterable<T>> split(Iterable<T> iterable, int quantity) {
    if (iterable == null) {
      return null;
    }
    List<List<T>> lists = splitToList(iterable, quantity);
    return new ArrayList<>(lists);
  }

  public static <T> Iterable<T> merge(Iterable<T> a, Iterable<T> b) {
    return mergeToList(a, b);
  }

  public static <T> List<T> mergeToList(Iterable<T> a, Iterable<T> b) {
    if (a == null && b == null) {
      return new ArrayList<>();
    }
    if (a == null) {
      return toArrayList(b);
    }
    if (b == null) {
      return toArrayList(a);
    }
    List<T> result = new ArrayList<>();
    a.forEach(result::add);
    b.forEach(result::add);
    return result;
  }

  public static <T, P> Map<P, List<T>> toGroup(Iterable<T> iterable, Function<T, P> function) {
    Assert.notNull(function, "`function` can not be null!");
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.groupingBy(function));
  }

  public static <T, P> Map<P, T> toMap(Iterable<T> iterable, Function<T, P> function) {
    Assert.notNull(function, "`function` can not be null!");
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.toMap(function, a -> a));
  }

  /**
   * 如果需要对空字段排序，使用{@link IterableUtils#deDuplicate(Iterable, Comparator)}或{@link IterableUtils#deDuplicate(Iterable, Comparator, Function)}
   */
  public static <T, R extends Comparable<? super R>> Iterable<T> deDuplicate(Iterable<T> iterable, Function<T, R> function) {
    return deDuplicate(iterable, Comparator.comparing(function), ArrayList::new);
  }

  /**
   * 使用{@link CompareUtils#nullMaxCompare(Comparable, Comparable)}或{@link CompareUtils#nullMinCompare(Comparable, Comparable)}构造comparator可以避免NPE
   */
  public static <T> Iterable<T> deDuplicate(Iterable<T> iterable, Comparator<T> comparator) {
    return deDuplicate(iterable, comparator, ArrayList::new);
  }

  /**
   * 使用{@link CompareUtils#nullMaxCompare(Comparable, Comparable)}或{@link CompareUtils#nullMinCompare(Comparable, Comparable)}构造comparator可以避免NPE
   */
  public static <T> Iterable<T> deDuplicate(Iterable<T> iterable, Comparator<T> comparator, Function<TreeSet<T>, List<T>> finisher) {
    Assert.notNull(comparator, "`comparator` can not be null!");
    Assert.notNull(finisher, "`finisher` can not be null!");
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(comparator)), finisher));
  }

  public static <T, P> Iterable<P> collect(Iterable<T> iterable, Function<T, P> function) {
    Assert.notNull(function, "`function` can not be null!");
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .map(function).collect(Collectors.toList());
  }

  public static <T> ArrayList<T> toArrayList(Iterable<T> iterable) {
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * 将List集合转为HashSet集合。
   */
  public static <T> HashSet<T> toHashSet(Iterable<T> iterable) {
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.toCollection(HashSet::new));
  }

  /**
   * 将List集合转为TreeSet集合。
   */
  public static <T> TreeSet<T> toTreeSet(Iterable<T> iterable, Comparator<T> comparator) {
    Assert.notNull(comparator, "`comparator` can not be null!");
    if (iterable == null) {
      return null;
    }
    return (TreeSet<T>) StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.toCollection((Supplier<Set<T>>) () -> new TreeSet<>(comparator)));
  }

  /**
   * 将List集合转为TreeSet集合。
   */
  public static <T extends Comparable<? super T>> TreeSet<T> toTreeSet(Iterable<T> iterable) {
    if (iterable == null) {
      return null;
    }
    return (TreeSet<T>) StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.toCollection((Supplier<Set<T>>) TreeSet::new));
  }

  public static <T> List<T> castToList(Iterable<T> iterable) {
    return (List<T>) iterable;
  }

  public static <T> Set<T> castToSet(Iterable<T> iterable) {
    return (Set<T>) iterable;
  }

  @SneakyThrows
  public static <T> List<T> createList(Class<T> tClass, int size) {
    Assert.notNull(tClass, "`tClass` can not be null!");
    if (size < 0) {
      return new ArrayList<>();
    }
    List<T> arrayList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      T t = tClass.newInstance();
      arrayList.add(t);
    }
    return arrayList;
  }

  @SneakyThrows
  public static <T> Set<T> createSet(Class<T> tClass, int size) {
    Assert.notNull(tClass, "`tClass` can not be null!");
    if (size < 0) {
      return new HashSet<>();
    }
    Set<T> arrayList = new HashSet<>();
    for (int i = 0; i < size; i++) {
      T t = tClass.newInstance();
      arrayList.add(t);
    }
    return arrayList;
  }

  @SneakyThrows
  public static <T> Iterable<T> createIterable(Class<T> tClass, int size) {
    return createList(tClass, size);
  }


  public static <T, P1> T oneOrNull(P1 p1, Function<P1, List<T>> function) {
    return oneOrNull(p1, function, StackTraceUtils.getCallerInfo());
  }

  public static <T, P1> T oneOrNull(P1 p1, Function<P1, List<T>> function, String callerInfo) {
    List<T> apply = function.apply(p1);
    if (CollectionUtils.isEmpty(apply)) {
      return null;
    }
    if (apply.size() > 1) {
      throw new IllegalStateException(String.format("When invoking [%s], multi result of param[%s] are returned.", callerInfo, p1));
    }
    return apply.get(0);
  }

  public static <T, P1, P2> T oneOrNull(P1 p1, P2 p2, BiFunction<P1, P2, List<T>> function) {
    return oneOrNull(p1, p2, function, StackTraceUtils.getCallerInfo());
  }

  public static <T, P1, P2> T oneOrNull(P1 p1, P2 p2, BiFunction<P1, P2, List<T>> function, String callerInfo) {
    List<T> apply = function.apply(p1, p2);
    if (CollectionUtils.isEmpty(apply)) {
      return null;
    }
    if (apply.size() > 1) {
      throw new IllegalStateException(String.format("When invoking [%s], multi result of param[%s,%s] are returned.", callerInfo, p1, p2));
    }
    return apply.get(0);
  }

}
