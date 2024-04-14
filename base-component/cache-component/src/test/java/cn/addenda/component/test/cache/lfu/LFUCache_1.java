package cn.addenda.component.test.cache.lfu;

import cn.addenda.component.cache.KVCache;
import cn.addenda.component.cache.eviction.lfu.LfuHashMapKVCache;

/**
 * 通过leetCode测试
 */
public class LFUCache_1 {

  private KVCache<Integer, Integer> kvCache;

  public LFUCache_1(int capacity) {
    kvCache = new LfuHashMapKVCache<>(capacity);
  }

  public int get(int key) {
    Integer a = kvCache.get(key);
    return a == null ? -1 : a;
  }

  public void put(int key, int value) {
    kvCache.set(key, value);
  }
}