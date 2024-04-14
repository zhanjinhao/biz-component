package cn.addenda.component.cache.eviction.lfu;

import cn.addenda.component.cache.ExpiredHashMapKVCache;
import cn.addenda.component.cache.KVCache;
import cn.addenda.component.cache.SortedKVCache;

import java.util.LinkedHashSet;

/**
 * @author addenda
 * @since 2023/05/30
 */
public class LfuHashMapKVCache<K, V> extends LfuKVCache<K, V> {

  public LfuHashMapKVCache(long capacity) {
    super(capacity, new ExpiredHashMapKVCache<>());
  }

  public LfuHashMapKVCache(KVCache<K, Long> keyToVisitorCount, SortedKVCache<Long, LinkedHashSet<K>> visitorCountToKeySet, long capacity) {
    super(keyToVisitorCount, visitorCountToKeySet, capacity, new ExpiredHashMapKVCache<>());
  }

}
