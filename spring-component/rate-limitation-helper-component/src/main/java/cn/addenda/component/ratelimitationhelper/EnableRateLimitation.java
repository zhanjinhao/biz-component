package cn.addenda.component.ratelimitationhelper;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/8/26 21:48
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RateLimitationSelector.class)
public @interface EnableRateLimitation {

  String namespace() default "rateLimitation";

  int order() default Ordered.LOWEST_PRECEDENCE;

  boolean proxyTargetClass() default false;

  AdviceMode mode() default AdviceMode.PROXY;

}