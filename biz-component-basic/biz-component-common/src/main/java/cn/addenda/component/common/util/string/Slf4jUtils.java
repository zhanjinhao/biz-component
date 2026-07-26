package cn.addenda.component.common.util.string;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.helpers.MessageFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Slf4jUtils {

  private static volatile boolean MESSAGE_FORMATTER_AVAILABLE = true;

  public static String format(String tql, Object... args) {
    if (MESSAGE_FORMATTER_AVAILABLE) {
      try {
        return MessageFormatter.arrayFormat(tql, args).getMessage();
      } catch (NoClassDefFoundError e) {
        MESSAGE_FORMATTER_AVAILABLE = false;
      }
    }
    return StrFormatUtils.format(tql, args);
  }

}
