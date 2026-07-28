package cn.addenda.component.common.test.jackson;

import cn.addenda.component.common.jackson.JacksonException;
import cn.addenda.component.common.jackson.deserializer.DateTimeDeUtils;
import cn.addenda.component.common.jackson.deserializer.LocalDateTimeDeserializer;
import cn.addenda.component.common.jackson.deserializer.key.LocalDateKeyDeserializer;
import cn.addenda.component.common.jackson.deserializer.key.LocalDateTimeKeyDeserializer;
import cn.addenda.component.common.jackson.deserializer.key.LocalTimeKeyDeserializer;
import cn.addenda.component.common.jackson.serializer.DateTimeFormat;
import cn.addenda.component.common.jackson.serializer.LocalDateTimeTsSerializer;
import cn.addenda.component.common.jackson.serializer.key.DefaultNullKeySerializer;
import cn.addenda.component.common.jackson.serializer.key.LocalDateTsKeySerializer;
import cn.addenda.component.common.jackson.serializer.key.LocalDateTimeTsKeySerializer;
import cn.addenda.component.common.jackson.serializer.key.StrKeySerializer;
import cn.addenda.component.common.jackson.serializer.StrSerializer;
import cn.addenda.component.common.jackson.util.JacksonUtils;
import cn.addenda.component.common.jackson.util.TypeFactoryUtils;
import cn.addenda.component.common.util.datetime.DateUtils;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

class DateTimeJacksonTest {

  // ==================== StrSerializer ====================

