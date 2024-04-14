package cn.addenda.component.test.allocator;

import cn.addenda.component.allocator.AllocatorException;
import cn.addenda.component.allocator.lock.LockAllocator;
import cn.addenda.component.bean.pojo.Binary;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author addenda
 * @since 2023/6/3 12:36
 */
public class Test_Synchronized_Segment extends Test_Base_Allocator {

  public Test_Synchronized_Segment() {
    super(new Impl_Synchronized_Segment());
  }

  @Test
  public void test() {
    // avg: 30
    baseTest();
  }


  public static class Impl_Synchronized_Segment implements LockAllocator<Lock> {

    private final Map<String, Binary<Lock, AtomicInteger>> lockMap = new ConcurrentHashMap<>(2048);

    private final Lock[] locks;

    public Impl_Synchronized_Segment() {
      locks = new Lock[2 << 4];
      for (int i = 0; i < 2 << 4; i++) {
        locks[i] = new ReentrantLock();
      }
    }

    @Override
    public Lock allocate(String name) {
      Lock lock = locks[index(name)];
      synchronized (lock) {
        Binary<Lock, AtomicInteger> lockBinary = lockMap
                .computeIfAbsent(name, s -> new Binary<>(new ReentrantLock(), new AtomicInteger(0)));
        lockBinary.getF2().getAndIncrement();
        return lockBinary.getF1();
      }
    }

    @Override
    public void release(String name) {
      Lock lock = locks[index(name)];
      synchronized (lock) {
        Binary<Lock, AtomicInteger> lockBinary = lockMap.get(name);
        if (lockBinary == null) {
          String msg = String.format("锁 [%s] 不存在！", name);
          throw new AllocatorException(msg);
        }
        int i = lockBinary.getF2().decrementAndGet();
        if (i == 0) {
          lockMap.remove(name);
        }
      }
    }

    private int index(String name) {
      return name.hashCode() & ((2 << 4) - 1);
    }

    public Map<String, Binary<Lock, AtomicInteger>> getLockMap() {
      return lockMap;
    }

  }


}
