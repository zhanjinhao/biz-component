package cn.addenda.component.common.lambda;

@FunctionalInterface
public interface ThrowableBiConsumer<T, U> {

  void accept(T t, U u) throws Throwable;

}
