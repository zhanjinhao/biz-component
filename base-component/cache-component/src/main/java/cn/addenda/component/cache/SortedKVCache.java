package cn.addenda.component.cache;

import java.util.Set;

/**
 * k有序的缓存
 *
 * @author addenda
 * @since 2023/05/30
 */
public interface SortedKVCache<K, V> extends KVCache<K, V> {

  K getFirst();

  K getLast();

  Set<K> keySet();

}
