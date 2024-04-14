package cn.addenda.component.redis.ratelimiter;

import org.redisson.RedissonRateLimiter;
import org.redisson.api.RFuture;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateLimiterConfig;
import org.redisson.api.RateType;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.protocol.RedisCommand;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.command.CommandAsyncExecutor;
import org.redisson.misc.CompletableFutureWrapper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/9/18 22:28
 */
public class ExpiredRedissonRateLimiter extends RedissonRateLimiter {

  private final RateType mode;
  private final long rate;
  private final long rateInterval;
  private final RateIntervalUnit rateIntervalUnit;

  /**
   * 单位 ms
   */
  private final long ttl;

  public ExpiredRedissonRateLimiter(
          CommandAsyncExecutor commandExecutor, String name,
          RateType mode, long rate, long rateInterval, RateIntervalUnit rateIntervalUnit) {
    super(commandExecutor, name);
    this.mode = mode;
    this.rate = rate;
    this.rateInterval = rateInterval;
    this.rateIntervalUnit = rateIntervalUnit;
    this.ttl = rateIntervalUnit.toMillis(rateInterval) * 2;
  }

  public ExpiredRedissonRateLimiter(
          CommandAsyncExecutor commandExecutor, String name,
          RateType mode, long rate, long rateInterval, RateIntervalUnit rateIntervalUnit,
          TimeUnit timeUnit, long ttl) {
    super(commandExecutor, name);
    this.mode = mode;
    this.rate = rate;
    this.rateInterval = rateInterval;
    this.rateIntervalUnit = rateIntervalUnit;
    this.ttl = timeUnit.toMillis(ttl);
  }

  @Override
  public RateLimiterConfig getConfig() {
    return get(getConfigAsync());
  }

  @Override
  public long availablePermits() {
    return get(availablePermitsAsync());
  }

  @Override
  public RFuture<Boolean> expireAsync(long timeToLive, TimeUnit timeUnit, String param, String... keys) {
    return super.expireAsync(timeToLive, timeUnit, param, keys);
  }

  @Override
  protected RFuture<Boolean> expireAtAsync(long timestamp, String param, String... keys) {
    return super.expireAtAsync(timestamp, param, keys);
  }

  @Override
  public RFuture<Boolean> clearExpireAsync() {
    return super.clearExpireAsync();
  }

  @Override
  public RFuture<Boolean> deleteAsync() {
    return super.deleteAsync();
  }

  @Override
  public boolean tryAcquire() {
    return tryAcquire(1);
  }

  @Override
  public RFuture<Boolean> tryAcquireAsync() {
    return tryAcquireAsync(1L);
  }

  @Override
  public boolean tryAcquire(long permits) {
    return get(tryAcquireAsync(RedisCommands.EVAL_NULL_BOOLEAN, permits));
  }

  @Override
  public RFuture<Boolean> tryAcquireAsync(long permits) {
    return tryAcquireAsync(RedisCommands.EVAL_NULL_BOOLEAN, permits);
  }

  @Override
  public void acquire() {
    get(acquireAsync());
  }

  @Override
  public RFuture<Void> acquireAsync() {
    return acquireAsync(1);
  }

  @Override
  public void acquire(long permits) {
    get(acquireAsync(permits));
  }

  @Override
  public RFuture<Void> acquireAsync(long permits) {
    CompletionStage<Void> f = tryAcquireAsync(permits, -1, null).thenApply(res -> null);
    return new CompletableFutureWrapper<>(f);
  }

  @Override
  public boolean tryAcquire(long timeout, TimeUnit unit) {
    return get(tryAcquireAsync(timeout, unit));
  }

  @Override
  public RFuture<Boolean> tryAcquireAsync(long timeout, TimeUnit unit) {
    return tryAcquireAsync(1, timeout, unit);
  }

  @Override
  public boolean tryAcquire(long permits, long timeout, TimeUnit unit) {
    return get(tryAcquireAsync(permits, timeout, unit));
  }

  @Override
  public RFuture<Boolean> tryAcquireAsync(long permits, long timeout, TimeUnit unit) {
    long timeoutInMillis = -1;
    if (timeout >= 0) {
      timeoutInMillis = unit.toMillis(timeout);
    }
    CompletableFuture<Boolean> f = tryAcquireAsync(permits, timeoutInMillis);
    return new CompletableFutureWrapper<>(f);
  }

