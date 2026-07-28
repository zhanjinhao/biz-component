package cn.addenda.component.common.test.jackson;

import cn.addenda.component.common.util.datetime.DateUtils;
import cn.addenda.component.common.jackson.deserializer.LocalDateTimeDeserializer;
import cn.addenda.component.common.jackson.serializer.LocalDateTimeTsSerializer;
import cn.addenda.component.common.jackson.util.JacksonUtils;
import cn.addenda.component.common.jackson.util.TypeFactoryUtils;
import cn.addenda.component.common.lambda.costed.AbstractCostedFunction;
import cn.addenda.component.common.lambda.costed.CostedRunnable;
import cn.addenda.component.common.lambda.named.NamedRunnable;
import cn.addenda.component.common.pojo.Binary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
class JacksonUtilsTest {

  @Test
  void test1() {
    User<String> user = new User<>();
    String userJson = JacksonUtils.toStr(user);
    log.info("{}", userJson);
    User<String> user1 = JacksonUtils.toObj(userJson, TypeFactoryUtils.construct(User.class));
    log.info("{}", JacksonUtils.toStr(user1));
    Assertions.assertEquals(user1, user);
  }

  @Test
  void test2() {
    User<String> user = new User<>();
    user.setBirth(LocalDateTime.now());
    user.setDeath(LocalDateTime.now());
    user.setT("asd");
    String userJson = JacksonUtils.toStr(user);
    log.info("{}", userJson);
    User<String> user1 = JacksonUtils.toObj(userJson, TypeFactoryUtils.constructParametricType(User.class, String.class));
    log.info("{}", JacksonUtils.toStr(user1));
    Assertions.assertEquals(user1, user);
  }

  @Test
  void test3() {
    User2<String> user2String = new User2<>();
    user2String.setBirth(LocalDateTime.now());
    user2String.setDeath(LocalDateTime.now());
    user2String.setT("asd");
    User2<Integer> user2Integer = new User2<>();
    user2Integer.setBirth(LocalDateTime.now());
    user2Integer.setDeath(LocalDateTime.now());
    user2Integer.setT(123);
    Map<User2<String>, User2<Integer>> map1 = new HashMap<>();
    map1.put(user2String, user2Integer);

    JavaType user2StringType = TypeFactoryUtils.constructParametricType(User2.class, String.class);
    JavaType user2IntegerType = TypeFactoryUtils.constructParametricType(User2.class, Integer.class);
    JavaType javaType = TypeFactoryUtils.constructMap(user2StringType, user2IntegerType);

    String map1Json = JacksonUtils.toStr(map1);
    log.info("{}", map1Json);
    Map<User2<String>, User2<Integer>> map2 = JacksonUtils.toObj(map1Json, javaType);
    log.info("{}", JacksonUtils.toStr(map2));
    Assertions.assertEquals(map1, map2);
  }

  @Test
  void test4() {
    User2<Integer> user2Integer = new User2<>();
    user2Integer.setBirth(LocalDateTime.now());
    user2Integer.setDeath(LocalDateTime.now());
    user2Integer.setT(123);
    Map<User2<String>, User2<Integer>> map1 = new HashMap<>();
    map1.put(null, user2Integer);

    JavaType user2StringType = TypeFactoryUtils.constructParametricType(User2.class, String.class);
    JavaType user2IntegerType = TypeFactoryUtils.constructParametricType(User2.class, Integer.class);
    JavaType javaType = TypeFactoryUtils.constructMap(user2StringType, user2IntegerType);

    String map1Json = JacksonUtils.toStr(map1);
    log.info("{}", map1Json);
    Map<User2<String>, User2<Integer>> map2 = JacksonUtils.toObj(map1Json, javaType);
    log.info("{}", JacksonUtils.toStr(map2));
    Assertions.assertEquals(map1, map2);
  }


  @Test
  void test5() {
    testDefault();
    System.out.println("\n------------------\n");
    testCustomized();
    System.out.println("\n------------------\n");
    testJsonPropertyOrder();
  }


  @Test
  void test6() {
    NamedRunnable namedRunnable = NamedRunnable.of(new Runnable() {
      @Override
      public void run() {

      }
    });
    LocalDateTime now = LocalDateTime.now();
    CostedRunnable costedRunnable = CostedRunnable.of(now, AbstractCostedFunction.DEFAULT_THRESHOLD, namedRunnable);
    String str = JacksonUtils.toStr(costedRunnable);
    Assertions.assertEquals("{\"createDateTime\":\"" + DateUtils.format(now, DateUtils.yMdHmsS_FMT) + "\",\"threshold\":200,\"queueSize\":null,\"poolSize\":null,\"activeCount\":null,\"runnable\":{\"name\":\"JacksonUtilsTest#test6\",\"runnable\":{}}}", str);
  }

  @Test
  void testNullGuard() {
    Assertions.assertNull(JacksonUtils.formatJson(null));
    Assertions.assertNull(JacksonUtils.trimNull(null));
    Assertions.assertNull(JacksonUtils.toStr((Object) null));
    Assertions.assertNull(JacksonUtils.toObj((String) null, (Class<?>) null));
  }

  @Test
  void testToStr_IgnoreProperties() {
    Pojo pojo = new Pojo();
    String filtered = JacksonUtils.toStr(pojo, "localDateTime", "localDate", "localTime");
    Assertions.assertEquals("{\"nullLocalDateTime\":null,\"nullLocalDate\":null,\"nullLocalTime\":null}", filtered);
  }

  @Test
  void testToObj_ClassOverload() {
    Pojo source = new Pojo();
    String json = JacksonUtils.toStr(source);
    Pojo back = JacksonUtils.toObj(json, Pojo.class);
    Assertions.assertEquals(source, back);
  }

