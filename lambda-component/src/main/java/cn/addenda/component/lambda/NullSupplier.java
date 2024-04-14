package cn.addenda.component.lambda;

import java.util.function.Supplier;

public class NullSupplier<T> implements Supplier<T> {

  private final Runnable runnable;

  public NullSupplier(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public T get() {
    runnable.run();
    return null;
  }
}
