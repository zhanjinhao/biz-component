package cn.addenda.component.ratelimiter.allcator;

import cn.addenda.component.allocator.ReferenceCountAllocator;
import cn.addenda.component.allocator.factory.ReentrantSegmentLockFactory;
import cn.addenda.component.ratelimiter.TokenBucketRateLimiter;

import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
public class TokenBucketRateLimiterAllocator
        extends ReferenceCountAllocator<TokenBucketRateLimiter>
        implements RateLimiterAllocator<TokenBucketRateLimiter> {

  private final long capacity;

  private final long permitsPerSecond;

  public TokenBucketRateLimiterAllocator(long capacity, long permitsPerSecond) {
    super(new ReentrantSegmentLockFactory());
    this.capacity = capacity;
    this.permitsPerSecond = permitsPerSecond;
  }

  @Override
  protected Function<String, TokenBucketRateLimiter> referenceFunction() {
    return s -> new TokenBucketRateLimiter(capacity, permitsPerSecond);
  }
}
