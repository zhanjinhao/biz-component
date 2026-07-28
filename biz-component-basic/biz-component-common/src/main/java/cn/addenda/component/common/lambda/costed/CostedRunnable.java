package cn.addenda.component.common.lambda.costed;

import cn.addenda.component.common.util.datetime.DateUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;

public class CostedRunnable extends AbstractCostedFunction implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(CostedRunnable.class);

  /**
   * 真正运行的任务
   */
  @Getter
  private final Runnable runnable;

  protected CostedRunnable(LocalDateTime createDateTime, Long threshold, Runnable runnable) {
    super(createDateTime, threshold);
    this.runnable = runnable;
  }

  protected CostedRunnable(LocalDateTime createDateTime, Long threshold, Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
    super(createDateTime, threshold, threadPoolExecutor);
    this.runnable = runnable;
  }

  @Override
  public void run() {
    LocalDateTime startDateTime = LocalDateTime.now();
    try {
      runnable.run();
      log(startDateTime, LocalDateTime.now(), runnable.getClass().getName(), runnable.toString(), null);
    } catch (Exception exception) {
      log(startDateTime, LocalDateTime.now(), runnable.getClass().getName(), runnable.toString(), exception);
      throw exception;
    }
  }

  @Override
  public String toString() {
    return "CostedRunnable{" +
            "runnable=" + runnable +
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

  public static CostedRunnable of(LocalDateTime createDateTime, Long threshold, Runnable runnable) {
    return new CostedRunnable(createDateTime, threshold, runnable);
  }

  public static CostedRunnable of(LocalDateTime createDateTime, Long threshold, Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedRunnable(createDateTime, threshold, runnable, threadPoolExecutor);
  }

  public static CostedRunnable of(Long threshold, Runnable runnable) {
    return new CostedRunnable(LocalDateTime.now(), threshold, runnable);
  }

  public static CostedRunnable of(Long threshold, Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedRunnable(LocalDateTime.now(), threshold, runnable, threadPoolExecutor);
  }

  public static CostedRunnable of(Runnable runnable) {
    return new CostedRunnable(LocalDateTime.now(), DEFAULT_THRESHOLD, runnable);
  }

  public static CostedRunnable of(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
    return new CostedRunnable(LocalDateTime.now(), DEFAULT_THRESHOLD, runnable, threadPoolExecutor);
  }

}
