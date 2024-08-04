package cn.addenda.component.idempotence;

import cn.addenda.component.jdk.lambda.FunctionConverter;
import cn.addenda.component.jdk.lambda.TRunnable;
import cn.addenda.component.jdk.lambda.TSupplier;
import cn.addenda.component.jdk.util.ExceptionUtils;

/**
 * @author addenda
 * @since 2023/7/31 15:00
 */
public class IdempotenceHelper extends IdempotenceSupport {

  /**
   * 最简单的加锁场景，arguments[0] 是 key
   */
  public <R> R idempotent(IdempotenceScenario scenario, TSupplier<R> supplier, Object... arguments) {
    IdempotenceAttr attr = IdempotenceAttr.builder().scenario(scenario).build();
    return idempotent(attr, supplier, arguments);
  }

  public void idempotent(IdempotenceScenario scenario, TRunnable runnable, Object... arguments) {
    IdempotenceAttr attr = IdempotenceAttr.builder().scenario(scenario).build();
    idempotent(attr, runnable, arguments);
  }

  /**
   * 较上一个场景，arguments[0] 是 key，prefix可以指定
   */
  public <R> R idempotent(IdempotenceScenario scenario, String prefix, TSupplier<R> supplier, Object... arguments) {
    IdempotenceAttr attr = IdempotenceAttr.builder().scenario(scenario).prefix(prefix).build();
    return idempotent(attr, supplier, arguments);
  }

  public void idempotent(IdempotenceScenario scenario, String prefix, TRunnable runnable, Object... arguments) {
    IdempotenceAttr attr = IdempotenceAttr.builder().scenario(scenario).prefix(prefix).build();
    idempotent(attr, runnable, arguments);
  }

  /**
   * 较上一个场景，arguments[0] 是 key，consumeMode和prefix可以指定
   */
  public <R> R idempotent(IdempotenceScenario scenario, ConsumeMode consumeMode, String prefix, TSupplier<R> supplier, Object... arguments) {
    IdempotenceAttr attr = IdempotenceAttr.builder().scenario(scenario).prefix(prefix).consumeMode(consumeMode).build();
    return idempotent(attr, supplier, arguments);
  }

  public void idempotent(IdempotenceScenario scenario, ConsumeMode consumeMode, String prefix, TRunnable runnable, Object... arguments) {
    IdempotenceAttr attr = IdempotenceAttr.builder().scenario(scenario).prefix(prefix).consumeMode(consumeMode).build();
    idempotent(attr, runnable, arguments);
  }

  public void idempotent(IdempotenceAttr attr, TRunnable runnable, Object... arguments) {
    idempotent(attr, FunctionConverter.toTSupplier(runnable), arguments);
  }

  public <R> R idempotent(IdempotenceAttr attr, TSupplier<R> supplier, Object... arguments) {
    if (arguments == null || arguments.length == 0 || attr == null || attr.getScenario() == null || supplier == null) {
      throw new IdempotenceException("参数不能为空！");
    }
    try {
      return (R) invokeWithinIdempotence(attr, arguments, supplier::get, null);
    } catch (Throwable throwable) {
      throw ExceptionUtils.wrapAsRuntimeException(throwable, IdempotenceException.class);
    }
  }

}
