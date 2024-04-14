package cn.addenda.component.lambda;

public class NullTBiFunction<T, U, R> implements TBiFunction<T, U, R> {

  private final TBiConsumer<T, U> consumer;

  public NullTBiFunction(TBiConsumer<T, U> consumer) {
    this.consumer = consumer;
  }

  @Override
  public R apply(T t, U u) throws Throwable {
    consumer.accept(t, u);
    return null;
  }
}
