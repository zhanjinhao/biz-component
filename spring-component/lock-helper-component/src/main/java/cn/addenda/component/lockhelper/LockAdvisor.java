package cn.addenda.component.lockhelper;

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
public class LockAdvisor extends AbstractBeanFactoryPointcutAdvisor {

  @Override
  public Pointcut getPointcut() {
    return new LockPointcut();
  }

  public static class LockPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
      Locked locked = AnnotationUtils.findAnnotation(targetClass, Locked.class);
      if (locked == null) {
        Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        locked = AnnotationUtils.findAnnotation(actualMethod, Locked.class);
      }
      return locked != null;
    }

  }

}
