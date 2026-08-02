package cn.addenda.component.common.lambda;

@FunctionalInterface
public interface ExceptionBiConsumer<T, U> {

  void accept(T t, U u) throws Exception;

}
