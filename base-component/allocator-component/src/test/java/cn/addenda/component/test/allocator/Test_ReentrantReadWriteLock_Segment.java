package cn.addenda.component.test.allocator;

import cn.addenda.component.allocator.AllocatorException;
import cn.addenda.component.allocator.lock.LockAllocator;
import cn.addenda.component.bean.pojo.Binary;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author addenda
 * @since 2023/6/3 12:36
 */
public class Test_ReentrantReadWriteLock_Segment extends Test_Base_Allocator {

  public Test_ReentrantReadWriteLock_Segment() {
    super(new Impl_ReentrantReadWriteLock_Segment());
  }

  @Test
  public void test() {
    // avg : 75
    baseTest();
  }


  public static class Impl_ReentrantReadWriteLock_Segment implements LockAllocator<Lock> {

    private final Map<String, Binary<Lock, AtomicInteger>> lockMap = new ConcurrentHashMap<>();

    private final ReadWriteLock[] locks;

    private final int segmentSize;

    public Impl_ReentrantReadWriteLock_Segment(int segmentSize) {
      this.segmentSize = segmentSize;
      locks = new ReadWriteLock[segmentSize];
      for (int i = 0; i < segmentSize; i++) {
        locks[i] = new ReentrantReadWriteLock();
      }
    }

    public Impl_ReentrantReadWriteLock_Segment() {
      this(2 << 5);
    }

    @Override
    public Lock allocate(String name) {
      ReadWriteLock readWriteLock = locks[index(name)];
      Lock readLock = readWriteLock.readLock();
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
      ReadWriteLock readWriteLock = locks[index(name)];
      Lock writeLock = readWriteLock.writeLock();

      writeLock.lock();
      try {
        Binary<Lock, AtomicInteger> lockBinary = lockMap.get(name);
        if (lockBinary == null) {
          String msg = String.format("锁 [%s] 不存在！", name);
          throw new AllocatorException(msg);
        }
        int i = lockBinary.getF2().decrementAndGet();
        if (i == 0) {
          i = lockBinary.getF2().get();
          if (i == 0) {
            lockMap.remove(name);
          }
        }
      } finally {
        writeLock.unlock();
      }

    }

    private int index(String name) {
      return name.hashCode() & (segmentSize - 1);
    }

  }


}
