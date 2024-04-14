package cn.addenda.component.idempotence;

import cn.addenda.component.jackson.util.JacksonUtils;
import cn.addenda.component.jdk.util.my.MyArrayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @author addenda
 * @since 2023/7/29 15:18
 */
@Slf4j
public class RedisStorageCenter implements StorageCenter {

  private final StringRedisTemplate stringRedisTemplate;

  private static final DefaultRedisScript<String> GET_SET_SCRIPT = new DefaultRedisScript<>();
  private static final DefaultRedisScript<Boolean> DO_CAS_STATUS_1_SCRIPT = new DefaultRedisScript<>();
  private static final DefaultRedisScript<Boolean> DO_CAS_STATUS_2_SCRIPT = new DefaultRedisScript<>();
  private static final DefaultRedisScript<Boolean> DELETE_SCRIPT = new DefaultRedisScript<>();

  static {
    GET_SET_SCRIPT.setScriptText(
            "local key = KEYS[1];"
                    + "local xId = ARGV[1];"
                    + "local consumeMode = ARGV[2];"
                    + "local consumeStatus = ARGV[3];"
                    + "local ttlSecs = ARGV[4];"
                    + "if(redis.call('exists', key) == 0) then "
                    + "     redis.call('hset', key, 'xId', xId); "
                    + "     redis.call('hset', key, 'consumeMode', consumeMode); "
                    + "     redis.call('hset', key, 'consumeStatus', consumeStatus); "
                    + "     redis.call('expire', key, ttlSecs); "
                    + "     return nil; "
                    + "else "
                    + "     return redis.call('hget', key, 'consumeStatus');"
                    + "end ");
    GET_SET_SCRIPT.setResultType(String.class);

    DO_CAS_STATUS_1_SCRIPT.setScriptText(
            "local key = KEYS[1];"
                    + "local xId = ARGV[1];"
                    + "local expected = ARGV[2];"
                    + "local consumeStatus = ARGV[3];"
                    + "local ttlSecs = ARGV[4];"
                    + "if(redis.call('hget', key, 'xId')) ~= xId then "
                    + "    return false "
                    + "else "
                    + "    if(redis.call('hget', key, 'consumeStatus')) == expected then "
                    + "         redis.call('hset', key, 'xId', xId); "
                    + "         redis.call('hset', key, 'consumeStatus', consumeStatus); "
                    + "         redis.call('expire', key, ttlSecs); "
                    + "         return true "
                    + "     else "
                    + "         return false "
                    + "     end "
                    + "end ");
    DO_CAS_STATUS_1_SCRIPT.setResultType(Boolean.class);

    DO_CAS_STATUS_2_SCRIPT.setScriptText(
            "local key = KEYS[1];"
                    + "local xId = ARGV[1];"
                    + "local expected = ARGV[2];"
                    + "local consumeStatus = ARGV[3];"
                    + "local ttlSecs = ARGV[4];"
                    + "if(redis.call('hget', key, 'consumeStatus')) == expected then "
                    + "    redis.call('hset', key, 'xId', xId); "
                    + "    redis.call('hset', key, 'consumeStatus', consumeStatus); "
                    + "    redis.call('expire', key, ttlSecs); "
                    + "    return true "
                    + "else "
                    + "    return false "
                    + "end");
    DO_CAS_STATUS_2_SCRIPT.setResultType(Boolean.class);

    DELETE_SCRIPT.setScriptText(
            "local key = KEYS[1];"
                    + "local xId = ARGV[1];"
                    + "if(redis.call('hget', key, 'xId')) == xId then "
                    + "    redis.call('del', key);"
                    + "    return true "
                    + "else "
                    + "    return false "
                    + "end");
    DELETE_SCRIPT.setResultType(Boolean.class);
  }

  public RedisStorageCenter(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  @Override
  public ConsumeStatus getSet(IdempotenceParamWrapper param, ConsumeStatus consumeStatus) {
    String old = stringRedisTemplate.execute(GET_SET_SCRIPT, MyArrayUtils.asArrayList(param.getFullKey())
            , param.getXId(), param.getConsumeMode().name(), consumeStatus.name(), String.valueOf(param.getTtlSecs()));
    if (old == null) {
      return null;
    }
    return ConsumeStatus.valueOf(old);
  }

  @Override
  public boolean casStatus(IdempotenceParamWrapper param, ConsumeStatus expected, ConsumeStatus consumeStatus, boolean casOther) {
    return casOther ? doCasStatus2(param, expected, consumeStatus) : doCasStatus1(param, expected, consumeStatus);
  }

  private boolean doCasStatus1(IdempotenceParamWrapper param, ConsumeStatus expected, ConsumeStatus consumeStatus) {
    return Boolean.TRUE.equals(stringRedisTemplate.execute(DO_CAS_STATUS_1_SCRIPT, MyArrayUtils.asArrayList(param.getFullKey()),
            param.getXId(), expected.name(), consumeStatus.name(), String.valueOf(param.getTtlSecs())));
  }

  private boolean doCasStatus2(IdempotenceParamWrapper param, ConsumeStatus expected, ConsumeStatus consumeStatus) {
    return Boolean.TRUE.equals(stringRedisTemplate.execute(DO_CAS_STATUS_2_SCRIPT, MyArrayUtils.asArrayList(param.getFullKey()),
            param.getXId(), expected.name(), consumeStatus.name(), String.valueOf(param.getTtlSecs())));
  }

  /**
   * 打印error日志。参数不存Redis，因为Redis数据在内存中，存Redis可能把内存打满进而影响到其他业务。 <br/>
   */
  @Override
  public void saveExceptionLog(IdempotenceParamWrapper param, IdempotenceScenario scenario, Object[] arguments, ConsumeStage consumeStage, String message, Throwable throwable) {
    String argsJson = JacksonUtils.toStr(arguments);
    log.error("Consume [{}] error. Scenario: [{}], ConsumeMode: [{}]. ConsumeStage: [{}]. Message: [{}]. Arguments: [{}]. XId: [{}]",
            param.getFullKey(), scenario, param.getConsumeMode(), consumeStage, argsJson, message, param.getXId(), throwable);
  }

  @Override
  public boolean delete(IdempotenceParamWrapper param) {
    return Boolean.TRUE.equals(stringRedisTemplate.execute(
            DELETE_SCRIPT, MyArrayUtils.asArrayList(param.getFullKey()), param.getXId()));
  }

}
