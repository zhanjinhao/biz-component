package cn.addenda.component.lockhelper;

import cn.addenda.component.redis.allocator.RedissonLockAllocator;

/**
 * @author addenda
 * @since 2023/6/4 11:53
 */
public class RedissonLockHelper extends LockHelper {

  public RedissonLockHelper(String namespace, RedissonLockAllocator redissonLockAllocator) {
    super(namespace, redissonLockAllocator);
  }

}
