package cn.addenda.component.basemybatis.util;

import cn.addenda.component.basemybatis.BaseMybatisException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InstanceUtils {

  public static <T> T newInstance(String clazzName, Class<T> tClass) {
    try {
      Class<?> aClass = Class.forName(clazzName);
      if (!tClass.isAssignableFrom(aClass)) {
        String msg = String.format("初始化失败，期望类型是[%s]，当前是[%s]", tClass.getName(), aClass);
        throw new BaseMybatisException(msg);
      }

      // 如果存在单例方法，优先取单例方法。
      Method[] methods = aClass.getMethods();
      for (Method method : methods) {
        if (method.getName().equals("getInstance") && Modifier.isStatic(method.getModifiers()) &&
                method.getParameterCount() == 0 && tClass.isAssignableFrom(method.getReturnType())) {
          return (T) method.invoke(null);
        }
      }

      // 如果不存在单例方法，取默认构造函数
      return (T) aClass.newInstance();
    } catch (Exception e) {
      String msg = String.format("初始化失败，期望类型[%s]，当前是[%s]", tClass.getName(), clazzName);
      throw new BaseMybatisException(msg, e);
    }
  }

}
