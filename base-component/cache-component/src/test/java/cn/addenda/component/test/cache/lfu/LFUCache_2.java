package cn.addenda.component.test.cache.lfu;

import cn.addenda.component.cache.KVCache;
import cn.addenda.component.cache.eviction.lfu.LfuLruKVCache;

/**
 * 通过leetCode测试
 */
class LFUCache_2 {

  private KVCache<Integer, Integer> kvCache;

  public LFUCache_2(int capacity) {
    kvCache = new LfuLruKVCache<>(capacity);
  }

  public int get(int key) {
    Integer a = kvCache.get(key);
    return a == null ? -1 : a;
  }

  public void put(int key, int value) {
    kvCache.set(key, value);
  }
}