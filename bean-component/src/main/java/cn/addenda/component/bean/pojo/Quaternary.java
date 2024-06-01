package cn.addenda.component.bean.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.IOException;
import java.util.Objects;

/**
 * 四元
 *
 * @author addenda
 * @since 2023/1/22 13:46
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = Quaternary.QuaternarySerializer.class)
public class Quaternary<T1, T2, T3, T4> {

  private T1 f1;
  private T2 f2;
  private T3 f3;
  private T4 f4;

  @Override
  public boolean equals(Object source) {
    if (this == source) return true;
    if (source == null || getClass() != source.getClass()) return false;
    Quaternary<?, ?, ?, ?> that = (Quaternary<?, ?, ?, ?>) source;
    return Objects.equals(f1, that.f1) && Objects.equals(f2, that.f2) && Objects.equals(f3, that.f3) && Objects.equals(f4, that.f4);
  }

  @Override
  public int hashCode() {
    return Objects.hash(f1, f2, f3, f4);
  }

  static class QuaternarySerializer<T1, T2, T3, T4> extends JsonSerializer<Quaternary<T1, T2, T3, T4>> {

    @Override
    public void serialize(Quaternary<T1, T2, T3, T4> quaternary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("f1", quaternary.getF1());
      jsonGenerator.writeObjectField("f2", quaternary.getF2());
      jsonGenerator.writeObjectField("f3", quaternary.getF3());
      jsonGenerator.writeObjectField("f4", quaternary.getF4());
      jsonGenerator.writeEndObject();
    }
  }

}
