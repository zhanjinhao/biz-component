package cn.addenda.component.common.lambda.named;

import cn.addenda.component.common.AbstractNamed;
import cn.addenda.component.stacktrace.StackTraceUtils;
import lombok.Getter;

import java.util.function.BiConsumer;

public class NamedBiConsumer<T, U> extends AbstractNamed implements BiConsumer<T, U> {

  @Getter
  private final BiConsumer<T, U> biConsumer;

  private NamedBiConsumer(String name, BiConsumer<T, U> biConsumer) {
    super(name);
    this.biConsumer = biConsumer;
  }

  @Override
  public void accept(T t, U u) {
    biConsumer.accept(t, u);
  }

  public static <T, U> NamedBiConsumer<T, U> of(String name, BiConsumer<T, U> biConsumer) {
    return new NamedBiConsumer<>(name, biConsumer);
  }

  public static <T, U> NamedBiConsumer<T, U> of(BiConsumer<T, U> biConsumer) {
    return new NamedBiConsumer<>(StackTraceUtils.getCallerInfo(), biConsumer);
  }

  @Override
  public String toString() {
    return "NamedBiConsumer{" +
            "biConsumer=" + biConsumer +
            ", name=" + getName() +
            '}';
  }
}
