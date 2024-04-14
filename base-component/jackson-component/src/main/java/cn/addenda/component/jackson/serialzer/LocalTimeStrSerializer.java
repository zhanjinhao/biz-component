package cn.addenda.component.jackson.serialzer;

import cn.addenda.component.jdk.util.my.MyDateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;

import java.time.LocalTime;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalTimeStrSerializer extends JsonSerializer<LocalTime> {

  @Override
  @SneakyThrows
  public void serialize(LocalTime localTime, JsonGenerator jgen, SerializerProvider provider) {
    if (localTime == null) {
      jgen.writeString((String) null);
      return;
    }
    jgen.writeString(MyDateUtils.format(localTime, MyDateUtils.HMSS_FORMATTER));
  }

}
