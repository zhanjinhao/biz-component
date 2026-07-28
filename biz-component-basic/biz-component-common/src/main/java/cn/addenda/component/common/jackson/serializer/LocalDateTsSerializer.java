package cn.addenda.component.common.jackson.serializer;

import cn.addenda.component.common.util.datetime.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalDateTsSerializer extends JsonSerializer<LocalDate> {

  @Override
  public void serialize(LocalDate localDate, JsonGenerator jgen, SerializerProvider provider)
          throws IOException {
    if (localDate == null) {
      jgen.writeNumber((BigInteger) null);
      return;
    }
    jgen.writeNumber(DateUtils.localDateTimeToTimestamp(localDate.atTime(0, 0)));
  }

}
