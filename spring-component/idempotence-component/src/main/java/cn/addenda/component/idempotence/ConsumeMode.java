package cn.addenda.component.idempotence;

/**
 * @author addenda
 * @since 2023/7/29 18:29
 */
public enum ConsumeMode {

  /**
   * 只要处理了，就认为数据被消费成功了
   */
  COMPLETE,

  /**
   * 成功，才认为数据被消费成功
   */
  SUCCESS

}
