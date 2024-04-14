package cn.addenda.component.cache.eviction.lru;

import cn.addenda.component.cache.ExpiredHashMapKVCache;
import cn.addenda.component.cache.KVCache;
import lombok.*;

/**
 * 分代LRU缓存算法实现（MySQL淘汰页使用的LRU实现）。
 * <p>
 * 老年代用于存储低频数据。
 *
 * @author addenda
 * @since 2023/05/30x
 */
@ToString
public class LruGenerationKVCache<K, V> extends LruKVCache<K, V> {

  /**
   * 老年代cache大小
   */
  private long oldCapacity;
  /**
   * 连续两次访问低于阈值时入年轻代
   */
  private final long threshold;
  /**
   * 老年代：用于存储低频数据
   */
  private final LruKVCache<K, Entry<V>> oldGenerationKVCache;

  public LruGenerationKVCache(long newCapacity, long oldCapacity, int threshold) {
    super(newCapacity, new ExpiredHashMapKVCache<>());
    this.oldCapacity = oldCapacity;
    this.threshold = threshold;
    this.oldGenerationKVCache = new LruKVCache<>(oldCapacity, new ExpiredHashMapKVCache<>());
  }

  public LruGenerationKVCache(long newCapacity, KVCache<K, V> kvCacheDelegate, long oldCapacity, int threshold) {
    super(newCapacity, kvCacheDelegate);
    this.oldCapacity = oldCapacity;
    this.threshold = threshold;
    this.oldGenerationKVCache = new LruKVCache<>(oldCapacity, new ExpiredHashMapKVCache<>());
  }

  public LruGenerationKVCache(LruDeque<K> lruDeque, long newCapacity, KVCache<K, V> kvCacheDelegate, long oldCapacity, int threshold) {
    super(newCapacity, kvCacheDelegate, lruDeque);
    this.oldCapacity = oldCapacity;
    this.threshold = threshold;
    this.oldGenerationKVCache = new LruKVCache<>(oldCapacity, new ExpiredHashMapKVCache<>());
  }

  /**
   * -1：如果年轻代存在k，将其移至年轻代的头部并返回（与基础LRU实现一致）
   * <p>
   * 0：如果老年代不存在k，返回bull
   * <p>
   * 1：如果老年代存在k且满足阈值，移动至年轻代
   * <p>
   * 2：如果老年代存在k但不满足阈值，更新时间
   */
  @Override
  protected V getWhenNonContainsKey(K k) {
    Entry<V> entry = oldGenerationKVCache.onlyGet(k);
    if (entry == null) {
      return null;
    } else {
      long createTm = entry.getVisitTm();
      long currentTimeMillis = System.currentTimeMillis();
      if (currentTimeMillis - createTm > threshold) {
        entry.setVisitTm(currentTimeMillis);
      } else {
        oldGenerationKVCache.delete(k);
        super.set(k, entry.getV());
      }
      return entry.getV();
    }
  }

  /**
   * 存cache的时候存入老年代
   */
  @Override
  protected void setWhenNonContainsKey(K k, V v) {
    oldGenerationKVCache.set(k, new Entry<>(v, System.currentTimeMillis()));
  }

  @Override
  public void delete(K k) {
    oldGenerationKVCache.delete(k);
    super.delete(k);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Entry<V> {
    private V v;
    private long visitTm;
  }

}