  private CompletableFuture<Boolean> tryAcquireAsync(long permits, long timeoutInMillis) {
    long s = System.currentTimeMillis();
    RFuture<Long> future = tryAcquireAsync(RedisCommands.EVAL_LONG, permits);
    return future.thenCompose(delay -> {
      if (delay == null) {
        return CompletableFuture.completedFuture(true);
      }

      if (timeoutInMillis == -1) {
        CompletableFuture<Boolean> f = new CompletableFuture<>();
        commandExecutor.getConnectionManager().getGroup().schedule(() -> {
          CompletableFuture<Boolean> r = tryAcquireAsync(permits, timeoutInMillis);
          commandExecutor.transfer(r, f);
        }, delay, TimeUnit.MILLISECONDS);
        return f;
      }

      long el = System.currentTimeMillis() - s;
      long remains = timeoutInMillis - el;
      if (remains <= 0) {
        return CompletableFuture.completedFuture(false);
      }

      CompletableFuture<Boolean> f = new CompletableFuture<>();
      if (remains < delay) {
        commandExecutor.getConnectionManager().getGroup().schedule(() -> {
          f.complete(false);
        }, remains, TimeUnit.MILLISECONDS);
      } else {
        long start = System.currentTimeMillis();
        commandExecutor.getConnectionManager().getGroup().schedule(() -> {
          long elapsed = System.currentTimeMillis() - start;
          if (remains <= elapsed) {
            f.complete(false);
            return;
          }

          CompletableFuture<Boolean> r = tryAcquireAsync(permits, remains - elapsed);
          commandExecutor.transfer(r, f);
        }, delay, TimeUnit.MILLISECONDS);
      }
      return f;
    }).toCompletableFuture();
  }

  @Override
  public boolean trySetRate(RateType type, long rate, long rateInterval, RateIntervalUnit unit) {
    return get(trySetRateAsync(type, rate, rateInterval, unit));
  }

  @Override
  public RFuture<Boolean> trySetRateAsync(RateType type, long rate, long rateInterval, RateIntervalUnit unit) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setRate(RateType type, long rate, long rateInterval, RateIntervalUnit unit) {
    get(setRateAsync(type, rate, rateInterval, unit));
  }

  @Override
  public RFuture<Void> setRateAsync(RateType type, long rate, long rateInterval, RateIntervalUnit unit) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RFuture<RateLimiterConfig> getConfigAsync() {
    RateLimiterConfig rateLimiterConfig = new RateLimiterConfig(mode, rateInterval, rate);
    return new CompletableFutureWrapper<>(rateLimiterConfig);
  }

  @Override
  public RFuture<Long> availablePermitsAsync() {
    return commandExecutor.evalWriteAsync(getRawName(), LongCodec.INSTANCE, RedisCommands.EVAL_LONG,
            "local rate = ARGV[2];"
                    + "local interval = ARGV[3];"
                    + "local type = ARGV[4];"
                    + "assert(rate ~= false and interval ~= false and type ~= false, 'RateLimiter is not initialized')"
                    + "local valueName = KEYS[2];"
                    + "local permitsName = KEYS[4];"
                    + "if type == '1' then "
                    + "     valueName = KEYS[3];"
                    + "     permitsName = KEYS[5];"
                    + "end;"
                    + "local currentValue = redis.call('get', valueName); "
                    + "if currentValue == false then "
                    + "     redis.call('set', valueName, rate); "
                    + "     redis.call('pexpire', valueName, ARGV[5] * 2);"
                    + "     return rate; "
                    + "else "
                    + "     local expiredValues = redis.call('zrangebyscore', permitsName, 0, tonumber(ARGV[1]) - interval); "
                    + "     local released = 0; "
                    + "     for i, v in ipairs(expiredValues) do "
                    + "         local random, permits = struct.unpack('Bc0I', v);"
                    + "         released = released + permits;"
                    + "     end; "
                    + "     if released > 0 then "
                    + "         redis.call('zremrangebyscore', permitsName, 0, tonumber(ARGV[1]) - interval); "
                    + "         currentValue = tonumber(currentValue) + released; "
                    + "         redis.call('set', valueName, currentValue);"
                    + "         redis.call('pexpire', valueName, ARGV[5] * 2);"
                    + "     end;"
                    + "     return currentValue; "
                    + "end;",
            Arrays.asList(getRawName(), getValueName(), getClientValueName(), getPermitsName(), getClientPermitsName()),
            System.currentTimeMillis(), rate, rateIntervalUnit.toMillis(rateInterval), mode, ttl);
  }

