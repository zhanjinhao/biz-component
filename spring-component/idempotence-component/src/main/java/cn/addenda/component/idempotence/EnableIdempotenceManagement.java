package cn.addenda.component.idempotence;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2022/9/29 13:55
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(IdempotenceSelector.class)
public @interface EnableIdempotenceManagement {

  String namespace() default "idempotence";

  int order() default Ordered.LOWEST_PRECEDENCE;

  boolean proxyTargetClass() default false;

  AdviceMode mode() default AdviceMode.PROXY;

}
