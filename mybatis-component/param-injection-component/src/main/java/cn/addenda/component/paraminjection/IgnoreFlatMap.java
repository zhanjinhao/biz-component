package cn.addenda.component.paraminjection;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/4 23:19
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreFlatMap {

  boolean name() default true;

}
