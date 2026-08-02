package cn.addenda.component.common.lambda;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author addenda
 * @since 2023/6/4 15:01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionConverter {

  // ==================== XXX → ThrowableXXX ====================

  public static <T, R> ThrowableFunction<T, R> toThrowableFunction(Function<T, R> function) {
    return new FunctionAsThrowableFunction<>(function);
  }

  public static <T, R> ThrowableFunction<T, R> toThrowableFunction(ExceptionFunction<T, R> exceptionFunction) {
    return new ExceptionFunctionAsThrowableFunction<>(exceptionFunction);
  }

  public static <T> ThrowableConsumer<T> toThrowableConsumer(Consumer<T> consumer) {
    return new ConsumerAsThrowableConsumer<>(consumer);
  }

  public static <T> ThrowableConsumer<T> toThrowableConsumer(ExceptionConsumer<T> exceptionConsumer) {
    return new ExceptionConsumerAsThrowableConsumer<>(exceptionConsumer);
  }

  public static <T, U, R> ThrowableBiFunction<T, U, R> toThrowableBiFunction(BiFunction<T, U, R> biFunction) {
    return new BiFunctionAsThrowableBiFunction<>(biFunction);
  }

  public static <T, U, R> ThrowableBiFunction<T, U, R> toThrowableBiFunction(ExceptionBiFunction<T, U, R> exceptionBiFunction) {
    return new ExceptionBiFunctionAsThrowableBiFunction<>(exceptionBiFunction);
  }

  public static <T, U> ThrowableBiConsumer<T, U> toThrowableBiConsumer(BiConsumer<T, U> biConsumer) {
    return new BiConsumerAsThrowableBiConsumer<>(biConsumer);
  }

  public static <T, U> ThrowableBiConsumer<T, U> toThrowableBiConsumer(ExceptionBiConsumer<T, U> exceptionBiConsumer) {
    return new ExceptionBiConsumerAsThrowableBiConsumer<>(exceptionBiConsumer);
  }

  public static <T> ThrowableSupplier<T> toThrowableSupplier(Supplier<T> supplier) {
    return new SupplierAsThrowableSupplier<>(supplier);
  }

  public static <T> ThrowableSupplier<T> toThrowableSupplier(ExceptionSupplier<T> exceptionSupplier) {
    return new ExceptionSupplierAsThrowableSupplier<>(exceptionSupplier);
  }

  public static ThrowableRunnable toThrowableRunnable(Runnable runnable) {
    return new RunnableAsThrowableRunnable(runnable);
  }

  public static ThrowableRunnable toThrowableRunnable(ExceptionRunnable exceptionRunnable) {
    return new ExceptionRunnableAsThrowableRunnable(exceptionRunnable);
  }

  // ==================== XXX → ExceptionXXX ====================

  public static <T, R> ExceptionFunction<T, R> toExceptionFunction(Function<T, R> function) {
    return new FunctionAsExceptionFunction<>(function);
  }

  public static <T> ExceptionConsumer<T> toExceptionConsumer(Consumer<T> consumer) {
    return new ConsumerAsExceptionConsumer<>(consumer);
  }

  public static <T, U, R> ExceptionBiFunction<T, U, R> toExceptionBiFunction(BiFunction<T, U, R> biFunction) {
    return new BiFunctionAsExceptionBiFunction<>(biFunction);
  }

  public static <T, U> ExceptionBiConsumer<T, U> toExceptionBiConsumer(BiConsumer<T, U> biConsumer) {
    return new BiConsumerAsExceptionBiConsumer<>(biConsumer);
  }

  public static <T> ExceptionSupplier<T> toExceptionSupplier(Supplier<T> supplier) {
    return new SupplierAsExceptionSupplier<>(supplier);
  }

  public static ExceptionRunnable toExceptionRunnable(Runnable runnable) {
    return new RunnableAsExceptionRunnable(runnable);
  }

  // ==================== Consumer / Runnable → Null 返回 ====================

  public static <T, R> Function<T, R> toNullFunction(Consumer<T> consumer) {
    return new NullFunction<>(consumer);
  }

  public static <T, U, R> BiFunction<T, U, R> toNullBiFunction(BiConsumer<T, U> biConsumer) {
    return new NullBiFunction<>(biConsumer);
  }

  public static <T, R> ThrowableFunction<T, R> toNullThrowableFunction(ThrowableConsumer<T> throwableConsumer) {
    return new NullThrowableFunction<>(throwableConsumer);
  }

  public static <T, U, R> ThrowableBiFunction<T, U, R> toNullThrowableBiFunction(ThrowableBiConsumer<T, U> throwableBiConsumer) {
    return new NullThrowableBiFunction<>(throwableBiConsumer);
  }

  public static <T, R> ExceptionFunction<T, R> toNullExceptionFunction(ExceptionConsumer<T> exceptionConsumer) {
    return new NullExceptionFunction<>(exceptionConsumer);
  }

  public static <T, U, R> ExceptionBiFunction<T, U, R> toNullExceptionBiFunction(ExceptionBiConsumer<T, U> exceptionBiConsumer) {
    return new NullExceptionBiFunction<>(exceptionBiConsumer);
  }

  public static <T> Supplier<T> toNullSupplier(Runnable runnable) {
    return new NullSupplier<>(runnable);
  }

  public static <T> ThrowableSupplier<T> toNullThrowableSupplier(ThrowableRunnable throwableRunnable) {
    return new NullThrowableSupplier<>(throwableRunnable);
  }

  public static <T> ExceptionSupplier<T> toNullExceptionSupplier(ExceptionRunnable exceptionRunnable) {
    return new NullExceptionSupplier<>(exceptionRunnable);
  }

  // ==================== Wrapper 内部类：XXX → ThrowableXXX ====================

  public static class FunctionAsThrowableFunction<T, R> implements ThrowableFunction<T, R> {

    private final Function<T, R> function;

    FunctionAsThrowableFunction(Function<T, R> function) {
      this.function = function;
    }

    @Override
    public String toString() {
      return "FunctionAsThrowableFunction{function=" + function + '}';
    }

    @Override
    public R apply(T t) throws Throwable {
      return function.apply(t);
    }

  }

  public static class ExceptionFunctionAsThrowableFunction<T, R> implements ThrowableFunction<T, R> {

    private final ExceptionFunction<T, R> exceptionFunction;

    ExceptionFunctionAsThrowableFunction(ExceptionFunction<T, R> exceptionFunction) {
      this.exceptionFunction = exceptionFunction;
    }

    @Override
    public String toString() {
      return "ExceptionFunctionAsThrowableFunction{exceptionFunction=" + exceptionFunction + '}';
    }

    @Override
    public R apply(T t) throws Throwable {
      return exceptionFunction.apply(t);
    }

  }

  public static class ConsumerAsThrowableConsumer<T> implements ThrowableConsumer<T> {

    private final Consumer<T> consumer;

    ConsumerAsThrowableConsumer(Consumer<T> consumer) {
      this.consumer = consumer;
    }

    @Override
    public String toString() {
      return "ConsumerAsThrowableConsumer{consumer=" + consumer + '}';
    }

    @Override
    public void accept(T t) throws Throwable {
      consumer.accept(t);
    }

  }

  public static class ExceptionConsumerAsThrowableConsumer<T> implements ThrowableConsumer<T> {

    private final ExceptionConsumer<T> exceptionConsumer;

    ExceptionConsumerAsThrowableConsumer(ExceptionConsumer<T> exceptionConsumer) {
      this.exceptionConsumer = exceptionConsumer;
    }

    @Override
    public String toString() {
      return "ExceptionConsumerAsThrowableConsumer{exceptionConsumer=" + exceptionConsumer + '}';
    }

    @Override
    public void accept(T t) throws Throwable {
      exceptionConsumer.accept(t);
    }

  }

  public static class BiFunctionAsThrowableBiFunction<T, U, R> implements ThrowableBiFunction<T, U, R> {

    private final BiFunction<T, U, R> biFunction;

    BiFunctionAsThrowableBiFunction(BiFunction<T, U, R> biFunction) {
      this.biFunction = biFunction;
    }

    @Override
    public String toString() {
      return "BiFunctionAsThrowableBiFunction{biFunction=" + biFunction + '}';
    }

    @Override
    public R apply(T t, U u) throws Throwable {
      return biFunction.apply(t, u);
    }

  }

  public static class ExceptionBiFunctionAsThrowableBiFunction<T, U, R> implements ThrowableBiFunction<T, U, R> {

    private final ExceptionBiFunction<T, U, R> exceptionBiFunction;

    ExceptionBiFunctionAsThrowableBiFunction(ExceptionBiFunction<T, U, R> exceptionBiFunction) {
      this.exceptionBiFunction = exceptionBiFunction;
    }

    @Override
    public String toString() {
      return "ExceptionBiFunctionAsThrowableBiFunction{exceptionBiFunction=" + exceptionBiFunction + '}';
    }

    @Override
    public R apply(T t, U u) throws Throwable {
      return exceptionBiFunction.apply(t, u);
    }

  }

  public static class BiConsumerAsThrowableBiConsumer<T, U> implements ThrowableBiConsumer<T, U> {

    private final BiConsumer<T, U> biConsumer;

    BiConsumerAsThrowableBiConsumer(BiConsumer<T, U> biConsumer) {
      this.biConsumer = biConsumer;
    }

    @Override
    public String toString() {
      return "BiConsumerAsThrowableBiConsumer{biConsumer=" + biConsumer + '}';
    }

    @Override
    public void accept(T t, U u) throws Throwable {
      biConsumer.accept(t, u);
    }

  }

  public static class ExceptionBiConsumerAsThrowableBiConsumer<T, U> implements ThrowableBiConsumer<T, U> {

    private final ExceptionBiConsumer<T, U> exceptionBiConsumer;

    ExceptionBiConsumerAsThrowableBiConsumer(ExceptionBiConsumer<T, U> exceptionBiConsumer) {
      this.exceptionBiConsumer = exceptionBiConsumer;
    }

    @Override
    public String toString() {
      return "ExceptionBiConsumerAsThrowableBiConsumer{exceptionBiConsumer=" + exceptionBiConsumer + '}';
    }

    @Override
    public void accept(T t, U u) throws Throwable {
      exceptionBiConsumer.accept(t, u);
    }

  }

  public static class SupplierAsThrowableSupplier<T> implements ThrowableSupplier<T> {

    private final Supplier<T> supplier;

    SupplierAsThrowableSupplier(Supplier<T> supplier) {
      this.supplier = supplier;
    }

    @Override
    public String toString() {
      return "SupplierAsThrowableSupplier{supplier=" + supplier + '}';
    }

    @Override
    public T get() throws Throwable {
      return supplier.get();
    }

  }

  public static class ExceptionSupplierAsThrowableSupplier<T> implements ThrowableSupplier<T> {

    private final ExceptionSupplier<T> exceptionSupplier;

    ExceptionSupplierAsThrowableSupplier(ExceptionSupplier<T> exceptionSupplier) {
      this.exceptionSupplier = exceptionSupplier;
    }

    @Override
    public String toString() {
      return "ExceptionSupplierAsThrowableSupplier{exceptionSupplier=" + exceptionSupplier + '}';
    }

    @Override
    public T get() throws Throwable {
      return exceptionSupplier.get();
    }

  }

  public static class RunnableAsThrowableRunnable implements ThrowableRunnable {

    private final Runnable runnable;

    RunnableAsThrowableRunnable(Runnable runnable) {
      this.runnable = runnable;
    }

    @Override
    public String toString() {
      return "RunnableAsThrowableRunnable{runnable=" + runnable + '}';
    }

    @Override
    public void run() throws Throwable {
      runnable.run();
    }

  }

  public static class ExceptionRunnableAsThrowableRunnable implements ThrowableRunnable {

    private final ExceptionRunnable exceptionRunnable;

    ExceptionRunnableAsThrowableRunnable(ExceptionRunnable exceptionRunnable) {
      this.exceptionRunnable = exceptionRunnable;
    }

    @Override
    public String toString() {
      return "ExceptionRunnableAsThrowableRunnable{exceptionRunnable=" + exceptionRunnable + '}';
    }

    @Override
    public void run() throws Throwable {
      exceptionRunnable.run();
    }

  }

  // ==================== Wrapper 内部类：XXX → ExceptionXXX ====================

  public static class FunctionAsExceptionFunction<T, R> implements ExceptionFunction<T, R> {

    private final Function<T, R> function;

    FunctionAsExceptionFunction(Function<T, R> function) {
      this.function = function;
    }

    @Override
    public String toString() {
      return "FunctionAsExceptionFunction{function=" + function + '}';
    }

    @Override
    public R apply(T t) throws Exception {
      return function.apply(t);
    }

  }

  public static class ConsumerAsExceptionConsumer<T> implements ExceptionConsumer<T> {

    private final Consumer<T> consumer;

    ConsumerAsExceptionConsumer(Consumer<T> consumer) {
      this.consumer = consumer;
    }

    @Override
    public String toString() {
      return "ConsumerAsExceptionConsumer{consumer=" + consumer + '}';
    }

    @Override
    public void accept(T t) throws Exception {
      consumer.accept(t);
    }

  }

  public static class BiFunctionAsExceptionBiFunction<T, U, R> implements ExceptionBiFunction<T, U, R> {

    private final BiFunction<T, U, R> biFunction;

    BiFunctionAsExceptionBiFunction(BiFunction<T, U, R> biFunction) {
      this.biFunction = biFunction;
    }

    @Override
    public String toString() {
      return "BiFunctionAsExceptionBiFunction{biFunction=" + biFunction + '}';
    }

    @Override
    public R apply(T t, U u) throws Exception {
      return biFunction.apply(t, u);
    }

  }

  public static class BiConsumerAsExceptionBiConsumer<T, U> implements ExceptionBiConsumer<T, U> {

    private final BiConsumer<T, U> biConsumer;

    BiConsumerAsExceptionBiConsumer(BiConsumer<T, U> biConsumer) {
      this.biConsumer = biConsumer;
    }

    @Override
    public String toString() {
      return "BiConsumerAsExceptionBiConsumer{biConsumer=" + biConsumer + '}';
    }

    @Override
    public void accept(T t, U u) throws Exception {
      biConsumer.accept(t, u);
    }

  }

  public static class SupplierAsExceptionSupplier<T> implements ExceptionSupplier<T> {

    private final Supplier<T> supplier;

    SupplierAsExceptionSupplier(Supplier<T> supplier) {
      this.supplier = supplier;
    }

    @Override
    public String toString() {
      return "SupplierAsExceptionSupplier{supplier=" + supplier + '}';
    }

    @Override
    public T get() throws Exception {
      return supplier.get();
    }

  }

  public static class RunnableAsExceptionRunnable implements ExceptionRunnable {

    private final Runnable runnable;

    RunnableAsExceptionRunnable(Runnable runnable) {
      this.runnable = runnable;
    }

    @Override
    public String toString() {
      return "RunnableAsExceptionRunnable{runnable=" + runnable + '}';
    }

    @Override
    public void run() throws Exception {
      runnable.run();
    }

  }

  // ==================== Null 内部类 ====================

  public static class NullFunction<T, R> implements Function<T, R> {

    private final Consumer<T> consumer;

    NullFunction(Consumer<T> consumer) {
      this.consumer = consumer;
    }

    @Override
    public String toString() {
      return "NullFunction{consumer=" + consumer + '}';
    }

    @Override
    public R apply(T t) {
      consumer.accept(t);
      return null;
    }

  }

  public static class NullBiFunction<T, U, R> implements BiFunction<T, U, R> {

    private final BiConsumer<T, U> biConsumer;

    NullBiFunction(BiConsumer<T, U> biConsumer) {
      this.biConsumer = biConsumer;
    }

    @Override
    public String toString() {
      return "NullBiFunction{biConsumer=" + biConsumer + '}';
    }

    @Override
    public R apply(T t, U u) {
      biConsumer.accept(t, u);
      return null;
    }

  }

  public static class NullThrowableFunction<T, R> implements ThrowableFunction<T, R> {

    private final ThrowableConsumer<T> throwableConsumer;

    NullThrowableFunction(ThrowableConsumer<T> throwableConsumer) {
      this.throwableConsumer = throwableConsumer;
    }

    @Override
    public String toString() {
      return "NullThrowableFunction{throwableConsumer=" + throwableConsumer + '}';
    }

    @Override
    public R apply(T o) throws Throwable {
      throwableConsumer.accept(o);
      return null;
    }

  }

  public static class NullThrowableBiFunction<T, U, R> implements ThrowableBiFunction<T, U, R> {

    private final ThrowableBiConsumer<T, U> throwableBiConsumer;

    NullThrowableBiFunction(ThrowableBiConsumer<T, U> throwableBiConsumer) {
      this.throwableBiConsumer = throwableBiConsumer;
    }

    @Override
    public String toString() {
      return "NullThrowableBiFunction{throwableBiConsumer=" + throwableBiConsumer + '}';
    }

    @Override
    public R apply(T t, U u) throws Throwable {
      throwableBiConsumer.accept(t, u);
      return null;
    }

  }

  public static class NullExceptionFunction<T, R> implements ExceptionFunction<T, R> {

    private final ExceptionConsumer<T> exceptionConsumer;

    NullExceptionFunction(ExceptionConsumer<T> exceptionConsumer) {
      this.exceptionConsumer = exceptionConsumer;
    }

    @Override
    public String toString() {
      return "NullExceptionFunction{exceptionConsumer=" + exceptionConsumer + '}';
    }

    @Override
    public R apply(T o) throws Exception {
      exceptionConsumer.accept(o);
      return null;
    }

  }

  public static class NullExceptionBiFunction<T, U, R> implements ExceptionBiFunction<T, U, R> {

    private final ExceptionBiConsumer<T, U> exceptionBiConsumer;

    NullExceptionBiFunction(ExceptionBiConsumer<T, U> exceptionBiConsumer) {
      this.exceptionBiConsumer = exceptionBiConsumer;
    }

    @Override
    public String toString() {
      return "NullExceptionBiFunction{exceptionBiConsumer=" + exceptionBiConsumer + '}';
    }

    @Override
    public R apply(T t, U u) throws Exception {
      exceptionBiConsumer.accept(t, u);
      return null;
    }

  }

  public static class NullSupplier<T> implements Supplier<T> {

    private final Runnable runnable;

    NullSupplier(Runnable runnable) {
      this.runnable = runnable;
    }

    @Override
    public String toString() {
      return "NullSupplier{runnable=" + runnable + '}';
    }

    @Override
    public T get() {
      runnable.run();
      return null;
    }

  }

  public static class NullThrowableSupplier<T> implements ThrowableSupplier<T> {

    private final ThrowableRunnable throwableRunnable;

    NullThrowableSupplier(ThrowableRunnable throwableRunnable) {
      this.throwableRunnable = throwableRunnable;
    }

    @Override
    public String toString() {
      return "NullThrowableSupplier{throwableRunnable=" + throwableRunnable + '}';
    }

    @Override
    public T get() throws Throwable {
      throwableRunnable.run();
      return null;
    }

  }

  public static class NullExceptionSupplier<T> implements ExceptionSupplier<T> {

    private final ExceptionRunnable exceptionRunnable;

    NullExceptionSupplier(ExceptionRunnable exceptionRunnable) {
      this.exceptionRunnable = exceptionRunnable;
    }

    @Override
    public String toString() {
      return "NullExceptionSupplier{exceptionRunnable=" + exceptionRunnable + '}';
    }

    @Override
    public T get() throws Exception {
      exceptionRunnable.run();
      return null;
    }

  }

}
