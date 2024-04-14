package cn.addenda.component.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author addenda
 * @since 2023/05/30
 */
public class GuavaKVCache implements KVCache<String, String> {

  private long capacity;

  private Cache<String, String> cache;

  public GuavaKVCache(Cache<String, String> cache, long capacity) {
    this.cache = cache;
    this.capacity = capacity;
  }

  public GuavaKVCache() {
    cache = CacheBuilder.newBuilder()
            .maximumSize(1000).build();
    this.capacity = 1000L;
  }

  public GuavaKVCache(long capacity) {
    cache = CacheBuilder.newBuilder()
            .maximumSize(capacity).build();
    this.capacity = capacity;
  }

  @Override
  public void set(String key, String value) {
    cache.put(key, value);
  }

  @Override
  public boolean containsKey(String key) {
    return cache.getIfPresent(key) != null;
  }

  @Override
  public String get(String key) {
    return cache.getIfPresent(key);
  }

  @Override
  public void delete(String key) {
    cache.invalidate(key);
  }

  @Override
  public long size() {
    return cache.size();
  }

  @Override
  public long capacity() {
    return capacity;
  }

  @Override
  public String toString() {
    return "GuavaKVCache{" +
            "capacity=" + capacity +
            ", cache size=" + cache.size() +
            '}';
  }

}
