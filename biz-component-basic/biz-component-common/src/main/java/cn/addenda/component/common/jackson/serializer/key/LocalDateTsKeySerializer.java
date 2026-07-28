package cn.addenda.component.common.jackson.serializer.key;

import cn.addenda.component.common.util.datetime.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalDateTsKeySerializer extends JsonSerializer<LocalDate> {

  @Override
  public void serialize(LocalDate localDate, JsonGenerator jgen, SerializerProvider provider)
          throws IOException {
    if (localDate == null) {
      jgen.writeFieldName("null");
      return;
    }
    jgen.writeFieldName(String.valueOf(DateUtils.localDateTimeToTimestamp(localDate.atTime(0, 0))));
  }

}
