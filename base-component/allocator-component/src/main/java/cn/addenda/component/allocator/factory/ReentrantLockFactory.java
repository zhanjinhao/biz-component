package cn.addenda.component.allocator.factory;

import lombok.ToString;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author addenda
 * @since 2023/6/4 16:30
 */
@ToString
public class ReentrantLockFactory implements LockFactory<String> {

  private final Lock lock;

  public ReentrantLockFactory() {
    lock = new ReentrantLock();
  }

  @Override
  public Lock getLock(String k) {
    return lock;
  }

}
