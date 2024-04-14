package cn.addenda.component.idempotence;

/**
 * @author addenda
 * @since 2023/9/14 20:08
 */
public enum ConsumeStage {
  /**
   * 未进入消费
   */
  BEFORE_CONSUME,
  /**
   * 消费中
   */
  CONSUME,
  /**
   * 消费成功后设置SUCCESS
   */
  AFTER_CONSUME,
  /**
   * REQUEST场景出现BusinessException后删除key失败
   */
  CONSUME_DELETE,
  /**
   * key的状态为SUCCESS后再次消费
   */
  REPEAT_CONSUME,
  /**
   * key的状态为CONSUMING，当前线程等待超时
   */
  WAITING_TIMEOUT,
  /**
   * 上次状态为EXCEPTION，本次重试失败
   */
  BEFORE_RETRY,
}