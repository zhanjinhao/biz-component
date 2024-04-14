package cn.addenda.component.ratelimitationhelper;

import cn.addenda.component.basaspring.util.AnnotationUtils;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/9/29 13:52
 */
public class RateLimitationAdvisor extends AbstractBeanFactoryPointcutAdvisor {

  @Override
  public Pointcut getPointcut() {
    return new RateLimitationPointcut();
  }

  public static class RateLimitationPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
      return AnnotationUtils.extractAnnotationFromMethodOrClass(method, targetClass, RateLimited.class) != null;
    }
  }

}
