package cn.addenda.component.common.lambda.costed;

import cn.addenda.component.common.util.datetime.DateUtils;
import cn.addenda.component.common.util.string.Slf4jUtils;
import lombok.Getter;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

public abstract class AbstractCostedFunction {

  /**
   * todo 从系统变量里面拿
   */
  public static Long DEFAULT_THRESHOLD = 200L;

  /**
   * 任务创建时间
   */
  @Getter
  protected final LocalDateTime createDateTime;

  /**
   * 超时的阈值
   */
  @Getter
  protected final Long threshold;

  // ------------------------------------
  //  下面的属性是在线程池里面运行任务的时候会有
  // ------------------------------------

  @Getter
  protected Integer queueSize;

  @Getter
  protected Integer poolSize;

  @Getter
  protected Integer activeCount;

  protected AbstractCostedFunction(LocalDateTime createDateTime, Long threshold) {
    this.createDateTime = createDateTime;
    this.threshold = threshold;
  }

  protected AbstractCostedFunction(LocalDateTime createDateTime, Long threshold, ThreadPoolExecutor threadPoolExecutor) {
    this.createDateTime = createDateTime;
    this.threshold = threshold;
    this.queueSize = threadPoolExecutor.getQueue().size();
    this.poolSize = threadPoolExecutor.getPoolSize();
    this.activeCount = threadPoolExecutor.getActiveCount();
  }

  /**
   * 记录任务的耗时日志。
   *
   * <p><b>时间计算：</b>
   * <ul>
   *   <li>{@code totalCost = endDateTime - createDateTime}：从任务创建到执行完毕的总耗时（含排队等待时间）</li>
   *   <li>{@code runCost = endDateTime - startDateTime}：纯执行耗时（不含排队等待时间）</li>
   * </ul>
   *
   * <p><b>日志消息：</b>
   * <ul>
   *   <li>若 {@code queueSize == null}（非线程池提交场景）：
   *       仅记录 createDateTime、endDateTime、totalCost</li>
   *   <li>若 {@code queueSize != null}（线程池提交场景）：
   *       额外记录 startDateTime、runCost 以及提交时刻的线程池状态（queueSize、poolSize、activeCount）</li>
   * </ul>
   *
   * <p><b>日志级别决策：</b>
   * <ol>
   *   <li>如果发生了异常（{@code throwable != null}）：记录 {@code ERROR} 级别日志，并附带异常堆栈</li>
   *   <li>如果 {@code runCost == 0}（无法精确测量 runCost，因为 startDateTime 和 endDateTime 在同一毫秒）：
   *       仅当 {@code totalCost > threshold} 时记录 {@code ERROR}，否则降级到 {@code DEBUG}</li>
   *   <li>如果 {@code runCost > 0}：同时满足以下任一条件时记录 {@code ERROR}，否则降级到 {@code DEBUG}
   *     <ul>
   *       <li>{@code totalCost > 2 * runCost}：排队等待时间是执行时间的 2 倍以上，说明线程池繁忙</li>
   *       <li>{@code totalCost - runCost > threshold}：排队等待时间超过阈值</li>
   *     </ul>
   *   </li>
   * </ol>
   *
   * <p><b>注意：</b>当 {@code runCost == 0} 时不使用 {@code totalCost > 2 * runCost} 判断，因为除法分母为 0
   * 会导致条件永远不成立（或数值溢出），因此退化为仅依赖 threshold 的单条件判断，避免假阴性。
   *
   * @param startDateTime 任务开始执行的时间（run 的起始时刻）
   * @param endDateTime   任务执行完毕的时间
   * @param functionType  函数类型（通常为 delegate 的 className）
   * @param functionName  函数名称（通常为 delegate 的 toString）
   * @param throwable     执行过程中抛出的异常，为 {@code null} 表示正常执行
   */
  protected void log(LocalDateTime startDateTime, LocalDateTime endDateTime, String functionType, String functionName, Throwable throwable) {
    long totalCost = DateUtils.localDateTimeToTimestamp(endDateTime) - DateUtils.localDateTimeToTimestamp(createDateTime);
    long runCost = DateUtils.localDateTimeToTimestamp(endDateTime) - DateUtils.localDateTimeToTimestamp(startDateTime);
    Supplier<String> msgSupplier;
    if (queueSize == null) {
      msgSupplier = () -> Slf4jUtils.format(
              "{}[{}]: createDateTime[{}], endDateTime[{}], totalCost[{}ms]. ",
              functionType, functionName, DateUtils.format(createDateTime, DateUtils.yMdHmsS_FMT),
              DateUtils.format(endDateTime, DateUtils.yMdHmsS_FMT), totalCost);
    } else {
      msgSupplier = () -> Slf4jUtils.format(
              "{}[{}]: createDateTime[{}], startDateTime[{}], endDateTime[{}], totalCost[{}ms], runCost[{}ms]. The state of the thread pool at the moment the task is submitted: queueSize[{}], poolSize[{}], activeCount[{}].",
              functionType, functionName, DateUtils.format(createDateTime, DateUtils.yMdHmsS_FMT),
              DateUtils.format(startDateTime, DateUtils.yMdHmsS_FMT), DateUtils.format(endDateTime, DateUtils.yMdHmsS_FMT),
              totalCost, runCost, queueSize, poolSize, activeCount);
    }

    if (throwable != null) {
      getLogger().error(msgSupplier.get(), throwable);
    } else {
      boolean flag = false;
      if (runCost == 0) {
        if (totalCost > threshold) {
          getLogger().error(msgSupplier.get());
          flag = true;
        }
      } else if (totalCost > 2 * runCost || totalCost - runCost > threshold) {
        getLogger().error(msgSupplier.get());
        flag = true;
      }
      if (!flag && getLogger().isDebugEnabled()) {
        getLogger().debug(msgSupplier.get());
      }
    }
  }

  protected abstract Logger getLogger();

}
