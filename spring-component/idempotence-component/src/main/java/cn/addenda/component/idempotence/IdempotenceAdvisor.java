package cn.addenda.component.idempotence;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/9/29 13:52
 */
public class IdempotenceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

  @Override
  public Pointcut getPointcut() {
    return new IdempotencePointcut();
  }

  public static class IdempotencePointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
      Idempotent annotation = AnnotationUtils.findAnnotation(targetClass, Idempotent.class);
      if (annotation == null) {
        Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        annotation = AnnotationUtils.findAnnotation(actualMethod, Idempotent.class);
      }

      return annotation != null;
    }

  }

}
