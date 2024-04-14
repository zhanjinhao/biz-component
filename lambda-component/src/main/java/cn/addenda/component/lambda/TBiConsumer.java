package cn.addenda.component.lambda;

@FunctionalInterface
public interface TBiConsumer<T, U> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t the first input argument
   * @param u the second input argument
   */
  void accept(T t, U u) throws Throwable;

  default <R> TBiFunction<T, U, R> toBiTFunction() {
    return new TBiFunction<T, U, R>() {
      @Override
      public R apply(T t, U u) throws Throwable {
        accept(t, u);
        return null;
      }
    };
  }
}
