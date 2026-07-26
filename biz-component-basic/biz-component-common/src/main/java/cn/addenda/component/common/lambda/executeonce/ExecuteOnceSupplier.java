package cn.addenda.component.common.lambda.executeonce;

import cn.addenda.component.common.jackson.util.JacksonUtils;

import java.util.function.Supplier;

public class ExecuteOnceSupplier<R> implements Supplier<R> {
  private final Supplier<R> supplier;
  private R r;
  private boolean executed;
  private final boolean jacksonToString;

  public ExecuteOnceSupplier(Supplier<R> supplier) {
    this(supplier, false);
  }

  public ExecuteOnceSupplier(Supplier<R> supplier, boolean jacksonToString) {
    this.supplier = supplier;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public R get() {
    synchronized (this) {
      if (!executed) {
        r = supplier.get();
        executed = true;
      }
      return r;
    }
  }

  public static <R> ExecuteOnceSupplier<R> of(Supplier<R> supplier) {
    return new ExecuteOnceSupplier<>(supplier);
  }

  public static <R> ExecuteOnceSupplier<R> of(Supplier<R> supplier, boolean jacksonToString) {
    return new ExecuteOnceSupplier<>(supplier, jacksonToString);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ExecuteOnceSupplier{")
            .append("supplier=").append(supplier)
            .append(", executed=").append(executed);
    if (executed) {
      sb.append(", result=");
      if (jacksonToString) {
        sb.append(JacksonUtils.toStr(r));
      } else {
        sb.append(r);
      }
    }
    sb.append('}');
    return sb.toString();
  }
}
