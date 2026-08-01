package cn.addenda.component.common.test.util.datetime;

import cn.addenda.component.common.util.collection.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static cn.addenda.component.common.util.datetime.TimeZoneUtils.*;

class TimeZoneUtilsTest {

  @Test
  void test1() {
    // 定义24个时区名称
    List<String> timeZones = ArrayUtils.asArrayList(
            "Africa/Cairo", "America/New_York", "America/Los_Angeles", "America/Chicago",
            "America/Denver", "America/Phoenix", "America/Anchorage", "America/Adak",
            "Asia/Shanghai", "Asia/Tokyo", "Asia/Kolkata", "Asia/Dubai",
            "Asia/Singapore", "Asia/Bangkok", "Asia/Hong_Kong", "Asia/Seoul",
            "Australia/Sydney", "Australia/Melbourne", "Australia/Brisbane", "Europe/London",
            "Europe/Berlin", "Europe/Paris", "Europe/Rome", "Europe/Moscow"
    );

    // 获取并打印每个时区的偏移值
    for (String timeZoneName : timeZones) {
      try {
        ZoneId zoneId = ZoneId.of(timeZoneName);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        ZoneOffset zoneOffset = zonedDateTime.getOffset();
        System.out.println("时区: " + timeZoneName + ", 偏移量: " + zoneOffset);
      } catch (Exception e) {
        System.out.println("时区: " + timeZoneName + ", 获取偏移量失败: " + e.getMessage());
      }
    }
  }


  @Test
  void testLocalDateOffset() {
    int l0 = dateOffsetBetween("+08:00", "+00:00", LocalTime.of(7, 59));
    Assertions.assertEquals(-1, l0);

    int l1 = dateOffsetBetween("+08:00", "+00:00", LocalTime.of(8, 0));
    Assertions.assertEquals(0, l1);

    int l2 = dateOffsetBetween("+08:00", "+00:00", LocalTime.of(23, 59));
    Assertions.assertEquals(0, l2);

    int l3 = dateOffsetBetween("+00:00", "+08:00", LocalTime.of(15, 59));
    Assertions.assertEquals(0, l3);

    int l4 = dateOffsetBetween("+00:00", "+08:00", LocalTime.of(16, 0));
    Assertions.assertEquals(1, l4);

    int l5 = dateOffsetBetween("+00:00", "+08:00", LocalTime.of(23, 59));
    Assertions.assertEquals(1, l5);

    int l6 = dateOffsetBetween("+08:00", "+08:00", LocalTime.of(15, 59));
    Assertions.assertEquals(0, l6);

    int l7 = dateOffsetBetween("+08:00", "+08:00", LocalTime.of(16, 0));
    Assertions.assertEquals(0, l7);

    int l8 = dateOffsetBetween("+08:00", "+08:00", LocalTime.of(23, 59));
    Assertions.assertEquals(0, l8);

    int l9 = dateOffsetBetween("+00:00", "+00:00", LocalTime.of(15, 59));
    Assertions.assertEquals(0, l9);

    int l10 = dateOffsetBetween("+00:00", "+00:00", LocalTime.of(16, 0));
    Assertions.assertEquals(0, l10);

    int l11 = dateOffsetBetween("+00:00", "+00:00", LocalTime.of(23, 59));
    Assertions.assertEquals(0, l11);

    int l12 = dateOffsetBetween("+12:00", "-12:00", LocalTime.of(23, 59));
    Assertions.assertEquals(-1, l12);

    int l13 = dateOffsetBetween("-12:00", "+12:00", LocalTime.of(23, 59));
    Assertions.assertEquals(1, l13);

    int l14 = dateOffsetBetween("+12:00", "-12:00", LocalTime.of(0, 0));
    Assertions.assertEquals(-1, l14);

    int l15 = dateOffsetBetween("-12:00", "+12:00", LocalTime.of(0, 0));
    Assertions.assertEquals(1, l15);

    int l16 = dateOffsetBetween("-12:00", "-11:45", LocalTime.of(23, 59));
    Assertions.assertEquals(1, l16);

  }


