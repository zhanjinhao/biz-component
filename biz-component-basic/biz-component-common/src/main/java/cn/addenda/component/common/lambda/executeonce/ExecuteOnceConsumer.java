package cn.addenda.component.common.lambda.executeonce;

import cn.addenda.component.common.jackson.util.JacksonUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ExecuteOnceConsumer<T> implements Consumer<T> {

  private final Consumer<T> consumer;
  private final Set<T> executedKeys = new HashSet<>();
  private final boolean jacksonToString;

  public ExecuteOnceConsumer(Consumer<T> consumer) {
    this(consumer, false);
  }

  public ExecuteOnceConsumer(Consumer<T> consumer, boolean jacksonToString) {
    this.consumer = consumer;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public void accept(T t) {
    synchronized (this) {
      if (!executedKeys.contains(t)) {
        consumer.accept(t);
        executedKeys.add(t);
      }
    }
  }

  public static <T> ExecuteOnceConsumer<T> of(Consumer<T> consumer) {
    return new ExecuteOnceConsumer<>(consumer);
  }

  public static <T> ExecuteOnceConsumer<T> of(Consumer<T> consumer, boolean jacksonToString) {
    return new ExecuteOnceConsumer<>(consumer, jacksonToString);
  }

  @Override
  public String toString() {
    int count;
    synchronized (this) {
      count = executedKeys.size();
    }
    return "ExecuteOnceConsumer{" +
            "consumer=" + consumer +
            ", executedCount=" + count +
            '}';
  }

  public String toString(T key) {
    boolean contained;
    synchronized (this) {
      contained = executedKeys.contains(key);
    }
    return "ExecuteOnceConsumer{" +
            "consumer=" + consumer +
            ", key=" + (jacksonToString ? JacksonUtils.toStr(key) : key) +
            ", contained=" + contained +
            '}';
  }
}
