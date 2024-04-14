package cn.addenda.component.test.util;

import cn.addenda.component.jdk.util.BatchUtils;
import cn.addenda.component.jdk.util.my.MyArrayUtils;
import cn.addenda.component.jdk.util.IterableUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author addenda
 * @since 2022/11/17 19:18
 */
public class IterableUtilsTest {

  @Test
  public void test1() {
    BatchUtils.acceptInBatches(MyArrayUtils.asArrayList("a", "b"), System.out::println, 1);
  }

  @Test
  public void test2() {
    BatchUtils.applyInBatches(MyArrayUtils.asArrayList("a", "b"), new Function<Iterable<String>, Iterable<Void>>() {
      @Override
      public Iterable<Void> apply(Iterable<String> objects) {
        System.out.println(objects);
        return null;
      }
    }, 1);
  }


  @Test
  public void test3() {
    BatchUtils.acceptInBatches(
            MyArrayUtils.asArrayList("a", "b"),
            MyArrayUtils.asArrayList(1, 2),
            (objects, objects2) -> System.out.println(objects.toString() + objects2.toString()), 1);
  }

  @Test
  public void test4() {
    List<String> list = BatchUtils.applyInBatches(
            MyArrayUtils.asArrayList("a", "b"),
            MyArrayUtils.asArrayList(1, 2),
            (objects, objects2) -> {
              return new ArrayList<>(Collections.singletonList(objects.toString() + objects2.toString()));
            }, 1);
    list.forEach(System.out::println);
  }


  @Test
  public void test5() {
    List<String> list = Arrays.asList("1", "2", "1");
    System.out.println(IterableUtils.deDuplicate(list, Comparator.comparing(String::toString)));
    System.out.println(IterableUtils.deDuplicate(list, String::toString));
    System.out.println(IterableUtils.deDuplicate(list, Comparator.comparing(String::toString), ArrayList::new));
  }


  @Rule
  public ExpectedException expectedException6 = ExpectedException.none();

  @Test
  public void test6() {
    Assert.assertNull(IterableUtils.oneOrNull("a", (Function<String, List<String>>) s -> null));
    Assert.assertEquals("1", IterableUtils.oneOrNull("a", s -> MyArrayUtils.asArrayList("1")));
    try {
      System.out.println(IterableUtils.oneOrNull("a", (Function<String, List<String>>) s -> MyArrayUtils.asArrayList("1", "2")));
    } catch (Exception e) {
      expectedException6.expect(IllegalStateException.class);
      expectedException6.expectMessage("When invoking [IterableUtilsTest#test6], multi result of param[a] are returned.");
      throw e;
    }
  }

  @Rule
  public ExpectedException expectedException7 = ExpectedException.none();

  @Test
  public void test7() {
    Assert.assertNull(IterableUtils.oneOrNull("a", "b", (BiFunction<String, String, List<String>>) (s, s2) -> null));
    Assert.assertEquals("1", IterableUtils.oneOrNull("a", "b", (s, s2) -> MyArrayUtils.asArrayList("1")));
    try {
      System.out.println(IterableUtils.oneOrNull("a", "b", (BiFunction<String, String, List<String>>) (o, o2) -> MyArrayUtils.asArrayList("1", "2")));
    } catch (Exception e) {
      expectedException7.expect(IllegalStateException.class);
      expectedException7.expectMessage("When invoking [IterableUtilsTest#test7], multi result of param[a,b] are returned.");
      throw e;
    }
  }

}
