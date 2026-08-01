package cn.addenda.component.common.test.util.datetime;

import cn.addenda.component.common.util.datetime.WeekScheduleUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static cn.addenda.component.common.util.datetime.WeekScheduleUtils.convertWeekSchedule;
import static cn.addenda.component.common.util.datetime.WeekScheduleUtils.getWeekSchedule;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeekScheduleUtilsTest {

  @Test
  void testWeekScheduleConversion() {
    String l0 = convertWeekSchedule("123", 1);
    Assertions.assertEquals("234", l0);

    String l1 = convertWeekSchedule("567", 1);
    Assertions.assertEquals("167", l1);

    String l2 = convertWeekSchedule("456", 2);
    Assertions.assertEquals("167", l2);

    String l3 = convertWeekSchedule("123", -1);
    Assertions.assertEquals("127", l3);

    String l4 = convertWeekSchedule("234", -1);
    Assertions.assertEquals("123", l4);

    String l5 = convertWeekSchedule("345", -3);
    Assertions.assertEquals("127", l5);

    String l6 = convertWeekSchedule("123", 0);
    Assertions.assertEquals("123", l6);

    String l7 = convertWeekSchedule("234", 0);
    Assertions.assertEquals("234", l7);

    String l8 = convertWeekSchedule("345", 0);
    Assertions.assertEquals("345", l8);

    String l9 = convertWeekSchedule("167", 1);
    Assertions.assertEquals("127", l9);

    String l10 = convertWeekSchedule("167", 3);
    Assertions.assertEquals("234", l10);
  }

  @Test
  void testEmptyInput() {
    assertThrows(IllegalArgumentException.class, () -> {
      convertWeekSchedule("", 1);
    });
  }

  @Test
  void testNonDigitInput() {
    assertThrows(IllegalArgumentException.class, () -> {
      convertWeekSchedule("1a3", 1);
    });
  }

  @Test
  void testDuplicateDigits() {
    assertThrows(IllegalArgumentException.class, () -> {
      convertWeekSchedule("112", 1);
    });
  }

  @Test
  void testLargeOffsetPositive() {
    String result1 = convertWeekSchedule("123", 7);
    Assertions.assertEquals("123", result1);

    String result2 = convertWeekSchedule("123", 8);
    Assertions.assertEquals("234", result2);

    String result3 = convertWeekSchedule("123", 16);
    Assertions.assertEquals("345", result3);
  }

  @Test
  void testLargeOffsetNegative() {
    String result1 = convertWeekSchedule("123", -6);
    Assertions.assertEquals("234", result1);

    String result2 = convertWeekSchedule("123", -7);
    Assertions.assertEquals("123", result2);

    String result3 = convertWeekSchedule("123", -16);
    Assertions.assertEquals("167", result3);
  }

  @Test
  void testSingleDigit() {
    String result = convertWeekSchedule("7", 1);
    Assertions.assertEquals("1", result);
  }

  @Test
  void testAllSameDigit() {
    assertThrows(IllegalArgumentException.class, () -> {
      convertWeekSchedule("111", 2);
    });
  }

  @Test
  void testGetWeekSchedule_Monday() {
    // 测试星期一，LocalDateTime中ordinal为0
    LocalDateTime monday = LocalDateTime.of(2025, 7, 21, 12, 0);
    String result = WeekScheduleUtils.getWeekSchedule(monday);
    Assertions.assertEquals("1", result);
  }

  @Test
  void testGetWeekSchedule_Tuesday() {
    // 测试星期二，LocalDateTime中ordinal为1
    LocalDateTime tuesday = LocalDateTime.of(2025, 7, 22, 12, 0);
    String result = WeekScheduleUtils.getWeekSchedule(tuesday);
    Assertions.assertEquals("2", result);
  }

  @Test
  void testGetWeekSchedule_Wednesday() {
    // 测试星期三，LocalDateTime中ordinal为2
    LocalDateTime wednesday = LocalDateTime.of(2025, 7, 23, 12, 0);
    String result = WeekScheduleUtils.getWeekSchedule(wednesday);
    Assertions.assertEquals("3", result);
  }

  @Test
  void testGetWeekSchedule_Thursday() {
    // 测试星期四，LocalDateTime中ordinal为3
    LocalDateTime thursday = LocalDateTime.of(2025, 7, 24, 12, 0);
    String result = WeekScheduleUtils.getWeekSchedule(thursday);
    Assertions.assertEquals("4", result);
  }

  @Test
  void testGetWeekSchedule_Friday() {
    // 测试星期五，LocalDateTime中ordinal为4
    LocalDateTime friday = LocalDateTime.of(2025, 7, 25, 12, 0);
    String result = WeekScheduleUtils.getWeekSchedule(friday);
    Assertions.assertEquals("5", result);
  }

  @Test
  void testGetWeekSchedule_Saturday() {
    // 测试星期六，LocalDateTime中ordinal为5
    LocalDateTime saturday = LocalDateTime.of(2025, 7, 26, 12, 0);
    String result = WeekScheduleUtils.getWeekSchedule(saturday);
    Assertions.assertEquals("6", result);
  }

  @Test
  void testGetWeekSchedule_Sunday() {
    // 测试星期日，LocalDateTime中ordinal为6
    LocalDateTime sunday = LocalDateTime.of(2025, 7, 27, 12, 0);
    String result = WeekScheduleUtils.getWeekSchedule(sunday);
    Assertions.assertEquals("7", result);
  }

  @Test
  void testGetWeekSchedule_DifferentTimeSameDate() {
    // 测试同一天的不同时间应该返回相同结果
    LocalDateTime morning = LocalDateTime.of(2023, 10, 2, 9, 30); // 星期一早上
    LocalDateTime evening = LocalDateTime.of(2023, 10, 2, 21, 45); // 星期一晚上
    String result1 = WeekScheduleUtils.getWeekSchedule(morning);
    String result2 = WeekScheduleUtils.getWeekSchedule(evening);
    Assertions.assertEquals(result1, result2);
  }

  @Test
  void testInvalidRangeContainsZero() {
    assertThrows(IllegalArgumentException.class, () -> {
      convertWeekSchedule("10", 1);
    });
  }

  @Test
  void testInvalidRangeContainsEight() {
    assertThrows(IllegalArgumentException.class, () -> {
      convertWeekSchedule("128", 1);
    });
  }

  @Test
  void testInvalidRangeContainsNine() {
    assertThrows(IllegalArgumentException.class, () -> {
      convertWeekSchedule("9", 1);
    });
  }

  @Test
  void testNullInput() {
    assertThrows(IllegalArgumentException.class, () -> {
      convertWeekSchedule(null, 1);
    });
  }

  @Test
  void testFullWeekSchedule() {
    String result = convertWeekSchedule("1234567", 0);
    Assertions.assertEquals("1234567", result);
  }

  @Test
  void testFullWeekScheduleOffset() {
    String result = convertWeekSchedule("1234567", 7);
    Assertions.assertEquals("1234567", result);
  }

  @Test
  void testFullWeekScheduleAnyOffset() {
    for (int offset = 1; offset <= 6; offset++) {
      Assertions.assertEquals("1234567", convertWeekSchedule("1234567", offset));
      Assertions.assertEquals("1234567", convertWeekSchedule("1234567", -offset));
    }
  }

  @Test
  void testWrappingEdgePlus1To7() {
    Assertions.assertEquals("7", convertWeekSchedule("1", 6));
  }

  @Test
  void testWrappingEdgePlus7To6() {
    Assertions.assertEquals("6", convertWeekSchedule("7", 6));
  }

  @Test
  void testWrappingEdgeMinus1To7() {
    Assertions.assertEquals("7", convertWeekSchedule("1", -1));
  }

  @Test
  void testWrappingEdgeMinus7To6() {
    Assertions.assertEquals("6", convertWeekSchedule("7", -1));
  }

  @Test
  void testSingleDigitAllOffsets() {
    Assertions.assertEquals("1", convertWeekSchedule("1", 0));
    Assertions.assertEquals("2", convertWeekSchedule("1", 1));
    Assertions.assertEquals("3", convertWeekSchedule("1", 2));
    Assertions.assertEquals("4", convertWeekSchedule("1", 3));
    Assertions.assertEquals("5", convertWeekSchedule("1", 4));
    Assertions.assertEquals("6", convertWeekSchedule("1", 5));
    Assertions.assertEquals("7", convertWeekSchedule("1", 6));
  }

  @Test
  void testUnorderedInput() {
    Assertions.assertEquals("125", convertWeekSchedule("741", 1));
  }

  @Test
  void testCombinedWrappingEdge() {
    Assertions.assertEquals("12", convertWeekSchedule("17", 1));
    Assertions.assertEquals("67", convertWeekSchedule("17", 6));
  }

  @Test
  void testOffsetMinValue() {
    String result = convertWeekSchedule("1", Integer.MIN_VALUE);
    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.matches("[1-7]"));
  }

  @Test
  void testOffsetMaxValue() {
    String result = convertWeekSchedule("1", Integer.MAX_VALUE);
    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.matches("[1-7]"));
  }

  @Test
  void testGetWeekScheduleNullLocalDateTime() {
    assertThrows(NullPointerException.class, () -> {
      getWeekSchedule((LocalDateTime) null);
    });
  }

  @Test
  void testGetWeekScheduleNullLocalDate() {
    assertThrows(NullPointerException.class, () -> {
      getWeekSchedule((LocalDate) null);
    });
  }

  @Test
  void testGetWeekScheduleWithLocalDate() {
    Assertions.assertEquals("4", getWeekSchedule(LocalDate.of(2024, 2, 29)));
    Assertions.assertEquals("3", getWeekSchedule(LocalDate.of(2024, 1, 31)));
    Assertions.assertEquals("1", getWeekSchedule(LocalDate.of(2024, 3, 4)));
  }

  @Test
  void testGetWeekScheduleMonthBoundaries() {
    Assertions.assertEquals("1", getWeekSchedule(LocalDate.of(2024, 4, 1)));
    Assertions.assertEquals("2", getWeekSchedule(LocalDate.of(2024, 4, 30)));
    Assertions.assertEquals("7", getWeekSchedule(LocalDate.of(2024, 12, 1)));
  }

  @Test
  void testConvertWeekScheduleNegativeSign() {
    assertThrows(IllegalArgumentException.class, () -> {
      convertWeekSchedule("-1", 1);
    });
  }

}