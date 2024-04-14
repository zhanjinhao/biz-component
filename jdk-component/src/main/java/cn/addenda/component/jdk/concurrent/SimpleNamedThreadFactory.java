package cn.addenda.component.jdk.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * copy from apache-dubbo, simplify its implementation.
 * InternalThreadFactory.
 */
public class SimpleNamedThreadFactory implements ThreadFactory {

  private final String mPrefix;

  public SimpleNamedThreadFactory(String mPrefix) {
    this.mPrefix = mPrefix;
  }

  private final AtomicInteger mThreadNum = new AtomicInteger(1);

  @Override
  public Thread newThread(Runnable runnable) {
    String name = mPrefix + "-" + mThreadNum.getAndIncrement();
    return new Thread(runnable, name);
  }

}
