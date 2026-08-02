package cn.addenda.component.common.test.util;

import cn.addenda.component.common.util.AssertUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author addenda
 * @since 2026/8/1
 */
class AssertUtilsTest {

  private static Supplier<String> neverCalled() {
    return () -> {
      throw new RuntimeException("Supplier should not be called");
    };
  }

  // ==================== state ====================

  @Test
  void state_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.state(true, "应该通过"));
  }

  @Test
  void state_Fail() {
    Assertions.assertThrows(IllegalStateException.class,
            () -> AssertUtils.state(false, "状态错误"));
  }

  @Test
  void state_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.state(true, neverCalled()));
  }

  @Test
  void state_Supplier_Fail() {
    Assertions.assertThrows(IllegalStateException.class,
            () -> AssertUtils.state(false, () -> "延迟构建的消息"));
  }

  // ==================== isTrue ====================

  @Test
  void isTrue_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isTrue(1 > 0, "应该通过"));
  }

  @Test
  void isTrue_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isTrue(1 < 0, "表达式为 false"));
  }

  @Test
  void isTrue_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isTrue(true, neverCalled()));
  }

  @Test
  void isTrue_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isTrue(false, () -> "延迟构建的消息"));
  }

  // ==================== isNull ====================

  @Test
  void isNull_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isNull(null, "应该为 null"));
  }

  @Test
  void isNull_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isNull(new Object(), "期望为 null"));
  }

  @Test
  void isNull_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isNull(null, neverCalled()));
  }

  @Test
  void isNull_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isNull("not null", () -> "延迟构建的消息"));
  }

  // ==================== notNull ====================

  @Test
  void notNull_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.notNull(new Object(), "不应该为 null"));
  }

  @Test
  void notNull_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notNull(null, "不能为 null"));
  }

  @Test
  void notNull_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.notNull("hello", neverCalled()));
  }

  @Test
  void notNull_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notNull(null, () -> "延迟构建的消息"));
  }

  // ==================== hasLength ====================

  @Test
  void hasLength_Pass_NonEmpty() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.hasLength("abc", "不应该为 null"));
  }

  @Test
  void hasLength_Fail_Null() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.hasLength(null, "不能为空"));
  }

  @Test
  void hasLength_Fail_Empty() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.hasLength("", "不能为空字符串"));
  }

  @Test
  void hasLength_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.hasLength("a", neverCalled()));
  }

  @Test
  void hasLength_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.hasLength("", () -> "延迟构建的消息"));
  }

  // ==================== hasText ====================

  @Test
  void hasText_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.hasText("abc", "不应该为 null"));
  }

  @Test
  void hasText_Fail_Null() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.hasText(null, "不能为 null"));
  }

  @Test
  void hasText_Fail_Empty() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.hasText("", "不能为空"));
  }

  @Test
  void hasText_Fail_Blank() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.hasText("   ", "不能为空白"));
  }

  @Test
  void hasText_Pass_Trimmed() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.hasText(" a ", "有文本内容"));
  }

  @Test
  void hasText_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.hasText("hello", neverCalled()));
  }

  @Test
  void hasText_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.hasText("   ", () -> "延迟构建的消息"));
  }

  // ==================== doesNotContain ====================

  @Test
  void doesNotContain_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.doesNotContain("hello", "world", "不应包含"));
  }

  @Test
  void doesNotContain_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.doesNotContain("hello world", "world", "包含非法字符"));
  }

  @Test
  void doesNotContain_Pass_NullText() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.doesNotContain(null, "abc", "null 文本不抛异常"));
  }

  @Test
  void doesNotContain_Pass_EmptyText() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.doesNotContain("", "abc", "空文本不抛异常"));
  }

  @Test
  void doesNotContain_Pass_NullSubstring() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.doesNotContain("hello", null, "null 子串不抛异常"));
  }

  @Test
  void doesNotContain_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.doesNotContain("abc", "def", neverCalled()));
  }

  @Test
  void doesNotContain_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.doesNotContain("abc", "b", () -> "延迟构建的消息"));
  }

  // ==================== notEmpty (数组) ====================

  @Test
  void notEmpty_Array_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.notEmpty(new Object[]{"a"}, "不应该为 null"));
  }

  @Test
  void notEmpty_Array_Fail_Null() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notEmpty((Object[]) null, "不能为 null"));
  }

  @Test
  void notEmpty_Array_Fail_Empty() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notEmpty(new Object[0], "不能为空数组"));
  }

  @Test
  void notEmpty_Array_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.notEmpty(new String[]{"x"}, neverCalled()));
  }

  @Test
  void notEmpty_Array_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notEmpty(new Object[0], () -> "延迟构建的消息"));
  }

  // ==================== noNullElements (数组) ====================

  @Test
  void noNullElements_Array_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements(new Object[]{"a", "b"}, "不应该为 null"));
  }

  @Test
  void noNullElements_Array_Pass_NullArray() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements((Object[]) null, "null 数组不抛异常"));
  }

  @Test
  void noNullElements_Array_Pass_EmptyArray() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements(new Object[0], "空数组不抛异常"));
  }

  @Test
  void noNullElements_Array_Fail() {
    Object[] array = new Object[]{"a", null, "b"};
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.noNullElements(array, "数组含 null 元素"));
  }

  @Test
  void noNullElements_Array_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements(new String[]{"a"}, neverCalled()));
  }

  @Test
  void noNullElements_Array_Supplier_Fail() {
    Object[] array = new Object[]{null};
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.noNullElements(array, () -> "延迟构建的消息"));
  }

  // ==================== notEmpty (集合) ====================

  @Test
  void notEmpty_Collection_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.notEmpty(Arrays.asList("a"), "不应该为 null"));
  }

  @Test
  void notEmpty_Collection_Fail_Null() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notEmpty((Collection<?>) null, "不能为 null"));
  }

  @Test
  void notEmpty_Collection_Fail_Empty() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notEmpty(Collections.emptyList(), "不能为空集合"));
  }

  @Test
  void notEmpty_Collection_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.notEmpty(Collections.singletonList("x"), neverCalled()));
  }

  @Test
  void notEmpty_Collection_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notEmpty(new ArrayList<>(), () -> "延迟构建的消息"));
  }

  // ==================== noNullElements (集合) ====================

  @Test
  void noNullElements_Collection_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements(Arrays.asList("a", "b"), "不应该为 null"));
  }

  @Test
  void noNullElements_Collection_Pass_NullCollection() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements((Collection<?>) null, "null 集合不抛异常"));
  }

  @Test
  void noNullElements_Collection_Pass_EmptyCollection() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements(Collections.emptyList(), "空集合不抛异常"));
  }

  @Test
  void noNullElements_Collection_Fail() {
    List<Object> list = Arrays.asList("a", null, "b");
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.noNullElements(list, "集合含 null 元素"));
  }

  @Test
  void noNullElements_Collection_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements(Collections.singleton("a"), neverCalled()));
  }

  @Test
  void noNullElements_Collection_Supplier_Fail() {
    List<Object> list = new ArrayList<>();
    list.add(null);
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.noNullElements(list, () -> "延迟构建的消息"));
  }

  // ==================== notEmpty (Map) ====================

  @Test
  void notEmpty_Map_Pass() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    Assertions.assertDoesNotThrow(() -> AssertUtils.notEmpty(map, "不应该为 null"));
  }

  @Test
  void notEmpty_Map_Fail_Null() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notEmpty((Map<?, ?>) null, "不能为 null"));
  }

  @Test
  void notEmpty_Map_Fail_Empty() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notEmpty(Collections.emptyMap(), "不能为空 Map"));
  }

  @Test
  void notEmpty_Map_Supplier_Pass() {
    Map<String, String> map = Collections.singletonMap("k", "v");
    Assertions.assertDoesNotThrow(() -> AssertUtils.notEmpty(map, neverCalled()));
  }

  @Test
  void notEmpty_Map_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notEmpty(new HashMap<>(), () -> "延迟构建的消息"));
  }

  // ==================== isInstanceOf ====================

  @Test
  void isInstanceOf_NoMessage_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isInstanceOf(String.class, "hello"));
  }

  @Test
  void isInstanceOf_NoMessage_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isInstanceOf(Integer.class, "hello"));
  }

  @Test
  void isInstanceOf_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isInstanceOf(CharSequence.class, "hello", "类型匹配"));
  }

  @Test
  void isInstanceOf_Fail_Null() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isInstanceOf(String.class, null, "期望 String"));
  }

  @Test
  void isInstanceOf_Fail_WrongType() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isInstanceOf(Integer.class, "hello", "期望 Integer"));
  }

  @Test
  void isInstanceOf_SeparatorEnding() {
    IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isInstanceOf(Integer.class, "hello", "类型错误:"));
    Assertions.assertTrue(ex.getMessage().contains("java.lang.Integer"));
    Assertions.assertTrue(ex.getMessage().contains("java.lang.String"));
  }

  @Test
  void isInstanceOf_SpaceEnding() {
    IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isInstanceOf(Integer.class, "hello", "类型错误 "));
    Assertions.assertTrue(ex.getMessage().endsWith("java.lang.String"));
  }

  @Test
  void isInstanceOf_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isInstanceOf(String.class, "hello", neverCalled()));
  }

  @Test
  void isInstanceOf_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isInstanceOf(Integer.class, "hello", () -> "延迟构建的消息"));
  }

  // ==================== isAssignable ====================

  @Test
  void isAssignable_NoMessage_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isAssignable(Number.class, Integer.class));
  }

  @Test
  void isAssignable_NoMessage_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isAssignable(Integer.class, Number.class));
  }

  @Test
  void isAssignable_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isAssignable(Object.class, String.class, "类型可赋值"));
  }

  @Test
  void isAssignable_Fail_NullSubType() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isAssignable(Number.class, null, "期望可赋值"));
  }

  @Test
  void isAssignable_Fail_NotAssignable() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isAssignable(String.class, Integer.class, "期望可赋值"));
  }

  @Test
  void isAssignable_SeparatorEnding() {
    IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isAssignable(String.class, Integer.class, "类型错误:"));
    Assertions.assertTrue(ex.getMessage().contains("java.lang.String"));
    Assertions.assertTrue(ex.getMessage().contains("java.lang.Integer"));
  }

  @Test
  void isAssignable_SpaceEnding() {
    IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isAssignable(String.class, Integer.class, "类型错误 "));
    Assertions.assertTrue(ex.getMessage().endsWith("java.lang.Integer"));
  }

  @Test
  void isAssignable_Supplier_Pass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.isAssignable(Number.class, Integer.class, neverCalled()));
  }

  @Test
  void isAssignable_Supplier_Fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.isAssignable(Integer.class, Number.class, () -> "延迟构建的消息"));
  }

  // ==================== Supplier 延迟求值验证 ====================

  @Test
  void supplier_NotEvaluatedOnPass() {
    Assertions.assertDoesNotThrow(() -> AssertUtils.notNull(new Object(), neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.isTrue(true, neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.isNull(null, neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.state(true, neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.hasLength("a", neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.hasText("a", neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.doesNotContain("a", "b", neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.notEmpty(new Object[]{"a"}, neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements(new Object[]{"a"}, neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.notEmpty(Collections.singletonList("a"), neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.noNullElements(Collections.singletonList("a"), neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.notEmpty(Collections.singletonMap("k", "v"), neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.isInstanceOf(String.class, "a", neverCalled()));
    Assertions.assertDoesNotThrow(() -> AssertUtils.isAssignable(Number.class, Integer.class, neverCalled()));
  }

  @Test
  void nullSupplier_ReturnsNullMessage() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> AssertUtils.notNull(null, (Supplier<String>) null));
  }
}
