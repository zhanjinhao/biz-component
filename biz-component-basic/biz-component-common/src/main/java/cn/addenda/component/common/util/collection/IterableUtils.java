package cn.addenda.component.common.util.collection;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import cn.addenda.component.common.pojo.Ternary;
import cn.addenda.component.common.util.AssertUtils;
import cn.addenda.component.common.util.string.StrFormatUtils;
import cn.addenda.component.stacktrace.StackTraceUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
      return Ternary.of(null, null, null);
    }
    if (a == null) {
      return Ternary.of(new ArrayList<>(), new ArrayList<>(), toArrayList(b));
    }
    if (b == null) {
      return Ternary.of(toArrayList(a), new ArrayList<>(), new ArrayList<>());
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
        if (Objects.equals(t, next)) {
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

    List<T> notInAButInB = castToList(b);

    return Ternary.of(inAButNotInB, inAAndB, notInAButInB);
  }

  /**
   * @return <pre>inAButNotInB, inAAndB, notInAButInB</pre>
   */
  public static <T> Ternary<Iterable<T>, Iterable<T>, Iterable<T>> separate(Iterable<T> a, Iterable<T> b) {
    Ternary<List<T>, List<T>, List<T>> ternary = separateToList(a, b);
    return Ternary.of(ternary.getF1(), ternary.getF2(), ternary.getF3());
  }

  /**
   * @return <pre>inAButNotInB, inAAndB, notInAButInB</pre>
   */
  public static <T> Ternary<Set<T>, Set<T>, Set<T>> separateSetToSet(Set<T> a, Set<T> b) {
    if (a == null && b == null) {
      return Ternary.of(null, null, null);
    }
    if (a == null) {
      return Ternary.of(new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>(b));
    }
    if (b == null) {
      return Ternary.of(new LinkedHashSet<>(a), new LinkedHashSet<>(), new LinkedHashSet<>());
    }
    Set<T> inAButNotInB = new LinkedHashSet<>();
    Set<T> inAAndB = new LinkedHashSet<>();
    for (T t : a) {
      if (b.contains(t)) {
        inAAndB.add(t);
      } else {
        inAButNotInB.add(t);
      }
    }
    Set<T> notInAButInB = new LinkedHashSet<>();
    for (T t : b) {
      if (!a.contains(t)) {
        notInAButInB.add(t);
      }
    }
    return Ternary.of(inAButNotInB, inAAndB, notInAButInB);
  }

  /**
   * 集合做拆分
   */
  public static <T> List<List<T>> splitToListList(Iterable<T> iterable, int quantity) {
    if (iterable == null) {
      return null;
    }
    AssertUtils.isTrue(quantity > 0, () -> StrFormatUtils.format("`quantity` must be greater than 0, but was {}.", quantity));
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
  public static <T> List<Set<T>> splitToSetList(Iterable<T> iterable, int quantity) {
    if (iterable == null) {
      return null;
    }
    AssertUtils.isTrue(quantity > 0, StrFormatUtils.format("`quantity` must be greater than 0, but was {}.", quantity));
    List<Set<T>> setList = new ArrayList<>();
    Set<T> seg = null;
    int i = 0;
    for (T t : iterable) {
      if (i % quantity == 0) {
        if (seg != null) {
          setList.add(seg);
        }
        seg = new LinkedHashSet<>();
      }
      seg.add(t);
      i++;
    }
    if (seg != null && !seg.isEmpty()) {
      setList.add(seg);
    }
    return setList;
  }

  /**
   * Set集合做拆分
   */
  public static <T> List<Set<T>> splitSetToSetList(Set<T> set, int quantity) {
    return splitToSetList(set, quantity);
  }

  /**
   * 集合做拆分
   */
  public static <T> List<Iterable<T>> splitToIterableList(Iterable<T> iterable, int quantity) {
    if (iterable == null) {
      return null;
    }
    AssertUtils.isTrue(quantity > 0, StrFormatUtils.format("`quantity` must be greater than 0, but was {}.", quantity));
    List<Iterable<T>> collectionList = new ArrayList<>();
    List<T> seg = null;
    int i = 0;
    for (T t : iterable) {
      if (i % quantity == 0) {
        if (seg != null) {
          collectionList.add(seg);
        }
        seg = new ArrayList<>();
      }
      seg.add(t);
      i++;
    }
    if (seg != null && !seg.isEmpty()) {
      collectionList.add(seg);
    }
    return collectionList;
  }

  /**
   * 集合做拆分
   */
  public static <T> Iterable<Iterable<T>> split(Iterable<T> iterable, int quantity) {
    List<List<T>> lists = splitToListList(iterable, quantity);
    if (lists == null) {
      return null;
    }
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
    AssertUtils.notNull(function, "`function` can not be null!");
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.groupingBy(function));
  }

  /**
   * 按 function 将 Iterable 转为 Map。若存在重复 key，抛 {@link IllegalStateException}。
   */
  public static <T, P> Map<P, T> toMap(Iterable<T> iterable, Function<T, P> function) {
    AssertUtils.notNull(function, "`function` can not be null!");
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
   * 使用{@link CompareUtils#nullLastCompare(Comparable, Comparable)}或{@link CompareUtils#nullFirstCompare(Comparable, Comparable)}构造comparator可以避免NPE
   */
  public static <T> Iterable<T> deDuplicate(Iterable<T> iterable, Comparator<T> comparator) {
    return deDuplicate(iterable, comparator, ArrayList::new);
  }

  /**
   * 使用{@link CompareUtils#nullLastCompare(Comparable, Comparable)}或{@link CompareUtils#nullFirstCompare(Comparable, Comparable)}构造comparator可以避免NPE
   */
  public static <T> Iterable<T> deDuplicate(Iterable<T> iterable, Comparator<T> comparator, Function<TreeSet<T>, List<T>> finisher) {
    AssertUtils.notNull(comparator, "`comparator` can not be null!");
    AssertUtils.notNull(finisher, "`finisher` can not be null!");
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(comparator)), finisher));
  }

  public static <T, P> Iterable<P> collect(Iterable<T> iterable, Function<T, P> function) {
    return collectToList(iterable, function);
  }

  public static <T, P> List<P> collectToList(Iterable<T> iterable, Function<T, P> function) {
    AssertUtils.notNull(function, "`function` can not be null!");
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
    // 此种优化不可取，返回值是旧集合还是新集合必须是确定的行为
//    if (iterable instanceof ArrayList) {
//      return ((ArrayList<T>) iterable);
//    }
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
//    if (iterable instanceof HashSet) {
//      return ((HashSet<T>) iterable);
//    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.toCollection(HashSet::new));
  }

  /**
   * 将List集合转为TreeSet集合。
   */
  public static <T> TreeSet<T> toTreeSet(Iterable<T> iterable, Comparator<T> comparator) {
    AssertUtils.notNull(comparator, "`comparator` can not be null!");
    if (iterable == null) {
      return null;
    }
//    if (iterable instanceof TreeSet) {
//      return ((TreeSet<T>) iterable);
//    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));
  }

  /**
   * 将List集合转为TreeSet集合。
   */
  public static <T extends Comparable<? super T>> TreeSet<T> toTreeSet(Iterable<T> iterable) {
    if (iterable == null) {
      return null;
    }
//    if (iterable instanceof TreeSet) {
//      return ((TreeSet<T>) iterable);
//    }
    return StreamSupport.stream(iterable.spliterator(), false)
            .collect(Collectors.toCollection(TreeSet::new));
  }

  public static <T> List<T> castToList(Iterable<T> iterable) {
    return (List<T>) iterable;
  }

  public static <T> Set<T> castToSet(Iterable<T> iterable) {
    return (Set<T>) iterable;
  }

  @SneakyThrows
  public static <T> List<T> createList(Class<T> tClass, int size) {
    AssertUtils.notNull(tClass, "`tClass` can not be null!");
    AssertUtils.isTrue(size >= 0, () -> StrFormatUtils.format("`size` must be >= 0, but was {}.", size));
    List<T> arrayList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      T t = tClass.getDeclaredConstructor().newInstance();
      arrayList.add(t);
    }
    return arrayList;
  }

  @SneakyThrows
  public static <T> Set<T> createSet(Class<T> tClass, int size) {
    AssertUtils.notNull(tClass, "`tClass` can not be null!");
    AssertUtils.isTrue(size >= 0, () -> StrFormatUtils.format("`size` must be >= 0, but was {}.", size));
    Set<T> set = new HashSet<>();
    for (int i = 0; i < size; i++) {
      T t = tClass.getDeclaredConstructor().newInstance();
      set.add(t);
    }
    return set;
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
    if (apply == null || apply.isEmpty()) {
      return null;
    }
    if (apply.size() > 1) {
      throw new IllegalStateException(
              String.format("When invoking [%s], multi result[%s] of param[%s] are returned.",
                      callerInfo, JacksonUtils.toStr(apply), p1));
    }
    return apply.get(0);
  }

  public static <T> T oneOrNull(List<T> apply) {
    return oneOrNull(apply, StackTraceUtils.getCallerInfo());
  }

  public static <T> T oneOrNull(List<T> apply, String callerInfo) {
    if (apply == null || apply.isEmpty()) {
      return null;
    }
    if (apply.size() > 1) {
      throw new IllegalStateException(String.format("When invoking [%s], multi result[%s] are returned.", callerInfo, JacksonUtils.toStr(apply)));
    }
    return apply.get(0);
  }

  public static <T, P1, P2> T oneOrNull(P1 p1, P2 p2, BiFunction<P1, P2, List<T>> function) {
    return oneOrNull(p1, p2, function, StackTraceUtils.getCallerInfo());
  }

  public static <T, P1, P2> T oneOrNull(P1 p1, P2 p2, BiFunction<P1, P2, List<T>> function, String callerInfo) {
    List<T> apply = function.apply(p1, p2);
    if (apply == null || apply.isEmpty()) {
      return null;
    }
    if (apply.size() > 1) {
      throw new IllegalStateException(String.format("When invoking [%s], multi result[%s] of param[%s,%s] are returned.", callerInfo, JacksonUtils.toStr(apply), p1, p2));
    }
    return apply.get(0);
  }

  public static <T> Stream<T> ofStream(Iterable<T> iterable) {
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), false);
  }

  public static <T> Stream<T> ofStream(Iterable<T> iterable, boolean parallel) {
    if (iterable == null) {
      return null;
    }
    return StreamSupport.stream(iterable.spliterator(), parallel);
  }

}
