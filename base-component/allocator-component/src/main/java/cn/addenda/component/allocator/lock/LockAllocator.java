package cn.addenda.component.allocator.lock;

import cn.addenda.component.allocator.Allocator;

import java.util.concurrent.locks.Lock;

/**
 * {@link LockAllocator#allocate(String)}动态的是与name绑定的锁。
 *
 * @author addenda
 * @since 2023/6/4 16:30
 */
public interface LockAllocator<T extends Lock> extends Allocator<T> {

  T allocate(String name);

  void release(String name);

}
