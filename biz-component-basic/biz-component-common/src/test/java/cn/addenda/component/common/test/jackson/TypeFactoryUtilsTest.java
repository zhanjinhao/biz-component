package cn.addenda.component.common.test.jackson;

import cn.addenda.component.common.util.collection.ArrayUtils;
import cn.addenda.component.common.jackson.util.JacksonUtils;
import cn.addenda.component.common.jackson.util.TypeFactoryUtils;
import cn.addenda.component.common.pojo.Binary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
class TypeFactoryUtilsTest {

  @Test
  void test1() {
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
    Set<User<String>> set1 = ArrayUtils.asHashSet(user1, user2);

    String s = JacksonUtils.toStr(set1);
    log.info("{}", s);
    Set<User<String>> set2 = JacksonUtils.toObj(s, setUserString);
    log.info("{}", JacksonUtils.toStr(set2));
    Assertions.assertEquals(set1, set2);
  }

  @Test
  void test2() {
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
    List<User<String>> list1 = ArrayUtils.asArrayList(user1, user2);

    String s = JacksonUtils.toStr(list1);
    log.info("{}", s);
    List<User<String>> list2 = JacksonUtils.toObj(s, listUserString);
    log.info("{}", JacksonUtils.toStr(list2));
    Assertions.assertEquals(list1, list2);
  }

  @Test
  void test3() {
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
    Assertions.assertArrayEquals(array1, array2);

  }

  @Test
  void test4() {
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
    Assertions.assertEquals(map1, map2);
  }

  @Test
  void test5() {
    User<String> user1 = new User<>();
    user1.setBirth(LocalDateTime.now());
    user1.setDeath(LocalDateTime.now());
    user1.setT("asd");
    User<Integer> user2 = new User<>();
    user2.setBirth(LocalDateTime.now());
    user2.setDeath(LocalDateTime.now());
    user2.setT(123);
    Binary<User<String>, User<Integer>> binary = Binary.of(user1, user2);

    String mapJson = JacksonUtils.toStr(binary);
    log.info("{}", mapJson);

    JavaType userString = TypeFactoryUtils.constructParametricType(User.class, String.class);
    JavaType userInteger = TypeFactoryUtils.constructParametricType(User.class, Integer.class);
    Binary<User<String>, User<Integer>> binary1 = JacksonUtils.toObj(mapJson,
            TypeFactoryUtils.constructParametricType(Binary.class, userString, userInteger));
    log.info("{}", binary1);
    Assertions.assertEquals(binary, binary1);
  }

  @Test
  void testGetTypeArguments() {
    List<JavaType> args = TypeFactoryUtils.getTypeArguments(
            new TypeReference<List<User<String>>>() {
            });
    Assertions.assertEquals(1, args.size());

    args = TypeFactoryUtils.getTypeArguments(
            new TypeReference<Map<String, User<Integer>>>() {
            });
    Assertions.assertEquals(2, args.size());

    args = TypeFactoryUtils.getTypeArguments(
            new TypeReference<String>() {
            });
    Assertions.assertEquals(0, args.size());
  }

  @Test
  void testGetTypeArgument() {
    JavaType arg = TypeFactoryUtils.getTypeArgument(
            new TypeReference<Map<String, Integer>>() {
            }, 0);
    Assertions.assertEquals(String.class, arg.getRawClass());

    Assertions.assertThrows(IndexOutOfBoundsException.class,
            () -> TypeFactoryUtils.getTypeArgument(
                    new TypeReference<List<String>>() {
                    }, 5));
  }

}
