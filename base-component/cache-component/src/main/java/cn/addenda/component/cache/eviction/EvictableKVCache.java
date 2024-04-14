package cn.addenda.component.cache.eviction;

import cn.addenda.component.cache.KVCache;
import cn.addenda.component.cache.KVCacheWrapper;

/**
 * kv-cache的包装类，用于实现KVCache的淘汰策略。 <br/>
 * 实现淘汰策略的统计数据不随Cache一起存储，所以Cache不能自主驱逐元素。
 *
 * @author addenda
 * @since 2023/05/30
 */
public abstract class EvictableKVCache<K, V> extends KVCacheWrapper<K, V> {
  /**
   * cache的大小
   */
  private final long capacity;

  protected EvictableKVCache(long capacity, KVCache<K, V> kvCache) {
    super(kvCache);
    if (capacity <= 0L) {
      throw new IllegalArgumentException("缓存的大小需要 > 0！");
    }
    this.capacity = capacity;
  }

  @Override
  public void set(K k, V v) {
    if (containsKey(k)) {
      setWhenContainsKey(k, v);
      return;
    }
    setWhenNonContainsKey(k, v);
  }

  protected abstract void setWhenContainsKey(K k, V v);

  protected abstract void setWhenNonContainsKey(K k, V v);

  @Override
  public V get(K k) {
    if (!containsKey(k)) {
      return getWhenNonContainsKey(k);
    }
    return getWhenContainsKey(k);
  }

  protected abstract V getWhenNonContainsKey(K k);

  protected abstract V getWhenContainsKey(K k);

  @Override
  public boolean containsKey(K k) {
    boolean b = getKvCacheDelegate().containsKey(k);
    if (!b) {
      clearKeyStatistic(k);
    }
    return b;
  }

  protected abstract void clearKeyStatistic(K k);

  @Override
  public long capacity() {
    return capacity;
  }

}
