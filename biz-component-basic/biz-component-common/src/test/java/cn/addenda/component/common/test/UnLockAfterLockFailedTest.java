package cn.addenda.component.common.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author addenda
 * @since 2022/8/11
 */
class UnLockAfterLockFailedTest {

  private ReentrantLock lock = new ReentrantLock();

  @Test
  void test() {

    Assertions.assertThrows(IllegalMonitorStateException.class, () -> {
      lock.unlock();
    });

  }

}
