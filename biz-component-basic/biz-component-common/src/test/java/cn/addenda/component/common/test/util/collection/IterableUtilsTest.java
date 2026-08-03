package cn.addenda.component.common.test.util.collection;

import cn.addenda.component.common.pojo.Ternary;
import cn.addenda.component.common.util.collection.ArrayUtils;
import cn.addenda.component.common.util.collection.IterableUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author addenda
 * @since 2022/11/17 19:18
 */
@Slf4j
class IterableUtilsTest {

  // ====================================================================================================
  //  separateToList
  // ====================================================================================================

  @Test
  void testSeparateToList_bothNull() {
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(null, null);
    Assertions.assertNull(result.getF1());
    Assertions.assertNull(result.getF2());
    Assertions.assertNull(result.getF3());
  }

  @Test
  void testSeparateToList_aNull() {
    List<String> b = ArrayUtils.asArrayList("x", "y");
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(null, b);
    Assertions.assertTrue(result.getF1().isEmpty(), "inAButNotInB");
    Assertions.assertTrue(result.getF2().isEmpty(), "inAAndB");
    Assertions.assertEquals(ArrayUtils.asArrayList("x", "y"), result.getF3(), "notInAButInB");
  }

  @Test
  void testSeparateToList_bNull() {
    List<String> a = ArrayUtils.asArrayList("x", "y");
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(a, null);
    Assertions.assertEquals(ArrayUtils.asArrayList("x", "y"), result.getF1(), "inAButNotInB");
    Assertions.assertTrue(result.getF2().isEmpty(), "inAAndB");
    Assertions.assertTrue(result.getF3().isEmpty(), "notInAButInB");
  }

