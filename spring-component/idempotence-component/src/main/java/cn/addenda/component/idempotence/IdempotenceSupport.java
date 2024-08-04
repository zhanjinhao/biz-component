package cn.addenda.component.idempotence;

import cn.addenda.component.basaspring.context.ValueResolverHelper;
import cn.addenda.component.basaspring.util.SpELUtils;
import cn.addenda.component.jackson.util.JacksonUtils;
import cn.addenda.component.jdk.exception.ServiceException;
import cn.addenda.component.jdk.exception.component.ComponentServiceException;
import cn.addenda.component.jdk.lambda.TBiFunction;
import cn.addenda.component.jdk.lambda.TSupplier;
import cn.addenda.component.jdk.util.RetryUtils;
import cn.addenda.component.jdk.util.SleepUtils;
import cn.addenda.component.jdk.util.TimeUnitUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author addenda
 * @since 2023/7/29 14:11
 */
@Slf4j
public class IdempotenceSupport implements EnvironmentAware, InitializingBean, ApplicationContextAware {

  /**
   * {@link EnableIdempotenceManagement#namespace()}
   */
  @Setter
  private String namespace;

  private Environment environment;

  private ApplicationContext applicationContext;

  @Setter
  protected String spELArgsName = "spELArgs";

  private final Map<String, StorageCenter> storageCenterMap = new ConcurrentHashMap<>();

  public Object invokeWithinIdempotence(IdempotenceAttr attr, Object[] arguments, TSupplier<Object> supplier, Method method) throws Throwable {

    String spEL = attr.getSpEL();
    String key = SpELUtils.getKey(spEL, method, spELArgsName, arguments);

    if (key == null || key.isEmpty()) {
      String msg = String.format("Key of idempotence operation can not be null or \"\". arguments: [%s], spEL: [%s].",
              JacksonUtils.toStr(arguments), spEL);
      throw new IdempotenceException(msg);
    }

    ConsumeMode consumeMode = attr.getConsumeMode();
    IdempotenceParamWrapper param = IdempotenceParamWrapper.builder()
            .namespace(namespace)
            .prefix(attr.getPrefix())
            .key(key)
            .consumeMode(consumeMode)
            .ttlSecs((int) attr.getTimeUnit().toSeconds(attr.getTtl()))
            .xId(UUID.randomUUID().toString().replace("-", ""))
            .build();

    switch (consumeMode) {
      case SUCCESS:
        return handleSuccessMode(param, attr, supplier, true, arguments);
      case COMPLETE:
        return handleCompleteMode(param, attr, supplier, arguments);
      default: // unreachable
        return null;
    }
  }

  /**
   * 数据当前状态无论是 {@link ConsumeStatus#CONSUMING}/{@link ConsumeStatus#SUCCESS}/{@link ConsumeStatus#EXCEPTION}，都认为消费过。
   */
  private Object handleCompleteMode(IdempotenceParamWrapper param, IdempotenceAttr attr,
                                    TSupplier<Object> supplier, Object[] arguments) throws Throwable {
    return handle(param, attr, arguments, new TBiFunction<StorageCenter, ConsumeStatus, Object>() {
      @Override
      public Object apply(StorageCenter storageCenter, ConsumeStatus consumeStatus) throws Throwable {
        if (consumeStatus == null) {
          return consume(storageCenter, supplier, param, attr, arguments);
        } else {
          return repeatConsume(storageCenter, param, attr, arguments);
        }
      }
    });
  }

