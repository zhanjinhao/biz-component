package cn.addenda.component.common.lambda.costed;

import cn.addenda.component.common.util.datetime.DateUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

public class CostedSupplier<R> extends AbstractCostedFunction implements Supplier<R> {

  private static final Logger logger = LoggerFactory.getLogger(CostedSupplier.class);

  /**
   * 真正运行的任务
   */
  @Getter
  private final Supplier<R> supplier;

  protected CostedSupplier(LocalDateTime createDateTime, Long threshold, Supplier<R> supplier) {
    super(createDateTime, threshold);
    this.supplier = supplier;
  }

  protected CostedSupplier(LocalDateTime createDateTime, Long threshold, Supplier<R> supplier, ThreadPoolExecutor threadPoolExecutor) {
    super(createDateTime, threshold, threadPoolExecutor);
    this.supplier = supplier;
  }

  @Override
  public R get() {
    LocalDateTime startDateTime = LocalDateTime.now();
    try {
      R r = supplier.get();
      log(startDateTime, LocalDateTime.now(), supplier.getClass().getName(), supplier.toString(), null);
      return r;
    } catch (Exception exception) {
      log(startDateTime, LocalDateTime.now(), supplier.getClass().getName(), supplier.toString(), exception);
      throw exception;
    }
  }

  @Override
  public String toString() {
    return "CostedSupplier{" +
            "supplier=" + supplier +
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

  public static <R> CostedSupplier<R> of(LocalDateTime createDateTime, Long threshold, Supplier<R> supplier) {
    return new CostedSupplier<>(createDateTime, threshold, supplier);
  }

  public static <R> CostedSupplier<R> of(LocalDateTime createDateTime, Long threshold, Supplier<R> supplier, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedSupplier<>(createDateTime, threshold, supplier, threadPoolExecutor);
  }

  public static <R> CostedSupplier<R> of(Long threshold, Supplier<R> supplier) {
    return new CostedSupplier<>(LocalDateTime.now(), threshold, supplier);
  }

  public static <R> CostedSupplier<R> of(Long threshold, Supplier<R> supplier, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedSupplier<>(LocalDateTime.now(), threshold, supplier, threadPoolExecutor);
  }

  public static <R> CostedSupplier<R> of(Supplier<R> supplier) {
    return new CostedSupplier<>(LocalDateTime.now(), DEFAULT_THRESHOLD, supplier);
  }

  public static <R> CostedSupplier<R> of(Supplier<R> supplier, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedSupplier<>(LocalDateTime.now(), DEFAULT_THRESHOLD, supplier, threadPoolExecutor);
  }

}
