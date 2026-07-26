package cn.addenda.component.common.test.util.string;

import cn.addenda.component.common.util.string.StrFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class StrFormatUtilsTest {

  // ==================== format(CharSequence, Object...) ====================

  @Test
  void testFormat_NullTemplate() {
    Assertions.assertNull(StrFormatUtils.format(null));
  }

  @Test
  void testFormat_NullParams() {
    Assertions.assertEquals("hello {}", StrFormatUtils.format("hello {}", (Object[]) null));
  }

  @Test
  void testFormat_EmptyParams() {
    Assertions.assertEquals("hello {}", StrFormatUtils.format("hello {}"));
  }

  @Test
  void testFormat_EmptyTemplate() {
    Assertions.assertEquals("", StrFormatUtils.format("", "a", "b"));
  }

  @Test
  void testFormat_BasicReplacement() {
    Assertions.assertEquals("hello world", StrFormatUtils.format("hello {}", "world"));
  }

  @Test
  void testFormat_MultiplePlaceholders() {
    Assertions.assertEquals("a b c", StrFormatUtils.format("{} {} {}", "a", "b", "c"));
  }

  @Test
  void testFormat_FewerParamsThanPlaceholders() {
    Assertions.assertEquals("a b {}", StrFormatUtils.format("{} {} {}", "a", "b"));
  }

  @Test
  void testFormat_MoreParamsThanPlaceholders() {
    Assertions.assertEquals("a b", StrFormatUtils.format("{} {}", "a", "b", "c"));
  }

  @Test
  void testFormat_NoPlaceholders() {
    Assertions.assertEquals("hello world", StrFormatUtils.format("hello world", "a", "b"));
  }

  @Test
  void testFormat_NullParam() {
    Assertions.assertEquals("null", StrFormatUtils.format("{}", (Object) null));
  }

  @Test
  void testFormat_IntAndLong() {
    Assertions.assertEquals("42 99", StrFormatUtils.format("{} {}", 42, 99L));
  }

  @Test
  void testFormat_PlaceholderAtStart() {
    Assertions.assertEquals("hello", StrFormatUtils.format("{}hello", ""));
  }

  @Test
  void testFormat_PlaceholderAtEnd() {
    Assertions.assertEquals("hello", StrFormatUtils.format("hello{}", ""));
  }

  @Test
  void testFormat_ConsecutivePlaceholders() {
    Assertions.assertEquals("ab", StrFormatUtils.format("{}{}", "a", "b"));
  }

  // ==================== escape handling ====================

  @Test
  void testFormat_SingleEscape_BecomesLiteral() {
    Assertions.assertEquals("value {}", StrFormatUtils.format("value \\{}", "ignored"));
  }

  @Test
  void testFormat_DoubleEscape_PassesThrough() {
    Assertions.assertEquals("value \\world", StrFormatUtils.format("value \\\\{}", "world"));
  }

  @Test
  void testFormat_SingleEscapeAtStart() {
    Assertions.assertEquals("{}", StrFormatUtils.format("\\{}", "ignored"));
  }

  @Test
  void testFormat_DoubleEscapeAtStart() {
    Assertions.assertEquals("\\world", StrFormatUtils.format("\\\\{}", "world"));
  }

  // ==================== formatWith ====================

  @Test
  void testFormatWith_NullPattern() {
    Assertions.assertNull(StrFormatUtils.formatWith(null, "{}", false, null, "a"));
  }

  @Test
  void testFormatWith_BlankPattern() {
    Assertions.assertEquals("", StrFormatUtils.formatWith("", "{}", false, null, "a"));
  }

  @Test
  void testFormatWith_NullPlaceHolder() {
    Assertions.assertEquals("hello {}", StrFormatUtils.formatWith("hello {}", null, false, null, "a"));
  }

  @Test
  void testFormatWith_BlankPlaceHolder() {
    Assertions.assertEquals("hello {}", StrFormatUtils.formatWith("hello {}", "  ", false, null, "a"));
  }

  @Test
  void testFormatWith_EmptyArgArray() {
    Assertions.assertEquals("hello {}", StrFormatUtils.formatWith("hello {}", "{}", false, null));
  }

  @Test
  void testFormatWith_CustomPlaceHolder() {
    Assertions.assertEquals("hello world", StrFormatUtils.formatWith("hello @", "@", false, null, "world"));
  }

  @Test
  void testFormatWith_MultiplePlaceholdersCustom() {
    Assertions.assertEquals("a 1 b 2 c", StrFormatUtils.formatWith("a @ b @ c", "@", false, null, "1", "2"));
  }

  @Test
  void testFormatWith_MoreParamsThanPlaceholders() {
    Assertions.assertEquals("a c", StrFormatUtils.formatWith("a c", "@", false, null, "x"));
  }

  // ==================== format with decodeByte ====================

  @Test
  void testFormatWith_DecodeByte_False() {
    byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
    String result = StrFormatUtils.formatWith("{}", "{}", false, null, data);
    Assertions.assertEquals("[104, 101, 108, 108, 111]", result);
  }

  @Test
  void testFormatWith_DecodeByte_True_WithCharset() {
    byte[] data = "你好".getBytes(StandardCharsets.UTF_8);
    String result = StrFormatUtils.formatWith("{}", "{}", true, StandardCharsets.UTF_8, data);
    Assertions.assertEquals("你好", result);
  }

  @Test
  void testFormatWith_DecodeByte_True_NullCharset() {
    byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
    String result = StrFormatUtils.formatWith("{}", "{}", true, null, data);
    Assertions.assertEquals("hello", result);
  }

  @Test
  void testFormatWith_DecodeByte_True_ByteWrapperArray() {
    Byte[] data = {'h', 'e', 'l', 'l', 'o'};
    String result = StrFormatUtils.formatWith("{}", "{}", true, StandardCharsets.UTF_8, (Object) data);
    Assertions.assertEquals("hello", result);
  }

  @Test
  void testFormatWith_DecodeByte_True_ByteBuffer() {
    ByteBuffer buffer = ByteBuffer.wrap("world".getBytes(StandardCharsets.UTF_8));
    String result = StrFormatUtils.formatWith("{}", "{}", true, StandardCharsets.UTF_8, buffer);
    Assertions.assertEquals("world", result);
  }

  @Test
  void testFormatWith_DecodeByte_True_ByteWrapper_NullElement() {
    Byte[] data = {'h', null, 'l', 'l', 'o'};
    String result = StrFormatUtils.formatWith("{}", "{}", true, StandardCharsets.UTF_8, (Object) data);
    Assertions.assertEquals("h\uFFFDllo", result);
  }

  // ==================== escape edges ====================

  @Test
  void testFormatWith_EscapeAtPosition1() {
    Assertions.assertEquals("{}b", StrFormatUtils.formatWith("\\{}b", "{}", false, null, "a"));
  }

  @Test
  void testFormatWith_TripleBackslash() {
    Assertions.assertEquals("\\\\a", StrFormatUtils.formatWith("\\\\\\{}", "{}", false, null, "a"));
  }

  @Test
  void testFormatWith_EscapeThenNormal() {
    Assertions.assertEquals("{} world", StrFormatUtils.formatWith("\\{} {}", "{}", false, null, "world"));
  }

  // ==================== formatWith: no placeholder in pattern ====================

  @Test
  void testFormatWith_NoPlaceholder_NoHandledPosition() {
    Assertions.assertEquals("plain text", StrFormatUtils.formatWith("plain text", "{}", false, null, "a"));
  }

  @Test
  void testFormatWith_NoPlaceholder_WithHandledPosition() {
    Assertions.assertEquals("start replaced end noPH", StrFormatUtils.formatWith("start {} end noPH", "{}", false, null, "replaced", "extra"));
  }

  // ==================== formatWith: null args in argArray ====================

  @Test
  void testFormatWith_NullInArgs() {
    Assertions.assertEquals("a a null c", StrFormatUtils.formatWith("a {} {} c", "{}", false, null, "a", null, "c"));
  }

  @Test
  void testFormatWith_AllNullArgs() {
    Assertions.assertEquals("null null", StrFormatUtils.formatWith("{} {}", "{}", false, null, null, null));
  }

  // ==================== format: whitespace / blank templates ====================

  @Test
  void testFormat_BlankTemplateWithArgs() {
    Assertions.assertEquals("", StrFormatUtils.format("", "a", "b"));
  }

  @Test
  void testFormat_WhitespaceOnlyTemplate() {
    Assertions.assertEquals("   ", StrFormatUtils.format("   ", "a"));
  }

  // ==================== format: various arg types ====================

  @Test
  void testFormat_BooleanParam() {
    Assertions.assertEquals("value: true", StrFormatUtils.format("value: {}", true));
  }

  @Test
  void testFormat_DoubleParam() {
    Assertions.assertEquals("pi=3.14", StrFormatUtils.format("pi={}", 3.14));
  }

  @Test
  void testFormat_CharParam() {
    Assertions.assertEquals("A", StrFormatUtils.format("{}", 'A'));
  }

  @Test
  void testFormat_MultiTypeMix() {
    Assertions.assertEquals("42 hello true", StrFormatUtils.format("{} {} {}", 42, "hello", true));
  }

  @Test
  void testFormat_StringArrayParam() {
    String[] arr = {"x", "y"};
    String result = StrFormatUtils.format("{}", (Object) arr);
    Assertions.assertEquals("[x, y]", result);
  }

  // ==================== toStr directly ====================

  @Test
  void testToStr_NullObj() {
    Assertions.assertNull(StrFormatUtils.toStr(null, false, null));
  }

  @Test
  void testToStr_StringObj() {
    Assertions.assertEquals("hello", StrFormatUtils.toStr("hello", false, null));
  }

  @Test
  void testToStr_ObjectWithToString() {
    Assertions.assertEquals("42", StrFormatUtils.toStr(42, false, null));
  }

  @Test
  void testToStr_ByteArray_NullCharset() {
    byte[] data = "hi".getBytes(StandardCharsets.UTF_8);
    Assertions.assertEquals("hi", StrFormatUtils.toStr(data, true, null));
  }

  @Test
  void testToStr_ByteArray_WithCharset() {
    byte[] data = "你好".getBytes(StandardCharsets.UTF_8);
    Assertions.assertEquals("你好", StrFormatUtils.toStr(data, true, StandardCharsets.UTF_8));
  }

  @Test
  void testToStr_ByteArray_DecodeByteFalse() {
    byte[] data = "hi".getBytes(StandardCharsets.UTF_8);
    String result = StrFormatUtils.toStr(data, false, null);
    Assertions.assertEquals("[104, 105]", result);
  }

  @Test
  void testToStr_ByteWrapper_NullElement() {
    Byte[] data = {'h', null, 'i'};
    Assertions.assertEquals("h\uFFFDi", StrFormatUtils.toStr(data, true, StandardCharsets.UTF_8));
  }

  @Test
  void testToStr_ByteBuffer_NullCharset() {
    ByteBuffer buffer = ByteBuffer.wrap("buf".getBytes(StandardCharsets.UTF_8));
    String result = StrFormatUtils.toStr(buffer, true, null);
    Assertions.assertEquals("buf", result);
  }

  @Test
  void testToStr_ByteBuffer_ExplicitCharset() {
    ByteBuffer buffer = ByteBuffer.wrap("explicit".getBytes(StandardCharsets.UTF_8));
    String result = StrFormatUtils.toStr(buffer, true, StandardCharsets.UTF_8);
    Assertions.assertEquals("explicit", result);
  }

  @Test
  void testToStr_ByteBuffer_DecodeByteFalse() {
    ByteBuffer buffer = ByteBuffer.wrap("buf".getBytes(StandardCharsets.UTF_8));
    String result = StrFormatUtils.toStr(buffer, false, null);
    Assertions.assertTrue(result.startsWith("java.nio.HeapByteBuffer"));
  }
}
