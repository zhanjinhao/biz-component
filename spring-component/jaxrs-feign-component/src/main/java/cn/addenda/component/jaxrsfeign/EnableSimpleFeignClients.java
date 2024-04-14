package cn.addenda.component.jaxrsfeign;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/7/9 12:15
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(SimpleFeignClientsRegistrar.class)
public @interface EnableSimpleFeignClients {

  String[] basePackages() default {};

}
