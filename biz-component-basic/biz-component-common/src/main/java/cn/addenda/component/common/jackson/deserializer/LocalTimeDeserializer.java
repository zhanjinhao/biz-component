package cn.addenda.component.common.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalTime;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

  @Override
  public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt)
          throws IOException {
    JsonNode jsonNode = jp.getCodec().readTree(jp);
    return DateTimeDeUtils.parseLt(jsonNode.asText());
  }

}
