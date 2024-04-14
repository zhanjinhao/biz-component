package cn.addenda.component.dynamicdatasource;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/3/2 23:02
 */
public class MultiDataSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

  @Override
  public Pointcut getPointcut() {
    return new MultiDataSourcePointcut();
  }

  public static class MultiDataSourcePointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
      Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
      return AnnotationUtils.findAnnotation(actualMethod, MultiDataSourceKey.class) != null;
    }

  }

}
