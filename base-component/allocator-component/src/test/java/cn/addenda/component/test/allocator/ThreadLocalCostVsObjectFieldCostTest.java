package cn.addenda.component.test.allocator;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author addenda
 * @since 2023/8/28 20:32
 */
public class ThreadLocalCostVsObjectFieldCostTest {

  private int value = 0;

  private ThreadLocalHoldCounter threadLocal = new ThreadLocalHoldCounter();

  /**
   * ThreadLocal subclass. Easiest to explicitly define for sake
   * of deserialization mechanics.
   */
  static final class ThreadLocalHoldCounter
          extends ThreadLocal<HoldCounter> {
    public HoldCounter initialValue() {
      return new HoldCounter();
    }
  }

  static final class HoldCounter {
    int count = 0;
    // Use id, not reference, to avoid garbage retention
  }


  @Test
  public void test() {
    for (int i = 0; i < 10; i++) {
      test1();
      test2();
      test3();
    }
  }

  public void test1() {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 200 * 1000 * 2 * 100; i++) {
      value = value + 1;
    }
    long end = System.currentTimeMillis();
    System.out.println("object field cost: " + (end - start) + "ms");
  }

  public void test2() {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 200 * 1000 * 2 * 100; i++) {
      threadLocal.get().count++;
    }
    long end = System.currentTimeMillis();
    System.out.println("threadlocal cost: " + (end - start) + "ms");
  }

  public void test3() {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 200 * 1000 * 2 * 100; i++) {
      getThreadId(Thread.currentThread());
    }
    long end = System.currentTimeMillis();
    System.out.println("thread get id cost: " + (end - start) + "ms");
  }

  static final long getThreadId(Thread thread) {
    return UNSAFE.getLongVolatile(thread, TID_OFFSET);
  }

  // Unsafe mechanics
  private static final Unsafe UNSAFE;
  private static final long TID_OFFSET;

  static {
    try {
      UNSAFE = createUnsafe();
      Class<?> tk = Thread.class;
      TID_OFFSET = UNSAFE.objectFieldOffset
              (tk.getDeclaredField("tid"));
    } catch (Exception e) {
      throw new Error(e);
    }
  }

  public static Unsafe createUnsafe() {
    try {
      Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
      Field field = unsafeClass.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      Unsafe unsafe = (Unsafe) field.get(null);
      return unsafe;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
