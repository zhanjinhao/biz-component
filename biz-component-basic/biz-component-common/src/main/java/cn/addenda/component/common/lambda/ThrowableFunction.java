package cn.addenda.component.common.lambda;

/**
 * @author addenda
 * @since 2023/6/4 14:58
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> {

  R apply(T t) throws Throwable;

}
