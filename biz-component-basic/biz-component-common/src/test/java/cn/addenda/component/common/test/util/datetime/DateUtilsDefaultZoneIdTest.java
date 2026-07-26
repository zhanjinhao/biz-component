package cn.addenda.component.common.test.util.datetime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.time.ZoneId;

/**
 * 验证 {@code biz.component.timezone} 系统属性对 defaultZoneId 初始化的影响。
 * 通过独立 ClassLoader 控制 DateUtils 类加载时机，模拟不同属性值场景。
 */
class DateUtilsDefaultZoneIdTest {

  private static final String CLASS_NAME = "cn.addenda.component.common.util.datetime.DateUtils";
  private static URL[] classpathUrls;

  @BeforeAll
  static void initClasspath() {
    classpathUrls = ((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs();
  }

  // ==================================================================
  //  正常属性值
  // ==================================================================

  @Test
  void testProperty_Valid() throws Exception {
    System.setProperty("biz.component.timezone", "Asia/Shanghai");
    ZoneId zoneId = loadFreshAndGetDefaultZoneId();
    Assertions.assertEquals(ZoneId.of("Asia/Shanghai"), zoneId);
  }

  @Test
  void testProperty_AnotherValid() throws Exception {
    System.setProperty("biz.component.timezone", "America/New_York");
    ZoneId zoneId = loadFreshAndGetDefaultZoneId();
    Assertions.assertEquals(ZoneId.of("America/New_York"), zoneId);
  }

  // ==================================================================
  //  空 / null
  // ==================================================================

  @Test
  void testProperty_Null() throws Exception {
    System.clearProperty("biz.component.timezone");
    ZoneId zoneId = loadFreshAndGetDefaultZoneId();
    Assertions.assertEquals(ZoneId.systemDefault(), zoneId);
  }

  @Test
  void testProperty_Empty() throws Exception {
    System.setProperty("biz.component.timezone", "");
    ZoneId zoneId = loadFreshAndGetDefaultZoneId();
    Assertions.assertEquals(ZoneId.systemDefault(), zoneId);
  }

  // ==================================================================
  //  无效属性值回退
  // ==================================================================

  @Test
  void testProperty_InvalidThrows() {
    System.setProperty("biz.component.timezone", "invalid-zone-id");
    Assertions.assertThrows(java.lang.ExceptionInInitializerError.class,
        () -> loadFreshAndGetDefaultZoneId());
  }

  // ==================================================================
  //  相对时区格式
  // ==================================================================

  @Test
  void testProperty_OffsetFormat() throws Exception {
    System.setProperty("biz.component.timezone", "+08:00");
    ZoneId zoneId = loadFreshAndGetDefaultZoneId();
    Assertions.assertEquals(ZoneId.of("+08:00"), zoneId);
  }

  @Test
  void testProperty_NegativeOffset() throws Exception {
    System.setProperty("biz.component.timezone", "-05:00");
    ZoneId zoneId = loadFreshAndGetDefaultZoneId();
    Assertions.assertEquals(ZoneId.of("-05:00"), zoneId);
  }

  // ==================================================================
  //  helpers
  // ==================================================================

  /**
   * 在独立 ClassLoader 中加载 DateUtils 并读取其 defaultZoneId。
   *
   * <h3>为什么不能直接调 DateUtils？</h3>
   * DateUtils 的 static 块在类加载时执行，读取 {@code System.getProperty("biz.component.timezone")}
   * 并赋值给 {@code defaultZoneId}（final）。SystemClassLoader 早已加载过该类，
   * 之后再调 {@code setProperty} 也不会触发 static 块重跑。
   *
   * <h3>解决思路</h3>
   * 每次调用创建一个全新的 URLClassLoader：
   * <ol>
   *   <li>Parent 设为 {@code SystemClassLoader.getParent()}（即 ExtClassLoader），
   *       它不包含应用类，双亲委派时不会返回已加载的旧 DateUtils</li>
   *   <li>URLs 复用 SystemClassLoader 的 classpath，保证能找到 DateUtils 及其依赖</li>
   *   <li>新 ClassLoader 首次加载 DateUtils 时 static 块重新执行，
   *       此时 {@code System.setProperty} 已将属性写入全局 JVM，初始化逻辑读到新值</li>
   * </ol>
   */
  private static ZoneId loadFreshAndGetDefaultZoneId() throws Exception {
    ClassLoader parent = ClassLoader.getSystemClassLoader().getParent();
    URLClassLoader cl = new URLClassLoader(classpathUrls, parent);
    // 新 ClassLoader 加载 DateUtils → 触发 static {} → 读取已写入的 System.property
    Class<?> c = cl.loadClass(CLASS_NAME);
    Field field = c.getDeclaredField("defaultZoneId");
    field.setAccessible(true);
    return (ZoneId) field.get(null);
  }
}
