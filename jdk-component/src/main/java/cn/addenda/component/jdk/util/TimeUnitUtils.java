package cn.addenda.component.jdk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/9/2 15:35
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUnitUtils {

  private static final Map<TimeUnit, String> NAME_TO_ALIAS_MAP = new EnumMap<>(TimeUnit.class);
  private static final Map<String, TimeUnit> ALIAS_NAME_TO_MAP = new HashMap<>();

  public static final String NANOSECONDS_NAME = "ns";
  public static final String MICROSECONDS_NAME = "μs";
  public static final String MILLISECONDS_NAME = "ms";
  public static final String SECONDS_NAME = "s";
  public static final String MINUTES_NAME = "min(s)";
  public static final String HOURS_NAME = "hour(s)";
  public static final String DAYS_NAME = "day(s)";

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

  public static String aliasTimeUnit(TimeUnit timeUnit) {
    return NAME_TO_ALIAS_MAP.get(timeUnit);
  }

  public static TimeUnit formatTimeUnit(String name) {
    return ALIAS_NAME_TO_MAP.get(name);
  }

}
