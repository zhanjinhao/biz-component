package cn.addenda.component.lambda;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class NullBiFunction<T, U, R> implements BiFunction<T, U, R> {

  private final BiConsumer<T, U> consumer;

  public NullBiFunction(BiConsumer<T, U> consumer) {
    this.consumer = consumer;
  }

  @Override
  public R apply(T t, U u) {
    consumer.accept(t, u);
    return null;
  }
}
