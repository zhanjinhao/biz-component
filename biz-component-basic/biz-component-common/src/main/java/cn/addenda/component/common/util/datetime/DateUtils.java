package cn.addenda.component.common.util.datetime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author addenda
 * @since 2022/2/7 12:37
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

  // --------
  //  Single
  // --------

  public static final String y_FMT = "yyyy";
  public static final String M_FMT = "MM";
  public static final String d_FMT = "dd";
  public static final String H_FMT = "HH";
  public static final String m_FMT = "mm";
  public static final String s_FMT = "ss";
  public static final String S_FMT = "SSS";

  // ----------
  //  Compound
  // ----------

  public static final String yMdHmsS_FMT = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String yMdHms_FMT = "yyyy-MM-dd HH:mm:ss";
  public static final String yMdHm_FMT = "yyyy-MM-dd HH:mm";
  public static final String yMdH_FMT = "yyyy-MM-dd HH";
  public static final String yMd_FMT = "yyyy-MM-dd";
  public static final String yM_FMT = "yyyy-MM";
  public static final String MdHmsS_FMT = "MM-dd HH:mm:ss.SSS";
  public static final String MdHms_FMT = "MM-dd HH:mm:ss";
  public static final String MdHm_FMT = "MM-dd HH:mm";
  public static final String MdH_FMT = "MM-dd HH";
  public static final String Md_FMT = "MM-dd";
  public static final String dHmsS_FMT = "dd HH:mm:ss.SSS";
  public static final String dHms_FMT = "dd HH:mm:ss";
  public static final String dHm_FMT = "dd HH:mm";
  public static final String dH_FMT = "dd HH";
  public static final String HmsS_FMT = "HH:mm:ss.SSS";
  public static final String Hms_FMT = "HH:mm:ss";
  public static final String Hm_FMT = "HH:mm";
  public static final String msS_FMT = "mm:ss.SSS";
  public static final String ms_FMT = "mm:ss";
  public static final String sS_FMT = "ss.SSS";

  // ---------
  //  Compact
  // ---------

  /**
   * format-only in Java 8. {@code DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")}
   * cannot parse due to {@code ssSSS} adjacency bug. Use {@link #yMdHmsS_FMT} for roundtrip.
   */
  public static final String yMdHmsS_C_FMT = "yyyyMMddHHmmssSSS";
  public static final String yMdHms_C_FMT = "yyyyMMddHHmmss";
  public static final String yMdHm_C_FMT = "yyyyMMddHHmm";
  public static final String yMdH_C_FMT = "yyyyMMddHH";
  public static final String yMd_C_FMT = "yyyyMMdd";
  public static final String yM_C_FMT = "yyyyMM";
  public static final String MdHmsS_C_FMT = "MMddHHmmssSSS";
  public static final String MdHms_C_FMT = "MMddHHmmss";
  public static final String MdHm_C_FMT = "MMddHHmm";
  public static final String MdH_C_FMT = "MMddHH";
  public static final String Md_C_FMT = "MMdd";
  public static final String dHmsS_C_FMT = "ddHHmmssSSS";
  public static final String dHms_C_FMT = "ddHHmmss";
  public static final String dHm_C_FMT = "ddHHmm";
  public static final String dH_C_FMT = "ddHH";
  public static final String HmsS_C_FMT = "HHmmssSSS";
  public static final String Hms_C_FMT = "HHmmss";
  public static final String Hm_C_FMT = "HHmm";
  public static final String msS_C_FMT = "mmssSSS";
  public static final String ms_C_FMT = "mmss";
  public static final String sS_C_FMT = "ssSSS";

  // --------
  //  Slash
  // --------

  public static final String yMdHmsS_S_FMT = "yyyy/MM/dd HH:mm:ss.SSS";
  public static final String yMdHms_S_FMT = "yyyy/MM/dd HH:mm:ss";
  public static final String yMdHm_S_FMT = "yyyy/MM/dd HH:mm";
  public static final String yMdH_S_FMT = "yyyy/MM/dd HH";
  public static final String yMd_S_FMT = "yyyy/MM/dd";
  public static final String yM_S_FMT = "yyyy/MM";
  public static final String MdHmsS_S_FMT = "MM/dd HH:mm:ss.SSS";
  public static final String MdHms_S_FMT = "MM/dd HH:mm:ss";
  public static final String MdHm_S_FMT = "MM/dd HH:mm";
  public static final String MdH_S_FMT = "MM/dd HH";
  public static final String Md_S_FMT = "MM/dd";

  // ----------
  //  ISO 8601
  // ----------

  public static final String yMdHmsS_T_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  public static final String yMdHms_T_FMT = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String yMdHm_T_FMT = "yyyy-MM-dd'T'HH:mm";
  public static final String yMdH_T_FMT = "yyyy-MM-dd'T'HH";
  public static final String MdHmsS_T_FMT = "MM-dd'T'HH:mm:ss.SSS";
  public static final String MdHms_T_FMT = "MM-dd'T'HH:mm:ss";
  public static final String MdHm_T_FMT = "MM-dd'T'HH:mm";
  public static final String MdH_T_FMT = "MM-dd'T'HH";
  public static final String dHmsS_T_FMT = "dd'T'HH:mm:ss.SSS";
  public static final String dHms_T_FMT = "dd'T'HH:mm:ss";
  public static final String dHm_T_FMT = "dd'T'HH:mm";
  public static final String dH_T_FMT = "dd'T'HH";

  private static final Map<String, DateTimeFormatter> FORMATTER_MAP = new ConcurrentHashMap<>();

  private static final ZoneId defaultZoneId;

  private static final LocalDate EPOCH_DATE = LocalDate.of(1970, 1, 1);

  static {
    ZoneId tmpDefaultZoneId;
    String property = System.getProperty("biz.component.timezone");
    if (property == null || property.isEmpty()) {
      tmpDefaultZoneId = ZoneId.systemDefault();
    } else {
      try {
        tmpDefaultZoneId = ZoneId.of(property);
      } catch (Exception e) {
        throw new IllegalArgumentException(
            "Invalid biz.component.timezone: '" + property + "'.", e);
      }
    }
    defaultZoneId = tmpDefaultZoneId;
  }

  public static LocalDateTime dateToLocalDateTime(Date date) {
    return dateToLocalDateTime(date, defaultZoneId);
  }

  public static LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId) {
    if (date == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    Instant instant = date.toInstant();
    return LocalDateTime.ofInstant(instant, zoneId);
  }

  public static Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
    return localDateTimeToTimestamp(localDateTime, defaultZoneId);
  }

  public static Long localDateTimeToTimestamp(LocalDateTime localDateTime, ZoneId zoneId) {
    if (localDateTime == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
  }

  public static LocalDateTime timestampToLocalDateTime(Long timestamp) {
    return timestampToLocalDateTime(timestamp, defaultZoneId);
  }

  public static LocalDateTime timestampToLocalDateTime(Long timestamp, ZoneId zoneId) {
    if (timestamp == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    Instant instant = Instant.ofEpochMilli(timestamp);
    return LocalDateTime.ofInstant(instant, zoneId);
  }

  public static Date localDateTimeToDate(LocalDateTime localDateTime) {
    return localDateTimeToDate(localDateTime, defaultZoneId);
  }

  public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId) {
    if (localDateTime == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
    Instant instant = zonedDateTime.toInstant();
    return Date.from(instant);
  }

  public static Date localDateToDate(LocalDate localDate) {
    return localDateToDate(localDate, defaultZoneId);
  }

  public static Date localDateToDate(LocalDate localDate, ZoneId zoneId) {
    if (localDate == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    LocalDateTime localDateTime = localDate.atTime(0, 0, 0);
    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
    Instant instant = zonedDateTime.toInstant();
    return Date.from(instant);
  }

  public static Date localTimeToDate(LocalTime localTime) {
    return localTimeToDate(localTime, defaultZoneId);
  }

  public static Date localTimeToDate(LocalTime localTime, ZoneId zoneId) {
    if (localTime == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    LocalDateTime localDateTime = localTime.atDate(EPOCH_DATE);
    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
    Instant instant = zonedDateTime.toInstant();
    return Date.from(instant);
  }

  public static String format(LocalDateTime localDateTime, String formatter) {
    if (localDateTime == null) {
      return null;
    }
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return dateTimeFormatter.format(localDateTime);
  }

  public static String format(LocalDate localDate, String formatter) {
    if (localDate == null) {
      return null;
    }
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return dateTimeFormatter.format(localDate);
  }

  public static String format(LocalTime localTime, String formatter) {
    if (localTime == null) {
      return null;
    }
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return dateTimeFormatter.format(localTime);
  }

  public static LocalDateTime parseLdt(String localDateTime, String formatter) {
    if (localDateTime == null) {
      return null;
    }
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return LocalDateTime.parse(localDateTime, dateTimeFormatter);
  }

  public static LocalDate parseLd(String localDate, String formatter) {
    if (localDate == null) {
      return null;
    }
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return LocalDate.parse(localDate, dateTimeFormatter);
  }

  public static LocalTime parseLt(String localTime, String formatter) {
    if (localTime == null) {
      return null;
    }
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return LocalTime.parse(localTime, dateTimeFormatter);
  }

}
