package cn.addenda.component.common.lambda.att;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import lombok.Getter;

import java.util.function.BiFunction;

public class AttBiFunction<A, T, U, R> implements BiFunction<T, U, R> {

  @Getter
  private final BiFunction<T, U, R> biFunction;

  @Getter
  private final A att;

  private final boolean jacksonToString;

  private AttBiFunction(A att, BiFunction<T, U, R> biFunction, boolean jacksonToString) {
    this.att = att;
    this.biFunction = biFunction;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public R apply(T t, U u) {
    return biFunction.apply(t, u);
  }

  @Override
  public String toString() {
    return "AttBiFunction{" +
            "biFunction=" + biFunction +
            ", att=" + (jacksonToString ? JacksonUtils.toStr(att) : att) +
            '}';
  }

  public static <A, T, U, R> AttBiFunction<A, T, U, R> of(A att, BiFunction<T, U, R> biFunction) {
    return new AttBiFunction<>(att, biFunction, false);
  }

  public static <A, T, U, R> AttBiFunction<A, T, U, R> of(A att, BiFunction<T, U, R> biFunction, boolean jacksonToString) {
    return new AttBiFunction<>(att, biFunction, jacksonToString);
  }

}
