package cn.addenda.component.common.test.jackson;

import cn.addenda.component.common.jackson.deserializer.LocalDateDeserializer;
import cn.addenda.component.common.jackson.serializer.LocalDateTsSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class LocalDateTsSerializerTest {

  /**
   * fix: LocalDateTsSerializer 非 null 分支使用了 writeFieldName 而非 writeNumber，
   * 导致输出非法 JSON field name 而非数字时间戳。
   */
  @Test
  void testSerialize_NonNull_WritesNumber() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDate.class, new LocalDateTsSerializer());
    mapper.registerModule(module);

    LocalDate date = LocalDate.of(2026, 7, 26);
    String json = mapper.writeValueAsString(date);

    // 反解析 JSON 验证输出的是数字节点，而非字符串或 field name
    ObjectMapper verifyMapper = new ObjectMapper();
    com.fasterxml.jackson.databind.JsonNode node = verifyMapper.readTree(json);
    Assertions.assertTrue(node instanceof NumericNode,
            "LocalDateTsSerializer 应输出 JSON 数字（时间戳），实际: " + json);
  }

  @Test
  void testSerialize_Null_WritesNull() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDate.class, new LocalDateTsSerializer());
    mapper.registerModule(module);

    String json = mapper.writeValueAsString((LocalDate) null);

    com.fasterxml.jackson.databind.JsonNode node = new ObjectMapper().readTree(json);
    Assertions.assertTrue(node instanceof NullNode,
            "LocalDateTsSerializer null 应输出 JSON null，实际: " + json);
  }

  @Test
  void testSerialize_Roundtrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDate.class, new LocalDateTsSerializer());
    module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
    mapper.registerModule(module);

    LocalDate date = LocalDate.of(2026, 7, 26);
    String json = mapper.writeValueAsString(date);
    LocalDate parsed = mapper.readValue(json, LocalDate.class);

    Assertions.assertEquals(date, parsed);
  }

  @Test
  void testAnnotation_Roundtrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper();

    TsDateBean bean = new TsDateBean();
    bean.setDate(LocalDate.of(2026, 7, 26));

    String json = mapper.writeValueAsString(bean);
    TsDateBean parsed = mapper.readValue(json, TsDateBean.class);

    Assertions.assertEquals(bean.getDate(), parsed.getDate());

    // 确认序列化后的 date 字段是数字而非字符串
    com.fasterxml.jackson.databind.JsonNode tree = new ObjectMapper().readTree(json);
    Assertions.assertTrue(tree.get("date") instanceof NumericNode,
            "@JsonSerialize(LocalDateTsSerializer) 应输出数字时间戳，实际: " + json);
  }

  @Setter
  @Getter
  @NoArgsConstructor
  static class TsDateBean {
    @JsonSerialize(using = LocalDateTsSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
  }
}
