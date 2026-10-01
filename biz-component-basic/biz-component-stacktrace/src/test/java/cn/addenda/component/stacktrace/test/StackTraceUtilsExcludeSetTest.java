package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.ClassNameMatcher;
import cn.addenda.component.stacktrace.IdentifierMatcher;
import cn.addenda.component.stacktrace.IdentifierMatcherFactory;
import cn.addenda.component.stacktrace.StackTraceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * {@link StackTraceUtils#readExcludeSet(InputStream)} 的排除清单读取用例。
 * <p>重点覆盖"会静默生成永不匹配 matcher"的输入：UTF-8 BOM、行首行尾空白、空行、{@code --} 注释行。
 * 这些情况下 matcher 一旦读错，排除规则会静默失效，调用方拿到的是框架内部栈帧。
 */
class StackTraceUtilsExcludeSetTest {

  private static final String EXCLUDED_PATH = "META-INF/biz-component-stacktrace.excluded";

  private static final String BOM = String.valueOf((char) 0xFEFF);

  private static final String STACK_TRACE_UTILS = "cn.addenda.component.stacktrace.StackTraceUtils";

  /**
   * Windows 记事本另存 UTF-8 会写入 BOM。BOM 不剔除时该行会变成
   * ClassNamePrefixMatcher("﻿#cn.a.MyClass")，永远匹配不上。
   */
  @Test
  void testBomAtFileStart() throws IOException {
    Set<IdentifierMatcher> excludeSet = read(BOM + "#cn.a.MyClass\n");

    Assertions.assertEquals(1, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "m"));
    Assertions.assertFalse(matches(excludeSet, "cn.a.MyClass2", "m"));
  }

  @Test
  void testBomAfterLeadingWhitespace() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("  " + BOM + "#cn.a.MyClass\n");

    Assertions.assertEquals(1, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "m"));
  }

  /**
   * 行尾空白不剔除时，matcher 会变成 ClassNameMatcher("cn.a.MyClass  ")，永远匹配不上。
   */
  @Test
  void testTrailingAndLeadingWhitespace() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("  #cn.a.MyClass  \t\n \t#cn.a.Other#m2 \n");

    Assertions.assertEquals(2, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "m"));
    // "m2 " 的尾部空白若未剔除，下面这条会是 false
    Assertions.assertTrue(matches(excludeSet, "cn.a.Other", "m2"));
    Assertions.assertFalse(matches(excludeSet, "cn.a.Other", "otherMethod"));
    Assertions.assertFalse(matches(excludeSet, "cn.a.NotExcluded", "m"));
  }

  @Test
  void testBlankLinesAreSkipped() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("\n   \n\t\n#cn.a.MyClass\n\n");

    Assertions.assertEquals(1, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "m"));
  }

  @Test
  void testBlankFileProducesEmptySet() throws IOException {
    Assertions.assertTrue(read("").isEmpty());
    Assertions.assertTrue(read("\n \n\t\n").isEmpty());
    Assertions.assertTrue(read(BOM).isEmpty());
  }

  /**
   * {@code --} 开头的行是注释，即使注释里写着合法标识也不能生效。
   */
  @Test
  void testCommentLinesAreSkipped() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("-- 说明文字\n"
            + "--\n"
            + "--#cn.a.CommentOut#myMethod\n"
            + "--cn.a.CommentOut\n"
            + "#cn.a.MyClass\n");

    Assertions.assertEquals(1, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "m"));
    Assertions.assertFalse(matches(excludeSet, "cn.a.CommentOut", "m"));
    Assertions.assertFalse(matches(excludeSet, "cn.a.CommentOut", "myMethod"));
  }

  /**
   * 注释前的空白和 BOM 都要先被归一化掉，否则 "  -- x" 会被当成标识解析。
   */
  @Test
  void testCommentWithLeadingWhitespaceOrBom() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("   -- 前置空白\n" + BOM + "-- BOM\n" + "\t-- Tab\n");

    Assertions.assertTrue(excludeSet.isEmpty(), "excludeSet=" + excludeSet);
  }

  @Test
  void testCommentOnlyFileProducesEmptySet() throws IOException {
    Assertions.assertTrue(read("-- a\n--b\n--\n-- \n").isEmpty());
  }

  /**
   * 注释与标识混排时，标识部分照常生效——注释不会"吃掉"后面的内容。
   */
  @Test
  void testCommentsInterleavedWithIdentifiers() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("-- 文件头说明\n"
            + "\n"
            + "#cn.a.MyClass\n"
            + "  -- 中间说明\n"
            + "#cn.a.Other#myMethod\n"
            + "-- 文件尾说明\n");

    Assertions.assertEquals(2, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "m"));
    Assertions.assertTrue(matches(excludeSet, "cn.a.Other", "myMethod"));
  }

  /**
   * 只有行首的 {@code --} 才是注释：类名/方法名里不可能出现 {@code --}，
   * 但 "a--b" 这种前缀标识仍然按标识解析（避免把前缀匹配里的连字符误判成注释）。
   */
  @Test
  void testDashInsideIdentifierIsNotComment() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("#cn.a.MyClass#my--Method\n");

    Assertions.assertEquals(1, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "my--Method"));
  }

  /**
   * 排除清单按 UTF-8 解码，不受平台默认字符集影响（例如 GBK）。
   */
  @Test
  void testUtf8Decoding() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("#cn.addenda.测试类#测试方法\n");

    Assertions.assertEquals(1, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.addenda.测试类", "测试方法"));
  }

  @Test
  void testCrLfLineEndings() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("#cn.a.MyClass\r\n#cn.a.Other\r\n");

    Assertions.assertEquals(2, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "m"));
    Assertions.assertTrue(matches(excludeSet, "cn.a.Other", "m"));
  }

  /**
   * 三类标识格式都要走通。
   */
  @Test
  void testAllIdentifierFormats() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("#cn.a.MyClass\n#cn.a.MyClass#myMethod\ncn.a.prefix\n");

    Assertions.assertEquals(3, excludeSet.size());
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "anyMethod"));
    Assertions.assertTrue(matches(excludeSet, "cn.a.MyClass", "myMethod"));
    Assertions.assertTrue(matches(excludeSet, "cn.a.prefix.Any", "anyMethod"));
  }

  /**
   * 读取的是同一个 java 对象语义：ClassNameMatcher 会去重，重复行不会撑大集合。
   */
  @Test
  void testDuplicatedLinesAreDeduplicated() throws IOException {
    Set<IdentifierMatcher> excludeSet = read("#cn.a.MyClass\n#cn.a.MyClass\n  #cn.a.MyClass  \n");

    Assertions.assertEquals(1, excludeSet.size());
    Assertions.assertEquals(ClassNameMatcher.class, excludeSet.iterator().next().getClass());
  }

  /**
   * 真实资源文件的端到端校验：能读到、能解析、且 StackTraceUtils 自身被排除。
   * <p>一旦读取链路（BOM/空白/字符集）出问题，本模块会返回自己的内部栈帧而不是调用方栈帧。
   */
  @Test
  void testModuleResourceIsReadable() throws IOException {
    InputStream inputStream = StackTraceUtils.class.getClassLoader().getResourceAsStream(EXCLUDED_PATH);
    Assertions.assertNotNull(inputStream, "not found: " + EXCLUDED_PATH);

    Set<IdentifierMatcher> excludeSet = StackTraceUtils.readExcludeSet(inputStream);

    Assertions.assertTrue(matches(excludeSet, STACK_TRACE_UTILS, "getCallerInfo"),
            "StackTraceUtils 必须排除自己，否则 getCallerInfo 返回的是内部栈帧。excludeSet=" + excludeSet);
    Assertions.assertTrue(matches(excludeSet, "java.lang.Thread", "run"));
  }

  /**
   * 静态初始化读到的排除集合必须包含 StackTraceUtils 自身。
   */
  @Test
  void testDefaultExcludeSetContainsStackTraceUtils() {
    Assertions.assertTrue(
            IdentifierMatcherFactory.match(StackTraceUtils.getDefaultExcludeSet(),
                    element(STACK_TRACE_UTILS, "getCallerInfo")),
            "defaultExcludeSet=" + StackTraceUtils.getDefaultExcludeSet());
  }

  private static Set<IdentifierMatcher> read(String content) throws IOException {
    return StackTraceUtils.readExcludeSet(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
  }

  private static boolean matches(Set<IdentifierMatcher> excludeSet, String className, String methodName) {
    return IdentifierMatcherFactory.match(excludeSet, element(className, methodName));
  }

  private static StackTraceElement element(String className, String methodName) {
    return new StackTraceElement(className, methodName, null, -1);
  }
}
