package cn.addenda.component.basemybatis.util;


import cn.addenda.component.basemybatis.BaseMybatisException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2023/8/10 19:05
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MsIdUtils {

  public static <T extends Annotation> T extract(String msId, Class<T> tClass) {
    int end = msId.lastIndexOf(".");
    try {
      Class<?> aClass = Class.forName(msId.substring(0, end));
      String methodName = msId.substring(end + 1);
      return extractAnnotationFromMethod(aClass, methodName, tClass);
    } catch (ClassNotFoundException e) {
      String msg = String.format("无法找到对应的Mapper：[%s]。", msId);
      throw new BaseMybatisException(msg, e);
    }
  }

  public static <T extends Annotation> T extractAnnotationFromMethod(Class<?> aClass, String methodName, Class<T> tClazz) {
    Method[] methods = aClass.getMethods();
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        Annotation[] methodAnnotations = method.getAnnotations();
        for (Annotation annotation : methodAnnotations) {
          if (tClazz.isAssignableFrom(annotation.getClass())) {
            return (T) annotation;
          }
        }
      }
    }
    return null;
  }

  public static <T extends Annotation> T extractAnnotationFromClass(Class<?> aClass, Class<T> tClazz) {
    Annotation[] annotations = aClass.getAnnotations();
    for (Annotation annotation : annotations) {
      if (tClazz.isAssignableFrom(annotation.getClass())) {
        return (T) annotation;
      }
    }
    return null;
  }

}
