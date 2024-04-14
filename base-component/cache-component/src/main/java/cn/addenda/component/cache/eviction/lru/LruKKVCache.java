package cn.addenda.component.cache.eviction.lru;

import cn.addenda.component.cache.ExpiredHashMapKVCache;
import cn.addenda.component.cache.KVCache;

/**
 * LRU-K缓存算法实现（基础的LRU缓存等价于LRU-1）。<p/>
 *
 * @author addenda
 * @since 2023/05/30
 */
public class LruKKVCache<K, V> extends LruKVCache<K, V> {
  /**
   * 访问多少次可以入队
   */
  private final int threshold;
  /**
   * 记录key被访问了多少次
   */
  private final KVCache<K, Integer> visitorCount;

  public LruKKVCache(long capacity, int threshold) {
    super(capacity, new ExpiredHashMapKVCache<>());
    this.threshold = threshold;
    this.visitorCount = new LruKVCache<>(capacity * 2, new ExpiredHashMapKVCache<>());
  }

  public LruKKVCache(long capacity, int threshold, KVCache<K, V> kvCacheDelegate) {
    super(capacity, kvCacheDelegate);
    this.threshold = threshold;
    this.visitorCount = new LruKVCache<>(capacity * 2, new ExpiredHashMapKVCache<>());
  }

  public LruKKVCache(long capacity, int threshold, LruDeque<K> lruDeque, KVCache<K, V> kvCacheDelegate) {
    super(capacity, kvCacheDelegate, lruDeque);
    this.threshold = threshold;
    this.visitorCount = new LruKVCache<>(capacity * 2, new ExpiredHashMapKVCache<>());
  }

  @Override
  public V get(K k) {
    Integer count = visitorCount.get(k);
    visitorCount.set(k, count == null ? 1 : count + 1);
    return super.get(k);
  }

  @Override
  protected void setWhenNonContainsKey(K k, V v) {
    // 如果不到入队次数，不入队
    Integer count = visitorCount.computeIfAbsent(k, s -> 1);
    if (count >= threshold) {
      visitorCount.delete(k);
      super.setWhenNonContainsKey(k, v);
    }
  }

  /**
   * 自动过期的需要清理visitorCount
   * <p>
   * 本来就不存在的不需要清理visitorCount
   */
  @Override
  protected void clearKeyStatistic(K k) {
    // lruDeque里如果存在k，说明k是自动过期的
    if (getLruDeque().contains(k)) {
      visitorCount.delete(k);
    }
    super.clearKeyStatistic(k);
  }

  @Override
  public void delete(K k) {
    super.delete(k);
    visitorCount.delete(k);
  }

}
