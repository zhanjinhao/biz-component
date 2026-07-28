package cn.addenda.component.common.lambda.costed;

import cn.addenda.component.common.util.datetime.DateUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

public class CostedCallable<V> extends AbstractCostedFunction implements Callable<V> {

  private static final Logger logger = LoggerFactory.getLogger(CostedCallable.class);

  @Getter
  private final Callable<V> callable;

  protected CostedCallable(LocalDateTime createDateTime, Long threshold, Callable<V> callable) {
    super(createDateTime, threshold);
    this.callable = callable;
  }

  protected CostedCallable(LocalDateTime createDateTime, Long threshold, Callable<V> callable, ThreadPoolExecutor threadPoolExecutor) {
    super(createDateTime, threshold, threadPoolExecutor);
    this.callable = callable;
  }

  @Override
  public V call() throws Exception {
    LocalDateTime startDateTime = LocalDateTime.now();
    try {
      V r = callable.call();
      log(startDateTime, LocalDateTime.now(), callable.getClass().getName(), callable.toString(), null);
      return r;
    } catch (Exception exception) {
      log(startDateTime, LocalDateTime.now(), callable.getClass().getName(), callable.toString(), exception);
      throw exception;
    }
  }

  @Override
  public String toString() {
    return "CostedCallable{" +
            "callable=" + callable +
            ", createDateTime=" + DateUtils.format(createDateTime, DateUtils.yMdHmsS_FMT) +
            ", threshold=" + threshold +
            ", queueSize=" + queueSize +
            ", poolSize=" + poolSize +
            ", activeCount=" + activeCount +
            '}';
  }

  @Override
  protected Logger getLogger() {
    return logger;
  }

  public static <V> CostedCallable<V> of(LocalDateTime createDateTime, Long threshold, Callable<V> callable) {
    return new CostedCallable<>(createDateTime, threshold, callable);
  }

  public static <V> CostedCallable<V> of(LocalDateTime createDateTime, Long threshold, Callable<V> callable, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedCallable<>(createDateTime, threshold, callable, threadPoolExecutor);
  }

  public static <V> CostedCallable<V> of(Long threshold, Callable<V> callable) {
    return new CostedCallable<>(LocalDateTime.now(), threshold, callable);
  }

  public static <V> CostedCallable<V> of(Long threshold, Callable<V> callable, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedCallable<>(LocalDateTime.now(), threshold, callable, threadPoolExecutor);
  }

  public static <V> CostedCallable<V> of(Callable<V> callable) {
    return new CostedCallable<>(LocalDateTime.now(), DEFAULT_THRESHOLD, callable);
  }

  public static <V> CostedCallable<V> of(Callable<V> callable, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedCallable<>(LocalDateTime.now(), DEFAULT_THRESHOLD, callable, threadPoolExecutor);
  }

}
