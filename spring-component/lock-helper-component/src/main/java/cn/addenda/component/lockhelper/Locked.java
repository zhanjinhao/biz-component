package cn.addenda.component.lockhelper;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2022/9/29 14:00
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Locked {

  /**
   * {@link LockedAttr#getPrefix()}
   */
  String prefix() default LockedAttr.DEFAULT_PREFIX;

  /**
   * {@link LockedAttr#getSpEL()}
   */
  String spEL() default "";

  /**
   * {@link LockedAttr#getLockFailedMsg()}
   */
  String lockFailedMsg() default LockedAttr.DEFAULT_LOCK_FAILED_MSG;

  /**
   * {@link LockedAttr#getTimeUnit()}
   */
  TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

  /**
   * {@link LockedAttr#getWaitTime()}
   */
  long waitTime() default LockedAttr.DEFAULT_WAIT_TIME;

  /**
   * {@link LockedAttr#isRejectServiceException()}
   */
  boolean rejectServiceException() default LockedAttr.DEFAULT_REJECT_SERVICE_EXCEPTION;

}