  @Test
  void testSeparateToList_noOverlap() {
    List<String> a = ArrayUtils.asArrayList("a", "b");
    List<String> b = ArrayUtils.asArrayList("c", "d");
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(a, b);
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "b"), result.getF1(), "inAButNotInB");
    Assertions.assertTrue(result.getF2().isEmpty(), "inAAndB");
    Assertions.assertEquals(ArrayUtils.asArrayList("c", "d"), result.getF3(), "notInAButInB");
  }

  @Test
  void testSeparateToList_partialOverlap() {
    List<String> a = ArrayUtils.asArrayList("a", "b", "c");
    List<String> b = ArrayUtils.asArrayList("b", "c", "d");
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(a, b);
    Assertions.assertEquals(ArrayUtils.asArrayList("a"), result.getF1(), "inAButNotInB");
    Assertions.assertEquals(ArrayUtils.asArrayList("b", "c"), result.getF2(), "inAAndB");
    Assertions.assertEquals(ArrayUtils.asArrayList("d"), result.getF3(), "notInAButInB");
  }

  @Test
  void testSeparateToList_fullOverlap() {
    List<String> a = ArrayUtils.asArrayList("a", "b");
    List<String> b = ArrayUtils.asArrayList("a", "b");
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(a, b);
    Assertions.assertTrue(result.getF1().isEmpty());
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "b"), result.getF2());
    Assertions.assertTrue(result.getF3().isEmpty());
  }

  @Test
  void testSeparateToList_duplicatesA() {
    List<String> a = ArrayUtils.asArrayList("a", "a", "b");
    List<String> b = ArrayUtils.asArrayList("a", "c");
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(a, b);
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "b"), result.getF1(), "second 'a' stays inAButNotInB");
    Assertions.assertEquals(ArrayUtils.asArrayList("a"), result.getF2(), "only one 'a' matched");
    Assertions.assertEquals(ArrayUtils.asArrayList("c"), result.getF3());
  }

  @Test
  void testSeparateToList_duplicatesB() {
    List<String> a = ArrayUtils.asArrayList("a", "b");
    List<String> b = ArrayUtils.asArrayList("a", "a", "c");
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(a, b);
    Assertions.assertEquals(ArrayUtils.asArrayList("b"), result.getF1());
    Assertions.assertEquals(ArrayUtils.asArrayList("a"), result.getF2());
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "c"), result.getF3(), "extra 'a' in b becomes notInAButInB");
  }

  @Test
  void testSeparateToList_nullElements() {
    List<String> a = new ArrayList<>(Arrays.asList("a", null, "c"));
    List<String> b = new ArrayList<>(Arrays.asList(null, "d"));
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(a, b);
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "c"), result.getF1());
    Assertions.assertTrue(result.getF2().size() == 1 && result.getF2().get(0) == null, "null matches null");
    Assertions.assertEquals(ArrayUtils.asArrayList("d"), result.getF3());
  }

  @Test
  void testSeparateToList_bothEmpty() {
    List<String> a = new ArrayList<>();
    List<String> b = new ArrayList<>();
    Ternary<List<String>, List<String>, List<String>> result = IterableUtils.separateToList(a, b);
    Assertions.assertTrue(result.getF1().isEmpty());
    Assertions.assertTrue(result.getF2().isEmpty());
    Assertions.assertTrue(result.getF3().isEmpty());
  }

  // ====================================================================================================
  //  separate
  // ====================================================================================================

  @Test
  void testSeparate_noOverlap() {
    List<String> a = ArrayUtils.asArrayList("a", "b");
    List<String> b = ArrayUtils.asArrayList("c", "d");
    Ternary<Iterable<String>, Iterable<String>, Iterable<String>> result = IterableUtils.separate(a, b);
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "b"), result.getF1());
    Assertions.assertTrue(IterableUtils.toArrayList(result.getF2()).isEmpty());
    Assertions.assertEquals(ArrayUtils.asArrayList("c", "d"), result.getF3());
  }

  @Test
  void testSeparate_nullPropagation() {
    Ternary<Iterable<String>, Iterable<String>, Iterable<String>> result = IterableUtils.separate(null, null);
    Assertions.assertNull(result.getF1());
    Assertions.assertNull(result.getF2());
    Assertions.assertNull(result.getF3());
  }

  // ====================================================================================================
  //  separateSetToSet
  // ====================================================================================================

  @Test
  void testSeparateSetToSet_bothNull() {
    Ternary<Set<String>, Set<String>, Set<String>> result = IterableUtils.separateSetToSet(null, null);
    Assertions.assertNull(result.getF1());
    Assertions.assertNull(result.getF2());
    Assertions.assertNull(result.getF3());
  }

  @Test
  void testSeparateSetToSet_aNull() {
    Set<String> b = new LinkedHashSet<>(Arrays.asList("x", "y"));
    Ternary<Set<String>, Set<String>, Set<String>> result = IterableUtils.separateSetToSet(null, b);
    Assertions.assertTrue(result.getF1().isEmpty());
    Assertions.assertTrue(result.getF2().isEmpty());
    Assertions.assertEquals(b, result.getF3());
  }

  @Test
  void testSeparateSetToSet_bNull() {
    Set<String> a = new LinkedHashSet<>(Arrays.asList("x", "y"));
    Ternary<Set<String>, Set<String>, Set<String>> result = IterableUtils.separateSetToSet(a, null);
    Assertions.assertEquals(a, result.getF1());
    Assertions.assertTrue(result.getF2().isEmpty());
    Assertions.assertTrue(result.getF3().isEmpty());
  }

  @Test
  void testSeparateSetToSet_noOverlap() {
    Set<String> a = new LinkedHashSet<>(Arrays.asList("a", "b"));
    Set<String> b = new LinkedHashSet<>(Arrays.asList("c", "d"));
    Ternary<Set<String>, Set<String>, Set<String>> result = IterableUtils.separateSetToSet(a, b);
    Assertions.assertEquals(a, result.getF1());
    Assertions.assertTrue(result.getF2().isEmpty());
    Assertions.assertEquals(b, result.getF3());
  }

  @Test
  void testSeparateSetToSet_partialOverlap() {
    Set<String> a = new LinkedHashSet<>(Arrays.asList("a", "b", "c"));
    Set<String> b = new LinkedHashSet<>(Arrays.asList("b", "c", "d"));
    Ternary<Set<String>, Set<String>, Set<String>> result = IterableUtils.separateSetToSet(a, b);
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("a")), result.getF1());
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("b", "c")), result.getF2());
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("d")), result.getF3());
  }

  @Test
  void testSeparateSetToSet_fullOverlap() {
    Set<String> a = new LinkedHashSet<>(Arrays.asList("a", "b"));
    Set<String> b = new LinkedHashSet<>(Arrays.asList("a", "b"));
    Ternary<Set<String>, Set<String>, Set<String>> result = IterableUtils.separateSetToSet(a, b);
    Assertions.assertTrue(result.getF1().isEmpty());
    Assertions.assertEquals(a, result.getF2());
    Assertions.assertTrue(result.getF3().isEmpty());
  }

  @Test
  void testSeparateSetToSet_bothEmpty() {
    Set<String> a = new LinkedHashSet<>();
    Set<String> b = new LinkedHashSet<>();
    Ternary<Set<String>, Set<String>, Set<String>> result = IterableUtils.separateSetToSet(a, b);
    Assertions.assertTrue(result.getF1().isEmpty());
    Assertions.assertTrue(result.getF2().isEmpty());
    Assertions.assertTrue(result.getF3().isEmpty());
  }

  @Test
  void testSeparateSetToSet_nullElements() {
    Set<String> a = new LinkedHashSet<>(Arrays.asList("a", null, "c"));
    Set<String> b = new LinkedHashSet<>(Arrays.asList(null, "d"));
    Ternary<Set<String>, Set<String>, Set<String>> result = IterableUtils.separateSetToSet(a, b);
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("a", "c")), result.getF1());
    Assertions.assertEquals(1, result.getF2().size());
    Assertions.assertTrue(result.getF2().contains(null));
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("d")), result.getF3());
  }

  @Test
  void testSeparateSetToSet_preservesOrder() {
    Set<String> a = new LinkedHashSet<>(Arrays.asList("c", "a", "b"));
    Set<String> b = new LinkedHashSet<>(Arrays.asList("b", "d"));
    Ternary<Set<String>, Set<String>, Set<String>> result = IterableUtils.separateSetToSet(a, b);
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("c", "a")), result.getF1());
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("b")), result.getF2());
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("d")), result.getF3());
  }

  // ====================================================================================================
  //  splitToListList
  // ====================================================================================================

  @Test
  void testSplitToListList_null() {
    Assertions.assertNull(IterableUtils.splitToListList(null, 2));
  }

  @Test
  void testSplitToListList_quantityZero() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.splitToListList(Arrays.asList(1, 2, 3), 0));
  }

  @Test
  void testSplitToListList_quantityNegative() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.splitToListList(Arrays.asList(1, 2, 3), -1));
  }

  @Test
  void testSplitToListList_empty() {
    List<List<Integer>> result = IterableUtils.splitToListList(new ArrayList<Integer>(), 3);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void testSplitToListList_exactMultiple() {
    List<List<Integer>> result = IterableUtils.splitToListList(Arrays.asList(1, 2, 3, 4), 2);
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(Arrays.asList(1, 2), result.get(0));
    Assertions.assertEquals(Arrays.asList(3, 4), result.get(1));
  }

  @Test
  void testSplitToListList_partialLast() {
    List<List<Integer>> result = IterableUtils.splitToListList(Arrays.asList(1, 2, 3, 4, 5), 2);
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals(Arrays.asList(1, 2), result.get(0));
    Assertions.assertEquals(Arrays.asList(3, 4), result.get(1));
    Assertions.assertEquals(Arrays.asList(5), result.get(2));
  }

  @Test
  void testSplitToListList_singleElement() {
    List<List<Integer>> result = IterableUtils.splitToListList(Arrays.asList(1), 5);
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Arrays.asList(1), result.get(0));
  }

  @Test
  void testSplitToListList_quantityOne() {
    List<List<Integer>> result = IterableUtils.splitToListList(Arrays.asList(1, 2, 3), 1);
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals(Arrays.asList(1), result.get(0));
    Assertions.assertEquals(Arrays.asList(2), result.get(1));
    Assertions.assertEquals(Arrays.asList(3), result.get(2));
  }

  @Test
  void testSplitToListList_containsNull() {
    List<String> input = new ArrayList<>(Arrays.asList("a", null, "c"));
    List<List<String>> result = IterableUtils.splitToListList(input, 2);
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(Arrays.asList("a", null), result.get(0));
    Assertions.assertEquals(Arrays.asList("c"), result.get(1));
  }

  // ====================================================================================================
  //  splitToSetList
  // ====================================================================================================

  @Test
  void testSplitToSetList_null() {
    Assertions.assertNull(IterableUtils.splitToSetList(null, 2));
  }

  @Test
  void testSplitToSetList_empty() {
    List<Set<Integer>> result = IterableUtils.splitToSetList(new ArrayList<Integer>(), 3);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void testSplitToSetList_duplicatesInSegment() {
    List<Integer> input = Arrays.asList(1, 1, 2, 2, 3);
    List<Set<Integer>> result = IterableUtils.splitToSetList(input, 2);
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(1)), result.get(0));
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(2)), result.get(1));
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(3)), result.get(2));
  }

  @Test
  void testSplitToSetList_preservesOrder() {
    List<String> input = Arrays.asList("c", "a", "b", "d");
    List<Set<String>> result = IterableUtils.splitToSetList(input, 2);
    Assertions.assertEquals(2, result.size());
    List<String> seg0 = new ArrayList<>(result.get(0));
    Assertions.assertEquals(Arrays.asList("c", "a"), seg0);
    List<String> seg1 = new ArrayList<>(result.get(1));
    Assertions.assertEquals(Arrays.asList("b", "d"), seg1);
  }

  @Test
  void testSplitToSetList_containsNull() {
    List<String> input = new ArrayList<>(Arrays.asList("a", null, null));
    List<Set<String>> result = IterableUtils.splitToSetList(input, 2);
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(2, result.get(0).size());
    Assertions.assertTrue(result.get(0).contains("a"));
    Assertions.assertTrue(result.get(0).contains(null));
  }

  // ====================================================================================================
  //  splitSetToSetList
  // ====================================================================================================

  @Test
  void testSplitSetToSetList_null() {
    Assertions.assertNull(IterableUtils.splitSetToSetList(null, 2));
  }

  @Test
  void testSplitSetToSetList_empty() {
    List<Set<Integer>> result = IterableUtils.splitSetToSetList(new LinkedHashSet<>(), 3);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void testSplitSetToSetList_exactMultiple() {
    Set<Integer> input = new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4));
    List<Set<Integer>> result = IterableUtils.splitSetToSetList(input, 2);
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(1, 2)), result.get(0));
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(3, 4)), result.get(1));
  }

  @Test
  void testSplitSetToSetList_partialLast() {
    Set<Integer> input = new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4, 5));
    List<Set<Integer>> result = IterableUtils.splitSetToSetList(input, 2);
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(1, 2)), result.get(0));
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(3, 4)), result.get(1));
    Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(5)), result.get(2));
  }

  @Test
  void testSplitSetToSetList_preservesOrder() {
    Set<String> input = new LinkedHashSet<>(Arrays.asList("c", "a", "b", "d"));
    List<Set<String>> result = IterableUtils.splitSetToSetList(input, 2);
    Assertions.assertEquals(2, result.size());
    List<String> seg0 = new ArrayList<>(result.get(0));
    Assertions.assertEquals(Arrays.asList("c", "a"), seg0);
    List<String> seg1 = new ArrayList<>(result.get(1));
    Assertions.assertEquals(Arrays.asList("b", "d"), seg1);
  }

  @Test
  void testSplitSetToSetList_containsNull() {
    Set<String> input = new LinkedHashSet<>(Arrays.asList("a", null));
    List<Set<String>> result = IterableUtils.splitSetToSetList(input, 2);
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(2, result.get(0).size());
    Assertions.assertTrue(result.get(0).contains("a"));
    Assertions.assertTrue(result.get(0).contains(null));
  }

  // ====================================================================================================
  //  splitToIterableList
  // ====================================================================================================

  @Test
  void testSplitToIterableList_null() {
    Assertions.assertNull(IterableUtils.splitToIterableList(null, 2));
  }

  @Test
  void testSplitToIterableList_empty() {
    List<Iterable<Integer>> result = IterableUtils.splitToIterableList(new ArrayList<Integer>(), 3);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void testSplitToIterableList_normal() {
    List<Iterable<Integer>> result = IterableUtils.splitToIterableList(Arrays.asList(1, 2, 3, 4, 5), 2);
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals(Arrays.asList(1, 2), IterableUtils.toArrayList(result.get(0)));
    Assertions.assertEquals(Arrays.asList(3, 4), IterableUtils.toArrayList(result.get(1)));
    Assertions.assertEquals(Arrays.asList(5), IterableUtils.toArrayList(result.get(2)));
  }

  @Test
  void testSplitToIterableList_containsNull() {
    List<String> input = new ArrayList<>(Arrays.asList("a", null, "c"));
    List<Iterable<String>> result = IterableUtils.splitToIterableList(input, 2);
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(Arrays.asList("a", null), IterableUtils.toArrayList(result.get(0)));
  }

  // ====================================================================================================
  //  split
  // ====================================================================================================

  @Test
  void testSplit_null() {
    Assertions.assertNull(IterableUtils.split(null, 2));
  }

  @Test
  void testSplit_normal() {
    Iterable<Iterable<Integer>> result = IterableUtils.split(Arrays.asList(1, 2, 3, 4, 5), 2);
    List<Iterable<Integer>> list = IterableUtils.toArrayList(result);
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(Arrays.asList(1, 2), IterableUtils.toArrayList(list.get(0)));
    Assertions.assertEquals(Arrays.asList(3, 4), IterableUtils.toArrayList(list.get(1)));
    Assertions.assertEquals(Arrays.asList(5), IterableUtils.toArrayList(list.get(2)));
  }

  // ====================================================================================================
  //  merge / mergeToList
  // ====================================================================================================

  @Test
  void testMergeToList_bothNull() {
    Assertions.assertTrue(IterableUtils.mergeToList(null, null).isEmpty());
  }

  @Test
  void testMergeToList_aNull() {
    List<String> result = IterableUtils.mergeToList(null, Arrays.asList("x", "y"));
    Assertions.assertEquals(Arrays.asList("x", "y"), result);
  }

  @Test
  void testMergeToList_bNull() {
    List<String> result = IterableUtils.mergeToList(Arrays.asList("x", "y"), null);
    Assertions.assertEquals(Arrays.asList("x", "y"), result);
  }

  @Test
  void testMergeToList_normal() {
    List<String> result = IterableUtils.mergeToList(Arrays.asList("a", "b"), Arrays.asList("c", "d"));
    Assertions.assertEquals(Arrays.asList("a", "b", "c", "d"), result);
  }

  @Test
  void testMergeToList_containsNull() {
    List<String> a = new ArrayList<>(Arrays.asList("a", null));
    List<String> b = new ArrayList<>(Arrays.asList(null, "c"));
    List<String> result = IterableUtils.mergeToList(a, b);
    Assertions.assertEquals(Arrays.asList("a", null, null, "c"), result);
  }

  @Test
  void testMerge_delegates() {
    Assertions.assertEquals(Arrays.asList("a", "b"), IterableUtils.toArrayList(IterableUtils.merge(Arrays.asList("a"), Arrays.asList("b"))));
  }

  // ====================================================================================================
  //  toGroup
  // ====================================================================================================

  @Test
  void testToGroup_nullIterable() {
    Assertions.assertNull(IterableUtils.toGroup(null, (Function<String, Integer>) Integer::valueOf));
  }

  @Test
  void testToGroup_nullFunction() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.toGroup(Arrays.asList("1"), null));
  }

  @Test
  void testToGroup_empty() {
    Map<String, List<String>> result = IterableUtils.toGroup(new ArrayList<String>(), Function.identity());
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void testToGroup_normal() {
    Map<Integer, List<String>> result = IterableUtils.toGroup(Arrays.asList("a", "bb", "ccc"), String::length);
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals(Arrays.asList("a"), result.get(1));
    Assertions.assertEquals(Arrays.asList("bb"), result.get(2));
    Assertions.assertEquals(Arrays.asList("ccc"), result.get(3));
  }

  @Test
  void testToGroup_sameKey() {
    Map<Integer, List<String>> result = IterableUtils.toGroup(Arrays.asList("a", "b", "c"), s -> 1);
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Arrays.asList("a", "b", "c"), result.get(1));
  }

  @Test
  void testToGroup_containsNull() {
    List<String> input = new ArrayList<>(Arrays.asList("a", null, "b"));
    Map<Integer, List<String>> result = IterableUtils.toGroup(input, s -> s == null ? 0 : s.length());
    Assertions.assertEquals(2, result.size());
    Assertions.assertTrue(result.get(0).contains(null));
    Assertions.assertEquals(Arrays.asList("a", "b"), result.get(1));
  }

  // ====================================================================================================
  //  toMap
  // ====================================================================================================

  @Test
  void testToMap_nullIterable() {
    Assertions.assertNull(IterableUtils.toMap(null, Function.identity()));
  }

  @Test
  void testToMap_nullFunction() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.toMap(Arrays.asList(1), null));
  }

  @Test
  void testToMap_empty() {
    Map<String, String> result = IterableUtils.toMap(new ArrayList<String>(), Function.identity());
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void testToMap_normal() {
    Map<Integer, String> result = IterableUtils.toMap(Arrays.asList("a", "bb"), String::length);
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("a", result.get(1));
    Assertions.assertEquals("bb", result.get(2));
  }

  @Test
  void testToMap_duplicateKey() {
    Assertions.assertThrows(IllegalStateException.class,
            () -> IterableUtils.toMap(Arrays.asList("a", "b"), s -> 1));
  }

  // ====================================================================================================
  //  deDuplicate (Function)
  // ====================================================================================================

  @Test
  void testDeDuplicate_function_nullIterable() {
    Assertions.assertNull(IterableUtils.deDuplicate(null, String::toString));
  }

  @Test
  void testDeDuplicate_function_empty() {
    Assertions.assertTrue(IterableUtils.toArrayList(IterableUtils.deDuplicate(new ArrayList<String>(), String::toString)).isEmpty());
  }

  @Test
  void testDeDuplicate_function_normal() {
    List<String> input = Arrays.asList("1", "2", "1");
    Iterable<String> result = IterableUtils.deDuplicate(input, String::toString);
    Assertions.assertEquals(Arrays.asList("1", "2"), IterableUtils.toArrayList(result));
  }

  @Test
  void testDeDuplicate_function_containsNull() {
    List<String> input = new ArrayList<>(Arrays.asList("a", null, "a", null));
    Assertions.assertThrows(NullPointerException.class,
            () -> IterableUtils.deDuplicate(input, String::toString));
  }

  // ====================================================================================================
  //  deDuplicate (Comparator)
  // ====================================================================================================

  @Test
  void testDeDuplicate_comparator_nullIterable() {
    Assertions.assertNull(IterableUtils.deDuplicate((Iterable<Integer>) null, Comparator.naturalOrder()));
  }

  @Test
  void testDeDuplicate_comparator_empty() {
    Assertions.assertTrue(IterableUtils.toArrayList(
            IterableUtils.deDuplicate(new ArrayList<Integer>(), Comparator.naturalOrder())).isEmpty());
  }

  @Test
  void testDeDuplicate_comparator_normal() {
    Iterable<Integer> result = IterableUtils.deDuplicate(Arrays.asList(3, 1, 2, 1), Comparator.naturalOrder());
    Assertions.assertEquals(Arrays.asList(1, 2, 3), IterableUtils.toArrayList(result));
  }

  @Test
  void testDeDuplicate_comparator_nullSafe() {
    List<String> input = new ArrayList<>(Arrays.asList("b", null, "a", null));
    Comparator<String> c = Comparator.nullsFirst(Comparator.<String>naturalOrder());
    Iterable<String> result = IterableUtils.deDuplicate(input, c);
    List<String> list = IterableUtils.toArrayList(result);
    Assertions.assertNull(list.get(0));
    Assertions.assertEquals("a", list.get(1));
    Assertions.assertEquals("b", list.get(2));
  }

  // ====================================================================================================
  //  deDuplicate (Comparator + Finisher)
  // ====================================================================================================

  @Test
  void testDeDuplicate_comparatorFinisher_nullIterable() {
    Assertions.assertNull(IterableUtils.deDuplicate((Iterable<Integer>) null, Comparator.naturalOrder(), ArrayList::new));
  }

  @Test
  void testDeDuplicate_comparatorFinisher_nullComparator() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.deDuplicate(Arrays.asList(1), null, ArrayList::new));
  }

  @Test
  void testDeDuplicate_comparatorFinisher_nullFinisher() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.deDuplicate(Arrays.asList(1), Comparator.naturalOrder(), null));
  }

  @Test
  void testDeDuplicate_comparatorFinisher_customFinisher() {
    Iterable<Integer> result = IterableUtils.deDuplicate(Arrays.asList(3, 1, 2, 1), Comparator.naturalOrder(),
            set -> {
              LinkedList<Integer> list = new LinkedList<>();
              for (Integer i : set) {
                list.add(i);
              }
              return list;
            });
    Assertions.assertEquals(Arrays.asList(1, 2, 3), IterableUtils.toArrayList(result));
  }

  // ====================================================================================================
  //  collect / collectToList
  // ====================================================================================================

  @Test
  void testCollect_nullIterable() {
    Assertions.assertNull(IterableUtils.collect(null, Function.identity()));
  }

  @Test
  void testCollect_nullFunction() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.collect(Arrays.asList(1), null));
  }

  @Test
  void testCollect_empty() {
    Assertions.assertTrue(IterableUtils.toArrayList(IterableUtils.collect(new ArrayList<Integer>(), Function.identity())).isEmpty());
  }

  @Test
  void testCollect_normal() {
    Iterable<String> result = IterableUtils.collect(Arrays.asList("a", "bb"), String::toUpperCase);
    Assertions.assertEquals(Arrays.asList("A", "BB"), IterableUtils.toArrayList(result));
  }

  @Test
  void testCollectToList_normal() {
    List<String> result = IterableUtils.collectToList(Arrays.asList("a", "bb"), String::toUpperCase);
    Assertions.assertEquals(Arrays.asList("A", "BB"), result);
  }

  @Test
  void testCollect_containsNull() {
    List<String> input = new ArrayList<>(Arrays.asList("a", null, "c"));
    List<Integer> result = IterableUtils.collectToList(input, s -> s == null ? 0 : s.length());
    Assertions.assertEquals(Arrays.asList(1, 0, 1), result);
  }

  // ====================================================================================================
  //  toArrayList
  // ====================================================================================================

  @Test
  void testToArrayList_null() {
    Assertions.assertNull(IterableUtils.toArrayList(null));
  }

  @Test
  void testToArrayList_empty() {
    Assertions.assertTrue(IterableUtils.toArrayList(new ArrayList<>()).isEmpty());
  }

  @Test
  void testToArrayList_normal() {
    Assertions.assertEquals(Arrays.asList("a", "b"), IterableUtils.toArrayList(Arrays.asList("a", "b")));
  }

  @Test
  void testToArrayList_containsNull() {
    List<String> input = new ArrayList<>(Arrays.asList("a", null, "c"));
    Assertions.assertEquals(Arrays.asList("a", null, "c"), IterableUtils.toArrayList(input));
  }

  @Test
  void testToArrayList_alwaysNew() {
    List<String> input = new ArrayList<>(Arrays.asList("a", "b"));
    ArrayList<String> result = IterableUtils.toArrayList(input);
    Assertions.assertNotSame(input, result, "should be a new ArrayList");
  }

  // ====================================================================================================
  //  toHashSet
  // ====================================================================================================

  @Test
  void testToHashSet_null() {
    Assertions.assertNull(IterableUtils.toHashSet(null));
  }

  @Test
  void testToHashSet_empty() {
    Assertions.assertTrue(IterableUtils.toHashSet(new ArrayList<>()).isEmpty());
  }

  @Test
  void testToHashSet_normal() {
    HashSet<String> result = IterableUtils.toHashSet(Arrays.asList("a", "b", "a"));
    Assertions.assertEquals(2, result.size());
    Assertions.assertTrue(result.contains("a"));
    Assertions.assertTrue(result.contains("b"));
  }

  @Test
  void testToHashSet_containsNull() {
    List<String> input = new ArrayList<>(Arrays.asList("a", null));
    HashSet<String> result = IterableUtils.toHashSet(input);
    Assertions.assertEquals(2, result.size());
    Assertions.assertTrue(result.contains(null));
  }

  // ====================================================================================================
  //  toTreeSet (with Comparator)
  // ====================================================================================================

  @Test
  void testToTreeSet_comparator_nullIterable() {
    Assertions.assertNull(IterableUtils.toTreeSet((Iterable<Integer>) null, Comparator.naturalOrder()));
  }

  @Test
  void testToTreeSet_comparator_nullComparator() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.toTreeSet(Arrays.asList(1), null));
  }

  @Test
  void testToTreeSet_comparator_empty() {
    Assertions.assertTrue(IterableUtils.toTreeSet(new ArrayList<Integer>(), Comparator.naturalOrder()).isEmpty());
  }

  @Test
  void testToTreeSet_comparator_sorted() {
    TreeSet<Integer> result = IterableUtils.toTreeSet(Arrays.asList(3, 1, 2, 1), Comparator.naturalOrder());
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals(Arrays.asList(1, 2, 3), new ArrayList<>(result));
  }

  @Test
  void testToTreeSet_comparator_nullSafe() {
    List<String> input = new ArrayList<>(Arrays.asList("b", null, "a"));
    Comparator<String> c = Comparator.nullsFirst(Comparator.<String>naturalOrder());
    TreeSet<String> result = IterableUtils.toTreeSet(input, c);
    Assertions.assertEquals(3, result.size());
    List<String> list = new ArrayList<>(result);
    Assertions.assertNull(list.get(0));
    Assertions.assertEquals("a", list.get(1));
    Assertions.assertEquals("b", list.get(2));
  }

  // ====================================================================================================
  //  toTreeSet (natural ordering)
  // ====================================================================================================

  @Test
  void testToTreeSet_natural_nullIterable() {
    Assertions.assertNull(IterableUtils.toTreeSet((Iterable<Integer>) null));
  }

  @Test
  void testToTreeSet_natural_empty() {
    Assertions.assertTrue(IterableUtils.toTreeSet(new ArrayList<Integer>()).isEmpty());
  }

  @Test
  void testToTreeSet_natural_sorted() {
    TreeSet<Integer> result = IterableUtils.toTreeSet(Arrays.asList(3, 1, 2));
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals(Arrays.asList(1, 2, 3), new ArrayList<>(result));
  }

  // ====================================================================================================
  //  castToList / castToSet
  // ====================================================================================================

  @Test
  void testCastToList_normal() {
    List<String> list = new ArrayList<>(Arrays.asList("a", "b"));
    Assertions.assertSame(list, IterableUtils.castToList(list));
  }

  @Test
  void testCastToSet_normal() {
    Set<String> set = new HashSet<>(Arrays.asList("a", "b"));
    Assertions.assertSame(set, IterableUtils.castToSet(set));
  }

  // ====================================================================================================
  //  createList
  // ====================================================================================================

  @Test
  void testCreateList_nullClass() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.createList(null, 3));
  }

  @Test
  void testCreateList_sizeNegative() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.createList(String.class, -1));
  }

  @Test
  void testCreateList_sizeZero() {
    Assertions.assertTrue(IterableUtils.createList(String.class, 0).isEmpty());
  }

  @Test
  void testCreateList_normal() {
    List<StringBuilder> result = IterableUtils.createList(StringBuilder.class, 3);
    Assertions.assertEquals(3, result.size());
    Assertions.assertNotNull(result.get(0));
    Assertions.assertNotNull(result.get(1));
    Assertions.assertNotNull(result.get(2));
  }

  // ====================================================================================================
  //  createSet
  // ====================================================================================================

  @Test
  void testCreateSet_nullClass() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.createSet(null, 3));
  }

  @Test
  void testCreateSet_sizeNegative() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> IterableUtils.createSet(String.class, -1));
  }

  @Test
  void testCreateSet_sizeZero() {
    Assertions.assertTrue(IterableUtils.createSet(String.class, 0).isEmpty());
  }

  @Test
  void testCreateSet_normal() {
    Set<StringBuilder> result = IterableUtils.createSet(StringBuilder.class, 3);
    Assertions.assertEquals(3, result.size());
  }

  // ====================================================================================================
  //  createIterable
  // ====================================================================================================

  @Test
  void testCreateIterable_normal() {
    Iterable<StringBuilder> result = IterableUtils.createIterable(StringBuilder.class, 2);
    List<StringBuilder> list = IterableUtils.toArrayList(result);
    Assertions.assertEquals(2, list.size());
    Assertions.assertNotNull(list.get(0));
  }

  // ====================================================================================================
  //  oneOrNull (List)
  // ====================================================================================================

  @Test
  void testOneOrNull_list_nullList() {
    Assertions.assertNull(IterableUtils.oneOrNull((List<String>) null));
  }

  @Test
  void testOneOrNull_list_empty() {
    Assertions.assertNull(IterableUtils.oneOrNull(new ArrayList<String>()));
  }

  @Test
  void testOneOrNull_list_single() {
    Assertions.assertEquals("x", IterableUtils.oneOrNull(Arrays.asList("x")));
  }

  @Test
  void testOneOrNull_list_multi() {
    Assertions.assertThrows(IllegalStateException.class,
            () -> IterableUtils.oneOrNull(Arrays.asList("x", "y")));
  }

  // ====================================================================================================
  //  oneOrNull (Function)
  // ====================================================================================================

  @Test
  void testOneOrNull_function_nullResult() {
    Assertions.assertNull(IterableUtils.oneOrNull("a", (Function<String, List<String>>) s -> null));
  }

  @Test
  void testOneOrNull_function_emptyResult() {
    Assertions.assertNull(IterableUtils.oneOrNull("a", s -> new ArrayList<>()));
  }

  @Test
  void testOneOrNull_function_single() {
    Assertions.assertEquals("1", IterableUtils.oneOrNull("a", s -> ArrayUtils.asArrayList("1")));
  }

  @Test
  void testOneOrNull_function_multi() {
    Assertions.assertThrows(IllegalStateException.class,
            () -> IterableUtils.oneOrNull("a", s -> ArrayUtils.asArrayList("1", "2")));
  }

  // ====================================================================================================
  //  oneOrNull (BiFunction)
  // ====================================================================================================

  @Test
  void testOneOrNull_biFunction_nullResult() {
    Assertions.assertNull(IterableUtils.oneOrNull("a", "b", (BiFunction<String, String, List<String>>) (s1, s2) -> null));
  }

  @Test
  void testOneOrNull_biFunction_emptyResult() {
    Assertions.assertNull(IterableUtils.oneOrNull("a", "b", (s1, s2) -> new ArrayList<>()));
  }

  @Test
  void testOneOrNull_biFunction_single() {
    Assertions.assertEquals("1", IterableUtils.oneOrNull("a", "b", (s1, s2) -> ArrayUtils.asArrayList("1")));
  }

  @Test
  void testOneOrNull_biFunction_multi() {
    Assertions.assertThrows(IllegalStateException.class,
            () -> IterableUtils.oneOrNull("a", "b", (s1, s2) -> ArrayUtils.asArrayList("1", "2")));
  }

  // ====================================================================================================
  //  ofStream
  // ====================================================================================================

  @Test
  void testOfStream_null() {
    Assertions.assertNull(IterableUtils.ofStream(null));
  }

  @Test
  void testOfStream_empty() {
    Assertions.assertEquals(0, IterableUtils.ofStream(new ArrayList<>()).count());
  }

  @Test
  void testOfStream_normal() {
    List<Integer> result = IterableUtils.ofStream(Arrays.asList(1, 2, 3)).collect(Collectors.toList());
    Assertions.assertEquals(Arrays.asList(1, 2, 3), result);
  }

  @Test
  void testOfStream_parallel_null() {
    Assertions.assertNull(IterableUtils.ofStream(null, true));
  }

  @Test
  void testOfStream_parallel_normal() {
    Stream<Integer> stream = IterableUtils.ofStream(Arrays.asList(1, 2, 3), true);
    Assertions.assertTrue(stream.isParallel());
    Assertions.assertEquals(Arrays.asList(1, 2, 3), stream.collect(Collectors.toList()));
  }

}