  @Test
  void testStrSerializer_ModuleRegistration() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDateTime.class, new StrSerializer(DateUtils.yMdHms_FMT));
    module.addSerializer(LocalDate.class, new StrSerializer(DateUtils.yMd_FMT));
    module.addSerializer(LocalTime.class, new StrSerializer(DateUtils.Hms_FMT));
    mapper.registerModule(module);

    LocalDateTime ldt = LocalDateTime.of(2026, 7, 26, 14, 30, 45);
    String json = mapper.writeValueAsString(ldt);
    Assertions.assertTrue(new ObjectMapper().readTree(json) instanceof TextNode);
    Assertions.assertEquals("2026-07-26 14:30:45", new ObjectMapper().readTree(json).asText());

    LocalDate ld = LocalDate.of(2026, 7, 26);
    Assertions.assertEquals("\"2026-07-26\"", mapper.writeValueAsString(ld));

    LocalTime lt = LocalTime.of(14, 30, 45);
    Assertions.assertEquals("\"14:30:45\"", mapper.writeValueAsString(lt));
  }

  @Test
  void testStrSerializer_DateTimeFormatAnnotation() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDateTime.class, new StrSerializer(DateUtils.yMdHmsS_FMT));
    mapper.registerModule(module);

    AnnBean bean = new AnnBean();
    bean.setDefaultFmt(LocalDateTime.of(2026, 7, 26, 14, 30, 45));
    bean.setSlashFmt(LocalDateTime.of(2026, 7, 26, 14, 30, 45));
    bean.setIsoFmt(LocalDateTime.of(2026, 7, 26, 14, 30, 45));

    String json = mapper.writeValueAsString(bean);
    Assertions.assertTrue(json.contains("\"2026-07-26 14:30:45\""));
    Assertions.assertTrue(json.contains("\"2026/07/26 14:30:45\""));
    Assertions.assertTrue(json.contains("\"2026-07-26T14:30:45\""));
  }

  @Test
  void testStrSerializer_DateTimeFormat_MissingAnnotation() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDateTime.class, new StrSerializer(DateUtils.yMdHms_FMT));
    mapper.registerModule(module);

    NoAnnBean bean = new NoAnnBean();
    bean.setDefaultFmt(LocalDateTime.of(2026, 7, 26, 14, 30, 45));

    Assertions.assertThrows(JsonMappingException.class,
            () -> mapper.writeValueAsString(bean));
  }

  @Test
  void testStrSerializer_NullValue() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDateTime.class, new StrSerializer(DateUtils.yMdHmsS_FMT));
    mapper.registerModule(module);

    String json = mapper.writeValueAsString((LocalDateTime) null);
    Assertions.assertTrue(new ObjectMapper().readTree(json) instanceof NullNode);
  }

  @Test
  void testStrSerializer_NoFormatThrows() {
    ObjectMapper m = new ObjectMapper();
    SimpleModule mod = new SimpleModule();
    mod.addSerializer(LocalTime.class, new StrSerializer());
    m.registerModule(mod);

    Assertions.assertThrows(JsonMappingException.class,
            () -> m.writeValueAsString(LocalTime.now()));
  }

  @Test
  void testStrSerializer_UnsupportedTemporal() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(java.time.Instant.class, new StrSerializer(DateUtils.yMdHmsS_FMT));
    mapper.registerModule(module);

    Assertions.assertThrows(JsonMappingException.class,
            () -> mapper.writeValueAsString(java.time.Instant.now()));
  }

  // ==================== StrKeySerializer ====================

  @Test
  void testStrKeySerializer_ModuleRegistration() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(LocalDate.class, new StrKeySerializer(DateUtils.yMd_FMT));
    module.addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer());
    mapper.registerModule(module);

    Map<LocalDate, String> map = new HashMap<>();
    map.put(LocalDate.of(2026, 7, 26), "val");

    String json = mapper.writeValueAsString(map);
    Assertions.assertEquals("{\"2026-07-26\":\"val\"}", json);

    Map<LocalDate, String> parsed = mapper.readValue(json,
            TypeFactoryUtils.constructMap(LocalDate.class, String.class));
    Assertions.assertEquals("val", parsed.get(LocalDate.of(2026, 7, 26)));
  }

  @Test
  void testStrKeySerializer_NullKey() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(LocalTime.class, new StrKeySerializer(DateUtils.Hms_FMT));
    module.addKeyDeserializer(LocalTime.class, new LocalTimeKeyDeserializer());
    mapper.registerModule(module);
    mapper.getSerializerProvider().setNullKeySerializer(
            new cn.addenda.component.common.jackson.serializer.key.DefaultNullKeySerializer());

    Map<LocalTime, String> map = new HashMap<>();
    map.put(null, "val");

    String json = mapper.writeValueAsString(map);
    Assertions.assertEquals("{\"null\":\"val\"}", json);
  }

  @Test
  void testStrKeySerializer_NoFormatThrows() {
    ObjectMapper m = new ObjectMapper();
    SimpleModule mod = new SimpleModule();
    mod.addKeySerializer(LocalTime.class, new StrKeySerializer());
    m.registerModule(mod);
    Map<LocalTime, String> map = new HashMap<>();
    map.put(LocalTime.now(), "v");

    Assertions.assertThrows(JsonMappingException.class,
            () -> m.writeValueAsString(map));
  }

  // ==================== Deserializer ====================

  @Test
  void testDeserializer_TimestampInput() {
    long ts = DateUtils.localDateTimeToTimestamp(LocalDateTime.of(2026, 7, 26, 14, 30, 45));
    LocalDateTime ldt = DateTimeDeUtils.parseLdt(String.valueOf(ts));
    Assertions.assertEquals(LocalDateTime.of(2026, 7, 26, 14, 30, 45), ldt);

    LocalDate ld = DateTimeDeUtils.parseLd(String.valueOf(ts));
    Assertions.assertEquals(LocalDate.of(2026, 7, 26), ld);
  }

  @Test
  void testDeserializer_CompoundFormat() {
    LocalDateTime ldt = DateTimeDeUtils.parseLdt("2026-07-26 14:30:45");
    Assertions.assertEquals(LocalDateTime.of(2026, 7, 26, 14, 30, 45), ldt);

    ldt = DateTimeDeUtils.parseLdt("2026-07-26 14:30");
    Assertions.assertEquals(LocalDateTime.of(2026, 7, 26, 14, 30, 0), ldt);

    LocalDate ld = DateTimeDeUtils.parseLd("2026-07-26");
    Assertions.assertEquals(LocalDate.of(2026, 7, 26), ld);

    LocalTime lt = DateTimeDeUtils.parseLt("14:30:45.123");
    Assertions.assertEquals(LocalTime.of(14, 30, 45, 123000000), lt);

    lt = DateTimeDeUtils.parseLt("14:30");
    Assertions.assertEquals(LocalTime.of(14, 30, 0), lt);
  }

  @Test
  void testDeserializer_SlashFormat() {
    LocalDateTime ldt = DateTimeDeUtils.parseLdt("2026/07/26 14:30:45");
    Assertions.assertEquals(LocalDateTime.of(2026, 7, 26, 14, 30, 45), ldt);

    LocalDate ld = DateTimeDeUtils.parseLd("2026/07/26");
    Assertions.assertEquals(LocalDate.of(2026, 7, 26), ld);
  }

  @Test
  void testDeserializer_ISOFormat() {
    LocalDateTime ldt = DateTimeDeUtils.parseLdt("2026-07-26T14:30:45");
    Assertions.assertEquals(LocalDateTime.of(2026, 7, 26, 14, 30, 45), ldt);

    ldt = DateTimeDeUtils.parseLdt("2026-07-26T14:30");
    Assertions.assertEquals(LocalDateTime.of(2026, 7, 26, 14, 30, 0), ldt);
  }

  @Test
  void testDeserializer_CompactFormat() {
    // LocalDate compact: 8 chars, won't be treated as timestamp
    LocalDate ld = DateTimeDeUtils.parseLd("20260726");
    Assertions.assertEquals(LocalDate.of(2026, 7, 26), ld);

    // LocalTime compact: 6 chars, won't be treated as timestamp
    LocalTime lt = DateTimeDeUtils.parseLt("143045");
    Assertions.assertEquals(LocalTime.of(14, 30, 45), lt);
  }

  @Test
  void testDeserializer_NullAndEmpty() {
    Assertions.assertNull(DateTimeDeUtils.parseLdt(null));
    Assertions.assertNull(DateTimeDeUtils.parseLdt(""));
    Assertions.assertNull(DateTimeDeUtils.parseLdt("null"));
    Assertions.assertNull(DateTimeDeUtils.parseLd(null));
    Assertions.assertNull(DateTimeDeUtils.parseLt(null));
  }

  @Test
  void testDeserializer_InvalidInputThrows() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> DateTimeDeUtils.parseLdt("not-a-date"));
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> DateTimeDeUtils.parseLd("not-a-date"));
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> DateTimeDeUtils.parseLt("not-a-time"));
  }

  @Test
  void testDeserializer_Roundtrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDateTime.class, new StrSerializer(DateUtils.yMdHmsS_S_FMT));
    module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    mapper.registerModule(module);

    LocalDateTime src = LocalDateTime.of(2026, 7, 26, 14, 30, 45);
    String json = mapper.writeValueAsString(src);
    LocalDateTime back = mapper.readValue(json, LocalDateTime.class);
    Assertions.assertEquals(src, back);
  }

  // ==================== KeyDeserializer ====================

  @Test
  void testKeyDeserializer() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(LocalDate.class, new StrKeySerializer(DateUtils.yMd_S_FMT));
    module.addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer());
    mapper.registerModule(module);

    Map<LocalDate, String> map = new HashMap<>();
    map.put(LocalDate.of(2026, 7, 26), "val");

    String json = mapper.writeValueAsString(map);
    Map<LocalDate, String> back = mapper.readValue(json,
            TypeFactoryUtils.constructMap(LocalDate.class, String.class));
    Assertions.assertEquals(map, back);
  }

  @Test
  void testKeyDeserializer_LocalTime() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(LocalTime.class, new StrKeySerializer(DateUtils.Hms_FMT));
    module.addKeyDeserializer(LocalTime.class, new LocalTimeKeyDeserializer());
    mapper.registerModule(module);

    Map<LocalTime, String> map = new HashMap<>();
    map.put(LocalTime.of(14, 30, 45), "val");

    String json = mapper.writeValueAsString(map);
    Map<LocalTime, String> back = mapper.readValue(json,
            TypeFactoryUtils.constructMap(LocalTime.class, String.class));
    Assertions.assertEquals(map, back);
  }

  @Test
  void testKeyDeserializer_LocalDateTime() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(LocalDateTime.class, new StrKeySerializer(DateUtils.yMdHms_FMT));
    module.addKeyDeserializer(LocalDateTime.class, new LocalDateTimeKeyDeserializer());
    mapper.registerModule(module);

    LocalDateTime key = LocalDateTime.of(2026, 7, 26, 14, 30, 45);
    Map<LocalDateTime, String> map = new HashMap<>();
    map.put(key, "val");

    String json = mapper.writeValueAsString(map);
    Map<LocalDateTime, String> back = mapper.readValue(json,
            TypeFactoryUtils.constructMap(LocalDateTime.class, String.class));
    Assertions.assertEquals(map, back);
  }

  @Test
  void testKeyDeserializer_NullKey() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer());
    mapper.registerModule(module);

    Map<LocalDate, String> back = mapper.readValue("{\"null\":\"val\"}",
            TypeFactoryUtils.constructMap(LocalDate.class, String.class));
    Assertions.assertEquals("val", back.get(null));
  }

  @Test
  void testNullKey_Roundtrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(LocalDate.class, new StrKeySerializer(DateUtils.yMd_FMT));
    module.addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer());
    mapper.registerModule(module);
    mapper.getSerializerProvider().setNullKeySerializer(new DefaultNullKeySerializer());

    Map<LocalDate, String> map = new HashMap<>();
    map.put(LocalDate.of(2026, 7, 26), "a");
    map.put(null, "b");

    String json = mapper.writeValueAsString(map);
    Map<LocalDate, String> back = mapper.readValue(json,
        TypeFactoryUtils.constructMap(LocalDate.class, String.class));

    Assertions.assertEquals(map, back);
  }

  // ==================== JacksonException ====================

  @Test
  void testJacksonException_Wrapping() {
    Assertions.assertThrows(JacksonException.class,
            () -> JacksonUtils.toObj("not-json", Object.class));
  }

  // ==================== Ts Serializer ====================

  @Test
  void testLocalDateTimeTsSerializer_Value() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDateTime.class, new LocalDateTimeTsSerializer());
    module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    mapper.registerModule(module);

    LocalDateTime ldt = LocalDateTime.of(2026, 7, 26, 14, 30, 45);
    String json = mapper.writeValueAsString(ldt);
    Assertions.assertTrue(new ObjectMapper().readTree(json).isNumber());

    LocalDateTime back = mapper.readValue(json, LocalDateTime.class);
    Assertions.assertEquals(ldt, back);
  }

  @Test
  void testLocalDateTimeTsSerializer_Null() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDateTime.class, new LocalDateTimeTsSerializer());
    mapper.registerModule(module);

    String json = mapper.writeValueAsString((LocalDateTime) null);
    Assertions.assertTrue(new ObjectMapper().readTree(json).isNull());
  }

  // ==================== Ts Key Serializer ====================

  @Test
  void testLocalDateTimeTsKeySerializer_Roundtrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(LocalDateTime.class, new LocalDateTimeTsKeySerializer());
    module.addKeyDeserializer(LocalDateTime.class, new LocalDateTimeKeyDeserializer());
    mapper.registerModule(module);

    LocalDateTime key = LocalDateTime.of(2026, 7, 26, 14, 30, 45);
    Map<LocalDateTime, String> map = new HashMap<>();
    map.put(key, "val");

    String json = mapper.writeValueAsString(map);
    Map<LocalDateTime, String> back = mapper.readValue(json,
            TypeFactoryUtils.constructMap(LocalDateTime.class, String.class));
    Assertions.assertEquals(map, back);
  }

  @Test
  void testLocalDateTimeTsKeySerializer_Null() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(LocalDateTime.class, new LocalDateTimeTsKeySerializer());
    mapper.registerModule(module);
    mapper.getSerializerProvider().setNullKeySerializer(new DefaultNullKeySerializer());

    Map<LocalDateTime, String> map = new HashMap<>();
    map.put(null, "val");

    String json = mapper.writeValueAsString(map);
    Assertions.assertEquals("{\"null\":\"val\"}", json);
  }

  @Test
  void testLocalDateTsKeySerializer_Roundtrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addKeySerializer(LocalDate.class, new LocalDateTsKeySerializer());
    module.addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer());
    mapper.registerModule(module);

    LocalDate key = LocalDate.of(2026, 7, 26);
    Map<LocalDate, String> map = new HashMap<>();
    map.put(key, "val");

    String json = mapper.writeValueAsString(map);
    Map<LocalDate, String> back = mapper.readValue(json,
            TypeFactoryUtils.constructMap(LocalDate.class, String.class));
    Assertions.assertEquals(map, back);
  }

  // ==================== DateTimeFormat Contextual ====================

  @Test
  void testDateTimeFormat_Contextual_CustomFormat() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDateTime.class, new StrSerializer(DateUtils.yMdHmsS_FMT));
    mapper.registerModule(module);

    FormatBean bean = new FormatBean();
    bean.setNormal(LocalDateTime.of(2026, 7, 26, 14, 30, 45));
    bean.setSlash(LocalDateTime.of(2026, 7, 26, 14, 30, 45));

    String json = mapper.writeValueAsString(bean);
    Assertions.assertEquals("{\"normal\":\"2026-07-26 14:30:45.000\",\"slash\":\"2026/07/26 14:30:45\"}", json);
  }

  @Test
  void testDateTimeFormat_Contextual_NoAnnotationUsesDefault() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDate.class, new StrSerializer(DateUtils.yMd_FMT));
    mapper.registerModule(module);

    String json = mapper.writeValueAsString(LocalDate.of(2026, 7, 26));
    Assertions.assertEquals("\"2026-07-26\"", json);
  }

  @Setter
  @Getter
  static class AnnBean {
    @JsonSerialize(using = StrSerializer.class)
    @DateTimeFormat(DateUtils.yMdHms_FMT)
    private LocalDateTime defaultFmt;

    @JsonSerialize(using = StrSerializer.class)
    @DateTimeFormat(DateUtils.yMdHms_S_FMT)
    private LocalDateTime slashFmt;

    @JsonSerialize(using = StrSerializer.class)
    @DateTimeFormat(DateUtils.yMdHms_T_FMT)
    private LocalDateTime isoFmt;
  }

  @Setter
  @Getter
  static class NoAnnBean {
    @JsonSerialize(using = StrSerializer.class)
    private LocalDateTime defaultFmt;
  }

  @Setter
  @Getter
  static class FormatBean {
    private LocalDateTime normal;

    @JsonSerialize(using = StrSerializer.class)
    @DateTimeFormat("yyyy/MM/dd HH:mm:ss")
    private LocalDateTime slash;
  }

}