  @Test
  void testLocalDateConversion() {
    LocalDate pre = LocalDate.of(2025, 1, 4);
    LocalDate base = LocalDate.of(2025, 1, 5);
    LocalDate next = LocalDate.of(2025, 1, 6);

    LocalDate l0 = adjustDate("+08:00", "+00:00", LocalTime.of(7, 59), base);
    Assertions.assertEquals(pre, l0);

    LocalDate l1 = adjustDate("+08:00", "+00:00", LocalTime.of(8, 0), base);
    Assertions.assertEquals(base, l1);

    LocalDate l2 = adjustDate("+08:00", "+00:00", LocalTime.of(23, 59), base);
    Assertions.assertEquals(base, l2);

    LocalDate l3 = adjustDate("+00:00", "+08:00", LocalTime.of(15, 59), base);
    Assertions.assertEquals(base, l3);

    LocalDate l4 = adjustDate("+00:00", "+08:00", LocalTime.of(16, 0), base);
    Assertions.assertEquals(next, l4);

    LocalDate l5 = adjustDate("+00:00", "+08:00", LocalTime.of(23, 59), base);
    Assertions.assertEquals(next, l5);

    LocalDate l6 = adjustDate("+08:00", "+08:00", LocalTime.of(15, 59), base);
    Assertions.assertEquals(base, l6);

    LocalDate l7 = adjustDate("+08:00", "+08:00", LocalTime.of(16, 0), base);
    Assertions.assertEquals(base, l7);

    LocalDate l8 = adjustDate("+08:00", "+08:00", LocalTime.of(23, 59), base);
    Assertions.assertEquals(base, l8);

    LocalDate l9 = adjustDate("+00:00", "+00:00", LocalTime.of(15, 59), base);
    Assertions.assertEquals(base, l9);

    LocalDate l10 = adjustDate("+00:00", "+00:00", LocalTime.of(16, 0), base);
    Assertions.assertEquals(base, l10);

    LocalDate l11 = adjustDate("+00:00", "+00:00", LocalTime.of(23, 59), base);
    Assertions.assertEquals(base, l11);

    LocalDate l12 = adjustDate("+12:00", "-12:00", LocalTime.of(23, 59), base);
    Assertions.assertEquals(pre, l12);

    LocalDate l13 = adjustDate("-12:00", "+12:00", LocalTime.of(23, 59), base);
    Assertions.assertEquals(next, l13);

    LocalDate l14 = adjustDate("+12:00", "-12:00", LocalTime.of(0, 0), base);
    Assertions.assertEquals(pre, l14);

    LocalDate l15 = adjustDate("-12:00", "+12:00", LocalTime.of(0, 0), base);
    Assertions.assertEquals(next, l15);

    LocalDate l16 = adjustDate("-12:00", "-11:45", LocalTime.of(23, 59), base);
    Assertions.assertEquals(next, l16);

    LocalDate l17 = adjustDate("-12:00", "+14:00", LocalTime.of(23, 59), pre);
    Assertions.assertEquals(next, l17);
  }


  @Test
  void testLocalTimeConversion() {
    LocalTime l0 = convertLocalTime("+08:00", "+00:00", LocalTime.of(7, 59));
    Assertions.assertEquals(LocalTime.of(23, 59), l0);

    LocalTime l1 = convertLocalTime("+08:00", "+00:00", LocalTime.of(8, 0));
    Assertions.assertEquals(LocalTime.of(0, 0), l1);

    LocalTime l2 = convertLocalTime("+08:00", "+00:00", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(15, 59), l2);

    LocalTime l3 = convertLocalTime("+00:00", "+08:00", LocalTime.of(15, 59));
    Assertions.assertEquals(LocalTime.of(23, 59), l3);

    LocalTime l4 = convertLocalTime("+00:00", "+08:00", LocalTime.of(16, 0));
    Assertions.assertEquals(LocalTime.of(0, 0), l4);

    LocalTime l5 = convertLocalTime("+00:00", "+08:00", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(7, 59), l5);

    LocalTime l6 = convertLocalTime("+08:00", "+08:00", LocalTime.of(15, 59));
    Assertions.assertEquals(LocalTime.of(15, 59), l6);

    LocalTime l7 = convertLocalTime("+08:00", "+08:00", LocalTime.of(16, 0));
    Assertions.assertEquals(LocalTime.of(16, 0), l7);

    LocalTime l8 = convertLocalTime("+08:00", "+08:00", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(23, 59), l8);

    LocalTime l9 = convertLocalTime("+00:00", "+00:00", LocalTime.of(15, 59));
    Assertions.assertEquals(LocalTime.of(15, 59), l9);

    LocalTime l10 = convertLocalTime("+00:00", "+00:00", LocalTime.of(16, 0));
    Assertions.assertEquals(LocalTime.of(16, 0), l10);

    LocalTime l11 = convertLocalTime("+00:00", "+00:00", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(23, 59), l11);

    LocalTime l12 = convertLocalTime("+12:00", "-12:00", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(23, 59), l12);

    LocalTime l13 = convertLocalTime("-12:00", "+12:00", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(23, 59), l13);

    LocalTime l14 = convertLocalTime("+12:00", "-12:00", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(23, 59), l14);

    LocalTime l15 = convertLocalTime("-12:00", "+12:00", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(23, 59), l15);

    LocalTime l16 = convertLocalTime("-12:00", "-11:45", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(00, 14), l16);

    LocalTime l17 = convertLocalTime("-12:00", "+14:00", LocalTime.of(23, 59));
    Assertions.assertEquals(LocalTime.of(01, 59), l17);
  }

