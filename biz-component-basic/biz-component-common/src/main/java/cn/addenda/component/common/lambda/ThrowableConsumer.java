package cn.addenda.component.common.lambda;

/**
 * @author addenda
 * @since 2023/6/4 15:10
 */
@FunctionalInterface
public interface ThrowableConsumer<T> {

  void accept(T t) throws Throwable;

}
