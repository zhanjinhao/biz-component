package cn.addenda.component.common.test.result;

import cn.addenda.component.common.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

class ResultTest {

  // ==================================================================
  //  success
  // ==================================================================

  @Test
  void success_noArg() {
    Result<String> r = Result.success();
    Assertions.assertTrue(r.isSuccess());
    Assertions.assertFalse(r.isFail());
    Assertions.assertEquals(Result.STATUS_OK, r.getStatus());
    Assertions.assertNull(r.getValue());
    Assertions.assertNull(r.getErrorMsg());
  }

  @Test
  void success_withValue() {
    Result<String> r = Result.success("hello");
    Assertions.assertTrue(r.isSuccess());
    Assertions.assertEquals("hello", r.getValue());
    Assertions.assertNull(r.getErrorMsg());
  }

  @Test
  void success_withNullValue() {
    Result<String> r = Result.success((String) null);
    Assertions.assertTrue(r.isSuccess());
    Assertions.assertNull(r.getValue());
  }

  @Test
  void success_withSupplier() {
    Result<Integer> r = Result.success(() -> 42);
    Assertions.assertTrue(r.isSuccess());
    Assertions.assertEquals(42, (int) r.getValue());
  }

  @Test
  void success_withSupplier_returnsNull() {
    Result<Object> r = Result.success(() -> null);
    Assertions.assertTrue(r.isSuccess());
    Assertions.assertNull(r.getValue());
  }

  @Test
  void success_withFunction() {
    Function<String, Integer> fn = String::length;
    Result<Integer> r = Result.success("hello", fn);
    Assertions.assertTrue(r.isSuccess());
    Assertions.assertEquals(5, (int) r.getValue());
  }

  @Test
  void success_withSupplierAndFunction() {
    Supplier<List<String>> supplier = () -> Arrays.asList("a", "b", "c");
    Function<List<String>, String> fn = Object::toString;
    Result<String> r = Result.success(supplier, fn);
    Assertions.assertTrue(r.isSuccess());
    Assertions.assertEquals("[a, b, c]", r.getValue());
  }

  // ==================================================================
  //  fail
  // ==================================================================

  @Test
  void fail_noArg() {
    Result<String> r = Result.fail();
    Assertions.assertTrue(r.isFail());
    Assertions.assertFalse(r.isSuccess());
    Assertions.assertEquals(Result.STATUS_FAILED, r.getStatus());
    Assertions.assertNull(r.getValue());
    Assertions.assertNull(r.getErrorMsg());
  }

  @Test
  void fail_withErrorMsg() {
    Result<String> r = Result.fail("something went wrong");
    Assertions.assertTrue(r.isFail());
    Assertions.assertEquals("something went wrong", r.getErrorMsg());
    Assertions.assertNull(r.getValue());
  }

  @Test
  void fail_withNullErrorMsg() {
    Result<String> r = Result.fail((String) null);
    Assertions.assertTrue(r.isFail());
    Assertions.assertNull(r.getErrorMsg());
  }

  // ==================================================================
  //  value / errorMsg 互斥
  // ==================================================================

  @Test
  void success_valuePresent_errorMsgNull() {
    Result<String> r = Result.success("data");
    Assertions.assertNotNull(r.getValue());
    Assertions.assertNull(r.getErrorMsg());
  }

  @Test
  void fail_errorMsgPresent_valueNull() {
    Result<String> r = Result.fail("boom");
    Assertions.assertNotNull(r.getErrorMsg());
    Assertions.assertNull(r.getValue());
  }

  // ==================================================================
  //  isSuccess / isFail
  // ==================================================================

  @Test
  void isSuccess_returnsTrueForOK() {
    Assertions.assertTrue(Result.success().isSuccess());
    Assertions.assertTrue(Result.success("x").isSuccess());
    Assertions.assertTrue(Result.success(() -> "x").isSuccess());
  }

