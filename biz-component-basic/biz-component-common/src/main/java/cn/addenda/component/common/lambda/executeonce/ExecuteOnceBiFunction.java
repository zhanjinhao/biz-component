package cn.addenda.component.common.lambda.executeonce;

import cn.addenda.component.common.jackson.util.JacksonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ExecuteOnceBiFunction<T, U, R> implements BiFunction<T, U, R> {

  private final BiFunction<T, U, R> biFunction;
  private final Map<T, Map<U, R>> resultMap = new HashMap<>();
  private final Map<T, Map<U, Boolean>> executedMap = new HashMap<>();
  private final boolean cacheNull;
  private final boolean jacksonToString;

  public ExecuteOnceBiFunction(BiFunction<T, U, R> biFunction) {
    this(biFunction, true, false);
  }

  public ExecuteOnceBiFunction(BiFunction<T, U, R> biFunction, boolean cacheNull) {
    this(biFunction, cacheNull, false);
  }

  public ExecuteOnceBiFunction(BiFunction<T, U, R> biFunction, boolean cacheNull, boolean jacksonToString) {
    this.biFunction = biFunction;
    this.cacheNull = cacheNull;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public R apply(T t, U u) {
    synchronized (this) {
      Map<U, Boolean> innerExecuted = executedMap.get(t);
      if (innerExecuted != null && Boolean.TRUE.equals(innerExecuted.get(u))) {
        return resultMap.get(t).get(u);
      }
      R result = biFunction.apply(t, u);
      if (result != null || cacheNull) {
        resultMap.computeIfAbsent(t, k -> new HashMap<>()).put(u, result);
        executedMap.computeIfAbsent(t, k -> new HashMap<>()).put(u, true);
      }
      return result;
    }
  }

  public static <T, U, R> ExecuteOnceBiFunction<T, U, R> of(BiFunction<T, U, R> biFunction) {
    return new ExecuteOnceBiFunction<>(biFunction);
  }

  public static <T, U, R> ExecuteOnceBiFunction<T, U, R> of(BiFunction<T, U, R> biFunction, boolean cacheNull) {
    return new ExecuteOnceBiFunction<>(biFunction, cacheNull);
  }

  public static <T, U, R> ExecuteOnceBiFunction<T, U, R> of(BiFunction<T, U, R> biFunction, boolean cacheNull, boolean jacksonToString) {
    return new ExecuteOnceBiFunction<>(biFunction, cacheNull, jacksonToString);
  }

  @Override
  public String toString() {
    int count;
    synchronized (this) {
      count = 0;
      for (Map<U, Boolean> inner : executedMap.values()) {
        count += inner.size();
      }
    }
    return "ExecuteOnceBiFunction{" +
            "biFunction=" + biFunction +
            ", cacheNull=" + cacheNull +
            ", executedCount=" + count +
            '}';
  }

  public String toString(T keyT, U keyU) {
    boolean contains;
    R value;
    synchronized (this) {
      Map<U, Boolean> innerExecuted = executedMap.get(keyT);
      contains = innerExecuted != null && Boolean.TRUE.equals(innerExecuted.get(keyU));
      value = contains ? resultMap.get(keyT).get(keyU) : null;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("ExecuteOnceBiFunction{")
            .append("biFunction=").append(biFunction)
            .append(", contained=").append(contains);
    if (contains) {
      sb.append(", keyT=");
      if (jacksonToString) {
        sb.append(JacksonUtils.toStr(keyT));
      } else {
        sb.append(keyT);
      }
      sb.append(", keyU=");
      if (jacksonToString) {
        sb.append(JacksonUtils.toStr(keyU));
      } else {
        sb.append(keyU);
      }
      sb.append(", result=");
      if (jacksonToString) {
        sb.append(JacksonUtils.toStr(value));
      } else {
        sb.append(value);
      }
    }
    sb.append('}');
    return sb.toString();
  }
}
