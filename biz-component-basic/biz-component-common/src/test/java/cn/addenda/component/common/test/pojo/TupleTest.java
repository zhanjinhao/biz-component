package cn.addenda.component.common.test.pojo;

import cn.addenda.component.common.pojo.Binary;
import cn.addenda.component.common.pojo.Quaternary;
import cn.addenda.component.common.pojo.Ternary;
import cn.addenda.component.common.pojo.Unary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class TupleTest {

  // ==================================================================
  //  Unary
  // ==================================================================

  @Test
  void testUnary_of() {
    Unary<String> u = Unary.of("hello");
    Assertions.assertEquals("hello", u.getF1());
  }

  @Test
  void testUnary_equals_sameObject() {
    Unary<String> u = Unary.of("hello");
    Assertions.assertEquals(u, u);
  }

  @Test
  void testUnary_equals_equalContent() {
    Unary<String> u1 = Unary.of("hello");
    Unary<String> u2 = Unary.of("hello");
    Assertions.assertEquals(u1, u2);
    Assertions.assertEquals(u2, u1);
  }

  @Test
  void testUnary_equals_differentContent() {
    Unary<String> u1 = Unary.of("hello");
    Unary<String> u2 = Unary.of("world");
    Assertions.assertNotEquals(u1, u2);
  }

  @Test
  void testUnary_equals_null() {
    Unary<String> u = Unary.of("hello");
    Assertions.assertNotEquals(u, null);
  }

  @Test
  void testUnary_equals_differentType() {
    Unary<String> u = Unary.of("hello");
    Assertions.assertNotEquals("hello", u);
  }

  @Test
  void testUnary_equals_nullField() {
    Unary<String> u1 = Unary.of(null);
    Unary<String> u2 = Unary.of(null);
    Assertions.assertEquals(u1, u2);
    Assertions.assertNotEquals(Unary.of("a"), Unary.of(null));
    Assertions.assertNotEquals(Unary.of(null), Unary.of("a"));
  }

  @Test
  void testUnary_hashCode_consistentWithEquals() {
    Unary<String> u1 = Unary.of("hello");
    Unary<String> u2 = Unary.of("hello");
    Assertions.assertEquals(u1.hashCode(), u2.hashCode());
  }

  @Test
  void testUnary_hashCode_nullSafe() {
    Assertions.assertEquals(Unary.of(null).hashCode(), Unary.of(null).hashCode());
    Unary.of(null).hashCode();
  }

  // ==================================================================
  //  Binary — Map.Entry 契约
  // ==================================================================

  @Test
  void testBinary_of() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Assertions.assertEquals("key", b.getKey());
    Assertions.assertEquals(1, (int) b.getValue());
  }

  @Test
  void testBinary_setValue() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Integer old = b.setValue(2);
    Assertions.assertEquals(1, (int) old);
    Assertions.assertEquals(2, (int) b.getValue());
    Assertions.assertEquals("key", b.getKey());
  }

  @Test
  void testBinary_equals_sameObject() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Assertions.assertEquals(b, b);
  }

  @Test
  void testBinary_equals_equalContent() {
    Binary<String, Integer> b1 = Binary.of("key", 1);
    Binary<String, Integer> b2 = Binary.of("key", 1);
    Assertions.assertEquals(b1, b2);
    Assertions.assertEquals(b2, b1);
  }

  @Test
  void testBinary_equals_differentKey() {
    Binary<String, Integer> b1 = Binary.of("key1", 1);
    Binary<String, Integer> b2 = Binary.of("key2", 1);
    Assertions.assertNotEquals(b1, b2);
  }

  @Test
  void testBinary_equals_differentValue() {
    Binary<String, Integer> b1 = Binary.of("key", 1);
    Binary<String, Integer> b2 = Binary.of("key", 2);
    Assertions.assertNotEquals(b1, b2);
  }

  @Test
  void testBinary_equals_null() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Assertions.assertNotEquals(b, null);
  }

  @Test
  void testBinary_equals_nonMapEntry() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Assertions.assertNotEquals("key", b);
  }

  @Test
  void testBinary_equals_nullFields() {
    Binary<String, Integer> b1 = Binary.of(null, null);
    Binary<String, Integer> b2 = Binary.of(null, null);
    Assertions.assertEquals(b1, b2);

    Binary<String, Integer> b3 = Binary.of(null, 1);
    Binary<String, Integer> b4 = Binary.of(null, 2);
    Assertions.assertNotEquals(b3, b4);

    Binary<String, Integer> b5 = Binary.of(null, 1);
    Binary<String, Integer> b6 = Binary.of("key", 1);
    Assertions.assertNotEquals(b5, b6);
  }

  // ---------- Map.Entry 契约：与 non-Binary Map.Entry 比较 ----------

  @Test
  void testBinary_equals_SimpleEntry_sameContent() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>("key", 1);
    Assertions.assertEquals(b, entry);
    Assertions.assertEquals(entry, b);
  }

  @Test
  void testBinary_equals_SimpleEntry_differentKey() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>("other", 1);
    Assertions.assertNotEquals(b, entry);
  }

  @Test
  void testBinary_equals_SimpleEntry_differentValue() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>("key", 2);
    Assertions.assertNotEquals(b, entry);
  }

  @Test
  void testBinary_equals_SimpleEntry_nullKeyValue() {
    Binary<String, Integer> b = Binary.of(null, null);
    Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>(null, null);
    Assertions.assertEquals(b, entry);
  }

  @Test
  void testBinary_equals_customMapEntry() {
    Binary<String, String> b = Binary.of("a", "b");
    Map.Entry<String, String> custom = new Map.Entry<String, String>() {
      @Override
      public String getKey() {
        return "a";
      }

      @Override
      public String getValue() {
        return "b";
      }

      @Override
      public String setValue(String value) {
        throw new UnsupportedOperationException();
      }
    };
    Assertions.assertEquals(b, custom);
  }

  // ---------- hashCode Map.Entry 契约 ----------

  @Test
  void testBinary_hashCode_consistentWithEquals() {
    Binary<String, Integer> b1 = Binary.of("key", 1);
    Binary<String, Integer> b2 = Binary.of("key", 1);
    Assertions.assertEquals(b1.hashCode(), b2.hashCode());
  }

  @Test
  void testBinary_hashCode_matchesMapEntryContract() {
    String key = "key";
    Integer value = 1;
    Binary<String, Integer> b = Binary.of(key, value);
    int expected = (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    Assertions.assertEquals(expected, b.hashCode());
  }

  @Test
  void testBinary_hashCode_matchesMapEntryContract_nullKey() {
    Integer value = 1;
    Binary<String, Integer> b = Binary.of(null, value);
    int expected = 0 ^ value.hashCode();
    Assertions.assertEquals(expected, b.hashCode());
  }

  @Test
  void testBinary_hashCode_matchesMapEntryContract_nullValue() {
    String key = "key";
    Binary<String, Integer> b = Binary.of(key, null);
    int expected = key.hashCode() ^ 0;
    Assertions.assertEquals(expected, b.hashCode());
  }

  @Test
  void testBinary_hashCode_matchesMapEntryContract_nullBoth() {
    Binary<String, Integer> b = Binary.of(null, null);
    Assertions.assertEquals(0, b.hashCode());
  }

  @Test
  void testBinary_hashCode_matchesSimpleEntry() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>("key", 1);
    Assertions.assertEquals(entry.hashCode(), b.hashCode());
  }

  // ---------- HashMap 兼容性 ----------

  @Test
  void testBinary_usedAsHashMapKey() {
    Map<Binary<String, Integer>, String> map = new HashMap<>();
    Binary<String, Integer> b1 = Binary.of("k1", 1);
    map.put(b1, "v1");

    Binary<String, Integer> b2 = Binary.of("k1", 1);
    Assertions.assertEquals("v1", map.get(b2));

    Binary<String, Integer> b3 = Binary.of("k2", 1);
    Assertions.assertNull(map.get(b3));
  }

  @Test
  void testBinary_setValue_null() {
    Binary<String, Integer> b = Binary.of("key", 1);
    Integer old = b.setValue(null);
    Assertions.assertEquals(1, (int) old);
    Assertions.assertNull(b.getValue());
    Assertions.assertEquals("key", b.getKey());
  }

  @Test
  void testBinary_mutationBreaksHashMapLookup() {
    Map<Binary<String, Integer>, String> map = new HashMap<>();
    Binary<String, Integer> b = Binary.of("k", 1);
    map.put(b, "v");
    Assertions.assertEquals("v", map.get(b));

    b.setValue(2);
    Assertions.assertNull(map.get(b));
  }

  // ==================================================================
  //  Ternary
  // ==================================================================

  @Test
  void testTernary_of() {
    Ternary<String, Integer, Boolean> t = Ternary.of("a", 1, true);
    Assertions.assertEquals("a", t.getF1());
    Assertions.assertEquals(1, (int) t.getF2());
    Assertions.assertEquals(true, t.getF3());
  }

  @Test
  void testTernary_equals_sameObject() {
    Ternary<String, Integer, Boolean> t = Ternary.of("a", 1, true);
    Assertions.assertEquals(t, t);
  }

  @Test
  void testTernary_equals_equalContent() {
    Ternary<String, Integer, Boolean> t1 = Ternary.of("a", 1, true);
    Ternary<String, Integer, Boolean> t2 = Ternary.of("a", 1, true);
    Assertions.assertEquals(t1, t2);
    Assertions.assertEquals(t2, t1);
  }

  @Test
  void testTernary_equals_differentF1() {
    Ternary<String, Integer, Boolean> t1 = Ternary.of("a", 1, true);
    Ternary<String, Integer, Boolean> t2 = Ternary.of("b", 1, true);
    Assertions.assertNotEquals(t1, t2);
  }

  @Test
  void testTernary_equals_differentF2() {
    Ternary<String, Integer, Boolean> t1 = Ternary.of("a", 1, true);
    Ternary<String, Integer, Boolean> t2 = Ternary.of("a", 2, true);
    Assertions.assertNotEquals(t1, t2);
  }

  @Test
  void testTernary_equals_differentF3() {
    Ternary<String, Integer, Boolean> t1 = Ternary.of("a", 1, true);
    Ternary<String, Integer, Boolean> t2 = Ternary.of("a", 1, false);
    Assertions.assertNotEquals(t1, t2);
  }

  @Test
  void testTernary_equals_null() {
    Ternary<String, Integer, Boolean> t = Ternary.of("a", 1, true);
    Assertions.assertNotEquals(t, null);
  }

  @Test
  void testTernary_equals_differentType() {
    Ternary<String, Integer, Boolean> t = Ternary.of("a", 1, true);
    Assertions.assertNotEquals("a", t);
  }

  @Test
  void testTernary_equals_nullFields() {
    Ternary<String, Integer, Boolean> t1 = Ternary.of(null, null, null);
    Ternary<String, Integer, Boolean> t2 = Ternary.of(null, null, null);
    Assertions.assertEquals(t1, t2);

    Ternary<String, Integer, Boolean> t3 = Ternary.of("a", null, null);
    Ternary<String, Integer, Boolean> t4 = Ternary.of("a", null, null);
    Assertions.assertEquals(t3, t4);
    Assertions.assertNotEquals(Ternary.of("a", null, null), Ternary.of("b", null, null));
  }

  @Test
  void testTernary_hashCode_consistentWithEquals() {
    Ternary<String, Integer, Boolean> t1 = Ternary.of("a", 1, true);
    Ternary<String, Integer, Boolean> t2 = Ternary.of("a", 1, true);
    Assertions.assertEquals(t1.hashCode(), t2.hashCode());
  }

  @Test
  void testTernary_hashCode_nullSafe() {
    Assertions.assertEquals(
            Ternary.of(null, null, null).hashCode(),
            Ternary.of(null, null, null).hashCode());
    Ternary.of(null, null, null).hashCode();
  }

  // ==================================================================
  //  Quaternary
  // ==================================================================

  @Test
  void testQuaternary_of() {
    Quaternary<String, Integer, Boolean, Double> q = Quaternary.of("a", 1, true, 3.14);
    Assertions.assertEquals("a", q.getF1());
    Assertions.assertEquals(1, (int) q.getF2());
    Assertions.assertEquals(true, q.getF3());
    Assertions.assertEquals(3.14, q.getF4(), 0.001);
  }

  @Test
  void testQuaternary_equals_equalContent() {
    Quaternary<String, Integer, Boolean, Double> q1 = Quaternary.of("a", 1, true, 3.14);
    Quaternary<String, Integer, Boolean, Double> q2 = Quaternary.of("a", 1, true, 3.14);
    Assertions.assertEquals(q1, q2);
  }

  @Test
  void testQuaternary_equals_differentFields() {
    Assertions.assertNotEquals(Quaternary.of("a", 1, true, 3.14), Quaternary.of("b", 1, true, 3.14));
    Assertions.assertNotEquals(Quaternary.of("a", 1, true, 3.14), Quaternary.of("a", 2, true, 3.14));
    Assertions.assertNotEquals(Quaternary.of("a", 1, true, 3.14), Quaternary.of("a", 1, false, 3.14));
    Assertions.assertNotEquals(Quaternary.of("a", 1, true, 3.14), Quaternary.of("a", 1, true, 2.71));
  }

  @Test
  void testQuaternary_equals_null() {
    Assertions.assertNotEquals(Quaternary.of("a", 1, true, 3.14), null);
  }

  @Test
  void testQuaternary_equals_differentType() {
    Assertions.assertNotEquals(Quaternary.of("a", 1, true, 3.14), "string");
  }

  @Test
  void testQuaternary_equals_nullFields() {
    Quaternary<String, Integer, Boolean, Double> q1 = Quaternary.of(null, null, null, null);
    Quaternary<String, Integer, Boolean, Double> q2 = Quaternary.of(null, null, null, null);
    Assertions.assertEquals(q1, q2);
    Assertions.assertNotEquals(Quaternary.of("a", null, null, null), Quaternary.of("b", null, null, null));
  }

  @Test
  void testQuaternary_hashCode_consistentWithEquals() {
    Quaternary<String, Integer, Boolean, Double> q1 = Quaternary.of("a", 1, true, 3.14);
    Quaternary<String, Integer, Boolean, Double> q2 = Quaternary.of("a", 1, true, 3.14);
    Assertions.assertEquals(q1.hashCode(), q2.hashCode());
  }

  @Test
  void testQuaternary_hashCode_nullSafe() {
    Assertions.assertEquals(
            Quaternary.of(null, null, null, null).hashCode(),
            Quaternary.of(null, null, null, null).hashCode());
  }

  @Test
  void testQuaternary_equals_specialDoubles() {
    // NaN.equals(NaN) is true in Double.equals, consistent with Objects.equals
    Quaternary<Double, Double, Double, Double> q1 = Quaternary.of(Double.NaN, 1.0, 2.0, 3.0);
    Quaternary<Double, Double, Double, Double> q2 = Quaternary.of(Double.NaN, 1.0, 2.0, 3.0);
    Assertions.assertEquals(q1, q2);

    // Infinity equals Infinity
    Quaternary<Double, Double, Double, Double> inf1 = Quaternary.of(Double.POSITIVE_INFINITY, 0d, 0d, 0d);
    Quaternary<Double, Double, Double, Double> inf2 = Quaternary.of(Double.POSITIVE_INFINITY, 0d, 0d, 0d);
    Assertions.assertEquals(inf1, inf2);

    // NaN != non-NaN
    Assertions.assertNotEquals(Quaternary.of(Double.NaN, 0d, 0d, 0d), Quaternary.of(0.0, 0d, 0d, 0d));
  }

  // ==================================================================
  //  toString
  // ==================================================================

  @Test
  void testToString() {
    Assertions.assertTrue(Unary.of("hello").toString().contains("hello"));
    Assertions.assertTrue(Binary.of("k", 1).toString().contains("k"));
    Assertions.assertTrue(Ternary.of("a", 1, true).toString().contains("true"));
    Assertions.assertTrue(Quaternary.of("a", 1, true, 3.14).toString().contains("3.14"));
  }

  // ==================================================================
  //  NoArgsConstructor (private, 通过 of() / 反序列化间接验证)
  // ==================================================================

  @Test
  void testNoArgsConstructor_viaOf() {
    // 已验证通过 of() 创建，此处验证 null 字段的 equals/hashCode 一致性
    Unary<String> u = Unary.of(null);
    Assertions.assertNull(u.getF1());
    Assertions.assertEquals(Unary.of(null), Unary.of(null));

    Binary<String, Integer> b = Binary.of(null, null);
    Assertions.assertNull(b.getF1());
    Assertions.assertNull(b.getF2());
    Assertions.assertEquals(Binary.of(null, null), Binary.of(null, null));
  }

  // ==================================================================
  //  Serializable
  // ==================================================================

  @Test
  void testSerializable_unary() {
    Unary<String> original = Unary.of("hello");
    Unary<?> deserialized = serializeAndDeserialize(original);
    Assertions.assertEquals(original, deserialized);
  }

  @Test
  void testSerializable_binary() {
    Binary<String, Integer> original = Binary.of("key", 1);
    Binary<?, ?> deserialized = serializeAndDeserialize(original);
    Assertions.assertEquals(original, deserialized);
  }

  @Test
  void testSerializable_ternary() {
    Ternary<String, Integer, Boolean> original = Ternary.of("a", 1, true);
    Ternary<?, ?, ?> deserialized = serializeAndDeserialize(original);
    Assertions.assertEquals(original, deserialized);
  }

  @Test
  void testSerializable_quaternary() {
    Quaternary<String, Integer, Boolean, Double> original = Quaternary.of("a", 1, true, 3.14);
    Quaternary<?, ?, ?, ?> deserialized = serializeAndDeserialize(original);
    Assertions.assertEquals(original, deserialized);
  }

  // ==================================================================
  //  Jackson JSON 序列化 / 反序列化
  // ==================================================================

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testJsonRoundTrip_unary() {
    Unary<String> original = Unary.of("hello");
    String json = toJson(original);
    Assertions.assertTrue(json.contains("f1"));
    Unary<?> deserialized = fromJson(json, Unary.class);
    Assertions.assertEquals(original, deserialized);
  }

  @Test
  void testJsonRoundTrip_binary() {
    Binary<String, Integer> original = Binary.of("key", 1);
    String json = toJson(original);
    Assertions.assertTrue(json.contains("f1"));
    Assertions.assertTrue(json.contains("f2"));
    Binary<?, ?> deserialized = fromJson(json, Binary.class);
    Assertions.assertEquals(original, deserialized);
  }

  @Test
  void testJsonRoundTrip_ternary() {
    Ternary<String, Integer, Boolean> original = Ternary.of("a", 1, true);
    String json = toJson(original);
    Assertions.assertTrue(json.contains("f3"));
    Ternary<?, ?, ?> deserialized = fromJson(json, Ternary.class);
    Assertions.assertEquals(original, deserialized);
  }

  @Test
  void testJsonRoundTrip_quaternary() {
    Quaternary<String, Integer, Boolean, Double> original = Quaternary.of("a", 1, true, 3.14);
    String json = toJson(original);
    Assertions.assertTrue(json.contains("f4"));
    Quaternary<?, ?, ?, ?> deserialized = fromJson(json, Quaternary.class);
    Assertions.assertEquals(original, deserialized);
  }

  @Test
  void testJsonRoundTrip_withNullFields() {
    Binary<String, Integer> original = Binary.of("key", null);
    Binary<?, ?> deserialized = fromJson(toJson(original), Binary.class);
    Assertions.assertEquals(original, deserialized);

    Ternary<String, Integer, String> original2 = Ternary.of("key", null, null);
    Ternary<?, ?, ?> deserialized2 = fromJson(toJson(original), Ternary.class);
    Assertions.assertEquals(original2, deserialized2);

    Quaternary<String, Integer, Boolean, Double> q = Quaternary.of(null, 1, null, 3.14);
    Quaternary<?, ?, ?, ?> q2 = fromJson(toJson(q), Quaternary.class);
    Assertions.assertEquals(q, q2);
  }

  @Test
  void testJsonRoundTrip_specialDoubles() throws Exception {
    Quaternary<Double, Double, Double, Double> original = Quaternary.of(Double.NaN, 1.0, Double.POSITIVE_INFINITY, 0.0);
    String json = toJson(original);
    Assertions.assertTrue(json.contains("NaN"));
    Assertions.assertTrue(json.contains("Infinity"));

    Quaternary<Double, Double, Double, Double> deserialized = objectMapper.readValue(json,
            new TypeReference<Quaternary<Double, Double, Double, Double>>() {
            });
    Assertions.assertEquals(original, deserialized);
    Assertions.assertTrue(Double.isNaN(deserialized.getF1()));
    Assertions.assertTrue(Double.isInfinite(deserialized.getF3()));
  }

  // ---------- 类型错误 / 不匹配 ----------

  @Test
  void testJson_typeMismatch_withoutTypeReference() throws Exception {
    // 无 TypeReference → 退化为 Object.class → 不抛异常，但类型不对
    String json = "{\"f1\":\"not-a-number\",\"f2\":true}";
    Binary<?, ?> b = objectMapper.readValue(json, Binary.class);
    // f1 应为 Integer 但实际是 String
    Assertions.assertInstanceOf(String.class, b.getF1());
    Assertions.assertEquals("not-a-number", b.getF1());
    Assertions.assertInstanceOf(Boolean.class, b.getF2());
  }

  @Test
  void testJson_typeMismatch_withTypeReference_throws() {
    // 有 TypeReference → Integer.class 无法从字符串 "hello" 反序列化 → 抛异常
    Assertions.assertThrows(JsonMappingException.class, () ->
            objectMapper.readValue("{\"f1\":\"hello\",\"f2\":1}",
                    new TypeReference<Binary<Integer, Integer>>() {
                    }));
  }

  @Test
  void testJson_typeMismatch_nestedTuple_fallback() throws Exception {
    // 嵌套字段给了非对象 JSON → Deserializer 静默处理为全 null
    String json = "{\"f1\":\"outer\",\"f2\":123,\"f3\":true}";
    Ternary<String, Unary<Integer>, Boolean> deserialized = objectMapper.readValue(json,
            new TypeReference<Ternary<String, Unary<Integer>, Boolean>>() {
            });
    Assertions.assertEquals("outer", deserialized.getF1());
    // f2 期望 Unary<Integer> 但输入是数字 → UnaryDeserializer 对非对象节点返回全 null
    Assertions.assertNull(deserialized.getF2().getF1());
    Assertions.assertEquals(true, deserialized.getF3());
  }

  @Test
  void testJsonRoundTrip_emptyJson() {
    Unary<?> u = fromJson("{}", Unary.class);
    Assertions.assertNull(u.getF1());

    Binary<?, ?> b = fromJson("{}", Binary.class);
    Assertions.assertNull(b.getF1());
    Assertions.assertNull(b.getF2());

    Ternary<?, ?, ?> t = fromJson("{}", Ternary.class);
    Assertions.assertNull(t.getF1());
    Assertions.assertNull(t.getF2());
    Assertions.assertNull(t.getF3());

    Quaternary<?, ?, ?, ?> q = fromJson("{}", Quaternary.class);
    Assertions.assertNull(q.getF1());
    Assertions.assertNull(q.getF2());
    Assertions.assertNull(q.getF3());
    Assertions.assertNull(q.getF4());
  }

  @Test
  void testJsonRoundTrip_complexTypes() {
    // 验证 constructType(Object.class) 能正确反序列化集合、嵌套对象等复杂类型
    List<Integer> list = Arrays.asList(1, 2, 3);
    Map<String, Object> map = new HashMap<>();
    map.put("nested", true);

    Binary<List<Integer>, Map<String, Object>> original = Binary.of(list, map);
    String json = toJson(original);
    Assertions.assertTrue(json.contains("[1,2,3]"));
    Assertions.assertTrue(json.contains("nested"));

    Binary<?, ?> deserialized = fromJson(json, Binary.class);
    Assertions.assertEquals(original, deserialized);
    Assertions.assertEquals(list, deserialized.getF1());
    Assertions.assertEquals(map, deserialized.getF2());
  }

  @Test
  void testJsonRoundTrip_nestedTuple() throws Exception {
    // ContextualDeserializer 能从 TypeReference 获取完整泛型，正确反序列化嵌套 Tuple
    Ternary<String, Unary<Integer>, Boolean> original = Ternary.of("outer", Unary.of(42), true);
    String json = toJson(original);

    Ternary<String, Unary<Integer>, Boolean> deserialized = objectMapper.readValue(json,
            new TypeReference<Ternary<String, Unary<Integer>, Boolean>>() {
            });
    Assertions.assertEquals(original, deserialized);
    Assertions.assertEquals(Unary.of(42), deserialized.getF2());
  }

  @Test
  void testJsonRoundTrip_deeplyNested() throws Exception {
    // 深层嵌套：四层，四种 Tuple 全参与
    Ternary<Integer, Integer, Integer> inner3 = Ternary.of(1, 2, 3);
    Quaternary<Ternary<Integer, Integer, Integer>, String, Integer, Long> q1 =
            Quaternary.of(inner3, "str", 100, Long.MAX_VALUE);
    Ternary<Ternary<Integer, Integer, Integer>, Integer, String> t1 =
            Ternary.of(Ternary.of(10, 20, 30), -1, "hello");
    Binary<Binary<Integer, Integer>, String> b1 =
            Binary.of(Binary.of(99, 100), "world");
    Unary<String> u1 = Unary.of("deep");

    Quaternary<Quaternary<Ternary<Integer, Integer, Integer>, String, Integer, Long>,
            Ternary<Ternary<Integer, Integer, Integer>, Integer, String>,
            Binary<Binary<Integer, Integer>, String>,
            Unary<String>> original = Quaternary.of(q1, t1, b1, u1);

    String json = toJson(original);
    Assertions.assertTrue(json.contains("deep"));
    Assertions.assertTrue(json.contains(Long.toString(Long.MAX_VALUE)));

    Quaternary<Quaternary<Ternary<Integer, Integer, Integer>, String, Integer, Long>,
            Ternary<Ternary<Integer, Integer, Integer>, Integer, String>,
            Binary<Binary<Integer, Integer>, String>,
            Unary<String>> deserialized = objectMapper.readValue(json,
            new TypeReference<Quaternary<Quaternary<Ternary<Integer, Integer, Integer>, String, Integer, Long>,
                    Ternary<Ternary<Integer, Integer, Integer>, Integer, String>,
                    Binary<Binary<Integer, Integer>, String>,
                    Unary<String>>>() {
            });

    Assertions.assertEquals(original, deserialized);
  }

  @Test
  void testJsonRoundTrip_deeplyNested_withNulls() throws Exception {
    Ternary<Ternary<Integer, Integer, Integer>, Integer, String> t1 =
            Ternary.of(Ternary.of(10, 20, 30), -1, "hello");
    Binary<Binary<Integer, Integer>, String> b1 =
            Binary.of(Binary.of(99, 100), "world");

    Quaternary<Quaternary<Ternary<Integer, Integer, Integer>, String, Integer, Long>,
            Ternary<Ternary<Integer, Integer, Integer>, Integer, String>,
            Binary<Binary<Integer, Integer>, String>,
            Unary<String>> original = Quaternary.of(null, t1, b1, null);

    Quaternary<Quaternary<Ternary<Integer, Integer, Integer>, String, Integer, Long>,
            Ternary<Ternary<Integer, Integer, Integer>, Integer, String>,
            Binary<Binary<Integer, Integer>, String>,
            Unary<String>> deserialized = objectMapper.readValue(toJson(original),
            new TypeReference<Quaternary<Quaternary<Ternary<Integer, Integer, Integer>, String, Integer, Long>,
                    Ternary<Ternary<Integer, Integer, Integer>, Integer, String>,
                    Binary<Binary<Integer, Integer>, String>,
                    Unary<String>>>() {
            });

    Assertions.assertEquals(original, deserialized);
    Assertions.assertNull(deserialized.getF1());
    Assertions.assertNull(deserialized.getF4());
    Assertions.assertEquals(t1, deserialized.getF2());
    Assertions.assertEquals(b1, deserialized.getF3());
  }

  private static String toJson(Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T fromJson(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T serializeAndDeserialize(T obj) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      new ObjectOutputStream(baos).writeObject(obj);
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      return (T) new ObjectInputStream(bais).readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  // ==================================================================
  //  并发安全
  // ==================================================================

  @Test
  void testDeserializerConcurrency_stressTest() throws Exception {
    int threads = 10;
    int iterations = 500;
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    CountDownLatch latch = new CountDownLatch(1);
    ConcurrentLinkedQueue<Throwable> errors = new ConcurrentLinkedQueue<>();

    List<Runnable> tasks = new ArrayList<>();
    for (int t = 0; t < threads; t++) {
      int threadId = t;
      tasks.add(() -> {
        try {
          latch.await();
          for (int i = 0; i < iterations; i++) {
            // 不同类型交替反序列化，验证无跨类型污染
            String json1 = "{\"f1\":" + (threadId * 1000 + i) + ",\"f2\":\"val-" + threadId + "\"}";
            Binary<Integer, String> b = objectMapper.readValue(json1,
                    new TypeReference<Binary<Integer, String>>() {
                    });
            Assertions.assertEquals((Integer) (threadId * 1000 + i), b.getF1());
            Assertions.assertEquals("val-" + threadId, b.getF2());

            String json2 = "{\"f1\":\"hello\",\"f2\":true,\"f3\":42}";
            Ternary<String, Boolean, Integer> t3 = objectMapper.readValue(json2,
                    new TypeReference<Ternary<String, Boolean, Integer>>() {
                    });
            Assertions.assertEquals("hello", t3.getF1());
            Assertions.assertEquals(Boolean.TRUE, t3.getF2());
            Assertions.assertEquals((Integer) 42, t3.getF3());

            String json3 = "{\"f1\":\"xy\",\"f2\":\"yz\",\"f3\":\"zz\",\"f4\":\"wz\"}";
            Quaternary<String, String, String, String> q = objectMapper.readValue(json3,
                    new TypeReference<Quaternary<String, String, String, String>>() {
                    });
            Assertions.assertEquals("xy", q.getF1());
            Assertions.assertEquals("yz", q.getF2());
            Assertions.assertEquals("zz", q.getF3());
            Assertions.assertEquals("wz", q.getF4());
          }
        } catch (Throwable e) {
          errors.add(e);
        }
      });
    }

    tasks.forEach(executor::submit);
    latch.countDown();
    executor.shutdown();
    executor.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS);

    Assertions.assertTrue(errors.isEmpty(), "Concurrency errors: " + errors);
  }

  @Test
  void testDeserializerConcurrency_sameInstanceSharedAcrossThreads() throws Exception {
    int threads = 8;
    int iterations = 100;
    CountDownLatch latch = new CountDownLatch(1);
    ConcurrentLinkedQueue<Throwable> errors = new ConcurrentLinkedQueue<>();
    ExecutorService executor = Executors.newFixedThreadPool(threads);

    TypeReference<Binary<Integer, Integer>> typeReference = new TypeReference<Binary<Integer, Integer>>() {
    };

    // 所有线程使用同一个 raw type 反序列化，验证共享同一个 cached Deserializer 实例
    for (int t = 0; t < threads; t++) {
      executor.submit(() -> {
        try {
          latch.await();
          for (int i = 0; i < iterations; i++) {
            Binary<Integer, Integer> b = objectMapper.readValue("{\"f1\":1,\"f2\":2}",
                    typeReference);
            Assertions.assertEquals((Integer) 1, b.getF1());
            Assertions.assertEquals((Integer) 2, b.getF2());
          }
        } catch (Throwable e) {
          errors.add(e);
        }
      });
    }

    latch.countDown();
    executor.shutdown();
    executor.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS);

    Assertions.assertTrue(errors.isEmpty(), "Errors sharing same deserializer: " + errors);
  }

}