  @Test
  void testInvalidFromZone() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      dateOffsetBetween(null, "+00:00", LocalTime.now());
    });
  }

  @Test
  void testInvalidToZone() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      dateOffsetBetween("+08:00", "invalid-zone", LocalTime.now());
    });
  }

  @Test
  void testCrossMonthConversion() {
    LocalDate baseDate = LocalDate.of(2023, 12, 31);
    LocalDate result = adjustDate("+00:00", "+12:00", LocalTime.of(23, 59), baseDate);
    Assertions.assertEquals(LocalDate.of(2024, 1, 1), result);

    LocalDate result1 = adjustDate("+12:00", "+00:00", LocalTime.of(11, 59), LocalDate.of(2024, 1, 1));
    Assertions.assertEquals(baseDate, result1);
  }

  @Test
  void testCrossYearConversion() {
    LocalDate baseDate = LocalDate.of(2023, 12, 31);
    LocalDate result = adjustDate("+00:00", "+01:00", LocalTime.of(23, 59), baseDate);
    Assertions.assertEquals(LocalDate.of(2024, 1, 1), result);

    LocalDate result1 = adjustDate("+12:00", "+00:00", LocalTime.of(11, 59), LocalDate.of(2024, 1, 1));
    Assertions.assertEquals(baseDate, result1);
  }

  @Test
  void testMinTimeConversion() {
    LocalTime result = convertLocalTime("+00:00", "+08:00", LocalTime.MIN);
    Assertions.assertEquals(LocalTime.of(8, 0), result);
  }

  @Test
  void testMaxTimeConversion() {
    LocalTime result = convertLocalTime("+00:00", "+08:00", LocalTime.MAX);
    Assertions.assertEquals(LocalTime.of(7, 59, 59, 999_999_999), result);
  }

  @Test
  void testMidnightEdgeCase() {
    LocalDate result = adjustDate("+08:00", "+00:00", LocalTime.of(0, 0), LocalDate.of(2025, 1, 5));
    Assertions.assertEquals(LocalDate.of(2025, 1, 4), result);
  }

  @Test
  void testLocalDateTimeConversion_NormalCase_Back() {
    // 测试正常的时区转换
    LocalDateTime inputDateTime = LocalDateTime.of(2023, 6, 15, 14, 30, 0);
    LocalDateTime result = convertLocalDateTime("+08:00", "+00:00", inputDateTime);
    LocalDateTime expected = LocalDateTime.of(2023, 6, 15, 6, 30, 0);
    Assertions.assertEquals(expected, result);
  }

  @Test
  void testLocalDateTimeConversion_NormalCase_Forward() {
    // 测试正常的时区转换
    LocalDateTime inputDateTime = LocalDateTime.of(2023, 6, 15, 6, 30, 0);
    LocalDateTime result = convertLocalDateTime("+00:00", "+08:00", inputDateTime);
    LocalDateTime expected = LocalDateTime.of(2023, 6, 15, 14, 30, 0);
    Assertions.assertEquals(expected, result);
  }

  @Test
  void testLocalDateTimeConversion_SameTimeZone() {
    // 测试相同时区的转换
    LocalDateTime inputDateTime = LocalDateTime.of(2023, 6, 15, 14, 30, 0);
    LocalDateTime result = convertLocalDateTime("+08:00", "+08:00", inputDateTime);
    Assertions.assertEquals(inputDateTime, result);
  }

  @Test
  void testLocalDateTimeConversion_DateCrossing_Back() {
    // 测试跨日期的时区转换
    LocalDateTime inputDateTime = LocalDateTime.of(2023, 6, 15, 10, 30, 0);
    LocalDateTime result = convertLocalDateTime("+08:00", "-08:00", inputDateTime);
    LocalDateTime expected = LocalDateTime.of(2023, 6, 14, 18, 30, 0);
    Assertions.assertEquals(expected, result);
  }

  @Test
  void testLocalDateTimeConversion_DateCrossing_Forward() {
    // 测试向未来日期的时区转换
    LocalDateTime inputDateTime = LocalDateTime.of(2023, 6, 15, 23, 30, 0);
    LocalDateTime result = convertLocalDateTime("-08:00", "+08:00", inputDateTime);
    LocalDateTime expected = LocalDateTime.of(2023, 6, 16, 15, 30, 0);
    Assertions.assertEquals(expected, result);
  }

  @Test
  void testLocalDateTimeConversion_HalfHourTimeZone() {
    // 测试半小时间 zone 转换
    LocalDateTime inputDateTime = LocalDateTime.of(2023, 6, 15, 14, 30, 0);
    LocalDateTime result = convertLocalDateTime("+08:00", "+05:30", inputDateTime);
    LocalDateTime expected = LocalDateTime.of(2023, 6, 15, 12, 0, 0);
    Assertions.assertEquals(expected, result);
  }

  @Test
  void testLocalDateTimeConversion_InvalidSourceTimeZone() {
    // 测试无效的源时区
    LocalDateTime inputDateTime = LocalDateTime.of(2023, 6, 15, 14, 30, 0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      convertLocalDateTime("+25:00", "+08:00", inputDateTime);
    });
  }

  @Test
  void testLocalDateTimeConversion_InvalidTargetTimeZone() {
    // 测试无效的目标时区
    LocalDateTime inputDateTime = LocalDateTime.of(2023, 6, 15, 14, 30, 0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      convertLocalDateTime("+08:00", "UTC+8", inputDateTime);
    });
  }

  @Test
  void testLocalDateTimeConversion_BothInvalidTimeZones() {
    // 测试源和目标时区都无效
    LocalDateTime inputDateTime = LocalDateTime.of(2023, 6, 15, 14, 30, 0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      convertLocalDateTime("GMT+8", "UTC+8", inputDateTime);
    });
  }

  @Test
  void testLocalDateTimeConversion_MinDateTime() {
    // 测试最小日期时间
    LocalDateTime inputDateTime = LocalDateTime.of(1, 1, 1, 0, 0, 0);
    LocalDateTime result = convertLocalDateTime("+00:00", "+08:00", inputDateTime);
    LocalDateTime expected = LocalDateTime.of(1, 1, 1, 8, 0, 0);
    Assertions.assertEquals(expected, result);
  }

  @Test
  void testLocalDateTimeConversion_MaxDateTime() {
    // 测试最大日期时间
    LocalDateTime inputDateTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
    LocalDateTime result = convertLocalDateTime("+08:00", "+00:00", inputDateTime);
    LocalDateTime expected = LocalDateTime.of(9999, 12, 31, 15, 59, 59);
    Assertions.assertEquals(expected, result);
  }


  @Test
  void testOffsetTimeZone_hours() {
    // 正常情况：给+08:00时区增加2小时
    Assertions.assertEquals("+10:00", shiftTimeZone("+08:00", 2));

    // 正常情况：给-05:00时区增加3小时
    Assertions.assertEquals("-02:00", shiftTimeZone("-05:00", 3));

    // 跨越0点：给+02:30时区减少4小时
    Assertions.assertEquals("-01:30", shiftTimeZone("+02:30", -4));

    // 跨越0点：给-11:00时区增加15小时（跨过±12时区）
    Assertions.assertEquals("+04:00", shiftTimeZone("-11:00", 15));

    // 减少小时数：给+03:00时区减少5小时
    Assertions.assertEquals("-02:00", shiftTimeZone("+03:00", -5));

    // 增加半小时时区
    Assertions.assertEquals("+08:00", shiftTimeZone("+08:00", 0));

    // 测试边界值：+14:00减少24小时
    Assertions.assertEquals("-10:00", shiftTimeZone("+14:00", -24));

    // 测试边界值：+12:00减少24小时
    Assertions.assertEquals("-12:00", shiftTimeZone("+12:00", -24));

    // 测试边界值：-12:00增加24小时
    Assertions.assertEquals("+12:00", shiftTimeZone("-12:00", 24));

    // 给+05:30增加2小时
    Assertions.assertEquals("+07:30", shiftTimeZone("+05:30", 2));

    // 给-09:30减少3小时
    Assertions.assertEquals("-12:30", shiftTimeZone("-09:30", -3));

    // 给+00:30增加1小时
    Assertions.assertEquals("+01:30", shiftTimeZone("+00:30", 1));

    // 给+00:30增加2小时
    Assertions.assertEquals("+02:30", shiftTimeZone("+00:30", 2));

    // 超过24小时取模：+00:30 + 26h = +02:30 (26h % 24h = 2h)
    Assertions.assertEquals("+02:30", shiftTimeZone("+00:30", 26));
    // 超过24小时取模：+00:30 + 50h = +02:30 (50h % 24h = 2h)
    Assertions.assertEquals("+02:30", shiftTimeZone("+00:30", 50));

    // 测试边界值：-12:00增加36小时
    Assertions.assertEquals("+00:00", shiftTimeZone("-12:00", 36));

    // 测试边界值：-12:00减少36小时
    Assertions.assertEquals("+00:00", shiftTimeZone("-12:00", -36));

    // 溢出 ±18:00 折叠：+14:00 + 5h = +19h → -05:00
    Assertions.assertEquals("-05:00", shiftTimeZone("+14:00", 5));
    // 溢出 ±18:00 折叠：-12:00 - 7h = -19h → +05:00
    Assertions.assertEquals("+05:00", shiftTimeZone("-12:00", -7));
    // 溢出 ±18:00 折叠：+12:00 + 7h = +19h → -05:00
    Assertions.assertEquals("-05:00", shiftTimeZone("+12:00", 7));
    // 溢出 ±18:00 折叠：-05:00 - 14h = -19h → +05:00
    Assertions.assertEquals("+05:00", shiftTimeZone("-05:00", -14));
  }

  @Test
  void testOffsetTimeZone_hours_minutes() {
    Assertions.assertEquals("+10:30", shiftTimeZone("+08:00", 2, 30));

    Assertions.assertEquals("-01:30", shiftTimeZone("-05:00", 3, 30));

    Assertions.assertEquals("-01:00", shiftTimeZone("+02:30", -4, 30));

    Assertions.assertEquals("+04:30", shiftTimeZone("-11:00", 15, 30));

    Assertions.assertEquals("-01:30", shiftTimeZone("+03:00", -5, 30));

    Assertions.assertEquals("+08:30", shiftTimeZone("+08:00", 0, 30));

    Assertions.assertEquals("-09:30", shiftTimeZone("+14:00", -24, 30));

    Assertions.assertEquals("-11:30", shiftTimeZone("+12:00", -24, 30));

    Assertions.assertEquals("+12:30", shiftTimeZone("-12:00", 24, 30));

    Assertions.assertEquals("+07:00", shiftTimeZone("+05:30", 2, -30));

    Assertions.assertEquals("-13:00", shiftTimeZone("-09:30", -3, -30));

    Assertions.assertEquals("+01:00", shiftTimeZone("+00:30", 1, -30));

    // 15分钟偏移
    Assertions.assertEquals("+08:15", shiftTimeZone("+08:00", 0, 15));
    Assertions.assertEquals("+08:45", shiftTimeZone("+08:00", 0, 45));
    Assertions.assertEquals("+07:45", shiftTimeZone("+08:00", 0, -15));
    Assertions.assertEquals("+07:15", shiftTimeZone("+08:00", 0, -45));

    // 尼泊尔时区
    Assertions.assertEquals("+06:00", shiftTimeZone("+05:45", 0, 15));

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> shiftTimeZone("+00:30", 1, -40));

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> shiftTimeZone("+00:30", 1, 40));

    // 测试边界值：-12:00增加36小时30分
    Assertions.assertEquals("+00:30", shiftTimeZone("-12:00", 36, 30));
  }

  @Test
  void testOffsetTimeZoneWithInvalidTimeZone() {
    // 测试无效时区格式
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      shiftTimeZone("+25:00", 2);
    });
    // +25:01 不在15分钟步进集合里
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      shiftTimeZone("+25:01", 0, 0);
    });
  }

  @Test
  void testOffsetTimeZoneQuarterHourTimeZones() {
    Assertions.assertEquals("+10:45", shiftTimeZone("+05:45", 5));
    Assertions.assertEquals("-09:15", shiftTimeZone("-08:15", -1));
    Assertions.assertEquals("+12:45", shiftTimeZone("+12:15", 0, 30));
  }

  @Test
  void testOffsetTimeZoneWithInvalidTimeZoneFormat() {
    // 测试无效时区格式
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      shiftTimeZone("0800", 2);
    });
  }

  @Test
  void testOffsetTimeZoneInvalidMinutes() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone("+08:00", 0, 60));

    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone("+08:00", 0, 90));

    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone("+08:00", 0, -60));

    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone("+08:00", 0, -90));
  }

  @Test
  void testOffsetTimeZoneTwoArgsEqualsThreeArgsZero() {
    Assertions.assertEquals(shiftTimeZone("+08:00", 2), shiftTimeZone("+08:00", 2, 0));
    Assertions.assertEquals(shiftTimeZone("+08:00", -3), shiftTimeZone("+08:00", -3, 0));
    Assertions.assertEquals(shiftTimeZone("-05:00", 1), shiftTimeZone("-05:00", 1, 0));
    Assertions.assertEquals(shiftTimeZone("+02:30", -4), shiftTimeZone("+02:30", -4, 0));
  }

  // ==================================================================
  //  checkHHmmValid
  // ==================================================================

  @Test
  void testCheckHHmmValidNormal() {
    Assertions.assertTrue(checkHHmmValid("00:00"));
    Assertions.assertTrue(checkHHmmValid("12:30"));
    Assertions.assertTrue(checkHHmmValid("23:59"));
  }

  @Test
  void testCheckHHmmValidNull() {
    Assertions.assertFalse(checkHHmmValid(null));
  }

  @Test
  void testCheckHHmmValidNegativeHours() {
    Assertions.assertTrue(checkHHmmValid("-01:00"));
    Assertions.assertTrue(checkHHmmValid("-12:30"));
    Assertions.assertTrue(checkHHmmValid("-18:00"));
  }

  @Test
  void testCheckHHmmValidNegativeMinutes() {
    Assertions.assertFalse(checkHHmmValid("00:-05"));
  }

  @Test
  void testCheckHHmmValidInvalidFormat() {
    Assertions.assertFalse(checkHHmmValid("1234"));
    Assertions.assertFalse(checkHHmmValid("12:34:56"));
    Assertions.assertFalse(checkHHmmValid("ab:cd"));
  }

  @Test
  void testCheckHHmmValidOutOfBounds() {
    Assertions.assertFalse(checkHHmmValid("24:00"));
    Assertions.assertFalse(checkHHmmValid("12:60"));
    Assertions.assertFalse(checkHHmmValid("99:99"));
  }

  // ==================================================================
  //  确定性：FIXED_DATE 替代 LocalDate.now()
  // ==================================================================

  @Test
  void testTimeZoneOffsetDeterministic() {
    LocalTime t = LocalTime.of(7, 59);
    int expected = dateOffsetBetween("+08:00", "+00:00", t);
    for (int i = 0; i < 5; i++) {
      Assertions.assertEquals(expected, dateOffsetBetween("+08:00", "+00:00", t));
    }
  }

  @Test
  void testConvertLocalTimeDeterministic() {
    LocalTime t = LocalTime.of(7, 59);
    LocalTime expected = convertLocalTime("+08:00", "+00:00", t);
    for (int i = 0; i < 5; i++) {
      Assertions.assertEquals(expected, convertLocalTime("+08:00", "+00:00", t));
    }
  }

  // ==================================================================
  //  shiftTimeZone 边界
  // ==================================================================

  @Test
  void testShiftTimeZoneNullTimeZone() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone(null, 2));
  }

  @Test
  void testShiftTimeZoneNullTimeZoneThreeArgs() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone(null, 2, 30));
  }

  @Test
  void testShiftTimeZoneZeroShift() {
    Assertions.assertEquals("+12:00", shiftTimeZone("+12:00", 0));
    Assertions.assertEquals("-12:00", shiftTimeZone("-12:00", 0, 0));
    Assertions.assertEquals("+00:00", shiftTimeZone("+00:00", 0));
    Assertions.assertEquals("+14:00", shiftTimeZone("+14:00", 0));
    Assertions.assertEquals("+08:15", shiftTimeZone("+08:15", 0, 0));
    Assertions.assertEquals("+05:45", shiftTimeZone("+05:45", 0));
  }

  @Test
  void testShiftTimeZoneMinutesBoundary59() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone("+08:00", 0, 59));
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone("+08:00", 0, -59));
  }

  @Test
  void testShiftTimeZoneMinutesBoundary75() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone("+08:00", 0, 75));
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> shiftTimeZone("+08:00", 0, -75));
  }

  @Test
  void testShiftTimeZoneMinutesBoundaryZero() {
    Assertions.assertEquals("+08:00", shiftTimeZone("+08:00", 0, 0));
    Assertions.assertEquals("-05:45", shiftTimeZone("-05:45", 0, 0));
  }

  // ==================================================================
  //  dateOffsetBetween 边界
  // ==================================================================

  @Test
  void testDateOffsetBetweenNullTime() {
    Assertions.assertThrows(NullPointerException.class,
            () -> dateOffsetBetween("+08:00", "+00:00", null));
  }

  @Test
  void testDateOffsetBetweenQuarterHourTimeZones() {
    Assertions.assertEquals(-1,
            dateOffsetBetween("+05:45", "+00:00", LocalTime.of(5, 44)));
    Assertions.assertEquals(0,
            dateOffsetBetween("+05:45", "+00:00", LocalTime.of(5, 45)));
    Assertions.assertEquals(0,
            dateOffsetBetween("+05:45", "+00:00", LocalTime.of(23, 59)));
  }

  @Test
  void testDateOffsetBetweenQuarterHourTimeZonesForward() {
    Assertions.assertEquals(0,
            dateOffsetBetween("+00:00", "+08:45", LocalTime.of(15, 14)));
    Assertions.assertEquals(1,
            dateOffsetBetween("+00:00", "+08:45", LocalTime.of(15, 15)));
  }

  @Test
  void testDateOffsetBetweenLocalTimeMin() {
    Assertions.assertEquals(-1,
            dateOffsetBetween("+08:00", "+00:00", LocalTime.MIN));
  }

  @Test
  void testDateOffsetBetweenLocalTimeMax() {
    Assertions.assertEquals(0,
            dateOffsetBetween("+08:00", "+00:00", LocalTime.MAX));
  }

  // ==================================================================
  //  convertLocalTime 边界
  // ==================================================================

  @Test
  void testConvertLocalTimeNull() {
    Assertions.assertThrows(NullPointerException.class,
            () -> convertLocalTime("+08:00", "+00:00", null));
  }

  @Test
  void testConvertLocalTimeInvalidSource() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> convertLocalTime("+25:00", "+00:00", LocalTime.NOON));
  }

  @Test
  void testConvertLocalTimeInvalidTarget() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> convertLocalTime("+08:00", "invalid", LocalTime.NOON));
  }

  @Test
  void testConvertLocalTimeQuarterHour() {
    Assertions.assertEquals(LocalTime.of(23, 44),
            convertLocalTime("+08:15", "+00:00", LocalTime.of(7, 59)));
    Assertions.assertEquals(LocalTime.of(23, 45),
            convertLocalTime("+08:15", "+00:00", LocalTime.of(8, 0)));
    Assertions.assertEquals(LocalTime.of(15, 59),
            convertLocalTime("+00:00", "+08:15", LocalTime.of(7, 44)));
    Assertions.assertEquals(LocalTime.of(16, 0),
            convertLocalTime("+00:00", "+08:15", LocalTime.of(7, 45)));
  }

  // ==================================================================
  //  convertLocalDateTime 边界
  // ==================================================================

  @Test
  void testConvertLocalDateTimeQuarterHour() {
    LocalDateTime result = convertLocalDateTime("+05:45", "+00:00",
            LocalDateTime.of(2023, 6, 15, 5, 44, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 14, 23, 59, 0), result);

    LocalDateTime result2 = convertLocalDateTime("+05:45", "+00:00",
            LocalDateTime.of(2023, 6, 15, 5, 45, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 15, 0, 0, 0), result2);
  }

  @Test
  void testConvertLocalDateTimeQuarterHourDateCrossing() {
    LocalDateTime result = convertLocalDateTime("+00:00", "+08:45",
            LocalDateTime.of(2023, 6, 15, 15, 14, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 15, 23, 59, 0), result);

    LocalDateTime result2 = convertLocalDateTime("+00:00", "+08:45",
            LocalDateTime.of(2023, 6, 15, 15, 15, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 16, 0, 0, 0), result2);
  }

  @Test
  void testConvertLocalDateTimeNull() {
    Assertions.assertThrows(NullPointerException.class,
            () -> convertLocalDateTime("+08:00", "+00:00", null));
  }

  // ==================================================================
  //  checkHHmmValid 边界
  // ==================================================================

  @Test
  void testCheckHHmmValidBoundaryValues() {
    Assertions.assertTrue(checkHHmmValid("00:00"));
    Assertions.assertTrue(checkHHmmValid("00:59"));
    Assertions.assertTrue(checkHHmmValid("23:00"));
    Assertions.assertTrue(checkHHmmValid("23:59"));
    Assertions.assertTrue(checkHHmmValid("00:01"));
  }

  @Test
  void testCheckHHmmValidLeadingZeros() {
    Assertions.assertTrue(checkHHmmValid("07:05"));
    Assertions.assertTrue(checkHHmmValid("09:03"));
  }

  @Test
  void testCheckHHmmValidSingleDigit() {
    Assertions.assertFalse(checkHHmmValid("0:00"));
    Assertions.assertFalse(checkHHmmValid("2:30"));
  }

  @Test
  void testCheckHHmmValidEmptyString() {
    Assertions.assertFalse(checkHHmmValid(""));
  }

  @Test
  void testCheckHHmmValidMissingColon() {
    Assertions.assertFalse(checkHHmmValid("1230"));
  }

  // ==================================================================
  //  assertHHmmValid
  // ==================================================================

  @Test
  void testAssertHHmmValidPass() {
    assertHHmmValid("12:30");
    assertHHmmValid("00:00");
    assertHHmmValid("23:59");
  }

  @Test
  void testAssertHHmmValidFailNull() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> assertHHmmValid(null));
  }

  @Test
  void testAssertHHmmValidFailInvalid() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> assertHHmmValid("24:00"));
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> assertHHmmValid("ab:cd"));
  }

  // ==================================================================
  //  边界时区：+14:00 和 -12:00
  // ==================================================================

  @Test
  void testShiftTimeZoneBoundaryPlusFourteen() {
    Assertions.assertEquals("+14:00", shiftTimeZone("+14:00", 0));
    Assertions.assertEquals("+13:00", shiftTimeZone("+14:00", -1));
    Assertions.assertEquals("+13:45", shiftTimeZone("+14:00", 0, -15));
  }

  @Test
  void testShiftTimeZoneBoundaryMinusTwelve() {
    Assertions.assertEquals("-12:00", shiftTimeZone("-12:00", 0));
    Assertions.assertEquals("-11:00", shiftTimeZone("-12:00", 1));
    Assertions.assertEquals("-11:45", shiftTimeZone("-12:00", 0, 15));
    Assertions.assertEquals("-13:00", shiftTimeZone("-12:00", -1));
  }

  @Test
  void testDateOffsetBetweenBoundary() {
    Assertions.assertEquals(-2,
            dateOffsetBetween("+14:00", "-12:00", LocalTime.of(1, 59)));
    Assertions.assertEquals(-1,
            dateOffsetBetween("+14:00", "-12:00", LocalTime.of(2, 0)));
    Assertions.assertEquals(1,
            dateOffsetBetween("-12:00", "+14:00", LocalTime.of(21, 59)));
    Assertions.assertEquals(2,
            dateOffsetBetween("-12:00", "+14:00", LocalTime.of(22, 0)));
  }

  @Test
  void testConvertLocalTimeBoundary() {
    Assertions.assertEquals(LocalTime.of(0, 0),
            convertLocalTime("+14:00", "-12:00", LocalTime.of(2, 0)));
    Assertions.assertEquals(LocalTime.of(23, 59),
            convertLocalTime("+14:00", "-12:00", LocalTime.of(1, 59)));
    Assertions.assertEquals(LocalTime.of(0, 0),
            convertLocalTime("-12:00", "+14:00", LocalTime.of(22, 0)));
    Assertions.assertEquals(LocalTime.of(23, 59),
            convertLocalTime("-12:00", "+14:00", LocalTime.of(21, 59)));
  }

  @Test
  void testConvertLocalDateTimeBoundary() {
    LocalDateTime result = convertLocalDateTime("+14:00", "-12:00",
            LocalDateTime.of(2023, 6, 15, 1, 59, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 13, 23, 59, 0), result);

    LocalDateTime result2 = convertLocalDateTime("+14:00", "-12:00",
            LocalDateTime.of(2023, 6, 15, 2, 0, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 14, 0, 0, 0), result2);

    LocalDateTime result3 = convertLocalDateTime("-12:00", "+14:00",
            LocalDateTime.of(2023, 6, 15, 21, 59, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 16, 23, 59, 0), result3);

    LocalDateTime result4 = convertLocalDateTime("-12:00", "+14:00",
            LocalDateTime.of(2023, 6, 15, 22, 0, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 17, 0, 0, 0), result4);
  }

  @Test
  void testAdjustDateBoundary() {
    LocalDate base = LocalDate.of(2025, 1, 5);

    Assertions.assertEquals(LocalDate.of(2025, 1, 3),
        adjustDate("+14:00", "-12:00", LocalTime.of(1, 59), base));
    Assertions.assertEquals(LocalDate.of(2025, 1, 4),
        adjustDate("+14:00", "-12:00", LocalTime.of(2, 0), base));
    Assertions.assertEquals(LocalDate.of(2025, 1, 6),
        adjustDate("-12:00", "+14:00", LocalTime.of(21, 59), base));
    Assertions.assertEquals(LocalDate.of(2025, 1, 7),
        adjustDate("-12:00", "+14:00", LocalTime.of(22, 0), base));
  }

  // ==================================================================
  //  ±18:00 边界时区
  // ==================================================================

  @Test
  void testTimeZoneSetAcceptsBoundary() {
    Assertions.assertEquals("+18:00", shiftTimeZone("+18:00", 0));
    Assertions.assertEquals("-18:00", shiftTimeZone("-18:00", 0));
    Assertions.assertEquals("+17:45", shiftTimeZone("+17:45", 0));
    Assertions.assertEquals("-17:45", shiftTimeZone("-17:45", 0));
  }

  @Test
  void testShiftTimeZonePlusEighteen() {
    Assertions.assertEquals("+18:00", shiftTimeZone("+18:00", 0));
    Assertions.assertEquals("+17:00", shiftTimeZone("+18:00", -1));
    Assertions.assertEquals("-18:00", shiftTimeZone("+18:00", -36));
    Assertions.assertEquals("+17:45", shiftTimeZone("+18:00", 0, -15));
  }

  @Test
  void testShiftTimeZoneMinusEighteen() {
    Assertions.assertEquals("-18:00", shiftTimeZone("-18:00", 0));
    Assertions.assertEquals("-17:00", shiftTimeZone("-18:00", 1));
    Assertions.assertEquals("+18:00", shiftTimeZone("-18:00", 36));
    Assertions.assertEquals("-17:45", shiftTimeZone("-18:00", 0, 15));
  }

  @Test
  void testShiftTimeZoneOverflowFromPlusEighteen() {
    Assertions.assertEquals("-05:00", shiftTimeZone("+18:00", 1));
    Assertions.assertEquals("-05:45", shiftTimeZone("+18:00", 0, 15));
  }

  @Test
  void testShiftTimeZoneOverflowFromMinusEighteen() {
    Assertions.assertEquals("+05:00", shiftTimeZone("-18:00", -1));
    Assertions.assertEquals("+05:45", shiftTimeZone("-18:00", 0, -15));
  }

  @Test
  void testConvertLocalTimePlusMinusEighteen() {
    Assertions.assertEquals(LocalTime.of(0, 0),
        convertLocalTime("+18:00", "-18:00", LocalTime.NOON));
    Assertions.assertEquals(LocalTime.of(0, 0),
        convertLocalTime("-18:00", "+18:00", LocalTime.NOON));
  }

  @Test
  void testConvertLocalDateTimePlusMinusEighteen() {
    LocalDateTime result = convertLocalDateTime("+18:00", "-18:00",
        LocalDateTime.of(2023, 6, 15, 12, 0, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 14, 0, 0, 0), result);

    LocalDateTime result2 = convertLocalDateTime("-18:00", "+18:00",
        LocalDateTime.of(2023, 6, 15, 12, 0, 0));
    Assertions.assertEquals(LocalDateTime.of(2023, 6, 17, 0, 0, 0), result2);
  }

  @Test
  void testDateOffsetBetweenPlusMinusEighteen() {
    Assertions.assertEquals(-2,
        dateOffsetBetween("+18:00", "-18:00", LocalTime.of(11, 59)));
    Assertions.assertEquals(-1,
        dateOffsetBetween("+18:00", "-18:00", LocalTime.of(12, 0)));
    Assertions.assertEquals(1,
        dateOffsetBetween("-18:00", "+18:00", LocalTime.of(11, 59)));
    Assertions.assertEquals(2,
        dateOffsetBetween("-18:00", "+18:00", LocalTime.of(12, 0)));
  }

  @Test
  void testTimeZoneSetRejectsBeyondBoundary() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> shiftTimeZone("+18:15", 0));
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> shiftTimeZone("-18:15", 0));
  }
}