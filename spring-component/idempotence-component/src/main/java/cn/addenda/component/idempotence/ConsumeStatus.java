package cn.addenda.component.idempotence;

/**
 * @author addenda
 * @since 2023/7/29 18:11
 */
public enum ConsumeStatus {

  /**
   * 正在消费
   */
  CONSUMING,

  /**
   * 消费完成-正常
   */
  SUCCESS,

  /**
   * 消费完成-异常
   */
  EXCEPTION

}
