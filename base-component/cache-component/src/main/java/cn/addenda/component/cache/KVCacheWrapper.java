package cn.addenda.component.cache;

import lombok.Getter;

/**
 * kv-cache包装类的抽象。
 *
 * @author addenda
 * @since 2023/05/30
 */
public abstract class KVCacheWrapper<K, V> implements KVCache<K, V> {

  /**
   * 真正存储数据的cache
   */
  @Getter
  private final KVCache<K, V> kvCacheDelegate;

  protected KVCacheWrapper(KVCache<K, V> kvCacheDelegate) {
    if (kvCacheDelegate == null) {
      throw new IllegalArgumentException("kvCacheDelegate is null! ");
    }
    this.kvCacheDelegate = kvCacheDelegate;
  }

  @Override
  public long size() {
    return kvCacheDelegate.size();
  }

}
