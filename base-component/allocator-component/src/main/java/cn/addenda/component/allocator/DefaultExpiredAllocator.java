package cn.addenda.component.allocator;

import cn.addenda.component.bean.pojo.Binary;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/9/13 21:35
 */
public abstract class DefaultExpiredAllocator<T> implements ExpiredAllocator<T> {

  /**
   * key：名字
   * value: T,对象；Long,过期时间
   */
  private final Map<String, Binary<T, Long>> map = new HashMap<>();

  /**
   * key：过期时间，value：name Set
   */
  private final TreeMap<Long, Set<String>> treeMap = new TreeMap<>();

  private final Lock lock = new ReentrantLock();

  private long count = 0;

  @Setter
  private long cleaningFrequency = 100;

  @Override
  public T allocate(String name) {
    return allocate(name, TimeUnit.DAYS, 3650);
  }

  @Override
  public T allocateWithDefaultTtl(String name) {
    return allocate(name, TimeUnit.DAYS, 3650);
  }

  @Override
  public void release(String name) {
    lock.lock();
    try {
      Binary<T, Long> remove = map.remove(name);
      if (remove != null) {
        Long f2 = remove.getF2();
        Set<String> list = treeMap.get(f2);
        list.remove(name);
      }
      clear();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public T allocate(String name, TimeUnit timeUnit, long timeout) {
    lock.lock();
    try {
      Param param = Param.builder()
              .name(name)
              .timeUnit(timeUnit)
              .ttl(timeout).build();
      Long expire = timeUnit.toMillis(timeout) + System.currentTimeMillis();
      Binary<T, Long> oldBinary = map.get(name);
      // 如果之前不存在，存数据及ttl
      if (oldBinary == null) {
        T apply = referenceFunction().apply(param);
        map.put(name, new Binary<>(apply, expire));
        treeMap.computeIfAbsent(expire, k -> new LinkedHashSet<>()).add(name);
        clear();
        return apply;
      }
      // 如果之前存在，更新ttl
      else {
        Long oldExpire = oldBinary.getF2();
        Set<String> nameSet = treeMap.get(oldExpire);
        nameSet.remove(name);
        if (nameSet.isEmpty()) {
          treeMap.remove(oldExpire);
        }
        treeMap.computeIfAbsent(expire, k -> new LinkedHashSet<>()).add(name);
        clear();
        return oldBinary.getF1();
      }
    } finally {
      lock.unlock();
    }
  }

  private void clear() {
    count++;
    if (count % cleaningFrequency != 0) {
      return;
    }
    long now = System.currentTimeMillis();
    Iterator<Map.Entry<Long, Set<String>>> iterator = treeMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<Long, Set<String>> next = iterator.next();
      Long expire = next.getKey();
      if (expire < now) {
        iterator.remove();
      }
    }
  }

  protected abstract Function<Param, T> referenceFunction();

}
