package cn.addenda.component.cache.eviction.lru;

import cn.addenda.component.cache.KVCache;
import cn.addenda.component.cache.eviction.EvictableKVCache;

/**
 * LRU缓存：基础的LRU缓存，基于双向链表和KV存储实现
 *
 * @author addenda
 * @since 2023/05/30
 */
public class LruKVCache<K, V> extends EvictableKVCache<K, V> {

  /**
   * 记录新旧关系的双向队列：尾节点最新头节点最旧
   */
  private final LruDeque<K> lruDeque;

  public LruKVCache(long capacity, KVCache<K, V> kvCacheDelegate) {
    super(capacity, kvCacheDelegate);
    this.lruDeque = new LinkedHashSetLruDeque<>();
  }

  public LruKVCache(long capacity, KVCache<K, V> kvCacheDelegate, LruDeque<K> lruDeque) {
    super(capacity, kvCacheDelegate);
    this.lruDeque = lruDeque;
  }

  /**
   * k已存在：将k移至尾节点，更新cache
   */
  @Override
  protected void setWhenContainsKey(K k, V v) {
    delete(k);

    lruDeque.addLast(k);
    getKvCacheDelegate().set(k, v);
  }

  /**
   * k不存在：1、若缓存已满，移除头节点；2、将k插入尾节点，存cache
   */
  @Override
  protected void setWhenNonContainsKey(K k, V v) {
    if (capacity() == size()) {
      delete(lruDeque.getFirst());
    }

    lruDeque.addLast(k);
    getKvCacheDelegate().set(k, v);
  }

  @Override
  protected void clearKeyStatistic(K k) {
    lruDeque.remove(k);
  }

  /**
   * 如果cache不存在k，返回null;
   */
  @Override
  protected V getWhenNonContainsKey(K k) {
    return null;
  }

  /**
   * 如果cache存在k，返回v，同时将k移至尾节点。
   */
  @Override
  protected V getWhenContainsKey(K k) {
    lruDeque.remove(k);
    lruDeque.addLast(k);
    return getKvCacheDelegate().get(k);
  }

  /**
   * 同时删除cache里的数据和lruDeque里的数据
   */
  @Override
  public void delete(K k) {
    clearKeyStatistic(k);
    getKvCacheDelegate().delete(k);
  }

  @Override
  public long size() {
    return lruDeque.size();
  }

  protected LruDeque<K> getLruDeque() {
    return lruDeque;
  }

  protected void onlySet(K k, V v) {
    getKvCacheDelegate().set(k, v);
  }

  protected V onlyGet(K k) {
    return getKvCacheDelegate().get(k);
  }

}
