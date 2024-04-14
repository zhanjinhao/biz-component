package cn.addenda.component.test.jackson;

import cn.addenda.component.jackson.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;

public class User2KeySerializer extends JsonSerializer<User2<?>> {

  @Override
  @SneakyThrows
  public void serialize(User2<?> value, JsonGenerator gen, SerializerProvider serializers) {
    if (value == null) {
      gen.writeFieldName("null");
      return;
    }
    String s = JacksonUtils.toStr(value);
    gen.writeFieldName(s);
  }

}
