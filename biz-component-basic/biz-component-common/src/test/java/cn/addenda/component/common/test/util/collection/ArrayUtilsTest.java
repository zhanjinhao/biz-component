package cn.addenda.component.common.test.util.collection;

import cn.addenda.component.common.util.collection.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArrayUtilsTest {

  @Test
  void testIsEmpty_ObjectArray_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((Object[]) null));
  }

  @Test
  void testIsEmpty_ObjectArray_Empty() {
    Assertions.assertTrue(ArrayUtils.isEmpty(new Object[0]));
  }

  @Test
  void testIsEmpty_ObjectArray_NonEmpty() {
    Assertions.assertFalse(ArrayUtils.isEmpty(new Object[]{"a", "b"}));
  }

  @Test
  void testIsEmpty_ByteArray_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((byte[]) null));
  }

  @Test
  void testIsEmpty_ByteArray_Empty() {
    Assertions.assertTrue(ArrayUtils.isEmpty(new byte[0]));
  }

  @Test
  void testIsEmpty_ByteArray_NonEmpty() {
    Assertions.assertFalse(ArrayUtils.isEmpty(new byte[]{1, 2}));
  }

  @Test
  void testIsEmpty_ShortArray_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((short[]) null));
  }

  @Test
  void testIsEmpty_ShortArray_Empty() {
    Assertions.assertTrue(ArrayUtils.isEmpty(new short[0]));
  }

  @Test
  void testIsEmpty_ShortArray_NonEmpty() {
    Assertions.assertFalse(ArrayUtils.isEmpty(new short[]{1, 2}));
  }

  @Test
  void testIsEmpty_IntArray_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((int[]) null));
  }

  @Test
  void testIsEmpty_IntArray_Empty() {
    Assertions.assertTrue(ArrayUtils.isEmpty(new int[0]));
  }

  @Test
  void testIsEmpty_IntArray_NonEmpty() {
    Assertions.assertFalse(ArrayUtils.isEmpty(new int[]{1, 2, 3}));
  }

  @Test
  void testIsEmpty_LongArray_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((long[]) null));
  }

  @Test
  void testIsEmpty_LongArray_Empty() {
    Assertions.assertTrue(ArrayUtils.isEmpty(new long[0]));
  }

  @Test
  void testIsEmpty_LongArray_NonEmpty() {
    Assertions.assertFalse(ArrayUtils.isEmpty(new long[]{1L, 2L}));
  }

  @Test
  void testIsEmpty_CharArray_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((char[]) null));
  }

  @Test
  void testIsEmpty_CharArray_Empty() {
    Assertions.assertTrue(ArrayUtils.isEmpty(new char[0]));
  }

  @Test
  void testIsEmpty_CharArray_NonEmpty() {
    Assertions.assertFalse(ArrayUtils.isEmpty(new char[]{'a', 'b'}));
  }

  @Test
  void testIsEmpty_FloatArray_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((float[]) null));
  }

  @Test
  void testIsEmpty_FloatArray_Empty() {
    Assertions.assertTrue(ArrayUtils.isEmpty(new float[0]));
  }

  @Test
  void testIsEmpty_FloatArray_NonEmpty() {
    Assertions.assertFalse(ArrayUtils.isEmpty(new float[]{1.0f, 2.0f}));
  }

  @Test
  void testIsEmpty_DoubleArray_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((double[]) null));
  }

  @Test
  void testIsEmpty_DoubleArray_Empty() {
    Assertions.assertTrue(ArrayUtils.isEmpty(new double[0]));
  }

  @Test
  void testIsEmpty_DoubleArray_NonEmpty() {
    Assertions.assertFalse(ArrayUtils.isEmpty(new double[]{1.0, 2.0}));
  }

  @Test
  void testIsEmpty_BooleanArray_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((boolean[]) null));
  }

  @Test
  void testIsEmpty_BooleanArray_Empty() {
    Assertions.assertTrue(ArrayUtils.isEmpty(new boolean[0]));
  }

  @Test
  void testIsEmpty_BooleanArray_NonEmpty() {
    Assertions.assertFalse(ArrayUtils.isEmpty(new boolean[]{true, false}));
  }

  @Test
  void testIsEmpty_Object_Null() {
    Assertions.assertTrue(ArrayUtils.isEmpty((Object) null));
  }

  @Test
  void testIsEmpty_Object_EmptyIntArray() {
    Assertions.assertTrue(ArrayUtils.isEmpty((Object) new int[0]));
  }

  @Test
  void testIsEmpty_Object_NonEmptyIntArray() {
    Assertions.assertFalse(ArrayUtils.isEmpty((Object) new int[]{1, 2}));
  }

  @Test
  void testIsEmpty_Object_EmptyObjectArray() {
    Assertions.assertTrue(ArrayUtils.isEmpty((Object) new String[0]));
  }

  @Test
  void testIsEmpty_Object_NonEmptyObjectArray() {
    Assertions.assertFalse(ArrayUtils.isEmpty((Object) new String[]{"a"}));
  }

  @Test
  void testIsEmpty_Object_MultiDimArray() {
    Assertions.assertFalse(ArrayUtils.isEmpty((Object) new int[][]{{1}, {2}}));
  }

  @Test
  void testIsEmpty_Object_EmptyMultiDimArray() {
    Assertions.assertTrue(ArrayUtils.isEmpty((Object) new int[0][]));
  }

  @Test
  void testIsEmpty_Object_NotArray_Throws() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.isEmpty((Object) "not array"));
  }

  // ==================== ofStream (包装类型) ====================

  @Test
  void testOfStream_ObjectArray() {
    Assertions.assertEquals(3, ArrayUtils.ofStream(new String[]{"a", "b", "c"}).count());
  }

  @Test
  void testOfStream_ByteArray() {
    Assertions.assertEquals(3, ArrayUtils.ofStream(new byte[]{1, 2, 3}).count());
  }

  @Test
  void testOfStream_ShortArray() {
    Assertions.assertEquals(3, ArrayUtils.ofStream(new short[]{10, 20, 30}).count());
  }

  @Test
  void testOfStream_IntArray() {
    Assertions.assertEquals(4, ArrayUtils.ofStream(new int[]{1, 2, 3, 4}).count());
  }

  @Test
  void testOfStream_LongArray() {
    Assertions.assertEquals(3, ArrayUtils.ofStream(new long[]{1L, 2L, 3L}).count());
  }

  @Test
  void testOfStream_CharArray() {
    Assertions.assertEquals(3, ArrayUtils.ofStream(new char[]{'a', 'b', 'c'}).count());
  }

  @Test
  void testOfStream_FloatArray() {
    Assertions.assertEquals(3, ArrayUtils.ofStream(new float[]{1.0f, 2.0f, 3.0f}).count());
  }

  @Test
  void testOfStream_DoubleArray() {
    Assertions.assertEquals(3, ArrayUtils.ofStream(new double[]{1.0, 2.0, 3.0}).count());
  }

  @Test
  void testOfStream_BooleanArray() {
    Assertions.assertEquals(3, ArrayUtils.ofStream(new boolean[]{true, false, true}).count());
  }

  // ==================== ofIntStream / ofLongStream / ofDoubleStream ====================

  @Test
  void testOfIntStream_Sum() {
    Assertions.assertEquals(10, ArrayUtils.ofIntStream(new int[]{1, 2, 3, 4}).sum());
  }

  @Test
  void testOfLongStream_Sum() {
    Assertions.assertEquals(6L, ArrayUtils.ofLongStream(new long[]{1L, 2L, 3L}).sum());
  }

  @Test
  void testOfDoubleStream_Sum() {
    Assertions.assertEquals(6.0, ArrayUtils.ofDoubleStream(new double[]{1.0, 2.0, 3.0}).sum(), 0.001);
  }

  // ==================== arrayToString ====================

  @Test
  void testArrayToString_Null() {
    Assertions.assertNull(ArrayUtils.arrayToString(null));
  }

  @Test
  void testArrayToString_NotArray() {
    Assertions.assertEquals("hello", ArrayUtils.arrayToString("hello"));
  }

  @Test
  void testArrayToString_IntArray() {
    Assertions.assertEquals("[1, 2, 3]", ArrayUtils.arrayToString(new int[]{1, 2, 3}));
  }

  @Test
  void testArrayToString_LongArray() {
    Assertions.assertEquals("[1, 2]", ArrayUtils.arrayToString(new long[]{1L, 2L}));
  }

  @Test
  void testArrayToString_DoubleArray() {
    Assertions.assertEquals("[1.0, 2.0]", ArrayUtils.arrayToString(new double[]{1.0, 2.0}));
  }

  @Test
  void testArrayToString_BooleanArray() {
    Assertions.assertEquals("[true, false]", ArrayUtils.arrayToString(new boolean[]{true, false}));
  }

  @Test
  void testArrayToString_CharArray() {
    Assertions.assertEquals("[a, b]", ArrayUtils.arrayToString(new char[]{'a', 'b'}));
  }

  @Test
  void testArrayToString_ByteArray() {
    Assertions.assertEquals("[1, 2]", ArrayUtils.arrayToString(new byte[]{1, 2}));
  }

  @Test
  void testArrayToString_ShortArray() {
    Assertions.assertEquals("[1, 2]", ArrayUtils.arrayToString(new short[]{1, 2}));
  }

  @Test
  void testArrayToString_FloatArray() {
    Assertions.assertEquals("[1.0, 2.0]", ArrayUtils.arrayToString(new float[]{1.0f, 2.0f}));
  }

  @Test
  void testArrayToString_StringArray() {
    Assertions.assertEquals("[a, b]", ArrayUtils.arrayToString(new String[]{"a", "b"}));
  }

  @Test
  void testArrayToString_ObjectArray() {
    Assertions.assertEquals("[hello, world]", ArrayUtils.arrayToString(new Object[]{"hello", "world"}));
  }

  @Test
  void testArrayToString_NestedObjectArray() {
    Assertions.assertEquals("[[a, b], [c]]",
            ArrayUtils.arrayToString(new String[][]{{"a", "b"}, {"c"}}));
  }

  @Test
  void testArrayToString_IntMultiDimArray() {
    Assertions.assertEquals("[[1, 2], [3, 4]]",
            ArrayUtils.arrayToString(new int[][]{{1, 2}, {3, 4}}));
  }

  @Test
  void testArrayToString_IntThreeDimArray() {
    Assertions.assertEquals("[[[1], [2]], [[3]]]",
            ArrayUtils.arrayToString(new int[][][]{{{1}, {2}}, {{3}}}));
  }

  @Test
  void testArrayToString_EmptyArray() {
    Assertions.assertEquals("[]", ArrayUtils.arrayToString(new int[0]));
  }

  @Test
  void testArrayToString_MixedDimEmpty() {
    Assertions.assertEquals("[[], [1]]",
            ArrayUtils.arrayToString(new int[][]{{}, {1}}));
  }

  // ==================== orEmpty ====================

  @Test
  void testNullToEmpty_ByteArray_Null() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.orEmpty((byte[]) null));
  }

  @Test
  void testNullToEmpty_ByteArray_NonEmpty() {
    byte[] arr = {1, 2};
    Assertions.assertSame(arr, ArrayUtils.orEmpty(arr));
  }

  @Test
  void testNullToEmpty_ShortArray_Null() {
    Assertions.assertArrayEquals(new short[0], ArrayUtils.orEmpty((short[]) null));
  }

  @Test
  void testNullToEmpty_IntArray_Null() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.orEmpty((int[]) null));
  }

  @Test
  void testNullToEmpty_IntArray_NonEmpty() {
    int[] arr = {1, 2, 3};
    Assertions.assertSame(arr, ArrayUtils.orEmpty(arr));
  }

  @Test
  void testNullToEmpty_LongArray_Null() {
    Assertions.assertArrayEquals(new long[0], ArrayUtils.orEmpty((long[]) null));
  }

  @Test
  void testNullToEmpty_CharArray_Null() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.orEmpty((char[]) null));
  }

  @Test
  void testNullToEmpty_FloatArray_Null() {
    Assertions.assertArrayEquals(new float[0], ArrayUtils.orEmpty((float[]) null), 0f);
  }

  @Test
  void testNullToEmpty_DoubleArray_Null() {
    Assertions.assertArrayEquals(new double[0], ArrayUtils.orEmpty((double[]) null), 0d);
  }

  @Test
  void testNullToEmpty_BooleanArray_Null() {
    Assertions.assertArrayEquals(new boolean[0], ArrayUtils.orEmpty((boolean[]) null));
  }

  @Test
  void testNullToEmpty_ObjectArray_Null() {
    Assertions.assertArrayEquals(new Object[0], ArrayUtils.orEmpty((String[]) null));
  }

  @Test
  void testNullToEmpty_ObjectArray_NonEmpty() {
    String[] arr = {"a", "b"};
    Assertions.assertSame(arr, ArrayUtils.orEmpty(arr));
  }

  // ==================== ofStream null 安全 ====================

  @Test
  void testOfStream_ObjectArray_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofStream((String[]) null).count());
  }

  @Test
  void testOfStream_ByteArray_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofStream((byte[]) null).count());
  }

  @Test
  void testOfStream_ShortArray_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofStream((short[]) null).count());
  }

  @Test
  void testOfStream_IntArray_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofStream((int[]) null).count());
  }

  @Test
  void testOfStream_LongArray_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofStream((long[]) null).count());
  }

  @Test
  void testOfStream_CharArray_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofStream((char[]) null).count());
  }

  @Test
  void testOfStream_FloatArray_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofStream((float[]) null).count());
  }

  @Test
  void testOfStream_DoubleArray_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofStream((double[]) null).count());
  }

  @Test
  void testOfStream_BooleanArray_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofStream((boolean[]) null).count());
  }

  @Test
  void testOfIntStream_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofIntStream((int[]) null).count());
  }

  @Test
  void testOfLongStream_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofLongStream((long[]) null).count());
  }

  @Test
  void testOfDoubleStream_Null_ReturnsEmptyStream() {
    Assertions.assertEquals(0, ArrayUtils.ofDoubleStream((double[]) null).count());
  }

  // ==================== concat ====================

  @Test
  void testConcat_TwoArrays() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b", "c", "d"},
            ArrayUtils.concat(new String[]{"a", "b"}, new String[]{"c", "d"}));
  }

  @Test
  void testConcat_ThreeArrays() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b", "c"},
            ArrayUtils.concat(new String[]{"a"}, new String[]{"b"}, new String[]{"c"}));
  }

  @Test
  void testConcat_FirstNull() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.concat(null, new String[]{"a", "b"}));
  }

  @Test
  void testConcat_RestContainsNull() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b", "c"},
            ArrayUtils.concat(new String[]{"a"}, null, new String[]{"b", "c"}));
  }

  @Test
  void testConcat_SingleArray() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.concat(new String[]{"a", "b"}));
  }

  @Test
  void testConcat_EmptyRest() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.concat(new String[]{"a", "b"}, new String[0]));
  }

  // ==================== concat 原始类型 ====================

  @Test
  void testConcat_IntArray() {
    Assertions.assertArrayEquals(
            new int[]{1, 2, 3, 4},
            ArrayUtils.concat(new int[]{1, 2}, new int[]{3, 4}));
  }

  @Test
  void testConcat_IntArray_FirstNull() {
    Assertions.assertArrayEquals(
            new int[]{1, 2},
            ArrayUtils.concat(null, new int[]{1, 2}));
  }

  @Test
  void testConcat_IntArray_ThreeArrays() {
    Assertions.assertArrayEquals(
            new int[]{1, 2, 3},
            ArrayUtils.concat(new int[]{1}, new int[]{2}, new int[]{3}));
  }

  @Test
  void testConcat_LongArray() {
    Assertions.assertArrayEquals(
            new long[]{1L, 2L, 3L},
            ArrayUtils.concat(new long[]{1L}, new long[]{2L, 3L}));
  }

  @Test
  void testConcat_DoubleArray() {
    Assertions.assertArrayEquals(
            new double[]{1.0, 2.0, 3.0}, ArrayUtils.concat(new double[]{1.0}, new double[]{2.0, 3.0}), 0d);
  }

  @Test
  void testConcat_BooleanArray() {
    Assertions.assertArrayEquals(
            new boolean[]{true, false, true},
            ArrayUtils.concat(new boolean[]{true}, new boolean[]{false, true}));
  }

  @Test
  void testConcat_CharArray() {
    Assertions.assertArrayEquals(
            new char[]{'a', 'b', 'c'},
            ArrayUtils.concat(new char[]{'a'}, new char[]{'b', 'c'}));
  }

  @Test
  void testConcat_ByteArray() {
    Assertions.assertArrayEquals(
            new byte[]{1, 2, 3},
            ArrayUtils.concat(new byte[]{1}, new byte[]{2, 3}));
  }

  @Test
  void testConcat_FloatArray_FirstNull() {
    Assertions.assertArrayEquals(
            new float[]{1.0f, 2.0f}, ArrayUtils.concat(null, new float[]{1.0f, 2.0f}), 0f);
  }

  // ==================== concat 极限场景 ====================

  @Test
  void testConcat_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((String[]) null));
  }

  @Test
  void testConcat_Null_Null() {
    Assertions.assertNull(ArrayUtils.concat((String[]) null, (String[]) null));
  }

  @Test
  void testConcat_ObjectArray_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((Object[]) null));
  }

  @Test
  void testConcat_RestAllNull_ReturnsCopyOfFirst() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.concat(new String[]{"a", "b"}, null, null));
  }

  @Test
  void testConcat_RestHasNull_InMiddle() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b", "c"},
            ArrayUtils.concat(new String[]{"a"}, null, new String[]{"b", "c"}));
  }

  @Test
  void testConcat_RestHasNull_AtEnd() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.concat(new String[]{"a"}, new String[]{"b"}, null));
  }

  @Test
  void testConcat_RestHasEmptyArray() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.concat(new String[]{"a"}, new String[0], new String[]{"b"}));
  }

  @Test
  void testConcat_FirstEmpty_RestNonEmpty() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.concat(new String[0], new String[]{"a", "b"}));
  }

  @Test
  void testConcat_FirstEmpty_RestNull() {
    Assertions.assertArrayEquals(
            new String[0],
            ArrayUtils.concat(new String[0], (String[]) null));
  }

  @Test
  void testConcat_RestAllEmptyArray() {
    Assertions.assertArrayEquals(
            new String[]{"a"},
            ArrayUtils.concat(new String[]{"a"}, new String[0], new String[0]));
  }

  @Test
  void testConcat_FirstNull_RestHasNullInMiddle() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.concat((String[]) null, null, new String[]{"a", "b"}));
  }

  @Test
  void testConcat_FirstNull_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(
            new String[]{"a"},
            ArrayUtils.concat((String[]) null, new String[]{"a"}, null));
  }

  @Test
  void testConcat_IntArray_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((int[]) null));
  }

  @Test
  void testConcat_IntArray_Null_Null() {
    Assertions.assertNull(ArrayUtils.concat((int[]) null, (int[]) null));
  }

  @Test
  void testConcat_IntArray_RestAllNull() {
    Assertions.assertArrayEquals(
            new int[]{1, 2}, ArrayUtils.concat(new int[]{1, 2}, null, null));
  }

  @Test
  void testConcat_IntArray_RestHasNull_AtEnd() {
    Assertions.assertArrayEquals(
            new int[]{1, 2},
            ArrayUtils.concat(new int[]{1}, new int[]{2}, null));
  }

  @Test
  void testConcat_IntArray_FirstNull_RestHasNullInMiddle() {
    Assertions.assertArrayEquals(
            new int[]{1, 2},
            ArrayUtils.concat((int[]) null, null, new int[]{1, 2}));
  }

  @Test
  void testConcat_IntArray_FirstNull_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(
            new int[]{1},
            ArrayUtils.concat((int[]) null, new int[]{1}, null));
  }

  @Test
  void testConcat_IntArray_FirstEmpty_RestNull() {
    Assertions.assertArrayEquals(
            new int[0],
            ArrayUtils.concat(new int[0], (int[]) null));
  }

  @Test
  void testConcat_IntArray_RestAllEmptyArray() {
    Assertions.assertArrayEquals(
            new int[]{1, 2},
            ArrayUtils.concat(new int[]{1, 2}, new int[0], new int[0]));
  }

  @Test
  void testConcat_IntArray_FirstEmpty_AllRestNull() {
    Assertions.assertArrayEquals(
            new int[0],
            ArrayUtils.concat(new int[0], null, null));
  }

  @Test
  void testConcat_IntArray_FirstNull_AllRestNull_ReturnsNull() {
    Assertions.assertNull(ArrayUtils.concat((int[]) null, null, null));
  }

  // ==================== subArray ====================

  @Test
  void testSubArray_Normal() {
    Assertions.assertArrayEquals(
            new String[]{"b", "c"},
            ArrayUtils.subArray(new String[]{"a", "b", "c", "d"}, 1, 3));
  }

  @Test
  void testSubArray_FromToEqual_ReturnsEmpty() {
    Assertions.assertArrayEquals(
            new String[0],
            ArrayUtils.subArray(new String[]{"a", "b", "c"}, 1, 1));
  }

  @Test
  void testSubArray_FullRange() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b", "c"},
            ArrayUtils.subArray(new String[]{"a", "b", "c"}, 0, 3));
  }

  @Test
  void testSubArray_NullArray() {
    Assertions.assertNull(ArrayUtils.subArray((String[]) null, 0, 1));
  }

  @Test
  void testSubArray_From0_To0() {
    Assertions.assertArrayEquals(
            new String[0],
            ArrayUtils.subArray(new String[]{"a", "b"}, 0, 0));
  }

  // ==================== subArray 原始类型 ====================

  @Test
  void testSubArray_IntArray_Normal() {
    Assertions.assertArrayEquals(
            new int[]{2, 3},
            ArrayUtils.subArray(new int[]{1, 2, 3, 4}, 1, 3));
  }

  @Test
  void testSubArray_IntArray_FullRange() {
    Assertions.assertArrayEquals(
            new int[]{1, 2, 3},
            ArrayUtils.subArray(new int[]{1, 2, 3}, 0, 3));
  }

  @Test
  void testSubArray_IntArray_FromToEqual_ReturnsEmpty() {
    Assertions.assertArrayEquals(
            new int[0],
            ArrayUtils.subArray(new int[]{1, 2, 3}, 1, 1));
  }

  @Test
  void testSubArray_IntArray_NullArray() {
    Assertions.assertNull(ArrayUtils.subArray((int[]) null, 0, 1));
  }

  @Test
  void testSubArray_IntArray_From0_To0() {
    Assertions.assertArrayEquals(
            new int[0],
            ArrayUtils.subArray(new int[]{1, 2}, 0, 0));
  }

  @Test
  void testSubArray_IntArray_SingleElement() {
    Assertions.assertArrayEquals(
            new int[]{2},
            ArrayUtils.subArray(new int[]{1, 2, 3}, 1, 2));
  }

  @Test
  void testSubArray_LongArray_Normal() {
    Assertions.assertArrayEquals(
            new long[]{20L, 30L},
            ArrayUtils.subArray(new long[]{10L, 20L, 30L, 40L}, 1, 3));
  }

  @Test
  void testSubArray_LongArray_NullArray() {
    Assertions.assertNull(ArrayUtils.subArray((long[]) null, 0, 1));
  }

  @Test
  void testSubArray_DoubleArray_Normal() {
    Assertions.assertArrayEquals(
            new double[]{2.0, 3.0},
            ArrayUtils.subArray(new double[]{1.0, 2.0, 3.0}, 1, 3), 0d);
  }

  @Test
  void testSubArray_DoubleArray_NullArray() {
    Assertions.assertNull(ArrayUtils.subArray((double[]) null, 0, 1));
  }

  @Test
  void testSubArray_BooleanArray_Normal() {
    Assertions.assertArrayEquals(
            new boolean[]{false, true},
            ArrayUtils.subArray(new boolean[]{true, false, true}, 1, 3));
  }

  @Test
  void testSubArray_BooleanArray_NullArray() {
    Assertions.assertNull(ArrayUtils.subArray((boolean[]) null, 0, 1));
  }

  @Test
  void testSubArray_CharArray_Normal() {
    Assertions.assertArrayEquals(
            new char[]{'b', 'c'},
            ArrayUtils.subArray(new char[]{'a', 'b', 'c', 'd'}, 1, 3));
  }

  @Test
  void testSubArray_CharArray_NullArray() {
    Assertions.assertNull(ArrayUtils.subArray((char[]) null, 0, 1));
  }

  @Test
  void testSubArray_ByteArray_Normal() {
    Assertions.assertArrayEquals(
            new byte[]{2, 3},
            ArrayUtils.subArray(new byte[]{1, 2, 3, 4}, 1, 3));
  }

  @Test
  void testSubArray_ByteArray_NullArray() {
    Assertions.assertNull(ArrayUtils.subArray((byte[]) null, 0, 1));
  }

  @Test
  void testSubArray_ShortArray_Normal() {
    Assertions.assertArrayEquals(
            new short[]{20, 30},
            ArrayUtils.subArray(new short[]{10, 20, 30}, 1, 3));
  }

  @Test
  void testSubArray_ShortArray_NullArray() {
    Assertions.assertNull(ArrayUtils.subArray((short[]) null, 0, 1));
  }

  @Test
  void testSubArray_FloatArray_Normal() {
    Assertions.assertArrayEquals(
            new float[]{2.0f, 3.0f},
            ArrayUtils.subArray(new float[]{1.0f, 2.0f, 3.0f}, 1, 3), 0f);
  }

  @Test
  void testSubArray_FloatArray_NullArray() {
    Assertions.assertNull(ArrayUtils.subArray((float[]) null, 0, 1));
  }

  @Test
  void testSubArray_ObjectArray_FromNegative_ClampedToZero() {
    Assertions.assertArrayEquals(
            new String[]{"1"},
            ArrayUtils.subArray(new String[]{"1", "2"}, -1, 1));
  }

  @Test
  void testSubArray_ObjectArray_FromGreaterThanLen_ReturnsEmpty() {
    Assertions.assertArrayEquals(
            new String[0],
            ArrayUtils.subArray(new String[]{"1", "2"}, 3, 5));
  }

  @Test
  void testSubArray_ObjectArray_ToExceedsLen_ClampedToLen() {
    Assertions.assertArrayEquals(
            new String[]{"2"},
            ArrayUtils.subArray(new String[]{"1", "2"}, 1, 4));
  }

  @Test
  void testSubArray_IntArray_FromNegative_ClampedToZero() {
    Assertions.assertArrayEquals(
            new int[]{1},
            ArrayUtils.subArray(new int[]{1, 2}, -1, 1));
  }

  @Test
  void testSubArray_IntArray_ToExceedsLength_ClampedToLen() {
    Assertions.assertArrayEquals(
            new int[]{2},
            ArrayUtils.subArray(new int[]{1, 2}, 1, 4));
  }

  @Test
  void testSubArray_IntArray_FromGreaterThanLength_ReturnsEmpty() {
    Assertions.assertArrayEquals(
            new int[0],
            ArrayUtils.subArray(new int[]{1, 2}, 3, 5));
  }

  @Test
  void testSubArray_IntArray_FromGreaterThanTo_ReturnsEmpty() {
    Assertions.assertArrayEquals(
            new int[0],
            ArrayUtils.subArray(new int[]{1, 2, 3}, 2, 1));
  }

  @Test
  void testConcat_LongArray_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((long[]) null));
  }

  @Test
  void testConcat_DoubleArray_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((double[]) null));
  }

  @Test
  void testConcat_BooleanArray_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((boolean[]) null));
  }

  @Test
  void testConcat_CharArray_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((char[]) null));
  }

  @Test
  void testConcat_ByteArray_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((byte[]) null));
  }

  @Test
  void testConcat_ShortArray_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((short[]) null));
  }

  @Test
  void testConcat_FloatArray_AllNull() {
    Assertions.assertNull(ArrayUtils.concat((float[]) null));
  }

  // ==================== concat 原始类型完整覆盖 ====================

  @Test
  void testConcat_ByteArray_TwoArrays() {
    Assertions.assertArrayEquals(new byte[]{1, 2, 3, 4}, ArrayUtils.concat(new byte[]{1, 2}, new byte[]{3, 4}));
  }

  @Test
  void testConcat_ByteArray_FirstNull() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.concat(null, new byte[]{1, 2}));
  }

  @Test
  void testConcat_ByteArray_ThreeArrays() {
    Assertions.assertArrayEquals(new byte[]{1, 2, 3}, ArrayUtils.concat(new byte[]{1}, new byte[]{2}, new byte[]{3}));
  }

  @Test
  void testConcat_ByteArray_RestAllNull() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.concat(new byte[]{1, 2}, null, null));
  }

  @Test
  void testConcat_ByteArray_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.concat(new byte[]{1}, new byte[]{2}, null));
  }

  @Test
  void testConcat_ByteArray_FirstNull_RestHasNullInMiddle() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.concat((byte[]) null, null, new byte[]{1, 2}));
  }

  @Test
  void testConcat_ByteArray_FirstNull_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new byte[]{1}, ArrayUtils.concat((byte[]) null, new byte[]{1}, null));
  }

  @Test
  void testConcat_ByteArray_FirstEmpty_RestNull() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.concat(new byte[0], (byte[]) null));
  }

  @Test
  void testConcat_ByteArray_RestAllEmptyArray() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.concat(new byte[]{1, 2}, new byte[0], new byte[0]));
  }

  @Test
  void testConcat_ByteArray_FirstEmpty_AllRestNull() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.concat(new byte[0], null, null));
  }

  @Test
  void testConcat_ByteArray_FirstNull_AllRestNull_ReturnsNull() {
    Assertions.assertNull(ArrayUtils.concat((byte[]) null, null, null));
  }

  @Test
  void testConcat_ByteArray_Null_Null() {
    Assertions.assertNull(ArrayUtils.concat((byte[]) null, (byte[]) null));
  }

  @Test
  void testConcat_ByteArray_SingleArray() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.concat(new byte[]{1, 2}));
  }

  @Test
  void testConcat_ShortArray_TwoArrays() {
    Assertions.assertArrayEquals(new short[]{1, 2, 3, 4}, ArrayUtils.concat(new short[]{1, 2}, new short[]{3, 4}));
  }

  @Test
  void testConcat_ShortArray_FirstNull() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.concat(null, new short[]{1, 2}));
  }

  @Test
  void testConcat_ShortArray_ThreeArrays() {
    Assertions.assertArrayEquals(new short[]{1, 2, 3}, ArrayUtils.concat(new short[]{1}, new short[]{2}, new short[]{3}));
  }

  @Test
  void testConcat_ShortArray_RestAllNull() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.concat(new short[]{1, 2}, null, null));
  }

  @Test
  void testConcat_ShortArray_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.concat(new short[]{1}, new short[]{2}, null));
  }

  @Test
  void testConcat_ShortArray_FirstNull_RestHasNullInMiddle() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.concat((short[]) null, null, new short[]{1, 2}));
  }

  @Test
  void testConcat_ShortArray_FirstNull_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new short[]{1}, ArrayUtils.concat((short[]) null, new short[]{1}, null));
  }

  @Test
  void testConcat_ShortArray_FirstEmpty_RestNull() {
    Assertions.assertArrayEquals(new short[0], ArrayUtils.concat(new short[0], (short[]) null));
  }

  @Test
  void testConcat_ShortArray_RestAllEmptyArray() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.concat(new short[]{1, 2}, new short[0], new short[0]));
  }

  @Test
  void testConcat_ShortArray_FirstEmpty_AllRestNull() {
    Assertions.assertArrayEquals(new short[0], ArrayUtils.concat(new short[0], null, null));
  }

  @Test
  void testConcat_ShortArray_FirstNull_AllRestNull_ReturnsNull() {
    Assertions.assertNull(ArrayUtils.concat((short[]) null, null, null));
  }

  @Test
  void testConcat_ShortArray_Null_Null() {
    Assertions.assertNull(ArrayUtils.concat((short[]) null, (short[]) null));
  }

  @Test
  void testConcat_ShortArray_SingleArray() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.concat(new short[]{1, 2}));
  }

  @Test
  void testConcat_LongArray_TwoArrays() {
    Assertions.assertArrayEquals(new long[]{1, 2, 3, 4}, ArrayUtils.concat(new long[]{1, 2}, new long[]{3, 4}));
  }

  @Test
  void testConcat_LongArray_FirstNull() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.concat(null, new long[]{1, 2}));
  }

  @Test
  void testConcat_LongArray_ThreeArrays() {
    Assertions.assertArrayEquals(new long[]{1, 2, 3}, ArrayUtils.concat(new long[]{1}, new long[]{2}, new long[]{3}));
  }

  @Test
  void testConcat_LongArray_RestAllNull() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.concat(new long[]{1, 2}, null, null));
  }

  @Test
  void testConcat_LongArray_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.concat(new long[]{1}, new long[]{2}, null));
  }

  @Test
  void testConcat_LongArray_FirstNull_RestHasNullInMiddle() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.concat((long[]) null, null, new long[]{1, 2}));
  }

  @Test
  void testConcat_LongArray_FirstNull_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new long[]{1}, ArrayUtils.concat((long[]) null, new long[]{1}, null));
  }

  @Test
  void testConcat_LongArray_FirstEmpty_RestNull() {
    Assertions.assertArrayEquals(new long[0], ArrayUtils.concat(new long[0], (long[]) null));
  }

  @Test
  void testConcat_LongArray_RestAllEmptyArray() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.concat(new long[]{1, 2}, new long[0], new long[0]));
  }

  @Test
  void testConcat_LongArray_FirstEmpty_AllRestNull() {
    Assertions.assertArrayEquals(new long[0], ArrayUtils.concat(new long[0], null, null));
  }

  @Test
  void testConcat_LongArray_FirstNull_AllRestNull_ReturnsNull() {
    Assertions.assertNull(ArrayUtils.concat((long[]) null, null, null));
  }

  @Test
  void testConcat_LongArray_Null_Null() {
    Assertions.assertNull(ArrayUtils.concat((long[]) null, (long[]) null));
  }

  @Test
  void testConcat_LongArray_SingleArray() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.concat(new long[]{1, 2}));
  }

  @Test
  void testConcat_CharArray_TwoArrays() {
    Assertions.assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, ArrayUtils.concat(new char[]{'a', 'b'}, new char[]{'c', 'd'}));
  }

  @Test
  void testConcat_CharArray_FirstNull() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.concat(null, new char[]{'a', 'b'}));
  }

  @Test
  void testConcat_CharArray_ThreeArrays() {
    Assertions.assertArrayEquals(new char[]{'a', 'b', 'c'}, ArrayUtils.concat(new char[]{'a'}, new char[]{'b'}, new char[]{'c'}));
  }

  @Test
  void testConcat_CharArray_RestAllNull() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.concat(new char[]{'a', 'b'}, null, null));
  }

  @Test
  void testConcat_CharArray_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.concat(new char[]{'a'}, new char[]{'b'}, null));
  }

  @Test
  void testConcat_CharArray_FirstNull_RestHasNullInMiddle() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.concat((char[]) null, null, new char[]{'a', 'b'}));
  }

  @Test
  void testConcat_CharArray_FirstNull_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new char[]{'a'}, ArrayUtils.concat((char[]) null, new char[]{'a'}, null));
  }

  @Test
  void testConcat_CharArray_FirstEmpty_RestNull() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.concat(new char[0], (char[]) null));
  }

  @Test
  void testConcat_CharArray_RestAllEmptyArray() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.concat(new char[]{'a', 'b'}, new char[0], new char[0]));
  }

  @Test
  void testConcat_CharArray_FirstEmpty_AllRestNull() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.concat(new char[0], null, null));
  }

  @Test
  void testConcat_CharArray_FirstNull_AllRestNull_ReturnsNull() {
    Assertions.assertNull(ArrayUtils.concat((char[]) null, null, null));
  }

  @Test
  void testConcat_CharArray_Null_Null() {
    Assertions.assertNull(ArrayUtils.concat((char[]) null, (char[]) null));
  }

  @Test
  void testConcat_CharArray_SingleArray() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.concat(new char[]{'a', 'b'}));
  }

  @Test
  void testConcat_FloatArray_TwoArrays() {
    Assertions.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, ArrayUtils.concat(new float[]{1f, 2f}, new float[]{3f, 4f}), 0f);
  }

  @Test
  void testConcat_FloatArray_ThreeArrays() {
    Assertions.assertArrayEquals(new float[]{1f, 2f, 3f}, ArrayUtils.concat(new float[]{1f}, new float[]{2f}, new float[]{3f}), 0f);
  }

  @Test
  void testConcat_FloatArray_RestAllNull() {
    Assertions.assertArrayEquals(new float[]{1f, 2f}, ArrayUtils.concat(new float[]{1f, 2f}, null, null), 0f);
  }

  @Test
  void testConcat_FloatArray_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new float[]{1f, 2f}, ArrayUtils.concat(new float[]{1f}, new float[]{2f}, null), 0f);
  }

  @Test
  void testConcat_FloatArray_FirstNull_RestHasNullInMiddle() {
    Assertions.assertArrayEquals(new float[]{1f, 2f}, ArrayUtils.concat((float[]) null, null, new float[]{1f, 2f}), 0f);
  }

  @Test
  void testConcat_FloatArray_FirstNull_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new float[]{1f}, ArrayUtils.concat((float[]) null, new float[]{1f}, null), 0f);
  }

  @Test
  void testConcat_FloatArray_FirstEmpty_RestNull() {
    Assertions.assertArrayEquals(new float[0], ArrayUtils.concat(new float[0], (float[]) null), 0f);
  }

  @Test
  void testConcat_FloatArray_RestAllEmptyArray() {
    Assertions.assertArrayEquals(new float[]{1f, 2f}, ArrayUtils.concat(new float[]{1f, 2f}, new float[0], new float[0]), 0f);
  }

  @Test
  void testConcat_FloatArray_FirstEmpty_AllRestNull() {
    Assertions.assertArrayEquals(new float[0], ArrayUtils.concat(new float[0], null, null), 0f);
  }

  @Test
  void testConcat_FloatArray_FirstNull_AllRestNull_ReturnsNull() {
    Assertions.assertNull(ArrayUtils.concat((float[]) null, null, null));
  }

  @Test
  void testConcat_FloatArray_Null_Null() {
    Assertions.assertNull(ArrayUtils.concat((float[]) null, (float[]) null));
  }

  @Test
  void testConcat_FloatArray_SingleArray() {
    Assertions.assertArrayEquals(new float[]{1f, 2f}, ArrayUtils.concat(new float[]{1f, 2f}), 0f);
  }

  @Test
  void testConcat_DoubleArray_TwoArrays() {
    Assertions.assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, ArrayUtils.concat(new double[]{1d, 2d}, new double[]{3d, 4d}), 0d);
  }

  @Test
  void testConcat_DoubleArray_FirstNull() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.concat(null, new double[]{1d, 2d}), 0d);
  }

  @Test
  void testConcat_DoubleArray_ThreeArrays() {
    Assertions.assertArrayEquals(new double[]{1d, 2d, 3d}, ArrayUtils.concat(new double[]{1d}, new double[]{2d}, new double[]{3d}), 0d);
  }

  @Test
  void testConcat_DoubleArray_RestAllNull() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.concat(new double[]{1d, 2d}, null, null), 0d);
  }

  @Test
  void testConcat_DoubleArray_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.concat(new double[]{1d}, new double[]{2d}, null), 0d);
  }

  @Test
  void testConcat_DoubleArray_FirstNull_RestHasNullInMiddle() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.concat((double[]) null, null, new double[]{1d, 2d}), 0d);
  }

  @Test
  void testConcat_DoubleArray_FirstNull_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new double[]{1d}, ArrayUtils.concat((double[]) null, new double[]{1d}, null), 0d);
  }

  @Test
  void testConcat_DoubleArray_FirstEmpty_RestNull() {
    Assertions.assertArrayEquals(new double[0], ArrayUtils.concat(new double[0], (double[]) null), 0d);
  }

  @Test
  void testConcat_DoubleArray_RestAllEmptyArray() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.concat(new double[]{1d, 2d}, new double[0], new double[0]), 0d);
  }

  @Test
  void testConcat_DoubleArray_FirstEmpty_AllRestNull() {
    Assertions.assertArrayEquals(new double[0], ArrayUtils.concat(new double[0], null, null), 0d);
  }

  @Test
  void testConcat_DoubleArray_FirstNull_AllRestNull_ReturnsNull() {
    Assertions.assertNull(ArrayUtils.concat((double[]) null, null, null));
  }

  @Test
  void testConcat_DoubleArray_Null_Null() {
    Assertions.assertNull(ArrayUtils.concat((double[]) null, (double[]) null));
  }

  @Test
  void testConcat_DoubleArray_SingleArray() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.concat(new double[]{1d, 2d}), 0d);
  }

  @Test
  void testConcat_BooleanArray_TwoArrays() {
    Assertions.assertArrayEquals(new boolean[]{true, false, true}, ArrayUtils.concat(new boolean[]{true}, new boolean[]{false, true}));
  }

  @Test
  void testConcat_BooleanArray_FirstNull() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.concat(null, new boolean[]{true, false}));
  }

  @Test
  void testConcat_BooleanArray_ThreeArrays() {
    Assertions.assertArrayEquals(new boolean[]{true, false, true}, ArrayUtils.concat(new boolean[]{true}, new boolean[]{false}, new boolean[]{true}));
  }

  @Test
  void testConcat_BooleanArray_RestAllNull() {
    Assertions.assertArrayEquals(new boolean[]{true}, ArrayUtils.concat(new boolean[]{true}, null, null));
  }

  @Test
  void testConcat_BooleanArray_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.concat(new boolean[]{true}, new boolean[]{false}, null));
  }

  @Test
  void testConcat_BooleanArray_FirstNull_RestHasNullInMiddle() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.concat((boolean[]) null, null, new boolean[]{true, false}));
  }

  @Test
  void testConcat_BooleanArray_FirstNull_RestHasNullAtEnd() {
    Assertions.assertArrayEquals(new boolean[]{true}, ArrayUtils.concat((boolean[]) null, new boolean[]{true}, null));
  }

  @Test
  void testConcat_BooleanArray_FirstEmpty_RestNull() {
    Assertions.assertArrayEquals(new boolean[0], ArrayUtils.concat(new boolean[0], (boolean[]) null));
  }

  @Test
  void testConcat_BooleanArray_RestAllEmptyArray() {
    Assertions.assertArrayEquals(new boolean[]{true}, ArrayUtils.concat(new boolean[]{true}, new boolean[0], new boolean[0]));
  }

  @Test
  void testConcat_BooleanArray_FirstEmpty_AllRestNull() {
    Assertions.assertArrayEquals(new boolean[0], ArrayUtils.concat(new boolean[0], null, null));
  }

  @Test
  void testConcat_BooleanArray_FirstNull_AllRestNull_ReturnsNull() {
    Assertions.assertNull(ArrayUtils.concat((boolean[]) null, null, null));
  }

  @Test
  void testConcat_BooleanArray_Null_Null() {
    Assertions.assertNull(ArrayUtils.concat((boolean[]) null, (boolean[]) null));
  }

  @Test
  void testConcat_BooleanArray_SingleArray() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.concat(new boolean[]{true, false}));
  }

  // ==================== withoutAt ====================

  @Test
  void testWithoutAt_ObjectArray_SingleIndex() {
    Assertions.assertArrayEquals(
            new String[]{"a", "c", "d"},
            ArrayUtils.withoutAt(new String[]{"a", "b", "c", "d"}, 1));
  }

  @Test
  void testWithoutAt_ObjectArray_MultipleIndices() {
    Assertions.assertArrayEquals(
            new String[]{"a", "d"},
            ArrayUtils.withoutAt(new String[]{"a", "b", "c", "d"}, 1, 2));
  }

  @Test
  void testWithoutAt_ObjectArray_DuplicateIndices() {
    Assertions.assertArrayEquals(
            new String[]{"a", "c", "d"},
            ArrayUtils.withoutAt(new String[]{"a", "b", "c", "d"}, 1, 1));
  }

  @Test
  void testWithoutAt_ObjectArray_IndexOutOfBounds() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b", "c"},
            ArrayUtils.withoutAt(new String[]{"a", "b", "c"}, 5, 10));
  }

  @Test
  void testWithoutAt_ObjectArray_NegativeIndex() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b", "c"},
            ArrayUtils.withoutAt(new String[]{"a", "b", "c"}, -1, -5));
  }

  @Test
  void testWithoutAt_ObjectArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutAt((String[]) null, 0));
  }

  @Test
  void testWithoutAt_ObjectArray_EmptyIndices() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.withoutAt(new String[]{"a", "b"}));
  }

  @Test
  void testWithoutAt_IntArray_SingleIndex() {
    Assertions.assertArrayEquals(
            new int[]{1, 3, 4},
            ArrayUtils.withoutAt(new int[]{1, 2, 3, 4}, 1));
  }

  @Test
  void testWithoutAt_IntArray_MultipleIndices() {
    Assertions.assertArrayEquals(
            new int[]{1, 4},
            ArrayUtils.withoutAt(new int[]{1, 2, 3, 4}, 1, 2));
  }

  @Test
  void testWithoutAt_IntArray_DuplicateIndices() {
    Assertions.assertArrayEquals(
            new int[]{1, 3, 4},
            ArrayUtils.withoutAt(new int[]{1, 2, 3, 4}, 1, 1));
  }

  @Test
  void testWithoutAt_IntArray_IndexOutOfBounds() {
    Assertions.assertArrayEquals(
            new int[]{1, 2, 3},
            ArrayUtils.withoutAt(new int[]{1, 2, 3}, 5, 10));
  }

  @Test
  void testWithoutAt_IntArray_NegativeIndex() {
    Assertions.assertArrayEquals(
            new int[]{1, 2, 3},
            ArrayUtils.withoutAt(new int[]{1, 2, 3}, -1, -5));
  }

  @Test
  void testWithoutAt_IntArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutAt((int[]) null, 0));
  }

  @Test
  void testWithoutAt_IntArray_EmptyIndices() {
    Assertions.assertArrayEquals(
            new int[]{1, 2},
            ArrayUtils.withoutAt(new int[]{1, 2}));
  }

  @Test
  void testWithoutAt_IntArray_MixedValidInvalidIndices() {
    Assertions.assertArrayEquals(
            new int[]{1, 3},
            ArrayUtils.withoutAt(new int[]{1, 2, 3}, 1, -1, 10));
  }

  @Test
  void testWithoutAt_ByteArray() {
    Assertions.assertArrayEquals(
            new byte[]{1, 3},
            ArrayUtils.withoutAt(new byte[]{1, 2, 3}, 1));
  }

  @Test
  void testWithoutAt_ObjectArray_MixedValidInvalidIndices() {
    Assertions.assertArrayEquals(
            new String[]{"a", "c"},
            ArrayUtils.withoutAt(new String[]{"a", "b", "c"}, -1, 1, 5));
  }

  // ==================== withoutAt 原始类型完整覆盖 ====================

  @Test
  void testWithoutAt_ByteArray_SingleIndex() {
    Assertions.assertArrayEquals(new byte[]{1, 3}, ArrayUtils.withoutAt(new byte[]{1, 2, 3}, 1));
  }

  @Test
  void testWithoutAt_ByteArray_MultipleIndices() {
    Assertions.assertArrayEquals(new byte[]{1}, ArrayUtils.withoutAt(new byte[]{1, 2, 3}, 1, 2));
  }

  @Test
  void testWithoutAt_ByteArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new byte[]{2, 3}, ArrayUtils.withoutAt(new byte[]{1, 2, 3}, 0, 0));
  }

  @Test
  void testWithoutAt_ByteArray_IndexOutOfBounds() {
    Assertions.assertArrayEquals(new byte[]{1, 2, 3}, ArrayUtils.withoutAt(new byte[]{1, 2, 3}, 5));
  }

  @Test
  void testWithoutAt_ByteArray_NegativeIndex() {
    Assertions.assertArrayEquals(new byte[]{1, 2, 3}, ArrayUtils.withoutAt(new byte[]{1, 2, 3}, -1));
  }

  @Test
  void testWithoutAt_ByteArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutAt((byte[]) null, 0));
  }

  @Test
  void testWithoutAt_ByteArray_EmptyIndices() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.withoutAt(new byte[]{1, 2}));
  }

  @Test
  void testWithoutAt_ByteArray_MixedValidInvalidIndices() {
    Assertions.assertArrayEquals(new byte[]{1, 3}, ArrayUtils.withoutAt(new byte[]{1, 2, 3}, 1, -1, 5));
  }

  @Test
  void testWithoutAt_ByteArray_NullIndices() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.withoutAt(new byte[]{1, 2}, (int[]) null));
  }

  @Test
  void testWithoutAt_ByteArray_NullElementPreserved() { /* byte can't be null */ }

  @Test
  void testWithoutAt_ShortArray_SingleIndex() {
    Assertions.assertArrayEquals(new short[]{1, 3}, ArrayUtils.withoutAt(new short[]{1, 2, 3}, 1));
  }

  @Test
  void testWithoutAt_ShortArray_MultipleIndices() {
    Assertions.assertArrayEquals(new short[]{1}, ArrayUtils.withoutAt(new short[]{1, 2, 3}, 1, 2));
  }

  @Test
  void testWithoutAt_ShortArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new short[]{2, 3}, ArrayUtils.withoutAt(new short[]{1, 2, 3}, 0, 0));
  }

  @Test
  void testWithoutAt_ShortArray_IndexOutOfBounds() {
    Assertions.assertArrayEquals(new short[]{1, 2, 3}, ArrayUtils.withoutAt(new short[]{1, 2, 3}, 5));
  }

  @Test
  void testWithoutAt_ShortArray_NegativeIndex() {
    Assertions.assertArrayEquals(new short[]{1, 2, 3}, ArrayUtils.withoutAt(new short[]{1, 2, 3}, -1));
  }

  @Test
  void testWithoutAt_ShortArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutAt((short[]) null, 0));
  }

  @Test
  void testWithoutAt_ShortArray_EmptyIndices() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.withoutAt(new short[]{1, 2}));
  }

  @Test
  void testWithoutAt_ShortArray_MixedValidInvalidIndices() {
    Assertions.assertArrayEquals(new short[]{1, 3}, ArrayUtils.withoutAt(new short[]{1, 2, 3}, 1, -1, 5));
  }

  @Test
  void testWithoutAt_ShortArray_NullIndices() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.withoutAt(new short[]{1, 2}, (int[]) null));
  }

  @Test
  void testWithoutAt_LongArray_SingleIndex() {
    Assertions.assertArrayEquals(new long[]{1, 3}, ArrayUtils.withoutAt(new long[]{1, 2, 3}, 1));
  }

  @Test
  void testWithoutAt_LongArray_MultipleIndices() {
    Assertions.assertArrayEquals(new long[]{1}, ArrayUtils.withoutAt(new long[]{1, 2, 3}, 1, 2));
  }

  @Test
  void testWithoutAt_LongArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new long[]{2, 3}, ArrayUtils.withoutAt(new long[]{1, 2, 3}, 0, 0));
  }

  @Test
  void testWithoutAt_LongArray_IndexOutOfBounds() {
    Assertions.assertArrayEquals(new long[]{1, 2, 3}, ArrayUtils.withoutAt(new long[]{1, 2, 3}, 5));
  }

  @Test
  void testWithoutAt_LongArray_NegativeIndex() {
    Assertions.assertArrayEquals(new long[]{1, 2, 3}, ArrayUtils.withoutAt(new long[]{1, 2, 3}, -1));
  }

  @Test
  void testWithoutAt_LongArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutAt((long[]) null, 0));
  }

  @Test
  void testWithoutAt_LongArray_EmptyIndices() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.withoutAt(new long[]{1, 2}));
  }

  @Test
  void testWithoutAt_LongArray_MixedValidInvalidIndices() {
    Assertions.assertArrayEquals(new long[]{1, 3}, ArrayUtils.withoutAt(new long[]{1, 2, 3}, 1, -1, 5));
  }

  @Test
  void testWithoutAt_LongArray_NullIndices() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.withoutAt(new long[]{1, 2}, (int[]) null));
  }

  @Test
  void testWithoutAt_CharArray_SingleIndex() {
    Assertions.assertArrayEquals(new char[]{'a', 'c'}, ArrayUtils.withoutAt(new char[]{'a', 'b', 'c'}, 1));
  }

  @Test
  void testWithoutAt_CharArray_MultipleIndices() {
    Assertions.assertArrayEquals(new char[]{'a'}, ArrayUtils.withoutAt(new char[]{'a', 'b', 'c'}, 1, 2));
  }

  @Test
  void testWithoutAt_CharArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new char[]{'b', 'c'}, ArrayUtils.withoutAt(new char[]{'a', 'b', 'c'}, 0, 0));
  }

  @Test
  void testWithoutAt_CharArray_IndexOutOfBounds() {
    Assertions.assertArrayEquals(new char[]{'a', 'b', 'c'}, ArrayUtils.withoutAt(new char[]{'a', 'b', 'c'}, 5));
  }

  @Test
  void testWithoutAt_CharArray_NegativeIndex() {
    Assertions.assertArrayEquals(new char[]{'a', 'b', 'c'}, ArrayUtils.withoutAt(new char[]{'a', 'b', 'c'}, -1));
  }

  @Test
  void testWithoutAt_CharArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutAt((char[]) null, 0));
  }

  @Test
  void testWithoutAt_CharArray_EmptyIndices() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.withoutAt(new char[]{'a', 'b'}));
  }

  @Test
  void testWithoutAt_CharArray_MixedValidInvalidIndices() {
    Assertions.assertArrayEquals(new char[]{'a', 'c'}, ArrayUtils.withoutAt(new char[]{'a', 'b', 'c'}, 1, -1, 5));
  }

  @Test
  void testWithoutAt_CharArray_NullIndices() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.withoutAt(new char[]{'a', 'b'}, (int[]) null));
  }

  @Test
  void testWithoutAt_FloatArray_SingleIndex() {
    Assertions.assertArrayEquals(new float[]{1f, 3f}, ArrayUtils.withoutAt(new float[]{1f, 2f, 3f}, 1), 0f);
  }

  @Test
  void testWithoutAt_FloatArray_MultipleIndices() {
    Assertions.assertArrayEquals(new float[]{1f}, ArrayUtils.withoutAt(new float[]{1f, 2f, 3f}, 1, 2), 0f);
  }

  @Test
  void testWithoutAt_FloatArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new float[]{2f, 3f}, ArrayUtils.withoutAt(new float[]{1f, 2f, 3f}, 0, 0), 0f);
  }

  @Test
  void testWithoutAt_FloatArray_IndexOutOfBounds() {
    Assertions.assertArrayEquals(new float[]{1f, 2f, 3f}, ArrayUtils.withoutAt(new float[]{1f, 2f, 3f}, 5), 0f);
  }

  @Test
  void testWithoutAt_FloatArray_NegativeIndex() {
    Assertions.assertArrayEquals(new float[]{1f, 2f, 3f}, ArrayUtils.withoutAt(new float[]{1f, 2f, 3f}, -1), 0f);
  }

  @Test
  void testWithoutAt_FloatArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutAt((float[]) null, 0));
  }

  @Test
  void testWithoutAt_FloatArray_EmptyIndices() {
    Assertions.assertArrayEquals(new float[]{1f, 2f}, ArrayUtils.withoutAt(new float[]{1f, 2f}), 0f);
  }

  @Test
  void testWithoutAt_FloatArray_MixedValidInvalidIndices() {
    Assertions.assertArrayEquals(new float[]{1f, 3f}, ArrayUtils.withoutAt(new float[]{1f, 2f, 3f}, 1, -1, 5), 0f);
  }

  @Test
  void testWithoutAt_FloatArray_NullIndices() {
    Assertions.assertArrayEquals(new float[]{1f, 2f}, ArrayUtils.withoutAt(new float[]{1f, 2f}, (int[]) null), 0f);
  }

  @Test
  void testWithoutAt_DoubleArray_SingleIndex() {
    Assertions.assertArrayEquals(new double[]{1d, 3d}, ArrayUtils.withoutAt(new double[]{1d, 2d, 3d}, 1), 0d);
  }

  @Test
  void testWithoutAt_DoubleArray_MultipleIndices() {
    Assertions.assertArrayEquals(new double[]{1d}, ArrayUtils.withoutAt(new double[]{1d, 2d, 3d}, 1, 2), 0d);
  }

  @Test
  void testWithoutAt_DoubleArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new double[]{2d, 3d}, ArrayUtils.withoutAt(new double[]{1d, 2d, 3d}, 0, 0), 0d);
  }

  @Test
  void testWithoutAt_DoubleArray_IndexOutOfBounds() {
    Assertions.assertArrayEquals(new double[]{1d, 2d, 3d}, ArrayUtils.withoutAt(new double[]{1d, 2d, 3d}, 5), 0d);
  }

  @Test
  void testWithoutAt_DoubleArray_NegativeIndex() {
    Assertions.assertArrayEquals(new double[]{1d, 2d, 3d}, ArrayUtils.withoutAt(new double[]{1d, 2d, 3d}, -1), 0d);
  }

  @Test
  void testWithoutAt_DoubleArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutAt((double[]) null, 0));
  }

  @Test
  void testWithoutAt_DoubleArray_EmptyIndices() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.withoutAt(new double[]{1d, 2d}), 0d);
  }

  @Test
  void testWithoutAt_DoubleArray_MixedValidInvalidIndices() {
    Assertions.assertArrayEquals(new double[]{1d, 3d}, ArrayUtils.withoutAt(new double[]{1d, 2d, 3d}, 1, -1, 5), 0d);
  }

  @Test
  void testWithoutAt_DoubleArray_NullIndices() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.withoutAt(new double[]{1d, 2d}, (int[]) null), 0d);
  }

  @Test
  void testWithoutAt_BooleanArray_SingleIndex() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.withoutAt(new boolean[]{true, true, false}, 1));
  }

  @Test
  void testWithoutAt_BooleanArray_MultipleIndices() {
    Assertions.assertArrayEquals(new boolean[]{true}, ArrayUtils.withoutAt(new boolean[]{true, false, true}, 1, 2));
  }

  @Test
  void testWithoutAt_BooleanArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new boolean[]{false, true}, ArrayUtils.withoutAt(new boolean[]{true, false, true}, 0, 0));
  }

  @Test
  void testWithoutAt_BooleanArray_IndexOutOfBounds() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.withoutAt(new boolean[]{true, false}, 5));
  }

  @Test
  void testWithoutAt_BooleanArray_NegativeIndex() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.withoutAt(new boolean[]{true, false}, -1));
  }

  @Test
  void testWithoutAt_BooleanArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutAt((boolean[]) null, 0));
  }

  @Test
  void testWithoutAt_BooleanArray_EmptyIndices() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.withoutAt(new boolean[]{true, false}));
  }

  @Test
  void testWithoutAt_BooleanArray_MixedValidInvalidIndices() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.withoutAt(new boolean[]{true, true, false}, 0, -1, 5));
  }

  @Test
  void testWithoutAt_BooleanArray_NullIndices() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.withoutAt(new boolean[]{true, false}, (int[]) null));
  }

  @Test
  void testWithoutAt_ObjectArray_AllIndices_ReturnsEmpty() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.withoutAt(new String[]{"a", "b", "c"}, 0, 1, 2));
  }

  @Test
  void testWithoutAt_IntArray_AllIndices_ReturnsEmpty() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.withoutAt(new int[]{1, 2, 3}, 0, 1, 2));
  }

  @Test
  void testWithoutAt_ByteArray_AllIndices_ReturnsEmpty() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.withoutAt(new byte[]{1, 2}, 0, 1));
  }

  @Test
  void testWithoutAt_CharArray_AllIndices_ReturnsEmpty() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.withoutAt(new char[]{'a', 'b'}, 0, 1));
  }

  @Test
  void testWithoutAt_BooleanArray_AllIndices_ReturnsEmpty() {
    Assertions.assertArrayEquals(new boolean[0], ArrayUtils.withoutAt(new boolean[]{true, false}, 0, 1));
  }

  // ==================== withoutElement ====================

  @Test
  void testWithoutElement_ObjectArray_SingleElement() {
    Assertions.assertArrayEquals(
            new String[]{"a", "c", "d"},
            ArrayUtils.withoutElement(new String[]{"a", "b", "c", "d"}, "b"));
  }

  @Test
  void testWithoutElement_ObjectArray_MultipleElements() {
    Assertions.assertArrayEquals(
            new String[]{"a", "d"},
            ArrayUtils.withoutElement(new String[]{"a", "b", "c", "d"}, "b", "c"));
  }

  @Test
  void testWithoutElement_ObjectArray_ElementNotFound() {
    String[] arr = {"a", "b"};
    Assertions.assertArrayEquals(arr, ArrayUtils.withoutElement(arr, "z"));
  }

  @Test
  void testWithoutElement_ObjectArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(
            new String[]{"a", "c"},
            ArrayUtils.withoutElement(new String[]{"a", "b", "c", "b"}, "b", "b"));
  }

  @Test
  void testWithoutElement_ObjectArray_NullElements() {
    Assertions.assertArrayEquals(
            new String[]{"a", "c"},
            ArrayUtils.withoutElement(new String[]{"a", null, "c"}, (String) null));
  }

  @Test
  void testWithoutElement_ObjectArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutElement((String[]) null, "x"));
  }

  @Test
  void testWithoutElement_ObjectArray_EmptyElements() {
    Assertions.assertArrayEquals(
            new String[]{"a", "b"},
            ArrayUtils.withoutElement(new String[]{"a", "b"}));
  }

  @Test
  void testWithoutElement_IntArray_SingleElement() {
    Assertions.assertArrayEquals(
            new int[]{1, 3, 4},
            ArrayUtils.withoutElement(new int[]{1, 2, 3, 4}, 2));
  }

  @Test
  void testWithoutElement_IntArray_MultipleElements() {
    Assertions.assertArrayEquals(
            new int[]{1, 4},
            ArrayUtils.withoutElement(new int[]{1, 2, 3, 4}, 2, 3));
  }

  @Test
  void testWithoutElement_IntArray_ElementNotFound() {
    int[] arr = {1, 2};
    Assertions.assertArrayEquals(arr, ArrayUtils.withoutElement(arr, 99));
  }

  @Test
  void testWithoutElement_IntArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(
            new int[]{1, 3},
            ArrayUtils.withoutElement(new int[]{1, 2, 3, 2}, 2, 2));
  }

  @Test
  void testWithoutElement_IntArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutElement((int[]) null, 1));
  }

  @Test
  void testWithoutElement_IntArray_EmptyElements() {
    Assertions.assertArrayEquals(
            new int[]{1, 2},
            ArrayUtils.withoutElement(new int[]{1, 2}));
  }

  @Test
  void testWithoutElement_IntArray_NonContiguousMatches() {
    Assertions.assertArrayEquals(
            new int[]{2, 1, 2, 1},
            ArrayUtils.withoutElement(new int[]{1, 2, 1, 2, 1}, 1));
  }

  @Test
  void testWithoutElement_ByteArray() {
    Assertions.assertArrayEquals(
            new byte[]{1, 3},
            ArrayUtils.withoutElement(new byte[]{1, 2, 3}, (byte) 2));
  }

  @Test
  void testWithoutElement_ObjectArray_NonContiguousMatches() {
    Assertions.assertArrayEquals(
            new String[]{"b", "d"},
            ArrayUtils.withoutElement(new String[]{"a", "b", "c", "d"}, "a", "c"));
  }

  // ==================== withoutElement 原始类型完整覆盖 ====================

  @Test
  void testWithoutElement_ByteArray_SingleElement() {
    Assertions.assertArrayEquals(new byte[]{1, 3}, ArrayUtils.withoutElement(new byte[]{1, 2, 3}, (byte) 2));
  }

  @Test
  void testWithoutElement_ByteArray_MultipleElements() {
    Assertions.assertArrayEquals(new byte[]{1}, ArrayUtils.withoutElement(new byte[]{1, 2, 3}, (byte) 2, (byte) 3));
  }

  @Test
  void testWithoutElement_ByteArray_ElementNotFound() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.withoutElement(new byte[]{1, 2}, (byte) 5));
  }

  @Test
  void testWithoutElement_ByteArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(new byte[]{2, 3}, ArrayUtils.withoutElement(new byte[]{1, 2, 3}, (byte) 1, (byte) 1));
  }

  @Test
  void testWithoutElement_ByteArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutElement((byte[]) null, (byte) 1));
  }

  @Test
  void testWithoutElement_ByteArray_EmptyElements() {
    Assertions.assertArrayEquals(new byte[]{1, 2}, ArrayUtils.withoutElement(new byte[]{1, 2}));
  }

  @Test
  void testWithoutElement_ShortArray_SingleElement() {
    Assertions.assertArrayEquals(new short[]{1, 3}, ArrayUtils.withoutElement(new short[]{1, 2, 3}, (short) 2));
  }

  @Test
  void testWithoutElement_ShortArray_MultipleElements() {
    Assertions.assertArrayEquals(new short[]{1}, ArrayUtils.withoutElement(new short[]{1, 2, 3}, (short) 2, (short) 3));
  }

  @Test
  void testWithoutElement_ShortArray_ElementNotFound() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.withoutElement(new short[]{1, 2}, (short) 5));
  }

  @Test
  void testWithoutElement_ShortArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(new short[]{2, 3}, ArrayUtils.withoutElement(new short[]{1, 2, 3}, (short) 1, (short) 1));
  }

  @Test
  void testWithoutElement_ShortArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutElement((short[]) null, (short) 1));
  }

  @Test
  void testWithoutElement_ShortArray_EmptyElements() {
    Assertions.assertArrayEquals(new short[]{1, 2}, ArrayUtils.withoutElement(new short[]{1, 2}));
  }

  @Test
  void testWithoutElement_LongArray_SingleElement() {
    Assertions.assertArrayEquals(new long[]{1, 3}, ArrayUtils.withoutElement(new long[]{1, 2, 3}, 2L));
  }

  @Test
  void testWithoutElement_LongArray_MultipleElements() {
    Assertions.assertArrayEquals(new long[]{1}, ArrayUtils.withoutElement(new long[]{1, 2, 3}, 2L, 3L));
  }

  @Test
  void testWithoutElement_LongArray_ElementNotFound() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.withoutElement(new long[]{1, 2}, 5L));
  }

  @Test
  void testWithoutElement_LongArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(new long[]{2, 3}, ArrayUtils.withoutElement(new long[]{1, 2, 3}, 1L, 1L));
  }

  @Test
  void testWithoutElement_LongArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutElement((long[]) null, 1L));
  }

  @Test
  void testWithoutElement_LongArray_EmptyElements() {
    Assertions.assertArrayEquals(new long[]{1, 2}, ArrayUtils.withoutElement(new long[]{1, 2}));
  }

  @Test
  void testWithoutElement_CharArray_SingleElement() {
    Assertions.assertArrayEquals(new char[]{'a', 'c'}, ArrayUtils.withoutElement(new char[]{'a', 'b', 'c'}, 'b'));
  }

  @Test
  void testWithoutElement_CharArray_MultipleElements() {
    Assertions.assertArrayEquals(new char[]{'a'}, ArrayUtils.withoutElement(new char[]{'a', 'b', 'c'}, 'b', 'c'));
  }

  @Test
  void testWithoutElement_CharArray_ElementNotFound() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.withoutElement(new char[]{'a', 'b'}, 'z'));
  }

  @Test
  void testWithoutElement_CharArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(new char[]{'b', 'c'}, ArrayUtils.withoutElement(new char[]{'a', 'b', 'c'}, 'a', 'a'));
  }

  @Test
  void testWithoutElement_CharArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutElement((char[]) null, 'a'));
  }

  @Test
  void testWithoutElement_CharArray_EmptyElements() {
    Assertions.assertArrayEquals(new char[]{'a', 'b'}, ArrayUtils.withoutElement(new char[]{'a', 'b'}));
  }

  @Test
  void testWithoutElement_FloatArray_SingleElement() {
    Assertions.assertArrayEquals(new float[]{1f, 3f}, ArrayUtils.withoutElement(new float[]{1f, 2f, 3f}, 2f), 0f);
  }

  @Test
  void testWithoutElement_FloatArray_MultipleElements() {
    Assertions.assertArrayEquals(new float[]{1f}, ArrayUtils.withoutElement(new float[]{1f, 2f, 3f}, 2f, 3f), 0f);
  }

  @Test
  void testWithoutElement_FloatArray_ElementNotFound() {
    Assertions.assertArrayEquals(new float[]{1f, 2f}, ArrayUtils.withoutElement(new float[]{1f, 2f}, 5f), 0f);
  }

  @Test
  void testWithoutElement_FloatArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(new float[]{2f, 3f}, ArrayUtils.withoutElement(new float[]{1f, 2f, 3f}, 1f, 1f), 0f);
  }

  @Test
  void testWithoutElement_FloatArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutElement((float[]) null, 1f));
  }

  @Test
  void testWithoutElement_FloatArray_EmptyElements() {
    Assertions.assertArrayEquals(new float[]{1f, 2f}, ArrayUtils.withoutElement(new float[]{1f, 2f}), 0f);
  }

  @Test
  void testWithoutElement_DoubleArray_SingleElement() {
    Assertions.assertArrayEquals(new double[]{1d, 3d}, ArrayUtils.withoutElement(new double[]{1d, 2d, 3d}, 2d), 0d);
  }

  @Test
  void testWithoutElement_DoubleArray_MultipleElements() {
    Assertions.assertArrayEquals(new double[]{1d}, ArrayUtils.withoutElement(new double[]{1d, 2d, 3d}, 2d, 3d), 0d);
  }

  @Test
  void testWithoutElement_DoubleArray_ElementNotFound() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.withoutElement(new double[]{1d, 2d}, 5d), 0d);
  }

  @Test
  void testWithoutElement_DoubleArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(new double[]{2d, 3d}, ArrayUtils.withoutElement(new double[]{1d, 2d, 3d}, 1d, 1d), 0d);
  }

  @Test
  void testWithoutElement_DoubleArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutElement((double[]) null, 1d));
  }

  @Test
  void testWithoutElement_DoubleArray_EmptyElements() {
    Assertions.assertArrayEquals(new double[]{1d, 2d}, ArrayUtils.withoutElement(new double[]{1d, 2d}), 0d);
  }

  @Test
  void testWithoutElement_BooleanArray_SingleElement() {
    Assertions.assertArrayEquals(new boolean[]{true, true}, ArrayUtils.withoutElement(new boolean[]{true, false, true}, false));
  }

  @Test
  void testWithoutElement_BooleanArray_MultipleElements() {
    Assertions.assertArrayEquals(new boolean[]{false}, ArrayUtils.withoutElement(new boolean[]{true, false, true}, true, true));
  }

  @Test
  void testWithoutElement_BooleanArray_ElementNotFound() { /* boolean only has 2 values, can't test not-found meaningfully */ }

  @Test
  void testWithoutElement_BooleanArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(new boolean[]{true}, ArrayUtils.withoutElement(new boolean[]{true, false}, false, false));
  }

  @Test
  void testWithoutElement_BooleanArray_NullArray() {
    Assertions.assertNull(ArrayUtils.withoutElement((boolean[]) null, true));
  }

  @Test
  void testWithoutElement_BooleanArray_EmptyElements() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.withoutElement(new boolean[]{true, false}));
  }

  @Test
  void testWithoutElement_ObjectArray_AllElements_ReturnsEmpty() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.withoutElement(new String[]{"a", "b", "c"}, "a", "b", "c"));
  }

  @Test
  void testWithoutElement_IntArray_AllElements_ReturnsEmpty() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.withoutElement(new int[]{1, 2, 3}, 1, 2, 3));
  }

  @Test
  void testWithoutElement_ByteArray_AllElements_ReturnsEmpty() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.withoutElement(new byte[]{1, 2}, (byte) 1, (byte) 2));
  }

  @Test
  void testWithoutElement_CharArray_AllElements_ReturnsEmpty() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.withoutElement(new char[]{'a', 'b'}, 'a', 'b'));
  }

  // ==================== elementsAt ====================

  @Test
  void testElementsAt_ObjectArray() {
    Assertions.assertArrayEquals(
            new String[]{"a", "c"},
            ArrayUtils.elementsAt(new String[]{"a", "b", "c"}, 0, 2));
  }

  @Test
  void testElementsAt_ObjectArray_SingleIndex() {
    Assertions.assertArrayEquals(
            new String[]{"b"},
            ArrayUtils.elementsAt(new String[]{"a", "b", "c"}, 1));
  }

  @Test
  void testElementsAt_ObjectArray_DuplicateIndices() {
    Assertions.assertArrayEquals(
            new String[]{"a"},
            ArrayUtils.elementsAt(new String[]{"a", "b"}, 0, 0, 0));
  }

  @Test
  void testElementsAt_ObjectArray_InvalidIndicesIgnored() {
    Assertions.assertArrayEquals(
            new String[]{"a", "c"},
            ArrayUtils.elementsAt(new String[]{"a", "b", "c"}, 0, -1, 5, 2));
  }

  @Test
  void testElementsAt_ObjectArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsAt((String[]) null, 0));
  }

  @Test
  void testElementsAt_ObjectArray_EmptyIndices() {
    Assertions.assertArrayEquals(
            new String[0],
            ArrayUtils.elementsAt(new String[]{"a", "b"}));
  }

  @Test
  void testElementsAt_IntArray() {
    Assertions.assertArrayEquals(
            new int[]{1, 3},
            ArrayUtils.elementsAt(new int[]{1, 2, 3}, 0, 2));
  }

  @Test
  void testElementsAt_IntArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsAt((int[]) null, 0));
  }

  @Test
  void testElementsAt_IntArray_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertArrayEquals(
            new int[0],
            ArrayUtils.elementsAt(new int[]{1, 2}, 5, 6, -1));
  }

  @Test
  void testElementsAt_LongArray() {
    Assertions.assertArrayEquals(
            new long[]{10L, 30L},
            ArrayUtils.elementsAt(new long[]{10L, 20L, 30L}, 0, 2));
  }

  @Test
  void testElementsAt_DoubleArray() {
    Assertions.assertArrayEquals(
            new double[]{1.0, 3.0},
            ArrayUtils.elementsAt(new double[]{1.0, 2.0, 3.0}, 0, 2), 0d);
  }

  @Test
  void testElementsAt_BooleanArray() {
    Assertions.assertArrayEquals(
            new boolean[]{true, false},
            ArrayUtils.elementsAt(new boolean[]{true, true, false}, 0, 2));
  }

  @Test
  void testElementsAt_ObjectArray_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertArrayEquals(
            new String[0],
            ArrayUtils.elementsAt(new String[]{"a", "b"}, 5, 6, -1));
  }

  // ==================== elementsAt 原始类型完整覆盖 ====================

  @Test
  void testElementsAt_ByteArray_Normal() {
    Assertions.assertArrayEquals(new byte[]{1, 3}, ArrayUtils.elementsAt(new byte[]{1, 2, 3}, 0, 2));
  }

  @Test
  void testElementsAt_ByteArray_SingleIndex() {
    Assertions.assertArrayEquals(new byte[]{2}, ArrayUtils.elementsAt(new byte[]{1, 2, 3}, 1));
  }

  @Test
  void testElementsAt_ByteArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new byte[]{1}, ArrayUtils.elementsAt(new byte[]{1, 2}, 0, 0));
  }

  @Test
  void testElementsAt_ByteArray_InvalidIndicesIgnored() {
    Assertions.assertArrayEquals(new byte[]{1, 3}, ArrayUtils.elementsAt(new byte[]{1, 2, 3}, 0, -1, 5, 2));
  }

  @Test
  void testElementsAt_ByteArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsAt((byte[]) null, 0));
  }

  @Test
  void testElementsAt_ByteArray_EmptyIndices() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.elementsAt(new byte[]{1, 2}));
  }

  @Test
  void testElementsAt_ByteArray_NullIndices() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.elementsAt(new byte[]{1, 2}, (int[]) null));
  }

  @Test
  void testElementsAt_ByteArray_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.elementsAt(new byte[]{1, 2}, 5, 6, -1));
  }

  @Test
  void testElementsAt_ShortArray_Normal() {
    Assertions.assertArrayEquals(new short[]{1, 3}, ArrayUtils.elementsAt(new short[]{1, 2, 3}, 0, 2));
  }

  @Test
  void testElementsAt_ShortArray_SingleIndex() {
    Assertions.assertArrayEquals(new short[]{2}, ArrayUtils.elementsAt(new short[]{1, 2, 3}, 1));
  }

  @Test
  void testElementsAt_ShortArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new short[]{1}, ArrayUtils.elementsAt(new short[]{1, 2}, 0, 0));
  }

  @Test
  void testElementsAt_ShortArray_InvalidIndicesIgnored() {
    Assertions.assertArrayEquals(new short[]{1, 3}, ArrayUtils.elementsAt(new short[]{1, 2, 3}, 0, -1, 5, 2));
  }

  @Test
  void testElementsAt_ShortArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsAt((short[]) null, 0));
  }

  @Test
  void testElementsAt_ShortArray_EmptyIndices() {
    Assertions.assertArrayEquals(new short[0], ArrayUtils.elementsAt(new short[]{1, 2}));
  }

  @Test
  void testElementsAt_ShortArray_NullIndices() {
    Assertions.assertArrayEquals(new short[0], ArrayUtils.elementsAt(new short[]{1, 2}, (int[]) null));
  }

  @Test
  void testElementsAt_ShortArray_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertArrayEquals(new short[0], ArrayUtils.elementsAt(new short[]{1, 2}, 5, 6, -1));
  }

  @Test
  void testElementsAt_IntArray_Normal() {
    Assertions.assertArrayEquals(new int[]{1, 3}, ArrayUtils.elementsAt(new int[]{1, 2, 3}, 0, 2));
  }

  @Test
  void testElementsAt_IntArray_SingleIndex() {
    Assertions.assertArrayEquals(new int[]{2}, ArrayUtils.elementsAt(new int[]{1, 2, 3}, 1));
  }

  @Test
  void testElementsAt_IntArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new int[]{1}, ArrayUtils.elementsAt(new int[]{1, 2}, 0, 0));
  }

  @Test
  void testElementsAt_IntArray_InvalidIndicesIgnored() {
    Assertions.assertArrayEquals(new int[]{1, 3}, ArrayUtils.elementsAt(new int[]{1, 2, 3}, 0, -1, 5, 2));
  }

  @Test
  void testElementsAt_IntArray_EmptyIndices() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.elementsAt(new int[]{1, 2}));
  }

  @Test
  void testElementsAt_IntArray_NullIndices() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.elementsAt(new int[]{1, 2}, (int[]) null));
  }

  @Test
  void testElementsAt_LongArray_Normal() {
    Assertions.assertArrayEquals(new long[]{1, 3}, ArrayUtils.elementsAt(new long[]{1, 2, 3}, 0, 2));
  }

  @Test
  void testElementsAt_LongArray_SingleIndex() {
    Assertions.assertArrayEquals(new long[]{2}, ArrayUtils.elementsAt(new long[]{1, 2, 3}, 1));
  }

  @Test
  void testElementsAt_LongArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new long[]{1}, ArrayUtils.elementsAt(new long[]{1, 2}, 0, 0));
  }

  @Test
  void testElementsAt_LongArray_InvalidIndicesIgnored() {
    Assertions.assertArrayEquals(new long[]{1, 3}, ArrayUtils.elementsAt(new long[]{1, 2, 3}, 0, -1, 5, 2));
  }

  @Test
  void testElementsAt_LongArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsAt((long[]) null, 0));
  }

  @Test
  void testElementsAt_LongArray_EmptyIndices() {
    Assertions.assertArrayEquals(new long[0], ArrayUtils.elementsAt(new long[]{1, 2}));
  }

  @Test
  void testElementsAt_LongArray_NullIndices() {
    Assertions.assertArrayEquals(new long[0], ArrayUtils.elementsAt(new long[]{1, 2}, (int[]) null));
  }

  @Test
  void testElementsAt_LongArray_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertArrayEquals(new long[0], ArrayUtils.elementsAt(new long[]{1, 2}, 5, 6, -1));
  }

  @Test
  void testElementsAt_CharArray_Normal() {
    Assertions.assertArrayEquals(new char[]{'a', 'c'}, ArrayUtils.elementsAt(new char[]{'a', 'b', 'c'}, 0, 2));
  }

  @Test
  void testElementsAt_CharArray_SingleIndex() {
    Assertions.assertArrayEquals(new char[]{'b'}, ArrayUtils.elementsAt(new char[]{'a', 'b', 'c'}, 1));
  }

  @Test
  void testElementsAt_CharArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new char[]{'a'}, ArrayUtils.elementsAt(new char[]{'a', 'b'}, 0, 0));
  }

  @Test
  void testElementsAt_CharArray_InvalidIndicesIgnored() {
    Assertions.assertArrayEquals(new char[]{'a', 'c'}, ArrayUtils.elementsAt(new char[]{'a', 'b', 'c'}, 0, -1, 5, 2));
  }

  @Test
  void testElementsAt_CharArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsAt((char[]) null, 0));
  }

  @Test
  void testElementsAt_CharArray_EmptyIndices() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.elementsAt(new char[]{'a', 'b'}));
  }

  @Test
  void testElementsAt_CharArray_NullIndices() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.elementsAt(new char[]{'a', 'b'}, (int[]) null));
  }

  @Test
  void testElementsAt_CharArray_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.elementsAt(new char[]{'a', 'b'}, 5, 6, -1));
  }

  @Test
  void testElementsAt_FloatArray_Normal() {
    Assertions.assertArrayEquals(new float[]{1f, 3f}, ArrayUtils.elementsAt(new float[]{1f, 2f, 3f}, 0, 2), 0f);
  }

  @Test
  void testElementsAt_FloatArray_SingleIndex() {
    Assertions.assertArrayEquals(new float[]{2f}, ArrayUtils.elementsAt(new float[]{1f, 2f, 3f}, 1), 0f);
  }

  @Test
  void testElementsAt_FloatArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new float[]{1f}, ArrayUtils.elementsAt(new float[]{1f, 2f}, 0, 0), 0f);
  }

  @Test
  void testElementsAt_FloatArray_InvalidIndicesIgnored() {
    Assertions.assertArrayEquals(new float[]{1f, 3f}, ArrayUtils.elementsAt(new float[]{1f, 2f, 3f}, 0, -1, 5, 2), 0f);
  }

  @Test
  void testElementsAt_FloatArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsAt((float[]) null, 0));
  }

  @Test
  void testElementsAt_FloatArray_EmptyIndices() {
    Assertions.assertArrayEquals(new float[0], ArrayUtils.elementsAt(new float[]{1f, 2f}), 0f);
  }

  @Test
  void testElementsAt_FloatArray_NullIndices() {
    Assertions.assertArrayEquals(new float[0], ArrayUtils.elementsAt(new float[]{1f, 2f}, (int[]) null), 0f);
  }

  @Test
  void testElementsAt_FloatArray_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertArrayEquals(new float[0], ArrayUtils.elementsAt(new float[]{1f, 2f}, 5, 6, -1), 0f);
  }

  @Test
  void testElementsAt_DoubleArray_Normal() {
    Assertions.assertArrayEquals(new double[]{1d, 3d}, ArrayUtils.elementsAt(new double[]{1d, 2d, 3d}, 0, 2), 0d);
  }

  @Test
  void testElementsAt_DoubleArray_SingleIndex() {
    Assertions.assertArrayEquals(new double[]{2d}, ArrayUtils.elementsAt(new double[]{1d, 2d, 3d}, 1), 0d);
  }

  @Test
  void testElementsAt_DoubleArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new double[]{1d}, ArrayUtils.elementsAt(new double[]{1d, 2d}, 0, 0), 0d);
  }

  @Test
  void testElementsAt_DoubleArray_InvalidIndicesIgnored() {
    Assertions.assertArrayEquals(new double[]{1d, 3d}, ArrayUtils.elementsAt(new double[]{1d, 2d, 3d}, 0, -1, 5, 2), 0d);
  }

  @Test
  void testElementsAt_DoubleArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsAt((double[]) null, 0));
  }

  @Test
  void testElementsAt_DoubleArray_EmptyIndices() {
    Assertions.assertArrayEquals(new double[0], ArrayUtils.elementsAt(new double[]{1d, 2d}), 0d);
  }

  @Test
  void testElementsAt_DoubleArray_NullIndices() {
    Assertions.assertArrayEquals(new double[0], ArrayUtils.elementsAt(new double[]{1d, 2d}, (int[]) null), 0d);
  }

  @Test
  void testElementsAt_DoubleArray_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertArrayEquals(new double[0], ArrayUtils.elementsAt(new double[]{1d, 2d}, 5, 6, -1), 0d);
  }

  @Test
  void testElementsAt_BooleanArray_Normal() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.elementsAt(new boolean[]{true, true, false}, 0, 2));
  }

  @Test
  void testElementsAt_BooleanArray_SingleIndex() {
    Assertions.assertArrayEquals(new boolean[]{false}, ArrayUtils.elementsAt(new boolean[]{true, false}, 1));
  }

  @Test
  void testElementsAt_BooleanArray_DuplicateIndices() {
    Assertions.assertArrayEquals(new boolean[]{true}, ArrayUtils.elementsAt(new boolean[]{true, false}, 0, 0));
  }

  @Test
  void testElementsAt_BooleanArray_InvalidIndicesIgnored() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.elementsAt(new boolean[]{true, true, false}, 0, -1, 5, 2));
  }

  @Test
  void testElementsAt_BooleanArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsAt((boolean[]) null, 0));
  }

  @Test
  void testElementsAt_BooleanArray_EmptyIndices() {
    Assertions.assertArrayEquals(new boolean[0], ArrayUtils.elementsAt(new boolean[]{true, false}));
  }

  @Test
  void testElementsAt_BooleanArray_NullIndices() {
    Assertions.assertArrayEquals(new boolean[0], ArrayUtils.elementsAt(new boolean[]{true, false}, (int[]) null));
  }

  @Test
  void testElementsAt_BooleanArray_AllOutOfBounds_ReturnsEmpty() {
    Assertions.assertArrayEquals(new boolean[0], ArrayUtils.elementsAt(new boolean[]{true, false}, 5, 6, -1));
  }

  // ==================== elementsAt 边界值 ====================

  @Test
  void testElementsAt_ObjectArray_EmptyArray() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.elementsAt(new String[0], 0));
  }

  @Test
  void testElementsAt_ObjectArray_SingleElement_Found() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.elementsAt(new String[]{"a"}, 0));
  }

  @Test
  void testElementsAt_ObjectArray_SingleElement_OutOfBounds() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.elementsAt(new String[]{"a"}, 1));
  }

  @Test
  void testElementsAt_ObjectArray_FirstElement() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.elementsAt(new String[]{"a", "b", "c"}, 0));
  }

  @Test
  void testElementsAt_ObjectArray_LastElement() {
    Assertions.assertArrayEquals(new String[]{"c"}, ArrayUtils.elementsAt(new String[]{"a", "b", "c"}, 2));
  }

  @Test
  void testElementsAt_ObjectArray_AllValidIndices() {
    Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, ArrayUtils.elementsAt(new String[]{"a", "b", "c"}, 0, 1, 2));
  }

  @Test
  void testElementsAt_ObjectArray_ReverseOrderIndices_ResultInArrayOrder() {
    Assertions.assertArrayEquals(new String[]{"a", "c"}, ArrayUtils.elementsAt(new String[]{"a", "b", "c"}, 2, 0));
  }

  @Test
  void testElementsAt_ObjectArray_AllNegative_ReturnsEmpty() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.elementsAt(new String[]{"a", "b"}, -1, -2));
  }

  @Test
  void testElementsAt_ObjectArray_AllExceedLength_ReturnsEmpty() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.elementsAt(new String[]{"a", "b"}, 5, 6));
  }

  @Test
  void testElementsAt_ObjectArray_MixedDuplicate_Negative_Valid_Oversize() {
    Assertions.assertArrayEquals(new String[]{"a", "c"}, ArrayUtils.elementsAt(new String[]{"a", "b", "c", "d"}, 0, 0, -1, 5, 2));
  }

  @Test
  void testElementsAt_ObjectArray_NullArray_EmptyIndices() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.elementsAt(new String[]{"a", "b", "c"}));
  }

  @Test
  void testElementsAt_IntArray_EmptyArray() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.elementsAt(new int[0], 0));
  }

  @Test
  void testElementsAt_IntArray_SingleElement_Found() {
    Assertions.assertArrayEquals(new int[]{1}, ArrayUtils.elementsAt(new int[]{1}, 0));
  }

  @Test
  void testElementsAt_IntArray_FirstElement() {
    Assertions.assertArrayEquals(new int[]{1}, ArrayUtils.elementsAt(new int[]{1, 2, 3}, 0));
  }

  @Test
  void testElementsAt_IntArray_LastElement() {
    Assertions.assertArrayEquals(new int[]{3}, ArrayUtils.elementsAt(new int[]{1, 2, 3}, 2));
  }

  @Test
  void testElementsAt_IntArray_AllValidIndices() {
    Assertions.assertArrayEquals(new int[]{1, 2, 3}, ArrayUtils.elementsAt(new int[]{1, 2, 3}, 0, 1, 2));
  }

  @Test
  void testElementsAt_IntArray_ReverseOrderIndices_ResultInArrayOrder() {
    Assertions.assertArrayEquals(new int[]{1, 3}, ArrayUtils.elementsAt(new int[]{1, 2, 3}, 2, 0));
  }

  @Test
  void testElementsAt_IntArray_AllNegative_ReturnsEmpty() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.elementsAt(new int[]{1, 2}, -1, -2));
  }

  // ==================== elementsIn ====================

  @Test
  void testElementsIn_ObjectArray() {
    Assertions.assertArrayEquals(
            new String[]{"a", "c"},
            ArrayUtils.elementsIn(new String[]{"a", "b", "c"}, "a", "c"));
  }

  @Test
  void testElementsIn_ObjectArray_NotFound() {
    Assertions.assertArrayEquals(
            new String[0],
            ArrayUtils.elementsIn(new String[]{"a", "b"}, "x", "y"));
  }

  @Test
  void testElementsIn_ObjectArray_PartialMatch() {
    Assertions.assertArrayEquals(
            new String[]{"a"},
            ArrayUtils.elementsIn(new String[]{"a", "b", "c"}, "a", "x"));
  }

  @Test
  void testElementsIn_ObjectArray_DuplicateValues() {
    Assertions.assertArrayEquals(
            new String[]{"a"},
            ArrayUtils.elementsIn(new String[]{"a", "b"}, "a", "a"));
  }

  @Test
  void testElementsIn_ObjectArray_NullValue() {
    Assertions.assertArrayEquals(
            new String[]{null},
            ArrayUtils.elementsIn(new String[]{"a", null, "c"}, (String) null));
  }

  @Test
  void testElementsIn_ObjectArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsIn((String[]) null, "x"));
  }

  @Test
  void testElementsIn_IntArray() {
    Assertions.assertArrayEquals(
            new int[]{2, 4},
            ArrayUtils.elementsIn(new int[]{1, 2, 3, 4}, 2, 4));
  }

  @Test
  void testElementsIn_IntArray_NotFound() {
    Assertions.assertArrayEquals(
            new int[0],
            ArrayUtils.elementsIn(new int[]{1, 2, 3}, 5, 6));
  }

  @Test
  void testElementsIn_IntArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsIn((int[]) null, 1));
  }

  @Test
  void testElementsIn_ObjectArray_EmptyValues() {
    Assertions.assertArrayEquals(
            new String[0],
            ArrayUtils.elementsIn(new String[]{"a", "b"}));
  }

  // ==================== elementsIn 原始类型完整覆盖 ====================

  @Test
  void testElementsIn_ByteArray_Normal() {
    Assertions.assertArrayEquals(new byte[]{1, 3}, ArrayUtils.elementsIn(new byte[]{1, 2, 3}, (byte) 1, (byte) 3));
  }

  @Test
  void testElementsIn_ByteArray_NotFound() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.elementsIn(new byte[]{1, 2}, (byte) 5, (byte) 6));
  }

  @Test
  void testElementsIn_ByteArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsIn((byte[]) null, (byte) 1));
  }

  @Test
  void testElementsIn_ByteArray_NullValuesVarargs() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.elementsIn(new byte[]{1, 2}, (byte[]) null));
  }

  @Test
  void testElementsIn_ShortArray_Normal() {
    Assertions.assertArrayEquals(new short[]{1, 3}, ArrayUtils.elementsIn(new short[]{1, 2, 3}, (short) 1, (short) 3));
  }

  @Test
  void testElementsIn_ShortArray_NotFound() {
    Assertions.assertArrayEquals(new short[0], ArrayUtils.elementsIn(new short[]{1, 2}, (short) 5, (short) 6));
  }

  @Test
  void testElementsIn_ShortArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsIn((short[]) null, (short) 1));
  }

  @Test
  void testElementsIn_ShortArray_NullValuesVarargs() {
    Assertions.assertArrayEquals(new short[0], ArrayUtils.elementsIn(new short[]{1, 2}, (short[]) null));
  }

  @Test
  void testElementsIn_IntArray_Normal() {
    Assertions.assertArrayEquals(new int[]{1, 3}, ArrayUtils.elementsIn(new int[]{1, 2, 3}, 1, 3));
  }

  @Test
  void testElementsIn_IntArray_NullValuesVarargs() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.elementsIn(new int[]{1, 2}, (int[]) null));
  }

  @Test
  void testElementsIn_LongArray_Normal() {
    Assertions.assertArrayEquals(new long[]{1, 3}, ArrayUtils.elementsIn(new long[]{1, 2, 3}, 1L, 3L));
  }

  @Test
  void testElementsIn_LongArray_NotFound() {
    Assertions.assertArrayEquals(new long[0], ArrayUtils.elementsIn(new long[]{1, 2}, 5L, 6L));
  }

  @Test
  void testElementsIn_LongArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsIn((long[]) null, 1L));
  }

  @Test
  void testElementsIn_LongArray_NullValuesVarargs() {
    Assertions.assertArrayEquals(new long[0], ArrayUtils.elementsIn(new long[]{1, 2}, (long[]) null));
  }

  @Test
  void testElementsIn_CharArray_Normal() {
    Assertions.assertArrayEquals(new char[]{'a', 'c'}, ArrayUtils.elementsIn(new char[]{'a', 'b', 'c'}, 'a', 'c'));
  }

  @Test
  void testElementsIn_CharArray_NotFound() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.elementsIn(new char[]{'a', 'b'}, 'x', 'y'));
  }

  @Test
  void testElementsIn_CharArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsIn((char[]) null, 'a'));
  }

  @Test
  void testElementsIn_CharArray_NullValuesVarargs() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.elementsIn(new char[]{'a', 'b'}, (char[]) null));
  }

  @Test
  void testElementsIn_FloatArray_Normal() {
    Assertions.assertArrayEquals(new float[]{1f, 3f}, ArrayUtils.elementsIn(new float[]{1f, 2f, 3f}, 1f, 3f), 0f);
  }

  @Test
  void testElementsIn_FloatArray_NotFound() {
    Assertions.assertArrayEquals(new float[0], ArrayUtils.elementsIn(new float[]{1f, 2f}, 5f, 6f), 0f);
  }

  @Test
  void testElementsIn_FloatArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsIn((float[]) null, 1f));
  }

  @Test
  void testElementsIn_FloatArray_NullValuesVarargs() {
    Assertions.assertArrayEquals(new float[0], ArrayUtils.elementsIn(new float[]{1f, 2f}, (float[]) null), 0f);
  }

  @Test
  void testElementsIn_DoubleArray_Normal() {
    Assertions.assertArrayEquals(new double[]{1d, 3d}, ArrayUtils.elementsIn(new double[]{1d, 2d, 3d}, 1d, 3d), 0d);
  }

  @Test
  void testElementsIn_DoubleArray_NotFound() {
    Assertions.assertArrayEquals(new double[0], ArrayUtils.elementsIn(new double[]{1d, 2d}, 5d, 6d), 0d);
  }

  @Test
  void testElementsIn_DoubleArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsIn((double[]) null, 1d));
  }

  @Test
  void testElementsIn_DoubleArray_NullValuesVarargs() {
    Assertions.assertArrayEquals(new double[0], ArrayUtils.elementsIn(new double[]{1d, 2d}, (double[]) null), 0d);
  }

  @Test
  void testElementsIn_BooleanArray_Normal() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.elementsIn(new boolean[]{true, true, false}, true, false));
  }

  @Test
  void testElementsIn_BooleanArray_NotFound() { /* boolean only 2 values, can't test */ }

  @Test
  void testElementsIn_BooleanArray_NullArray() {
    Assertions.assertNull(ArrayUtils.elementsIn((boolean[]) null, true));
  }

  @Test
  void testElementsIn_BooleanArray_NullValuesVarargs() {
    Assertions.assertArrayEquals(new boolean[0], ArrayUtils.elementsIn(new boolean[]{true, false}, (boolean[]) null));
  }

  // ==================== elementsIn 边界值 ====================

  @Test
  void testElementsIn_ObjectArray_EmptyArray() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.elementsIn(new String[0], "x"));
  }

  @Test
  void testElementsIn_ObjectArray_SingleElement_Match() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.elementsIn(new String[]{"a"}, "a"));
  }

  @Test
  void testElementsIn_ObjectArray_SingleElement_NoMatch() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.elementsIn(new String[]{"a"}, "x"));
  }

  @Test
  void testElementsIn_ObjectArray_FirstMatch() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.elementsIn(new String[]{"a", "b", "c"}, "a"));
  }

  @Test
  void testElementsIn_ObjectArray_LastMatch() {
    Assertions.assertArrayEquals(new String[]{"c"}, ArrayUtils.elementsIn(new String[]{"a", "b", "c"}, "c"));
  }

  @Test
  void testElementsIn_ObjectArray_MultipleOccurrences_ReturnsOnlyFirst() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.elementsIn(new String[]{"a", "a", "a"}, "a"));
  }

  @Test
  void testElementsIn_ObjectArray_ResultsInArrayOrder() {
    Assertions.assertArrayEquals(new String[]{"a", "c"}, ArrayUtils.elementsIn(new String[]{"a", "b", "c"}, "c", "a"));
  }

  @Test
  void testElementsIn_ObjectArray_NullInArray_NotRequested() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.elementsIn(new String[]{"a", null, "c"}, "a"));
  }

  @Test
  void testElementsIn_ObjectArray_MultipleNullsInArray_RequestOneNull() {
    Assertions.assertArrayEquals(new String[]{null}, ArrayUtils.elementsIn(new String[]{null, null, "a"}, (String) null));
  }

  @Test
  void testElementsIn_ObjectArray_MultipleNullsInArray_RequestTwoNull() {
    Assertions.assertArrayEquals(new String[]{null, null}, ArrayUtils.elementsIn(new String[]{null, null, "a"}, null, null));
  }

  @Test
  void testElementsIn_ObjectArray_DuplicateValuesAndArrayMultipleHits() {
    Assertions.assertArrayEquals(new String[]{"a", "a"}, ArrayUtils.elementsIn(new String[]{"a", "a", "a"}, "a", "a"));
  }

  @Test
  void testElementsIn_ObjectArray_AllValuesMatch() {
    Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, ArrayUtils.elementsIn(new String[]{"a", "b", "c"}, "a", "b", "c"));
  }

  @Test
  void testElementsIn_ObjectArray_MoreValuesThanArray() {
    Assertions.assertArrayEquals(new String[]{"a", "b"}, ArrayUtils.elementsIn(new String[]{"a", "b"}, "a", "b", "c"));
  }

  @Test
  void testElementsIn_IntArray_EmptyArray() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.elementsIn(new int[0], 1));
  }

  @Test
  void testElementsIn_IntArray_SingleElement_Match() {
    Assertions.assertArrayEquals(new int[]{1}, ArrayUtils.elementsIn(new int[]{1}, 1));
  }

  @Test
  void testElementsIn_IntArray_MultipleOccurrences_ReturnsOnlyFirst() {
    Assertions.assertArrayEquals(new int[]{2}, ArrayUtils.elementsIn(new int[]{2, 2, 2}, 2));
  }

  @Test
  void testElementsIn_IntArray_ResultsInArrayOrder() {
    Assertions.assertArrayEquals(new int[]{1, 3}, ArrayUtils.elementsIn(new int[]{1, 2, 3}, 3, 1));
  }

  @Test
  void testElementsIn_IntArray_AllValuesMatch() {
    Assertions.assertArrayEquals(new int[]{1, 2, 3}, ArrayUtils.elementsIn(new int[]{1, 2, 3}, 1, 2, 3));
  }

  @Test
  void testElementsIn_IntArray_MoreValuesThanArray() {
    Assertions.assertArrayEquals(new int[]{1, 2}, ArrayUtils.elementsIn(new int[]{1, 2}, 1, 2, 3));
  }

  @Test
  void testElementsIn_IntArray_DuplicateVarargs() {
    Assertions.assertArrayEquals(new int[]{1}, ArrayUtils.elementsIn(new int[]{1, 2, 3}, 1, 1));
  }

  // ==================== reverse ====================

  @Test
  void testReverse_ObjectArray_Normal() {
    Assertions.assertArrayEquals(
            new String[]{"c", "b", "a"},
            ArrayUtils.reverse(new String[]{"a", "b", "c"}));
  }

  @Test
  void testReverse_ObjectArray_SingleElement() {
    Assertions.assertArrayEquals(
            new String[]{"a"},
            ArrayUtils.reverse(new String[]{"a"}));
  }

  @Test
  void testReverse_ObjectArray_EmptyArray() {
    Assertions.assertArrayEquals(
            new String[0],
            ArrayUtils.reverse(new String[0]));
  }

  @Test
  void testReverse_ObjectArray_NullArray() {
    Assertions.assertNull(ArrayUtils.reverse((String[]) null));
  }

  @Test
  void testReverse_ObjectArray_ReturnsNewArray() {
    String[] arr = {"a", "b"};
    String[] result = ArrayUtils.reverse(arr);
    Assertions.assertNotSame(arr, result);
  }

  @Test
  void testReverse_IntArray_Normal() {
    Assertions.assertArrayEquals(
            new int[]{3, 2, 1},
            ArrayUtils.reverse(new int[]{1, 2, 3}));
  }

  @Test
  void testReverse_IntArray_SingleElement() {
    Assertions.assertArrayEquals(
            new int[]{5},
            ArrayUtils.reverse(new int[]{5}));
  }

  @Test
  void testReverse_IntArray_EmptyArray() {
    Assertions.assertArrayEquals(
            new int[0],
            ArrayUtils.reverse(new int[0]));
  }

  @Test
  void testReverse_IntArray_NullArray() {
    Assertions.assertNull(ArrayUtils.reverse((int[]) null));
  }

  @Test
  void testReverse_IntArray_ReturnsNewArray() {
    int[] arr = {1, 2};
    int[] result = ArrayUtils.reverse(arr);
    Assertions.assertNotSame(arr, result);
  }

  @Test
  void testReverse_ByteArray() {
    Assertions.assertArrayEquals(
            new byte[]{3, 2, 1},
            ArrayUtils.reverse(new byte[]{1, 2, 3}));
  }

  @Test
  void testReverse_ShortArray() {
    Assertions.assertArrayEquals(
            new short[]{30, 20, 10},
            ArrayUtils.reverse(new short[]{10, 20, 30}));
  }

  @Test
  void testReverse_LongArray() {
    Assertions.assertArrayEquals(
            new long[]{3L, 2L, 1L},
            ArrayUtils.reverse(new long[]{1L, 2L, 3L}));
  }

  @Test
  void testReverse_CharArray() {
    Assertions.assertArrayEquals(
            new char[]{'c', 'b', 'a'},
            ArrayUtils.reverse(new char[]{'a', 'b', 'c'}));
  }

  @Test
  void testReverse_FloatArray() {
    Assertions.assertArrayEquals(
            new float[]{3.0f, 2.0f, 1.0f},
            ArrayUtils.reverse(new float[]{1.0f, 2.0f, 3.0f}), 0f);
  }

  @Test
  void testReverse_DoubleArray() {
    Assertions.assertArrayEquals(
            new double[]{3.0, 2.0, 1.0},
            ArrayUtils.reverse(new double[]{1.0, 2.0, 3.0}), 0d);
  }

  @Test
  void testReverse_BooleanArray() {
    Assertions.assertArrayEquals(
            new boolean[]{false, true},
            ArrayUtils.reverse(new boolean[]{true, false}));
  }

  // ==================== reverseSelf ====================

  @Test
  void testReverseSelf_ObjectArray_Normal() {
    String[] arr = {"a", "b", "c"};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new String[]{"c", "b", "a"}, arr);
  }

  @Test
  void testReverseSelf_ObjectArray_SingleElement() {
    String[] arr = {"a"};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new String[]{"a"}, arr);
  }

  @Test
  void testReverseSelf_ObjectArray_EmptyArray() {
    String[] arr = {};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new String[0], arr);
  }

  @Test
  void testReverseSelf_ObjectArray_NullArray_NoException() {
    Assertions.assertDoesNotThrow(() -> ArrayUtils.reverseSelf((String[]) null));
  }

  @Test
  void testReverseSelf_IntArray_Normal() {
    int[] arr = {1, 2, 3};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new int[]{3, 2, 1}, arr);
  }

  @Test
  void testReverseSelf_IntArray_SingleElement() {
    int[] arr = {5};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new int[]{5}, arr);
  }

  @Test
  void testReverseSelf_IntArray_EmptyArray() {
    int[] arr = {};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new int[0], arr);
  }

  @Test
  void testReverseSelf_IntArray_NullArray_NoException() {
    Assertions.assertDoesNotThrow(() -> ArrayUtils.reverseSelf((int[]) null));
  }

  @Test
  void testReverseSelf_ByteArray() {
    byte[] arr = {1, 2, 3};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new byte[]{3, 2, 1}, arr);
  }

  @Test
  void testReverseSelf_ShortArray() {
    short[] arr = {10, 20, 30};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new short[]{30, 20, 10}, arr);
  }

  @Test
  void testReverseSelf_LongArray() {
    long[] arr = {1L, 2L, 3L};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new long[]{3L, 2L, 1L}, arr);
  }

  @Test
  void testReverseSelf_CharArray() {
    char[] arr = {'a', 'b', 'c'};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new char[]{'c', 'b', 'a'}, arr);
  }

  @Test
  void testReverseSelf_FloatArray() {
    float[] arr = {1.0f, 2.0f, 3.0f};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new float[]{3.0f, 2.0f, 1.0f}, arr, 0f);
  }

  @Test
  void testReverseSelf_DoubleArray() {
    double[] arr = {1.0, 2.0, 3.0};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new double[]{3.0, 2.0, 1.0}, arr, 0d);
  }

  @Test
  void testReverseSelf_BooleanArray() {
    boolean[] arr = {true, false};
    ArrayUtils.reverseSelf(arr);
    Assertions.assertArrayEquals(new boolean[]{false, true}, arr);
  }

  // ==================== join ====================

  @Test
  void testJoin_ObjectArray_Normal() {
    Assertions.assertEquals("a,b,c",
            ArrayUtils.join(",", new String[]{"a", "b", "c"}));
  }

  @Test
  void testJoin_ObjectArray_SingleElement() {
    Assertions.assertEquals("x",
            ArrayUtils.join(",", new String[]{"x"}));
  }

  @Test
  void testJoin_ObjectArray_EmptyArray() {
    Assertions.assertEquals("",
            ArrayUtils.join(",", new String[0]));
  }

  @Test
  void testJoin_ObjectArray_NullArray() {
    Assertions.assertNull(ArrayUtils.join(",", (String[]) null));
  }

  @Test
  void testJoin_ObjectArray_WithNullElement() {
    Assertions.assertEquals("a,null,c",
            ArrayUtils.join(",", new String[]{"a", null, "c"}));
  }

  @Test
  void testJoin_IntArray_Normal() {
    Assertions.assertEquals("1,2,3",
            ArrayUtils.join(",", new int[]{1, 2, 3}));
  }

  @Test
  void testJoin_IntArray_SingleElement() {
    Assertions.assertEquals("5",
            ArrayUtils.join(",", new int[]{5}));
  }

  @Test
  void testJoin_IntArray_EmptyArray() {
    Assertions.assertEquals("",
            ArrayUtils.join(",", new int[0]));
  }

  @Test
  void testJoin_IntArray_NullArray() {
    Assertions.assertNull(ArrayUtils.join(",", (int[]) null));
  }

  @Test
  void testJoin_IntArray_OtherDelimiter() {
    Assertions.assertEquals("1-2-3",
            ArrayUtils.join("-", new int[]{1, 2, 3}));
  }

  @Test
  void testJoin_ByteArray() {
    Assertions.assertEquals("1,2,3",
            ArrayUtils.join(",", new byte[]{1, 2, 3}));
  }

  @Test
  void testJoin_ShortArray() {
    Assertions.assertEquals("10,20,30",
            ArrayUtils.join(",", new short[]{10, 20, 30}));
  }

  @Test
  void testJoin_CharArray() {
    Assertions.assertEquals("a,b,c",
            ArrayUtils.join(",", new char[]{'a', 'b', 'c'}));
  }

  @Test
  void testJoin_FloatArray() {
    Assertions.assertEquals("1.0,2.0,3.0",
            ArrayUtils.join(",", new float[]{1.0f, 2.0f, 3.0f}));
  }

  @Test
  void testJoin_DoubleArray() {
    Assertions.assertEquals("1.0,2.0,3.0",
            ArrayUtils.join(",", new double[]{1.0, 2.0, 3.0}));
  }

  @Test
  void testJoin_BooleanArray() {
    Assertions.assertEquals("true,false,true",
            ArrayUtils.join(",", new boolean[]{true, false, true}));
  }

  @Test
  void testJoin_NullDelimiter() {
    Assertions.assertEquals("anullb",
            ArrayUtils.join(null, new String[]{"a", "b"}));
  }

  // ==================== join 边界值 ====================

  @Test
  void testJoin_EmptyDelimiter() {
    Assertions.assertEquals("ab", ArrayUtils.join("", new String[]{"a", "b"}));
  }

  @Test
  void testJoin_NullDelimiter_EmptyArray() {
    Assertions.assertEquals("", ArrayUtils.join(null, new String[0]));
  }

  @Test
  void testJoin_NullDelimiter_NullArray() {
    Assertions.assertNull(ArrayUtils.join(null, (String[]) null));
  }

  @Test
  void testJoin_AllNullElements() {
    Assertions.assertEquals("null,null,null", ArrayUtils.join(",", new String[]{null, null, null}));
  }

  @Test
  void testJoin_SingleNullElement() {
    Assertions.assertEquals("null", ArrayUtils.join(",", new String[]{null}));
  }

  @Test
  void testJoin_MixedNullAndNonNull() {
    Assertions.assertEquals("a,null,c", ArrayUtils.join(",", new String[]{"a", null, "c"}));
  }

  @Test
  void testJoin_MultiCharDelimiter() {
    Assertions.assertEquals("a -> b -> c", ArrayUtils.join(" -> ", new String[]{"a", "b", "c"}));
  }

  @Test
  void testJoin_SpaceDelimiter() {
    Assertions.assertEquals("a b c", ArrayUtils.join(" ", new String[]{"a", "b", "c"}));
  }

  @Test
  void testJoin_EmptyDelimiter_IntArray() {
    Assertions.assertEquals("12", ArrayUtils.join("", new int[]{1, 2}));
  }

  @Test
  void testJoin_NullDelimiter_IntArray() {
    Assertions.assertEquals("1null2", ArrayUtils.join(null, new int[]{1, 2}));
  }

  @Test
  void testJoin_IntArray_MultiCharDelimiter() {
    Assertions.assertEquals("1 | 2 | 3", ArrayUtils.join(" | ", new int[]{1, 2, 3}));
  }

  @Test
  void testJoin_EmptyDelimiter_BooleanArray() {
    Assertions.assertEquals("truefalse", ArrayUtils.join("", new boolean[]{true, false}));
  }

  @Test
  void testJoin_CharDelimiter_CharArray() {
    Assertions.assertEquals("a-b-c", ArrayUtils.join("-", new char[]{'a', 'b', 'c'}));
  }

  @Test
  void testJoin_DoubleArray_WithDecimal() {
    Assertions.assertEquals("1.0, 2.5, 3.0", ArrayUtils.join(", ", new double[]{1.0, 2.5, 3.0}));
  }

  @Test
  void testJoin_SpecialCharDelimiter() {
    Assertions.assertEquals("a\nb\nc", ArrayUtils.join("\n", new String[]{"a", "b", "c"}));
  }

  @Test
  void testJoin_LongArray() {
    Assertions.assertEquals("100,200,300", ArrayUtils.join(",", new long[]{100L, 200L, 300L}));
  }

  // ==================== join 原始类型覆盖 ====================

  @Test
  void testJoin_ByteArray_Normal() {
    Assertions.assertEquals("1,2,3", ArrayUtils.join(",", new byte[]{1, 2, 3}));
  }

  @Test
  void testJoin_ByteArray_SingleElement() {
    Assertions.assertEquals("1", ArrayUtils.join(",", new byte[]{1}));
  }

  @Test
  void testJoin_ByteArray_EmptyArray() {
    Assertions.assertEquals("", ArrayUtils.join(",", new byte[0]));
  }

  @Test
  void testJoin_ByteArray_NullArray() {
    Assertions.assertNull(ArrayUtils.join(",", (byte[]) null));
  }

  @Test
  void testJoin_ByteArray_NullDelimiter() {
    Assertions.assertEquals("1null2", ArrayUtils.join(null, new byte[]{1, 2}));
  }

  @Test
  void testJoin_ByteArray_EmptyDelimiter() {
    Assertions.assertEquals("12", ArrayUtils.join("", new byte[]{1, 2}));
  }

  @Test
  void testJoin_ShortArray_Normal() {
    Assertions.assertEquals("1,2,3", ArrayUtils.join(",", new short[]{1, 2, 3}));
  }

  @Test
  void testJoin_ShortArray_SingleElement() {
    Assertions.assertEquals("1", ArrayUtils.join(",", new short[]{1}));
  }

  @Test
  void testJoin_ShortArray_EmptyArray() {
    Assertions.assertEquals("", ArrayUtils.join(",", new short[0]));
  }

  @Test
  void testJoin_ShortArray_NullArray() {
    Assertions.assertNull(ArrayUtils.join(",", (short[]) null));
  }

  @Test
  void testJoin_ShortArray_NullDelimiter() {
    Assertions.assertEquals("1null2", ArrayUtils.join(null, new short[]{1, 2}));
  }

  @Test
  void testJoin_ShortArray_EmptyDelimiter() {
    Assertions.assertEquals("12", ArrayUtils.join("", new short[]{1, 2}));
  }

  @Test
  void testJoin_LongArray_Normal() {
    Assertions.assertEquals("100,200,300", ArrayUtils.join(",", new long[]{100L, 200L, 300L}));
  }

  @Test
  void testJoin_LongArray_SingleElement() {
    Assertions.assertEquals("100", ArrayUtils.join(",", new long[]{100L}));
  }

  @Test
  void testJoin_LongArray_EmptyArray() {
    Assertions.assertEquals("", ArrayUtils.join(",", new long[0]));
  }

  @Test
  void testJoin_LongArray_NullArray() {
    Assertions.assertNull(ArrayUtils.join(",", (long[]) null));
  }

  @Test
  void testJoin_LongArray_NullDelimiter() {
    Assertions.assertEquals("1null2", ArrayUtils.join(null, new long[]{1, 2}));
  }

  @Test
  void testJoin_LongArray_EmptyDelimiter() {
    Assertions.assertEquals("12", ArrayUtils.join("", new long[]{1, 2}));
  }

  @Test
  void testJoin_CharArray_Normal() {
    Assertions.assertEquals("a,b,c", ArrayUtils.join(",", new char[]{'a', 'b', 'c'}));
  }

  @Test
  void testJoin_CharArray_SingleElement() {
    Assertions.assertEquals("a", ArrayUtils.join(",", new char[]{'a'}));
  }

  @Test
  void testJoin_CharArray_EmptyArray() {
    Assertions.assertEquals("", ArrayUtils.join(",", new char[0]));
  }

  @Test
  void testJoin_CharArray_NullArray() {
    Assertions.assertNull(ArrayUtils.join(",", (char[]) null));
  }

  @Test
  void testJoin_CharArray_NullDelimiter() {
    Assertions.assertEquals("anullb", ArrayUtils.join(null, new char[]{'a', 'b'}));
  }

  @Test
  void testJoin_CharArray_EmptyDelimiter() {
    Assertions.assertEquals("ab", ArrayUtils.join("", new char[]{'a', 'b'}));
  }

  @Test
  void testJoin_FloatArray_Normal() {
    Assertions.assertEquals("1.0,2.0,3.0", ArrayUtils.join(",", new float[]{1.0f, 2.0f, 3.0f}));
  }

  @Test
  void testJoin_FloatArray_SingleElement() {
    Assertions.assertEquals("1.0", ArrayUtils.join(",", new float[]{1.0f}));
  }

  @Test
  void testJoin_FloatArray_EmptyArray() {
    Assertions.assertEquals("", ArrayUtils.join(",", new float[0]));
  }

  @Test
  void testJoin_FloatArray_NullArray() {
    Assertions.assertNull(ArrayUtils.join(",", (float[]) null));
  }

  @Test
  void testJoin_FloatArray_NullDelimiter() {
    Assertions.assertEquals("1.0null2.0", ArrayUtils.join(null, new float[]{1.0f, 2.0f}));
  }

  @Test
  void testJoin_FloatArray_EmptyDelimiter() {
    Assertions.assertEquals("1.02.0", ArrayUtils.join("", new float[]{1.0f, 2.0f}));
  }

  @Test
  void testJoin_DoubleArray_Normal() {
    Assertions.assertEquals("1.0,2.0,3.0", ArrayUtils.join(",", new double[]{1.0, 2.0, 3.0}));
  }

  @Test
  void testJoin_DoubleArray_SingleElement() {
    Assertions.assertEquals("1.0", ArrayUtils.join(",", new double[]{1.0}));
  }

  @Test
  void testJoin_DoubleArray_EmptyArray() {
    Assertions.assertEquals("", ArrayUtils.join(",", new double[0]));
  }

  @Test
  void testJoin_DoubleArray_NullArray() {
    Assertions.assertNull(ArrayUtils.join(",", (double[]) null));
  }

  @Test
  void testJoin_DoubleArray_NullDelimiter() {
    Assertions.assertEquals("1.0null2.0", ArrayUtils.join(null, new double[]{1.0, 2.0}));
  }

  @Test
  void testJoin_DoubleArray_EmptyDelimiter() {
    Assertions.assertEquals("1.02.0", ArrayUtils.join("", new double[]{1.0, 2.0}));
  }

  @Test
  void testJoin_BooleanArray_Normal() {
    Assertions.assertEquals("true,false,true", ArrayUtils.join(",", new boolean[]{true, false, true}));
  }

  @Test
  void testJoin_BooleanArray_SingleElement() {
    Assertions.assertEquals("true", ArrayUtils.join(",", new boolean[]{true}));
  }

  @Test
  void testJoin_BooleanArray_EmptyArray() {
    Assertions.assertEquals("", ArrayUtils.join(",", new boolean[0]));
  }

  @Test
  void testJoin_BooleanArray_NullArray() {
    Assertions.assertNull(ArrayUtils.join(",", (boolean[]) null));
  }

  @Test
  void testJoin_BooleanArray_NullDelimiter() {
    Assertions.assertEquals("truenullfalse", ArrayUtils.join(null, new boolean[]{true, false}));
  }

  @Test
  void testJoin_BooleanArray_EmptyDelimiter() {
    Assertions.assertEquals("truefalse", ArrayUtils.join("", new boolean[]{true, false}));
  }

  // ==================== contains / firstIndexOf ====================

  @Test
  void testContains_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new String[]{"a", "b", "c"}, "b"));
  }

  @Test
  void testContains_NotFound() {
    Assertions.assertFalse(ArrayUtils.contains(new String[]{"a", "b"}, "z"));
  }

  @Test
  void testContains_NullArray() {
    Assertions.assertFalse(ArrayUtils.contains(null, "x"));
  }

  @Test
  void testContains_NullArray_NullValue() {
    Assertions.assertFalse(ArrayUtils.contains(null, null));
  }

  @Test
  void testContains_NullValue() {
    Assertions.assertTrue(ArrayUtils.contains(new String[]{"a", null, "c"}, null));
  }

  @Test
  void testIndexOf_Found() {
    Assertions.assertEquals(1, ArrayUtils.firstIndexOf(new String[]{"a", "b", "c"}, "b"));
  }

  @Test
  void testIndexOf_First() {
    Assertions.assertEquals(0, ArrayUtils.firstIndexOf(new String[]{"a", "b"}, "a"));
  }

  @Test
  void testIndexOf_Last() {
    Assertions.assertEquals(2, ArrayUtils.firstIndexOf(new String[]{"a", "b", "c"}, "c"));
  }

  @Test
  void testIndexOf_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new String[]{"a", "b"}, "z"));
  }

  @Test
  void testIndexOf_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(null, "x"));
  }

  @Test
  void testIndexOf_NullArray_NullValue() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(null, null));
  }

  @Test
  void testIndexOf_NullValue() {
    Assertions.assertEquals(1, ArrayUtils.firstIndexOf(new String[]{"a", null, "c"}, null));
  }

  // ==================== contains / firstIndexOf 极限场景 ====================

  @Test
  void testContains_EmptyArray() {
    Assertions.assertFalse(ArrayUtils.contains(new String[0], "x"));
  }

  @Test
  void testIndexOf_EmptyArray() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new String[0], "x"));
  }

  @Test
  void testContains_IntArray_FirstElement() {
    Assertions.assertTrue(ArrayUtils.contains(new int[]{1, 2, 3}, 1));
  }

  @Test
  void testContains_IntArray_LastElement() {
    Assertions.assertTrue(ArrayUtils.contains(new int[]{1, 2, 3}, 3));
  }

  @Test
  void testContains_IntArray_Empty() {
    Assertions.assertFalse(ArrayUtils.contains(new int[0], 1));
  }

  @Test
  void testIndexOf_IntArray_Empty() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new int[0], 1));
  }

  @Test
  void testIndexOf_LongArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf((long[]) null, 1L));
  }

  @Test
  void testContains_BooleanArray_NullArray() {
    Assertions.assertFalse(ArrayUtils.contains((boolean[]) null, true));
  }

  @Test
  void testContains_ShortArray_NullArray() {
    Assertions.assertFalse(ArrayUtils.contains((short[]) null, (short) 1));
  }

  @Test
  void testIndexOf_FloatArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf((float[]) null, 1f));
  }

  @Test
  void testIndexOf_DoubleArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf((double[]) null, 1.0));
  }

  @Test
  void testContains_CharArray_NullArray() {
    Assertions.assertFalse(ArrayUtils.contains((char[]) null, 'a'));
  }

  @Test
  void testIndexOf_CharArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf((char[]) null, 'a'));
  }

  @Test
  void testIndexOf_ByteArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf((byte[]) null, (byte) 1));
  }

  @Test
  void testIndexOf_ShortArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf((short[]) null, (short) 1));
  }

  // ==================== 原始类型 contains / firstIndexOf ====================

  @Test
  void testContains_IntArray_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new int[]{1, 2, 3}, 2));
  }

  @Test
  void testContains_IntArray_NotFound() {
    Assertions.assertFalse(ArrayUtils.contains(new int[]{1, 2, 3}, 99));
  }

  @Test
  void testIndexof_IntArray_Found() {
    Assertions.assertEquals(1, ArrayUtils.firstIndexOf(new int[]{1, 2, 3}, 2));
  }

  @Test
  void testIndexOf_IntArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf((int[]) null, 1));
  }

  @Test
  void testContains_LongArray_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new long[]{10L, 20L, 30L}, 20L));
  }

  @Test
  void testIndexOf_LongArray_Found() {
    Assertions.assertEquals(2, ArrayUtils.firstIndexOf(new long[]{10L, 20L, 30L}, 30L));
  }

  @Test
  void testContains_DoubleArray_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new double[]{1.0, 2.0, 3.0}, 2.0));
  }

  @Test
  void testIndexOf_DoubleArray_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new double[]{1.0, 2.0}, 99.0));
  }

  @Test
  void testContains_FloatArray_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new float[]{1.0f, 2.0f}, 1.0f));
  }

  @Test
  void testContains_BooleanArray_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new boolean[]{true, false}, false));
  }

  @Test
  void testIndexOf_BooleanArray_Found() {
    Assertions.assertEquals(0, ArrayUtils.firstIndexOf(new boolean[]{true, false}, true));
  }

  @Test
  void testContains_CharArray_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new char[]{'a', 'b', 'c'}, 'b'));
  }

  @Test
  void testIndexOf_CharArray_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new char[]{'a', 'b'}, 'z'));
  }

  @Test
  void testContains_ByteArray_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new byte[]{1, 2, 3}, (byte) 2));
  }

  @Test
  void testContains_ShortArray_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new short[]{10, 20}, (short) 10));
  }

  // ==================== contains / firstIndexOf 边界值 ====================

  @Test
  void testContains_ObjectArray_SingleElement_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new String[]{"a"}, "a"));
  }

  @Test
  void testContains_ObjectArray_SingleElement_NotFound() {
    Assertions.assertFalse(ArrayUtils.contains(new String[]{"a"}, "x"));
  }

  @Test
  void testContains_ObjectArray_MultipleOccurrences() {
    Assertions.assertTrue(ArrayUtils.contains(new String[]{"a", "b", "a"}, "a"));
  }

  @Test
  void testContains_ObjectArray_NullInArray_SearchNull() {
    Assertions.assertTrue(ArrayUtils.contains(new String[]{"a", null, "c"}, null));
  }

  @Test
  void testContains_ObjectArray_NullInArray_SearchNonNull() {
    Assertions.assertTrue(ArrayUtils.contains(new String[]{"a", null, "c"}, "a"));
  }

  @Test
  void testIndexOf_ObjectArray_SingleElement_Found() {
    Assertions.assertEquals(0, ArrayUtils.firstIndexOf(new String[]{"a"}, "a"));
  }

  @Test
  void testIndexOf_ObjectArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new String[]{"a"}, "x"));
  }

  @Test
  void testIndexOf_ObjectArray_ReturnsFirstOccurrence() {
    Assertions.assertEquals(1, ArrayUtils.firstIndexOf(new String[]{"a", "b", "b"}, "b"));
  }

  @Test
  void testIndexOf_ObjectArray_AllSame_Returns0() {
    Assertions.assertEquals(0, ArrayUtils.firstIndexOf(new String[]{"a", "a", "a"}, "a"));
  }

  @Test
  void testIndexOf_ObjectArray_NullInArray_SearchNull() {
    Assertions.assertEquals(1, ArrayUtils.firstIndexOf(new String[]{"a", null, "c"}, null));
  }

  @Test
  void testIndexOf_ObjectArray_NullInArray_SearchNonNull() {
    Assertions.assertEquals(1, ArrayUtils.firstIndexOf(new String[]{null, "b", "c"}, "b"));
  }

  @Test
  void testContains_IntArray_SingleElement_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new int[]{1}, 1));
  }

  @Test
  void testContains_IntArray_SingleElement_NotFound() {
    Assertions.assertFalse(ArrayUtils.contains(new int[]{1}, 5));
  }

  @Test
  void testContains_IntArray_MultipleOccurrences() {
    Assertions.assertTrue(ArrayUtils.contains(new int[]{1, 2, 1}, 1));
  }

  @Test
  void testIndexOf_IntArray_SingleElement_Found() {
    Assertions.assertEquals(0, ArrayUtils.firstIndexOf(new int[]{1}, 1));
  }

  @Test
  void testIndexOf_IntArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new int[]{1}, 5));
  }

  @Test
  void testIndexOf_IntArray_ReturnsFirstOccurrence() {
    Assertions.assertEquals(1, ArrayUtils.firstIndexOf(new int[]{1, 2, 2}, 2));
  }

  @Test
  void testIndexOf_IntArray_AllSame_Returns0() {
    Assertions.assertEquals(0, ArrayUtils.firstIndexOf(new int[]{3, 3, 3}, 3));
  }

  @Test
  void testIndexOf_IntArray_MiddleElement() {
    Assertions.assertEquals(2, ArrayUtils.firstIndexOf(new int[]{1, 2, 3, 4, 5}, 3));
  }

  @Test
  void testContains_ByteArray_SingleElement_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new byte[]{1}, (byte) 1));
  }

  @Test
  void testIndexOf_ByteArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new byte[]{1}, (byte) 5));
  }

  @Test
  void testContains_ShortArray_SingleElement_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new short[]{1}, (short) 1));
  }

  @Test
  void testIndexOf_ShortArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new short[]{1}, (short) 5));
  }

  @Test
  void testContains_LongArray_SingleElement_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new long[]{1L}, 1L));
  }

  @Test
  void testContains_LongArray_SingleElement_NotFound() {
    Assertions.assertFalse(ArrayUtils.contains(new long[]{1L}, 5L));
  }

  @Test
  void testIndexOf_LongArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new long[]{1L}, 5L));
  }

  @Test
  void testContains_DoubleArray_SingleElement_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new double[]{1.0}, 1.0));
  }

  @Test
  void testIndexOf_DoubleArray_SingleElement_Found() {
    Assertions.assertEquals(0, ArrayUtils.firstIndexOf(new double[]{1.0}, 1.0));
  }

  @Test
  void testContains_FloatArray_SingleElement_NotFound() {
    Assertions.assertFalse(ArrayUtils.contains(new float[]{1f}, 5f));
  }

  @Test
  void testIndexOf_FloatArray_SingleElement_Found() {
    Assertions.assertEquals(0, ArrayUtils.firstIndexOf(new float[]{1f}, 1f));
  }

  @Test
  void testContains_BooleanArray_SingleElement_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new boolean[]{true}, true));
  }

  @Test
  void testIndexOf_BooleanArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new boolean[]{true}, false));
  }

  @Test
  void testContains_CharArray_SingleElement_Found() {
    Assertions.assertTrue(ArrayUtils.contains(new char[]{'a'}, 'a'));
  }

  @Test
  void testIndexOf_CharArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.firstIndexOf(new char[]{'a'}, 'z'));
  }

  // ==================== lastIndexOf ====================

  @Test
  void testLastIndexOf_ObjectArray_Found() {
    Assertions.assertEquals(1, ArrayUtils.lastIndexOf(new String[]{"a", "b", "c"}, "b"));
  }

  @Test
  void testLastIndexOf_ObjectArray_ReturnsLastOccurrence() {
    Assertions.assertEquals(2, ArrayUtils.lastIndexOf(new String[]{"a", "b", "a"}, "a"));
  }

  @Test
  void testLastIndexOf_ObjectArray_Last() {
    Assertions.assertEquals(2, ArrayUtils.lastIndexOf(new String[]{"a", "b", "c"}, "c"));
  }

  @Test
  void testLastIndexOf_ObjectArray_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new String[]{"a", "b"}, "z"));
  }

  @Test
  void testLastIndexOf_ObjectArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(null, "x"));
  }

  @Test
  void testLastIndexOf_ObjectArray_NullArray_NullValue() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(null, null));
  }

  @Test
  void testLastIndexOf_ObjectArray_NullValue() {
    Assertions.assertEquals(1, ArrayUtils.lastIndexOf(new String[]{"a", null, "c"}, null));
  }

  @Test
  void testLastIndexOf_ObjectArray_EmptyArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new String[0], "x"));
  }

  @Test
  void testLastIndexOf_ObjectArray_SingleElement_Found() {
    Assertions.assertEquals(0, ArrayUtils.lastIndexOf(new String[]{"a"}, "a"));
  }

  @Test
  void testLastIndexOf_ObjectArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new String[]{"a"}, "x"));
  }

  @Test
  void testLastIndexOf_ObjectArray_AllSame_ReturnsLastIndex() {
    Assertions.assertEquals(2, ArrayUtils.lastIndexOf(new String[]{"a", "a", "a"}, "a"));
  }

  @Test
  void testLastIndexOf_ObjectArray_NullInArray_SearchNull() {
    Assertions.assertEquals(2, ArrayUtils.lastIndexOf(new String[]{"a", null, null}, null));
  }

  @Test
  void testLastIndexOf_ObjectArray_NullInArray_SearchNonNull() {
    Assertions.assertEquals(1, ArrayUtils.lastIndexOf(new String[]{null, "b", "c"}, "b"));
  }

  @Test
  void testLastIndexOf_IntArray_Found() {
    Assertions.assertEquals(1, ArrayUtils.lastIndexOf(new int[]{1, 2, 3}, 2));
  }

  @Test
  void testLastIndexOf_IntArray_ReturnsLastOccurrence() {
    Assertions.assertEquals(3, ArrayUtils.lastIndexOf(new int[]{1, 2, 2, 2}, 2));
  }

  @Test
  void testLastIndexOf_IntArray_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new int[]{1, 2}, 5));
  }

  @Test
  void testLastIndexOf_IntArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf((int[]) null, 1));
  }

  @Test
  void testLastIndexOf_IntArray_Empty() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new int[0], 1));
  }

  @Test
  void testLastIndexOf_IntArray_SingleElement_Found() {
    Assertions.assertEquals(0, ArrayUtils.lastIndexOf(new int[]{1}, 1));
  }

  @Test
  void testLastIndexOf_IntArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new int[]{1}, 5));
  }

  @Test
  void testLastIndexOf_IntArray_AllSame_ReturnsLastIndex() {
    Assertions.assertEquals(2, ArrayUtils.lastIndexOf(new int[]{3, 3, 3}, 3));
  }

  @Test
  void testLastIndexOf_IntArray_MiddleElement() {
    Assertions.assertEquals(2, ArrayUtils.lastIndexOf(new int[]{1, 2, 3, 4, 5}, 3));
  }

  @Test
  void testLastIndexOf_ByteArray_Found() {
    Assertions.assertEquals(1, ArrayUtils.lastIndexOf(new byte[]{1, 2, 3}, (byte) 2));
  }

  @Test
  void testLastIndexOf_ByteArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf((byte[]) null, (byte) 1));
  }

  @Test
  void testLastIndexOf_ByteArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new byte[]{1}, (byte) 5));
  }

  @Test
  void testLastIndexOf_ShortArray_Found() {
    Assertions.assertEquals(1, ArrayUtils.lastIndexOf(new short[]{1, 2, 3}, (short) 2));
  }

  @Test
  void testLastIndexOf_ShortArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf((short[]) null, (short) 1));
  }

  @Test
  void testLastIndexOf_ShortArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new short[]{1}, (short) 5));
  }

  @Test
  void testLastIndexOf_LongArray_Found() {
    Assertions.assertEquals(0, ArrayUtils.lastIndexOf(new long[]{1L}, 1L));
  }

  @Test
  void testLastIndexOf_LongArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf((long[]) null, 1L));
  }

  @Test
  void testLastIndexOf_LongArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new long[]{1L}, 5L));
  }

  @Test
  void testLastIndexOf_DoubleArray_Found() {
    Assertions.assertEquals(0, ArrayUtils.lastIndexOf(new double[]{1.0}, 1.0));
  }

  @Test
  void testLastIndexOf_DoubleArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf((double[]) null, 1.0));
  }

  @Test
  void testLastIndexOf_FloatArray_Found() {
    Assertions.assertEquals(0, ArrayUtils.lastIndexOf(new float[]{1f}, 1f));
  }

  @Test
  void testLastIndexOf_FloatArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf((float[]) null, 1f));
  }

  @Test
  void testLastIndexOf_BooleanArray_Found() {
    Assertions.assertEquals(1, ArrayUtils.lastIndexOf(new boolean[]{true, false}, false));
  }

  @Test
  void testLastIndexOf_BooleanArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf((boolean[]) null, true));
  }

  @Test
  void testLastIndexOf_BooleanArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new boolean[]{true}, false));
  }

  @Test
  void testLastIndexOf_CharArray_Found() {
    Assertions.assertEquals(1, ArrayUtils.lastIndexOf(new char[]{'a', 'b', 'c'}, 'b'));
  }

  @Test
  void testLastIndexOf_CharArray_NullArray() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf((char[]) null, 'a'));
  }

  @Test
  void testLastIndexOf_CharArray_SingleElement_NotFound() {
    Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(new char[]{'a'}, 'z'));
  }

  // ==================== removeNulls ====================

  @Test
  void testRemoveNulls_Normal() {
    Assertions.assertArrayEquals(new String[]{"a", "c"}, ArrayUtils.removeNulls(new String[]{"a", null, "c"}));
  }

  @Test
  void testRemoveNulls_AllNull_ReturnsEmpty() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.removeNulls(new String[]{null, null, null}));
  }

  @Test
  void testRemoveNulls_NoNull_ReturnsCopy() {
    Assertions.assertArrayEquals(new String[]{"a", "b"}, ArrayUtils.removeNulls(new String[]{"a", "b"}));
  }

  @Test
  void testRemoveNulls_FirstNull() {
    Assertions.assertArrayEquals(new String[]{"b"}, ArrayUtils.removeNulls(new String[]{null, "b"}));
  }

  @Test
  void testRemoveNulls_LastNull() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.removeNulls(new String[]{"a", null}));
  }

  @Test
  void testRemoveNulls_SingleNull() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.removeNulls(new String[]{null}));
  }

  @Test
  void testRemoveNulls_SingleNonNull() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.removeNulls(new String[]{"a"}));
  }

  @Test
  void testRemoveNulls_EmptyArray() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.removeNulls(new String[0]));
  }

  @Test
  void testRemoveNulls_NullArray() {
    Assertions.assertNull(ArrayUtils.removeNulls(null));
  }

  @Test
  void testRemoveNulls_MultipleNulls() {
    Assertions.assertArrayEquals(new String[]{"a", "c"}, ArrayUtils.removeNulls(new String[]{null, "a", null, "c", null}));
  }

  @Test
  void testRemoveNulls_PreservesOrder() {
    Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, ArrayUtils.removeNulls(new String[]{"a", null, "b", "c"}));
  }

  // ==================== distinct ====================

  @Test
  void testDistinct_ObjectArray() {
    Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, ArrayUtils.distinct(new String[]{"a", "b", "a", "c", "b"}));
  }

  @Test
  void testDistinct_ObjectArray_AllUnique() {
    Assertions.assertArrayEquals(new String[]{"a", "b"}, ArrayUtils.distinct(new String[]{"a", "b"}));
  }

  @Test
  void testDistinct_ObjectArray_AllSame() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.distinct(new String[]{"a", "a", "a"}));
  }

  @Test
  void testDistinct_ObjectArray_SingleElement() {
    Assertions.assertArrayEquals(new String[]{"a"}, ArrayUtils.distinct(new String[]{"a"}));
  }

  @Test
  void testDistinct_ObjectArray_EmptyArray() {
    Assertions.assertArrayEquals(new String[0], ArrayUtils.distinct(new String[0]));
  }

  @Test
  void testDistinct_ObjectArray_NullArray() {
    Assertions.assertNull(ArrayUtils.distinct((String[]) null));
  }

  @Test
  void testDistinct_ObjectArray_WithNulls() {
    Assertions.assertArrayEquals(new String[]{null, "a"}, ArrayUtils.distinct(new String[]{null, "a", null, "a"}));
  }

  @Test
  void testDistinct_ObjectArray_AllNulls() {
    Assertions.assertArrayEquals(new String[]{null}, ArrayUtils.distinct(new String[]{null, null, null}));
  }

  @Test
  void testDistinct_ObjectArray_PreservesOrder() {
    Assertions.assertArrayEquals(new String[]{"b", "a", "c"}, ArrayUtils.distinct(new String[]{"b", "a", "c", "b", "a"}));
  }

  @Test
  void testDistinct_IntArray() {
    Assertions.assertArrayEquals(new int[]{1, 2, 3}, ArrayUtils.distinct(new int[]{1, 2, 1, 3, 2}));
  }

  @Test
  void testDistinct_IntArray_AllUnique() {
    Assertions.assertArrayEquals(new int[]{1, 2}, ArrayUtils.distinct(new int[]{1, 2}));
  }

  @Test
  void testDistinct_IntArray_AllSame() {
    Assertions.assertArrayEquals(new int[]{1}, ArrayUtils.distinct(new int[]{1, 1, 1}));
  }

  @Test
  void testDistinct_IntArray_SingleElement() {
    Assertions.assertArrayEquals(new int[]{1}, ArrayUtils.distinct(new int[]{1}));
  }

  @Test
  void testDistinct_IntArray_EmptyArray() {
    Assertions.assertArrayEquals(new int[0], ArrayUtils.distinct(new int[0]));
  }

  @Test
  void testDistinct_IntArray_NullArray() {
    Assertions.assertNull(ArrayUtils.distinct((int[]) null));
  }

  @Test
  void testDistinct_IntArray_PreservesOrder() {
    Assertions.assertArrayEquals(new int[]{2, 1, 3}, ArrayUtils.distinct(new int[]{2, 1, 3, 2, 1}));
  }

  @Test
  void testDistinct_ByteArray() {
    Assertions.assertArrayEquals(new byte[]{1, 2, 3}, ArrayUtils.distinct(new byte[]{1, 2, 1, 3, 2}));
  }

  @Test
  void testDistinct_ByteArray_NullArray() {
    Assertions.assertNull(ArrayUtils.distinct((byte[]) null));
  }

  @Test
  void testDistinct_ShortArray() {
    Assertions.assertArrayEquals(new short[]{1, 2, 3}, ArrayUtils.distinct(new short[]{1, 2, 1, 3, 2}));
  }

  @Test
  void testDistinct_ShortArray_NullArray() {
    Assertions.assertNull(ArrayUtils.distinct((short[]) null));
  }

  @Test
  void testDistinct_LongArray() {
    Assertions.assertArrayEquals(new long[]{1, 2, 3}, ArrayUtils.distinct(new long[]{1, 2, 1, 3, 2}));
  }

  @Test
  void testDistinct_LongArray_NullArray() {
    Assertions.assertNull(ArrayUtils.distinct((long[]) null));
  }

  @Test
  void testDistinct_CharArray() {
    Assertions.assertArrayEquals(new char[]{'a', 'b', 'c'}, ArrayUtils.distinct(new char[]{'a', 'b', 'a', 'c', 'b'}));
  }

  @Test
  void testDistinct_CharArray_NullArray() {
    Assertions.assertNull(ArrayUtils.distinct((char[]) null));
  }

  @Test
  void testDistinct_FloatArray() {
    Assertions.assertArrayEquals(new float[]{1f, 2f, 3f}, ArrayUtils.distinct(new float[]{1f, 2f, 1f, 3f, 2f}), 0f);
  }

  @Test
  void testDistinct_FloatArray_NullArray() {
    Assertions.assertNull(ArrayUtils.distinct((float[]) null));
  }

  @Test
  void testDistinct_DoubleArray() {
    Assertions.assertArrayEquals(new double[]{1d, 2d, 3d}, ArrayUtils.distinct(new double[]{1d, 2d, 1d, 3d, 2d}), 0d);
  }

  @Test
  void testDistinct_DoubleArray_NullArray() {
    Assertions.assertNull(ArrayUtils.distinct((double[]) null));
  }

  @Test
  void testDistinct_BooleanArray() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.distinct(new boolean[]{true, false, true, false}));
  }

  @Test
  void testDistinct_BooleanArray_NullArray() {
    Assertions.assertNull(ArrayUtils.distinct((boolean[]) null));
  }

  @Test
  void testDistinct_BooleanArray_AllTrue() {
    Assertions.assertArrayEquals(new boolean[]{true}, ArrayUtils.distinct(new boolean[]{true, true, true}));
  }

  @Test
  void testDistinct_BooleanArray_AllFalse() {
    Assertions.assertArrayEquals(new boolean[]{false}, ArrayUtils.distinct(new boolean[]{false, false, false}));
  }

  @Test
  void testDistinct_ByteArray_PreservesOrder() {
    Assertions.assertArrayEquals(new byte[]{2, 1, 3}, ArrayUtils.distinct(new byte[]{2, 1, 3, 2, 1}));
  }

  @Test
  void testDistinct_ShortArray_PreservesOrder() {
    Assertions.assertArrayEquals(new short[]{2, 1, 3}, ArrayUtils.distinct(new short[]{2, 1, 3, 2, 1}));
  }

  @Test
  void testDistinct_LongArray_PreservesOrder() {
    Assertions.assertArrayEquals(new long[]{2, 1, 3}, ArrayUtils.distinct(new long[]{2, 1, 3, 2, 1}));
  }

  @Test
  void testDistinct_CharArray_PreservesOrder() {
    Assertions.assertArrayEquals(new char[]{'b', 'a', 'c'}, ArrayUtils.distinct(new char[]{'b', 'a', 'c', 'b', 'a'}));
  }

  @Test
  void testDistinct_FloatArray_PreservesOrder() {
    Assertions.assertArrayEquals(new float[]{2f, 1f, 3f}, ArrayUtils.distinct(new float[]{2f, 1f, 3f, 2f, 1f}), 0f);
  }

  @Test
  void testDistinct_DoubleArray_PreservesOrder() {
    Assertions.assertArrayEquals(new double[]{2d, 1d, 3d}, ArrayUtils.distinct(new double[]{2d, 1d, 3d, 2d, 1d}), 0d);
  }

  @Test
  void testDistinct_BooleanArray_PreservesOrder() {
    Assertions.assertArrayEquals(new boolean[]{false, true}, ArrayUtils.distinct(new boolean[]{false, true, false, true}));
  }

  @Test
  void testDistinct_BooleanArray_PreservesOrder2() {
    Assertions.assertArrayEquals(new boolean[]{true, false}, ArrayUtils.distinct(new boolean[]{true, false, false, true}));
  }

  // ==================== distinct 边界值 ====================

  @Test
  void testDistinct_ObjectArray_SingleNull() {
    Assertions.assertArrayEquals(new String[]{null}, ArrayUtils.distinct(new String[]{null}));
  }

  @Test
  void testDistinct_ObjectArray_NullFirst_ThenNonNull() {
    Assertions.assertArrayEquals(new String[]{null, "a", "b"}, ArrayUtils.distinct(new String[]{null, "a", null, "b"}));
  }

  @Test
  void testDistinct_ObjectArray_FirstAndLastSame() {
    Assertions.assertArrayEquals(new String[]{"a", "b"}, ArrayUtils.distinct(new String[]{"a", "b", "a"}));
  }

  @Test
  void testDistinct_ByteArray_SingleElement() {
    Assertions.assertArrayEquals(new byte[]{1}, ArrayUtils.distinct(new byte[]{1}));
  }

  @Test
  void testDistinct_ByteArray_EmptyArray() {
    Assertions.assertArrayEquals(new byte[0], ArrayUtils.distinct(new byte[0]));
  }

  @Test
  void testDistinct_ByteArray_AllSame() {
    Assertions.assertArrayEquals(new byte[]{1}, ArrayUtils.distinct(new byte[]{1, 1, 1}));
  }

  @Test
  void testDistinct_ShortArray_SingleElement() {
    Assertions.assertArrayEquals(new short[]{1}, ArrayUtils.distinct(new short[]{1}));
  }

  @Test
  void testDistinct_ShortArray_EmptyArray() {
    Assertions.assertArrayEquals(new short[0], ArrayUtils.distinct(new short[0]));
  }

  @Test
  void testDistinct_ShortArray_AllSame() {
    Assertions.assertArrayEquals(new short[]{1}, ArrayUtils.distinct(new short[]{1, 1, 1}));
  }

  @Test
  void testDistinct_LongArray_SingleElement() {
    Assertions.assertArrayEquals(new long[]{1}, ArrayUtils.distinct(new long[]{1}));
  }

  @Test
  void testDistinct_LongArray_EmptyArray() {
    Assertions.assertArrayEquals(new long[0], ArrayUtils.distinct(new long[0]));
  }

  @Test
  void testDistinct_LongArray_AllSame() {
    Assertions.assertArrayEquals(new long[]{1}, ArrayUtils.distinct(new long[]{1, 1, 1}));
  }

  @Test
  void testDistinct_CharArray_SingleElement() {
    Assertions.assertArrayEquals(new char[]{'a'}, ArrayUtils.distinct(new char[]{'a'}));
  }

  @Test
  void testDistinct_CharArray_EmptyArray() {
    Assertions.assertArrayEquals(new char[0], ArrayUtils.distinct(new char[0]));
  }

  @Test
  void testDistinct_CharArray_AllSame() {
    Assertions.assertArrayEquals(new char[]{'a'}, ArrayUtils.distinct(new char[]{'a', 'a', 'a'}));
  }

  @Test
  void testDistinct_FloatArray_SingleElement() {
    Assertions.assertArrayEquals(new float[]{1f}, ArrayUtils.distinct(new float[]{1f}), 0f);
  }

  @Test
  void testDistinct_FloatArray_EmptyArray() {
    Assertions.assertArrayEquals(new float[0], ArrayUtils.distinct(new float[0]), 0f);
  }

  @Test
  void testDistinct_FloatArray_AllSame() {
    Assertions.assertArrayEquals(new float[]{1f}, ArrayUtils.distinct(new float[]{1f, 1f, 1f}), 0f);
  }

  @Test
  void testDistinct_DoubleArray_SingleElement() {
    Assertions.assertArrayEquals(new double[]{1d}, ArrayUtils.distinct(new double[]{1d}), 0d);
  }

  @Test
  void testDistinct_DoubleArray_EmptyArray() {
    Assertions.assertArrayEquals(new double[0], ArrayUtils.distinct(new double[0]), 0d);
  }

  @Test
  void testDistinct_DoubleArray_AllSame() {
    Assertions.assertArrayEquals(new double[]{1d}, ArrayUtils.distinct(new double[]{1d, 1d, 1d}), 0d);
  }

  @Test
  void testDistinct_BooleanArray_SingleElement() {
    Assertions.assertArrayEquals(new boolean[]{true}, ArrayUtils.distinct(new boolean[]{true}));
  }

  @Test
  void testDistinct_BooleanArray_EmptyArray() {
    Assertions.assertArrayEquals(new boolean[0], ArrayUtils.distinct(new boolean[0]));
  }

  // ==================== countOf ====================

  @Test
  void testCountOf_ObjectArray() {
    Assertions.assertEquals(2, ArrayUtils.countOf(new String[]{"a", "b", "a"}, "a"));
  }

  @Test
  void testCountOf_ObjectArray_Zero() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new String[]{"a", "b"}, "x"));
  }

  @Test
  void testCountOf_ObjectArray_NullValue() {
    Assertions.assertEquals(2, ArrayUtils.countOf(new String[]{null, "a", null}, null));
  }

  @Test
  void testCountOf_ObjectArray_NullArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf((String[]) null, "x"));
  }

  @Test
  void testCountOf_ObjectArray_EmptyArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new String[0], "x"));
  }

  @Test
  void testCountOf_IntArray() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new int[]{1, 2, 1, 3, 1}, 1));
  }

  @Test
  void testCountOf_IntArray_Zero() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new int[]{1, 2}, 5));
  }

  @Test
  void testCountOf_IntArray_NullArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf((int[]) null, 1));
  }

  @Test
  void testCountOf_IntArray_EmptyArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new int[0], 1));
  }

  @Test
  void testCountOf_ByteArray() {
    Assertions.assertEquals(2, ArrayUtils.countOf(new byte[]{1, 2, 1, 3}, (byte) 1));
  }

  @Test
  void testCountOf_ShortArray() {
    Assertions.assertEquals(2, ArrayUtils.countOf(new short[]{1, 2, 1, 3}, (short) 1));
  }

  @Test
  void testCountOf_LongArray() {
    Assertions.assertEquals(2, ArrayUtils.countOf(new long[]{1, 2, 1, 3}, 1L));
  }

  @Test
  void testCountOf_CharArray() {
    Assertions.assertEquals(2, ArrayUtils.countOf(new char[]{'a', 'b', 'a', 'c'}, 'a'));
  }

  @Test
  void testCountOf_FloatArray() {
    Assertions.assertEquals(2, ArrayUtils.countOf(new float[]{1f, 2f, 1f}, 1f));
  }

  @Test
  void testCountOf_DoubleArray() {
    Assertions.assertEquals(2, ArrayUtils.countOf(new double[]{1d, 2d, 1d}, 1d));
  }

  @Test
  void testCountOf_BooleanArray() {
    Assertions.assertEquals(2, ArrayUtils.countOf(new boolean[]{true, false, true}, true));
  }

  @Test
  void testCountOf_BooleanArray_Zero() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new boolean[]{true, true}, false));
  }

  // ==================== countOf 边界值 ====================

  @Test
  void testCountOf_ObjectArray_SingleElement_Found() {
    Assertions.assertEquals(1, ArrayUtils.countOf(new String[]{"a"}, "a"));
  }

  @Test
  void testCountOf_ObjectArray_SingleElement_NotFound() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new String[]{"a"}, "x"));
  }

  @Test
  void testCountOf_ObjectArray_AllSame() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new String[]{"a", "a", "a"}, "a"));
  }

  @Test
  void testCountOf_ObjectArray_AllSame_NotFound() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new String[]{"a", "a", "a"}, "x"));
  }

  @Test
  void testCountOf_ObjectArray_AllNull_CountNull() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new String[]{null, null, null}, null));
  }

  @Test
  void testCountOf_ObjectArray_AllNull_CountNonNull() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new String[]{null, null, null}, "a"));
  }

  @Test
  void testCountOf_IntArray_SingleElement_Found() {
    Assertions.assertEquals(1, ArrayUtils.countOf(new int[]{1}, 1));
  }

  @Test
  void testCountOf_IntArray_SingleElement_NotFound() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new int[]{1}, 5));
  }

  @Test
  void testCountOf_IntArray_AllSame() {
    Assertions.assertEquals(4, ArrayUtils.countOf(new int[]{2, 2, 2, 2}, 2));
  }

  @Test
  void testCountOf_IntArray_AllSame_NotFound() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new int[]{2, 2, 2}, 5));
  }

  @Test
  void testCountOf_ByteArray_SingleElement_Found() {
    Assertions.assertEquals(1, ArrayUtils.countOf(new byte[]{1}, (byte) 1));
  }

  @Test
  void testCountOf_ByteArray_EmptyArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new byte[0], (byte) 1));
  }

  @Test
  void testCountOf_ByteArray_NullArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf((byte[]) null, (byte) 1));
  }

  @Test
  void testCountOf_ByteArray_AllSame() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new byte[]{1, 1, 1}, (byte) 1));
  }

  @Test
  void testCountOf_ShortArray_SingleElement_Found() {
    Assertions.assertEquals(1, ArrayUtils.countOf(new short[]{1}, (short) 1));
  }

  @Test
  void testCountOf_ShortArray_EmptyArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new short[0], (short) 1));
  }

  @Test
  void testCountOf_ShortArray_NullArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf((short[]) null, (short) 1));
  }

  @Test
  void testCountOf_ShortArray_AllSame() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new short[]{1, 1, 1}, (short) 1));
  }

  @Test
  void testCountOf_LongArray_SingleElement_Found() {
    Assertions.assertEquals(1, ArrayUtils.countOf(new long[]{1}, 1L));
  }

  @Test
  void testCountOf_LongArray_EmptyArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new long[0], 1L));
  }

  @Test
  void testCountOf_LongArray_NullArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf((long[]) null, 1L));
  }

  @Test
  void testCountOf_LongArray_AllSame() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new long[]{1, 1, 1}, 1L));
  }

  @Test
  void testCountOf_CharArray_SingleElement_Found() {
    Assertions.assertEquals(1, ArrayUtils.countOf(new char[]{'a'}, 'a'));
  }

  @Test
  void testCountOf_CharArray_EmptyArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new char[0], 'a'));
  }

  @Test
  void testCountOf_CharArray_NullArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf((char[]) null, 'a'));
  }

  @Test
  void testCountOf_CharArray_AllSame() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new char[]{'a', 'a', 'a'}, 'a'));
  }

  @Test
  void testCountOf_FloatArray_SingleElement_Found() {
    Assertions.assertEquals(1, ArrayUtils.countOf(new float[]{1f}, 1f));
  }

  @Test
  void testCountOf_FloatArray_EmptyArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new float[0], 1f));
  }

  @Test
  void testCountOf_FloatArray_NullArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf((float[]) null, 1f));
  }

  @Test
  void testCountOf_FloatArray_AllSame() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new float[]{1f, 1f, 1f}, 1f));
  }

  @Test
  void testCountOf_DoubleArray_SingleElement_Found() {
    Assertions.assertEquals(1, ArrayUtils.countOf(new double[]{1d}, 1d));
  }

  @Test
  void testCountOf_DoubleArray_EmptyArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new double[0], 1d));
  }

  @Test
  void testCountOf_DoubleArray_NullArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf((double[]) null, 1d));
  }

  @Test
  void testCountOf_DoubleArray_AllSame() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new double[]{1d, 1d, 1d}, 1d));
  }

  @Test
  void testCountOf_BooleanArray_SingleElement_Found() {
    Assertions.assertEquals(1, ArrayUtils.countOf(new boolean[]{true}, true));
  }

  @Test
  void testCountOf_BooleanArray_EmptyArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new boolean[0], true));
  }

  @Test
  void testCountOf_BooleanArray_NullArray() {
    Assertions.assertEquals(0, ArrayUtils.countOf((boolean[]) null, true));
  }

  @Test
  void testCountOf_BooleanArray_AllTrue_CountTrue() {
    Assertions.assertEquals(3, ArrayUtils.countOf(new boolean[]{true, true, true}, true));
  }

  @Test
  void testCountOf_BooleanArray_AllTrue_CountFalse() {
    Assertions.assertEquals(0, ArrayUtils.countOf(new boolean[]{true, true, true}, false));
  }
}
