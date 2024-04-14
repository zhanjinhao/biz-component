package cn.addenda.component.idempotence;

import cn.addenda.component.convention.util.ExceptionUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
public class IdempotenceInterceptor extends IdempotenceSupport implements MethodInterceptor {

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Class<?> aClass = invocation.getThis().getClass();
    Idempotent annotation = AnnotationUtils.findAnnotation(aClass, Idempotent.class);
    if (annotation == null) {
      Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), aClass);
      annotation = AnnotationUtils.findAnnotation(method, Idempotent.class);
    }
    if (annotation == null) {
      return invocation.proceed();
    }

    IdempotenceAttr attr = IdempotenceAttr.builder()
            .prefix(annotation.prefix())
            .spEL(annotation.spEL())
            .repeatConsumptionMsg(annotation.repeatConsumptionMsg())
            .scenario(annotation.scenario())
            .storageCenter(annotation.storageCenter())
            .consumeMode(annotation.consumeMode())
            .timeUnit(annotation.timeUnit())
            .expectCost(annotation.expectCost())
            .ttl(annotation.ttl())
            .build();

    try {
      return invokeWithinIdempotence(attr, invocation.getArguments(), invocation::proceed, invocation.getMethod());
    } catch (Throwable throwable) {
      throw ExceptionUtils.unwrapThrowable(throwable);
    }
  }

}
