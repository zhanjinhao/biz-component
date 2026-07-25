package cn.addenda.component.common.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.*;

import java.io.IOException;
import java.io.Serializable;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonSerialize(using = Binary.BinarySerializer.class)
@JsonDeserialize(using = Binary.BinaryDeserializer.class)
public class Binary<T1, T2> implements Map.Entry<T1, T2>, Serializable, Tuple {

  private static final long serialVersionUID = 1L;

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

  public static <T1, T2> Binary<T1, T2> of(T1 t1, T2 t2) {
    return new Binary<>(t1, t2);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Map.Entry)) return false;
    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
    return Objects.equals(getKey(), entry.getKey())
            && Objects.equals(getValue(), entry.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(f1) ^ Objects.hashCode(f2);
  }

  static class BinarySerializer<T1, T2> extends JsonSerializer<Binary<T1, T2>> {

    @Override
    public void serialize(Binary<T1, T2> binary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("f1", binary.getF1());
      jsonGenerator.writeObjectField("f2", binary.getF2());
      jsonGenerator.writeEndObject();
    }
  }

  static class BinaryDeserializer extends JsonDeserializer<Binary<?, ?>>
          implements ContextualDeserializer {

    private JavaType type1;
    private JavaType type2;

    public BinaryDeserializer() {
    }

    private BinaryDeserializer(JavaType type1, JavaType type2) {
      this.type1 = type1;
      this.type2 = type2;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
      JavaType type = property != null ? property.getType() : ctxt.getContextualType();
      JavaType t1 = type != null && type.containedTypeCount() > 0 ? type.containedType(0) : null;
      JavaType t2 = type != null && type.containedTypeCount() > 1 ? type.containedType(1) : null;
      if (t1 == null) {
        t1 = ctxt.constructType(Object.class);
      }
      if (t2 == null) {
        t2 = ctxt.constructType(Object.class);
      }
      return new BinaryDeserializer(t1, t2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Binary<?, ?> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
      JsonNode node = p.readValueAsTree();
      Binary<Object, Object> binary = new Binary<>();
      if (node.has("f1") && !node.get("f1").isNull()) {
        binary.setF1(ctxt.readTreeAsValue(node.get("f1"), type1));
      }
      if (node.has("f2") && !node.get("f2").isNull()) {
        binary.setF2(ctxt.readTreeAsValue(node.get("f2"), type2));
      }
      return binary;
    }
  }

}
