package cn.addenda.component.common.test.util.collection;

import cn.addenda.component.common.util.collection.ArrayUtils;
import cn.addenda.component.common.util.collection.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CollectionUtilsTest {

  // ==================== isEmpty ====================

  @Test
  void testIsEmpty_Null() {
    Assertions.assertTrue(CollectionUtils.isEmpty((List<String>) null));
  }

  @Test
  void testIsEmpty_EmptyCollection() {
    Assertions.assertTrue(CollectionUtils.isEmpty(new ArrayList<>()));
  }

  @Test
  void testIsEmpty_NonEmpty() {
    Assertions.assertFalse(CollectionUtils.isEmpty(ArrayUtils.asArrayList("a", "b")));
  }

  // ==================== orEmpty ====================

  @Test
  void testOrEmpty_Null_ReturnsEmptyList() {
    Assertions.assertEquals(Collections.emptyList(), CollectionUtils.orEmpty(null));
  }

  @Test
  void testOrEmpty_NonEmpty_ReturnsSame() {
    List<String> list = ArrayUtils.asArrayList("a", "b");
    Assertions.assertSame(list, CollectionUtils.orEmpty(list));
  }

  @Test
  void testOrEmpty_Empty_ReturnsSame() {
    List<String> list = new ArrayList<>();
    Assertions.assertSame(list, CollectionUtils.orEmpty(list));
  }

  // ==================== concat ====================

  @Test
  void testConcat_TwoLists() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c", "d"),
            CollectionUtils.concat(ArrayUtils.asArrayList("a", "b"), ArrayUtils.asArrayList("c", "d")));
  }

  @Test
  void testConcat_ThreeLists() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c"),
            CollectionUtils.concat(ArrayUtils.asArrayList("a"), ArrayUtils.asArrayList("b"), ArrayUtils.asArrayList("c")));
  }

  @Test
  void testConcat_SingleList() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.concat(ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testConcat_FirstNull() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.concat(null, ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testConcat_RestContainsNull() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c"),
            CollectionUtils.concat(ArrayUtils.asArrayList("a"), null, ArrayUtils.asArrayList("b", "c")));
  }

  @Test
  void testConcat_AllNull() {
    Assertions.assertNull(CollectionUtils.concat((List<String>) null));
  }

  @Test
  void testConcat_Null_Null() {
    Assertions.assertNull(CollectionUtils.concat(null, (List<String>) null));
  }

  @Test
  void testConcat_RestAllNull_ReturnsCopyOfFirst() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.concat(ArrayUtils.asArrayList("a", "b"), null, null));
  }

  @Test
  void testConcat_RestHasNull_InMiddle() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c"),
            CollectionUtils.concat(ArrayUtils.asArrayList("a"), null, ArrayUtils.asArrayList("b", "c")));
  }

  @Test
  void testConcat_RestHasNull_AtEnd() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.concat(ArrayUtils.asArrayList("a"), ArrayUtils.asArrayList("b"), null));
  }

  @Test
  void testConcat_RestHasEmptyList() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.concat(ArrayUtils.asArrayList("a"), new ArrayList<>(), ArrayUtils.asArrayList("b")));
  }

  @Test
  void testConcat_FirstEmpty_RestNonEmpty() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.concat(new ArrayList<>(), ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testConcat_FirstEmpty_RestNull() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.concat(new ArrayList<>(), (List<String>) null));
  }

  @Test
  void testConcat_RestAllEmptyList() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.concat(ArrayUtils.asArrayList("a"), new ArrayList<>(), new ArrayList<>()));
  }

  @Test
  void testConcat_FirstNull_RestHasNullInMiddle() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.concat((List<String>) null, null, ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testConcat_FirstNull_RestHasNullAtEnd() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.concat((List<String>) null, ArrayUtils.asArrayList("a"), null));
  }

  @Test
  void testConcat_FirstEmpty_AllRestNull() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.concat(new ArrayList<>(), null, null));
  }

  @Test
  void testConcat_FirstNull_AllRestNull_ReturnsNull() {
    Assertions.assertNull(CollectionUtils.concat((List<String>) null, null, null));
  }

  // ==================== subCollection ====================

  @Test
  void testSubCollection_Normal() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("b", "c"),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b", "c", "d"), 1, 3));
  }

  @Test
  void testSubCollection_FromToEqual_ReturnsEmpty() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b", "c"), 1, 1));
  }

  @Test
  void testSubCollection_FullRange() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c"),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b", "c"), 0, 3));
  }

  @Test
  void testSubCollection_NullList() {
    Assertions.assertNull(CollectionUtils.subCollection((List<String>) null, 0, 1));
  }

  @Test
  void testSubCollection_From0_To0() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b"), 0, 0));
  }

  @Test
  void testSubCollection_SingleElement() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("b"),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b", "c"), 1, 2));
  }

  @Test
  void testSubCollection_FromNegative_ClampedToZero() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b"), -1, 1));
  }

  @Test
  void testSubCollection_FromGreaterThanLen_ReturnsEmpty() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b"), 3, 5));
  }

  @Test
  void testSubCollection_ToExceedsLen_ClampedToLen() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("b"),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b"), 1, 4));
  }

  @Test
  void testSubCollection_FromGreaterThanTo_ReturnsEmpty() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b", "c"), 2, 1));
  }

  @Test
  void testSubCollection_FromGreaterThanLength_ReturnsEmpty() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.subCollection(ArrayUtils.asArrayList("a", "b"), 3, 5));
  }

  @Test
  void testSubCollection_EmptyList() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.subCollection(new ArrayList<>(), 0, 1));
  }

  // ==================== withoutAt ====================

  @Test
  void testWithoutAt_SingleIndex() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c", "d"),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", "b", "c", "d"), 1));
  }

  @Test
  void testWithoutAt_MultipleIndices() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "d"),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", "b", "c", "d"), 1, 2));
  }

  @Test
  void testWithoutAt_DuplicateIndices() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c", "d"),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", "b", "c", "d"), 1, 1));
  }

  @Test
  void testWithoutAt_IndexOutOfBounds() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c"),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", "b", "c"), 5, 10));
  }

  @Test
  void testWithoutAt_NegativeIndex() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c"),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", "b", "c"), -1, -5));
  }

  @Test
  void testWithoutAt_NullList() {
    Assertions.assertNull(CollectionUtils.withoutAt((List<String>) null, 0));
  }

  @Test
  void testWithoutAt_EmptyIndices() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testWithoutAt_NullIndices() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", "b"), (int[]) null));
  }

  @Test
  void testWithoutAt_AllIndices_ReturnsEmpty() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", "b", "c"), 0, 1, 2));
  }

  @Test
  void testWithoutAt_MixedValidInvalidIndices() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", "b", "c"), -1, 1, 5));
  }

  @Test
  void testWithoutAt_NullElementPreserved() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.withoutAt(ArrayUtils.asArrayList("a", null, "c"), 1));
  }

  // ==================== withoutElement ====================

  @Test
  void testWithoutElement_SingleElement() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c", "d"),
            CollectionUtils.withoutElement(ArrayUtils.asArrayList("a", "b", "c", "d"), "b"));
  }

  @Test
  void testWithoutElement_MultipleElements() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "d"),
            CollectionUtils.withoutElement(ArrayUtils.asArrayList("a", "b", "c", "d"), "b", "c"));
  }

  @Test
  void testWithoutElement_ElementNotFound() {
    List<String> list = ArrayUtils.asArrayList("a", "b");
    Assertions.assertEquals(list, CollectionUtils.withoutElement(list, "z"));
  }

  @Test
  void testWithoutElement_DuplicateVarargs() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.withoutElement(ArrayUtils.asArrayList("a", "b", "c", "b"), "b", "b"));
  }

  @Test
  void testWithoutElement_NullElements() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.withoutElement(ArrayUtils.asArrayList("a", null, "c"), (String) null));
  }

  @Test
  void testWithoutElement_NullList() {
    Assertions.assertNull(CollectionUtils.withoutElement((List<String>) null, "x"));
  }

  @Test
  void testWithoutElement_EmptyElements() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.withoutElement(ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testWithoutElement_NullElementsVarargs() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.withoutElement(ArrayUtils.asArrayList("a", "b"), (String[]) null));
  }

  @Test
  void testWithoutElement_MultipleNullValues() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.withoutElement(ArrayUtils.asArrayList("a", null, null, "c"), null, null));
  }

  @Test
  void testWithoutElement_MultipleNullValues2() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c", null),
            CollectionUtils.withoutElement(ArrayUtils.asArrayList("a", null, "c", null), (String) null));
  }

  // ==================== elementsAt ====================

  @Test
  void testElementsAt_Normal() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", "b", "c"), 0, 2));
  }

  @Test
  void testElementsAt_SingleIndex() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("b"),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", "b", "c"), 1));
  }

  @Test
  void testElementsAt_DuplicateIndices() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", "b"), 0, 0, 0));
  }

  @Test
  void testElementsAt_InvalidIndicesIgnored() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", "b", "c"), 0, -1, 5, 2));
  }

  @Test
  void testElementsAt_NullList() {
    Assertions.assertNull(CollectionUtils.elementsAt((List<String>) null, 0));
  }

  @Test
  void testElementsAt_EmptyIndices() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testElementsAt_NullIndices() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", "b"), (int[]) null));
  }

  @Test
  void testElementsAt_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", "b"), 5, 6, -1));
  }

  @Test
  void testElementsAt_NullElementPreserved() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", null),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", null, "c"), 0, 1));
  }

  @Test
  void testElementsAt_AllValidIndices() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c"),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", "b", "c"), 0, 1, 2));
  }

  @Test
  void testElementsAt_ReverseOrderIndices_ResultInListOrder() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.elementsAt(ArrayUtils.asArrayList("a", "b", "c"), 2, 0));
  }

  @Test
  void testElementsAt_EmptyList() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.elementsAt(new ArrayList<>(), 0));
  }

  // ==================== elementsIn ====================

  @Test
  void testElementsIn_Normal() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "b", "c"), "a", "c"));
  }

  @Test
  void testElementsIn_NotFound() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "b"), "x", "y"));
  }

  @Test
  void testElementsIn_PartialMatch() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "b", "c"), "a", "x"));
  }

  @Test
  void testElementsIn_DuplicateValues() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "b"), "a", "a"));
  }

  @Test
  void testElementsIn_NullValue() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList((String) null),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", null, "c"), (String) null));
  }

  @Test
  void testElementsIn_NullList() {
    Assertions.assertNull(CollectionUtils.elementsIn((List<String>) null, "x"));
  }

  @Test
  void testElementsIn_NullValuesVarargs() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "b"), (String[]) null));
  }

  @Test
  void testElementsIn_EmptyValues() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testElementsIn_EmptyList() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.elementsIn(new ArrayList<>(), "x"));
  }

  @Test
  void testElementsIn_SingleElement_Match() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a"), "a"));
  }

  @Test
  void testElementsIn_SingleElement_NoMatch() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a"), "x"));
  }

  @Test
  void testElementsIn_FirstMatch() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "b", "c"), "a"));
  }

  @Test
  void testElementsIn_LastMatch() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("c"),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "b", "c"), "c"));
  }

  @Test
  void testElementsIn_MultipleOccurrences_ReturnsOnlyFirst() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "a", "a"), "a"));
  }

  @Test
  void testElementsIn_ResultsInCollectionOrder() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList("a", "b", "c"), "c", "a"));
  }

  @Test
  void testElementsIn_MultipleNullsInArray_RequestOneNull() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList((String) null),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList(null, null, "a"), (String) null));
  }

  @Test
  void testElementsIn_MultipleNullsInArray_RequestMultiNull() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList((String) null, null),
            CollectionUtils.elementsIn(ArrayUtils.asArrayList(null, null, "a"), (String) null, null));
  }

  // ==================== reverse ====================

  @Test
  void testReverse_Normal() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("c", "b", "a"),
            CollectionUtils.reverse(ArrayUtils.asArrayList("a", "b", "c")));
  }

  @Test
  void testReverse_SingleElement() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.reverse(ArrayUtils.asArrayList("a")));
  }

  @Test
  void testReverse_Empty() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.reverse(new ArrayList<>()));
  }

  @Test
  void testReverse_NullList() {
    Assertions.assertNull(CollectionUtils.reverse((List<String>) null));
  }

  // ==================== reverseSelf ====================

  @Test
  void testReverseSelf_Normal() {
    List<String> list = ArrayUtils.asArrayList("a", "b", "c");
    CollectionUtils.reverseSelf(list);
    Assertions.assertEquals(ArrayUtils.asArrayList("c", "b", "a"), list);
  }

  @Test
  void testReverseSelf_SingleElement() {
    List<String> list = ArrayUtils.asArrayList("a");
    CollectionUtils.reverseSelf(list);
    Assertions.assertEquals(ArrayUtils.asArrayList("a"), list);
  }

  @Test
  void testReverseSelf_Empty() {
    List<String> list = new ArrayList<>();
    CollectionUtils.reverseSelf(list);
    Assertions.assertEquals(new ArrayList<>(), list);
  }

  @Test
  void testReverseSelf_NullList_NoException() {
    Assertions.assertDoesNotThrow(() -> CollectionUtils.reverseSelf((List<String>) null));
  }

  // ==================== join ====================

  @Test
  void testJoin_Normal() {
    Assertions.assertEquals("a,b,c",
            CollectionUtils.join(",", ArrayUtils.asArrayList("a", "b", "c")));
  }

  @Test
  void testJoin_SingleElement() {
    Assertions.assertEquals("x",
            CollectionUtils.join(",", ArrayUtils.asArrayList("x")));
  }

  @Test
  void testJoin_EmptyCollection() {
    Assertions.assertEquals("",
            CollectionUtils.join(",", new ArrayList<>()));
  }

  @Test
  void testJoin_NullCollection() {
    Assertions.assertNull(CollectionUtils.join(",", (List<String>) null));
  }

  @Test
  void testJoin_WithNullElement() {
    Assertions.assertEquals("a,null,c",
            CollectionUtils.join(",", ArrayUtils.asArrayList("a", null, "c")));
  }

  @Test
  void testJoin_NullDelimiter() {
    Assertions.assertEquals("anullb",
            CollectionUtils.join(null, ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testJoin_EmptyDelimiter() {
    Assertions.assertEquals("ab",
            CollectionUtils.join("", ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testJoin_AllNullElements() {
    Assertions.assertEquals("null,null,null",
            CollectionUtils.join(",", ArrayUtils.asArrayList(null, null, null)));
  }

  @Test
  void testJoin_SingleNullElement() {
    Assertions.assertEquals("null",
            CollectionUtils.join(",", Collections.singletonList(null)));
  }

  // ==================== firstIndexOf ====================

  @Test
  void testFirstIndexOf_Found() {
    Assertions.assertEquals(1,
            CollectionUtils.firstIndexOf(ArrayUtils.asArrayList("a", "b", "c"), "b"));
  }

  @Test
  void testFirstIndexOf_First() {
    Assertions.assertEquals(0,
            CollectionUtils.firstIndexOf(ArrayUtils.asArrayList("a", "b"), "a"));
  }

  @Test
  void testFirstIndexOf_Last() {
    Assertions.assertEquals(2,
            CollectionUtils.firstIndexOf(ArrayUtils.asArrayList("a", "b", "c"), "c"));
  }

  @Test
  void testFirstIndexOf_NotFound() {
    Assertions.assertEquals(-1,
            CollectionUtils.firstIndexOf(ArrayUtils.asArrayList("a", "b"), "z"));
  }

  @Test
  void testFirstIndexOf_NullList() {
    Assertions.assertEquals(-1,
            CollectionUtils.firstIndexOf((List<String>) null, "x"));
  }

  @Test
  void testFirstIndexOf_NullValue() {
    Assertions.assertEquals(1,
            CollectionUtils.firstIndexOf(ArrayUtils.asArrayList("a", null, "c"), null));
  }

  @Test
  void testFirstIndexOf_EmptyList() {
    Assertions.assertEquals(-1,
            CollectionUtils.firstIndexOf(new ArrayList<>(), "x"));
  }

  @Test
  void testFirstIndexOf_ReturnsFirstOccurrence() {
    Assertions.assertEquals(1,
            CollectionUtils.firstIndexOf(ArrayUtils.asArrayList("a", "b", "b"), "b"));
  }

  @Test
  void testFirstIndexOf_AllSame_Returns0() {
    Assertions.assertEquals(0,
            CollectionUtils.firstIndexOf(ArrayUtils.asArrayList("a", "a", "a"), "a"));
  }

  // ==================== lastIndexOf ====================

  @Test
  void testLastIndexOf_Found() {
    Assertions.assertEquals(1,
            CollectionUtils.lastIndexOf(ArrayUtils.asArrayList("a", "b", "c"), "b"));
  }

  @Test
  void testLastIndexOf_ReturnsLastOccurrence() {
    Assertions.assertEquals(2,
            CollectionUtils.lastIndexOf(ArrayUtils.asArrayList("a", "b", "a"), "a"));
  }

  @Test
  void testLastIndexOf_Last() {
    Assertions.assertEquals(2,
            CollectionUtils.lastIndexOf(ArrayUtils.asArrayList("a", "b", "c"), "c"));
  }

  @Test
  void testLastIndexOf_NotFound() {
    Assertions.assertEquals(-1,
            CollectionUtils.lastIndexOf(ArrayUtils.asArrayList("a", "b"), "z"));
  }

  @Test
  void testLastIndexOf_NullList() {
    Assertions.assertEquals(-1,
            CollectionUtils.lastIndexOf((List<String>) null, "x"));
  }

  @Test
  void testLastIndexOf_NullList_NullValue() {
    Assertions.assertEquals(-1,
            CollectionUtils.lastIndexOf((List<String>) null, null));
  }

  @Test
  void testLastIndexOf_NullValue() {
    Assertions.assertEquals(1,
            CollectionUtils.lastIndexOf(ArrayUtils.asArrayList("a", null, "c"), null));
  }

  @Test
  void testLastIndexOf_EmptyList() {
    Assertions.assertEquals(-1,
            CollectionUtils.lastIndexOf(new ArrayList<>(), "x"));
  }

  @Test
  void testLastIndexOf_SingleElement_Found() {
    Assertions.assertEquals(0,
            CollectionUtils.lastIndexOf(ArrayUtils.asArrayList("a"), "a"));
  }

  @Test
  void testLastIndexOf_SingleElement_NotFound() {
    Assertions.assertEquals(-1,
            CollectionUtils.lastIndexOf(ArrayUtils.asArrayList("a"), "x"));
  }

  @Test
  void testLastIndexOf_AllSame_ReturnsLastIndex() {
    Assertions.assertEquals(2,
            CollectionUtils.lastIndexOf(ArrayUtils.asArrayList("a", "a", "a"), "a"));
  }

  // ==================== removeNulls ====================

  @Test
  void testRemoveNulls_Normal() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.removeNulls(ArrayUtils.asArrayList("a", null, "c")));
  }

  @Test
  void testRemoveNulls_AllNull_ReturnsEmpty() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.removeNulls(ArrayUtils.asArrayList(null, null, null)));
  }

  @Test
  void testRemoveNulls_NoNull_ReturnsCopy() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.removeNulls(ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testRemoveNulls_FirstNull() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("b"),
            CollectionUtils.removeNulls(ArrayUtils.asArrayList(null, "b")));
  }

  @Test
  void testRemoveNulls_LastNull() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.removeNulls(ArrayUtils.asArrayList("a", null)));
  }

  @Test
  void testRemoveNulls_SingleNull() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.removeNulls(Collections.singletonList(null)));
  }

  @Test
  void testRemoveNulls_SingleNonNull() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.removeNulls(ArrayUtils.asArrayList("a")));
  }

  @Test
  void testRemoveNulls_EmptyList() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.removeNulls(new ArrayList<>()));
  }

  @Test
  void testRemoveNulls_NullList() {
    Assertions.assertNull(CollectionUtils.removeNulls(null));
  }

  @Test
  void testRemoveNulls_MultipleNulls() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "c"),
            CollectionUtils.removeNulls(ArrayUtils.asArrayList(null, "a", null, "c", null)));
  }

  @Test
  void testRemoveNulls_PreservesOrder() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c"),
            CollectionUtils.removeNulls(ArrayUtils.asArrayList("a", null, "b", "c")));
  }

  // ==================== distinct ====================

  @Test
  void testDistinct_Normal() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b", "c"),
            CollectionUtils.distinct(ArrayUtils.asArrayList("a", "b", "a", "c", "b")));
  }

  @Test
  void testDistinct_AllUnique() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.distinct(ArrayUtils.asArrayList("a", "b")));
  }

  @Test
  void testDistinct_AllSame() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.distinct(ArrayUtils.asArrayList("a", "a", "a")));
  }

  @Test
  void testDistinct_SingleElement() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a"),
            CollectionUtils.distinct(ArrayUtils.asArrayList("a")));
  }

  @Test
  void testDistinct_EmptyList() {
    Assertions.assertEquals(
            new ArrayList<>(),
            CollectionUtils.distinct(new ArrayList<>()));
  }

  @Test
  void testDistinct_NullList() {
    Assertions.assertNull(CollectionUtils.distinct((List<String>) null));
  }

  @Test
  void testDistinct_WithNulls() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList(null, "a"),
            CollectionUtils.distinct(ArrayUtils.asArrayList(null, "a", null, "a")));
  }

  @Test
  void testDistinct_AllNulls() {
    Assertions.assertEquals(
            Collections.singletonList(null),
            CollectionUtils.distinct(ArrayUtils.asArrayList(null, null, null)));
  }

  @Test
  void testDistinct_PreservesOrder() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("b", "a", "c"),
            CollectionUtils.distinct(ArrayUtils.asArrayList("b", "a", "c", "b", "a")));
  }

  @Test
  void testDistinct_SingleNull() {
    Assertions.assertEquals(
            Collections.singletonList(null),
            CollectionUtils.distinct(Collections.singletonList(null)));
  }

  @Test
  void testDistinct_NullFirst_ThenNonNull() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList(null, "a", "b"),
            CollectionUtils.distinct(ArrayUtils.asArrayList(null, "a", null, "b")));
  }

  @Test
  void testDistinct_FirstAndLastSame() {
    Assertions.assertEquals(
            ArrayUtils.asArrayList("a", "b"),
            CollectionUtils.distinct(ArrayUtils.asArrayList("a", "b", "a")));
  }

  // ==================== collectionToString ====================

  @Test
  void testCollectionToString_Normal() {
    Assertions.assertEquals("[a, b, c]",
            CollectionUtils.collectionToString(ArrayUtils.asArrayList("a", "b", "c")));
  }

  @Test
  void testCollectionToString_SingleElement() {
    Assertions.assertEquals("[a]",
            CollectionUtils.collectionToString(ArrayUtils.asArrayList("a")));
  }

  @Test
  void testCollectionToString_EmptyList() {
    Assertions.assertEquals("[]",
            CollectionUtils.collectionToString(new ArrayList<>()));
  }

  @Test
  void testCollectionToString_NullList() {
    Assertions.assertNull(CollectionUtils.collectionToString(null));
  }

  @Test
  void testCollectionToString_WithNullElement() {
    Assertions.assertEquals("[a, null, c]",
            CollectionUtils.collectionToString(ArrayUtils.asArrayList("a", null, "c")));
  }

  @Test
  void testCollectionToString_IntElements() {
    Assertions.assertEquals("[1, 2, 3]",
            CollectionUtils.collectionToString(ArrayUtils.asArrayList(1, 2, 3)));
  }

  @Test
  void testCollectionToString_NestedList() {
    Assertions.assertEquals("[[a, b], [c]]",
            CollectionUtils.collectionToString(
                    ArrayUtils.asArrayList(ArrayUtils.asArrayList("a", "b"), ArrayUtils.asArrayList("c"))));
  }

  @Test
  void testCollectionToString_BooleanElements() {
    Assertions.assertEquals("[true, false]",
            CollectionUtils.collectionToString(ArrayUtils.asArrayList(true, false)));
  }

  @Test
  void testCollectionToString_DoubleElements() {
    Assertions.assertEquals("[1.0, 2.0]",
            CollectionUtils.collectionToString(ArrayUtils.asArrayList(1.0, 2.0)));
  }

  @Test
  void testCollectionToString_CharElements() {
    Assertions.assertEquals("[a, b]",
            CollectionUtils.collectionToString(ArrayUtils.asArrayList('a', 'b')));
  }

  // ==================== countOf ====================

  @Test
  void testCountOf_Normal() {
    Assertions.assertEquals(2,
            CollectionUtils.countOf(ArrayUtils.asArrayList("a", "b", "a"), "a"));
  }

  @Test
  void testCountOf_Zero() {
    Assertions.assertEquals(0,
            CollectionUtils.countOf(ArrayUtils.asArrayList("a", "b"), "x"));
  }

  @Test
  void testCountOf_NullValue() {
    Assertions.assertEquals(2,
            CollectionUtils.countOf(ArrayUtils.asArrayList(null, "a", null), null));
  }

  @Test
  void testCountOf_NullList() {
    Assertions.assertEquals(0,
            CollectionUtils.countOf((List<String>) null, "x"));
  }

  @Test
  void testCountOf_EmptyList() {
    Assertions.assertEquals(0,
            CollectionUtils.countOf(new ArrayList<>(), "x"));
  }

  @Test
  void testCountOf_AllSame() {
    Assertions.assertEquals(3,
            CollectionUtils.countOf(ArrayUtils.asArrayList("a", "a", "a"), "a"));
  }

  @Test
  void testCountOf_SingleElement_Found() {
    Assertions.assertEquals(1,
            CollectionUtils.countOf(ArrayUtils.asArrayList("a"), "a"));
  }

  @Test
  void testCountOf_SingleElement_NotFound() {
    Assertions.assertEquals(0,
            CollectionUtils.countOf(ArrayUtils.asArrayList("a"), "x"));
  }

  @Test
  void testCountOf_AllNull_CountNull() {
    Assertions.assertEquals(3,
            CollectionUtils.countOf(ArrayUtils.asArrayList(null, null, null), null));
  }

  @Test
  void testCountOf_AllNull_CountNonNull() {
    Assertions.assertEquals(0,
            CollectionUtils.countOf(ArrayUtils.asArrayList(null, null, null), "a"));
  }

}
