package cn.addenda.component.common.util.string;

import cn.addenda.component.common.util.AssertUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2022/2/7 12:38
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

  private static final String DISPLAY_ELLIPSIS = "...";

  /**
   * 从头部截断字符串，结果长度不超过 {@code maxLength}。
   * <p>如果截断点落在代理对（surrogate pair）中间，被拆开的半个字符不会报错，
   * 而是以 {@code \\uXXXX} 形式的转义文本展示，此时结果长度会多出至多 5 个字符。
   *
   * @param string    原字符串，可为 null
   * @param maxLength 最大长度（按 char 计）；为负表示不截断
   * @return 截断后的字符串；{@code string} 为 null 时返回 null
   */
  public static String atMost(String string, int maxLength) {
    if (string == null) {
      return null;
    }
    if (maxLength < 0) {
      return string;
    }
    if (maxLength == 0) {
      return "";
    }
    return string.length() > maxLength
            ? escapeTrailingHighSurrogate(string.substring(0, maxLength))
            : string;
  }

  /**
   * 截断过长的展示文本：保留开头和结尾，中间用省略号代替。
   * <p>正常情况下结果长度不会超过 {@code maxLength}；当 {@code maxLength} 不足以放下
   * “开头 + 省略号 + 结尾”时，退化为只保留开头的 {@link #atMost(String, int)}。
   * <p>如果截断点落在代理对（surrogate pair）中间，被拆开的半个字符不会报错，
   * 而是以 {@code \\uXXXX} 形式的转义文本展示；此时结果长度可能比 {@code maxLength}
   * 多出至多 10 个字符（首尾各最多 5 个）。
   *
   * @param display    待展示的文本，可为 null
   * @param maxLength  最大展示长度（按 char 计）；为负表示不截断
   * @param tailLength 需要保留的结尾字符数；为负按 0 处理
   * @return 截断后的文本；{@code display} 为 null 时返回 null
   */
  public static String displayAtMost(String display, int maxLength, int tailLength) {
    return displayAtMost(display, maxLength, DISPLAY_ELLIPSIS, tailLength);
  }

  /**
   * 同 {@link #displayAtMost(String, int, int)}，但使用自定义省略号。
   *
   * @param ellipsis 自定义省略号；不能为 null
   */
  public static String displayAtMost(String display, int maxLength, String ellipsis, int tailLength) {
    if (display == null) {
      return null;
    }
    AssertUtils.notNull(ellipsis, "ellipsis must not be null");
    if (maxLength < 0) {
      return display;
    }
    if (maxLength == 0) {
      return "";
    }
    if (display.length() <= maxLength) {
      return display;
    }
    int effectiveTailLength = Math.max(tailLength, 0);
    int prefixLength = maxLength - ellipsis.length() - effectiveTailLength;
    if (prefixLength <= 0) {
      return atMost(display, maxLength);
    }
    String prefix = escapeTrailingHighSurrogate(display.substring(0, prefixLength));
    String tail = escapeLeadingLowSurrogate(display.substring(display.length() - effectiveTailLength));
    return prefix + ellipsis + tail;
  }

  /**
   * 若 {@code str} 以孤立的高位代理结尾（说明截断点落在代理对中间），
   * 将其转义为 {@code \\uXXXX} 文本，避免展示出非法字符。
   */
  private static String escapeTrailingHighSurrogate(String str) {
    int lastIndex = str.length() - 1;
    if (lastIndex >= 0 && Character.isHighSurrogate(str.charAt(lastIndex))) {
      return str.substring(0, lastIndex) + toUnicodeEscape(str.charAt(lastIndex));
    }
    return str;
  }

  /**
   * 若 {@code str} 以孤立的低位代理开头（说明截断点落在代理对中间），
   * 将其转义为 {@code \\uXXXX} 文本，避免展示出非法字符。
   */
  private static String escapeLeadingLowSurrogate(String str) {
    if (!str.isEmpty() && Character.isLowSurrogate(str.charAt(0))) {
      return toUnicodeEscape(str.charAt(0)) + str.substring(1);
    }
    return str;
  }

  private static String toUnicodeEscape(char c) {
    return String.format("\\u%04X", (int) c);
  }

  public static String expandWithSpecifiedChar(String str, char specifiedChar, int expectLength) {
    int length = str.length();
    StringBuilder zero = new StringBuilder();
    for (int i = length; i < expectLength; i++) {
      zero.append(specifiedChar);
    }
    return zero.append(str).toString();
  }

  public static String expandWithZero(String str, int expectLength) {
    return expandWithSpecifiedChar(str, '0', expectLength);
  }

  public static String join(String separator, String... values) {
    if (values.length == 0) {
      return "";
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < values.length; i++) {
      if (values[i] == null) {
        continue;
      }
      if (result.length() > 0) {
        result.append(separator);
      }
      result.append(values[i]);
    }
    return result.toString();
  }

  /**
   * @param fromIndex include
   * @param endIndex  exclude
   */
  public static String joinArrayToString(String[] pieces, int fromIndex, int endIndex) {
    return joinArrayToString(pieces, " ", fromIndex, endIndex);
  }

  /**
   * @param fromIndex include
   * @param endIndex  exclude
   */
  public static String joinArrayToString(String[] pieces, String delimiter, int fromIndex, int endIndex) {
    if (pieces == null) {
      return null;
    }
    int length = pieces.length;
    AssertUtils.isTrue(endIndex <= length, StrFormatUtils.format("`endIndex` should be less than or equal {}!", length));
    AssertUtils.isTrue(fromIndex >= 0, "`fromIndex` should be greater than or equal 0!");
    return String.join(delimiter, Arrays.stream(pieces).collect(Collectors.toList()).subList(fromIndex, endIndex));
  }

  public static String joinArrayToString(String content, int fromIndex, int endIndex) {
    return joinArrayToString(content, "\\s+", " ", fromIndex, endIndex);
  }

  public static String joinArrayToString(String content, String splitRegex, String delimiter, int fromIndex, int endIndex) {
    if (!hasText(content)) {
      return content;
    }
    return joinArrayToString(content.split(splitRegex), delimiter, fromIndex, endIndex);
  }

  public static String replaceCharAtIndex(String str, int index, char newChar) {
    if (str == null) {
      return null;
    }
    int length = str.length();
    AssertUtils.isTrue(index < length, StrFormatUtils.format("`index` should be less than {}!", length));
    AssertUtils.isTrue(index >= 0, "`index` should be greater than or equal 0!");
    return str.substring(0, index) + newChar + str.substring(index + 1);
  }

  public static String discardNull(String str) {
    if (str == null) {
      return "";
    }
    return str;
  }

  public static boolean hasText(CharSequence str) {
    return (str != null && str.length() > 0 && containsText(str));
  }

  public static boolean hasText(String str) {
    return str != null && !str.isEmpty() && containsText(str);
  }

  public static boolean containsText(CharSequence str) {
    int strLen = str.length();

    for (int i = 0; i < strLen; ++i) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }

    return false;
  }

  public static boolean hasLength(CharSequence str) {
    return (str != null && str.length() > 0);
  }

  public static String biTrimSpecifiedChar(String input, char c) {
    if (input == null) {
      return null;
    }

    int length = input.length();

    int start = 0;
    boolean started = false;
    for (int i = 0; i < length; i++) {
      if (input.charAt(i) != c) {
        start = i;
        started = true;
        break;
      }
    }

    int end = length - 1;
    boolean ended = false;
    for (int i = length - 1; i >= 0; i--) {
      if (input.charAt(i) != c) {
        end = i;
        ended = true;
        break;
      }
    }

    if (!started || !ended) {
      return "";
    }

    return input.substring(start, end + 1);
  }

  public static boolean isStrictlyNumeric(CharSequence cs) {
    if (cs == null || cs.length() == 0) {
      return false;
    }
    for (int i = 0; i < cs.length(); i++) {
      if (!Character.isDigit(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isInteger(CharSequence cs) {
    if (cs == null || cs.length() == 0) {
      return false;
    }
    int start = 0;
    char first = cs.charAt(0);
    if (first == '-' || first == '+') {
      if (cs.length() == 1) {
        return false;
      }
      start = 1;
    }
    for (int i = start; i < cs.length(); i++) {
      if (!Character.isDigit(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
    if (str == null || substring == null || substring.length() == 0) {
      return false;
    }
    if (index + substring.length() > str.length()) {
      return false;
    }
    for (int i = 0; i < substring.length(); i++) {
      if (str.charAt(index + i) != substring.charAt(i)) {
        return false;
      }
    }
    return true;
  }

  public static boolean startsWithIgnoreCase(String str, String prefix) {
    return (str != null && prefix != null && str.length() >= prefix.length() &&
            str.regionMatches(true, 0, prefix, 0, prefix.length()));
  }

  /**
   * 忽略空白字符和大小写，判断字符串的前缀是否匹配。
   * 跳过开头的空白字符，取前 N 个非空白字符（N = prefix.length()）与 prefix 做不区分大小写的比较。
   */
  public static boolean startsWithIgnoreBlankAndCase(String str, String prefix) {
    if (str == null || prefix == null) {
      return false;
    }
    int strLen = str.length();
    int prefixLen = prefix.length();
    int matched = 0;
    for (int i = 0; i < strLen; i++) {
      char c = str.charAt(i);
      if (Character.isWhitespace(c)) {
        continue;
      }
      if (matched >= prefixLen) {
        return true;
      }
      if (!regionMatchesIgnoreCase(c, prefix.charAt(matched))) {
        return false;
      }
      matched++;
    }
    return matched == prefixLen;
  }

  /**
   * 忽略空白字符和大小写，判断字符串的后缀是否匹配。
   * 跳过结尾的空白字符，取最后 N 个非空白字符（N = suffix.length()）与 suffix 做不区分大小写的比较。
   */
  public static boolean endsWithIgnoreBlankAndCase(String str, String suffix) {
    if (str == null || suffix == null) {
      return false;
    }
    int strLen = str.length();
    int suffixLen = suffix.length();
    int matched = 0;
    for (int i = strLen - 1; i >= 0; i--) {
      char c = str.charAt(i);
      if (Character.isWhitespace(c)) {
        continue;
      }
      if (matched >= suffixLen) {
        return true;
      }
      if (!regionMatchesIgnoreCase(c, suffix.charAt(suffixLen - 1 - matched))) {
        return false;
      }
      matched++;
    }
    return matched == suffixLen;
  }

  private static boolean regionMatchesIgnoreCase(char c1, char c2) {
    if (c1 == c2) {
      return true;
    }
    return Character.toUpperCase(c1) == Character.toUpperCase(c2)
            || Character.toLowerCase(c1) == Character.toLowerCase(c2);
  }

}
