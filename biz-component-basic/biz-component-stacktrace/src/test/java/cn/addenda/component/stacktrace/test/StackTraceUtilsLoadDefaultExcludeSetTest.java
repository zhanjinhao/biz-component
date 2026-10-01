package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.IdentifierMatcher;
import cn.addenda.component.stacktrace.IdentifierMatcherFactory;
import cn.addenda.component.stacktrace.StackTraceException;
import cn.addenda.component.stacktrace.StackTraceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

/**
 * {@link StackTraceUtils#loadDefaultExcludeSet(ClassLoader)} 的用例。
 * <p>排除清单是严格配置：文件读不到、某一行写得不对，都必须抛出 {@link StackTraceException} 中断启动，
 * 不允许静默跳过（静默跳过的后果是排除规则悄悄失效，调用方拿到的是框架内部栈帧）。
 * <p>异常必须能定位到具体文件、行号和非法内容——历史行为只有一句
 * "Can not open file"，既没行号也没内容，定位基本靠猜。
 */
class StackTraceUtilsLoadDefaultExcludeSetTest {

  private static final String EXCLUDED_PATH = "META-INF/biz-component-stacktrace.excluded";

  private static final String STACK_TRACE_UTILS = "cn.addenda.component.stacktrace.StackTraceUtils";

  @TempDir
  Path tempDir;

  /**
   * classpath 上完全没有排除清单时，自排除项必须依然存在。
   * <p>否则 {@code getCallerInfo()} 会返回 {@code StackTraceUtils#determineStackTraceElement} 这种内部栈帧。
   */
  @Test
  void testSelfExclusionIsBuiltInWhenNoFileExists() {
    Set<IdentifierMatcher> excludeSet = StackTraceUtils.loadDefaultExcludeSet(classLoaderOf());

    Assertions.assertEquals(1, excludeSet.size(), "excludeSet=" + excludeSet);
    Assertions.assertTrue(matches(excludeSet, STACK_TRACE_UTILS, "getCallerInfo"));
  }

  /**
   * 非法行必须中断加载，异常里带文件、行号、非法内容。
   */
  @Test
  void testMalformedLineThrowsWithLocation() throws IOException {
    URL url = urlOf("malformed.excluded", "-- 注释\n#cn.a.MyClass\n##\n#a#b#c\n");

    StackTraceException e = Assertions.assertThrows(StackTraceException.class, () ->
            StackTraceUtils.loadDefaultExcludeSet(classLoaderOf(url)));

    Assertions.assertTrue(e.getMessage().contains("[##]"), "message=" + e.getMessage());
    Assertions.assertTrue(e.getMessage().contains("line 3"), "message=" + e.getMessage());
    Assertions.assertTrue(e.getMessage().contains(url.toString()), "message=" + e.getMessage());
  }

  /**
   * 清单文件读不到（打包时被过滤、路径不存在）也要中断，不能退化成一个空的排除集合。
   */
  @Test
  void testUnreadableFileThrows() throws IOException {
    URL missing = tempDir.resolve("not-exists.excluded").toUri().toURL();

    StackTraceException e = Assertions.assertThrows(StackTraceException.class, () ->
            StackTraceUtils.loadDefaultExcludeSet(classLoaderOf(missing)));

    Assertions.assertTrue(e.getMessage().contains(missing.toString()), "message=" + e.getMessage());
  }

  /**
   * {@code getResources} 本身失败（类加载器异常）也要中断。
   */
  @Test
  void testGetResourcesFailureThrows() {
    ClassLoader classLoader = new ClassLoader() {
      @Override
      public Enumeration<URL> getResources(String name) throws IOException {
        throw new IOException("mock failure");
      }
    };

    StackTraceException e = Assertions.assertThrows(StackTraceException.class, () ->
            StackTraceUtils.loadDefaultExcludeSet(classLoader));

    Assertions.assertTrue(e.getMessage().contains("Can not list exclude files"), "message=" + e.getMessage());
    Assertions.assertNotNull(e.getCause());
  }

  @Test
  void testMultipleFilesAreMerged() throws IOException {
    URL first = urlOf("first.excluded", "#cn.a.First\n");
    URL second = urlOf("second.excluded", "\n-- 注释\n#cn.a.Second#myMethod\n");

    Set<IdentifierMatcher> excludeSet = StackTraceUtils.loadDefaultExcludeSet(classLoaderOf(first, second));

    Assertions.assertEquals(3, excludeSet.size(), "excludeSet=" + excludeSet);
    Assertions.assertTrue(matches(excludeSet, "cn.a.First", "m"));
    Assertions.assertTrue(matches(excludeSet, "cn.a.Second", "myMethod"));
  }

