package cn.addenda.component.cache;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public interface ExpiredKVCache<K, V> extends KVCache<K, V> {

  /**
   * @param k        key
   * @param v        value
   * @param timeout  过期时间
   * @param timeunit 过期时间单位
   */
  void set(K k, V v, long timeout, TimeUnit timeunit);

  default V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction, long timeout, TimeUnit timeunit) {
    Objects.requireNonNull(mappingFunction);
    V v;
    if ((v = get(key)) == null) {
      V newValue;
      if ((newValue = mappingFunction.apply(key)) != null) {
        set(key, newValue, timeout, timeunit);
        return newValue;
      }
    }
    return v;
  }

}
