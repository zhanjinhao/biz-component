package cn.addenda.component.paraminjection;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParamInjectedContainer {

  ParamInjected[] value();
}
