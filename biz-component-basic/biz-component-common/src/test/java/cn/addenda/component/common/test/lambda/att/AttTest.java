package cn.addenda.component.common.test.lambda.att;

import cn.addenda.component.common.lambda.att.AttBiConsumer;
import cn.addenda.component.common.lambda.att.AttBiFunction;
import cn.addenda.component.common.lambda.att.AttCallable;
import cn.addenda.component.common.lambda.att.AttConsumer;
import cn.addenda.component.common.lambda.att.AttFunction;
import cn.addenda.component.common.lambda.att.AttRunnable;
import cn.addenda.component.common.lambda.att.AttSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class AttTest {

  // ==================== AttRunnable ====================

  @Test
  void testRunnable_Run() {
    AtomicBoolean called = new AtomicBoolean(false);
    AttRunnable<String> r = AttRunnable.of("ctx", () -> called.set(true));
    r.run();
    Assertions.assertTrue(called.get());
  }

  @Test
  void testRunnable_Run_Throws() {
    RuntimeException ex = new RuntimeException("test");
    AttRunnable<String> r = AttRunnable.of("ctx", () -> {
      throw ex;
    });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, r::run);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testRunnable_GetAtt() {
    AttRunnable<String> r = AttRunnable.of("ctx", () -> {
    });
    Assertions.assertEquals("ctx", r.getAtt());
  }

  @Test
  void testRunnable_GetRunnable() {
    Runnable delegate = () -> {
    };
    AttRunnable<String> r = AttRunnable.of("ctx", delegate);
    Assertions.assertSame(delegate, r.getRunnable());
  }

  @Test
  void testRunnable_NullAtt() {
    AttRunnable<String> r = AttRunnable.of(null, () -> {
    });
    Assertions.assertNull(r.getAtt());
    Assertions.assertTrue(r.toString().contains(", att=null"));
  }

  @Test
  void testRunnable_NullAtt_JacksonTrue() {
    AttRunnable<String> r = AttRunnable.of(null, () -> {
    }, true);
    Assertions.assertTrue(r.toString().contains(", att=null"));
  }

  @Test
  void testRunnable_ToString() {
    AttRunnable<String> r = AttRunnable.of("ctx", () -> {
    });
    String s = r.toString();
    Assertions.assertTrue(s.contains("AttRunnable{runnable=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testRunnable_ToString_JacksonFalse() {
    AttRunnable<String> r = AttRunnable.of("ctx", () -> {
    }, false);
    String s = r.toString();
    Assertions.assertTrue(s.contains("AttRunnable{runnable=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testRunnable_ToString_JacksonTrue() {
    AttRunnable<String> r = AttRunnable.of("ctx", () -> {
    }, true);
    String s = r.toString();
    Assertions.assertTrue(s.contains("AttRunnable{runnable=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=\"ctx\"}"));
  }

  @Test
  void testRunnable_ToString_JacksonTrue_Complex() {
    java.util.Map<String, Object> att = new java.util.HashMap<>();
    att.put("key", "value");
    AttRunnable<java.util.Map<String, Object>> r = AttRunnable.of(att, () -> {
    }, true);
    String s = r.toString();
    Assertions.assertTrue(s.contains("\"key\""));
    Assertions.assertTrue(s.contains("\"value\""));
  }

  // ==================== AttSupplier ====================

  @Test
  void testSupplier_Get_ReturnsValue() {
    AttSupplier<String, Integer> s = AttSupplier.of("ctx", () -> 42);
    Assertions.assertEquals(42, s.get());
  }

  @Test
  void testSupplier_Get_ReturnsNull() {
    AttSupplier<String, Object> s = AttSupplier.of("ctx", () -> null);
    Assertions.assertNull(s.get());
  }

  @Test
  void testSupplier_Get_Throws() {
    RuntimeException ex = new RuntimeException("test");
    AttSupplier<String, Object> s = AttSupplier.of("ctx", () -> {
      throw ex;
    });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, s::get);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testSupplier_Get_ExecutesEachTime() {
    AtomicInteger counter = new AtomicInteger(0);
    AttSupplier<String, Integer> s = AttSupplier.of("ctx", counter::incrementAndGet);
    Assertions.assertEquals(1, s.get());
    Assertions.assertEquals(2, s.get());
    Assertions.assertEquals(3, s.get());
  }

  @Test
  void testSupplier_GetAtt() {
    AttSupplier<String, Integer> s = AttSupplier.of("ctx", () -> 42);
    Assertions.assertEquals("ctx", s.getAtt());
  }

  @Test
  void testSupplier_GetSupplier() {
    Supplier<Integer> delegate = () -> 42;
    AttSupplier<String, Integer> s = AttSupplier.of("ctx", delegate);
    Assertions.assertSame(delegate, s.getSupplier());
  }

  @Test
  void testSupplier_NullAtt() {
    AttSupplier<String, Integer> s = AttSupplier.of(null, () -> 42);
    Assertions.assertNull(s.getAtt());
    Assertions.assertTrue(s.toString().contains(", att=null"));
  }

  @Test
  void testSupplier_ToString() {
    AttSupplier<String, Integer> s = AttSupplier.of("ctx", () -> 42);
    String str = s.toString();
    Assertions.assertTrue(str.contains("AttSupplier{supplier=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(str.contains(", att=ctx}"));
  }

  @Test
  void testSupplier_ToString_JacksonFalse() {
    AttSupplier<String, Integer> s = AttSupplier.of("ctx", () -> 42, false);
    String str = s.toString();
    Assertions.assertTrue(str.contains("AttSupplier{supplier=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(str.contains(", att=ctx}"));
  }

  @Test
  void testSupplier_ToString_JacksonTrue() {
    AttSupplier<String, Integer> s = AttSupplier.of("ctx", () -> 42, true);
    String str = s.toString();
    Assertions.assertTrue(str.contains("AttSupplier{supplier=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(str.contains(", att=\"ctx\"}"));
  }

  @Test
  void testSupplier_ToString_JacksonTrue_Complex() {
    java.util.Map<String, Object> att = new java.util.HashMap<>();
    att.put("key", "value");
    AttSupplier<java.util.Map<String, Object>, Integer> s = AttSupplier.of(att, () -> 42, true);
    String str = s.toString();
    Assertions.assertTrue(str.contains("\"key\""));
    Assertions.assertTrue(str.contains("\"value\""));
  }

  // ==================== AttFunction ====================

  @Test
  void testFunction_Apply_ReturnsValue() {
    AttFunction<String, Integer, String> f = AttFunction.of("ctx", Object::toString);
    Assertions.assertEquals("42", f.apply(42));
  }

  @Test
  void testFunction_Apply_ReturnsNull() {
    AttFunction<String, String, Object> f = AttFunction.of("ctx", s -> null);
    Assertions.assertNull(f.apply("x"));
  }

  @Test
  void testFunction_Apply_Throws() {
    RuntimeException ex = new RuntimeException("test");
    AttFunction<String, Integer, String> f = AttFunction.of("ctx", i -> {
      throw ex;
    });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> f.apply(1));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testFunction_Apply_ExecutesEachTime() {
    AtomicInteger counter = new AtomicInteger(0);
    AttFunction<String, Integer, Integer> f = AttFunction.of("ctx", i -> counter.incrementAndGet());
    Assertions.assertEquals(1, f.apply(1));
    Assertions.assertEquals(2, f.apply(2));
    Assertions.assertEquals(3, f.apply(3));
  }

  @Test
  void testFunction_GetAtt() {
    AttFunction<String, Integer, String> f = AttFunction.of("ctx", Object::toString);
    Assertions.assertEquals("ctx", f.getAtt());
  }

  @Test
  void testFunction_GetFunction() {
    Function<Integer, String> delegate = Object::toString;
    AttFunction<String, Integer, String> f = AttFunction.of("ctx", delegate);
    Assertions.assertSame(delegate, f.getFunction());
  }

  @Test
  void testFunction_NullAtt() {
    AttFunction<String, Integer, String> f = AttFunction.of(null, Object::toString);
    Assertions.assertNull(f.getAtt());
    Assertions.assertTrue(f.toString().contains(", att=null"));
  }

  @Test
  void testFunction_ToString() {
    AttFunction<String, Integer, String> f = AttFunction.of("ctx", Object::toString);
    String s = f.toString();
    Assertions.assertTrue(s.contains("AttFunction{function=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testFunction_ToString_JacksonFalse() {
    AttFunction<String, Integer, String> f = AttFunction.of("ctx", Object::toString, false);
    String s = f.toString();
    Assertions.assertTrue(s.contains("AttFunction{function=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testFunction_ToString_JacksonTrue() {
    AttFunction<String, Integer, String> f = AttFunction.of("ctx", Object::toString, true);
    String s = f.toString();
    Assertions.assertTrue(s.contains("AttFunction{function=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=\"ctx\"}"));
  }

  @Test
  void testFunction_ToString_JacksonTrue_Complex() {
    java.util.Map<String, Object> att = new java.util.HashMap<>();
    att.put("key", "value");
    AttFunction<java.util.Map<String, Object>, String, String> f = AttFunction.of(att, s -> s, true);
    String str = f.toString();
    Assertions.assertTrue(str.contains("\"key\""));
    Assertions.assertTrue(str.contains("\"value\""));
  }

  // ==================== AttCallable ====================

  @Test
  void testCallable_Call_ReturnsValue() throws Exception {
    AttCallable<String, Integer> c = AttCallable.of("ctx", () -> 42);
    Assertions.assertEquals(42, c.call());
  }

  @Test
  void testCallable_Call_ReturnsNull() throws Exception {
    AttCallable<String, Object> c = AttCallable.of("ctx", () -> null);
    Assertions.assertNull(c.call());
  }

  @Test
  void testCallable_Call_Throws() {
    Exception ex = new Exception("test");
    AttCallable<String, Object> c = AttCallable.of("ctx", () -> {
      throw ex;
    });
    Exception thrown = Assertions.assertThrows(Exception.class, c::call);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testCallable_Call_ExecutesEachTime() throws Exception {
    AtomicInteger counter = new AtomicInteger(0);
    AttCallable<String, Integer> c = AttCallable.of("ctx", counter::incrementAndGet);
    Assertions.assertEquals(1, c.call());
    Assertions.assertEquals(2, c.call());
    Assertions.assertEquals(3, c.call());
  }

  @Test
  void testCallable_GetAtt() throws Exception {
    AttCallable<String, Integer> c = AttCallable.of("ctx", () -> 42);
    Assertions.assertEquals("ctx", c.getAtt());
  }

  @Test
  void testCallable_GetCallable() {
    java.util.concurrent.Callable<Integer> delegate = () -> 42;
    AttCallable<String, Integer> c = AttCallable.of("ctx", delegate);
    Assertions.assertSame(delegate, c.getCallable());
  }

  @Test
  void testCallable_NullAtt() {
    AttCallable<String, Integer> c = AttCallable.of(null, () -> 42);
    Assertions.assertNull(c.getAtt());
    Assertions.assertTrue(c.toString().contains(", att=null"));
  }

  @Test
  void testCallable_NullAtt_JacksonTrue() {
    AttCallable<String, Integer> c = AttCallable.of(null, () -> 42, true);
    Assertions.assertTrue(c.toString().contains(", att=null"));
  }

  @Test
  void testCallable_ToString() {
    AttCallable<String, Integer> c = AttCallable.of("ctx", () -> 42);
    String s = c.toString();
    Assertions.assertTrue(s.contains("AttCallable{callable=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testCallable_ToString_JacksonFalse() {
    AttCallable<String, Integer> c = AttCallable.of("ctx", () -> 42, false);
    String s = c.toString();
    Assertions.assertTrue(s.contains("AttCallable{callable=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testCallable_ToString_JacksonTrue() {
    AttCallable<String, Integer> c = AttCallable.of("ctx", () -> 42, true);
    String s = c.toString();
    Assertions.assertTrue(s.contains("AttCallable{callable=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=\"ctx\"}"));
  }

  @Test
  void testCallable_ToString_JacksonTrue_Complex() throws Exception {
    java.util.Map<String, Object> att = new java.util.HashMap<>();
    att.put("key", "value");
    att.put("num", 42);
    AttCallable<java.util.Map<String, Object>, Integer> c = AttCallable.of(att, () -> 42, true);
    String s = c.toString();
    Assertions.assertTrue(s.contains("\"key\""));
    Assertions.assertTrue(s.contains("\"value\""));
    Assertions.assertTrue(s.contains("\"num\""));
    Assertions.assertTrue(s.contains("42"));
  }

  // ==================== AttConsumer ====================

  @Test
  void testConsumer_Accept() {
    AtomicBoolean called = new AtomicBoolean(false);
    AttConsumer<String, String> c = AttConsumer.of("ctx", s -> called.set(true));
    c.accept("test");
    Assertions.assertTrue(called.get());
  }

  @Test
  void testConsumer_Accept_Throws() {
    RuntimeException ex = new RuntimeException("test");
    AttConsumer<String, String> c = AttConsumer.of("ctx", s -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> c.accept("x"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testConsumer_GetAtt() {
    AttConsumer<String, String> c = AttConsumer.of("ctx", s -> {});
    Assertions.assertEquals("ctx", c.getAtt());
  }

  @Test
  void testConsumer_GetConsumer() {
    Consumer<String> delegate = s -> {};
    AttConsumer<String, String> c = AttConsumer.of("ctx", delegate);
    Assertions.assertSame(delegate, c.getConsumer());
  }

  @Test
  void testConsumer_NullAtt() {
    AttConsumer<String, String> c = AttConsumer.of(null, s -> {});
    Assertions.assertNull(c.getAtt());
    Assertions.assertTrue(c.toString().contains(", att=null"));
  }

  @Test
  void testConsumer_ToString() {
    AttConsumer<String, String> c = AttConsumer.of("ctx", s -> {});
    String s = c.toString();
    Assertions.assertTrue(s.contains("AttConsumer{consumer=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testConsumer_ToString_JacksonFalse() {
    AttConsumer<String, String> c = AttConsumer.of("ctx", s -> {}, false);
    String s = c.toString();
    Assertions.assertTrue(s.contains("AttConsumer{consumer=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testConsumer_ToString_JacksonTrue() {
    AttConsumer<String, String> c = AttConsumer.of("ctx", s -> {}, true);
    String s = c.toString();
    Assertions.assertTrue(s.contains("AttConsumer{consumer=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=\"ctx\"}"));
  }

  // ==================== AttBiFunction ====================

  @Test
  void testBiFunction_Apply_ReturnsValue() {
    AttBiFunction<String, Integer, Integer, Integer> f = AttBiFunction.of("ctx", Integer::sum);
    Assertions.assertEquals(7, f.apply(3, 4));
  }

  @Test
  void testBiFunction_Apply_ReturnsNull() {
    AttBiFunction<String, String, String, Object> f = AttBiFunction.of("ctx", (a, b) -> null);
    Assertions.assertNull(f.apply("a", "b"));
  }

  @Test
  void testBiFunction_Apply_Throws() {
    RuntimeException ex = new RuntimeException("test");
    AttBiFunction<String, Integer, Integer, Integer> f = AttBiFunction.of("ctx", (a, b) -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> f.apply(1, 2));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testBiFunction_Apply_ExecutesEachTime() {
    AtomicInteger counter = new AtomicInteger(0);
    AttBiFunction<String, String, String, Integer> f = AttBiFunction.of("ctx", (a, b) -> counter.incrementAndGet());
    Assertions.assertEquals(1, f.apply("a", "b"));
    Assertions.assertEquals(2, f.apply("c", "d"));
  }

  @Test
  void testBiFunction_GetAtt() {
    AttBiFunction<String, Integer, Integer, Integer> f = AttBiFunction.of("ctx", Integer::sum);
    Assertions.assertEquals("ctx", f.getAtt());
  }

  @Test
  void testBiFunction_GetBiFunction() {
    BiFunction<Integer, Integer, Integer> delegate = Integer::sum;
    AttBiFunction<String, Integer, Integer, Integer> f = AttBiFunction.of("ctx", delegate);
    Assertions.assertSame(delegate, f.getBiFunction());
  }

  @Test
  void testBiFunction_NullAtt() {
    AttBiFunction<String, Integer, Integer, Integer> f = AttBiFunction.of(null, Integer::sum);
    Assertions.assertNull(f.getAtt());
    Assertions.assertTrue(f.toString().contains(", att=null"));
  }

  @Test
  void testBiFunction_ToString() {
    AttBiFunction<String, Integer, Integer, Integer> f = AttBiFunction.of("ctx", Integer::sum);
    String s = f.toString();
    Assertions.assertTrue(s.contains("AttBiFunction{biFunction=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testBiFunction_ToString_JacksonTrue() {
    AttBiFunction<String, Integer, Integer, Integer> f = AttBiFunction.of("ctx", Integer::sum, true);
    String s = f.toString();
    Assertions.assertTrue(s.contains("AttBiFunction{biFunction=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=\"ctx\"}"));
  }

  @Test
  void testBiFunction_ToString_JacksonTrue_Complex() {
    java.util.Map<String, Object> att = new java.util.HashMap<>();
    att.put("key", "value");
    AttBiFunction<java.util.Map<String, Object>, String, String, String> f = AttBiFunction.of(att, (a, b) -> a + b, true);
    String s = f.toString();
    Assertions.assertTrue(s.contains("\"key\""));
    Assertions.assertTrue(s.contains("\"value\""));
  }

  // ==================== AttBiConsumer ====================

  @Test
  void testBiConsumer_Accept() {
    AtomicInteger sum = new AtomicInteger(0);
    AttBiConsumer<String, Integer, Integer> c = AttBiConsumer.of("ctx", (a, b) -> sum.set(a + b));
    c.accept(3, 4);
    Assertions.assertEquals(7, sum.get());
  }

  @Test
  void testBiConsumer_Accept_Throws() {
    RuntimeException ex = new RuntimeException("test");
    AttBiConsumer<String, Integer, Integer> c = AttBiConsumer.of("ctx", (a, b) -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> c.accept(1, 2));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testBiConsumer_GetAtt() {
    AttBiConsumer<String, Integer, Integer> c = AttBiConsumer.of("ctx", (a, b) -> {});
    Assertions.assertEquals("ctx", c.getAtt());
  }

  @Test
  void testBiConsumer_GetBiConsumer() {
    BiConsumer<Integer, Integer> delegate = (a, b) -> {};
    AttBiConsumer<String, Integer, Integer> c = AttBiConsumer.of("ctx", delegate);
    Assertions.assertSame(delegate, c.getBiConsumer());
  }

  @Test
  void testBiConsumer_NullAtt() {
    AttBiConsumer<String, Integer, Integer> c = AttBiConsumer.of(null, (a, b) -> {});
    Assertions.assertNull(c.getAtt());
    Assertions.assertTrue(c.toString().contains(", att=null"));
  }

  @Test
  void testBiConsumer_ToString() {
    AttBiConsumer<String, Integer, Integer> c = AttBiConsumer.of("ctx", (a, b) -> {});
    String s = c.toString();
    Assertions.assertTrue(s.contains("AttBiConsumer{biConsumer=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=ctx}"));
  }

  @Test
  void testBiConsumer_ToString_JacksonTrue() {
    AttBiConsumer<String, Integer, Integer> c = AttBiConsumer.of("ctx", (a, b) -> {}, true);
    String s = c.toString();
    Assertions.assertTrue(s.contains("AttBiConsumer{biConsumer=cn.addenda.component.common.test.lambda.att.AttTest$$Lambda"));
    Assertions.assertTrue(s.contains(", att=\"ctx\"}"));
  }
}
