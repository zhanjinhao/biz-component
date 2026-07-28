package cn.addenda.component.common.lambda.costed;

import cn.addenda.component.common.util.datetime.DateUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiConsumer;

public class CostedBiConsumer<T, U> extends AbstractCostedFunction implements BiConsumer<T, U> {

  private static final Logger logger = LoggerFactory.getLogger(CostedBiConsumer.class);

  @Getter
  private final BiConsumer<T, U> biConsumer;

  protected CostedBiConsumer(LocalDateTime createDateTime, Long threshold, BiConsumer<T, U> biConsumer) {
    super(createDateTime, threshold);
    this.biConsumer = biConsumer;
  }

  protected CostedBiConsumer(LocalDateTime createDateTime, Long threshold, BiConsumer<T, U> biConsumer, ThreadPoolExecutor threadPoolExecutor) {
    super(createDateTime, threshold, threadPoolExecutor);
    this.biConsumer = biConsumer;
  }

  @Override
  public void accept(T t, U u) {
    LocalDateTime startDateTime = LocalDateTime.now();
    try {
      biConsumer.accept(t, u);
      log(startDateTime, LocalDateTime.now(), biConsumer.getClass().getName(), biConsumer.toString(), null);
    } catch (Exception exception) {
      log(startDateTime, LocalDateTime.now(), biConsumer.getClass().getName(), biConsumer.toString(), exception);
      throw exception;
    }
  }

  @Override
  public String toString() {
    return "CostedBiConsumer{" +
            "biConsumer=" + biConsumer +
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

  public static <T, U> CostedBiConsumer<T, U> of(LocalDateTime createDateTime, Long threshold, BiConsumer<T, U> biConsumer) {
    return new CostedBiConsumer<>(createDateTime, threshold, biConsumer);
  }

  public static <T, U> CostedBiConsumer<T, U> of(LocalDateTime createDateTime, Long threshold, BiConsumer<T, U> biConsumer, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedBiConsumer<>(createDateTime, threshold, biConsumer, threadPoolExecutor);
  }

  public static <T, U> CostedBiConsumer<T, U> of(Long threshold, BiConsumer<T, U> biConsumer) {
    return new CostedBiConsumer<>(LocalDateTime.now(), threshold, biConsumer);
  }

  public static <T, U> CostedBiConsumer<T, U> of(Long threshold, BiConsumer<T, U> biConsumer, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedBiConsumer<>(LocalDateTime.now(), threshold, biConsumer, threadPoolExecutor);
  }

  public static <T, U> CostedBiConsumer<T, U> of(BiConsumer<T, U> biConsumer) {
    return new CostedBiConsumer<>(LocalDateTime.now(), DEFAULT_THRESHOLD, biConsumer);
  }

  public static <T, U> CostedBiConsumer<T, U> of(BiConsumer<T, U> biConsumer, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedBiConsumer<>(LocalDateTime.now(), DEFAULT_THRESHOLD, biConsumer, threadPoolExecutor);
  }

}
