package cn.addenda.component.common.jackson.util;

import cn.addenda.component.common.jackson.JacksonException;
import cn.addenda.component.common.jackson.deserializer.LocalDateDeserializer;
import cn.addenda.component.common.jackson.deserializer.LocalDateTimeDeserializer;
import cn.addenda.component.common.jackson.deserializer.LocalTimeDeserializer;
import cn.addenda.component.common.jackson.deserializer.key.LocalDateKeyDeserializer;
import cn.addenda.component.common.jackson.deserializer.key.LocalDateTimeKeyDeserializer;
import cn.addenda.component.common.jackson.deserializer.key.LocalTimeKeyDeserializer;
import cn.addenda.component.common.jackson.serializer.StrSerializer;
import cn.addenda.component.common.util.datetime.DateUtils;
import cn.addenda.component.common.jackson.serializer.key.DefaultNullKeySerializer;
import cn.addenda.component.common.jackson.serializer.key.StrKeySerializer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

  public static String toStr(ObjectMapper objectMapper, Object input) {
    if (null == input) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(input);
    } catch (Exception e) {
      throw new JacksonException(e);
    }
  }

  public static String toStr(Object input) {
    if (null == input) {
      return null;
    }
    return toStr(BASIC, input);
  }

  /**
   * Serialize with specified properties excluded.
   * <p>Each call clones the {@code BASIC} mapper and injects a
   * {@link BeanSerializerModifier}. Suitable for one-off API response
   * trimming, not for hot loops.
   */
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

  public static <T> T toObj(String inputJson, TypeReference<T> reference) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }
    return toObj(BASIC, inputJson, reference);
  }

  public static <T> T toObj(String inputJson, Class<T> clazz) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }
    return toObj(BASIC, inputJson, TypeFactoryUtils.construct(clazz));
  }

  public static <T> T toObj(String inputJson, JavaType type) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }
    return toObj(BASIC, inputJson, type);
  }

  public static <T> T toObj(ObjectMapper objectMapper, String inputJson, TypeReference<T> targetType) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }
    try {
      return objectMapper.readValue(inputJson, targetType);
    } catch (Exception e) {
      throw new JacksonException(e);
    }
  }

  public static <T> T toObj(ObjectMapper objectMapper, String inputJson, Class<T> clazz) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }
    try {
      return objectMapper.readValue(inputJson, TypeFactoryUtils.construct(clazz));
    } catch (Exception e) {
      throw new JacksonException(e);
    }
  }

  public static <T> T toObj(ObjectMapper objectMapper, String inputJson, JavaType type) {
    if (inputJson == null || inputJson.isEmpty()) {
      return null;
    }
    try {
      return objectMapper.readValue(inputJson, type);
    } catch (Exception e) {
      throw new JacksonException(e);
    }
  }

  public static String formatJson(String content) {
    if (null == content) {
      return null;
    }
    try {
      Object obj = BASIC.readValue(content, Object.class);
      return BASIC.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (Exception e) {
      throw new JacksonException(e);
    }
  }

  public static String trimNull(String content) {
    if (null == content) {
      return null;
    }
    try {
      Object o = BASIC.readValue(content, Object.class);
      return toStr(TRIM_NULL, o);
    } catch (Exception e) {
      throw new JacksonException(e);
    }
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
      addSerializer(LocalDateTime.class, new StrSerializer(DateUtils.yMdHmsS_FMT));
      addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
      addKeySerializer(LocalDateTime.class, new StrKeySerializer(DateUtils.yMdHmsS_FMT));
      addKeyDeserializer(LocalDateTime.class, new LocalDateTimeKeyDeserializer());

      addSerializer(LocalDate.class, new StrSerializer(DateUtils.yMd_FMT));
      addDeserializer(LocalDate.class, new LocalDateDeserializer());
      addKeySerializer(LocalDate.class, new StrKeySerializer(DateUtils.yMd_FMT));
      addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer());

      addSerializer(LocalTime.class, new StrSerializer(DateUtils.HmsS_FMT));
      addDeserializer(LocalTime.class, new LocalTimeDeserializer());
      addKeySerializer(LocalTime.class, new StrKeySerializer(DateUtils.HmsS_FMT));
      addKeyDeserializer(LocalTime.class, new LocalTimeKeyDeserializer());
    }
  }

}
