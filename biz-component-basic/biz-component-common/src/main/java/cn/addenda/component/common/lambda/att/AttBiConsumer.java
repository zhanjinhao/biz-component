package cn.addenda.component.common.lambda.att;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import lombok.Getter;

import java.util.function.BiConsumer;

public class AttBiConsumer<A, T, U> implements BiConsumer<T, U> {

  @Getter
  private final BiConsumer<T, U> biConsumer;

  @Getter
  private final A att;

  private final boolean jacksonToString;

  private AttBiConsumer(A att, BiConsumer<T, U> biConsumer, boolean jacksonToString) {
    this.att = att;
    this.biConsumer = biConsumer;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public void accept(T t, U u) {
    biConsumer.accept(t, u);
  }

  @Override
  public String toString() {
    return "AttBiConsumer{" +
            "biConsumer=" + biConsumer +
            ", att=" + (jacksonToString ? JacksonUtils.toStr(att) : att) +
            '}';
  }

  public static <A, T, U> AttBiConsumer<A, T, U> of(A att, BiConsumer<T, U> biConsumer) {
    return new AttBiConsumer<>(att, biConsumer, false);
  }

  public static <A, T, U> AttBiConsumer<A, T, U> of(A att, BiConsumer<T, U> biConsumer, boolean jacksonToString) {
    return new AttBiConsumer<>(att, biConsumer, jacksonToString);
  }

}
