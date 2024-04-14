package cn.addenda.component.test.jackson;

import cn.addenda.component.jackson.util.TypeFactoryUtils;
import cn.addenda.component.jackson.util.JacksonUtils;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JacksonUtilsTest {

  @Test
  public void test1() {
    User<String> user = new User<>();
    String userJson = JacksonUtils.toStr(user);
    log.info("{}", userJson);
    User<String> user1 = JacksonUtils.toObj(userJson, TypeFactoryUtils.construct(User.class));
    log.info("{}", JacksonUtils.toStr(user1));
    Assert.assertEquals(user1, user);
  }

  @Test
  public void test2() {
    User<String> user = new User<>();
    user.setBirth(LocalDateTime.now());
    user.setDeath(LocalDateTime.now());
    user.setT("asd");
    String userJson = JacksonUtils.toStr(user);
    log.info("{}", userJson);
    User<String> user1 = JacksonUtils.toObj(userJson, TypeFactoryUtils.constructParametricType(User.class, String.class));
    log.info("{}", JacksonUtils.toStr(user1));
    Assert.assertEquals(user1, user);
  }

  @Test
  public void test3() {
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
    Assert.assertEquals(map1, map2);
  }

  @Test
  public void test4() {
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
    Assert.assertEquals(map1, map2);
  }

}
