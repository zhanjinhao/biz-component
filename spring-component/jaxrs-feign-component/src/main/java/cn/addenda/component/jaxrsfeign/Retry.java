package cn.addenda.component.jaxrsfeign;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2024/4/3 16:54
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

  long interval();

  TimeUnit timeunit();

  int maxAttempts();

}
