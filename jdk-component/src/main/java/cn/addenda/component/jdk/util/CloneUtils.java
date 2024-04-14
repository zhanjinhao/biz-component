package cn.addenda.component.jdk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Collection;

/**
 * todo 需要重构为接口，支持JDK、JSON、HESSIAN等各种实现
 *
 * @author addenda
 * @since 2023/05/30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloneUtils {

  @SneakyThrows
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> T cloneByJDKSerialization(T obj) {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bout);
    oos.writeObject(obj);
    ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bin);
    return (T) ois.readObject();
  }

  public static <T extends Serializable> Collection<T> cloneByJDKSerialization(Collection<T> collection) {
    if (collection == null) {
      return null;
    }
    Collection<T> newList = newInstance((Class<Collection<T>>) collection.getClass());
    if (collection.isEmpty()) {
      return newList;
    }
    for (T next : collection) {
      newList.add(cloneByJDKSerialization(next));
    }
    return newList;
  }

  @SneakyThrows
  private static <T extends Serializable> Collection<T> newInstance(Class<Collection<T>> collection) {
    return collection.newInstance();
  }

}
