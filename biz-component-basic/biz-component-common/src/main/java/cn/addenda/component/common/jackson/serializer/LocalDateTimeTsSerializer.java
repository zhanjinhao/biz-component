package cn.addenda.component.common.jackson.serializer;

import cn.addenda.component.common.util.datetime.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalDateTimeTsSerializer extends JsonSerializer<LocalDateTime> {

  @Override
  public void serialize(LocalDateTime localDateTime, JsonGenerator jgen, SerializerProvider provider)
          throws IOException {
    if (localDateTime == null) {
      jgen.writeNumber((BigInteger) null);
      return;
    }
    jgen.writeNumber(DateUtils.localDateTimeToTimestamp(localDateTime));
  }

}
