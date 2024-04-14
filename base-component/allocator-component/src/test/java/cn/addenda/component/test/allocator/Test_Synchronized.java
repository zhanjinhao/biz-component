package cn.addenda.component.test.allocator;

import cn.addenda.component.allocator.AllocatorException;
import cn.addenda.component.allocator.lock.LockAllocator;
import cn.addenda.component.bean.pojo.Binary;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author addenda
 * @since 2023/6/3 12:36
 */
public class Test_Synchronized extends Test_Base_Allocator {

  public Test_Synchronized() {
    super(new Impl_Synchronized());
  }

  @Test
  public void test() {
    // avg: 30
    baseTest();
  }


  public static class Impl_Synchronized implements LockAllocator<Lock> {

    private final Map<String, Binary<Lock, Integer>> lockMap = new HashMap<>();

    private final Lock lock = new ReentrantLock();

    @Override
    public Lock allocate(String name) {
      synchronized (lock) {
        Binary<Lock, Integer> lockBinary = lockMap
                .computeIfAbsent(name, s -> new Binary<>(new ReentrantLock(), 0));
        lockBinary.setF2(lockBinary.getF2() + 1);
        return lockBinary.getF1();
      }
    }

    @Override
    public void release(String name) {
      synchronized (lock) {
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
      }
    }
  }


}
