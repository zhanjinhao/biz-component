package cn.addenda.component.common.jackson.deserializer;

import cn.addenda.component.common.util.datetime.DateUtils;
import cn.addenda.component.common.util.string.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class DateTimeDeUtils {

  private DateTimeDeUtils() {
  }

  private static final String[] LOCAL_DATE_TIME_FMTS = {
          DateUtils.yMdHmsS_FMT,
          DateUtils.yMdHms_FMT,
          DateUtils.yMdHm_FMT,
          DateUtils.yMdHmsS_S_FMT,
          DateUtils.yMdHms_S_FMT,
          DateUtils.yMdHm_S_FMT,
          DateUtils.yMdHmsS_T_FMT,
          DateUtils.yMdHms_T_FMT,
          DateUtils.yMdHm_T_FMT,
          DateUtils.yMdHmsS_C_FMT,
          DateUtils.yMdHms_C_FMT,
          DateUtils.yMdHm_C_FMT,
  };

  private static final String[] LOCAL_DATE_FMTS = {
          DateUtils.yMd_FMT,
          DateUtils.yMd_S_FMT,
          DateUtils.yMd_C_FMT,
  };

  private static final String[] LOCAL_TIME_FMTS = {
          DateUtils.HmsS_FMT,
          DateUtils.Hms_FMT,
          DateUtils.Hm_FMT,
          DateUtils.HmsS_C_FMT,
          DateUtils.Hms_C_FMT,
          DateUtils.Hm_C_FMT,
  };

  public static LocalDateTime parseLdt(String s) {
    if (s == null || s.isEmpty() || "null".equals(s)) {
      return null;
    }
    if (StringUtils.isStrictlyNumeric(s) && s.length() > 8) {
      return DateUtils.timestampToLocalDateTime(Long.parseLong(s));
    }
    for (String fmt : LOCAL_DATE_TIME_FMTS) {
      try {
        return DateUtils.parseLdt(s, fmt);
      } catch (Exception ignored) {
        // try next format
      }
    }
    throw new IllegalArgumentException("Cannot parse LocalDateTime: '" + s + "'.");
  }

  public static LocalDate parseLd(String s) {
    if (s == null || s.isEmpty() || "null".equals(s)) {
      return null;
    }
    if (StringUtils.isStrictlyNumeric(s) && s.length() > 8) {
      return DateUtils.timestampToLocalDateTime(Long.parseLong(s)).toLocalDate();
    }
    for (String fmt : LOCAL_DATE_FMTS) {
      try {
        return DateUtils.parseLd(s, fmt);
      } catch (Exception ignored) {
        // try next format
      }
    }
    throw new IllegalArgumentException("Cannot parse LocalDate: '" + s + "'.");
  }

  public static LocalTime parseLt(String s) {
    if (s == null || s.isEmpty() || "null".equals(s)) {
      return null;
    }
    for (String fmt : LOCAL_TIME_FMTS) {
      try {
        return DateUtils.parseLt(s, fmt);
      } catch (Exception ignored) {
        // try next format
      }
    }
    throw new IllegalArgumentException("Cannot parse LocalTime: '" + s + "'.");
  }

}
