package cn.addenda.component.cache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * kv-cache的包装类，用于实现并发安全。<p/>
 *
 * @author addenda
 * @since 2023/05/30
 */
public class SynchronizedExpiredKVCache<K, V> extends ExpiredKVCacheWrapper<K, V> {

  private final ReentrantLock lock = new ReentrantLock();

  protected SynchronizedExpiredKVCache(ExpiredKVCache<K, V> kvCache) {
    super(kvCache);
  }

  public static <KK, VV> SynchronizedExpiredKVCache<KK, VV> synchronize(ExpiredKVCache<KK, VV> kvCache) {
    return new SynchronizedExpiredKVCache<>(kvCache);
  }

  @Override
  public void set(K k, V v) {
    lock.lock();
    try {
      getKvCacheDelegate().set(k, v);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void set(K k, V v, long timeout, TimeUnit timeunit) {
    lock.lock();
    try {
      getKvCacheDelegate().set(k, v, timeout, timeunit);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean containsKey(K k) {
    lock.lock();
    try {
      return getKvCacheDelegate().containsKey(k);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public V get(K k) {
    lock.lock();
    try {
      return getKvCacheDelegate().get(k);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void delete(K k) {
    lock.lock();
    try {
      getKvCacheDelegate().delete(k);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public long capacity() {
    lock.lock();
    try {
      return getKvCacheDelegate().capacity();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public long size() {
    lock.lock();
    try {
      return getKvCacheDelegate().size();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public V remove(K k) {
    lock.lock();
    try {
      return getKvCacheDelegate().remove(k);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
    lock.lock();
    try {
      return getKvCacheDelegate().computeIfAbsent(key, mappingFunction);
    } finally {
      lock.unlock();
    }
  }

}
