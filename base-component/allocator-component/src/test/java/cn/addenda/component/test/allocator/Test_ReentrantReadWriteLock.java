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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author addenda
 * @since 2023/6/3 12:36
 */
public class Test_ReentrantReadWriteLock extends Test_Base_Allocator {

  public Test_ReentrantReadWriteLock() {
    super(new Impl_ReentrantReadWriteLock(false));
  }

  @Test
  public void test() {
    // options unsafe true
    // monitor java.util.concurrent.locks.AbstractQueuedSynchronizer parkAndCheckInterrupt  -n 10  --cycle 60
    SleepUtils.sleep(TimeUnit.SECONDS, 30);

    // avg : 384
    baseTest();

    SleepUtils.sleep(TimeUnit.SECONDS, 30);

  }


  public static class Impl_ReentrantReadWriteLock implements LockAllocator<Lock> {

    private final Map<String, Binary<Lock, AtomicInteger>> lockMap = new ConcurrentHashMap<>();

    private final ReadWriteLock lock;

    public Impl_ReentrantReadWriteLock(boolean fair) {
      lock = new ReentrantReadWriteLock(fair);
    }

    @Override
    public Lock allocate(String name) {
      Lock readLock = lock.readLock();
      readLock.lock();
      try {
        Binary<Lock, AtomicInteger> lockBinary = lockMap
                .computeIfAbsent(name, s -> new Binary<>(new ReentrantLock(), new AtomicInteger(0)));
        lockBinary.getF2().getAndIncrement();
        return lockBinary.getF1();
      } finally {
        readLock.unlock();
      }
    }

    @Override
    public void release(String name) {
      Lock writeLock = lock.writeLock();
      writeLock.lock();
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
        writeLock.unlock();
      }
    }
  }

}
