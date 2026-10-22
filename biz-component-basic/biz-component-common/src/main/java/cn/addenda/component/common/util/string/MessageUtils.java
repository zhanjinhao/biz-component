package cn.addenda.component.common.util.string;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.helpers.MessageFormatter;

/**
 * 按 {@code {}} 占位符格式化文本模板。
 * <p>优先使用 SLF4J 的 {@link MessageFormatter}，不可用时回退到 {@link StrFormatUtils}，两者行为一致。
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {

  private static volatile boolean messageFormatterAvailable = true;

  public static String format(String template, Object... args) {
    if (messageFormatterAvailable) {
      try {
        return MessageFormatter.arrayFormat(template, args).getMessage();
      } catch (NoClassDefFoundError e) {
        messageFormatterAvailable = false;
      }
    }
    return StrFormatUtils.format(template, args);
  }

}
