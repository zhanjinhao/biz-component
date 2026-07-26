package cn.addenda.component.common.lambda.att;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import lombok.Getter;

import java.util.concurrent.Callable;

public class AttCallable<A, V> implements Callable<V> {

  @Getter
  private final Callable<V> callable;

  @Getter
  private final A att;

  private final boolean jacksonToString;

  private AttCallable(A att, Callable<V> callable, boolean jacksonToString) {
    this.att = att;
    this.callable = callable;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public V call() throws Exception {
    return callable.call();
  }

  @Override
  public String toString() {
    return "AttCallable{" +
            "callable=" + callable +
            ", att=" + (jacksonToString ? JacksonUtils.toStr(att) : att) +
            '}';
  }

  public static <A, V> AttCallable<A, V> of(A att, Callable<V> callable) {
    return new AttCallable<>(att, callable, false);
  }

  public static <A, V> AttCallable<A, V> of(A att, Callable<V> callable, boolean jacksonToString) {
    return new AttCallable<>(att, callable, jacksonToString);
  }

}
