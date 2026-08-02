package cn.addenda.component.common.lambda;

@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> {

  R apply(T t, U u) throws Throwable;

}