  @Test
  void isFail_returnsTrueForFailed() {
    Assertions.assertTrue(Result.fail().isFail());
    Assertions.assertTrue(Result.fail("error").isFail());
  }

  @Test
  void isSuccess_returnsFalseForFailed() {
    Assertions.assertFalse(Result.fail().isSuccess());
    Assertions.assertFalse(Result.fail("error").isSuccess());
  }

  @Test
  void isFail_returnsFalseForSuccess() {
    Assertions.assertFalse(Result.success().isFail());
    Assertions.assertFalse(Result.success("x").isFail());
    Assertions.assertFalse(Result.success(() -> "x").isFail());
  }

  // ==================================================================
  //  toString
  // ==================================================================

  @Test
  void toString_success() {
    String s = Result.success("data").toString();
    Assertions.assertTrue(s.contains("data"));
    Assertions.assertTrue(s.contains(Result.STATUS_OK));
  }

  @Test
  void toString_fail() {
    String s = Result.fail("error info").toString();
    Assertions.assertTrue(s.contains("error info"));
    Assertions.assertTrue(s.contains(Result.STATUS_FAILED));
  }

  // ==================================================================
  //  generic types
  // ==================================================================

  @Test
  void generic_complexType() {
    Result<List<Integer>> r = Result.success(Arrays.asList(1, 2, 3));
    Assertions.assertTrue(r.isSuccess());
    List<Integer> list = r.getValue();
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(1, (int) list.get(0));
  }

  @Test
  void generic_differentTypes_sameFactory() {
    Result<String> s = Result.success("str");
    Result<Integer> i = Result.success(42);
    Result<Boolean> b = Result.success(true);

    Assertions.assertEquals("str", s.getValue());
    Assertions.assertEquals(42, (int) i.getValue());
    Assertions.assertEquals(true, b.getValue());
  }

  @Test
  void generic_fail_erasedType() {
    Result<?> r = Result.fail("generic error");
    Assertions.assertTrue(r.isFail());
    Assertions.assertEquals("generic error", r.getErrorMsg());
  }

  // ==================================================================
  //  immutability
  // ==================================================================

  @Test
  void success_value_notNull() {
    Result<String> r = Result.success("immutable");
    Assertions.assertEquals("immutable", r.getValue());
    Assertions.assertSame(r.getValue(), r.getValue());
  }

  @Test
  void fail_errorMsg_notNull() {
    Result<String> r = Result.fail("immutable error");
    Assertions.assertEquals("immutable error", r.getErrorMsg());
    Assertions.assertSame(r.getErrorMsg(), r.getErrorMsg());
  }

  @Test
  void status_immutable() {
    Assertions.assertEquals(Result.STATUS_OK, Result.success().getStatus());
    Assertions.assertEquals(Result.STATUS_FAILED, Result.fail().getStatus());
  }

  // ==================================================================
  //  supplier exception propagation
  // ==================================================================

  @Test
  void success_supplierThrows_propagates() {
    Supplier<String> throwingSupplier = () -> {
      throw new IllegalStateException("supplier failed");
    };
    Assertions.assertThrows(IllegalStateException.class, () -> Result.success(throwingSupplier));
  }

  @Test
  void success_supplierAndFunction_propagates() {
    Supplier<String> throwingSupplier = () -> {
      throw new IllegalArgumentException("boom");
    };
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> Result.success(throwingSupplier, (Function<String, String>) x -> x));
  }

  // ==================================================================
  //  lazy evaluation
  // ==================================================================

  @Test
  void success_functionApplied_lazilyViaSupplier() {
    AtomicBoolean called = new AtomicBoolean(false);
    Function<Integer, Integer> doubleFn = x -> x * 2;
    Supplier<Integer> supplier = () -> {
      called.set(true);
      return 100;
    };
    Result<Integer> r = Result.success(supplier, doubleFn);
    Assertions.assertTrue(r.isSuccess());
    Assertions.assertEquals(200, (int) r.getValue());
    Assertions.assertTrue(called.get());
  }
}
