package cn.addenda.component.ratelimiter.allcator;

import cn.addenda.component.allocator.Allocator;
import cn.addenda.component.ratelimiter.RateLimiter;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
public interface RateLimiterAllocator<T extends RateLimiter> extends Allocator<T> {
}
