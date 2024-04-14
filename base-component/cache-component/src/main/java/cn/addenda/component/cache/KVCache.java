package cn.addenda.component.cache;

import java.util.Objects;
import java.util.function.Function;

/**
 * 抽象kv-cache的功能
 *
 * @author addenda
 * @since 2023/05/30
 */
public interface KVCache<K, V> {

  void set(K k, V v);

  boolean containsKey(K k);

  V get(K k);

  void delete(K k);

  long size();

  long capacity();

  /**
   * get & delete
   */
  default V remove(K k) {
    V v = get(k);
    delete(k);
    return v;
  }

  default V computeIfAbsent(K key,
                            Function<? super K, ? extends V> mappingFunction) {
    Objects.requireNonNull(mappingFunction);
    V v;
    if ((v = get(key)) == null) {
      V newValue;
      if ((newValue = mappingFunction.apply(key)) != null) {
        set(key, newValue);
        return newValue;
      }
    }

    return v;
  }

}
