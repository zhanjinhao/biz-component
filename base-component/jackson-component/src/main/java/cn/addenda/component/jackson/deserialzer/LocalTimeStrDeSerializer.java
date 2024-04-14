package cn.addenda.component.jackson.deserialzer;

import cn.addenda.component.jdk.util.my.MyDateUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

import java.time.LocalTime;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalTimeStrDeSerializer extends JsonDeserializer<LocalTime> {

  @Override
  @SneakyThrows
  public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt) {
    JsonNode jsonNode = jp.getCodec().readTree(jp);
    final String s = jsonNode.asText();
    if (s == null || s.isEmpty() || "null".equals(s)) {
      return null;
    }
    return MyDateUtils.parseLt(s, MyDateUtils.HMSS_FORMATTER);
  }
}
