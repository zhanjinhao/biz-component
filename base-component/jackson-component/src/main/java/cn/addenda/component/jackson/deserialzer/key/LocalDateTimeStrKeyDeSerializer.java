package cn.addenda.component.jackson.deserialzer.key;

import cn.addenda.component.jdk.util.my.MyDateUtils;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import lombok.SneakyThrows;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalDateTimeStrKeyDeSerializer extends KeyDeserializer {

  @Override
  @SneakyThrows
  public Object deserializeKey(String s, DeserializationContext ctxt) {
    if (s == null || s.isEmpty() || "null".equals(s)) {
      return null;
    }
    return MyDateUtils.parseLdt(s, MyDateUtils.FULL_FORMATTER);
  }

}
