package cn.addenda.component.bean.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.IOException;
import java.util.Objects;

/**
 * 一元
 *
 * @author addenda
 * @since 2023/1/22 13:47
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = Unary.UnarySerializer.class)
public class Unary<T1> {

  private T1 f1;

  @Override
  public boolean equals(Object source) {
    if (this == source) return true;
    if (source == null || getClass() != source.getClass()) return false;
    Unary<?> unary = (Unary<?>) source;
    return Objects.equals(f1, unary.f1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(f1);
  }

  static class UnarySerializer<T1> extends JsonSerializer<Unary<T1>> {

    @Override
    public void serialize(Unary<T1> unary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("f1", unary.getF1());
      jsonGenerator.writeEndObject();
    }
  }

}
