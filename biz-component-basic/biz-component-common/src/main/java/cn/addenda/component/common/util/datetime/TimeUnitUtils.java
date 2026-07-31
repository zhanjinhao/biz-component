package cn.addenda.component.common.util.datetime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link TimeUnit} 工具类，提供时间单位的别名映射和可读格式化。
 *
 * <p>支持的别名（紧凑格式）：
 * <table>
 *   <tr><th>TimeUnit</th><th>别名</th></tr>
 *   <tr><td>{@link TimeUnit#NANOSECONDS}</td><td>{@code ns}</td></tr>
 *   <tr><td>{@link TimeUnit#MICROSECONDS}</td><td>{@code μs}</td></tr>
 *   <tr><td>{@link TimeUnit#MILLISECONDS}</td><td>{@code ms}</td></tr>
 *   <tr><td>{@link TimeUnit#SECONDS}</td><td>{@code s}</td></tr>
 *   <tr><td>{@link TimeUnit#MINUTES}</td><td>{@code m}</td></tr>
 *   <tr><td>{@link TimeUnit#HOURS}</td><td>{@code h}</td></tr>
 *   <tr><td>{@link TimeUnit#DAYS}</td><td>{@code d}</td></tr>
 * </table>
 *
 * <p>可读格式示例：{@code 1h 2m 5s}、{@code 1h5s}、{@code -1h 30m}。
 * 单位间空格可选，数字紧贴单位，不支持重复单位。
 *
 * @author addenda
 * @since 2023/9/2 15:35
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUnitUtils {

  private static final Map<TimeUnit, String> NAME_TO_ALIAS_MAP = new EnumMap<>(TimeUnit.class);
  private static final Map<String, TimeUnit> ALIAS_NAME_TO_MAP = new HashMap<>();

  public static final String NANOSECONDS_NAME = "ns";
  public static final String MICROSECONDS_NAME = "\u03BCs";
  public static final String MILLISECONDS_NAME = "ms";
  public static final String SECONDS_NAME = "s";
  public static final String MINUTES_NAME = "m";
  public static final String HOURS_NAME = "h";
  public static final String DAYS_NAME = "d";

  private static final Pattern DURATION_TOKEN = Pattern.compile("(\\d+)([a-zA-Z\u03BC]+)");
  private static final Pattern DURATION_FORMAT = Pattern.compile("^\\s*(\\d+[a-zA-Z\u03BC]+\\s*)+$");

  private static final TimeUnit[] SORTED_UNITS = {
          TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS,
          TimeUnit.MILLISECONDS, TimeUnit.MICROSECONDS, TimeUnit.NANOSECONDS
  };

  static {
    NAME_TO_ALIAS_MAP.put(TimeUnit.NANOSECONDS, NANOSECONDS_NAME);
    NAME_TO_ALIAS_MAP.put(TimeUnit.MICROSECONDS, MICROSECONDS_NAME);
    NAME_TO_ALIAS_MAP.put(TimeUnit.MILLISECONDS, MILLISECONDS_NAME);
    NAME_TO_ALIAS_MAP.put(TimeUnit.SECONDS, SECONDS_NAME);
    NAME_TO_ALIAS_MAP.put(TimeUnit.MINUTES, MINUTES_NAME);
    NAME_TO_ALIAS_MAP.put(TimeUnit.HOURS, HOURS_NAME);
    NAME_TO_ALIAS_MAP.put(TimeUnit.DAYS, DAYS_NAME);

    NAME_TO_ALIAS_MAP.forEach((key, value) -> ALIAS_NAME_TO_MAP.put(value, key));
  }

  /**
   * 返回 {@link TimeUnit} 对应的紧凑别名。
   *
   * @param timeUnit 时间单位，为 {@code null} 时返回 {@code null}
   * @return 别名如 {@code "h"}、{@code "m"}，无映射时返回 {@code null}
   */
  public static String aliasTimeUnit(TimeUnit timeUnit) {
    return NAME_TO_ALIAS_MAP.get(timeUnit);
  }

  /**
   * 将紧凑别名字符串解析为对应的 {@link TimeUnit}。
   *
   * @param name 别名如 {@code "h"}、{@code "s"}，为 {@code null} 或未知时返回 {@code null}
   * @return 对应的 TimeUnit，无法识别时返回 {@code null}
   */
  public static TimeUnit parseTimeUnit(String name) {
    return ALIAS_NAME_TO_MAP.get(name);
  }

  /**
   * 将指定单位的时间段转换为紧凑可读格式。
   * 按从大到小拆分，值为 0 的单位自动省略。
   *
   * <pre>{@code
   * toHumanReadable(3725000, MILLISECONDS) → "1h 2m 5s"
   * toHumanReadable(0,     SECONDS)      → "0s"
   * toHumanReadable(-5,    SECONDS)      → "-5s"
   * }</pre>
   *
   * @param duration 时长
   * @param unit     时长单位，为 {@code null} 时返回 {@code null}
   * @return 紧凑格式字符串，如 {@code "1h 2m 5s"}
   */
  public static String toHumanReadable(long duration, TimeUnit unit) {
    if (unit == null) {
      return null;
    }
    if (duration == 0) {
      return "0" + NAME_TO_ALIAS_MAP.get(unit);
    }

    boolean negative = duration < 0;
    long remaining = unit.toNanos(Math.abs(duration));

    StringBuilder sb = new StringBuilder();
    if (negative) {
      sb.append('-');
    }

    boolean first = true;
    for (TimeUnit u : SORTED_UNITS) {
      long value = u.convert(remaining, TimeUnit.NANOSECONDS);
      if (value > 0) {
        if (!first) {
          sb.append(' ');
        }
        sb.append(value).append(NAME_TO_ALIAS_MAP.get(u));
        remaining -= u.toNanos(value);
        first = false;
      }
    }

    return sb.toString();
  }

  /**
   * 将 {@link Duration} 转换为紧凑可读格式，委托 {@link #toHumanReadable(long, TimeUnit)}。
   *
   * @param duration 时长，为 {@code null} 时返回 {@code null}
   * @return 紧凑格式字符串
   */
  public static String toHumanReadable(Duration duration) {
    if (duration == null) {
      return null;
    }
    return toHumanReadable(duration.toNanos(), TimeUnit.NANOSECONDS);
  }

  /**
   * 将紧凑格式的时间字符串解析为纳秒数。
   *
   * <p>格式规则：
   * <ul>
   *   <li>数字紧贴单位，单位间空格可选：{@code "1h 30m"} 或 {@code "1h30m"}</li>
   *   <li>支持负号前缀：{@code "-1h 5s"}、{@code "- 1h 5s"}</li>
   *   <li>单位必须为紧凑别名：{@code d h m s ms μs ns}</li>
   *   <li>不允许重复单位</li>
   * </ul>
   *
   * @param duration 紧凑格式时间字符串，不可为 {@code null} 或空
   * @return 总纳秒数
   * @throws IllegalArgumentException 格式无效、单位未知、单位重复时
   */
  public static long parseDuration(String duration) {
    if (duration == null || duration.trim().isEmpty()) {
      throw new IllegalArgumentException("duration cannot be null or empty");
    }

    String trimmed = duration.trim();
    boolean negative = trimmed.startsWith("-");
    if (negative) {
      trimmed = trimmed.substring(1).trim();
    }

    if (!DURATION_FORMAT.matcher(trimmed).matches()) {
      throw new IllegalArgumentException("Invalid duration format: " + duration);
    }

    Matcher m = DURATION_TOKEN.matcher(trimmed);
    if (!m.find()) {
      throw new IllegalArgumentException("Invalid duration format: " + duration);
    }

    long totalNanos = 0;
    Set<TimeUnit> seen = new HashSet<>();
    do {
      long value;
      try {
        value = Long.parseLong(m.group(1));
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid number in duration: " + m.group(1), e);
      }
      if (value < 0) {
        throw new IllegalArgumentException("Negative value not allowed: " + value);
      }
      TimeUnit tu = ALIAS_NAME_TO_MAP.get(m.group(2));
      if (tu == null) {
        throw new IllegalArgumentException("Unknown time unit: " + m.group(2));
      }
      if (!seen.add(tu)) {
        throw new IllegalArgumentException("Duplicate time unit: " + m.group(2));
      }
      totalNanos += tu.toNanos(value);
    } while (m.find());

    return negative ? -totalNanos : totalNanos;
  }

  /**
   * 将紧凑格式的时间字符串解析为毫秒数，纳秒部分会被截断。
   * 委托 {@link #parseDuration(String)} 后转换。
   *
   * @param duration 紧凑格式时间字符串
   * @return 总毫秒数（截断至毫秒精度）
   * @throws IllegalArgumentException 格式无效时
   */
  public static long parseDurationToMillis(String duration) {
    return TimeUnit.NANOSECONDS.toMillis(parseDuration(duration));
  }

}
