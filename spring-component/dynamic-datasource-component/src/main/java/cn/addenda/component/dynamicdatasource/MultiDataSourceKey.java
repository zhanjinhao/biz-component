package cn.addenda.component.dynamicdatasource;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2022/3/3 17:26
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiDataSourceKey {

  String dataSourceName() default MultiDataSourceConstant.DEFAULT;

  String mode() default MultiDataSourceConstant.MASTER;

}
