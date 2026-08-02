package cn.addenda.component.common.lambda;

/**
 * @author addenda
 * @since 2023/6/4 14:58
 */
@FunctionalInterface
public interface ThrowableSupplier<T> {

  T get() throws Throwable;

}