  /**
   * 模拟父子 classloader 结构：parent 里放一份清单，child 里放另一份，child 的 parent 是 parent。
   * <p>Java 的约束：{@code getResources} 只能看到自己和祖先的资源，<b>看不到子加载器的</b>。
   * 所以清单落在 child 里时，用 parent 去发现永远发现不到——而且是静默的：
   * {@code getResources} 返回空枚举，不抛异常，严格模式也拦不住。
   */
  @Test
  void testParentClassLoaderCanNotSeeChildResources() throws IOException {
    URL parentUrl = excludedDir("parent", "#cn.a.ParentEntry\n");
    URL childUrl = excludedDir("child", "#cn.a.ChildEntry\n");

    ClassLoader parentLoader = new URLClassLoader(new URL[]{parentUrl}, null);
    ClassLoader childLoader = new URLClassLoader(new URL[]{childUrl}, parentLoader);

    Set<IdentifierMatcher> parentSet = StackTraceUtils.loadDefaultExcludeSet(parentLoader);
    Set<IdentifierMatcher> childSet = StackTraceUtils.loadDefaultExcludeSet(childLoader);

    // 各自能看到自己那份（parent 还要加上内置的自排除项）
    Assertions.assertEquals(2, parentSet.size(), "parentSet=" + parentSet);
    Assertions.assertEquals(3, childSet.size(), "childSet=" + childSet);
    Assertions.assertTrue(matches(parentSet, "cn.a.ParentEntry", "m"));
    Assertions.assertTrue(matches(childSet, "cn.a.ChildEntry", "m"));

    // child 能看到 parent 的（getResources 是父级优先）
    Assertions.assertTrue(matches(childSet, "cn.a.ParentEntry", "m"));

    // parent 看不到 child 的
    Assertions.assertFalse(matches(parentSet, "cn.a.ChildEntry", "m"), "parentSet=" + parentSet);
  }

  /**
   * 真实形状：parent 就是加载 {@code StackTraceUtils} 的 loader（Spring Boot fat jar 里的
   * {@code LaunchedURLClassLoader}），child 是应用自己的 classpath（devtools 里的 {@code RestartClassLoader}）。
   * <p>本用例固定的是 Java 层面的可见性约束本身（与实现无关）。{@code resolveClassLoader()} 因此优先用
   * TCCL，devtools 下选中的是 child——这一条由 {@link StackTraceUtilsTcclTest} 在独立 JVM 里验证。
   */
  @Test
  void testChildContributionIsInvisibleToStackTraceUtilsClassLoader() throws IOException {
    ClassLoader parentLoader = StackTraceUtils.class.getClassLoader();
    URL childUrl = excludedDir("devtools-child", "#cn.addenda.myapp.MyService#doSomething\n");
    ClassLoader childLoader = new URLClassLoader(new URL[]{childUrl}, parentLoader);

    Assertions.assertFalse(
            matches(StackTraceUtils.loadDefaultExcludeSet(parentLoader), "cn.addenda.myapp.MyService", "doSomething"),
            "parent（= 当前实现选中的 loader）不应该看到 child 的清单");
    Assertions.assertTrue(
            matches(StackTraceUtils.loadDefaultExcludeSet(childLoader), "cn.addenda.myapp.MyService", "doSomething"),
            "child 应该同时看到自己和 parent 的清单");
  }

  /**
   * 真实 classpath：模块自带的排除清单必须能被读到并解析出来。
   */
  @Test
  void testRealClassLoader() {
    Set<IdentifierMatcher> excludeSet = StackTraceUtils.loadDefaultExcludeSet(
            StackTraceUtils.class.getClassLoader());

    Assertions.assertTrue(matches(excludeSet, STACK_TRACE_UTILS, "getCallerInfo"), "excludeSet=" + excludeSet);
    Assertions.assertTrue(matches(excludeSet, "java.lang.Thread", "run"), "excludeSet=" + excludeSet);
  }

  /**
   * 造一个"classpath 根目录"，里面放一份排除清单，返回目录的 URL（可当作 URLClassLoader 的根）。
   */
  private URL excludedDir(String dirName, String content) throws IOException {
    Path dir = Files.createDirectories(tempDir.resolve(dirName));
    Path resource = dir.resolve(EXCLUDED_PATH);
    Files.createDirectories(resource.getParent());
    Files.write(resource, content.getBytes(StandardCharsets.UTF_8));
    return dir.toUri().toURL();
  }

  private URL urlOf(String fileName, String content) throws IOException {
    Path path = tempDir.resolve(fileName);
    Files.write(path, content.getBytes(StandardCharsets.UTF_8));
    return path.toUri().toURL();
  }

  private static ClassLoader classLoaderOf(URL... urls) {
    return new ClassLoader() {
      @Override
      public Enumeration<URL> getResources(String name) {
        return Collections.enumeration(Arrays.asList(urls));
      }
    };
  }

  private static boolean matches(Set<IdentifierMatcher> excludeSet, String className, String methodName) {
    return IdentifierMatcherFactory.match(excludeSet, new StackTraceElement(className, methodName, null, -1));
  }

}
