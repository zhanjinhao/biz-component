package cn.addenda.component.allocator;

/**
 * 此接口的作用是从资源池分配/释放一个对象。至于资源池里存的是什么由具体的实现确定。
 *
 * @author addenda
 * @since 2023/9/1 8:59
 */
public interface Allocator<T> {

  /**
   * 分配一个对象并且后续的分配都获取的是此对象，除非调用{@link Allocator#release(String)}。
   */
  T allocate(String name);

  /**
   * 释放一个对象，即此后调用{@link Allocator#allocate(String)}获取的对象和之前分配的不是同一个对象。
   */
  void release(String name);

}
