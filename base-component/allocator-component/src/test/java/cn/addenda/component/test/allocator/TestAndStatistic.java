package cn.addenda.component.test.allocator;

import org.junit.Test;

/**
 * @author addenda
 * @since 2023/8/28 19:05
 */
public class TestAndStatistic {

  @Test
  public void test() {
    for (int i = 0; i < 10; i++) {
      System.out.println("------------------------------");
      doTest();
    }
  }

  public void doTest() {
    System.out.print("BaseData : ");
    new ReentrantLockAllocatorBaseDataTest().baseTest();
    System.out.print("ReentrantLock : ");
    new Test_ReentrantLock().test();
    System.out.print("ReentrantLock_Segment : ");
    new Test_ReentrantLock_Segment().test();
    System.out.print("Synchronized : ");
    new Test_Synchronized().test();
    System.out.print("Synchronized_Segment : ");
    new Test_Synchronized_Segment().test();
    System.out.print("ReentrantReadWriteLock : ");
    new Test_ReentrantReadWriteLock().test();
    System.out.print("ReentrantReadWriteLock_Segment : ");
    new Test_ReentrantReadWriteLock_Segment().test();
    System.out.print("CAS : ");
    new Test_CAS().test();
    System.out.print("CAS_Segment : ");
    new Test_CAS_Segment().test();
  }

}
