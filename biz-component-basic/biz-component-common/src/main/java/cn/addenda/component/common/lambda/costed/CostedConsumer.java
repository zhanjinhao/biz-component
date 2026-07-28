package cn.addenda.component.common.lambda.costed;

import cn.addenda.component.common.util.datetime.DateUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

public class CostedConsumer<T> extends AbstractCostedFunction implements Consumer<T> {

  private static final Logger logger = LoggerFactory.getLogger(CostedConsumer.class);

  @Getter
  private final Consumer<T> consumer;

  protected CostedConsumer(LocalDateTime createDateTime, Long threshold, Consumer<T> consumer) {
    super(createDateTime, threshold);
    this.consumer = consumer;
  }

  protected CostedConsumer(LocalDateTime createDateTime, Long threshold, Consumer<T> consumer, ThreadPoolExecutor threadPoolExecutor) {
    super(createDateTime, threshold, threadPoolExecutor);
    this.consumer = consumer;
  }

  @Override
  public void accept(T t) {
    LocalDateTime startDateTime = LocalDateTime.now();
    try {
      consumer.accept(t);
      log(startDateTime, LocalDateTime.now(), consumer.getClass().getName(), consumer.toString(), null);
    } catch (Exception exception) {
      log(startDateTime, LocalDateTime.now(), consumer.getClass().getName(), consumer.toString(), exception);
      throw exception;
    }
  }

  @Override
  public String toString() {
    return "CostedConsumer{" +
            "consumer=" + consumer +
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

  public static <T> CostedConsumer<T> of(LocalDateTime createDateTime, Long threshold, Consumer<T> consumer) {
    return new CostedConsumer<>(createDateTime, threshold, consumer);
  }

  public static <T> CostedConsumer<T> of(LocalDateTime createDateTime, Long threshold, Consumer<T> consumer, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedConsumer<>(createDateTime, threshold, consumer, threadPoolExecutor);
  }

  public static <T> CostedConsumer<T> of(Long threshold, Consumer<T> consumer) {
    return new CostedConsumer<>(LocalDateTime.now(), threshold, consumer);
  }

  public static <T> CostedConsumer<T> of(Long threshold, Consumer<T> consumer, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedConsumer<>(LocalDateTime.now(), threshold, consumer, threadPoolExecutor);
  }

  public static <T> CostedConsumer<T> of(Consumer<T> consumer) {
    return new CostedConsumer<>(LocalDateTime.now(), DEFAULT_THRESHOLD, consumer);
  }

  public static <T> CostedConsumer<T> of(Consumer<T> consumer, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedConsumer<>(LocalDateTime.now(), DEFAULT_THRESHOLD, consumer, threadPoolExecutor);
  }

}
