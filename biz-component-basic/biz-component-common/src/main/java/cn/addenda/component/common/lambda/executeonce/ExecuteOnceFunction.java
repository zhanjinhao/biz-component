package cn.addenda.component.common.lambda.executeonce;

import cn.addenda.component.common.jackson.util.JacksonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ExecuteOnceFunction<T, R> implements Function<T, R> {

  private final Function<T, R> function;
  private final Map<T, R> resultMap = new HashMap<>();
  private final Map<T, Boolean> executedMap = new HashMap<>();
  private final boolean cacheNull;
  private final boolean jacksonToString;

  public ExecuteOnceFunction(Function<T, R> function) {
    this(function, true, false);
  }

  public ExecuteOnceFunction(Function<T, R> function, boolean cacheNull) {
    this(function, cacheNull, false);
  }

  public ExecuteOnceFunction(Function<T, R> function, boolean cacheNull, boolean jacksonToString) {
    this.function = function;
    this.cacheNull = cacheNull;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public R apply(T t) {
    synchronized (this) {
      if (executedMap.containsKey(t)) {
        return resultMap.get(t);
      }
      R result = function.apply(t);
      if (result != null || cacheNull) {
        resultMap.put(t, result);
        executedMap.put(t, true);
      }
      return result;
    }
  }

  public static <T, R> ExecuteOnceFunction<T, R> of(Function<T, R> function) {
    return new ExecuteOnceFunction<>(function);
  }

  public static <T, R> ExecuteOnceFunction<T, R> of(Function<T, R> function, boolean cacheNull) {
    return new ExecuteOnceFunction<>(function, cacheNull);
  }

  public static <T, R> ExecuteOnceFunction<T, R> of(Function<T, R> function, boolean cacheNull, boolean jacksonToString) {
    return new ExecuteOnceFunction<>(function, cacheNull, jacksonToString);
  }

  @Override
  public String toString() {
    int count;
    synchronized (this) {
      count = executedMap.size();
    }
    return "ExecuteOnceFunction{" +
            "function=" + function +
            ", cacheNull=" + cacheNull +
            ", executedCount=" + count +
            '}';
  }

  public String toString(T key) {
    boolean contains;
    R value;
    synchronized (this) {
      contains = executedMap.containsKey(key);
      value = resultMap.get(key);
    }
    StringBuilder sb = new StringBuilder();
    sb.append("ExecuteOnceFunction{")
            .append("function=").append(function)
            .append(", key=");
    if (jacksonToString) {
      sb.append(JacksonUtils.toStr(key));
    } else {
      sb.append(key);
    }
    sb.append(", contained=").append(contains);
    if (contains) {
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
