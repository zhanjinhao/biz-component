package cn.addenda.component.common.lambda.named;

import cn.addenda.component.common.AbstractNamed;
import cn.addenda.component.stacktrace.StackTraceUtils;
import lombok.Getter;

import java.util.function.Function;
import java.util.function.Supplier;

public class NamedSupplier<R> extends AbstractNamed implements Supplier<R> {

  @Getter
  private final Supplier<R> supplier;

  private NamedSupplier(String name, Supplier<R> supplier) {
    super(name);
    this.supplier = supplier;
  }

  private NamedSupplier(String name, Function<String, R> function) {
    super(name);
    this.supplier = new Supplier<R>() {
      @Override
      public R get() {
        return function.apply(name);
      }

      @Override
      public String toString() {
        return function.toString();
      }
    };
  }

  public static <R> NamedSupplier<R> of(String name, Supplier<R> supplier) {
    return new NamedSupplier<>(name, supplier);
  }

  public static <R> NamedSupplier<R> of(String name, Function<String, R> function) {
    return new NamedSupplier<>(name, function);
  }

  public static <R> NamedSupplier<R> of(Supplier<R> supplier) {
    return new NamedSupplier<>(StackTraceUtils.getCallerInfo(), supplier);
  }

  public static <R> NamedSupplier<R> of(Function<String, R> function) {
    return new NamedSupplier<>(StackTraceUtils.getCallerInfo(), function);
  }

  @Override
  public R get() {
    return supplier.get();
  }

  @Override
  public String toString() {
    return "NamedSupplier{" +
            "supplier=" + supplier +
            ", name=" + getName() +
            '}';
  }

}
