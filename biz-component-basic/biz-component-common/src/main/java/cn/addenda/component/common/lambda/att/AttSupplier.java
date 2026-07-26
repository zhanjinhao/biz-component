package cn.addenda.component.common.lambda.att;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import lombok.Getter;

import java.util.function.Supplier;

public class AttSupplier<A, R> implements Supplier<R> {

  @Getter
  private final Supplier<R> supplier;

  @Getter
  private final A att;

  private final boolean jacksonToString;

  private AttSupplier(A att, Supplier<R> supplier, boolean jacksonToString) {
    this.att = att;
    this.supplier = supplier;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public R get() {
    return supplier.get();
  }

  @Override
  public String toString() {
    return "AttSupplier{" +
            "supplier=" + supplier +
            ", att=" + (jacksonToString ? JacksonUtils.toStr(att) : att) +
            '}';
  }

  public static <A, R> AttSupplier<A, R> of(A att, Supplier<R> supplier) {
    return new AttSupplier<>(att, supplier, false);
  }

  public static <A, R> AttSupplier<A, R> of(A att, Supplier<R> supplier, boolean jacksonToString) {
    return new AttSupplier<>(att, supplier, jacksonToString);
  }

}
