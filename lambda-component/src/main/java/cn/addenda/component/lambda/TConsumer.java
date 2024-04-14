package cn.addenda.component.lambda;

/**
 * @author addenda
 * @since 2023/6/4 15:10
 */
@FunctionalInterface
public interface TConsumer<T> {

  void accept(T t) throws Throwable;

  default <R> TFunction<T, R> toTFunction() {
    return new TFunction<T, R>() {
      @Override
      public R apply(T t) throws Throwable {
        accept(t);
        return null;
      }
    };
  }

}
