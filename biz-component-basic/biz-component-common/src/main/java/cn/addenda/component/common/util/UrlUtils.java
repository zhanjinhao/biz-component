package cn.addenda.component.common.util;

import cn.addenda.component.common.util.string.Slf4jUtils;
import cn.addenda.component.common.util.string.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * URL 编解码工具类。
 * <p>
 * 编解码遵循 <a href="https://url.spec.whatwg.org/#application/x-www-form-urlencoded">application/x-www-form-urlencoded</a>
 * 规范（HTML 表单编码），空格会被编码为 {@code +} 而非 {@code %20}。
 * 如需 RFC 3986 语义的 URI 编码，请使用 {@link java.net.URI} 或 {@link java.net.URLEncoder} 自行处理。
 *
 * @author addenda
 * @since 2023/8/15 22:12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlUtils {

  /**
   * 使用 UTF-8 对字符串进行 URL 编码。
   *
   * @param str 待编码的字符串（不能为 null）
   * @return 编码后的字符串
   * @throws NullPointerException 若 str 为 null
   */
  public static String encode(String str) {
    AssertUtils.notNull(str, "str must not be null");
    try {
      return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(Slf4jUtils.format("无法使用编码 [{}] 对 [{}] 进行 URL 编码", "UTF-8", StringUtils.atMost(str, 1000)), e);
    }
  }

  /**
   * 使用 UTF-8 对字符串进行 URL 解码。
   *
   * @param str 待解码的字符串（不能为 null）
   * @return 解码后的字符串
   * @throws NullPointerException 若 str 为 null
   */
  public static String decode(String str) {
    AssertUtils.notNull(str, "str must not be null");
    try {
      return URLDecoder.decode(str, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(Slf4jUtils.format("无法使用编码 [{}] 对 [{}] 进行 URL 解码", "UTF-8", StringUtils.atMost(str, 1000)), e);
    }
  }

  /**
   * 使用指定编码对字符串进行 URL 编码。
   *
   * @param str      待编码的字符串（不能为 null）
   * @param encoding 字符编码名称（不能为 null），例如 "UTF-8"、"GBK"
   * @return 编码后的字符串
   * @throws NullPointerException     若 str 或 encoding 为 null
   * @throws IllegalArgumentException 若指定的编码不被支持
   */
  public static String encode(String str, String encoding) {
    AssertUtils.notNull(str, "str must not be null");
    AssertUtils.notNull(encoding, "encoding must not be null");
    try {
      return URLEncoder.encode(str, encoding);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(Slf4jUtils.format("无法使用编码 [{}] 对 [{}] 进行 URL 编码", encoding, StringUtils.atMost(str, 1000)), e);
    }
  }

  /**
   * 使用指定编码对字符串进行 URL 解码。
   *
   * @param str      待解码的字符串（不能为 null）
   * @param encoding 字符编码名称（不能为 null），例如 "UTF-8"、"GBK"
   * @return 解码后的字符串
   * @throws NullPointerException     若 str 或 encoding 为 null
   * @throws IllegalArgumentException 若指定的编码不被支持
   */
  public static String decode(String str, String encoding) {
    AssertUtils.notNull(str, "str must not be null");
    AssertUtils.notNull(encoding, "encoding must not be null");
    try {
      return URLDecoder.decode(str, encoding);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(Slf4jUtils.format("无法使用编码 [{}] 对 [{}] 进行 URL 解码", encoding, StringUtils.atMost(str, 1000)), e);
    }
  }

}
