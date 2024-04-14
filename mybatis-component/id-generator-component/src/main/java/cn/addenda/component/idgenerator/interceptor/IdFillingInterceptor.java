package cn.addenda.component.idgenerator.interceptor;

import cn.addenda.component.basemybatis.util.MsIdUtils;
import cn.addenda.component.idgenerator.IdFillingException;
import cn.addenda.component.idgenerator.annotation.IdScope;
import cn.addenda.component.idgenerator.annotation.IdScopeController;
import cn.addenda.component.idgenerator.idgenerator.IdGenerator;
import lombok.Setter;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * todo 测试sql改写能自动填充主键吗？如果可以，需要下沉到prepareStatement
 *
 * @author addenda
 * @since 2022/2/3 20:17
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class})
})
public class IdFillingInterceptor implements Interceptor {

  private static final String ID_GENERATOR_NAME = "idGenerator";

  @Setter
  private IdGenerator idGenerator;

  private static final Map<String, IdScopeController> ID_SCOPE_CONTROLLER_MAP = new ConcurrentHashMap<>();
  private static final Map<String, IdScope> PARAMETER_ID_SCOPE_MAP = new ConcurrentHashMap<>();

  public IdFillingInterceptor() {
  }

  public IdFillingInterceptor(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  @Override
  public Object intercept(Invocation invocation) throws Throwable {

    Object[] args = invocation.getArgs();
    MappedStatement ms = (MappedStatement) args[0];
    Object parameterObject = args[1];

    String msId = ms.getId();

    if (!SqlCommandType.INSERT.equals(ms.getSqlCommandType())) {
      return invocation.proceed();
    }

    // IdScopeController可以压制注入ID
    IdScopeController idScopeController = extractIdScopeController(msId);

    if (idScopeController != null && IdScopeController.SUPPRESS == idScopeController.mode()) {
      return invocation.proceed();
    }

    if (idScopeController != null && IdScopeController.FORCE_INJECT != idScopeController.mode()) {
      throw new IdFillingException(IdScopeController.class.getName() + "的mode只有两种选项：FORCE_INJECT or SUPPRESS。");
    }

    if (parameterObject instanceof Collection) {
      injectCollection((Collection<?>) parameterObject, idScopeController);
    } else if (parameterObject instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) parameterObject;
      Set<? extends Map.Entry<?, ?>> entries = map.entrySet();
      for (Map.Entry<?, ?> next : entries) {
        Object value = next.getValue();
        if (value instanceof Collection) {
          injectCollection((Collection<?>) value, idScopeController);
        } else {
          injectPojo(value, idScopeController);
        }
      }
    } else {
      injectPojo(parameterObject, idScopeController);
    }
    return invocation.proceed();
  }

  private void injectCollection(Collection<?> collection, IdScopeController idScopeController) {
    for (Object parameter : collection) {
      injectPojo(parameter, idScopeController);
    }
  }

  private void injectPojo(Object object, IdScopeController idScopeController) {
    IdScope idScope = extractIdScopeFromObject(object);
    // 如果实体类上没有IdScope，不注入ID
    if (idScope == null) {
      return;
    }

    String scopeName = idScope.scopeName();
    if (scopeName == null) {
      throw new IdFillingException(IdScope.class.getName() + "的scopeName不能为空！");
    }

    boolean forceFlag = idScopeController != null && IdScopeController.FORCE_INJECT == idScopeController.mode();
    MetaObject metaObject = SystemMetaObject.forObject(object);

    if (forceFlag) {
      metaObject.setValue(idScope.idFieldName(), idGenerator.nextId(scopeName));
    } else {
      Object value = metaObject.getValue(idScope.idFieldName());
      if (value == null) {
        metaObject.setValue(idScope.idFieldName(), idGenerator.nextId(scopeName));
      }
    }
  }

  private IdScope extractIdScopeFromObject(Object object) {
    if (object == null) {
      return null;
    }
    Class<?> aClass = object.getClass();
    String className = aClass.getName();
    return PARAMETER_ID_SCOPE_MAP.computeIfAbsent
            (className, s -> MsIdUtils.extractAnnotationFromClass(aClass, IdScope.class));
  }


  @Override
  public void setProperties(Properties properties) {
    if (idGenerator == null) {
      if (properties.containsKey(ID_GENERATOR_NAME)) {
        String idGeneratorClassName = (String) properties.get(ID_GENERATOR_NAME);
        if (idGeneratorClassName != null) {
          idGenerator = newInstance(idGeneratorClassName);
        }
      } else {
        String msg = String.format("[%s] 初始化失败，idGenerator不能为空！", IdFillingInterceptor.class.getName());
        throw new IdFillingException(msg);
      }
    }
  }


  private IdGenerator newInstance(String clazzName) {
    try {
      Class<?> aClass = Class.forName(clazzName);
      if (!IdGenerator.class.isAssignableFrom(aClass)) {
        String msg = String.format("[%s] 初始化失败，idGenerator的类型应该是[%s]，当前是[%s]", IdFillingInterceptor.class.getName(), IdGenerator.class.getName(), aClass);
        throw new IdFillingException(msg);
      }

      // 如果IdGenerator存在单例方法，优先取单例方法。
      Method[] methods = aClass.getMethods();
      for (Method method : methods) {
        if (method.getName().equals("getInstance") && Modifier.isStatic(method.getModifiers()) &&
                method.getParameterCount() == 0 && IdGenerator.class.isAssignableFrom(method.getReturnType())) {
          return (IdGenerator) method.invoke(null);
        }
      }

      // 如果不存在单例方法，取默认构造函数
      return (IdGenerator) aClass.newInstance();
    } catch (Exception e) {
      String msg = String.format("[%s] 初始化失败，idGenerator的类型应该是[%s]，当前是[%s]", IdFillingInterceptor.class.getName(), IdGenerator.class.getName(), clazzName);
      throw new IdFillingException(msg, e);
    }
  }

  private IdScopeController extractIdScopeController(String msId) {
    return ID_SCOPE_CONTROLLER_MAP.computeIfAbsent(msId,
            s -> MsIdUtils.extract(msId, IdScopeController.class));
  }

}
