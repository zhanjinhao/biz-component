package cn.addenda.component.common.lambda.att;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import lombok.Getter;

import java.util.function.Function;

public class AttFunction<A, T, R> implements Function<T, R> {

  @Getter
  private final Function<T, R> function;

  @Getter
  private final A att;

  private final boolean jacksonToString;

  private AttFunction(A att, Function<T, R> function, boolean jacksonToString) {
    this.att = att;
    this.function = function;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public R apply(T t) {
    return function.apply(t);
  }

  @Override
  public String toString() {
    return "AttFunction{" +
            "function=" + function +
            ", att=" + (jacksonToString ? JacksonUtils.toStr(att) : att) +
            '}';
  }

  public static <A, T, R> AttFunction<A, T, R> of(A att, Function<T, R> function) {
    return new AttFunction<>(att, function, false);
  }

  public static <A, T, R> AttFunction<A, T, R> of(A att, Function<T, R> function, boolean jacksonToString) {
    return new AttFunction<>(att, function, jacksonToString);
  }

}
