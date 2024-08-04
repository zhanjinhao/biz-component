package cn.addenda.component.lockhelper;

import cn.addenda.component.jdk.allocator.lock.LockAllocator;
import cn.addenda.component.jdk.lambda.FunctionConverter;
import cn.addenda.component.jdk.lambda.TRunnable;
import cn.addenda.component.jdk.lambda.TSupplier;
import cn.addenda.component.jdk.util.ExceptionUtils;

import java.util.concurrent.locks.Lock;

/**
 * @author addenda
 * @since 2022/12/1 18:55
 */
public class LockHelper extends LockAspectSupport {

  public static final String SYSTEM_BUSY = "系统繁忙，请稍后重试！";

  public LockHelper(String namespace, LockAllocator<? extends Lock> lockAllocator) {
    this.setNamespace(namespace);
    this.setLockAllocator(lockAllocator);
  }

  /**
   * 最简单的加锁场景，arguments[0] 是 key
   */
  public <R> R lock(TSupplier<R> supplier, Object... arguments) {
    LockedAttr attr = LockedAttr.builder().build();
    return lock(attr, supplier, arguments);
  }

  public void lock(TRunnable runnable, Object... arguments) {
    LockedAttr attr = LockedAttr.builder().build();
    lock(attr, runnable, arguments);
  }

  /**
   * 较上一个场景，arguments[0] 是 key，prefix可以指定
   */
  public <R> R lock(String prefix, TSupplier<R> supplier, Object... arguments) {
    LockedAttr attr = LockedAttr.builder()
            .prefix(prefix).build();
    return lock(attr, supplier, arguments);
  }

  public void lock(String prefix, TRunnable runnable, Object... arguments) {
    LockedAttr attr = LockedAttr.builder()
            .prefix(prefix).build();
    lock(attr, runnable, arguments);
  }

  /**
   * 较上一个场景，arguments[0] 是 key，lockFailedMsg和prefix可以指定
   */
  public <R> R lock(String lockFailedMsg, String prefix, TSupplier<R> supplier, Object... arguments) {
    LockedAttr attr = LockedAttr.builder()
            .prefix(prefix).lockFailedMsg(lockFailedMsg).build();
    return lock(attr, supplier, arguments);
  }

  public void lock(String lockFailedMsg, String prefix, TRunnable runnable, Object... arguments) {
    LockedAttr attr = LockedAttr.builder()
            .prefix(prefix).lockFailedMsg(lockFailedMsg).build();
    lock(attr, runnable, arguments);
  }

  /**
   * 较上一个场景，arguments[0] 是 key，rejectServiceException和lockFailedMsg和prefix可以指定
   */
  public <R> R lock(boolean rejectServiceException, String lockFailedMsg, String prefix, TSupplier<R> supplier, Object... arguments) {
    LockedAttr attr = LockedAttr.builder()
            .rejectServiceException(rejectServiceException).prefix(prefix).lockFailedMsg(lockFailedMsg).build();
    return lock(attr, supplier, arguments);
  }

  public void lock(boolean rejectServiceException, String lockFailedMsg, String prefix, TRunnable runnable, Object... arguments) {
    LockedAttr attr = LockedAttr.builder()
            .rejectServiceException(rejectServiceException).prefix(prefix).lockFailedMsg(lockFailedMsg).build();
    lock(attr, runnable, arguments);
  }

  public void lock(LockedAttr attr, TRunnable runnable, Object... arguments) {
    lock(attr, FunctionConverter.toTSupplier(runnable), arguments);
  }

  public <R> R lock(LockedAttr attr, TSupplier<R> supplier, Object... arguments) {
    if (arguments == null || arguments.length == 0 || attr == null || supplier == null) {
      throw new LockHelperException("参数不能为空！");
    }
    try {
      return (R) invokeWithinLock(attr, arguments, supplier::get, null);
    } catch (Throwable throwable) {
      throw ExceptionUtils.wrapAsRuntimeException(throwable, LockHelperException.class);
    }
  }

}
