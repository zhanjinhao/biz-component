package cn.addenda.component.basaspring.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/2/5 16:20
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotationUtils {

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

  public static <T extends Annotation> T extractAnnotation(MethodInvocation invocation, Class<T> clazz) {
    Class<?> aClass = invocation.getThis().getClass();
    T annotation = org.springframework.core.annotation.AnnotationUtils.findAnnotation(aClass, clazz);
    if (annotation == null) {
      Method method = org.springframework.aop.support.AopUtils.getMostSpecificMethod(invocation.getMethod(), aClass);
      annotation = org.springframework.core.annotation.AnnotationUtils.findAnnotation(method, clazz);
    }
    return annotation;
  }

  public static <T extends Annotation> T extractAnnotationFromMethodOrClass(Method method, Class<?> targetClass, Class<T> clazz) {
    T annotation = org.springframework.core.annotation.AnnotationUtils.findAnnotation(targetClass, clazz);
    if (annotation == null) {
      Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
      annotation = org.springframework.core.annotation.AnnotationUtils.findAnnotation(actualMethod, clazz);
    }
    return annotation;
  }

}
