package cn.addenda.component.lockhelper;

import cn.addenda.component.jdk.allocator.lock.LockAllocator;

import java.util.concurrent.locks.Lock;

/**
 * @author addenda
 * @since 2022/11/30 19:21
 */
public class LockConfigurer {

  private final LockAllocator<? extends Lock> lockAllocator;

  public LockConfigurer(LockAllocator<? extends Lock> lockAllocator) {
    this.lockAllocator = lockAllocator;
  }

  public LockAllocator<? extends Lock> getLockAllocator() {
    return lockAllocator;
  }
}
