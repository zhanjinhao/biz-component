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
