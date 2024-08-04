package cn.addenda.component.lockhelper;

import cn.addenda.component.jdk.util.ExceptionUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
public class LockMethodInterceptor extends LockAspectSupport implements MethodInterceptor {

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Class<?> aClass = invocation.getThis().getClass();
    Locked locked = AnnotationUtils.findAnnotation(aClass, Locked.class);
    if (locked == null) {
      Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), aClass);
      locked = AnnotationUtils.findAnnotation(method, Locked.class);
    }
    if (locked == null) {
      return invocation.proceed();
    }

    LockedAttr attribute = LockedAttr.builder()
            .spEL(locked.spEL())
            .lockFailedMsg(locked.lockFailedMsg())
            .prefix(locked.prefix())
            .timeUnit(locked.timeUnit())
            .waitTime(locked.waitTime())
            .rejectServiceException(locked.rejectServiceException())
            .build();

    try {
      return invokeWithinLock(attribute, invocation.getArguments(), invocation::proceed, invocation.getMethod());
    } catch (Throwable throwable) {
      throw ExceptionUtils.unwrapThrowable(throwable);
    }
  }

}
