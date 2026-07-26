package cn.addenda.component.common.test.util.string;

import cn.addenda.component.common.util.string.StrFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.helpers.MessageFormatter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class MessageFormatterVsStrFormatUtilsTest {

  private static String mf(String tql, Object... args) {
    return MessageFormatter.arrayFormat(tql, args).getMessage();
  }

  private static String sf(CharSequence tql, Object... args) {
    return StrFormatUtils.format(tql, args);
  }

  private static void assertEq(String tql, Object... args) {
    String mfr = mf(tql, args);
    String sfr = sf(tql, args);
    Assertions.assertEquals(mfr, sfr,
        () -> "MISMATCH for template: '" + tql + "'\n  MF: " + mfr + "\n  SF: " + sfr);
  }

  // ==================== basic ====================

  @Test
  void testBasic() {
    assertEq("hello {}", "world");
  }

  @Test
  void testMultiplePlaceholders() {
    assertEq("{} {} {}", "a", "b", "c");
  }

  @Test
  void testFewerParamsThanPlaceholders() {
    assertEq("{} {} {}", "a", "b");
  }

  @Test
  void testMoreParamsThanPlaceholders() {
    assertEq("{} {}", "a", "b", "c");
  }

  @Test
  void testNoPlaceholders() {
    assertEq("hello world", "a", "b");
  }

  @Test
  void testPlaceholderAtStart() {
    assertEq("{}hello", "world");
  }

  @Test
  void testPlaceholderAtEnd() {
    assertEq("hello{}", "world");
  }

  @Test
  void testConsecutivePlaceholders() {
    assertEq("{}{}", "a", "b");
  }

  // ==================== null ====================

  @Test
  void testNullParam() {
    assertEq("{}", (Object) null);
  }

  @Test
  void testNullParamsBetween() {
    assertEq("{} {} {}", "a", null, "c");
  }

  // ==================== various types ====================

  @Test
  void testIntParam() {
    assertEq("{}", 42);
  }

  @Test
  void testLongParam() {
    assertEq("{}", 999L);
  }

  @Test
  void testDoubleParam() {
    assertEq("{}", 3.14);
  }

  @Test
  void testBooleanParam() {
    assertEq("{}", true);
  }

  @Test
  void testObjectParam() {
    assertEq("{}", new Object() {
      @Override
      public String toString() {
        return "FooBar";
      }
    });
  }

  // ==================== escape ====================

  @Test
  void testEscape_SingleEscape() {
    assertEq("value \\{}", "ignored");
  }

  @Test
  void testEscape_DoubleEscape() {
    assertEq("value \\\\{}", "world");
  }

  @Test
  void testEscape_SingleAtStart() {
    assertEq("\\{}", "ignored");
  }

  @Test
  void testEscape_DoubleAtStart() {
    assertEq("\\\\{}", "world");
  }

  @Test
  void testEscape_Mixed() {
    assertEq("a \\{} b \\\\{} c {}", "x", "y", "z");
  }

  @Test
  void testEscape_Adjacent() {
    assertEq("\\{} \\\\{}", "x");
  }

  @Test
  void testEscape_TripleBackslash() {
    assertEq("\\\\\\{}", "a");
  }

  // ==================== edge ====================

  @Test
  void testEmptyTemplate() {
    assertEq("");
  }

  @Test
  void testBlankTemplate() {
    assertEq("", "a");
  }

  @Test
  void testWhitespaceOnlyTemplate() {
    assertEq("   ", "a");
  }

  @Test
  void testTemplateWithOnlyPlaceholders() {
    assertEq("{}{}{}", "a", "b", "c");
  }

  @Test
  void testTemplateWithOnlyEscape() {
    assertEq("\\\\");
  }

  @Test
  void testTemplateWithTextAndNoPlaceholder() {
    assertEq("hello, world!", "a", "b", "c");
  }

  @Test
  void testManyPlaceholders() {
    assertEq("{}-{}-{}-{}-{}", "1", "2", "3", "4", "5");
  }

  // ==================== various types ====================

  @Test
  void testCharParam() {
    assertEq("{}", 'X');
  }

  @Test
  void testFloatParam() {
    assertEq("{}", 3.14f);
  }

  @Test
  void testShortParam() {
    assertEq("{}", (short) 99);
  }

  @Test
  void testByteParam() {
    assertEq("{}", (byte) 127);
  }

  @Test
  void testMultiTypeMix() {
    assertEq("{} {} {} {} {}", 42, "hello", true, 3.14, 'Z');
  }

  // ==================== null edge cases ====================

  @Test
  void testAllNullArgs() {
    assertEq("{} {} {}", null, null, null);
  }

  @Test
  void testFirstArgNull() {
    assertEq("{} {} {}", null, "b", "c");
  }

  @Test
  void testLastArgNull() {
    assertEq("{} {} {}", "a", "b", null);
  }

  // ==================== array param ====================

  @Test
  void testArrayParam_int() {
    int[] arr = {1, 2, 3};
    String mfr = mf("{}", (Object) arr);
    String sfr = sf("{}", (Object) arr);
    Assertions.assertEquals(mfr, sfr);
  }

  @Test
  void testArrayParam_String() {
    String[] arr = {"x", "y"};
    String mfr = mf("{} {}", (Object) arr);
    String sfr = sf("{} {}", (Object) arr);
    Assertions.assertEquals(mfr, sfr);
  }

  // ==================== StrFormatUtilsTest parity ====================

  @Test
  void testNullTemplate() {
    assertEq(null);
  }

  @Test
  void testNullParams() {
    assertEq("hello {}", (Object[]) null);
  }

  @Test
  void testEmptyParams() {
    assertEq("hello {}");
  }

  @Test
  void testPlaceholderAtStart_EmptyArg() {
    assertEq("{}hello", "");
  }

  @Test
  void testPlaceholderAtEnd_EmptyArg() {
    assertEq("hello{}", "");
  }

  @Test
  void testIntAndLong() {
    assertEq("{} {}", 42, 99L);
  }

  @Test
  void testBooleanWithPrefix() {
    assertEq("value: {}", true);
  }

  @Test
  void testDoubleWithPrefix() {
    assertEq("pi={}", 3.14);
  }

  @Test
  void testCharWithPrefix() {
    assertEq("A", 'A');
  }

  @Test
  void testEscape_AtPosition1() {
    assertEq("\\{}b", "a");
  }

  @Test
  void testEscape_ThenNormalPlaceholder() {
    assertEq("\\{} {}", "world");
  }

  @Test
  void testNoPlaceholder_WithArgs() {
    assertEq("plain text", "a", "b");
  }

  @Test
  void testNullInArgs_WithMixedText() {
    assertEq("a {} {} c", "a", null, "c");
  }

  @Test
  void testByteArray_AsParam() {
    byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
    String mfr = mf("{}", (Object) data);
    String sfr = sf("{}", (Object) data);
    Assertions.assertEquals(mfr, sfr);
  }

  @Test
  void testByteWrapperArray_AsParam() {
    Byte[] data = {'h', 'e', 'l', 'l', 'o'};
    String mfr = mf("{}", (Object) data);
    String sfr = sf("{}", (Object) data);
    Assertions.assertEquals(mfr, sfr);
  }

  @Test
  void testByteBuffer_AsParam() {
    ByteBuffer buffer = ByteBuffer.wrap("buf".getBytes(StandardCharsets.UTF_8));
    String mfr = mf("{}", buffer);
    String sfr = sf("{}", buffer);
    Assertions.assertEquals(mfr, sfr);
  }

  @Test
  void testNullObj() {
    String mfr = mf("{}", (Object) null);
    String sfr = sf("{}", (Object) null);
    Assertions.assertEquals(mfr, sfr);
  }

  @Test
  void testStringArray_AsParam() {
    String[] arr = {"x", "y"};
    String mfr = mf("{}", (Object) arr);
    String sfr = sf("{}", (Object) arr);
    Assertions.assertEquals(mfr, sfr);
  }

  @Test
  void testStringObj() {
    assertEq("hello", "hello");
  }
}
