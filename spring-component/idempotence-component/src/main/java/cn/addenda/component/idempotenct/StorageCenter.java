package cn.addenda.component.idempotenct;

/**
 * @author addenda
 * @since 2023/7/29 15:10
 */
public interface StorageCenter {

  /**
   * key不存在的时候，设置key状态。
   *
   * @return 返回旧的key状态
   */
  ConsumeStatus getSet(IdempotenceParamWrapper param, ConsumeStatus consumeStatus);

  /**
   * 状态cas到指定的状态，cas的时候不判断xId，但是cas之后key的xId为当前线程。会更新ttl。
   *
   * @param casOther 是否能cas其他XId设置的数据。
   */
  boolean casStatus(IdempotenceParamWrapper param, ConsumeStatus expected, ConsumeStatus consumeStatus, boolean casOther);

  /**
   * 记录异常日志，方便后续处理。此方法不能抛异常。
   */
  void saveExceptionLog(IdempotenceParamWrapper param, IdempotenceScenario scenario, Object[] arguments, ConsumeStage consumeStage, String message, Throwable e);

  /**
   * 删除本次消费的记录（xId一致才能删除）。幂等。
   */
  boolean delete(IdempotenceParamWrapper param);

}
