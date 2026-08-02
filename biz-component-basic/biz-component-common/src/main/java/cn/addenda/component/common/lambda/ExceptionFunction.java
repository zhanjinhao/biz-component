package cn.addenda.component.common.lambda;

/**
 * @author addenda
 * @since 2023/10/5 23:29
 */
@FunctionalInterface
public interface ExceptionFunction<T, R> {

  R apply(T t) throws Exception;

}
