package cn.addenda.component.cronclean;

import java.util.Date;

import cn.addenda.component.jdk.util.DateUtils;
import lombok.Getter;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.Assert;

/**
 * @author addenda
 * @since 2024/3/29 10:34
 */
public abstract class CronClean {

  @Getter
  private final String cron;

  protected CronClean(String cron) {
    this.cron = cron;
    Assert.notNull(cron, "`cron` can not be null!");
    Assert.isTrue(CronSequenceGenerator.isValidExpression(cron), String.format("`cron`[%s] is not valid!", cron));
  }

  public abstract void clean();

  public abstract void cronClean();

  public abstract void close();

  protected String cronDescription() {
    Date now = new Date();
    CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
    Date next1 = cronSequenceGenerator.next(now);
    Date next2 = cronSequenceGenerator.next(next1);
    Date next3 = cronSequenceGenerator.next(next2);
    return String.format("当前时间是[%s]。前三次执行时间是[%s]、[%s]、[%s]。",
            DateUtils.format(DateUtils.dateToLocalDateTime(now), DateUtils.yMdHmsS_FORMATTER),
            DateUtils.format(DateUtils.dateToLocalDateTime(next1), DateUtils.yMdHmsS_FORMATTER),
            DateUtils.format(DateUtils.dateToLocalDateTime(next2), DateUtils.yMdHmsS_FORMATTER),
            DateUtils.format(DateUtils.dateToLocalDateTime(next3), DateUtils.yMdHmsS_FORMATTER));
  }

  protected String removeGrave(String str) {
    if (str == null) {
      throw new NullPointerException("fieldName or tableName can not be null!");
    }
    str = str.trim();
    if ("`".equals(str)) {
      return str;
    }
    int start = 0;
    int end = str.length();
    if (str.startsWith("`")) {
      start = start + 1;
    }
    if (str.endsWith("`")) {
      end = end - 1;
    }

    if (start != 0 || end != str.length()) {
      return str.substring(start, end);
    }
    return str;
  }

}
