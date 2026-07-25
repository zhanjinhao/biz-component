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
 * 三元
 *
 * @author addenda
 * @since 2023/1/22 13:46
 */
@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonSerialize(using = Ternary.TernarySerializer.class)
@JsonDeserialize(using = Ternary.TernaryDeserializer.class)
public class Ternary<T1, T2, T3> implements Serializable, Tuple {

  private static final long serialVersionUID = 1L;

  private T1 f1;
  private T2 f2;
  private T3 f3;

  @Override
  public boolean equals(Object source) {
    if (this == source) return true;
    if (source == null || getClass() != source.getClass()) return false;
    Ternary<?, ?, ?> ternary = (Ternary<?, ?, ?>) source;
    return Objects.equals(f1, ternary.f1)
            && Objects.equals(f2, ternary.f2)
            && Objects.equals(f3, ternary.f3);
  }

  @Override
  public int hashCode() {
    return Objects.hash(f1, f2, f3);
  }

  public static <T1, T2, T3> Ternary<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
    return new Ternary<>(t1, t2, t3);
  }

  static class TernarySerializer<T1, T2, T3> extends JsonSerializer<Ternary<T1, T2, T3>> {

    @Override
    public void serialize(Ternary<T1, T2, T3> ternary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("f1", ternary.getF1());
      jsonGenerator.writeObjectField("f2", ternary.getF2());
      jsonGenerator.writeObjectField("f3", ternary.getF3());
      jsonGenerator.writeEndObject();
    }
  }

  static class TernaryDeserializer extends JsonDeserializer<Ternary<?, ?, ?>>
          implements ContextualDeserializer {

    private JavaType type1;
    private JavaType type2;
    private JavaType type3;

    public TernaryDeserializer() {
    }

    private TernaryDeserializer(JavaType type1, JavaType type2, JavaType type3) {
      this.type1 = type1;
      this.type2 = type2;
      this.type3 = type3;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
      JavaType type = property != null ? property.getType() : ctxt.getContextualType();
      JavaType t1 = type != null && type.containedTypeCount() > 0 ? type.containedType(0) : null;
      JavaType t2 = type != null && type.containedTypeCount() > 1 ? type.containedType(1) : null;
      JavaType t3 = type != null && type.containedTypeCount() > 2 ? type.containedType(2) : null;
      if (t1 == null) {
        t1 = ctxt.constructType(Object.class);
      }
      if (t2 == null) {
        t2 = ctxt.constructType(Object.class);
      }
      if (t3 == null) {
        t3 = ctxt.constructType(Object.class);
      }
      return new TernaryDeserializer(t1, t2, t3);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Ternary<?, ?, ?> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
      JsonNode node = p.readValueAsTree();
      Ternary<Object, Object, Object> ternary = new Ternary<>();
      if (node.has("f1") && !node.get("f1").isNull()) {
        ternary.setF1(ctxt.readTreeAsValue(node.get("f1"), type1));
      }
      if (node.has("f2") && !node.get("f2").isNull()) {
        ternary.setF2(ctxt.readTreeAsValue(node.get("f2"), type2));
      }
      if (node.has("f3") && !node.get("f3").isNull()) {
        ternary.setF3(ctxt.readTreeAsValue(node.get("f3"), type3));
      }
      return ternary;
    }
  }

}
