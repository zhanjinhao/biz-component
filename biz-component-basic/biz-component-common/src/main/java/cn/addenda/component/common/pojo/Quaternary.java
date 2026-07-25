package cn.addenda.component.common.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.*;

import java.io.IOException;
import java.io.Serializable;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonSerialize(using = Quaternary.QuaternarySerializer.class)
@JsonDeserialize(using = Quaternary.QuaternaryDeserializer.class)
public class Quaternary<T1, T2, T3, T4> implements Serializable, Tuple {

  private static final long serialVersionUID = 1L;

  private T1 f1;
  private T2 f2;
  private T3 f3;
  private T4 f4;

  @Override
  public boolean equals(Object source) {
    if (this == source) return true;
    if (source == null || getClass() != source.getClass()) return false;
    Quaternary<?, ?, ?, ?> that = (Quaternary<?, ?, ?, ?>) source;
    return Objects.equals(f1, that.f1)
            && Objects.equals(f2, that.f2)
            && Objects.equals(f3, that.f3)
            && Objects.equals(f4, that.f4);
  }

  @Override
  public int hashCode() {
    return Objects.hash(f1, f2, f3, f4);
  }

  public static <T1, T2, T3, T4> Quaternary<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
    return new Quaternary<>(t1, t2, t3, t4);
  }

  static class QuaternarySerializer<T1, T2, T3, T4> extends JsonSerializer<Quaternary<T1, T2, T3, T4>> {

    @Override
    public void serialize(Quaternary<T1, T2, T3, T4> quaternary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("f1", quaternary.getF1());
      jsonGenerator.writeObjectField("f2", quaternary.getF2());
      jsonGenerator.writeObjectField("f3", quaternary.getF3());
      jsonGenerator.writeObjectField("f4", quaternary.getF4());
      jsonGenerator.writeEndObject();
    }
  }

  static class QuaternaryDeserializer extends JsonDeserializer<Quaternary<?, ?, ?, ?>>
          implements ContextualDeserializer {

    private JavaType type1;
    private JavaType type2;
    private JavaType type3;
    private JavaType type4;

    public QuaternaryDeserializer() {
    }

    private QuaternaryDeserializer(JavaType type1, JavaType type2, JavaType type3, JavaType type4) {
      this.type1 = type1;
      this.type2 = type2;
      this.type3 = type3;
      this.type4 = type4;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
      JavaType type = property != null ? property.getType() : ctxt.getContextualType();
      JavaType t1 = type != null && type.containedTypeCount() > 0 ? type.containedType(0) : null;
      JavaType t2 = type != null && type.containedTypeCount() > 1 ? type.containedType(1) : null;
      JavaType t3 = type != null && type.containedTypeCount() > 2 ? type.containedType(2) : null;
      JavaType t4 = type != null && type.containedTypeCount() > 3 ? type.containedType(3) : null;
      if (t1 == null) {
        t1 = ctxt.constructType(Object.class);
      }
      if (t2 == null) {
        t2 = ctxt.constructType(Object.class);
      }
      if (t3 == null) {
        t3 = ctxt.constructType(Object.class);
      }
      if (t4 == null) {
        t4 = ctxt.constructType(Object.class);
      }
      return new QuaternaryDeserializer(t1, t2, t3, t4);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Quaternary<?, ?, ?, ?> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
      JsonNode node = p.readValueAsTree();
      Quaternary<Object, Object, Object, Object> quaternary = new Quaternary<>();
      if (node.has("f1") && !node.get("f1").isNull()) {
        quaternary.setF1(ctxt.readTreeAsValue(node.get("f1"), type1));
      }
      if (node.has("f2") && !node.get("f2").isNull()) {
        quaternary.setF2(ctxt.readTreeAsValue(node.get("f2"), type2));
      }
      if (node.has("f3") && !node.get("f3").isNull()) {
        quaternary.setF3(ctxt.readTreeAsValue(node.get("f3"), type3));
      }
      if (node.has("f4") && !node.get("f4").isNull()) {
        quaternary.setF4(ctxt.readTreeAsValue(node.get("f4"), type4));
      }
      return quaternary;
    }
  }

}
