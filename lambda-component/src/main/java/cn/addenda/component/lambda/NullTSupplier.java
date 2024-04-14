package cn.addenda.component.lambda;

public class NullTSupplier<T> implements TSupplier<T> {

  private final TRunnable tRunnable;

  public NullTSupplier(TRunnable tRunnable) {
    this.tRunnable = tRunnable;
  }

  @Override
  public T get() throws Throwable {
    tRunnable.run();
    return null;
  }

}
