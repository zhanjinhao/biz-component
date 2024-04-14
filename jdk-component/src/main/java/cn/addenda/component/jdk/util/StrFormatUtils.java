package cn.addenda.component.jdk.util;

import cn.addenda.component.jdk.util.my.MyArrayUtils;
import cn.addenda.component.jdk.util.my.MyStringUtils;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import lombok.AccessLevel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StrFormatUtils {

  /**
   * 字符常量：反斜杠 {@code '\\'}
   */
  public static final char C_BACKSLASH = '\\';
  public static final String DEFAULT_PLACE_HOLDER = "{}";

  public static String format(CharSequence template, Object... params) {
    if (null == template) {
      return null;
    }
    if ((params == null || params.length == 0) || !MyStringUtils.hasText(template)) {
      return template.toString();
    }
    // decodeByte: false  ->  对齐log4j
    return formatWith(template.toString(), DEFAULT_PLACE_HOLDER, false, null, params);
  }

  public static String format(CharSequence template, boolean decodeByte, Charset charset, Object... params) {
    if (null == template) {
      return null;
    }
    if ((params == null || params.length == 0) || !MyStringUtils.hasText(template)) {
      return template.toString();
    }
    return formatWith(template.toString(), DEFAULT_PLACE_HOLDER, decodeByte, charset, params);
  }

  public static String formatWith(String strPattern, String placeHolder, boolean decodeByte, Charset charset, Object... argArray) {
    if (!MyStringUtils.hasText(strPattern) || !MyStringUtils.hasText(placeHolder) || MyArrayUtils.isEmpty(argArray)) {
      return strPattern;
    }
    final int strPatternLength = strPattern.length();
    final int placeHolderLength = placeHolder.length();

    // 初始化定义好的长度以获得更好的性能
    final StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

    // 记录已经处理到的位置
    int handledPosition = 0;
    // 占位符所在位置
    int delimIndex;
    for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
      delimIndex = strPattern.indexOf(placeHolder, handledPosition);
      // 剩余部分无占位符
      if (delimIndex == -1) {
        // 不带占位符的模板直接返回
        if (handledPosition == 0) {
          return strPattern;
        }
        // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
        sbuf.append(strPattern, handledPosition, strPatternLength);
        return sbuf.toString();
      }

      // 转义符
      if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == C_BACKSLASH) {// 转义符
        if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == C_BACKSLASH) {// 双转义符
          // 转义符之前还有一个转义符，占位符依旧有效
          sbuf.append(strPattern, handledPosition, delimIndex - 1);
          sbuf.append(toStr(argArray[argIndex], decodeByte, charset));
          handledPosition = delimIndex + placeHolderLength;
        } else {
          // 占位符被转义
          argIndex--;
          sbuf.append(strPattern, handledPosition, delimIndex - 1);
          sbuf.append(placeHolder.charAt(0));
          handledPosition = delimIndex + 1;
        }
      }
      // 正常占位符
      else {
        sbuf.append(strPattern, handledPosition, delimIndex);
        sbuf.append(toStr(argArray[argIndex], decodeByte, charset));
        handledPosition = delimIndex + placeHolderLength;
      }
    }

    // 加入最后一个占位符后所有的字符
    sbuf.append(strPattern, handledPosition, strPatternLength);

    return sbuf.toString();
  }

  public static String toStr(Object obj, boolean decodeByte, Charset charset) {
    if (null == obj) {
      return null;
    }

    if (obj instanceof String) {
      return (String) obj;
    } else if (decodeByte && obj instanceof byte[]) {
      return toStr((byte[]) obj, charset);
    } else if (decodeByte && obj instanceof Byte[]) {
      return toStr((Byte[]) obj, charset);
    } else if (decodeByte && obj instanceof ByteBuffer) {
      return toStr((ByteBuffer) obj, charset);
    } else if (MyArrayUtils.isArray(obj)) {
      return MyArrayUtils.arrayToString(obj);
    }

    return obj.toString();
  }

  private static String toStr(byte[] data, Charset charset) {
    if (data == null) {
      return null;
    }

    if (null == charset) {
      return new String(data);
    }
    return new String(data, charset);
  }

  private static String toStr(Byte[] data, Charset charset) {
    if (data == null) {
      return null;
    }

    byte[] bytes = new byte[data.length];
    Byte dataByte;
    for (int i = 0; i < data.length; i++) {
      dataByte = data[i];
      bytes[i] = (null == dataByte) ? -1 : dataByte;
    }

    return new String(bytes, charset);
  }

  private static String toStr(ByteBuffer data, Charset charset) {
    if (null == charset) {
      charset = Charset.defaultCharset();
    }
    return charset.decode(data).toString();
  }

}
