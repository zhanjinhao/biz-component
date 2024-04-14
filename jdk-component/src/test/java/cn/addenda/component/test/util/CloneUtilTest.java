package cn.addenda.component.test.util;

import cn.addenda.component.jdk.util.CloneUtils;
import cn.addenda.component.jdk.util.my.MyArrayUtils;
import lombok.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author addenda
 * @since 2022/2/15
 */
public class CloneUtilTest {

  @Test
  public void test1() {

    List<Son> sons1 = MyArrayUtils.asArrayList(new Son("a", "1"), new Son("b", "2"), new Son("c", "2"));
    Collection<Son> sons11 = CloneUtils.cloneByJDKSerialization(sons1);
    Assert.assertEquals(sons11, sons1);

    List<Son> sons2 = MyArrayUtils.asLinkedList(new Son("a", "1"), new Son("b", "2"), new Son("c", "2"));
    Collection<Son> sons22 = CloneUtils.cloneByJDKSerialization(sons2);
    Assert.assertEquals(sons22, sons2);

    Set<Son> sons3 = MyArrayUtils.asHashSet(new Son("a", "1"), new Son("b", "2"), new Son("c", "2"));
    Collection<Son> sons33 = CloneUtils.cloneByJDKSerialization(sons3);
    Assert.assertEquals(sons33, sons3);
  }

  @Setter
  @Getter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  static class Son implements Serializable {

    private String name;
    private String age;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Son son = (Son) o;
      return Objects.equals(name, son.name) && Objects.equals(age, son.age);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, age);
    }
  }

}
