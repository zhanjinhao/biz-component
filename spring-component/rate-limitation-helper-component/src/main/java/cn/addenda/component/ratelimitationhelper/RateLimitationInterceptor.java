package cn.addenda.component.ratelimitationhelper;

import cn.addenda.component.basaspring.util.AnnotationUtils;
import cn.addenda.component.jdk.allocator.NamedExpiredAllocator;
import cn.addenda.component.jdk.util.ExceptionUtils;
import cn.addenda.component.ratelimiter.RateLimiter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.List;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
public class RateLimitationInterceptor extends RateLimitationSupport implements MethodInterceptor {

  public RateLimitationInterceptor(List<? extends NamedExpiredAllocator<? extends RateLimiter>> rateLimiterList) {
    super(rateLimiterList);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    RateLimited annotation = AnnotationUtils.extractAnnotation(invocation, RateLimited.class);

    if (annotation == null) {
      return invocation.proceed();
    }

    RateLimitationAttr attr = RateLimitationAttr.builder()
            .prefix(annotation.prefix())
            .spEL(annotation.spEL())
            .timeUnit(annotation.timeUnit())
            .ttl(annotation.ttl())
            .rateLimiterAllocator(annotation.rateLimiterAllocator())
            .rateLimitedMsg(annotation.rateLimitedMsg())
            .build();

    try {
      return invokeWithinRateLimitation(attr, invocation.getArguments(), invocation::proceed, invocation.getMethod());
    } catch (Throwable throwable) {
      throw ExceptionUtils.unwrapThrowable(throwable);
    }
  }

}
