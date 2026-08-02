package cn.addenda.component.common.test.util.collection;

import cn.addenda.component.common.util.collection.ArrayUtils;
import cn.addenda.component.common.util.collection.CompareUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author addenda
 * @since 2024/1/9 10:39
 */
class CompareUtilsTest {


  @Test
  void testNullFirst() {
    List<String> list = ArrayUtils.asArrayList("b", null, "a");
    list.sort((a, b) -> CompareUtils.nullFirstCompare(a, b, Comparator.naturalOrder()));
    Assertions.assertEquals(ArrayUtils.asArrayList(null, "a", "b"), list);
  }

  @Test
  void testNullFirst_AllNull() {
    List<String> list = Arrays.asList(null, null);
    list.sort((a, b) -> CompareUtils.nullFirstCompare(a, b, Comparator.naturalOrder()));
    Assertions.assertEquals(Arrays.asList(null, null), list);
  }

  @Test
  void testNullFirst_NoNull() {
    List<String> list = ArrayUtils.asArrayList("c", "a", "b");
    list.sort((a, b) -> CompareUtils.nullFirstCompare(a, b, Comparator.naturalOrder()));
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "b", "c"), list);
  }

  @Test
  void testNullLast() {
    List<String> list = ArrayUtils.asArrayList("b", null, "a");
    list.sort((a, b) -> CompareUtils.nullLastCompare(a, b, Comparator.naturalOrder()));
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "b", null), list);
  }

  @Test
  void testNullLast_AllNull() {
    List<String> list = Arrays.asList(null, null);
    list.sort((a, b) -> CompareUtils.nullLastCompare(a, b, Comparator.naturalOrder()));
    Assertions.assertEquals(Arrays.asList(null, null), list);
  }

  @Test
  void testNullLast_NoNull() {
    List<String> list = ArrayUtils.asArrayList("c", "a", "b");
    list.sort((a, b) -> CompareUtils.nullLastCompare(a, b, Comparator.naturalOrder()));
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "b", "c"), list);
  }

  @Test
  void test1() {
    List<String> sequence = ArrayUtils.asArrayList("1", "2", null, "3");
    sequence.sort(CompareUtils::nullLastCompare);
    Assertions.assertEquals(ArrayUtils.asArrayList("1", "2", "3", null), sequence);
    sequence.sort(CompareUtils::nullFirstCompare);
    Assertions.assertEquals(ArrayUtils.asArrayList(null, "1", "2", "3"), sequence);
  }

  @Test
  void testNullFirstComparator() {
    List<String> list = ArrayUtils.asArrayList("b", null, "a");
    list.sort(CompareUtils.nullFirstComparator(Comparator.naturalOrder()));
    Assertions.assertEquals(ArrayUtils.asArrayList(null, "a", "b"), list);
  }

  @Test
  void testNullLastComparator() {
    List<String> list = ArrayUtils.asArrayList("b", null, "a");
    list.sort(CompareUtils.nullLastComparator(Comparator.naturalOrder()));
    Assertions.assertEquals(ArrayUtils.asArrayList("a", "b", null), list);
  }

  @Test
  void testNullFirstComparator_ToString() {
    String s = CompareUtils.nullFirstComparator(Comparator.naturalOrder()).toString();
    Assertions.assertTrue(s.startsWith("NullFirstComparator{comparator="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testNullLastComparator_ToString() {
    String s = CompareUtils.nullLastComparator(Comparator.naturalOrder()).toString();
    Assertions.assertTrue(s.startsWith("NullLastComparator{comparator="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== nullFirstCompare(T, T) 分支测试 ====================

  @Test
  void testNullFirstCompare_BothNull() {
    Assertions.assertEquals(0, CompareUtils.nullFirstCompare(null, null));
  }

  @Test
  void testNullFirstCompare_FirstNull() {
    Assertions.assertEquals(-1, CompareUtils.nullFirstCompare(null, "a"));
  }

  @Test
  void testNullFirstCompare_SecondNull() {
    Assertions.assertEquals(1, CompareUtils.nullFirstCompare("a", null));
  }

  @Test
  void testNullFirstCompare_BothNonNull() {
    Assertions.assertTrue(CompareUtils.nullFirstCompare("a", "b") < 0);
    Assertions.assertEquals(0, CompareUtils.nullFirstCompare("a", "a"));
    Assertions.assertTrue(CompareUtils.nullFirstCompare("b", "a") > 0);
  }

  // ==================== nullLastCompare(T, T) 分支测试 ====================

  @Test
  void testNullLastCompare_BothNull() {
    Assertions.assertEquals(0, CompareUtils.nullLastCompare(null, null));
  }

  @Test
  void testNullLastCompare_FirstNull() {
    Assertions.assertEquals(1, CompareUtils.nullLastCompare(null, "a"));
  }

  @Test
  void testNullLastCompare_SecondNull() {
    Assertions.assertEquals(-1, CompareUtils.nullLastCompare("a", null));
  }

  @Test
  void testNullLastCompare_BothNonNull() {
    Assertions.assertTrue(CompareUtils.nullLastCompare("a", "b") < 0);
    Assertions.assertEquals(0, CompareUtils.nullLastCompare("a", "a"));
    Assertions.assertTrue(CompareUtils.nullLastCompare("b", "a") > 0);
  }

  // ==================== nullFirstCompare(T, T, Comparator) 分支测试 ====================

  @Test
  void testNullFirstCompare_Comparator_BothNull() {
    Assertions.assertEquals(0, CompareUtils.nullFirstCompare(null, null, Comparator.naturalOrder()));
  }

  @Test
  void testNullFirstCompare_Comparator_FirstNull() {
    Assertions.assertEquals(-1, CompareUtils.nullFirstCompare(null, "a", Comparator.naturalOrder()));
  }

  @Test
  void testNullFirstCompare_Comparator_SecondNull() {
    Assertions.assertEquals(1, CompareUtils.nullFirstCompare("a", null, Comparator.naturalOrder()));
  }

  @Test
  void testNullFirstCompare_Comparator_BothNonNull() {
    Assertions.assertTrue(CompareUtils.nullFirstCompare("a", "b", Comparator.naturalOrder()) < 0);
    Assertions.assertEquals(0, CompareUtils.nullFirstCompare("a", "a", Comparator.naturalOrder()));
    Assertions.assertTrue(CompareUtils.nullFirstCompare("b", "a", Comparator.naturalOrder()) > 0);
  }

  @Test
  void testNullFirstCompare_CustomComparator() {
    Comparator<String> reversed = Comparator.<String>naturalOrder().reversed();
    Assertions.assertTrue(CompareUtils.nullFirstCompare("a", "b", reversed) > 0);
  }

  // ==================== nullLastCompare(T, T, Comparator) 分支测试 ====================

  @Test
  void testNullLastCompare_Comparator_BothNull() {
    Assertions.assertEquals(0, CompareUtils.nullLastCompare(null, null, Comparator.naturalOrder()));
  }

  @Test
  void testNullLastCompare_Comparator_FirstNull() {
    Assertions.assertEquals(1, CompareUtils.nullLastCompare(null, "a", Comparator.naturalOrder()));
  }

  @Test
  void testNullLastCompare_Comparator_SecondNull() {
    Assertions.assertEquals(-1, CompareUtils.nullLastCompare("a", null, Comparator.naturalOrder()));
  }

  @Test
  void testNullLastCompare_Comparator_BothNonNull() {
    Assertions.assertTrue(CompareUtils.nullLastCompare("a", "b", Comparator.naturalOrder()) < 0);
    Assertions.assertEquals(0, CompareUtils.nullLastCompare("a", "a", Comparator.naturalOrder()));
    Assertions.assertTrue(CompareUtils.nullLastCompare("b", "a", Comparator.naturalOrder()) > 0);
  }

  @Test
  void testNullLastCompare_CustomComparator() {
    Comparator<String> reversed = Comparator.<String>naturalOrder().reversed();
    Assertions.assertTrue(CompareUtils.nullLastCompare("a", "b", reversed) > 0);
  }

  @Test
  void testNullFirstComparator_InstanceOf() {
    Assertions.assertTrue(CompareUtils.nullFirstComparator(Comparator.naturalOrder()) instanceof CompareUtils.NullFirstComparator);
  }

  @Test
  void testNullLastComparator_InstanceOf() {
    Assertions.assertTrue(CompareUtils.nullLastComparator(Comparator.naturalOrder()) instanceof CompareUtils.NullLastComparator);
  }

}
