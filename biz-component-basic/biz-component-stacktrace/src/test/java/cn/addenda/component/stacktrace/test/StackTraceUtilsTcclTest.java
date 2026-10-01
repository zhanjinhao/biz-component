package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.StackTraceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * 验证排除清单的发现用的是 TCCL（{@link StackTraceUtils#validate()} / {@code getDefaultExcludeSet()}）。
 * <p>场景取自 Spring Boot devtools：依赖 jar 在 base loader，应用自己的 classpath 在它的子加载器
 * {@code RestartClassLoader} 里。子能看到父、父看不到子，所以必须用 TCCL 才能读到应用自己声明的清单。
 * <p><b>必须在新的 JVM 里跑</b>：排除集合首次加载后就缓存了，同一个 JVM 里改 TCCL 不会重新加载。
 * 因此这里 fork 一个子进程，由 {@link TcclClassLoaderProbe} 在里面设置 TCCL 并断言。
 */
class StackTraceUtilsTcclTest {

  @TempDir
  Path tempDir;

  @Test
  void testExcludeListIsDiscoveredByTccl() throws Exception {
    // 一份"只有 TCCL 才看得到"的清单：故意不放进子进程的 classpath
    Path tcclOnlyDir = tempDir.resolve("tccl-only");
    Path resource = tcclOnlyDir.resolve("META-INF/biz-component-stacktrace.excluded");
    Files.createDirectories(resource.getParent());
    Files.write(resource, "-- 只有 TCCL 看得到\n#cn.addenda.myapp.MyService#doSomething\n"
            .getBytes(StandardCharsets.UTF_8));

    String output = runInNewJvm(TcclClassLoaderProbe.class, tcclOnlyDir.toString());

    Assertions.assertTrue(output.contains("TCCL_ONLY_ENTRY=true"),
            "TCCL 上的清单必须被发现（用加载本类的加载器会漏掉）。子进程输出：\n" + output);
    Assertions.assertTrue(output.contains("SELF_EXCLUSION=true"),
            "切到 TCCL 后不能丢掉父加载器上的清单与内置排除项。子进程输出：\n" + output);
    // validate() 打出的 discoveryClassLoader 必须就是 TCCL（探针里是个 URLClassLoader），
    // 而不是加载本类的 AppClassLoader —— 这行日志是排查"清单没生效"的第一手信息
    Assertions.assertTrue(output.contains("discoveryClassLoader:[java.net.URLClassLoader@"),
            "discoveryClassLoader 应该是 TCCL。子进程输出：\n" + output);
    Assertions.assertTrue(output.contains("thisClassLoader:["),
            "子进程输出：\n" + output);
  }

  /**
   * 在**新 JVM** 里执行 {@code probeClass#main(args)}，classpath 只放模块类和探针类。
   *
   * @return 子进程的合并输出（stdout + stderr）
   */
  private static String runInNewJvm(Class<?> probeClass, String... args) throws IOException, InterruptedException, URISyntaxException {
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command().add(javaBin());
    processBuilder.command().add("-cp");
    processBuilder.command().add(classpathOf(StackTraceUtils.class) + File.pathSeparator + classpathOf(probeClass));
    processBuilder.command().add(probeClass.getName());
    for (String arg : args) {
      processBuilder.command().add(arg);
    }
    processBuilder.redirectErrorStream(true);

    Process process = processBuilder.start();
    boolean finished = process.waitFor(60, TimeUnit.SECONDS);
    if (!finished) {
      process.destroyForcibly();
      Assertions.fail("子进程超时未结束：" + processBuilder.command());
    }

    byte[] output = readAll(process.getInputStream());
    String text = new String(output, StandardCharsets.UTF_8);
    Assertions.assertEquals(0, process.exitValue(), "子进程退出码非 0：\n" + text);
    return text;
  }

  private static byte[] readAll(java.io.InputStream inputStream) throws IOException {
    java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
    byte[] chunk = new byte[4096];
    int read;
    while ((read = inputStream.read(chunk)) != -1) {
      buffer.write(chunk, 0, read);
    }
    return buffer.toByteArray();
  }

  private static String javaBin() {
    Path bin = Paths.get(System.getProperty("java.home"), "bin", "java");
    Path exe = Paths.get(bin + ".exe");
    return Files.exists(exe) ? exe.toString() : bin.toString();
  }

  /**
   * 取类所在的位置（target/classes、target/test-classes），不用 {@code java.class.path}——
   * surefire fork 出来的 JVM 里它可能只有 surefirebooter 的 jar。
   */
  private static String classpathOf(Class<?> clazz) throws URISyntaxException {
    return Paths.get(clazz.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
  }

}
