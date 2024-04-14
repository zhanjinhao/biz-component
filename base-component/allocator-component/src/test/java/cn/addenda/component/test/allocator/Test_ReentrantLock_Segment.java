package cn.addenda.component.test.allocator;

import cn.addenda.component.allocator.AllocatorException;
import cn.addenda.component.allocator.lock.LockAllocator;
import cn.addenda.component.bean.pojo.Binary;
import cn.addenda.component.jdk.util.SleepUtils;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author addenda
 * @since 2023/6/3 12:36
 */
public class Test_ReentrantLock_Segment extends Test_Base_Allocator {

  public Test_ReentrantLock_Segment() {
    super(new Impl_ReentrantLock_Segment());
  }

  @Test
  public void test() {
    // options unsafe true
    // monitor java.util.concurrent.locks.AbstractQueuedSynchronizer parkAndCheckInterrupt  -n 10  --cycle 30
    SleepUtils.sleep(TimeUnit.SECONDS, 30);

    // avg : 28
    baseTest();

    SleepUtils.sleep(TimeUnit.SECONDS, 30);
  }


  public static class Impl_ReentrantLock_Segment implements LockAllocator<Lock> {

    private final Map<String, Binary<Lock, AtomicInteger>> lockMap = new ConcurrentHashMap<>();

    private final Lock[] locks;

    public Impl_ReentrantLock_Segment() {
      locks = new Lock[2 << 4];
      for (int i = 0; i < 2 << 4; i++) {
        locks[i] = new ReentrantLock();
      }
    }

    @Override
    public Lock allocate(String name) {
      Lock lock = locks[index(name)];
      lock.lock();
      try {
        Binary<Lock, AtomicInteger> lockBinary = lockMap
                .computeIfAbsent(name, s -> new Binary<>(new ReentrantLock(), new AtomicInteger(0)));
        lockBinary.getF2().getAndIncrement();
        return lockBinary.getF1();
      } finally {
        lock.unlock();
      }
    }

    @Override
    public void release(String name) {
      Lock lock = locks[index(name)];
      lock.lock();
      try {
        Binary<Lock, AtomicInteger> lockBinary = lockMap.get(name);
        if (lockBinary == null) {
          String msg = String.format("锁 [%s] 不存在！", name);
          throw new AllocatorException(msg);
        }
        int i = lockBinary.getF2().decrementAndGet();
        if (i == 0) {
          lockMap.remove(name);
        }
      } finally {
        lock.unlock();
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
