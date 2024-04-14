package cn.addenda.component.jackson.util;

import cn.addenda.component.jackson.deserialzer.LocalDateStrDeSerializer;
import cn.addenda.component.jackson.deserialzer.LocalDateTimeStrDeSerializer;
import cn.addenda.component.jackson.deserialzer.LocalTimeStrDeSerializer;
import cn.addenda.component.jackson.deserialzer.key.LocalDateStrKeyDeSerializer;
import cn.addenda.component.jackson.deserialzer.key.LocalDateTimeStrKeyDeSerializer;
import cn.addenda.component.jackson.deserialzer.key.LocalTimeStrKeyDeSerializer;
import cn.addenda.component.jackson.serialzer.LocalDateStrSerializer;
import cn.addenda.component.jackson.serialzer.LocalDateTimeStrSerializer;
import cn.addenda.component.jackson.serialzer.LocalTimeStrSerializer;
import cn.addenda.component.jackson.serialzer.key.DefaultNullKeySerializer;
import cn.addenda.component.jackson.serialzer.key.LocalDateStrKeySerializer;
import cn.addenda.component.jackson.serialzer.key.LocalDateTimeStrKeySerializer;
import cn.addenda.component.jackson.serialzer.key.LocalTimeStrKeySerializer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2022/2/7 12:38
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonUtils {

  private static final ObjectMapper BASIC = new ObjectMapper();
  private static final ObjectMapper TRIM_NULL = new ObjectMapper();

  static {
    BASIC.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    BASIC.registerModule(new Jdk8DateTimeModule());
    BASIC.getSerializerProvider().setNullKeySerializer(new DefaultNullKeySerializer());

    TRIM_NULL.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    TRIM_NULL.setSerializationInclusion(Include.NON_NULL);
    TRIM_NULL.registerModule(new Jdk8DateTimeModule());
    TRIM_NULL.getSerializerProvider().setNullKeySerializer(new DefaultNullKeySerializer());
  }

  @SneakyThrows
  public static String toStr(ObjectMapper objectMapper, Object input) {
    if (null == input) {
      return null;
    }

    return objectMapper.writeValueAsString(input);
  }

  @SneakyThrows
  public static String toStr(Object input) {
    if (null == input) {
      return null;
    }

    return toStr(BASIC, input);
  }

  @SneakyThrows
  public static String toStr(Object input, String... ignoreProperties) {
    if (null == input) {
      return null;
    }
    ObjectMapper objectMapper = cloneBasicMapper();
    Set<String> ignorePropertySet = Arrays.stream(ignoreProperties).collect(Collectors.toSet());
    objectMapper.setSerializerFactory(objectMapper.getSerializerFactory()
            .withSerializerModifier(new IgnorePropertiesBeanSerializerModifier(ignorePropertySet)));
    return toStr(objectMapper, input);
  }

  @SneakyThrows
  public static <T> T toObj(String inputJson, TypeReference<T> reference) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }

    return toObj(BASIC, inputJson, reference);
  }

  @SneakyThrows
  public static <T> T toObj(String inputJson, Class<T> clazz) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }

    return toObj(BASIC, inputJson, TypeFactoryUtils.construct(clazz));
  }

  @SneakyThrows
  public static <T> T toObj(String inputJson, JavaType type) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }

    return toObj(BASIC, inputJson, type);
  }

  @SneakyThrows
  public static <T> T toObj(ObjectMapper objectMapper, String inputJson, TypeReference<T> targetType) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }

    return objectMapper.readValue(inputJson, targetType);
  }

  @SneakyThrows
  public static <T> T toObj(ObjectMapper objectMapper, String inputJson, Class<T> clazz) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }

    return objectMapper.readValue(inputJson, TypeFactoryUtils.construct(clazz));
  }

  @SneakyThrows
  public static <T> T toObj(ObjectMapper objectMapper, String inputJson, JavaType type) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }

    return objectMapper.readValue(inputJson, type);
  }

  @SneakyThrows
  public static String formatJson(String content) {
    Object obj = BASIC.readValue(content, Object.class);
    return BASIC.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
  }

  @SneakyThrows
  public static String trimNull(String content) {
    Object o = BASIC.readValue(content, Object.class);
    return toStr(TRIM_NULL, o);
  }

  public static ObjectMapper cloneBasicMapper() {
    return BASIC.copy();
  }

  public static ObjectMapper cloneTrimNullMapper() {
    return TRIM_NULL.copy();
  }

  private static class IgnorePropertiesBeanSerializerModifier extends BeanSerializerModifier {

    private final Set<String> ignoreProperties;

    public IgnorePropertiesBeanSerializerModifier(Set<String> ignoreProperties) {
      this.ignoreProperties = ignoreProperties;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(
            SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
      beanProperties.removeIf(writer -> ignoreProperties.contains(writer.getName()));
      return beanProperties;
    }
  }

  private static class Jdk8DateTimeModule extends SimpleModule {

    public Jdk8DateTimeModule() {
      addSerializer(LocalDateTime.class, new LocalDateTimeStrSerializer());
      addDeserializer(LocalDateTime.class, new LocalDateTimeStrDeSerializer());
      addKeySerializer(LocalDateTime.class, new LocalDateTimeStrKeySerializer());
      addKeyDeserializer(LocalDateTime.class, new LocalDateTimeStrKeyDeSerializer());

      addSerializer(LocalDate.class, new LocalDateStrSerializer());
      addDeserializer(LocalDate.class, new LocalDateStrDeSerializer());
      addKeySerializer(LocalDate.class, new LocalDateStrKeySerializer());
      addKeyDeserializer(LocalDate.class, new LocalDateStrKeyDeSerializer());

      addSerializer(LocalTime.class, new LocalTimeStrSerializer());
      addDeserializer(LocalTime.class, new LocalTimeStrDeSerializer());
      addKeySerializer(LocalTime.class, new LocalTimeStrKeySerializer());
      addKeyDeserializer(LocalTime.class, new LocalTimeStrKeyDeSerializer());
    }

  }

}
