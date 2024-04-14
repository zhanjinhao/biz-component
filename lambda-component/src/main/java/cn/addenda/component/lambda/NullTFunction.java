package cn.addenda.component.lambda;

public class NullTFunction<T, R> implements TFunction<T, R> {

  private final TConsumer<T> consumer;

  public NullTFunction(TConsumer<T> consumer) {
    this.consumer = consumer;
  }

  @Override
  public R apply(T o) throws Throwable {
    consumer.accept(o);
    return null;
  }
}
