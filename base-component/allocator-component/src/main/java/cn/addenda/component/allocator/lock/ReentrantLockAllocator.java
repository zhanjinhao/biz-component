package cn.addenda.component.allocator.lock;

import cn.addenda.component.allocator.ReferenceCountAllocator;
import cn.addenda.component.allocator.factory.LockFactory;
import cn.addenda.component.allocator.factory.ReentrantSegmentLockFactory;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/5/30 22:51
 */
public class ReentrantLockAllocator extends ReferenceCountAllocator<ReentrantLock> implements LockAllocator<ReentrantLock> {

  public ReentrantLockAllocator(LockFactory<String> lockFactory) {
    super(lockFactory);
  }

  public ReentrantLockAllocator() {
    this(new ReentrantSegmentLockFactory());
  }

  @Override
  protected Function<String, ReentrantLock> referenceFunction() {
    return s -> new ReentrantLock();
  }

}
