package cn.addenda.component.common.test.util;

import cn.addenda.component.common.util.CloneUtils;
import cn.addenda.component.common.util.collection.ArrayUtils;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.*;

class CloneUtilsTest {

  @Test
  void cloneSingleObject_null() {
    Assertions.assertNull(CloneUtils.cloneByJDKSerialization((Serializable) null));
  }

  @Test
  void cloneSingleObject_deepCopy() {
    Son original = new Son("a", "1");
    Son cloned = CloneUtils.cloneByJDKSerialization(original);
    Assertions.assertEquals(original, cloned);
    Assertions.assertNotSame(original, cloned, "should be a deep copy");
  }

  @Test
  void cloneSingleObject_nestedObject() {
    Parent original = new Parent("p", new Son("child", "2"));
    Parent cloned = CloneUtils.cloneByJDKSerialization(original);
    Assertions.assertEquals(original, cloned);
    Assertions.assertNotSame(original, cloned);
    Assertions.assertNotSame(original.getSon(), cloned.getSon(), "nested object should also be deep copy");
  }

  @Test
  void cloneSingleObject_nonSerializableField() {
    // Serializable 对象的字段不可序列化 → SneakyThrows 让 NotSerializableException 透出
    Container obj = new Container("test", new NonSerializableField("secret"));
    try {
      CloneUtils.cloneByJDKSerialization(obj);
      Assertions.fail("should have thrown");
    } catch (Exception e) {
      Assertions.assertEquals(NotSerializableException.class, e.getClass());
    }
  }

  @Test
  void sneakyThrows_runtimeTypeIsCheckedException() {
    // @SneakyThrows 不包装异常，运行时仍是 NotSerializableException（checked）
    Container obj = new Container("test", new NonSerializableField("secret"));
    try {
      CloneUtils.cloneByJDKSerialization(obj);
      Assertions.fail("should have thrown");
    } catch (Exception e) {
      Assertions.assertEquals(NotSerializableException.class, e.getClass());
    }
  }

  @Test
  void sneakyThrows_cannotCatchAsRuntimeException() {
    // NotSerializableException extends IOException，不是 RuntimeException
    // catch(RuntimeException) 抓不住 —— 证明 @SneakyThrows 没包装异常
    Container obj = new Container("test", new NonSerializableField("secret"));
    Assertions.assertThrows(NotSerializableException.class, () -> {
      try {
        CloneUtils.cloneByJDKSerialization(obj);
      } catch (RuntimeException e) {
        Assertions.fail("NotSerializableException should NOT be caught by RuntimeException");
      }
    });
  }

  // ====================================================================================================
  //  Collection clone
  // ====================================================================================================

  @Test
  void cloneCollection_null() {
    Assertions.assertNull(CloneUtils.cloneByJDKSerialization((Collection<Son>) null));
  }

  @Test
  void cloneCollection_empty() {
    Collection<Son> result = CloneUtils.cloneByJDKSerialization((Collection<Son>) new ArrayList<Son>());
    Assertions.assertTrue(result.isEmpty());
    Assertions.assertTrue(result instanceof ArrayList);
  }

  @Test
  void cloneCollection_ArrayList() {
    List<Son> original = ArrayUtils.asArrayList(new Son("a", "1"), new Son("b", "2"));
    Collection<Son> cloned = CloneUtils.cloneByJDKSerialization(original);
    Assertions.assertEquals(original, cloned);
    Assertions.assertNotSame(original, cloned);
    Assertions.assertTrue(cloned instanceof ArrayList);
  }

  @Test
  void cloneCollection_LinkedList() {
    LinkedList<Son> original = new LinkedList<>(Arrays.asList(new Son("a", "1"), new Son("b", "2")));
    Collection<Son> cloned = CloneUtils.cloneByJDKSerialization((Collection<Son>) original);
    Assertions.assertEquals(original.size(), cloned.size());
    Assertions.assertTrue(cloned instanceof LinkedList);
  }

  @Test
  void cloneCollection_HashSet() {
    Set<Son> original = new HashSet<>(Arrays.asList(new Son("a", "1"), new Son("b", "2")));
    Collection<Son> cloned = CloneUtils.cloneByJDKSerialization((Collection<Son>) original);
    Assertions.assertEquals(original.size(), cloned.size());
    Assertions.assertTrue(cloned instanceof HashSet);
  }

  @Test
  void cloneCollection_preservesCollectionType() {
    List<Son> original = new ArrayList<>(Arrays.asList(new Son("a", "1")));
    Collection<Son> cloned = CloneUtils.cloneByJDKSerialization(original);
    Assertions.assertEquals(ArrayList.class, cloned.getClass());
  }

  @Test
  void cloneCollection_deepCopyEachElement() {
    List<Son> original = ArrayUtils.asArrayList(new Son("a", "1"), new Son("b", "2"));
    Collection<Son> cloned = CloneUtils.cloneByJDKSerialization(original);

    Iterator<Son> origIter = original.iterator();
    Iterator<Son> cloneIter = cloned.iterator();
    while (origIter.hasNext()) {
      Son o = origIter.next();
      Son c = cloneIter.next();
      Assertions.assertEquals(o, c);
      Assertions.assertNotSame(o, c, "each element should be deep copy");
    }
  }

  @Test
  void cloneCollection_withNullElement() {
    List<Son> original = new ArrayList<>();
    original.add(new Son("a", "1"));
    original.add(null);
    original.add(new Son("b", "2"));
    Collection<Son> cloned = CloneUtils.cloneByJDKSerialization(original);
    Assertions.assertEquals(original.size(), cloned.size());
    Assertions.assertNull(((List<Son>) cloned).get(1));
  }

  @Test
  void cloneCollection_allElementsAreClones() {
    Set<Son> original = new HashSet<>(Arrays.asList(new Son("a", "1"), new Son("b", "2")));
    Collection<Son> cloned = CloneUtils.cloneByJDKSerialization((Collection<Son>) original);
    for (Son c : cloned) {
      for (Son o : original) {
        Assertions.assertNotSame(o, c, "cloned elements should have different references");
      }
    }
  }

  // ====================================================================================================
  //  辅助类
  // ====================================================================================================

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

  @Setter
  @Getter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  static class Parent implements Serializable {

    private String name;
    private Son son;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Parent parent = (Parent) o;
      return Objects.equals(name, parent.name) && Objects.equals(son, parent.son);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, son);
    }
  }

  @Setter
  @Getter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  static class Container implements Serializable {

    private String name;
    private NonSerializableField secret;
  }

  @Setter
  @Getter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  static class NonSerializableField {

    private String value;
  }
}
