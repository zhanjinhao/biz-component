package cn.addenda.component.allocator;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/9/12 20:58
 */
public interface ExpiredAllocator<T> extends Allocator<T> {

  /**
   * 分配一个对象并且后续的分配都获取的是此对象，
   * 除非调用{@link Allocator#release(String)} 或达到默认的的过期时间。
   */
  T allocateWithDefaultTtl(String name);

  /**
   * 分配一个对象并且后续的分配都获取的是此对象，
   * 除非调用{@link Allocator#release(String)} 或达到指定的的过期时间。
   */
  T allocate(String name, TimeUnit timeUnit, long ttl);

  @Setter
  @Getter
  @ToString
  @Builder
  class Param {
    private String name;
    private TimeUnit timeUnit;
    private long ttl;
  }

}
