package cn.addenda.component.common.jackson.serializer.key;

import cn.addenda.component.common.util.datetime.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalDateTimeTsKeySerializer extends JsonSerializer<LocalDateTime> {

  @Override
  public void serialize(LocalDateTime localDateTime, JsonGenerator jgen, SerializerProvider provider)
          throws IOException {
    if (localDateTime == null) {
      jgen.writeFieldName("null");
      return;
    }
    jgen.writeFieldName(String.valueOf(DateUtils.localDateTimeToTimestamp(localDateTime)));
  }

}