  /**
   * 对于新数据：<br/>
   * 开始消费之前，将数据置为 {@link ConsumeStatus#CONSUMING}。 <br/>
   * 消费正常完成，将数据置为 {@link ConsumeStatus#SUCCESS}。 <br/>
   * 消费异常完成，将数据置为 {@link ConsumeStatus#EXCEPTION}。
   * <p/>
   * 对于重复消费数据：<br/>
   * 如果上一条数据是正在消费中，等待后重试。 <br/>
   * 如果上一条数据是消费异常完成，本次按新数据消费。 <br/>
   * 如果上一条数据是消费正常完成，本次消费进入重复消费逻辑。
   */
  private Object handleSuccessMode(IdempotenceParamWrapper param, IdempotenceAttr attr,
                                   TSupplier<Object> supplier, boolean retry, Object[] arguments) throws Throwable {
    return handle(param, attr, arguments, new TBiFunction<StorageCenter, ConsumeStatus, Object>() {
      @Override
      public Object apply(StorageCenter storageCenter, ConsumeStatus consumeStatus) throws Throwable {
        if (consumeStatus == null) {
          return consume(storageCenter, supplier, param, attr, arguments);
        } else if (consumeStatus == ConsumeStatus.EXCEPTION) {
          boolean b;
          try {
            b = storageCenter.casStatus(param, ConsumeStatus.EXCEPTION, ConsumeStatus.CONSUMING, true);
          } catch (Throwable e) {
            throw exceptionCallback(storageCenter, param, attr.getScenario(), arguments, ConsumeStage.BEFORE_RETRY,
                    "Last consumption result is EXCEPTION and cas EXCEPTION to CONSUMING error.", e);
          }
          if (b) {
            log.info("[{}] has consumed exceptionally. re-consume it. Mode: [{}]. Arguments: [{}].",
                    param, param.getConsumeMode(), JacksonUtils.toStr(arguments));
            return consume(storageCenter, supplier, param, attr, arguments);
          } else {
            SleepUtils.sleep(attr.getTimeUnit(), attr.getExpectCost());
            return handleSuccessMode(param, attr, supplier, true, arguments);
          }
        } else if (consumeStatus == ConsumeStatus.SUCCESS) {
          return repeatConsume(storageCenter, param, attr, arguments);
        } else if (consumeStatus == ConsumeStatus.CONSUMING) {
          if (retry) {
            SleepUtils.sleep(attr.getTimeUnit(), attr.getExpectCost());
            return handleSuccessMode(param, attr, supplier, false, arguments);
          } else {
            IdempotenceScenario scenario = attr.getScenario();
            String msg = String.format("[%s] has always been in consumption. Expected cost: [%s ms].", param, attr.getTimeUnit().toMillis(attr.getExpectCost()));
            throw exceptionCallback(storageCenter, param, scenario, arguments, ConsumeStage.WAITING_TIMEOUT, msg, new ComponentServiceException(msg));
          }
        }
        return null; // unreachable
      }
    });
  }

  private Object handle(IdempotenceParamWrapper param, IdempotenceAttr attr, Object[] arguments,
                        TBiFunction<StorageCenter, ConsumeStatus, Object> function) throws Throwable {
    String storageCenterName = attr.getStorageCenter();
    StorageCenter storageCenter = storageCenterMap.computeIfAbsent(storageCenterName, s -> (StorageCenter) applicationContext.getBean(storageCenterName));
    ConsumeStatus consumeStatus = setConsumingStatusIfAbsent(param, attr, storageCenter, arguments);
    return function.apply(storageCenter, consumeStatus);
  }

  private ConsumeStatus setConsumingStatusIfAbsent(IdempotenceParamWrapper param, IdempotenceAttr attr, StorageCenter storageCenter, Object[] arguments) throws Throwable {
    try {
      return storageCenter.getSet(param, ConsumeStatus.CONSUMING);
    } catch (Throwable e) {
      throw exceptionCallback(storageCenter, param, attr.getScenario(), arguments, ConsumeStage.BEFORE_CONSUME,
              "GetSet CONSUMING status error.", e);
    }
  }

  private Object consume(StorageCenter storageCenter, TSupplier<Object> supplier,
                         IdempotenceParamWrapper param, IdempotenceAttr attr, Object[] arguments) throws Throwable {
    Object o;
    try {
      o = supplier.get();
    } catch (ServiceException e) {
      // MQ场景下，如果发生BusinessException，和其他异常一样处理
      // REQUEST场景下，如果发生了BusinessException，不能阻塞用户的重试
      IdempotenceScenario scenario = attr.getScenario();
      switch (scenario) {
        case MQ:
          throw exceptionCallback(storageCenter, param, scenario, arguments, ConsumeStage.CONSUME, "Biz consumption error.", e);
        case REQUEST:
          try {
            RetryUtils.retryWhenException(() -> storageCenter.delete(param), param);
          } catch (Throwable e1) {
            throw exceptionCallback(storageCenter, param, scenario, arguments, ConsumeStage.CONSUME_DELETE,
                    "Biz consumption interrupt and deletion error.", e1);
          }
          throw e;
        default: // unreachable
          return null;
      }
    } catch (Throwable e) {
      IdempotenceScenario scenario = attr.getScenario();
      throw exceptionCallback(storageCenter, param, scenario, arguments, ConsumeStage.CONSUME, "Biz consumption error.", e);
    }
    try {
      RetryUtils.retryWhenException(() -> storageCenter.casStatus(param, ConsumeStatus.CONSUMING, ConsumeStatus.SUCCESS, false), param);
      return o;
    } catch (Throwable e) {
      IdempotenceScenario scenario = attr.getScenario();
      throw exceptionCallback(storageCenter, param, scenario, arguments, ConsumeStage.AFTER_CONSUME,
              "Biz consumption success but set SUCCESS status error.", e);
    }
  }

