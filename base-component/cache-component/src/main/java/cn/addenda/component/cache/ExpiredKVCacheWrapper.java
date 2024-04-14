package cn.addenda.component.cache;

import java.util.concurrent.TimeUnit;

public abstract class ExpiredKVCacheWrapper<K, V> extends KVCacheWrapper<K, V> {

  protected ExpiredKVCacheWrapper(ExpiredKVCache<K, V> kvCacheDelegate) {
    super(kvCacheDelegate);
  }

  public abstract void set(K k, V v, long timeout, TimeUnit timeunit);

  @Override
  public ExpiredKVCache<K, V> getKvCacheDelegate() {
    return (ExpiredKVCache<K, V>) super.getKvCacheDelegate();
  }

}
