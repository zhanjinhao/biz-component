package cn.addenda.component.common.jackson.serializer;

import cn.addenda.component.common.util.datetime.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class StrSerializer extends JsonSerializer<Temporal> implements ContextualSerializer {

  private final String fmt;

  public StrSerializer() {
    this.fmt = null;
  }

  public StrSerializer(String fmt) {
    this.fmt = fmt;
  }

  @Override
  public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
    if (property == null) {
      return this;
    }
    DateTimeFormat ann = property.getAnnotation(DateTimeFormat.class);
    if (ann == null) {
      return this;
    }
    return new StrSerializer(ann.value());
  }

  @Override
  public void serialize(Temporal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    if (value == null) {
      jgen.writeString((String) null);
      return;
    }
    if (fmt == null) {
      throw new IllegalStateException("No format configured for StrSerializer.");
    }
    if (value instanceof LocalDateTime) {
      jgen.writeString(DateUtils.format((LocalDateTime) value, fmt));
    } else if (value instanceof LocalDate) {
      jgen.writeString(DateUtils.format((LocalDate) value, fmt));
    } else if (value instanceof LocalTime) {
      jgen.writeString(DateUtils.format((LocalTime) value, fmt));
    } else {
      throw new IllegalArgumentException(
              "Unsupported temporal type: " + value.getClass().getName() + ".");
    }
  }

}
