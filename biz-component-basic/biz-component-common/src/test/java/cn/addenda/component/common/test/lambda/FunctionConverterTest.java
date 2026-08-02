package cn.addenda.component.common.test.lambda;

import cn.addenda.component.common.lambda.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class FunctionConverterTest {

  // ==================== Consumer → Function (Null) ====================

  @Test
  void testToFunction_Consumer() {
    String[] captured = {null};
    Function<String, Object> f = FunctionConverter.toNullFunction((Consumer<String>) s -> captured[0] = s);
    Assertions.assertNull(f.apply("hello"));
    Assertions.assertEquals("hello", captured[0]);
  }

  @Test
  void testToFunction_Consumer_Throws() {
    RuntimeException ex = new RuntimeException("test");
    Function<String, Object> f = FunctionConverter.toNullFunction((Consumer<String>) s -> {
      throw ex;
    });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> f.apply("x"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testToFunction_Consumer_ToString() {
    Consumer<String> c = s -> {
    };
    String s = FunctionConverter.toNullFunction(c).toString();
    Assertions.assertTrue(s.startsWith("NullFunction{consumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== BiConsumer → BiFunction (Null) ====================

  @Test
  void testToBiFunction_BiConsumer() {
    int[] sum = {0};
    BiFunction<Integer, Integer, Object> f = FunctionConverter.toNullBiFunction((BiConsumer<Integer, Integer>) (a, b) -> sum[0] = a + b);
    Assertions.assertNull(f.apply(3, 4));
    Assertions.assertEquals(7, sum[0]);
  }

  @Test
  void testToBiFunction_BiConsumer_ReturnsNull() {
    BiFunction<String, String, Object> f = FunctionConverter.toNullBiFunction((BiConsumer<String, String>) (a, b) -> {
    });
    Assertions.assertNull(f.apply("a", "b"));
  }

  @Test
  void testToBiFunction_BiConsumer_Throws() {
    RuntimeException ex = new RuntimeException("test");
    BiFunction<String, String, Object> f = FunctionConverter.toNullBiFunction((BiConsumer<String, String>) (a, b) -> {
      throw ex;
    });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> f.apply("a", "b"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testToBiFunction_BiConsumer_ToString() {
    BiConsumer<String, String> c = (a, b) -> {
    };
    String s = FunctionConverter.toNullBiFunction(c).toString();
    Assertions.assertTrue(s.startsWith("NullBiFunction{biConsumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== ThrowableConsumer → ThrowableFunction (Null) ====================

  @Test
  void testToThrowableFunction_ThrowableConsumer() throws Throwable {
    String[] captured = {null};
    ThrowableFunction<String, Object> f = FunctionConverter.toNullThrowableFunction((ThrowableConsumer<String>) s -> captured[0] = s);
    Assertions.assertNull(f.apply("hello"));
    Assertions.assertEquals("hello", captured[0]);
  }

  @Test
  void testToThrowableFunction_ThrowableConsumer_Throws() {
    Exception ex = new Exception("test");
    ThrowableFunction<String, Object> f = FunctionConverter.toNullThrowableFunction((ThrowableConsumer<String>) s -> {
      throw ex;
    });
    Exception thrown = Assertions.assertThrows(Exception.class, () -> f.apply("x"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testToThrowableFunction_ThrowableConsumer_ToString() {
    ThrowableConsumer<String> c = s -> {
    };
    String s = FunctionConverter.toNullThrowableFunction(c).toString();
    Assertions.assertTrue(s.startsWith("NullThrowableFunction{throwableConsumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== ThrowableBiConsumer → ThrowableBiFunction (Null) ====================

  @Test
  void testToThrowableBiFunction_ThrowableBiConsumer() throws Throwable {
    boolean[] called = {false};
    ThrowableBiFunction<String, Integer, Object> f = FunctionConverter.toNullThrowableBiFunction((ThrowableBiConsumer<String, Integer>) (a, b) -> called[0] = true);
    Assertions.assertNull(f.apply("x", 1));
    Assertions.assertTrue(called[0]);
  }

  @Test
  void testToThrowableBiFunction_ThrowableBiConsumer_Throws() {
    Exception ex = new Exception("test");
    ThrowableBiFunction<String, Integer, Object> f = FunctionConverter.toNullThrowableBiFunction((ThrowableBiConsumer<String, Integer>) (a, b) -> {
      throw ex;
    });
    Exception thrown = Assertions.assertThrows(Exception.class, () -> f.apply("a", 1));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testToThrowableBiFunction_ThrowableBiConsumer_ToString() {
    ThrowableBiConsumer<String, Integer> c = (a, b) -> {
    };
    String s = FunctionConverter.toNullThrowableBiFunction(c).toString();
    Assertions.assertTrue(s.startsWith("NullThrowableBiFunction{throwableBiConsumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== ExceptionConsumer → ExceptionFunction (Null) ====================

  @Test
  void testToExceptionFunction_ExceptionConsumer() throws Exception {
    String[] captured = {null};
    ExceptionFunction<String, Object> f = FunctionConverter.toNullExceptionFunction((ExceptionConsumer<String>) s -> captured[0] = s);
    Assertions.assertNull(f.apply("hello"));
    Assertions.assertEquals("hello", captured[0]);
  }

  @Test
  void testToExceptionFunction_ExceptionConsumer_Throws() {
    Exception ex = new Exception("test");
    ExceptionFunction<String, Object> f = FunctionConverter.toNullExceptionFunction((ExceptionConsumer<String>) s -> {
      throw ex;
    });
    Exception thrown = Assertions.assertThrows(Exception.class, () -> f.apply("x"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testToExceptionFunction_ExceptionConsumer_ToString() {
    ExceptionConsumer<String> c = s -> {
    };
    String s = FunctionConverter.toNullExceptionFunction(c).toString();
    Assertions.assertTrue(s.startsWith("NullExceptionFunction{exceptionConsumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== ExceptionBiConsumer → ExceptionBiFunction (Null) ====================

  @Test
  void testToExceptionBiFunction_ExceptionBiConsumer() throws Exception {
    boolean[] called = {false};
    ExceptionBiFunction<String, Integer, Object> f = FunctionConverter.toNullExceptionBiFunction((ExceptionBiConsumer<String, Integer>) (a, b) -> called[0] = true);
    Assertions.assertNull(f.apply("x", 1));
    Assertions.assertTrue(called[0]);
  }

  @Test
  void testToExceptionBiFunction_ExceptionBiConsumer_Throws() {
    Exception ex = new Exception("test");
    ExceptionBiFunction<String, Integer, Object> f = FunctionConverter.toNullExceptionBiFunction((ExceptionBiConsumer<String, Integer>) (a, b) -> {
      throw ex;
    });
    Exception thrown = Assertions.assertThrows(Exception.class, () -> f.apply("a", 1));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testToExceptionBiFunction_ExceptionBiConsumer_ToString() {
    ExceptionBiConsumer<String, Integer> c = (a, b) -> {
    };
    String s = FunctionConverter.toNullExceptionBiFunction(c).toString();
    Assertions.assertTrue(s.startsWith("NullExceptionBiFunction{exceptionBiConsumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== Runnable → Supplier (Null) ====================

  @Test
  void testToSupplier_Runnable() {
    boolean[] called = {false};
    Supplier<Integer> s = FunctionConverter.toNullSupplier(() -> called[0] = true);
    Assertions.assertNull(s.get());
    Assertions.assertTrue(called[0]);
  }

  @Test
  void testToSupplier_Runnable_Throws() {
    RuntimeException ex = new RuntimeException("test");
    Supplier<Integer> s = FunctionConverter.toNullSupplier(() -> {
      throw ex;
    });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, s::get);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testToSupplier_Runnable_ToString() {
    Runnable r = () -> {
    };
    String s = FunctionConverter.toNullSupplier(r).toString();
    Assertions.assertTrue(s.startsWith("NullSupplier{runnable="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== ThrowableRunnable → ThrowableSupplier (Null) ====================

  @Test
  void testToThrowableSupplier_ThrowableRunnable() throws Throwable {
    boolean[] called = {false};
    ThrowableSupplier<Integer> s = FunctionConverter.toNullThrowableSupplier((ThrowableRunnable) () -> {
      called[0] = true;
    });
    Assertions.assertNull(s.get());
    Assertions.assertTrue(called[0]);
  }

  @Test
  void testToThrowableSupplier_ThrowableRunnable_Throws() {
    Exception ex = new Exception("test");
    ThrowableSupplier<Integer> s = FunctionConverter.toNullThrowableSupplier((ThrowableRunnable) () -> {
      throw ex;
    });
    Exception thrown = Assertions.assertThrows(Exception.class, s::get);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testToThrowableSupplier_ThrowableRunnable_ToString() {
    ThrowableRunnable r = () -> {
    };
    String s = FunctionConverter.toNullThrowableSupplier(r).toString();
    Assertions.assertTrue(s.startsWith("NullThrowableSupplier{throwableRunnable="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== ExceptionRunnable → ExceptionSupplier (Null) ====================

  @Test
  void testToExceptionSupplier_ExceptionRunnable() throws Exception {
    boolean[] called = {false};
    ExceptionSupplier<Integer> s = FunctionConverter.toNullExceptionSupplier((ExceptionRunnable) () -> {
      called[0] = true;
    });
    Assertions.assertNull(s.get());
    Assertions.assertTrue(called[0]);
  }

  @Test
  void testToExceptionSupplier_ExceptionRunnable_Throws() {
    Exception ex = new Exception("test");
    ExceptionSupplier<Integer> s = FunctionConverter.toNullExceptionSupplier((ExceptionRunnable) () -> {
      throw ex;
    });
    Exception thrown = Assertions.assertThrows(Exception.class, s::get);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testToExceptionSupplier_ExceptionRunnable_ToString() {
    ExceptionRunnable r = () -> {
    };
    String s = FunctionConverter.toNullExceptionSupplier(r).toString();
    Assertions.assertTrue(s.startsWith("NullExceptionSupplier{exceptionRunnable="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== Wrapper: Function → ThrowableFunction / ExceptionFunction ====================

  @Test
  void testToThrowableFunction_Function() throws Throwable {
    Function<Integer, String> f = Object::toString;
    ThrowableFunction<Integer, String> tf = FunctionConverter.toThrowableFunction(f);
    Assertions.assertEquals("42", tf.apply(42));
  }

  @Test
  void testToThrowableFunction_Function_ToString() {
    Function<Integer, String> f = i -> "val";
    String s = FunctionConverter.toThrowableFunction(f).toString();
    Assertions.assertTrue(s.startsWith("FunctionAsThrowableFunction{function="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToThrowableFunction_ExceptionFunction() throws Throwable {
    Function<Integer, String> f = Object::toString;
    ThrowableFunction<Integer, String> tf = FunctionConverter.toThrowableFunction(FunctionConverter.toExceptionFunction(f));
    Assertions.assertEquals("42", tf.apply(42));
  }

  @Test
  void testToThrowableFunction_ExceptionFunction_ToString() throws Exception {
    ExceptionFunction<Integer, String> ef = i -> "val";
    String s = FunctionConverter.toThrowableFunction(ef).toString();
    Assertions.assertTrue(s.startsWith("ExceptionFunctionAsThrowableFunction{exceptionFunction="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToExceptionFunction_Function() throws Exception {
    Function<Integer, String> f = Object::toString;
    ExceptionFunction<Integer, String> ef = FunctionConverter.toExceptionFunction(f);
    Assertions.assertEquals("42", ef.apply(42));
  }

  @Test
  void testToExceptionFunction_Function_ToString() {
    Function<Integer, String> f = i -> "val";
    String s = FunctionConverter.toExceptionFunction(f).toString();
    Assertions.assertTrue(s.startsWith("FunctionAsExceptionFunction{function="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== Wrapper: Consumer → ThrowableConsumer / ExceptionConsumer ====================

  @Test
  void testToThrowableConsumer_Consumer() throws Throwable {
    String[] captured = {null};
    ThrowableConsumer<String> tc = FunctionConverter.toThrowableConsumer((Consumer<String>) s -> captured[0] = s);
    tc.accept("hello");
    Assertions.assertEquals("hello", captured[0]);
  }

  @Test
  void testToThrowableConsumer_Consumer_ToString() {
    Consumer<String> c = s -> {
    };
    String s = FunctionConverter.toThrowableConsumer(c).toString();
    Assertions.assertTrue(s.startsWith("ConsumerAsThrowableConsumer{consumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToThrowableConsumer_ExceptionConsumer() throws Throwable {
    String[] captured = {null};
    ThrowableConsumer<String> tc = FunctionConverter.toThrowableConsumer(
            FunctionConverter.toExceptionConsumer((Consumer<String>) s -> captured[0] = s));
    tc.accept("hello");
    Assertions.assertEquals("hello", captured[0]);
  }

  @Test
  void testToThrowableConsumer_ExceptionConsumer_ToString() throws Exception {
    ExceptionConsumer<String> ec = s -> {
    };
    String s = FunctionConverter.toThrowableConsumer(ec).toString();
    Assertions.assertTrue(s.startsWith("ExceptionConsumerAsThrowableConsumer{exceptionConsumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToExceptionConsumer_Consumer() throws Exception {
    String[] captured = {null};
    ExceptionConsumer<String> ec = FunctionConverter.toExceptionConsumer((Consumer<String>) s -> captured[0] = s);
    ec.accept("hello");
    Assertions.assertEquals("hello", captured[0]);
  }

  @Test
  void testToExceptionConsumer_Consumer_ToString() {
    Consumer<String> c = s -> {
    };
    String s = FunctionConverter.toExceptionConsumer(c).toString();
    Assertions.assertTrue(s.startsWith("ConsumerAsExceptionConsumer{consumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== Wrapper: BiFunction → ThrowableBiFunction / ExceptionBiFunction ====================

  @Test
  void testToThrowableBiFunction_BiFunction() throws Throwable {
    ThrowableBiFunction<Integer, Integer, Integer> tbf = FunctionConverter.toThrowableBiFunction((BiFunction<Integer, Integer, Integer>) Integer::sum);
    Assertions.assertEquals(7, tbf.apply(3, 4));
  }

  @Test
  void testToThrowableBiFunction_BiFunction_ToString() {
    BiFunction<Integer, Integer, Object> bf = (a, b) -> null;
    String s = FunctionConverter.toThrowableBiFunction(bf).toString();
    Assertions.assertTrue(s.startsWith("BiFunctionAsThrowableBiFunction{biFunction="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToThrowableBiFunction_ExceptionBiFunction() throws Throwable {
    ExceptionBiFunction<Integer, Integer, String> ebf = (a, b) -> String.valueOf(a + b);
    ThrowableBiFunction<Integer, Integer, String> tbf = FunctionConverter.toThrowableBiFunction(ebf);
    Assertions.assertEquals("7", tbf.apply(3, 4));
  }

  @Test
  void testToThrowableBiFunction_ExceptionBiFunction_ToString() throws Exception {
    ExceptionBiFunction<Integer, Integer, Object> ebf = (a, b) -> null;
    String s = FunctionConverter.toThrowableBiFunction(ebf).toString();
    Assertions.assertTrue(s.startsWith("ExceptionBiFunctionAsThrowableBiFunction{exceptionBiFunction="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToExceptionBiFunction_BiFunction() throws Exception {
    ExceptionBiFunction<Integer, Integer, Integer> ebf = FunctionConverter.toExceptionBiFunction((BiFunction<Integer, Integer, Integer>) Integer::sum);
    Assertions.assertEquals(7, ebf.apply(3, 4));
  }

  @Test
  void testToExceptionBiFunction_BiFunction_ToString() {
    BiFunction<Integer, Integer, Object> bf = (a, b) -> null;
    String s = FunctionConverter.toExceptionBiFunction(bf).toString();
    Assertions.assertTrue(s.startsWith("BiFunctionAsExceptionBiFunction{biFunction="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== Wrapper: BiConsumer → ThrowableBiConsumer / ExceptionBiConsumer ====================

  @Test
  void testToThrowableBiConsumer_BiConsumer() throws Throwable {
    Object[] captured = {null, null};
    ThrowableBiConsumer<String, Integer> tbc = FunctionConverter.toThrowableBiConsumer((BiConsumer<String, Integer>) (a, b) -> {
      captured[0] = a;
      captured[1] = b;
    });
    tbc.accept("hello", 42);
    Assertions.assertEquals("hello", captured[0]);
    Assertions.assertEquals(42, captured[1]);
  }

  @Test
  void testToThrowableBiConsumer_BiConsumer_ToString() {
    BiConsumer<String, String> bc = (a, b) -> {
    };
    String s = FunctionConverter.toThrowableBiConsumer(bc).toString();
    Assertions.assertTrue(s.startsWith("BiConsumerAsThrowableBiConsumer{biConsumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToThrowableBiConsumer_ExceptionBiConsumer() throws Throwable {
    Object[] captured = {null, null};
    ThrowableBiConsumer<String, Integer> tbc = FunctionConverter.toThrowableBiConsumer(
            FunctionConverter.toExceptionBiConsumer((BiConsumer<String, Integer>) (a, b) -> {
              captured[0] = a;
              captured[1] = b;
            }));
    tbc.accept("hello", 42);
    Assertions.assertEquals("hello", captured[0]);
    Assertions.assertEquals(42, captured[1]);
  }

  @Test
  void testToThrowableBiConsumer_ExceptionBiConsumer_ToString() throws Exception {
    ExceptionBiConsumer<String, String> ebc = (a, b) -> {
    };
    String s = FunctionConverter.toThrowableBiConsumer(ebc).toString();
    Assertions.assertTrue(s.startsWith("ExceptionBiConsumerAsThrowableBiConsumer{exceptionBiConsumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToExceptionBiConsumer_BiConsumer() throws Exception {
    Object[] captured = {null, null};
    ExceptionBiConsumer<String, Integer> ebc = FunctionConverter.toExceptionBiConsumer((BiConsumer<String, Integer>) (a, b) -> {
      captured[0] = a;
      captured[1] = b;
    });
    ebc.accept("hello", 42);
    Assertions.assertEquals("hello", captured[0]);
    Assertions.assertEquals(42, captured[1]);
  }

  @Test
  void testToExceptionBiConsumer_BiConsumer_ToString() {
    BiConsumer<String, String> bc = (a, b) -> {
    };
    String s = FunctionConverter.toExceptionBiConsumer(bc).toString();
    Assertions.assertTrue(s.startsWith("BiConsumerAsExceptionBiConsumer{biConsumer="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== Wrapper: Supplier → ThrowableSupplier / ExceptionSupplier ====================

  @Test
  void testToThrowableSupplier_Supplier() throws Throwable {
    ThrowableSupplier<Integer> ts = FunctionConverter.toThrowableSupplier((Supplier<Integer>) () -> 42);
    Assertions.assertEquals(42, ts.get());
  }

  @Test
  void testToThrowableSupplier_Supplier_ToString() {
    Supplier<Integer> sup = () -> 1;
    String s = FunctionConverter.toThrowableSupplier(sup).toString();
    Assertions.assertTrue(s.startsWith("SupplierAsThrowableSupplier{supplier="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToThrowableSupplier_ExceptionSupplier() throws Throwable {
    ExceptionSupplier<Integer> es = () -> 42;
    ThrowableSupplier<Integer> ts = FunctionConverter.toThrowableSupplier(es);
    Assertions.assertEquals(42, ts.get());
  }

  @Test
  void testToThrowableSupplier_ExceptionSupplier_ToString() throws Exception {
    ExceptionSupplier<Integer> es = () -> 1;
    String s = FunctionConverter.toThrowableSupplier(es).toString();
    Assertions.assertTrue(s.startsWith("ExceptionSupplierAsThrowableSupplier{exceptionSupplier="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToExceptionSupplier_Supplier() throws Exception {
    ExceptionSupplier<Integer> es = FunctionConverter.toExceptionSupplier(() -> 42);
    Assertions.assertEquals(42, es.get());
  }

  @Test
  void testToExceptionSupplier_Supplier_ToString() {
    Supplier<Integer> sup = () -> 1;
    String s = FunctionConverter.toExceptionSupplier(sup).toString();
    Assertions.assertTrue(s.startsWith("SupplierAsExceptionSupplier{supplier="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== Wrapper: Runnable → ThrowableRunnable / ExceptionRunnable ====================

  @Test
  void testToThrowableRunnable_Runnable() throws Throwable {
    boolean[] called = {false};
    ThrowableRunnable tr = FunctionConverter.toThrowableRunnable((Runnable) () -> {
      called[0] = true;
    });
    tr.run();
    Assertions.assertTrue(called[0]);
  }

  @Test
  void testToThrowableRunnable_Runnable_ToString() {
    Runnable r = () -> {
    };
    String s = FunctionConverter.toThrowableRunnable(r).toString();
    Assertions.assertTrue(s.startsWith("RunnableAsThrowableRunnable{runnable="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToThrowableRunnable_ExceptionRunnable() throws Throwable {
    boolean[] called = {false};
    ThrowableRunnable tr = FunctionConverter.toThrowableRunnable(FunctionConverter.toExceptionRunnable(() -> called[0] = true));
    tr.run();
    Assertions.assertTrue(called[0]);
  }

  @Test
  void testToThrowableRunnable_ExceptionRunnable_ToString() throws Exception {
    ExceptionRunnable er = () -> {
    };
    String s = FunctionConverter.toThrowableRunnable(er).toString();
    Assertions.assertTrue(s.startsWith("ExceptionRunnableAsThrowableRunnable{exceptionRunnable="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  @Test
  void testToExceptionRunnable_Runnable() throws Exception {
    boolean[] called = {false};
    ExceptionRunnable er = FunctionConverter.toExceptionRunnable((Runnable) () -> {
      called[0] = true;
    });
    er.run();
    Assertions.assertTrue(called[0]);
  }

  @Test
  void testToExceptionRunnable_Runnable_ToString() {
    Runnable r = () -> {
    };
    String s = FunctionConverter.toExceptionRunnable(r).toString();
    Assertions.assertTrue(s.startsWith("RunnableAsExceptionRunnable{runnable="), s);
    Assertions.assertTrue(s.endsWith("}"), s);
  }

  // ==================== Throwable 接口独立测试 ====================

  @Test
  void testThrowableSupplier_Get() throws Throwable {
    ThrowableSupplier<Integer> s = () -> 42;
    Assertions.assertEquals(42, s.get());
  }

  @Test
  void testThrowableSupplier_Get_ReturnsNull() throws Throwable {
    ThrowableSupplier<Object> s = () -> null;
    Assertions.assertNull(s.get());
  }

  @Test
  void testThrowableFunction_Apply_ReturnsNull() throws Throwable {
    ThrowableFunction<String, Object> f = s -> null;
    Assertions.assertNull(f.apply("x"));
  }

  @Test
  void testThrowableConsumer_Accept() throws Throwable {
    boolean[] called = {false};
    ThrowableConsumer<String> c = s -> called[0] = true;
    c.accept("test");
    Assertions.assertTrue(called[0]);
  }

  @Test
  void testThrowableConsumer_Accept_WithArg() throws Throwable {
    String[] captured = {null};
    ThrowableConsumer<String> c = s -> captured[0] = s;
    c.accept("hello");
    Assertions.assertEquals("hello", captured[0]);
  }

  @Test
  void testThrowableBiFunction_Apply_ReturnsNull() throws Throwable {
    ThrowableBiFunction<String, String, Object> f = (a, b) -> null;
    Assertions.assertNull(f.apply("a", "b"));
  }

  @Test
  void testThrowableBiConsumer_Accept() throws Throwable {
    int[] sum = {0};
    ThrowableBiConsumer<Integer, Integer> c = (a, b) -> sum[0] = a + b;
    c.accept(3, 4);
    Assertions.assertEquals(7, sum[0]);
  }

  @Test
  void testThrowableBiConsumer_Accept_WithArgs() throws Throwable {
    Object[] captured = {null, null};
    ThrowableBiConsumer<String, Integer> c = (s, i) -> {
      captured[0] = s;
      captured[1] = i;
    };
    c.accept("hello", 42);
    Assertions.assertEquals("hello", captured[0]);
    Assertions.assertEquals(42, captured[1]);
  }

}
