package cn.addenda.component.common.test.lambda.named;

import cn.addenda.component.common.lambda.named.NamedCallable;
import cn.addenda.component.common.lambda.named.NamedFunction;
import cn.addenda.component.common.lambda.named.NamedRunnable;
import cn.addenda.component.common.lambda.named.NamedSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

class NamedTest {

  // ==================== NamedRunnable ====================

  @Test
  void testRunnable_Run() {
    AtomicBoolean called = new AtomicBoolean(false);
    NamedRunnable r = NamedRunnable.of("test", () -> called.set(true));
    r.run();
    Assertions.assertTrue(called.get());
  }

  @Test
  void testRunnable_ConsumerConstructor() {
    AtomicBoolean called = new AtomicBoolean(false);
    Consumer<String> consumer = name -> called.set(true);
    NamedRunnable r = NamedRunnable.of("test", consumer);
    r.run();
    Assertions.assertTrue(called.get());
  }

  @Test
  void testRunnable_AutoName() {
    NamedRunnable r = NamedRunnable.of(() -> {
    });
    Assertions.assertTrue(r.getName().contains("testRunnable_AutoName"));
  }

  @Test
  void testRunnable_ToString() {
    NamedRunnable r = NamedRunnable.of("test", () -> {
    });
    String s = r.toString();
    Assertions.assertTrue(s.contains("NamedRunnable{runnable=cn.addenda.component.common.test.lambda.named.NamedTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", name=test}"));
  }

  // ==================== NamedSupplier ====================

  @Test
  void testSupplier_Get_ReturnsValue() {
    NamedSupplier<Integer> s = NamedSupplier.of("test", () -> 42);
    Assertions.assertEquals(42, s.get());
  }

  @Test
  void testSupplier_FunctionConstructor() {
    Function<String, Integer> fn = name -> 99;
    NamedSupplier<Integer> s = NamedSupplier.of("test", fn);
    Assertions.assertEquals(99, s.get());
  }

  @Test
  void testSupplier_AutoName() {
    NamedSupplier<Integer> s = NamedSupplier.of(() -> 42);
    Assertions.assertTrue(s.getName().contains("testSupplier_AutoName"));
  }

  @Test
  void testSupplier_ToString() {
    NamedSupplier<Integer> supplier = NamedSupplier.of("test", () -> 42);
    String s = supplier.toString();
    Assertions.assertTrue(s.contains("NamedSupplier{supplier=cn.addenda.component.common.test.lambda.named.NamedTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", name=test}"));
  }

  // ==================== NamedFunction ====================

  @Test
  void testFunction_Apply_ReturnsValue() {
    NamedFunction<Integer, String> f = NamedFunction.of("test", Object::toString);
    Assertions.assertEquals("42", f.apply(42));
  }

  @Test
  void testFunction_AutoName() {
    NamedFunction<Integer, String> f = NamedFunction.of(Object::toString);
    Assertions.assertTrue(f.getName().contains("testFunction_AutoName"));
  }

  @Test
  void testFunction_ToString() {
    NamedFunction<Integer, String> f = NamedFunction.of("test", Object::toString);
    String s = f.toString();
    Assertions.assertTrue(s.contains("NamedFunction{function=cn.addenda.component.common.test.lambda.named.NamedTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", name=test}"));
  }

  // ==================== NamedCallable ====================

  @Test
  void testCallable_Call_ReturnsValue() throws Exception {
    NamedCallable<Integer> c = NamedCallable.of("test", () -> 42);
    Assertions.assertEquals(42, c.call());
  }

  @Test
  void testCallable_Call_ReturnsNull() throws Exception {
    NamedCallable<Object> c = NamedCallable.of("test", () -> null);
    Assertions.assertNull(c.call());
  }

  @Test
  void testCallable_Call_Throws() {
    Exception ex = new Exception("test");
    NamedCallable<Object> c = NamedCallable.of("test", () -> {
      throw ex;
    });
    Exception thrown = Assertions.assertThrows(Exception.class, c::call);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testCallable_Call_ExecutesEachTime() throws Exception {
    AtomicInteger counter = new AtomicInteger(0);
    NamedCallable<Integer> c = NamedCallable.of("test", counter::incrementAndGet);
    Assertions.assertEquals(1, c.call());
    Assertions.assertEquals(2, c.call());
    Assertions.assertEquals(3, c.call());
  }

  @Test
  void testCallable_FunctionConstructor() throws Exception {
    Function<String, Integer> fn = name -> 99;
    NamedCallable<Integer> c = NamedCallable.of("test", fn);
    Assertions.assertEquals(99, c.call());
  }

  @Test
  void testCallable_AutoName() throws Exception {
    NamedCallable<Integer> c = NamedCallable.of(() -> 42);
    String name = c.getName();
    Assertions.assertTrue(name.contains("testCallable_AutoName"));
  }

  @Test
  void testCallable_ToString() {
    NamedCallable<Integer> c = NamedCallable.of("test", () -> 42);
    String s = c.toString();
    Assertions.assertTrue(s.contains("NamedCallable{callable=cn.addenda.component.common.test.lambda.named.NamedTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", name=test}"));
  }
}
