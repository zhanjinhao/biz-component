package cn.addenda.component.cache.eviction.lfu;

import cn.addenda.component.cache.ExpiredHashMapKVCache;
import cn.addenda.component.cache.KVCache;
import cn.addenda.component.cache.SortedKVCache;
import cn.addenda.component.cache.SortedMapKVCache;
import cn.addenda.component.cache.eviction.EvictableKVCache;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.TreeMap;

/**
 * LFU缓存：基础的LFU缓存，基于双哈希表构建。
 *
 * @author addenda
 * @since 2023/05/30
 */
@Getter
public class LfuKVCache<K, V> extends EvictableKVCache<K, V> {
  /**
   * 记录key出现的次数
   */
  private final KVCache<K, Long> keyToVisitorCount;
  /**
   * 记录次数队列的keys
   */
  private final SortedKVCache<Long, LinkedHashSet<K>> visitorCountToKeySet;

  public LfuKVCache(long capacity, KVCache<K, V> kvCacheDelegate) {
    super(capacity, kvCacheDelegate);
    this.keyToVisitorCount = new ExpiredHashMapKVCache<>();
    this.visitorCountToKeySet = new SortedMapKVCache<>(new TreeMap<>());
  }

  public LfuKVCache(KVCache<K, Long> keyToVisitorCount,
                    SortedKVCache<Long, LinkedHashSet<K>> visitorCountToKeySet,
                    long capacity, KVCache<K, V> kvCacheDelegate) {
    super(capacity, kvCacheDelegate);
    this.keyToVisitorCount = keyToVisitorCount;
    this.visitorCountToKeySet = visitorCountToKeySet;
  }

  @Override
  protected void setWhenContainsKey(K k, V v) {
    calculateKeyUsage(k);
    getKvCacheDelegate().set(k, v);
  }

  @Override
  protected void setWhenNonContainsKey(K k, V v) {
    if (capacity() == size()) {
      removeMinOldest();
    }

    keyToVisitorCount.set(k, 0L);
    visitorCountToKeySet.computeIfAbsent(0L, s -> new LinkedHashSet<>()).add(k);
    getKvCacheDelegate().set(k, v);
  }

  private void removeMinOldest() {
    Long minVisitorCount = visitorCountToKeySet.size() == 0L ? 0L : visitorCountToKeySet.getFirst();

    LinkedHashSet<K> keyList = visitorCountToKeySet.get(minVisitorCount);
    K oldest = keyList.iterator().next();
    keyList.remove(oldest);
    if (keyList.isEmpty()) {
      visitorCountToKeySet.delete(minVisitorCount);
    }

    keyToVisitorCount.delete(oldest);
    getKvCacheDelegate().delete(oldest);
  }

  @Override
  protected V getWhenNonContainsKey(K k) {
    return null;
  }

  @Override
  protected V getWhenContainsKey(K k) {
    calculateKeyUsage(k);
    return getKvCacheDelegate().get(k);
  }


  private void calculateKeyUsage(K k) {
    Long visitorCount = keyToVisitorCount.get(k);
    keyToVisitorCount.set(k, visitorCount + 1);

    LinkedHashSet<K> keyList = visitorCountToKeySet.get(visitorCount);
    keyList.remove(k);
    if (keyList.isEmpty()) {
      visitorCountToKeySet.delete(visitorCount);
    }

    visitorCountToKeySet.computeIfAbsent(visitorCount + 1, s -> new LinkedHashSet<>()).add(k);
  }

  @Override
  protected void clearKeyStatistic(K k) {
    Long visitorCount = keyToVisitorCount.get(k);
    if (visitorCount != null) {
      keyToVisitorCount.delete(k);
      LinkedHashSet<K> keyList = visitorCountToKeySet.get(visitorCount);
      keyList.remove(k);
      if (keyList.isEmpty()) {
        visitorCountToKeySet.delete(visitorCount);
      }
    }
  }

  @Override
  public void delete(K k) {
    clearKeyStatistic(k);

    getKvCacheDelegate().delete(k);
  }

  @Override
  public long size() {
    return keyToVisitorCount.size();
  }

}
