package cn.addenda.component.argreslog;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2022/9/29 14:00
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArgResLog {

}
