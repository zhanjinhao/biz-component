package cn.addenda.component.idgenerator.annotation;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2022/2/5 15:57
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdScopeController {

    int SUPPRESS = 1;

    int FORCE_INJECT = 2;

    int mode() default SUPPRESS;

}
