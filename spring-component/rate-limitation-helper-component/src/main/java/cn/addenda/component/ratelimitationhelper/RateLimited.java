package cn.addenda.component.ratelimitationhelper;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/8/26 23:05
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {

    /**
     * {@link RateLimitationAttr#getPrefix()}
     */
    String prefix() default RateLimitationAttr.DEFAULT_PREFIX;

    /**
     * {@link RateLimitationAttr#getSpEL()}
     */
    String spEL() default "";

    /**
     * {@link RateLimitationAttr#getTimeUnit()}
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * {@link RateLimitationAttr#getTtl()}
     */
    long ttl() default RateLimitationAttr.DEFAULT_TTL;

    /**
     * {@link RateLimitationAttr#getRateLimiterAllocator()}
     */
    String rateLimiterAllocator();

    /**
     * {@link RateLimitationAttr#getRateLimitedMsg()}
     */
    String rateLimitedMsg() default RateLimitationAttr.DEFAULT_RATE_LIMITED_MSG;

}
