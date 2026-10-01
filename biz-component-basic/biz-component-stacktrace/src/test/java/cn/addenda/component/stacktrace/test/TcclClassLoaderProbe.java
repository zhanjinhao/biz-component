package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.IdentifierMatcher;
import cn.addenda.component.stacktrace.IdentifierMatcherFactory;
import cn.addenda.component.stacktrace.StackTraceUtils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

/**
 * 由 {@link StackTraceUtilsTcclTest} 在<b>新 JVM</b> 里启动的探针。
 * <p>必须在独立的 JVM 里跑：排除集合是首次加载后缓存的，同一个 JVM 里换 TCCL 不会重新加载。
 * <p>启动参数 {@code args[0]} 是一个"只有 TCCL 才看得到"的清单目录（故意不放在子进程的 classpath 上）。
 * <p>输出行：{@code TCCL_ONLY_ENTRY=true|false}、{@code SELF_EXCLUSION=true|false}（纯 ASCII，方便断言）。
 */
public class TcclClassLoaderProbe {

  private static final String TCCL_ONLY_CLASS = "cn.addenda.myapp.MyService";

  private static final String TCCL_ONLY_METHOD = "doSomething";

  private static final String STACK_TRACE_UTILS = "cn.addenda.component.stacktrace.StackTraceUtils";

  public static void main(String[] args) throws Exception {
    // 模拟 devtools：TCCL 是本类加载器的子加载器，额外能看到 args[0] 里的清单
    ClassLoader parent = TcclClassLoaderProbe.class.getClassLoader();
    URL tcclOnlyDir = new File(args[0]).toURI().toURL();
    ClassLoader contextClassLoader = new URLClassLoader(new URL[]{tcclOnlyDir}, parent);
    Thread.currentThread().setContextClassLoader(contextClassLoader);

    // 首次使用：这里才会解析 classloader 并加载排除清单
    StackTraceUtils.validate();
    Set<IdentifierMatcher> excludeSet = StackTraceUtils.getDefaultExcludeSet();

    System.out.println("TCCL_ONLY_ENTRY=" + matches(excludeSet, TCCL_ONLY_CLASS, TCCL_ONLY_METHOD));
    System.out.println("SELF_EXCLUSION=" + matches(excludeSet, STACK_TRACE_UTILS, "getCallerInfo"));
  }

  private static boolean matches(Set<IdentifierMatcher> excludeSet, String className, String methodName) {
    return IdentifierMatcherFactory.match(excludeSet, new StackTraceElement(className, methodName, null, -1));
  }

}
