package cn.addenda.component.test.jackson;

import cn.addenda.component.bean.pojo.Binary;
import cn.addenda.component.jackson.util.JacksonUtils;
import cn.addenda.component.jackson.util.TypeFactoryUtils;
import cn.addenda.component.jdk.util.my.MyArrayUtils;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class TypeFactoryUtilsTest {

  @Test
  public void test1() {
    JavaType string = TypeFactoryUtils.construct(String.class);
    JavaType userString = TypeFactoryUtils.constructParametricType(User.class, string);
    JavaType setUserString = TypeFactoryUtils.constructParametricType(Set.class, userString);

    User<String> user1 = new User<>();
    user1.setBirth(LocalDateTime.now());
    user1.setDeath(LocalDateTime.now());
    user1.setT("a");
    User<String> user2 = new User<>();
    user2.setBirth(LocalDateTime.now());
    user2.setDeath(LocalDateTime.now());
    user2.setT("s");
    Set<User<String>> set1 = MyArrayUtils.asHashSet(user1, user2);

    String s = JacksonUtils.toStr(set1);
    log.info("{}", s);
    Set<User<String>> set2 = JacksonUtils.toObj(s, setUserString);
    log.info("{}", JacksonUtils.toStr(set2));
    Assert.assertEquals(set1, set2);
  }

  @Test
  public void test2() {
    JavaType string = TypeFactoryUtils.construct(String.class);
    JavaType userString = TypeFactoryUtils.constructParametricType(User.class, string);
    JavaType listUserString = TypeFactoryUtils.constructParametricType(List.class, userString);

    User<String> user1 = new User<>();
    user1.setBirth(LocalDateTime.now());
    user1.setDeath(LocalDateTime.now());
    user1.setT("a");
    User<String> user2 = new User<>();
    user2.setBirth(LocalDateTime.now());
    user2.setDeath(LocalDateTime.now());
    user2.setT("s");
    List<User<String>> list1 = MyArrayUtils.asArrayList(user1, user2);

    String s = JacksonUtils.toStr(list1);
    log.info("{}", s);
    List<User<String>> list2 = JacksonUtils.toObj(s, listUserString);
    log.info("{}", JacksonUtils.toStr(list2));
    Assert.assertEquals(list1, list2);
  }

  @Test
  public void test3() {
    JavaType string = TypeFactoryUtils.construct(String.class);
    JavaType userString = TypeFactoryUtils.constructParametricType(User.class, string);
    JavaType listUserString = TypeFactoryUtils.constructArray(userString);

    User<String> user1 = new User<>();
    user1.setBirth(LocalDateTime.now());
    user1.setDeath(LocalDateTime.now());
    user1.setT("a");
    User<String> user2 = new User<>();
    user2.setBirth(LocalDateTime.now());
    user2.setDeath(LocalDateTime.now());
    user2.setT("s");

    User<String>[] array1 = new User[]{user1, user2};
    String s = JacksonUtils.toStr(array1);
    log.info("{}", s);
    User<String>[] array2 = JacksonUtils.toObj(s, listUserString);
    log.info("{}", JacksonUtils.toStr(array2));
    Assert.assertArrayEquals(array1, array2);

  }

  @Test
  public void test4() {
    Map<String, User<Integer>> map1 = new HashMap<>();
    User<Integer> user2 = new User<>();
    user2.setBirth(LocalDateTime.now());
    user2.setDeath(LocalDateTime.now());
    user2.setT(123);
    map1.put("asc", user2);

    String mapJson = JacksonUtils.toStr(map1);
    log.info("{}", mapJson);
    JavaType userString = TypeFactoryUtils.construct(String.class);
    JavaType userInteger = TypeFactoryUtils.constructParametricType(User.class, Integer.class);
    Map<String, User<Integer>> map2 = JacksonUtils.toObj(mapJson, TypeFactoryUtils.constructMap(userString, userInteger));
    log.info("{}", JacksonUtils.toStr(map2));
    Assert.assertEquals(map1, map2);
  }

  @Test
  public void test5() {
    User<String> user1 = new User<>();
    user1.setBirth(LocalDateTime.now());
    user1.setDeath(LocalDateTime.now());
    user1.setT("asd");
    User<Integer> user2 = new User<>();
    user2.setBirth(LocalDateTime.now());
    user2.setDeath(LocalDateTime.now());
    user2.setT(123);
    Binary<User<String>, User<Integer>> binary = new Binary<>(user1, user2);

    String mapJson = JacksonUtils.toStr(binary);
    log.info("{}", mapJson);

    JavaType userString = TypeFactoryUtils.constructParametricType(User.class, String.class);
    JavaType userInteger = TypeFactoryUtils.constructParametricType(User.class, Integer.class);
    Binary<User<String>, User<Integer>> binary1 = JacksonUtils.toObj(mapJson,
            TypeFactoryUtils.constructParametricType(Binary.class, userString, userInteger));
    log.info("{}", binary1);
    Assert.assertEquals(binary, binary1);
  }

}