  /**
   * MQ：打印error日志。 <br/>
   * REQUEST：抛BusinessException，通知用户。
   */
  private Object repeatConsume(StorageCenter storageCenter, IdempotenceParamWrapper param, IdempotenceAttr attr, Object[] arguments) throws Throwable {
    IdempotenceScenario scenario = attr.getScenario();
    switch (scenario) {
      case MQ:
        String msg1 = String.format("[%s] has consumed.", param.getFullKey());
        throw exceptionCallback(storageCenter, param, attr.getScenario(), arguments, ConsumeStage.REPEAT_CONSUME, msg1, new ComponentServiceException(msg1));
      case REQUEST:
        String repeatConsumptionMsg = attr.getRepeatConsumptionMsg();
        Properties properties = new Properties();
        properties.put("prefix", attr.getPrefix());
        if (StringUtils.hasLength(attr.getSpEL())) {
          properties.put("spEL", attr.getSpEL());
        }
        properties.put("repeatConsumptionMsg", attr.getRepeatConsumptionMsg());
        properties.put("scenario", attr.getScenario());
        properties.put("storageCenter", attr.getStorageCenter());
        properties.put("consumeMode", attr.getConsumeMode());
        properties.put("timeUnit", attr.getTimeUnit());
        properties.put("expectCost", attr.getExpectCost());
        properties.put("expectCostStr", attr.getExpectCost() + " " + TimeUnitUtils.aliasTimeUnit(attr.getTimeUnit()));
        properties.put("timeout", attr.getTtl());
        properties.put("timeoutStr", attr.getTtl() + " " + TimeUnitUtils.aliasTimeUnit(attr.getTimeUnit()));
        properties.put("key", param.getKey());
        properties.put("simpleKey", param.getSimpleKey());
        properties.put("fullKey", param.getFullKey());
        String msg2 = ValueResolverHelper.resolveDollarPlaceholder(repeatConsumptionMsg, properties);
        throw new ComponentServiceException(msg2);
      default: // unreachable
        return null;
    }
  }

  private Throwable exceptionCallback(StorageCenter storageCenter, IdempotenceParamWrapper param,
                                      IdempotenceScenario scenario, Object[] arguments, ConsumeStage consumeStage, String info, Throwable throwable) {
    try {
      String msg = String.format("Exception occurred in [%s] stage. %s", consumeStage, info);
      storageCenter.saveExceptionLog(param, scenario, arguments, consumeStage, msg, throwable);
      if (scenario == IdempotenceScenario.REQUEST && throwable instanceof ServiceException) {
        return throwable;
      }
      return new IdempotenceException(msg, consumeStage, throwable);
    } finally {
      if (consumeStage == ConsumeStage.BEFORE_CONSUME) {
        try {
          RetryUtils.retryWhenException(() -> storageCenter.delete(param), param);
        } catch (Throwable e) {
          String argsJson = JacksonUtils.toStr(arguments);
          log.error("Post handle exception error. Delete [{}] error. Scenario: [{}]. ConsumeMode: [{}]. ConsumeStage: [{}]. Arguments: [{}]. XId: [{}].",
                  param.getFullKey(), scenario, param.getConsumeMode(), consumeStage, argsJson, param.getXId(), e);
        }
      } else if (consumeStage == ConsumeStage.CONSUME) {
        try {
          RetryUtils.retryWhenException(() -> storageCenter.casStatus(param, ConsumeStatus.CONSUMING, ConsumeStatus.EXCEPTION, false), param);
        } catch (Throwable e) {
          String argsJson = JacksonUtils.toStr(arguments);
          log.error("Post handle exception error. CAS [{}] CONSUMING to EXCEPTION error. Scenario: [{}]. ConsumeMode: [{}]. ConsumeStage: [{}]. Arguments: [{}]. XId: [{}]."
                  , param.getFullKey(), scenario, param.getConsumeMode(), consumeStage, argsJson, param.getXId(), e);
        }
      } else if (consumeStage == ConsumeStage.BEFORE_RETRY) {
        try {
          RetryUtils.retryWhenException(() -> storageCenter.casStatus(param, ConsumeStatus.CONSUMING, ConsumeStatus.EXCEPTION, false), param);
        } catch (Throwable e) {
          String argsJson = JacksonUtils.toStr(arguments);
          log.error("Post handle exception error. Reset [{}] CONSUMING to EXCEPTION error. Scenario: [{}]. ConsumeMode: [{}]. ConsumeStage: [{}]. Arguments: [{}]. XId: [{}]."
                  , param.getFullKey(), scenario, param.getConsumeMode(), consumeStage, argsJson, param.getXId(), e);
        }
      }
    }
  }

  private String resolve(String value) {
    if (StringUtils.hasText(value)) {
      return this.environment.resolvePlaceholders(value);
    }
    return value;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.namespace = resolve(namespace);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
