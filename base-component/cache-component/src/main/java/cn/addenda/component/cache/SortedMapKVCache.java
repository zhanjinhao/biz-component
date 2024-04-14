package cn.addenda.component.cache;

import java.util.Set;
import java.util.SortedMap;

/**
 * @author addenda
 * @since 2023/05/30
 */
public class SortedMapKVCache<K, V> implements SortedKVCache<K, V> {

  private final SortedMap<K, V> sortedMap;

  public SortedMapKVCache(SortedMap<K, V> sortedMap) {
    this.sortedMap = sortedMap;
  }

  @Override
  public void set(K k, V v) {
    sortedMap.put(k, v);
  }

  @Override
  public boolean containsKey(K k) {
    return sortedMap.containsKey(k);
  }

  @Override
  public V get(K k) {
    return sortedMap.get(k);
  }

  @Override
  public void delete(K k) {
    sortedMap.remove(k);
  }

  @Override
  public long size() {
    return sortedMap.size();
  }

  @Override
  public long capacity() {
    return Integer.MAX_VALUE;
  }

  @Override
  public K getFirst() {
    return sortedMap.firstKey();
  }

  @Override
  public K getLast() {
    return sortedMap.lastKey();
  }

  @Override
  public Set<K> keySet() {
    return sortedMap.keySet();
  }

}
