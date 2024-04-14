package cn.addenda.component.paraminjection;

import org.apache.ibatis.annotations.Param;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/4 23:19
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ParamInjectedContainer.class)
public @interface ParamInjected {

  /**
   * 注入的param的名称，等同于 {@link Param}
   */
  String name();

  /**
   * 提取value的EL
   */
  String el();

}
