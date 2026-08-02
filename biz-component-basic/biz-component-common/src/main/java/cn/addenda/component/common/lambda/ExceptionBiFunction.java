package cn.addenda.component.common.lambda;

@FunctionalInterface
public interface ExceptionBiFunction<T, U, R> {

  R apply(T t, U u) throws Exception;

}