  @Test
  void testToObj_JavaTypeOverload() {
    Pojo source = new Pojo();
    String json = JacksonUtils.toStr(source);
    Pojo back = JacksonUtils.toObj(json, TypeFactoryUtils.construct(Pojo.class));
    Assertions.assertEquals(source, back);
  }

  @Test
  void testToObj_ObjectMapperOverload() {
    Pojo source = new Pojo();
    String json = JacksonUtils.toStr(source);
    Pojo back = JacksonUtils.toObj(JacksonUtils.cloneBasicMapper(), json, Pojo.class);
    Assertions.assertEquals(source, back);
  }

  @Test
  void testTrimNull_RemovesNullFields() {
    Pojo pojo = new Pojo();
    pojo.setNullLocalDateTime(null);
    pojo.setNullLocalDate(null);
    pojo.setNullLocalTime(null);
    String json = JacksonUtils.toStr(pojo);
    String trimmed = JacksonUtils.trimNull(json);
    Assertions.assertTrue(trimmed.length() < json.length());
    Assertions.assertFalse(trimmed.contains("nullLocalDateTime"));
    Assertions.assertFalse(trimmed.contains("nullLocalDate"));
    Assertions.assertFalse(trimmed.contains("nullLocalTime"));
  }

  @Test
  void testCloneMappers_ProduceWorkingCopies() {
    Pojo source = new Pojo();
    String json = JacksonUtils.toStr(JacksonUtils.cloneBasicMapper(), source);
    Pojo back = JacksonUtils.toObj(JacksonUtils.cloneBasicMapper(), json, Pojo.class);
    Assertions.assertEquals(source, back);

    String trimmed = JacksonUtils.toStr(JacksonUtils.cloneTrimNullMapper(), source);
    Assertions.assertFalse(trimmed.contains("nullLocalDateTime"));
  }

  private static void testJsonPropertyOrder() {
    Binary<Map<String, String>, Map<String, String>> binary = Binary.of(null, null);
    Map<String, String> before = new HashMap<>();
    before.put("1", "a");
    Map<String, String> after = new HashMap<>();
    after.put("2", "b");
    binary.setF1(before);
    binary.setF2(after);

    String s = JacksonUtils.toStr(binary);
    System.out.println(s);
    Binary<Map<String, String>, Map<String, String>> mapMapBinary = JacksonUtils.toObj(s, new TypeReference<Binary<Map<String, String>, Map<String, String>>>() {
    });

    System.out.println(mapMapBinary);
    Assertions.assertEquals(binary, mapMapBinary);

  }

  private static void testDefault() {
    Pojo source = new Pojo();
    String json = JacksonUtils.toStr(source);

    System.out.println("json: " + json);
    System.out.println("formatJson: " + JacksonUtils.formatJson(json));
    System.out.println("trimNull: " + JacksonUtils.trimNull(json));

    Pojo pojo = JacksonUtils.toObj(json, new TypeReference<Pojo>() {
    });

    System.out.println("pojo: " + pojo);
    Assertions.assertEquals(source, pojo);
  }

  private static void testCustomized() {
    CustomizedPojo source = new CustomizedPojo();
    String json = JacksonUtils.toStr(source);

    System.out.println("json: " + json);
    System.out.println("formatJson: " + JacksonUtils.formatJson(json));
    System.out.println("trimNull: " + JacksonUtils.trimNull(json));

    CustomizedPojo pojo = JacksonUtils.toObj(json, new TypeReference<CustomizedPojo>() {
    });

    System.out.println("pojo: " + pojo);
    Assertions.assertEquals(source, pojo);
  }

  @Setter
  @Getter
  @ToString
  private static class Pojo {

    private LocalDateTime localDateTime = LocalDateTime.now();
    private LocalDate localDate = LocalDate.now();
    private LocalTime localTime = LocalTime.now();
    private LocalDateTime nullLocalDateTime;
    private LocalDate nullLocalDate;
    private LocalTime nullLocalTime;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Pojo pojo = (Pojo) o;
      return Objects.equals(localDateTime, pojo.localDateTime) && Objects.equals(localDate, pojo.localDate) && Objects.equals(localTime, pojo.localTime) && Objects.equals(nullLocalDateTime, pojo.nullLocalDateTime) && Objects.equals(nullLocalDate, pojo.nullLocalDate) && Objects.equals(nullLocalTime, pojo.nullLocalTime);
    }

    @Override
    public int hashCode() {
      return Objects.hash(localDateTime, localDate, localTime, nullLocalDateTime, nullLocalDate, nullLocalTime);
    }
  }

  @Setter
  @Getter
  @ToString
  private static class CustomizedPojo {

    @JsonSerialize(using = LocalDateTimeTsSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime localDateTime = LocalDateTime.now();
    private LocalDate localDate = LocalDate.now();
    private LocalTime localTime = LocalTime.now();

    @JsonSerialize(using = LocalDateTimeTsSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime nullLocalDateTime;
    private LocalDate nullLocalDate;
    private LocalTime nullLocalTime;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CustomizedPojo that = (CustomizedPojo) o;
      return Objects.equals(localDateTime, that.localDateTime) && Objects.equals(localDate, that.localDate) && Objects.equals(localTime, that.localTime) && Objects.equals(nullLocalDateTime, that.nullLocalDateTime) && Objects.equals(nullLocalDate, that.nullLocalDate) && Objects.equals(nullLocalTime, that.nullLocalTime);
    }

    @Override
    public int hashCode() {
      return Objects.hash(localDateTime, localDate, localTime, nullLocalDateTime, nullLocalDate, nullLocalTime);
    }
  }


}
