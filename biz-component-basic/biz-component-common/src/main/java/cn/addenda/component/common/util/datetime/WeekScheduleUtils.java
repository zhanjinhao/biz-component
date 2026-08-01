package cn.addenda.component.common.util.datetime;

import cn.addenda.component.common.util.string.Slf4jUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekScheduleUtils {

  /**
   * 获取日期时间所在的班期
   *
   * @param localDateTime 日期时间
   */
  public static String getWeekSchedule(LocalDateTime localDateTime) {
    return getWeekSchedule(localDateTime.toLocalDate());
  }

  /**
   * 获取日期所在的班期
   *
   * @param localDate 日期
   */
  public static String getWeekSchedule(LocalDate localDate) {
    return String.valueOf(localDate.getDayOfWeek().ordinal() + 1);
  }

  /**
   * 将周班期按天数偏移
   *
   * @param weekSchedule 原班期
   * @param offsetDay    偏移天数
   * @return 原班期偏移指定天数之后的班期
   */
  public static String convertWeekSchedule(String weekSchedule, int offsetDay) {
    assertWeekSchedule(weekSchedule);

    if (offsetDay == 0) {
      return weekSchedule;
    }

    offsetDay = offsetDay % 7;
    offsetDay = add7UntilNonNegative(offsetDay);

    List<Integer> weekScheduleList = new ArrayList<>();
    int length = weekSchedule.length();
    for (int i = 0; i < length; i++) {
      weekScheduleList.add(Integer.parseInt(String.valueOf(weekSchedule.charAt(i))));
    }

    List<Integer> newWeekScheduleList = new ArrayList<>();

    for (Integer i : weekScheduleList) {
      int temp = (i + offsetDay) % 7;
      newWeekScheduleList.add(temp == 0 ? 7 : temp);
    }

    newWeekScheduleList.sort(Comparator.comparing(Integer::intValue));
    return newWeekScheduleList.stream().map(String::valueOf).collect(Collectors.joining(""));
  }

  private static void assertWeekSchedule(String weekSchedule) {
    if (weekSchedule == null || weekSchedule.isEmpty()) {
      throw new IllegalArgumentException(Slf4jUtils.format("班期[{}]格式错误", weekSchedule));
    }
    int length = weekSchedule.length();
    Set<Character> seen = new HashSet<>();
    for (int i = 0; i < length; i++) {
      char c = weekSchedule.charAt(i);
      if (c < '1' || c > '7') {
        throw new IllegalArgumentException(Slf4jUtils.format("班期[{}]格式错误", weekSchedule));
      }
      if (!seen.add(c)) {
        throw new IllegalArgumentException(Slf4jUtils.format("班期[{}]格式错误", weekSchedule));
      }
    }
  }

  private static int add7UntilNonNegative(int offset) {
    while (offset < 0) {
      offset += 7;
    }
    return offset;
  }

}
