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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonSerialize(using = Unary.UnarySerializer.class)
@JsonDeserialize(using = Unary.UnaryDeserializer.class)
public class Unary<T1> implements Serializable, Tuple {

  private static final long serialVersionUID = 1L;

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

  public static <T1> Unary<T1> of(T1 t1) {
    return new Unary<>(t1);
  }

  static class UnarySerializer<T1> extends JsonSerializer<Unary<T1>> {

    @Override
    public void serialize(Unary<T1> unary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("f1", unary.getF1());
      jsonGenerator.writeEndObject();
    }
  }

  static class UnaryDeserializer extends JsonDeserializer<Unary<?>>
          implements ContextualDeserializer {

    private JavaType type1;

    public UnaryDeserializer() {
    }

    private UnaryDeserializer(JavaType type1) {
      this.type1 = type1;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
      JavaType type = property != null ? property.getType() : ctxt.getContextualType();
      JavaType t1 = type != null && type.containedTypeCount() > 0 ? type.containedType(0) : null;
      if (t1 == null) {
        t1 = ctxt.constructType(Object.class);
      }
      return new UnaryDeserializer(t1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Unary<?> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
      JsonNode node = p.readValueAsTree();
      Unary<Object> unary = new Unary<>();
      if (node.has("f1") && !node.get("f1").isNull()) {
        unary.setF1(ctxt.readTreeAsValue(node.get("f1"), type1));
      }
      return unary;
    }
  }

}
