package cn.addenda.component.allocator;

/**
 * @author addenda
 * @since 2023/9/25 23:05
 */
public interface NamedExpiredAllocator<T> extends ExpiredAllocator<T> {

  String getName();

}
