package cn.addenda.component.test.bean;

import cn.addenda.component.bean.util.BeanUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class BeanUtilsTest {

  @Test
  public void test1() {
    char[] chars = new char[1];
    Assert.assertEquals('\u0000', chars[0]);
    Assert.assertEquals((char) BeanUtils.CHAR_BLANK, chars[0]);
    Assert.assertEquals('\0', chars[0]);
    Assert.assertEquals(new Child()._a, chars[0]);
  }

  @Test
  public void test2() {
    Child child = new Child();
    child.set_a('a');
    child.setAge(123);
    child.setName("123");
    Assert.assertEquals(child, BeanUtils.copyProperties(child, new Child()));
  }

  @Test
  public void test3() {
    Child child = new Child();
    child.set_a('a');
    child.setAge(123);
    child.setName("123");
    Assert.assertEquals(child, BeanUtils.copyProperties(child, Child.class));
  }

  @Test
  public void test4() {
    Child child = new Child();
    child.setAge(123);
    child.setName("");
    Assert.assertEquals(child, BeanUtils.copyProperties(child, new Child()));
  }

  @Test
  public void test5() {
    Child child = new Child();
    child.setAge(123);
    child.setName("");
    Child child1 = new Child();
    child1.setAge(123);
    Assert.assertEquals(child1, BeanUtils.copyPropertiesIgnoreBlank(child, new Child()));
  }

  @Test
  public void test6() {
    Child child = new Child();
    child.setAge(123);
    child.setName("");
    List<Child> children = Arrays.asList(child);
    Assert.assertEquals(children, BeanUtils.copyProperties(children, Child.class));
  }

  @Test
  public void test7() {
    Child child = new Child();
    child.setAge(123);
    child.setName("");
    Child[] children = new Child[]{child};
    Assert.assertArrayEquals(children, BeanUtils.copyProperties(children, Child.class));
  }

  @Test
  public void test8() {
    Child child = new Child();
    child.setAge(123);
    child.setName("abc");
    child.set_a('a');
    Child child1 = new Child();
    child1.set_a('a');
    Assert.assertEquals(child1, BeanUtils.copyPropertiesOnly(child, Child.class, "_a"));
  }

  @Test
  public void test9() {
    Child child = new Child();
    child.setAge(123);
    child.setName("abc");
    child.set_a('a');
    Child child1 = new Child();
    child1.set_a('a');
    Assert.assertEquals(child1, BeanUtils.copyPropertiesOnly(child, Child.class, binary -> "_a".equals(binary.getF1())));
  }

  @Test
  public void test10() {
    Child child = new Child();
    child.setAge(123);
    child.setName("abc");
    child.set_a('a');
    Child child1 = new Child();
    child1.setAge(123);
    child1.set_a('a');
    Assert.assertEquals(child1, BeanUtils.copyPropertiesOnly(child, Child.class, binary -> "_a".equals(binary.getF1()), "age"));
  }

  @Test
  public void test11() {
    Child child = new Child();
    child.setAge(123);
    child.setName("abc");
    child.set_a('a');
    Child child1 = new Child();
    child1.set_a('a');
    Assert.assertEquals(child1, BeanUtils.copyPropertiesIgnore(child, Child.class, "age", "name"));
  }

  @Test
  public void test12() {
    Child child = new Child();
    child.setAge(123);
    child.setName("abc");
    child.set_a('a');
    Child child1 = new Child();
    child1.set_a('a');
    Assert.assertEquals(child1, BeanUtils.copyPropertiesIgnore(child, Child.class, binary -> "age".equals(binary.getF1()) || "name".equals(binary.getF1())));
  }

  @Test
  public void test13() {
    Child child = new Child();
    child.setAge(123);
    child.setName("abc");
    child.set_a('a');
    Child child1 = new Child();
    child1.setAge(123);
    Assert.assertEquals(child1, BeanUtils.copyPropertiesIgnore(child, Child.class, binary -> "_a".equals(binary.getF1()), "name"));
  }

  @Setter
  @Getter
  @ToString
  public static class Child {
    private String name;
    private char _a;
    private Integer age;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Child child = (Child) o;
      return _a == child._a && Objects.equals(name, child.name) && Objects.equals(age, child.age);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, _a, age);
    }
  }

}
