package cn.addenda.component.common.lambda.named;

import cn.addenda.component.common.AbstractNamed;
import cn.addenda.component.stacktrace.StackTraceUtils;
import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class NamedCallable<V> extends AbstractNamed implements Callable<V> {

  @Getter
  private final Callable<V> callable;

  private NamedCallable(String name, Callable<V> callable) {
    super(name);
    this.callable = callable;
  }

  private NamedCallable(String name, Function<String, V> function) {
    super(name);
    this.callable = new Callable<V>() {
      @Override
      public V call() {
        return function.apply(name);
      }

      @Override
      public String toString() {
        return function.toString();
      }
    };
  }

  public static <V> NamedCallable<V> of(String name, Callable<V> callable) {
    return new NamedCallable<>(name, callable);
  }

  public static <V> NamedCallable<V> of(String name, Function<String, V> function) {
    return new NamedCallable<>(name, function);
  }

  public static <V> NamedCallable<V> of(Callable<V> callable) {
    return new NamedCallable<>(StackTraceUtils.getCallerInfo(), callable);
  }

  public static <V> NamedCallable<V> of(Function<String, V> function) {
    return new NamedCallable<>(StackTraceUtils.getCallerInfo(), function);
  }

  @Override
  public V call() throws Exception {
    return callable.call();
  }

  @Override
  public String toString() {
    return "NamedCallable{" +
            "callable=" + callable +
            ", name=" + getName() +
            '}';
  }

}
