package cn.addenda.component.bean.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * 二元
 *
 * @author addenda
 * @since 2023/1/21 16:00
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = Binary.BinarySerializer.class)
public class Binary<T1, T2> implements Map.Entry<T1, T2> {

  private T1 f1;

  private T2 f2;

  @Override
  public T1 getKey() {
    return f1;
  }

  @Override
  public T2 getValue() {
    return f2;
  }

  @Override
  public T2 setValue(T2 value) {
    T2 t2 = this.f2;
    this.f2 = value;
    return t2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Binary)) return false;
    Binary<?, ?> binary = (Binary<?, ?>) o;
    return Objects.equals(f1, binary.f1) && Objects.equals(f2, binary.f2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(f1, f2);
  }

  static class BinarySerializer<T1, T2> extends JsonSerializer<Binary<T1, T2>> {

    @Override
    public void serialize(Binary<T1, T2> binary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("f1", binary.getF1());
      jsonGenerator.writeObjectField("f2", binary.getF2());
      jsonGenerator.writeEndObject();
    }
  }

}
