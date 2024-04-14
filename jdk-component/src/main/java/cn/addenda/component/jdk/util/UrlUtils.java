package cn.addenda.component.jdk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author addenda
 * @since 2023/8/15 22:12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlUtils {

  @SneakyThrows
  public static String encode(String str) {
    return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
  }

  @SneakyThrows
  public static String decode(String str) {
    return URLDecoder.decode(str, UTF_8.name());
  }

  @SneakyThrows
  public static String encode(String str, String enc) {
    return URLEncoder.encode(str, enc);
  }

  @SneakyThrows
  public static String decode(String str, String enc) {
    return URLDecoder.decode(str, enc);
  }

}
