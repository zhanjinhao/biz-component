package cn.addenda.component.common.lambda.executeonce;

public class ExecuteOnceRunnable implements Runnable {

  private final Runnable runnable;
  private boolean executed;

  public ExecuteOnceRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public void run() {
    synchronized (this) {
      if (!executed) {
        runnable.run();
        executed = true;
      }
    }
  }

  public static ExecuteOnceRunnable of(Runnable runnable) {
    return new ExecuteOnceRunnable(runnable);
  }

  @Override
  public String toString() {
    return "ExecuteOnceRunnable{" +
            "runnable=" + runnable +
            ", executed=" + executed +
            '}';
  }
}
