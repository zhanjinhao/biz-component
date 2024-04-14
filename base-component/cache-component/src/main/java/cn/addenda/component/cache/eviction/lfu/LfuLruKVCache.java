package cn.addenda.component.cache.eviction.lfu;

import cn.addenda.component.cache.ExpiredHashMapKVCache;
import cn.addenda.component.cache.KVCache;
import cn.addenda.component.cache.SortedKVCache;
import cn.addenda.component.cache.eviction.lru.LruKVCache;

import java.util.LinkedHashSet;

/**
 * @author addenda
 * @since 2023/05/30
 */
public class LfuLruKVCache<K, V> extends LfuKVCache<K, V> {

  public LfuLruKVCache(long capacity) {
    super(capacity, new LruKVCache<>(capacity, new ExpiredHashMapKVCache<>()));
  }

  public LfuLruKVCache(long capacity, LruKVCache<K, V> kvCacheDelegate) {
    super(capacity, kvCacheDelegate);
  }

  public LfuLruKVCache(KVCache<K, Long> keyToVisitorCount,
                       SortedKVCache<Long, LinkedHashSet<K>> visitorCountToKeySet,
                       int capacity, LruKVCache<K, V> kvCacheDelegate) {
    super(keyToVisitorCount, visitorCountToKeySet, capacity, kvCacheDelegate);
  }

}
