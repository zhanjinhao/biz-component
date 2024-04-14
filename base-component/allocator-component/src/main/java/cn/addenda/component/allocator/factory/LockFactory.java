package cn.addenda.component.allocator.factory;

import java.util.concurrent.locks.Lock;

/**
 * Factory里有N把锁。通过{@link LockFactory#getLock(Object)} 可以获取与k对应的那把锁。
 *
 * @author addenda
 * @since 2023/6/4 16:29
 */
public interface LockFactory<K> {

  Lock getLock(K k);

}
