package cn.addenda.component.bean.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.IOException;
import java.util.Objects;

/**
 * 三元
 *
 * @author addenda
 * @since 2023/1/22 13:46
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = Ternary.TernarySerializer.class)
public class Ternary<T1, T2, T3> {

  private T1 f1;
  private T2 f2;
  private T3 f3;

  @Override
  public boolean equals(Object source) {
    if (this == source) return true;
    if (source == null || getClass() != source.getClass()) return false;
    Ternary<?, ?, ?> ternary = (Ternary<?, ?, ?>) source;
    return Objects.equals(f1, ternary.f1) && Objects.equals(f2, ternary.f2) && Objects.equals(f3, ternary.f3);
  }

  @Override
  public int hashCode() {
    return Objects.hash(f1, f2, f3);
  }

  static class TernarySerializer<T1, T2, T3> extends JsonSerializer<Ternary<T1, T2, T3>> {

    @Override
    public void serialize(Ternary<T1, T2, T3> ternary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("f1", ternary.getF1());
      jsonGenerator.writeObjectField("f2", ternary.getF2());
      jsonGenerator.writeObjectField("f3", ternary.getF3());
      jsonGenerator.writeEndObject();
    }
  }

}
