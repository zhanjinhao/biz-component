package cn.addenda.component.common.jackson.deserializer.key;

import cn.addenda.component.common.jackson.deserializer.DateTimeDeUtils;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalDateTimeKeyDeserializer extends KeyDeserializer {

  @Override
  public Object deserializeKey(String key, DeserializationContext ctxt) {
    return DateTimeDeUtils.parseLdt(key);
  }

}
