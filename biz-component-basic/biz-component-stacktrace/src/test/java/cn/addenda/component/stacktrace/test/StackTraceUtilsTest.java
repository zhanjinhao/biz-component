package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.IdentifierMatcher;
import cn.addenda.component.stacktrace.StackTraceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Supplier;

class StackTraceUtilsTest {

  @Test
  void test() {
    Set<IdentifierMatcher> defaultFilterPrefixSet = StackTraceUtils.getDefaultExcludeSet();
    System.out.println(defaultFilterPrefixSet);
  }

  @Test
  void test0() {
    String callerInfo = StackTraceUtils.getCallerInfo();
    System.out.println(callerInfo);
    Assertions.assertEquals("StackTraceUtilsTest#test0", callerInfo);
  }

  Supplier<String> a1 = new Supplier<String>() {
    @Override
    public String get() {
      return StackTraceUtils.getCallerInfo(true, true, true);
    }
  };
  Supplier<String> b1 = () -> {
    return StackTraceUtils.getCallerInfo(true, true, true);
  };
  Supplier<String> c1 = () -> StackTraceUtils.getCallerInfo(true, true, true);

  @Test
  void test1() {
    Supplier<String> a = new Supplier<String>() {
      @Override
      public String get() {
        return StackTraceUtils.getCallerInfo(true, true, true);
      }
    };
    Supplier<String> b = () -> {
      return StackTraceUtils.getCallerInfo(true, true, true);
    };
    Supplier<String> c = () -> StackTraceUtils.getCallerInfo(true, true, true);


    Assertions.assertEquals("StackTraceUtilsTest#test1", a.get());
    Assertions.assertEquals("StackTraceUtilsTest#test1", b.get());
    Assertions.assertEquals("StackTraceUtilsTest#test1", c.get());

    Assertions.assertEquals("StackTraceUtilsTest#test1", a1.get());
    Assertions.assertEquals("StackTraceUtilsTest#test1", b1.get());
    Assertions.assertEquals("StackTraceUtilsTest#test1", c1.get());
  }


  Supplier<String> a2 = new Supplier<String>() {
    @Override
    public String get() {
      return StackTraceUtils.getCallerInfo(true, false, false);
    }
  };
  Supplier<String> b2 = () -> {
    return StackTraceUtils.getCallerInfo(true, false, false);
  };
  Supplier<String> c2 = () -> StackTraceUtils.getCallerInfo(true, false, false);

  @Test
  void test2() {
    Supplier<String> a = new Supplier<String>() {
      @Override
      public String get() {
        return StackTraceUtils.getCallerInfo(true, false, false);
      }
    };
    Supplier<String> b = () -> {
      return StackTraceUtils.getCallerInfo(true, false, false);
    };

    Supplier<String> c = () -> StackTraceUtils.getCallerInfo(true, false, false);

    Assertions.assertEquals("StackTraceUtilsTest$4#get", a.get());
    Assertions.assertEquals("StackTraceUtilsTest#lambda$test2$6", b.get());
    Assertions.assertEquals("StackTraceUtilsTest#lambda$test2$7", c.get());

    Assertions.assertEquals("StackTraceUtilsTest$3#get", a2.get());
    Assertions.assertEquals("StackTraceUtilsTest#lambda$new$4", b2.get());
    Assertions.assertEquals("StackTraceUtilsTest#lambda$new$5", c2.get());
  }

  Supplier<String> a3 = new Supplier<String>() {
    @Override
    public String get() {
      return StackTraceUtils.getDetailedCallerInfo(true, true, true);
    }
  };
  Supplier<String> b3 = () -> {
    return StackTraceUtils.getDetailedCallerInfo(true, true, true);
  };
  Supplier<String> c3 = () -> StackTraceUtils.getDetailedCallerInfo(true, true, true);

  @Test
  void test3() {
    Supplier<String> a = new Supplier<String>() {
      @Override
      public String get() {
        return StackTraceUtils.getDetailedCallerInfo(true, true, true);
      }
    };
    Supplier<String> b = () -> {
      return StackTraceUtils.getDetailedCallerInfo(true, true, true);
    };
    Supplier<String> c = () -> StackTraceUtils.getDetailedCallerInfo(true, true, true);

    Assertions.assertEquals("StackTraceUtilsTest#test3 of StackTraceUtilsTest.java:120", a.get());
    Assertions.assertEquals("StackTraceUtilsTest#test3 of StackTraceUtilsTest.java:121", b.get());
    Assertions.assertEquals("StackTraceUtilsTest#test3 of StackTraceUtilsTest.java:122", c.get());

    Assertions.assertEquals("StackTraceUtilsTest#test3 of StackTraceUtilsTest.java:124", a3.get());
    Assertions.assertEquals("StackTraceUtilsTest#test3 of StackTraceUtilsTest.java:125", b3.get());
    Assertions.assertEquals("StackTraceUtilsTest#test3 of StackTraceUtilsTest.java:126", c3.get());
  }

  Supplier<String> a4 = new Supplier<String>() {
    @Override
    public String get() {
      return StackTraceUtils.getDetailedCallerInfo(true, false, false);
    }
  };
  Supplier<String> b4 = () -> {
    return StackTraceUtils.getDetailedCallerInfo(true, false, false);
  };
  Supplier<String> c4 = () -> StackTraceUtils.getDetailedCallerInfo(true, false, false);

  @Test
  void test4() {
    Supplier<String> a = new Supplier<String>() {
      @Override
      public String get() {
        return StackTraceUtils.getDetailedCallerInfo(true, false, false);
      }
    };
    Supplier<String> b = () -> {
      return StackTraceUtils.getDetailedCallerInfo(true, false, false);
    };
    Supplier<String> c = () -> StackTraceUtils.getDetailedCallerInfo(true, false, false);

    Assertions.assertEquals("StackTraceUtilsTest$8#get of StackTraceUtilsTest.java:145", a.get());
    Assertions.assertEquals("StackTraceUtilsTest#lambda$test4$14 of StackTraceUtilsTest.java:149", b.get());
    Assertions.assertEquals("StackTraceUtilsTest#lambda$test4$15 of StackTraceUtilsTest.java:151", c.get());

    Assertions.assertEquals("StackTraceUtilsTest$7#get of StackTraceUtilsTest.java:132", a4.get());
    Assertions.assertEquals("StackTraceUtilsTest#lambda$new$12 of StackTraceUtilsTest.java:136", b4.get());
    Assertions.assertEquals("StackTraceUtilsTest#lambda$new$13 of StackTraceUtilsTest.java:138", c4.get());
  }


  @Test
  void test5() throws Exception {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Callable<String> callable = new Callable<String>() {
      @Override
      public String call() {
        return StackTraceUtils.getDetailedCallerInfo(false, false);
      }
    };

    Future<String> submit = executorService.submit(callable);

    try {
      String o = submit.get();
      Assertions.assertEquals("StackTraceUtilsTest$9#call of StackTraceUtilsTest.java:170", o);
    } finally {
      executorService.shutdown();
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
  }

}
