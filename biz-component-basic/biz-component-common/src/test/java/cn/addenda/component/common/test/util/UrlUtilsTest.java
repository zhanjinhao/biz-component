package cn.addenda.component.common.test.util;

import cn.addenda.component.common.util.UrlUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author addenda
 * @since 2026/8/1
 */
class UrlUtilsTest {

  // ==================== encode(String) ====================

  @Test
  void testEncode_Normal() {
    Assertions.assertEquals("Hello+World", UrlUtils.encode("Hello World"));
  }

  @Test
  void testEncode_Chinese() {
    Assertions.assertEquals("%E4%B8%AD%E6%96%87", UrlUtils.encode("中文"));
  }

  @Test
  void testEncode_SpecialChars() {
    String encoded = UrlUtils.encode("a=1&b=2");
    Assertions.assertTrue(encoded.contains("%3D"));
    Assertions.assertTrue(encoded.contains("%26"));
  }

  @Test
  void testEncode_EmptyString() {
    Assertions.assertEquals("", UrlUtils.encode(""));
  }

  @Test
  void testEncode_Null() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> UrlUtils.encode(null));
  }

  // ==================== decode(String) ====================

  @Test
  void testDecode_Normal() {
    Assertions.assertEquals("Hello World", UrlUtils.decode("Hello+World"));
  }

  @Test
  void testDecode_Chinese() {
    Assertions.assertEquals("中文", UrlUtils.decode("%E4%B8%AD%E6%96%87"));
  }

  @Test
  void testDecode_PercentEncoding() {
    Assertions.assertEquals("a=1&b=2", UrlUtils.decode("a%3D1%26b%3D2"));
  }

  @Test
  void testDecode_EmptyString() {
    Assertions.assertEquals("", UrlUtils.decode(""));
  }

  @Test
  void testDecode_Null() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> UrlUtils.decode(null));
  }

  // ==================== encode + decode roundtrip ====================

  @Test
  void testRoundtrip_Ascii() {
    String original = "Hello World! @#$";
    Assertions.assertEquals(original, UrlUtils.decode(UrlUtils.encode(original)));
  }

  @Test
  void testRoundtrip_Chinese() {
    String original = "你好，世界！";
    Assertions.assertEquals(original, UrlUtils.decode(UrlUtils.encode(original)));
  }

  // ==================== encode(String, String) ====================

  @Test
  void testEncodeWithEncoding_UTF8() {
    Assertions.assertEquals("%E4%B8%AD%E6%96%87", UrlUtils.encode("中文", "UTF-8"));
  }

  @Test
  void testEncodeWithEncoding_GBK() {
    Assertions.assertEquals("%D6%D0%CE%C4", UrlUtils.encode("中文", "GBK"));
  }

  @Test
  void testEncodeWithEncoding_NullStr() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> UrlUtils.encode(null, "UTF-8"));
  }

  @Test
  void testEncodeWithEncoding_NullEncoding() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> UrlUtils.encode("test", null));
  }

  @Test
  void testEncodeWithEncoding_InvalidEncoding() {
    IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> UrlUtils.encode("test", "INVALID_ENCODING"));
    Assertions.assertTrue(ex.getMessage().contains("INVALID_ENCODING"));
    Assertions.assertTrue(ex.getMessage().contains("test"));
    Assertions.assertNotNull(ex.getCause());
  }

  // ==================== decode(String, String) ====================

  @Test
  void testDecodeWithEncoding_UTF8() {
    Assertions.assertEquals("中文", UrlUtils.decode("%E4%B8%AD%E6%96%87", "UTF-8"));
  }

  @Test
  void testDecodeWithEncoding_GBK() {
    Assertions.assertEquals("中文", UrlUtils.decode("%D6%D0%CE%C4", "GBK"));
  }

  @Test
  void testDecodeWithEncoding_NullStr() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> UrlUtils.decode(null, "UTF-8"));
  }

  @Test
  void testDecodeWithEncoding_NullEncoding() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> UrlUtils.decode("test", null));
  }

  // ==================== encode + decode roundtrip with encoding ====================

  @Test
  void testRoundtripWithEncoding_GBK() {
    String original = "中文测试";
    String encoded = UrlUtils.encode(original, "GBK");
    Assertions.assertEquals(original, UrlUtils.decode(encoded, "GBK"));
  }

  // ==================== 空格编码为 + 而非 %20 ====================

  @Test
  void testEncode_SpaceIsPlus() {
    Assertions.assertEquals("a+b", UrlUtils.encode("a b"));
  }

  @Test
  void testDecode_PlusIsSpace() {
    Assertions.assertEquals("a b", UrlUtils.decode("a+b"));
  }

}
