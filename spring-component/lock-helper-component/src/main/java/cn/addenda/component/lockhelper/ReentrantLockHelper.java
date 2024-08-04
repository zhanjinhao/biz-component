package cn.addenda.component.lockhelper;

import cn.addenda.component.jdk.allocator.lock.ReentrantLockAllocator;

/**
 * @author addenda
 * @since 2023/6/4 11:53
 */
public class ReentrantLockHelper extends LockHelper {

  public ReentrantLockHelper(String namespace, ReentrantLockAllocator reentrantLockAllocator) {
    super(namespace, reentrantLockAllocator);
  }

}
