package cn.addenda.component.test.jackson;

import cn.addenda.component.jackson.util.JacksonUtils;
import cn.addenda.component.jackson.util.TypeFactoryUtils;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Jdk8DateTimeJacksonUtilsTest {

  @Test
  public void test1() {
    User2<Integer> user2Integer = new User2<>();
    user2Integer.setBirth(LocalDateTime.now());
    user2Integer.setDeath(LocalDateTime.now());
    user2Integer.setT(123);
    Map<LocalDateTime, User2<Integer>> map1 = new HashMap<>();
    map1.put(LocalDateTime.now(), user2Integer);

    JavaType user2IntegerType = TypeFactoryUtils.constructParametricType(User2.class, Integer.class);
    JavaType javaType = TypeFactoryUtils.constructMap(TypeFactoryUtils.construct(LocalDateTime.class), user2IntegerType);

    String map1Json = JacksonUtils.toStr(map1);
    log.info("{}", map1Json);
    Map<LocalDateTime, User2<Integer>> map2 = JacksonUtils.toObj(map1Json, javaType);
    log.info("{}", JacksonUtils.toStr(map2));
    Assert.assertEquals(map1, map2);
  }

  @Test
  public void test2() {
    User2<Integer> user2Integer = new User2<>();
    user2Integer.setBirth(LocalDateTime.now());
    user2Integer.setDeath(LocalDateTime.now());
    user2Integer.setT(123);
    Map<LocalDateTime, User2<Integer>> map1 = new HashMap<>();
    map1.put(null, user2Integer);

    JavaType user2IntegerType = TypeFactoryUtils.constructParametricType(User2.class, Integer.class);
    JavaType javaType = TypeFactoryUtils.constructMap(TypeFactoryUtils.construct(LocalDateTime.class), user2IntegerType);

    String map1Json = JacksonUtils.toStr(map1);
    log.info("{}", map1Json);
    Map<LocalDateTime, User2<Integer>> map2 = JacksonUtils.toObj(map1Json, javaType);
    log.info("{}", JacksonUtils.toStr(map2));
    Assert.assertEquals(map1, map2);
  }

  @Test
  public void test3() {
    User2<Integer> user2Integer = new User2<>();
    user2Integer.setBirth(LocalDateTime.now());
    user2Integer.setDeath(LocalDateTime.now());
    user2Integer.setT(123);
    Map<LocalDate, User2<Integer>> map1 = new HashMap<>();
    map1.put(LocalDate.now(), user2Integer);

    JavaType user2IntegerType = TypeFactoryUtils.constructParametricType(User2.class, Integer.class);
    JavaType javaType = TypeFactoryUtils.constructMap(TypeFactoryUtils.construct(LocalDate.class), user2IntegerType);

    String map1Json = JacksonUtils.toStr(map1);
    log.info("{}", map1Json);
    Map<LocalDate, User2<Integer>> map2 = JacksonUtils.toObj(map1Json, javaType);
    log.info("{}", JacksonUtils.toStr(map2));
    Assert.assertEquals(map1, map2);
  }

  @Test
  public void test4() {
    User2<Integer> user2Integer = new User2<>();
    user2Integer.setBirth(LocalDateTime.now());
    user2Integer.setDeath(LocalDateTime.now());
    user2Integer.setT(123);
    Map<LocalDate, User2<Integer>> map1 = new HashMap<>();
    map1.put(null, user2Integer);

    JavaType user2IntegerType = TypeFactoryUtils.constructParametricType(User2.class, Integer.class);
    JavaType javaType = TypeFactoryUtils.constructMap(TypeFactoryUtils.construct(LocalDate.class), user2IntegerType);

    String map1Json = JacksonUtils.toStr(map1);
    log.info("{}", map1Json);
    Map<LocalDate, User2<Integer>> map2 = JacksonUtils.toObj(map1Json, javaType);
    log.info("{}", JacksonUtils.toStr(map2));
    Assert.assertEquals(map1, map2);
  }

  @Test
  public void test5() {
    User2<Integer> user2Integer = new User2<>();
    user2Integer.setBirth(LocalDateTime.now());
    user2Integer.setDeath(LocalDateTime.now());
    user2Integer.setT(123);
    Map<LocalTime, User2<Integer>> map1 = new HashMap<>();
    map1.put(LocalTime.now(), user2Integer);

    JavaType user2IntegerType = TypeFactoryUtils.constructParametricType(User2.class, Integer.class);
    JavaType javaType = TypeFactoryUtils.constructMap(TypeFactoryUtils.construct(LocalTime.class), user2IntegerType);

    String map1Json = JacksonUtils.toStr(map1);
    log.info("{}", map1Json);
    Map<LocalTime, User2<Integer>> map2 = JacksonUtils.toObj(map1Json, javaType);
    log.info("{}", JacksonUtils.toStr(map2));
    Assert.assertEquals(map1, map2);
  }

  @Test
  public void test6() {
    User2<Integer> user2Integer = new User2<>();
    user2Integer.setBirth(LocalDateTime.now());
    user2Integer.setDeath(LocalDateTime.now());
    user2Integer.setT(123);
    Map<LocalTime, User2<Integer>> map1 = new HashMap<>();
    map1.put(null, user2Integer);

    JavaType user2IntegerType = TypeFactoryUtils.constructParametricType(User2.class, Integer.class);
    JavaType javaType = TypeFactoryUtils.constructMap(TypeFactoryUtils.construct(LocalTime.class), user2IntegerType);

    String map1Json = JacksonUtils.toStr(map1);
    log.info("{}", map1Json);
    Map<LocalTime, User2<Integer>> map2 = JacksonUtils.toObj(map1Json, javaType);
    log.info("{}", JacksonUtils.toStr(map2));
    Assert.assertEquals(map1, map2);
  }

}
