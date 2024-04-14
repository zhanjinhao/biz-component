package cn.addenda.component.cache;

import cn.addenda.component.bean.pojo.Binary;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 基于HashMap实现的KVCache
 *
 * @author addenda
 * @since 2023/05/30
 */
public class ExpiredHashMapKVCache<K, V> implements ExpiredKVCache<K, V> {

  private final Map<K, Binary<V, Long>> map = new HashMap<>();

  @Override
  public void set(K k, V v) {
    map.put(k, new Binary<>(v, Long.MAX_VALUE));
  }

  @Override
  public void set(K k, V v, long timeout, TimeUnit timeunit) {
    long timeoutMills = timeunit.toMillis(timeout);
    map.put(k, new Binary<>(v, System.currentTimeMillis() + timeoutMills));
  }

  @Override
  public boolean containsKey(K k) {
    return map.containsKey(k);
  }

  @Override
  public V get(K k) {
    Binary<V, Long> binary = map.get(k);
    if (binary == null) {
      return null;
    }
    if (binary.getF2() < System.currentTimeMillis()) {
      map.remove(k);
      return null;
    }
    return binary.getF1();
  }

  @Override
  public void delete(K k) {
    map.remove(k);
  }

  @Override
  public long size() {
    return map.size();
  }

  @Override
  public long capacity() {
    return 1 << 30;
  }

  @Override
  public V remove(K k) {
    Binary<V, Long> remove = map.remove(k);
    if (remove != null) {
      return remove.getF1();
    }
    return null;
  }

  @Override
  public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
    V apply = mappingFunction.apply(key);
    return map.computeIfAbsent(key, k -> new Binary<>(apply, Long.MAX_VALUE)).getF1();
  }

  @Override
  public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction, long timeout, TimeUnit timeunit) {
    long timeoutMills = timeunit.toMillis(timeout);
    V apply = mappingFunction.apply(key);
    return map.computeIfAbsent(key, k -> new Binary<>(apply, System.currentTimeMillis() + timeoutMills)).getF1();
  }

}
