package cn.addenda.component.common.lambda.executeonce;

import cn.addenda.component.common.jackson.util.JacksonUtils;

import java.util.concurrent.Callable;

public class ExecuteOnceCallable<V> implements Callable<V> {
  private final Callable<V> callable;
  private V result;
  private boolean executed;
  private final boolean jacksonToString;

  public ExecuteOnceCallable(Callable<V> callable) {
    this(callable, false);
  }

  public ExecuteOnceCallable(Callable<V> callable, boolean jacksonToString) {
    this.callable = callable;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public V call() throws Exception {
    synchronized (this) {
      if (!executed) {
        result = callable.call();
        executed = true;
      }
      return result;
    }
  }

  public static <V> ExecuteOnceCallable<V> of(Callable<V> callable) {
    return new ExecuteOnceCallable<>(callable);
  }

  public static <V> ExecuteOnceCallable<V> of(Callable<V> callable, boolean jacksonToString) {
    return new ExecuteOnceCallable<>(callable, jacksonToString);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ExecuteOnceCallable{")
            .append("callable=").append(callable)
            .append(", executed=").append(executed);
    if (executed) {
      sb.append(", result=");
      if (jacksonToString) {
        sb.append(JacksonUtils.toStr(result));
      } else {
        sb.append(result);
      }
    }
    sb.append('}');
    return sb.toString();
  }
}
