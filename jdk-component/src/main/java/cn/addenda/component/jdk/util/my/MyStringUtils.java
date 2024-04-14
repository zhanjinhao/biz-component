package cn.addenda.component.jdk.util.my;

import cn.addenda.component.jdk.util.StrFormatUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2022/2/7 12:38
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyStringUtils {

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
      if (i == 0) {
        result.append(!hasText(values[i]) ? "" : values[i]);
      } else {
        result.append(!hasText(values[i]) ? "" : separator + values[i]);
      }
    }
    return result.toString();
  }

  public static boolean checkIsDigit(String piece) {
    if (!hasText(piece)) {
      return false;
    }
    return piece.matches("\\d+");
  }

  public static String joinArrayToString(String[] pieces, int fromIndex, int endIndex) {
    if (pieces == null) {
      return null;
    }
    int length = pieces.length;
    Assert.isTrue(endIndex <= length, StrFormatUtils.format("`endIndex` should be less than or equal {}!", length));
    Assert.isTrue(fromIndex >= 0, "`fromIndex` should be greater than or equal 0!");
    return String.join(" ", Arrays.stream(pieces).collect(Collectors.toList()).subList(fromIndex, endIndex));
  }

  public static String joinArrayToString(String content, int fromIndex, int endIndex) {
    if (!hasText(content)) {
      return content;
    }
    return joinArrayToString(content.split("\\s+"), fromIndex, endIndex);
  }

  public static String replaceCharAtIndex(String str, int index, char newChar) {
    if (str == null) {
      return null;
    }
    int length = str.length();
    Assert.isTrue(index < length, StrFormatUtils.format("`index` should be less than {}!", length));
    Assert.isTrue(index >= 0, "`index` should be greater than or equal 0!");
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

}
