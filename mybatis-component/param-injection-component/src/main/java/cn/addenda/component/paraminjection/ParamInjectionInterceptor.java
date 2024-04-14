package cn.addenda.component.paraminjection;

import cn.addenda.component.bean.pojo.Binary;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.ibatis.executor.keygen.SelectKeyGenerator.SELECT_KEY_SUFFIX;

/**
 * 只有被 {@link ParamInjected} 注释的方法会被处理。所以不会对存量功能造成影响。
 *
 * @author addenda
 * @since 2023/6/4 10:19
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "queryCursor",
                args = {MappedStatement.class, Object.class, RowBounds.class})
})
@Slf4j
public class ParamInjectionInterceptor implements Interceptor {

  private static final Set<String> POJO_CLASS_PREFIX_SET = new HashSet<>();

  private static final Map<String, ParamInjected[]> FIELD_STRATEGY_CONTROLLER_MAP = new ConcurrentHashMap<>();
  private static final Map<String, Method> METHOD_MAP = new ConcurrentHashMap<>();

  private static final String EL_EVALUATOR_NAME = "paramInjectionELEvaluator";
  private ParamInjectionELEvaluator paramInjectionELEvaluator;

  public ParamInjectionInterceptor(ParamInjectionELEvaluator paramInjectionELEvaluator) {
    this.paramInjectionELEvaluator = paramInjectionELEvaluator;
  }

  public ParamInjectionInterceptor() {
  }

  /**
   * Mybatis动态代理的入口：{@link MapperProxy#invoke(Object, Method, Object[])}。<br/>
   * 会调用到MapperMethod的：{@link MapperMethod#execute(SqlSession, Object[])}。<br/>
   * 原始的参数被{@link ParamNameResolver#getNamedParams(Object[])}处理之后传入
   * <ul>
   * <li>{@link Executor#update(MappedStatement, Object)}</li>
   * <li>{@link Executor#query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)}</li>
   * <li>{@link Executor#query(MappedStatement, Object, RowBounds, ResultHandler)}</li>
   * <li>{@link Executor#queryCursor(MappedStatement, Object, RowBounds)}</li>
   * </ul>
   */
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    Object[] args = invocation.getArgs();
    MappedStatement mappedStatement = (MappedStatement) args[0];
    String msId = mappedStatement.getId();
    ParamInjectionJdbc3KeyGenerator paramInjectionJdbc3KeyGenerator = null;
    ParamInjected[] paramInjecteds = extractFieldStrategyController(msId);
    if (paramInjecteds != null && paramInjecteds.length != 0) {
      // 观察org.apache.ibatis.reflection.ParamNameResolver.getNamedParams的实现可知，返回值一共有三种类型
      // - 没有参数时返回null
      // - 有一个参数且没有@Param注解时返回参数本身
      // - 返回MapperMethod.ParamMap
      Object arg = args[1];
      if (arg == null) {
        MapperMethod.ParamMap<Object> paramMap = new MapperMethod.ParamMap<>();
        for (ParamInjected paramInjected : paramInjecteds) {
          String name = paramInjected.name();
          paramMap.put(name, evaluateEL(paramInjected.el(), null));
        }
        args[1] = paramMap;
      } else {
        if (arg instanceof MapperMethod.ParamMap) {
          MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap<Object>) arg;
          for (ParamInjected paramInjected : paramInjecteds) {
            String name = paramInjected.name();
            paramMap.put(name, evaluateEL(paramInjected.el(), arg));
          }
        }
        // 一个参数且没有@Param时走这个地方
        else {
          ParamInjectionParamMap<Object> paramMap = new ParamInjectionParamMap<>();
          for (int i = 0; i < paramInjecteds.length; i++) {
            ParamInjected paramInjected = paramInjecteds[i];
            paramMap.put(paramInjected.name(), evaluateEL(paramInjected.el(), arg));
          }

          /**
           * 如果参数是pojo，利用MetaObject将pojo的属性写入Map
           */
          Class<?> clazz = arg.getClass();
          String className = clazz.getName();
          if (matchPojoPrefix(className)) {
            // 没被@IgnoreFlatMap标注 且 有getter的属性才会被flatMap
            Set<String> fieldNameSet = flatMap(clazz);
            MetaObject metaObject = SystemMetaObject.forObject(arg);
            List<String> getterNames = Arrays.stream(metaObject.getGetterNames()).collect(Collectors.toList());
            getterNames.retainAll(fieldNameSet);
            for (String getterName : getterNames) {
              Object value = metaObject.getValue(getterName);
              paramMap.put(getterName, value);
            }
          }

          paramMap.put(ParamInjectionParamMap.ORIGINAL_PARAM_NAME, arg);
          paramMap.setOriginalParam(arg);
          args[1] = paramMap;
        }
      }

      // 对于batch insert且有keyProperties的场景，需要使用EasyCodeJdbc3KeyGenerator执行
      Object target = invocation.getTarget();
      if (target instanceof BatchExecutor && SqlCommandType.INSERT == mappedStatement.getSqlCommandType() && mappedStatement.getKeyProperties() != null) {
        // 每次都新创建一个MappedStatement
        Binary<MappedStatement, ParamInjectionJdbc3KeyGenerator> binary = cloneMappedStatement(mappedStatement);
        args[0] = binary.getF1();
        paramInjectionJdbc3KeyGenerator = binary.getF2();
      }
    }

    try {
      return invocation.proceed();
    } finally {
      if (paramInjectionJdbc3KeyGenerator != null) {
        paramInjectionJdbc3KeyGenerator.close();
      }
    }
  }

  private Set<String> flatMap(Class<?> clazz) {
    Set<String> result = new HashSet<>();
    ReflectionUtils.doWithFields(clazz, new FieldCallback() {
      @Override
      public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        IgnoreFlatMap annotation = field.getAnnotation(IgnoreFlatMap.class);
        if (annotation == null) {
          result.add(field.getName());
        }
      }
    });
    return result;
  }

  private Object evaluateEL(String el, Object argument) {
    try {
      return paramInjectionELEvaluator.evaluate(el, argument);
    } catch (Exception e) {
      throw new ParamInjectionException(String.format("Evaluate el[%s] with argument[%s] exception!", el, argument), e);
    }
  }

  public Binary<MappedStatement, ParamInjectionJdbc3KeyGenerator> cloneMappedStatement(MappedStatement ms) {
    MappedStatement.Builder builder =
            new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), ms.getSqlSource(), ms.getSqlCommandType());
    builder.resource(ms.getResource());
    builder.parameterMap(ms.getParameterMap());
    builder.resultMaps(ms.getResultMaps());
    builder.fetchSize(ms.getFetchSize());
    builder.timeout(ms.getTimeout());
    builder.statementType(ms.getStatementType());
    builder.resultSetType(ms.getResultSetType());
    builder.cache(ms.getCache());
    builder.flushCacheRequired(ms.isFlushCacheRequired());
    builder.useCache(ms.isUseCache());
    builder.resultOrdered(ms.isResultOrdered());

    ParamInjectionJdbc3KeyGenerator paramInjectionJdbc3KeyGenerator = new ParamInjectionJdbc3KeyGenerator();
    builder.keyGenerator(paramInjectionJdbc3KeyGenerator);
    String[] keyProperties = ms.getKeyProperties();
    if (keyProperties != null) {
      builder.keyProperty(String.join(",", keyProperties));
    }
    String[] keyColumns = ms.getKeyColumns();
    if (keyColumns != null) {
      builder.keyColumn(String.join(",", keyColumns));
    }

    builder.databaseId(ms.getDatabaseId());
    builder.lang(ms.getLang());
    String[] resultSets = ms.getResultSets();
    if (resultSets != null) {
      builder.resultSets(String.join(",", resultSets));
    }

    return new Binary<>(builder.build(), paramInjectionJdbc3KeyGenerator);
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
    String pojoClassPrefix = (String) properties.get("pojoClassPrefix");
    if (StringUtils.hasLength(pojoClassPrefix)) {
      POJO_CLASS_PREFIX_SET.addAll(Arrays.stream(pojoClassPrefix.split(",")).collect(Collectors.toSet()));
    }
    if (paramInjectionELEvaluator == null) {
      if (properties.containsKey(EL_EVALUATOR_NAME)) {
        String paramInjectionELEvaluatorClassName = (String) properties.get(EL_EVALUATOR_NAME);
        if (paramInjectionELEvaluatorClassName != null) {
          paramInjectionELEvaluator = newInstance(paramInjectionELEvaluatorClassName);
        }
      } else {
        String msg = String.format("[%s] 初始化失败，paramInjectionELEvaluator不能为空！", ParamInjectionInterceptor.class.getName());
        throw new ParamInjectionException(msg);
      }
    }
  }

  private ParamInjectionELEvaluator newInstance(String clazzName) {
    try {
      Class<?> aClass = Class.forName(clazzName);
      if (!ParamInjectionELEvaluator.class.isAssignableFrom(aClass)) {
        String msg = String.format("[%s] 初始化失败，paramInjectionELEvaluator的类型应该是[%s]，当前是[%s]", ParamInjectionInterceptor.class.getName(), ParamInjectionELEvaluator.class.getName(), aClass);
        throw new ParamInjectionException(msg);
      }

      // 如果ParamInjectionELEvaluator存在单例方法，优先取单例方法。
      Method[] methods = aClass.getMethods();
      for (Method method : methods) {
        if (method.getName().equals("getInstance") && Modifier.isStatic(method.getModifiers()) &&
                method.getParameterCount() == 0 && ParamInjectionELEvaluator.class.isAssignableFrom(method.getReturnType())) {
          return (ParamInjectionELEvaluator) method.invoke(null);
        }
      }

      // 如果不存在单例方法，取默认构造函数
      return (ParamInjectionELEvaluator) aClass.newInstance();
    } catch (Exception e) {
      String msg = String.format("[%s] 初始化失败，paramInjectionELEvaluator的类型应该是[%s]，当前是[%s]", ParamInjectionInterceptor.class.getName(), ParamInjectionELEvaluator.class.getName(), clazzName);
      throw new ParamInjectionException(msg, e);
    }
  }

  private boolean matchPojoPrefix(String className) {
    for (String prefix : POJO_CLASS_PREFIX_SET) {
      if (className.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }

  private Method extractMethod(String msId) {
    if (msId.endsWith(SELECT_KEY_SUFFIX)) {
      return null;
    }
    return METHOD_MAP.computeIfAbsent(msId,
            new Function<String, Method>() {
              @Override
              @SneakyThrows
              public Method apply(String s) {
                int end = msId.lastIndexOf(".");
                Class<?> aClass = Class.forName(msId.substring(0, end));
                String methodName = msId.substring(end + 1);
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                  // mybatis 动态代理模式不支持函数重载。用方法名匹配没问题。
                  if (method.getName().equals(methodName)) {
                    return method;
                  }
                }
                throw new ParamInjectionException(String.format("无法从MappedStatement[%s]中提取出来方法！", msId));
              }
            });
  }

  private ParamInjected[] extractFieldStrategyController(String msId) {
    if (msId.endsWith(SELECT_KEY_SUFFIX)) {
      return null;
    }
    return FIELD_STRATEGY_CONTROLLER_MAP.computeIfAbsent(msId,
            s -> {
              Method method = extractMethod(msId);
              Assert.notNull(method, "unExcepted exception!");
              return method.getAnnotationsByType(ParamInjected.class);
            });
  }

}
