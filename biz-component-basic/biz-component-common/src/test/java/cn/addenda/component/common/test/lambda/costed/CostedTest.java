package cn.addenda.component.common.test.lambda.costed;

import cn.addenda.component.common.lambda.costed.AbstractCostedFunction;
import cn.addenda.component.common.lambda.costed.CostedBiConsumer;
import cn.addenda.component.common.lambda.costed.CostedBiFunction;
import cn.addenda.component.common.lambda.costed.CostedCallable;
import cn.addenda.component.common.lambda.costed.CostedConsumer;
import cn.addenda.component.common.lambda.costed.CostedFunction;
import cn.addenda.component.common.lambda.costed.CostedRunnable;
import cn.addenda.component.common.lambda.costed.CostedSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class CostedTest {

  private static final Long LOW_THRESHOLD = 0L;
  private static final Long HIGH_THRESHOLD = 60_000L;

  // ==================== CostedRunnable ====================

  @Test
  void testRunnable_Run() {
    AtomicBoolean called = new AtomicBoolean(false);
    CostedRunnable costed = CostedRunnable.of(() -> called.set(true));
    costed.run();
    Assertions.assertTrue(called.get());
  }

  @Test
  void testRunnable_Throws() {
    RuntimeException ex = new RuntimeException("test");
    CostedRunnable costed = CostedRunnable.of(() -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, costed::run);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testRunnable_Of_DefaultThreshold() {
    CostedRunnable costed = CostedRunnable.of(() -> {});
    Assertions.assertNotNull(costed);
    Assertions.assertEquals(AbstractCostedFunction.DEFAULT_THRESHOLD, costed.getThreshold());
    Assertions.assertNotNull(costed.getCreateDateTime());
  }

  @Test
  void testRunnable_Of_CustomThreshold() {
    CostedRunnable costed = CostedRunnable.of(HIGH_THRESHOLD, () -> {});
    Assertions.assertEquals(HIGH_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testRunnable_Of_WithExplicitDateTime() {
    LocalDateTime now = LocalDateTime.now();
    CostedRunnable costed = CostedRunnable.of(now, LOW_THRESHOLD, () -> {});
    Assertions.assertEquals(now, costed.getCreateDateTime());
    Assertions.assertEquals(LOW_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testRunnable_ToString() {
    CostedRunnable costed = CostedRunnable.of(HIGH_THRESHOLD, () -> {});
    String s = costed.toString();
    Assertions.assertTrue(s.contains("CostedRunnable"));
    Assertions.assertTrue(s.contains("threshold=" + HIGH_THRESHOLD));
  }

  // ==================== CostedSupplier ====================

  @Test
  void testSupplier_Get_ReturnsValue() {
    CostedSupplier<String> costed = CostedSupplier.of(() -> "hello");
    Assertions.assertEquals("hello", costed.get());
  }

  @Test
  void testSupplier_Get_ReturnsNull() {
    CostedSupplier<Object> costed = CostedSupplier.of(() -> null);
    Object result = costed.get();
    Assertions.assertNull(result);
  }

  @Test
  void testSupplier_Get_Throws() {
    RuntimeException ex = new RuntimeException("test");
    CostedSupplier<Object> costed = CostedSupplier.of(() -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, costed::get);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testSupplier_Get_ExecutesOncePerCall() {
    AtomicInteger counter = new AtomicInteger(0);
    CostedSupplier<Integer> costed = CostedSupplier.of(counter::incrementAndGet);
    Assertions.assertEquals(1, costed.get());
    Assertions.assertEquals(2, costed.get());
    Assertions.assertEquals(3, costed.get());
  }

  @Test
  void testSupplier_Of_DefaultThreshold() {
    CostedSupplier<String> costed = CostedSupplier.of(() -> "x");
    Assertions.assertEquals(AbstractCostedFunction.DEFAULT_THRESHOLD, costed.getThreshold());
    Assertions.assertNotNull(costed.getCreateDateTime());
  }

  @Test
  void testSupplier_Of_CustomThreshold() {
    CostedSupplier<String> costed = CostedSupplier.of(HIGH_THRESHOLD, () -> "x");
    Assertions.assertEquals(HIGH_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testSupplier_Of_WithExplicitDateTime() {
    LocalDateTime now = LocalDateTime.now();
    CostedSupplier<String> costed = CostedSupplier.of(now, LOW_THRESHOLD, () -> "x");
    Assertions.assertEquals(now, costed.getCreateDateTime());
    Assertions.assertEquals(LOW_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testSupplier_ToString() {
    CostedSupplier<String> costed = CostedSupplier.of(HIGH_THRESHOLD, () -> "x");
    String s = costed.toString();
    Assertions.assertTrue(s.contains("CostedSupplier"));
    Assertions.assertTrue(s.contains("threshold=" + HIGH_THRESHOLD));
  }

  // ==================== CostedFunction ====================

  @Test
  void testFunction_Apply_ReturnsValue() {
    CostedFunction<Integer, String> costed = CostedFunction.of(Object::toString);
    Assertions.assertEquals("42", costed.apply(42));
  }

  @Test
  void testFunction_Apply_ReturnsNull() {
    CostedFunction<String, Object> costed = CostedFunction.of(s -> null);
    Assertions.assertNull(costed.apply("x"));
  }

  @Test
  void testFunction_Apply_Throws() {
    RuntimeException ex = new RuntimeException("test");
    CostedFunction<String, String> costed = CostedFunction.of(s -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> costed.apply("x"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testFunction_Apply_CalledEachTime() {
    AtomicInteger counter = new AtomicInteger(0);
    CostedFunction<String, Integer> costed = CostedFunction.of(s -> counter.incrementAndGet());
    Assertions.assertEquals(1, costed.apply("a"));
    Assertions.assertEquals(2, costed.apply("b"));
    Assertions.assertEquals(3, costed.apply("c"));
  }

  @Test
  void testFunction_Of_DefaultThreshold() {
    CostedFunction<String, String> costed = CostedFunction.of(s -> s);
    Assertions.assertEquals(AbstractCostedFunction.DEFAULT_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testFunction_Of_CustomThreshold() {
    CostedFunction<String, String> costed = CostedFunction.of(HIGH_THRESHOLD, s -> s);
    Assertions.assertEquals(HIGH_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testFunction_Of_WithExplicitDateTime() {
    LocalDateTime now = LocalDateTime.now();
    CostedFunction<String, String> costed = CostedFunction.of(now, LOW_THRESHOLD, s -> s);
    Assertions.assertEquals(now, costed.getCreateDateTime());
    Assertions.assertEquals(LOW_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testFunction_ToString() {
    CostedFunction<String, String> costed = CostedFunction.of(HIGH_THRESHOLD, s -> s);
    String s = costed.toString();
    Assertions.assertTrue(s.contains("CostedFunction"));
    Assertions.assertTrue(s.contains("threshold=" + HIGH_THRESHOLD));
  }

  // ==================== CostedCallable ====================

  @Test
  void testCallable_Call_ReturnsValue() throws Exception {
    CostedCallable<String> costed = CostedCallable.of(() -> "hello");
    Assertions.assertEquals("hello", costed.call());
  }

  @Test
  void testCallable_Call_ReturnsNull() throws Exception {
    CostedCallable<Object> costed = CostedCallable.of(() -> null);
    Assertions.assertNull(costed.call());
  }

  @Test
  void testCallable_Call_Throws() {
    Exception ex = new Exception("test");
    CostedCallable<Object> costed = CostedCallable.of(() -> { throw ex; });
    Exception thrown = Assertions.assertThrows(Exception.class, costed::call);
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testCallable_Call_ExecutesEachTime() throws Exception {
    AtomicInteger counter = new AtomicInteger(0);
    CostedCallable<Integer> costed = CostedCallable.of(counter::incrementAndGet);
    Assertions.assertEquals(1, costed.call());
    Assertions.assertEquals(2, costed.call());
    Assertions.assertEquals(3, costed.call());
  }

  @Test
  void testCallable_Of_DefaultThreshold() {
    CostedCallable<String> costed = CostedCallable.of(() -> "x");
    Assertions.assertEquals(AbstractCostedFunction.DEFAULT_THRESHOLD, costed.getThreshold());
    Assertions.assertNotNull(costed.getCreateDateTime());
  }

  @Test
  void testCallable_Of_CustomThreshold() {
    CostedCallable<String> costed = CostedCallable.of(HIGH_THRESHOLD, () -> "x");
    Assertions.assertEquals(HIGH_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testCallable_ToString() {
    CostedCallable<String> costed = CostedCallable.of(HIGH_THRESHOLD, () -> "x");
    String s = costed.toString();
    Assertions.assertTrue(s.contains("CostedCallable"));
    Assertions.assertTrue(s.contains("threshold=" + HIGH_THRESHOLD));
  }

  // ==================== CostedConsumer ====================

  @Test
  void testConsumer_Accept() {
    AtomicBoolean called = new AtomicBoolean(false);
    CostedConsumer<String> costed = CostedConsumer.of(s -> called.set(true));
    costed.accept("test");
    Assertions.assertTrue(called.get());
  }

  @Test
  void testConsumer_Accept_Throws() {
    RuntimeException ex = new RuntimeException("test");
    CostedConsumer<String> costed = CostedConsumer.of(s -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> costed.accept("x"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testConsumer_Of_DefaultThreshold() {
    CostedConsumer<String> costed = CostedConsumer.of(s -> {});
    Assertions.assertEquals(AbstractCostedFunction.DEFAULT_THRESHOLD, costed.getThreshold());
    Assertions.assertNotNull(costed.getCreateDateTime());
  }

  @Test
  void testConsumer_Of_CustomThreshold() {
    CostedConsumer<String> costed = CostedConsumer.of(HIGH_THRESHOLD, s -> {});
    Assertions.assertEquals(HIGH_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testConsumer_ToString() {
    CostedConsumer<String> costed = CostedConsumer.of(HIGH_THRESHOLD, s -> {});
    String s = costed.toString();
    Assertions.assertTrue(s.contains("CostedConsumer"));
    Assertions.assertTrue(s.contains("threshold=" + HIGH_THRESHOLD));
  }

  // ==================== CostedBiFunction ====================

  @Test
  void testBiFunction_Apply_ReturnsValue() {
    CostedBiFunction<Integer, Integer, Integer> costed = CostedBiFunction.of(Integer::sum);
    Assertions.assertEquals(7, costed.apply(3, 4));
  }

  @Test
  void testBiFunction_Apply_ReturnsNull() {
    CostedBiFunction<String, String, Object> costed = CostedBiFunction.of((a, b) -> null);
    Assertions.assertNull(costed.apply("a", "b"));
  }

  @Test
  void testBiFunction_Apply_Throws() {
    RuntimeException ex = new RuntimeException("test");
    CostedBiFunction<String, String, String> costed = CostedBiFunction.of((a, b) -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> costed.apply("a", "b"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testBiFunction_Of_DefaultThreshold() {
    CostedBiFunction<String, String, String> costed = CostedBiFunction.of((a, b) -> a + b);
    Assertions.assertEquals(AbstractCostedFunction.DEFAULT_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testBiFunction_Of_CustomThreshold() {
    CostedBiFunction<String, String, String> costed = CostedBiFunction.of(HIGH_THRESHOLD, (a, b) -> a + b);
    Assertions.assertEquals(HIGH_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testBiFunction_ToString() {
    CostedBiFunction<String, String, String> costed = CostedBiFunction.of(HIGH_THRESHOLD, (a, b) -> a + b);
    String s = costed.toString();
    Assertions.assertTrue(s.contains("CostedBiFunction"));
    Assertions.assertTrue(s.contains("threshold=" + HIGH_THRESHOLD));
  }

  // ==================== CostedBiConsumer ====================

  @Test
  void testBiConsumer_Accept() {
    AtomicInteger sum = new AtomicInteger(0);
    CostedBiConsumer<Integer, Integer> costed = CostedBiConsumer.of((a, b) -> sum.set(a + b));
    costed.accept(3, 4);
    Assertions.assertEquals(7, sum.get());
  }

  @Test
  void testBiConsumer_Accept_Throws() {
    RuntimeException ex = new RuntimeException("test");
    CostedBiConsumer<String, String> costed = CostedBiConsumer.of((a, b) -> { throw ex; });
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> costed.accept("a", "b"));
    Assertions.assertSame(ex, thrown);
  }

  @Test
  void testBiConsumer_Of_DefaultThreshold() {
    CostedBiConsumer<String, String> costed = CostedBiConsumer.of((a, b) -> {});
    Assertions.assertEquals(AbstractCostedFunction.DEFAULT_THRESHOLD, costed.getThreshold());
    Assertions.assertNotNull(costed.getCreateDateTime());
  }

  @Test
  void testBiConsumer_Of_CustomThreshold() {
    CostedBiConsumer<String, String> costed = CostedBiConsumer.of(HIGH_THRESHOLD, (a, b) -> {});
    Assertions.assertEquals(HIGH_THRESHOLD, costed.getThreshold());
  }

  @Test
  void testBiConsumer_ToString() {
    CostedBiConsumer<String, String> costed = CostedBiConsumer.of(HIGH_THRESHOLD, (a, b) -> {});
    String s = costed.toString();
    Assertions.assertTrue(s.contains("CostedBiConsumer"));
    Assertions.assertTrue(s.contains("threshold=" + HIGH_THRESHOLD));
  }
}
