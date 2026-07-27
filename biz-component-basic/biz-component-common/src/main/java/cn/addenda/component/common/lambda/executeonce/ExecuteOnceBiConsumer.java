package cn.addenda.component.common.lambda.executeonce;

import cn.addenda.component.common.jackson.util.JacksonUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class ExecuteOnceBiConsumer<T, U> implements BiConsumer<T, U> {

  private final BiConsumer<T, U> biConsumer;
  private final Map<T, Set<U>> executedMap = new HashMap<>();
  private final boolean jacksonToString;

  public ExecuteOnceBiConsumer(BiConsumer<T, U> biConsumer) {
    this(biConsumer, false);
  }

  public ExecuteOnceBiConsumer(BiConsumer<T, U> biConsumer, boolean jacksonToString) {
    this.biConsumer = biConsumer;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public void accept(T t, U u) {
    synchronized (this) {
      Set<U> inner = executedMap.computeIfAbsent(t, k -> new HashSet<>());
      if (!inner.contains(u)) {
        biConsumer.accept(t, u);
        inner.add(u);
      }
    }
  }

  public static <T, U> ExecuteOnceBiConsumer<T, U> of(BiConsumer<T, U> biConsumer) {
    return new ExecuteOnceBiConsumer<>(biConsumer);
  }

  public static <T, U> ExecuteOnceBiConsumer<T, U> of(BiConsumer<T, U> biConsumer, boolean jacksonToString) {
    return new ExecuteOnceBiConsumer<>(biConsumer, jacksonToString);
  }

  @Override
  public String toString() {
    int count;
    synchronized (this) {
      count = 0;
      for (Set<U> inner : executedMap.values()) {
        count += inner.size();
      }
    }
    return "ExecuteOnceBiConsumer{" +
            "biConsumer=" + biConsumer +
            ", executedCount=" + count +
            '}';
  }

  public String toString(T keyT, U keyU) {
    boolean contained;
    synchronized (this) {
      Set<U> inner = executedMap.get(keyT);
      contained = inner != null && inner.contains(keyU);
    }
    return "ExecuteOnceBiConsumer{" +
            "biConsumer=" + biConsumer +
            ", keyT=" + (jacksonToString ? JacksonUtils.toStr(keyT) : keyT) +
            ", keyU=" + (jacksonToString ? JacksonUtils.toStr(keyU) : keyU) +
            ", contained=" + contained +
            '}';
  }
}
