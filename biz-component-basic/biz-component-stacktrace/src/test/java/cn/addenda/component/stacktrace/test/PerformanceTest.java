package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.StackTraceUtils;
import org.junit.jupiter.api.Test;

class PerformanceTest {

  private static final int WARMUP = 3_000;
  private static final int ITERS = 10_000;

  @Test
  void benchmarkRawStackTrace() {
    for (int i = 0; i < WARMUP; i++) {
      new Throwable().getStackTrace();
    }

    long t = System.nanoTime();
    for (int i = 0; i < ITERS; i++) {
      new Throwable().getStackTrace();
    }
    long elapsed = System.nanoTime() - t;

    System.out.println("========== Raw getStackTrace ==========");
    print("new Throwable().getStackTrace()", elapsed, ITERS);
    System.out.println("=======================================");
  }

  @Test
  void benchmarkGetCallerInfoByParams() {
    // warmup
    for (int i = 0; i < WARMUP; i++) {
      StackTraceUtils.getCallerInfo();
      StackTraceUtils.getCallerInfo(true, true, true);
      StackTraceUtils.getCallerInfo("x");
    }

    Object[][] cases = {
        {"() -> default",                (Runnable) () -> StackTraceUtils.getCallerInfo()},
        {"(F,F,F) no-excl",              (Runnable) () -> StackTraceUtils.getCallerInfo(false, false, false)},
        {"(F,T,F) excl-lambda",          (Runnable) () -> StackTraceUtils.getCallerInfo(false, true, false)},
        {"(F,F,T) excl-anon",            (Runnable) () -> StackTraceUtils.getCallerInfo(false, false, true)},
        {"(F,T,T) excl-both",            (Runnable) () -> StackTraceUtils.getCallerInfo(false, true, true)},
        {"(T,F,F) full-name",            (Runnable) () -> StackTraceUtils.getCallerInfo(true, false, false)},
        {"(T,T,F) full+excl-lambda",     (Runnable) () -> StackTraceUtils.getCallerInfo(true, true, false)},
        {"(T,F,T) full+excl-anon",       (Runnable) () -> StackTraceUtils.getCallerInfo(true, false, true)},
        {"(T,T,T) full+excl-both",       (Runnable) () -> StackTraceUtils.getCallerInfo(true, true, true)},
    };

    String[][] excludeCases = {
        {"1 exclude",  "cn.a.Cls1"},
        {"3 excludes", "cn.a.Cls1", "cn.a.Cls2", "cn.a.Cls3"},
        {"5 excludes", "cn.a.Cls1", "cn.a.Cls2", "cn.a.Cls3", "cn.a.Cls4", "cn.a.Cls5"},
    };

    System.out.println("========== getCallerInfo per-call (ns / call) ==========");

    for (Object[] c : cases) {
      benchmarkCall((String) c[0], (Runnable) c[1]);
    }

    for (String[] ec : excludeCases) {
      String label = ec[0];
      String[] excludes = new String[ec.length - 1];
      System.arraycopy(ec, 1, excludes, 0, excludes.length);
      benchmarkCall(label, () -> StackTraceUtils.getCallerInfo(excludes));
    }

    System.out.println("=========================================================");
  }

  @Test
  void benchmarkGetDetailedCallerInfoByParams() {
    // warmup
    for (int i = 0; i < WARMUP; i++) {
      StackTraceUtils.getDetailedCallerInfo();
      StackTraceUtils.getDetailedCallerInfo(true, true, true);
      StackTraceUtils.getDetailedCallerInfo("x");
    }

    Object[][] cases = {
        {"() -> default",                (Runnable) () -> StackTraceUtils.getDetailedCallerInfo()},
        {"(F,F,F) no-excl",              (Runnable) () -> StackTraceUtils.getDetailedCallerInfo(false, false, false)},
        {"(F,T,F) excl-lambda",          (Runnable) () -> StackTraceUtils.getDetailedCallerInfo(false, true, false)},
        {"(F,F,T) excl-anon",            (Runnable) () -> StackTraceUtils.getDetailedCallerInfo(false, false, true)},
        {"(F,T,T) excl-both",            (Runnable) () -> StackTraceUtils.getDetailedCallerInfo(false, true, true)},
        {"(T,F,F) full-name",            (Runnable) () -> StackTraceUtils.getDetailedCallerInfo(true, false, false)},
        {"(T,T,F) full+excl-lambda",     (Runnable) () -> StackTraceUtils.getDetailedCallerInfo(true, true, false)},
        {"(T,F,T) full+excl-anon",       (Runnable) () -> StackTraceUtils.getDetailedCallerInfo(true, false, true)},
        {"(T,T,T) full+excl-both",       (Runnable) () -> StackTraceUtils.getDetailedCallerInfo(true, true, true)},
    };

    String[][] excludeCases = {
        {"1 exclude",  "cn.a.Cls1"},
        {"3 excludes", "cn.a.Cls1", "cn.a.Cls2", "cn.a.Cls3"},
        {"5 excludes", "cn.a.Cls1", "cn.a.Cls2", "cn.a.Cls3", "cn.a.Cls4", "cn.a.Cls5"},
    };

    System.out.println("========== getDetailedCallerInfo per-call (ns / call) ==========");

    for (Object[] c : cases) {
      benchmarkCall((String) c[0], (Runnable) c[1]);
    }

    for (String[] ec : excludeCases) {
      String label = ec[0];
      String[] excludes = new String[ec.length - 1];
      System.arraycopy(ec, 1, excludes, 0, excludes.length);
      benchmarkCall(label, () -> StackTraceUtils.getDetailedCallerInfo(excludes));
    }

    System.out.println("==============================================================");
  }

  private static void benchmarkCall(String label, Runnable task) {
    long t = System.nanoTime();
    for (int i = 0; i < ITERS; i++) {
      task.run();
    }
    long elapsed = System.nanoTime() - t;
    print(label, elapsed, ITERS);
  }

  private static void print(String label, long nanos, int iters) {
    System.out.printf("  %-28s %8.0f ns/op  (%6.2f ms total)%n",
            label, (double) nanos / iters, nanos / 1_000_000.0);
  }
}

