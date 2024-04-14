package cn.addenda.component.test.allocator;

import cn.addenda.component.allocator.AllocatorException;
import cn.addenda.component.allocator.lock.LockAllocator;
import cn.addenda.component.bean.pojo.Binary;
import cn.addenda.component.jdk.util.SleepUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author addenda
 * @since 2023/6/3 12:36
 */
public class Test_ReentrantLock extends Test_Base_Allocator {

  public Test_ReentrantLock() {
    super(new Impl_ReentrantLock(false));
  }

  @Test
  public void test() {
    // options unsafe true
    // monitor java.util.concurrent.locks.AbstractQueuedSynchronizer parkAndCheckInterrupt  -n 10  --cycle 60
    SleepUtils.sleep(TimeUnit.SECONDS, 30);

    // avg: 30
    baseTest();

    SleepUtils.sleep(TimeUnit.SECONDS, 60);

  }

  public static class Impl_ReentrantLock implements LockAllocator<Lock> {

    private final Map<String, Binary<Lock, Integer>> lockMap = new HashMap<>();

    private final Lock lock;

    public Impl_ReentrantLock(boolean fair) {
      lock = new ReentrantLock(fair);
    }

    @Override
    public Lock allocate(String name) {
      lock.lock();
      try {
        Binary<Lock, Integer> lockBinary = lockMap
                .computeIfAbsent(name, s -> new Binary<>(new ReentrantLock(), 0));
        lockBinary.setF2(lockBinary.getF2() + 1);
        return lockBinary.getF1();
      } finally {
        lock.unlock();
      }
    }

    @Override
    public void release(String name) {
      lock.lock();
      try {
        Binary<Lock, Integer> lockBinary = lockMap.get(name);
        if (lockBinary == null) {
          String msg = String.format("锁 [%s] 不存在！", name);
          throw new AllocatorException(msg);
        }

        lockBinary.setF2(lockBinary.getF2() - 1);
        int i = lockBinary.getF2();
        if (i == 0) {
          lockMap.remove(name);
        }
      } finally {
        lock.unlock();
      }
    }

    public Map<String, Binary<Lock, Integer>> getLockMap() {
      return lockMap;
    }
  }

}
