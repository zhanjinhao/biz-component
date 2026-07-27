package cn.addenda.component.common.lambda.named;

import cn.addenda.component.common.AbstractNamed;
import cn.addenda.component.stacktrace.StackTraceUtils;
import lombok.Getter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NamedConsumer<T> extends AbstractNamed implements Consumer<T> {

  @Getter
  private final Consumer<T> consumer;

  private NamedConsumer(String name, Consumer<T> consumer) {
    super(name);
    this.consumer = consumer;
  }

  private NamedConsumer(String name, BiConsumer<String, T> biConsumer) {
    super(name);
    this.consumer = new Consumer<T>() {
      @Override
      public void accept(T t) {
        biConsumer.accept(name, t);
      }

      @Override
      public String toString() {
        return biConsumer.toString();
      }
    };
  }

  @Override
  public void accept(T t) {
    consumer.accept(t);
  }

  public static <T> NamedConsumer<T> of(String name, Consumer<T> consumer) {
    return new NamedConsumer<>(name, consumer);
  }

  public static <T> NamedConsumer<T> of(String name, BiConsumer<String, T> biConsumer) {
    return new NamedConsumer<>(name, biConsumer);
  }

  public static <T> NamedConsumer<T> of(Consumer<T> consumer) {
    return new NamedConsumer<>(StackTraceUtils.getCallerInfo(), consumer);
  }

  public static <T> NamedConsumer<T> of(BiConsumer<String, T> biConsumer) {
    return new NamedConsumer<>(StackTraceUtils.getCallerInfo(), biConsumer);
  }

  @Override
  public String toString() {
    return "NamedConsumer{" +
            "consumer=" + consumer +
            ", name=" + getName() +
            '}';
  }
}