  private <T> RFuture<T> tryAcquireAsync(RedisCommand<T> command, Long value) {
    byte[] random = new byte[8];
    ThreadLocalRandom.current().nextBytes(random);

    return commandExecutor.evalWriteAsync(getRawName(), LongCodec.INSTANCE, command,
            "local rate = ARGV[4];"
                    + "local interval = ARGV[5];"
                    + "local type = ARGV[6];"
                    + "assert(rate ~= false and interval ~= false and type ~= false, 'RateLimiter is not initialized')"
                    + "local valueName = KEYS[2];"
                    + "local permitsName = KEYS[4];"
                    + "if type == '1' then "
                    + "     valueName = KEYS[3];"
                    + "     permitsName = KEYS[5];"
                    + "end;"
                    + "assert(tonumber(rate) >= tonumber(ARGV[1]), 'Requested permits amount could not exceed defined rate'); "
                    + "local currentValue = redis.call('get', valueName); "
                    + "local res;"
                    + "if currentValue ~= false then "
                    + "     local expiredValues = redis.call('zrangebyscore', permitsName, 0, tonumber(ARGV[2]) - interval); "
                    + "     local released = 0; "
                    + "     for i, v in ipairs(expiredValues) do "
                    + "         local random, permits = struct.unpack('Bc0I', v);"
                    + "         released = released + permits;"
                    + "     end; "
                    + "     if released > 0 then "
                    + "         redis.call('zremrangebyscore', permitsName, 0, tonumber(ARGV[2]) - interval); "
                    + "         if tonumber(currentValue) + released > tonumber(rate) then "
                    + "             currentValue = tonumber(rate) - redis.call('zcard', permitsName); "
                    + "         else "
                    + "             currentValue = tonumber(currentValue) + released; "
                    + "         end; "
                    + "         redis.call('set', valueName, currentValue);"
                    + "     end;"
                    + "     if tonumber(currentValue) < tonumber(ARGV[1]) then "
                    + "         local firstValue = redis.call('zrange', permitsName, 0, 0, 'withscores'); "
                    + "         res = 3 + interval - (tonumber(ARGV[2]) - tonumber(firstValue[2]));"
                    + "     else "
                    + "         redis.call('zadd', permitsName, ARGV[2], struct.pack('Bc0I', string.len(ARGV[3]), ARGV[3], ARGV[1])); "
                    + "         redis.call('decrby', valueName, ARGV[1]); "
                    + "         res = nil; "
                    + "     end; "
                    + "else "
                    + "     redis.call('set', valueName, rate); "
                    + "     redis.call('zadd', permitsName, ARGV[2], struct.pack('Bc0I', string.len(ARGV[3]), ARGV[3], ARGV[1])); "
                    + "     redis.call('decrby', valueName, ARGV[1]); "
                    + "     res = nil; "
                    + "end;"
                    + "redis.call('pexpire', valueName, ARGV[7] * 2);"
                    + "redis.call('pexpire', permitsName, ARGV[7] * 2);"
                    + "return res;",
            Arrays.asList(getRawName(), getValueName(), getClientValueName(), getPermitsName(), getClientPermitsName()),
            value, System.currentTimeMillis(), random, rate, rateIntervalUnit.toMillis(rateInterval), mode, ttl);
  }

  public String getValueName() {
    return suffixName(getRawName(), "value");
  }

  public String getClientValueName() {
    return suffixName(getValueName(), commandExecutor.getConnectionManager().getId());
  }

  public String getPermitsName() {
    return suffixName(getRawName(), "permits");
  }

  public String getClientPermitsName() {
    return suffixName(getPermitsName(), commandExecutor.getConnectionManager().getId());
  }

  @Override
  public String toString() {
    return "ExpiredRedissonRateLimiter{" +
            "mode=" + mode +
            ", rate=" + rate +
            ", rateInterval=" + rateInterval +
            ", rateIntervalUnit=" + rateIntervalUnit +
            ", ttl=" + ttl +
            "} " + super.toString();
  }
}
