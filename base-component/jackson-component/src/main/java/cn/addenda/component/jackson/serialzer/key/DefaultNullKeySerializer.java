package cn.addenda.component.jackson.serialzer.key;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;

public class DefaultNullKeySerializer extends JsonSerializer<Object> {

  @Override
  @SneakyThrows
  public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused) {
    jsonGenerator.writeFieldName("null");
  }

}
