package cn.addenda.component.common.lambda.att;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import lombok.Getter;

import java.util.function.Consumer;

public class AttConsumer<A, T> implements Consumer<T> {

  @Getter
  private final Consumer<T> consumer;

  @Getter
  private final A att;

  private final boolean jacksonToString;

  private AttConsumer(A att, Consumer<T> consumer, boolean jacksonToString) {
    this.att = att;
    this.consumer = consumer;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public void accept(T t) {
    consumer.accept(t);
  }

  @Override
  public String toString() {
    return "AttConsumer{" +
            "consumer=" + consumer +
            ", att=" + (jacksonToString ? JacksonUtils.toStr(att) : att) +
            '}';
  }

  public static <A, T> AttConsumer<A, T> of(A att, Consumer<T> consumer) {
    return new AttConsumer<>(att, consumer, false);
  }

  public static <A, T> AttConsumer<A, T> of(A att, Consumer<T> consumer, boolean jacksonToString) {
    return new AttConsumer<>(att, consumer, jacksonToString);
  }

}
