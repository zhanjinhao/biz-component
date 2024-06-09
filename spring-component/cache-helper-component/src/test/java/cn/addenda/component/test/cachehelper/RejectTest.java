package cn.addenda.component.test.cachehelper;

import cn.addenda.component.jdk.util.SleepUtils;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class RejectTest {

  static ExecutorService executorService = new ThreadPoolExecutor(
          1, 1, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1));

  public static void main(String[] args) {
    CompletableFuture.supplyAsync(new Supplier() {
      @Override
      public Object get() {
        SleepUtils.sleep(TimeUnit.DAYS, 1);
        return null;
      }

    }, executorService);
    CompletableFuture.supplyAsync(new Supplier() {
      @Override
      public Object get() {
        SleepUtils.sleep(TimeUnit.DAYS, 1);
        return null;
      }

    }, executorService);
    CompletableFuture.supplyAsync(new Supplier() {
      @Override
      public Object get() {
        SleepUtils.sleep(TimeUnit.DAYS, 1);
        return null;
      }

    }, executorService);
  }

}
