package cn.addenda.component.common.test.lambda.named;

import cn.addenda.component.common.lambda.named.NamedBiConsumer;
import cn.addenda.component.common.lambda.named.NamedBiFunction;
import cn.addenda.component.common.lambda.named.NamedCallable;
import cn.addenda.component.common.lambda.named.NamedConsumer;
import cn.addenda.component.common.lambda.named.NamedFunction;
import cn.addenda.component.common.lambda.named.NamedRunnable;
import cn.addenda.component.common.lambda.named.NamedSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

  // ==================== NamedConsumer ====================

  @Test
  void testConsumer_Accept() {
    AtomicBoolean called = new AtomicBoolean(false);
    NamedConsumer<String> c = NamedConsumer.of("test", s -> called.set(true));
    c.accept("hello");
    Assertions.assertTrue(called.get());
  }

  @Test
  void testConsumer_BiConsumerConstructor() {
    BiConsumer<String, String> bc = (name, val) -> {};
    NamedConsumer<String> c = NamedConsumer.of("test", bc);
    Assertions.assertNotNull(c);
  }

  @Test
  void testConsumer_AutoName() {
    NamedConsumer<String> c = NamedConsumer.of(s -> {});
    String name = c.getName();
    Assertions.assertNotNull(name);
    Assertions.assertFalse(name.isEmpty());
  }

  @Test
  void testConsumer_ToString() {
    NamedConsumer<String> c = NamedConsumer.of("test", s -> {});
    String s = c.toString();
    Assertions.assertTrue(s.contains("NamedConsumer{consumer=cn.addenda.component.common.test.lambda.named.NamedTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", name=test}"));
  }

  @Test
  void testConsumer_GetConsumer() {
    Consumer<String> delegate = s -> {};
    NamedConsumer<String> c = NamedConsumer.of("test", delegate);
    Assertions.assertSame(delegate, c.getConsumer());
  }

  // ==================== NamedBiFunction ====================

  @Test
  void testBiFunction_Apply_ReturnsValue() {
    NamedBiFunction<Integer, Integer, Integer> f = NamedBiFunction.of("test", Integer::sum);
    Assertions.assertEquals(7, f.apply(3, 4));
  }

  @Test
  void testBiFunction_AutoName() {
    NamedBiFunction<Integer, Integer, Integer> f = NamedBiFunction.of(Integer::sum);
    String name = f.getName();
    Assertions.assertNotNull(name);
    Assertions.assertFalse(name.isEmpty());
  }

  @Test
  void testBiFunction_GetBiFunction() {
    BiFunction<Integer, Integer, Integer> delegate = Integer::sum;
    NamedBiFunction<Integer, Integer, Integer> f = NamedBiFunction.of("test", delegate);
    Assertions.assertSame(delegate, f.getBiFunction());
  }

  @Test
  void testBiFunction_ToString() {
    NamedBiFunction<Integer, Integer, Integer> f = NamedBiFunction.of("test", Integer::sum);
    String s = f.toString();
    Assertions.assertTrue(s.contains("NamedBiFunction{biFunction=cn.addenda.component.common.test.lambda.named.NamedTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", name=test}"));
  }

  // ==================== NamedBiConsumer ====================

  @Test
  void testBiConsumer_Accept() {
    AtomicInteger sum = new AtomicInteger(0);
    NamedBiConsumer<Integer, Integer> c = NamedBiConsumer.of("test", (a, b) -> sum.set(a + b));
    c.accept(3, 4);
    Assertions.assertEquals(7, sum.get());
  }

  @Test
  void testBiConsumer_AutoName() {
    NamedBiConsumer<Integer, Integer> c = NamedBiConsumer.of((a, b) -> {});
    String name = c.getName();
    Assertions.assertNotNull(name);
    Assertions.assertFalse(name.isEmpty());
  }

  @Test
  void testBiConsumer_GetBiConsumer() {
    BiConsumer<Integer, Integer> delegate = (a, b) -> {};
    NamedBiConsumer<Integer, Integer> c = NamedBiConsumer.of("test", delegate);
    Assertions.assertSame(delegate, c.getBiConsumer());
  }

  @Test
  void testBiConsumer_ToString() {
    NamedBiConsumer<Integer, Integer> c = NamedBiConsumer.of("test", (a, b) -> {});
    String s = c.toString();
    Assertions.assertTrue(s.contains("NamedBiConsumer{biConsumer=cn.addenda.component.common.test.lambda.named.NamedTest$$Lambda$"));
    Assertions.assertTrue(s.contains(", name=test}"));
  }
}
