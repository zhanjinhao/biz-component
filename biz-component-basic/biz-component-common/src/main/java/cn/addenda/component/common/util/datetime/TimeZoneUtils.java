package cn.addenda.component.common.util.datetime;

import cn.addenda.component.common.util.collection.ArrayUtils;
import cn.addenda.component.common.util.string.Slf4jUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeZoneUtils {

  public static final String TIME_ZONE_PEK = "+08:00";
  public static final String TIME_ZONE_UTC = "+00:00";
  public static final int QUARTER_HOUR_MINUTES = 15;

  private static final LocalDate FIXED_DATE = LocalDate.of(2000, 1, 1);

  private static final Set<String> timeZoneSet = ArrayUtils.asHashSet(
          "-18:00", "-17:45", "-17:30", "-17:15",
          "-17:00", "-16:45", "-16:30", "-16:15",
          "-16:00", "-15:45", "-15:30", "-15:15",
          "-15:00", "-14:45", "-14:30", "-14:15",
          "-14:00", "-13:45", "-13:30", "-13:15",
          "-13:00", "-12:45", "-12:30", "-12:15",
          "-12:00", "-11:45", "-11:30", "-11:15",
          "-11:00", "-10:45", "-10:30", "-10:15",
          "-10:00", "-09:45", "-09:30", "-09:15",
          "-09:00", "-08:45", "-08:30", "-08:15",
          "-08:00", "-07:45", "-07:30", "-07:15",
          "-07:00", "-06:45", "-06:30", "-06:15",
          "-06:00", "-05:45", "-05:30", "-05:15",
          "-05:00", "-04:45", "-04:30", "-04:15",
          "-04:00", "-03:45", "-03:30", "-03:15",
          "-03:00", "-02:45", "-02:30", "-02:15",
          "-02:00", "-01:45", "-01:30", "-01:15",
          "-01:00", "-00:45", "-00:30", "-00:15",

          "+00:00", "+00:15", "+00:30", "+00:45",
          "+01:00", "+01:15", "+01:30", "+01:45",
          "+02:00", "+02:15", "+02:30", "+02:45",
          "+03:00", "+03:15", "+03:30", "+03:45",
          "+04:00", "+04:15", "+04:30", "+04:45",
          "+05:00", "+05:15", "+05:30", "+05:45",
          "+06:00", "+06:15", "+06:30", "+06:45",
          "+07:00", "+07:15", "+07:30", "+07:45",
          "+08:00", "+08:15", "+08:30", "+08:45",
          "+09:00", "+09:15", "+09:30", "+09:45",
          "+10:00", "+10:15", "+10:30", "+10:45",
          "+11:00", "+11:15", "+11:30", "+11:45",
          "+12:00", "+12:15", "+12:30", "+12:45",
          "+13:00", "+13:15", "+13:30", "+13:45",
          "+14:00", "+14:15", "+14:30", "+14:45",
          "+15:00", "+15:15", "+15:30", "+15:45",
          "+16:00", "+16:15", "+16:30", "+16:45",
          "+17:00", "+17:15", "+17:30", "+17:45",
          "+18:00"
  );

  /**
   * @param timeZone 时区
   * @param hours    小时
   */
  public static String shiftTimeZone(String timeZone, int hours) {
    return shiftTimeZone(timeZone, hours, 0);
  }

  /**
   * @param timeZone 时区
   * @param hours    小时
   * @param minutes  分钟
   */
  public static String shiftTimeZone(String timeZone, int hours, int minutes) {
    assertTimeZone(timeZone);

    if (minutes % QUARTER_HOUR_MINUTES != 0 || Math.abs(minutes) >= 60) {
      throw new IllegalArgumentException(
              Slf4jUtils.format("minutes must be a multiple of 15. timeZone: {}, hours: {}, minutes: {}.",
                      timeZone, hours, minutes));
    }

    // 解析时区偏移量
    ZoneOffset offset = ZoneOffset.of(timeZone);

    // 获取总秒数，对24小时取模，溢出折叠回 ±18:00 范围
    long totalSeconds = offset.getTotalSeconds() + hours * 3600L + minutes * 60L;
    totalSeconds %= 86400;
    if (totalSeconds > 64800) {
      totalSeconds -= 86400;
    } else if (totalSeconds < -64800) {
      totalSeconds += 86400;
    }
    int totalSecondsInt = (int) totalSeconds;

    try {
      // 将总秒数转换回 ZoneOffset
      ZoneOffset newOffset = ZoneOffset.ofTotalSeconds(totalSecondsInt);

      String id = newOffset.getId();
      return "Z".equals(id) ? "+00:00" : id;
    } catch (DateTimeException dateTimeException) {
      throw new IllegalArgumentException(
              Slf4jUtils.format("illegal timeZone offset argument. timeZone: {}, hours: {}, minutes: {}.", timeZone, hours, minutes),
              dateTimeException);
    }
  }

  /**
   * 计算时间从原时区转到目标时区后日期的偏离值
   *
   * @param timeZoneSource 原时区
   * @param timeZoneTarget 目标时区
   * @param localTime      计算日期时的基准时间
   * @return localTime从源时区转到目标时区的日期差
   */
  public static int dateOffsetBetween(String timeZoneSource, String timeZoneTarget, LocalTime localTime) {
    assertTimeZone(timeZoneSource);
    assertTimeZone(timeZoneTarget);

    // 解析源时区偏移量
    ZoneOffset sourceOffset = ZoneOffset.of(timeZoneSource);
    // 解析目标时区偏移量
    ZoneOffset targetOffset = ZoneOffset.of(timeZoneTarget);

    // 使用固定日期，避免 LocalDate.now() 引入非确定性
    LocalDate currentDate = FIXED_DATE;

    // 创建源时区的 ZonedDateTime 对象
    ZonedDateTime sourceDateTime = ZonedDateTime.of(currentDate, localTime, sourceOffset);

    // 将 ZonedDateTime 对象转换为目标时区
    ZonedDateTime targetDateTime = sourceDateTime.withZoneSameInstant(targetOffset);

    // 计算sourceDateTime和targetDateTime的日期差
    return (int) ChronoUnit.DAYS.between(sourceDateTime.toLocalDate(), targetDateTime.toLocalDate());
  }


  /**
   * 将日期从原时区转到目标时区
   *
   * @param timeZoneSource 原时区
   * @param timeZoneTarget 目标时区
   * @param localTime      计算日期时的基准时间
   * @param localDate      待转换的日期
   * @return localDate从原时区转到目标时区后的日期
   */
  public static LocalDate adjustDate(String timeZoneSource, String timeZoneTarget, LocalTime localTime, LocalDate localDate) {
    return localDate.plusDays(dateOffsetBetween(timeZoneSource, timeZoneTarget, localTime));
  }


  /**
   * 将时间从原时区转到目标时区
   *
   * @param timeZoneSource 原时区
   * @param timeZoneTarget 目标时区
   * @param localTime      时间
   * @return localTime从原时区转到目标时区后的时间
   */
  public static LocalTime convertLocalTime(String timeZoneSource, String timeZoneTarget, LocalTime localTime) {
    assertTimeZone(timeZoneSource);
    assertTimeZone(timeZoneTarget);

    // 解析源时区偏移量
    ZoneOffset sourceOffset = ZoneOffset.of(timeZoneSource);
    // 解析目标时区偏移量
    ZoneOffset targetOffset = ZoneOffset.of(timeZoneTarget);

    // 使用固定日期，避免 LocalDate.now() 引入非确定性
    ZonedDateTime sourceDateTime = ZonedDateTime.of(FIXED_DATE, localTime, sourceOffset);

    // 将 ZonedDateTime 对象转换为目标时区
    ZonedDateTime targetDateTime = sourceDateTime.withZoneSameInstant(targetOffset);

    // 获取目标时区的时间
    return targetDateTime.toLocalTime();
  }

  /**
   * 将日期时间从原时区转到目标时区
   *
   * @param timeZoneSource 原时区
   * @param timeZoneTarget 目标时区
   * @param localDateTime  日期时间
   * @return localDateTime从原时区转到目标时区后的时间
   */
  public static LocalDateTime convertLocalDateTime(String timeZoneSource, String timeZoneTarget, LocalDateTime localDateTime) {
    assertTimeZone(timeZoneSource);
    assertTimeZone(timeZoneTarget);

    // 解析源时区偏移量
    ZoneOffset sourceOffset = ZoneOffset.of(timeZoneSource);
    // 解析目标时区偏移量
    ZoneOffset targetOffset = ZoneOffset.of(timeZoneTarget);

    // 创建源时区的 ZonedDateTime 对象
    ZonedDateTime sourceDateTime = ZonedDateTime.of(localDateTime, sourceOffset);

    // 将 ZonedDateTime 对象转换为目标时区
    ZonedDateTime targetDateTime = sourceDateTime.withZoneSameInstant(targetOffset);

    // 获取目标时区的 LocalDateTime
    return targetDateTime.toLocalDateTime();
  }

  /**
   * 校验是否是HH:MM格式的时间
   *
   * @param time HH:mm格式的时间
   * @return 是否是HH:mm格式的时间
   */
  public static boolean checkHHmmValid(String time) {
    if (time == null) {
      return false;
    }
    if (time.length() != 5 && time.length() != 6) {
      return false;
    }
    String[] parts = time.split(":");
    if (parts.length != 2) {
      return false;
    }
    if (parts[0].length() < 2 || parts[1].length() != 2) {
      return false;
    }
    try {
      int h = Integer.parseInt(parts[0]);
      int m = Integer.parseInt(parts[1]);
      return h >= -23 && h <= 23 && m >= 0 && m <= 59;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * 校验是否是HH:MM格式的时间，不是则抛出异常
   *
   * @param time HH:MM格式的时间
   */
  public static void assertHHmmValid(String time) {
    if (!checkHHmmValid(time)) {
      throw new IllegalArgumentException(Slf4jUtils.format("HH:mm[{}]格式错误", time));
    }
  }

  private static void assertTimeZone(String timeZone) {
    if (!timeZoneSet.contains(timeZone)) {
      throw new IllegalArgumentException(Slf4jUtils.format("时区[{}]格式错误！", timeZone));
    }
  }

}
