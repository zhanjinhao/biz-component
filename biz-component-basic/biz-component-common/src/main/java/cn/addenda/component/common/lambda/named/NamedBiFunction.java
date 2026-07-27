package cn.addenda.component.common.lambda.named;

import cn.addenda.component.common.AbstractNamed;
import cn.addenda.component.stacktrace.StackTraceUtils;
import lombok.Getter;

import java.util.function.BiFunction;

public class NamedBiFunction<T, U, R> extends AbstractNamed implements BiFunction<T, U, R> {

  @Getter
  private final BiFunction<T, U, R> biFunction;

  private NamedBiFunction(String name, BiFunction<T, U, R> biFunction) {
    super(name);
    this.biFunction = biFunction;
  }

  @Override
  public R apply(T t, U u) {
    return biFunction.apply(t, u);
  }

  public static <T, U, R> NamedBiFunction<T, U, R> of(String name, BiFunction<T, U, R> biFunction) {
    return new NamedBiFunction<>(name, biFunction);
  }

  public static <T, U, R> NamedBiFunction<T, U, R> of(BiFunction<T, U, R> biFunction) {
    return new NamedBiFunction<>(StackTraceUtils.getCallerInfo(), biFunction);
  }

  @Override
  public String toString() {
    return "NamedBiFunction{" +
            "biFunction=" + biFunction +
            ", name=" + getName() +
            '}';
  }
}
