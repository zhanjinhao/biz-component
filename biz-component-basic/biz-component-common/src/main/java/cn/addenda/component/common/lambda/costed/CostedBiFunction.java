package cn.addenda.component.common.lambda.costed;

import cn.addenda.component.common.util.datetime.DateUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiFunction;

public class CostedBiFunction<T, U, R> extends AbstractCostedFunction implements BiFunction<T, U, R> {

  private static final Logger logger = LoggerFactory.getLogger(CostedBiFunction.class);

  @Getter
  private final BiFunction<T, U, R> biFunction;

  protected CostedBiFunction(LocalDateTime createDateTime, Long threshold, BiFunction<T, U, R> biFunction) {
    super(createDateTime, threshold);
    this.biFunction = biFunction;
  }

  protected CostedBiFunction(LocalDateTime createDateTime, Long threshold, BiFunction<T, U, R> biFunction, ThreadPoolExecutor threadPoolExecutor) {
    super(createDateTime, threshold, threadPoolExecutor);
    this.biFunction = biFunction;
  }

  @Override
  public R apply(T t, U u) {
    LocalDateTime startDateTime = LocalDateTime.now();
    try {
      R r = biFunction.apply(t, u);
      log(startDateTime, LocalDateTime.now(), biFunction.getClass().getName(), biFunction.toString(), null);
      return r;
    } catch (Exception exception) {
      log(startDateTime, LocalDateTime.now(), biFunction.getClass().getName(), biFunction.toString(), exception);
      throw exception;
    }
  }

  @Override
  public String toString() {
    return "CostedBiFunction{" +
            "biFunction=" + biFunction +
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

  public static <T, U, R> CostedBiFunction<T, U, R> of(LocalDateTime createDateTime, Long threshold, BiFunction<T, U, R> biFunction) {
    return new CostedBiFunction<>(createDateTime, threshold, biFunction);
  }

  public static <T, U, R> CostedBiFunction<T, U, R> of(LocalDateTime createDateTime, Long threshold, BiFunction<T, U, R> biFunction, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedBiFunction<>(createDateTime, threshold, biFunction, threadPoolExecutor);
  }

  public static <T, U, R> CostedBiFunction<T, U, R> of(Long threshold, BiFunction<T, U, R> biFunction) {
    return new CostedBiFunction<>(LocalDateTime.now(), threshold, biFunction);
  }

  public static <T, U, R> CostedBiFunction<T, U, R> of(Long threshold, BiFunction<T, U, R> biFunction, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedBiFunction<>(LocalDateTime.now(), threshold, biFunction, threadPoolExecutor);
  }

  public static <T, U, R> CostedBiFunction<T, U, R> of(BiFunction<T, U, R> biFunction) {
    return new CostedBiFunction<>(LocalDateTime.now(), DEFAULT_THRESHOLD, biFunction);
  }

  public static <T, U, R> CostedBiFunction<T, U, R> of(BiFunction<T, U, R> biFunction, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedBiFunction<>(LocalDateTime.now(), DEFAULT_THRESHOLD, biFunction, threadPoolExecutor);
  }

}
