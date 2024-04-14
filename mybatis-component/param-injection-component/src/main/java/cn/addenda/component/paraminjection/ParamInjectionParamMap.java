package cn.addenda.component.paraminjection;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod.ParamMap;

/**
 * 只有一个参数且没有{@link Param}的场景会使用@{@link ParamInjectionParamMap}存储参数。
 * 原始的那一个参数的名字由{@link ParamInjectionParamMap#setOriginalParamName(String)} 配置。
 *
 * @author addenda
 * @since 2023/7/6 19:21
 */
public class ParamInjectionParamMap<V> extends ParamMap<V> {

  public static String ORIGINAL_PARAM_NAME = "originalParam";

  @Setter
  @Getter
  private Object originalParam;

  public static void setOriginalParamName(String originalParamName) {
    ORIGINAL_PARAM_NAME = originalParamName;
  }

  @Override
  public V put(String key, V value) {
    if (super.containsKey(key)) {
      V v = get(key);
      throw new ParamInjectionException(
              String.format("Parameter [%s] has existed and its corresponding value is [%s]. Current value is [%s]. All parameters are [%s].", key, v, value, keySet()));
    }
    return super.put(key, value);
  }
}
