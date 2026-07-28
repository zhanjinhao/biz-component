package cn.addenda.component.common.jackson.serializer.key;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DefaultNullKeySerializer extends JsonSerializer<Object> {

  @Override
  public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused)
          throws IOException {
    jsonGenerator.writeFieldName("null");
  }

}
