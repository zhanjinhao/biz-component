package cn.addenda.component.argreslog;

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
public class ArgResLogAdvisor extends AbstractBeanFactoryPointcutAdvisor {

  @Override
  public Pointcut getPointcut() {
    return new ArgResLogPointcut();
  }

  public static class ArgResLogPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
      ArgResLog annotation = AnnotationUtils.findAnnotation(targetClass, ArgResLog.class);
      if (annotation == null) {
        Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        annotation = AnnotationUtils.findAnnotation(actualMethod, ArgResLog.class);
      }

      return annotation != null;
    }
  }


}
