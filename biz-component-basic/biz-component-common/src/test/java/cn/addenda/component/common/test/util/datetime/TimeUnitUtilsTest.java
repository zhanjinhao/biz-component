package cn.addenda.component.common.test.util.datetime;

import cn.addenda.component.common.util.datetime.TimeUnitUtils;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TimeUnitUtilsTest {

  // ==================================================================
  //  aliasTimeUnit
  // ==================================================================

  @Test
  void testAliasTimeUnit() {
    assertEquals("ns", TimeUnitUtils.aliasTimeUnit(TimeUnit.NANOSECONDS));
    assertEquals("\u03BCs", TimeUnitUtils.aliasTimeUnit(TimeUnit.MICROSECONDS));
    assertEquals("ms", TimeUnitUtils.aliasTimeUnit(TimeUnit.MILLISECONDS));
    assertEquals("s", TimeUnitUtils.aliasTimeUnit(TimeUnit.SECONDS));
    assertEquals("m", TimeUnitUtils.aliasTimeUnit(TimeUnit.MINUTES));
    assertEquals("h", TimeUnitUtils.aliasTimeUnit(TimeUnit.HOURS));
    assertEquals("d", TimeUnitUtils.aliasTimeUnit(TimeUnit.DAYS));
  }

  @Test
  void testAliasTimeUnitNull() {
    assertNull(TimeUnitUtils.aliasTimeUnit(null));
  }

  // ==================================================================
  //  parseTimeUnit
  // ==================================================================

  @Test
  void testParseTimeUnit() {
    assertEquals(TimeUnit.NANOSECONDS, TimeUnitUtils.parseTimeUnit("ns"));
    assertEquals(TimeUnit.MICROSECONDS, TimeUnitUtils.parseTimeUnit("\u03BCs"));
    assertEquals(TimeUnit.MILLISECONDS, TimeUnitUtils.parseTimeUnit("ms"));
    assertEquals(TimeUnit.SECONDS, TimeUnitUtils.parseTimeUnit("s"));
    assertEquals(TimeUnit.MINUTES, TimeUnitUtils.parseTimeUnit("m"));
    assertEquals(TimeUnit.HOURS, TimeUnitUtils.parseTimeUnit("h"));
    assertEquals(TimeUnit.DAYS, TimeUnitUtils.parseTimeUnit("d"));
  }

  @Test
  void testParseTimeUnitUnknown() {
    assertNull(TimeUnitUtils.parseTimeUnit("xyz"));
  }

  @Test
  void testParseTimeUnitNull() {
    assertNull(TimeUnitUtils.parseTimeUnit(null));
  }

  // ==================================================================
  //  toHumanReadable(long, TimeUnit)
  // ==================================================================

  @Test
  void testToHumanReadableComplex() {
    assertEquals("1h 2m 5s",
            TimeUnitUtils.toHumanReadable(3725000, TimeUnit.MILLISECONDS));
  }

  @Test
  void testToHumanReadableSingleUnit() {
    assertEquals("5s",
            TimeUnitUtils.toHumanReadable(5, TimeUnit.SECONDS));
  }

  @Test
  void testToHumanReadableAllUnits() {
    long duration = TimeUnit.DAYS.toMillis(1)
            + TimeUnit.HOURS.toMillis(2)
            + TimeUnit.MINUTES.toMillis(3)
            + TimeUnit.SECONDS.toMillis(4)
            + 500;
    assertEquals("1d 2h 3m 4s 500ms",
            TimeUnitUtils.toHumanReadable(duration, TimeUnit.MILLISECONDS));
  }

  @Test
  void testToHumanReadableExactlyOneUnit() {
    assertEquals("1m",
            TimeUnitUtils.toHumanReadable(60, TimeUnit.SECONDS));
  }

  @Test
  void testToHumanReadableZero() {
    assertEquals("0ms", TimeUnitUtils.toHumanReadable(0, TimeUnit.MILLISECONDS));
  }

  @Test
  void testToHumanReadableZeroNanos() {
    assertEquals("0ns", TimeUnitUtils.toHumanReadable(0, TimeUnit.NANOSECONDS));
  }

  @Test
  void testToHumanReadableZeroMicros() {
    assertEquals("0\u03BCs", TimeUnitUtils.toHumanReadable(0, TimeUnit.MICROSECONDS));
  }

  @Test
  void testToHumanReadableZeroSeconds() {
    assertEquals("0s", TimeUnitUtils.toHumanReadable(0, TimeUnit.SECONDS));
  }

  @Test
  void testToHumanReadableZeroMinutes() {
    assertEquals("0m", TimeUnitUtils.toHumanReadable(0, TimeUnit.MINUTES));
  }

  @Test
  void testToHumanReadableZeroHours() {
    assertEquals("0h", TimeUnitUtils.toHumanReadable(0, TimeUnit.HOURS));
  }

  @Test
  void testToHumanReadableZeroDays() {
    assertEquals("0d", TimeUnitUtils.toHumanReadable(0, TimeUnit.DAYS));
  }

  @Test
  void testToHumanReadableNegative() {
    assertEquals("-1h 2m 5s",
            TimeUnitUtils.toHumanReadable(-3725000, TimeUnit.MILLISECONDS));
  }

  @Test
  void testToHumanReadableNegativeSingleUnit() {
    assertEquals("-5s",
            TimeUnitUtils.toHumanReadable(-5, TimeUnit.SECONDS));
  }

  @Test
  void testToHumanReadableNullUnit() {
    assertNull(TimeUnitUtils.toHumanReadable(1000, null));
  }

  @Test
  void testToHumanReadableNanosOnly() {
    assertEquals("1ns", TimeUnitUtils.toHumanReadable(1, TimeUnit.NANOSECONDS));
  }

  @Test
  void testToHumanReadableEachUnitSingle() {
    assertEquals("1ns", TimeUnitUtils.toHumanReadable(1, TimeUnit.NANOSECONDS));
    assertEquals("1\u03BCs", TimeUnitUtils.toHumanReadable(1, TimeUnit.MICROSECONDS));
    assertEquals("1ms", TimeUnitUtils.toHumanReadable(1, TimeUnit.MILLISECONDS));
    assertEquals("1s", TimeUnitUtils.toHumanReadable(1, TimeUnit.SECONDS));
    assertEquals("1m", TimeUnitUtils.toHumanReadable(1, TimeUnit.MINUTES));
    assertEquals("1h", TimeUnitUtils.toHumanReadable(1, TimeUnit.HOURS));
    assertEquals("1d", TimeUnitUtils.toHumanReadable(1, TimeUnit.DAYS));
  }

  @Test
  void testToHumanReadableMicroseconds() {
    assertEquals("1ms 234\u03BCs 567ns",
            TimeUnitUtils.toHumanReadable(1234567, TimeUnit.NANOSECONDS));
  }

  @Test
  void testToHumanReadableLargeValue() {
    assertEquals("1\u03BCs",
        TimeUnitUtils.toHumanReadable(1000, TimeUnit.NANOSECONDS));
  }

  @Test
  void testToHumanReadableSkipIntermediateUnit() {
    assertEquals("1h 5s",
        TimeUnitUtils.toHumanReadable(TimeUnit.HOURS.toSeconds(1) + 5, TimeUnit.SECONDS));
  }

  @Test
  void testToHumanReadableBelowMinuteBoundary() {
    assertEquals("59s",
        TimeUnitUtils.toHumanReadable(59, TimeUnit.SECONDS));
  }

  @Test
  void testToHumanReadableAboveMinuteBoundary() {
    assertEquals("1m 1s",
        TimeUnitUtils.toHumanReadable(61, TimeUnit.SECONDS));
  }

  @Test
  void testToHumanReadableBelowHourBoundary() {
    assertEquals("59m 59s",
        TimeUnitUtils.toHumanReadable(3599, TimeUnit.SECONDS));
  }

  @Test
  void testToHumanReadableSubMicrosBoundary() {
    assertEquals("1\u03BCs 1ns",
        TimeUnitUtils.toHumanReadable(1001, TimeUnit.NANOSECONDS));
  }

  @Test
  void testToHumanReadableBelowMicrosBoundary() {
    assertEquals("999ns",
        TimeUnitUtils.toHumanReadable(999, TimeUnit.NANOSECONDS));
  }

  @Test
  void testToHumanReadableFullUnitChain() {
    long nanos = TimeUnit.DAYS.toNanos(1)
        + TimeUnit.HOURS.toNanos(1)
        + TimeUnit.MINUTES.toNanos(1)
        + TimeUnit.SECONDS.toNanos(1)
        + TimeUnit.MILLISECONDS.toNanos(1)
        + TimeUnit.MICROSECONDS.toNanos(1)
        + 1;
    assertEquals("1d 1h 1m 1s 1ms 1\u03BCs 1ns",
        TimeUnitUtils.toHumanReadable(nanos, TimeUnit.NANOSECONDS));
  }

  // ==================================================================
  //  toHumanReadable(Duration)
  // ==================================================================

  @Test
  void testToHumanReadableDuration() {
    assertEquals("1h 2m 5s",
            TimeUnitUtils.toHumanReadable(Duration.ofMillis(3725000)));
  }

  @Test
  void testToHumanReadableDurationZero() {
    assertEquals("0ns", TimeUnitUtils.toHumanReadable(Duration.ZERO));
  }

  @Test
  void testToHumanReadableDurationNegative() {
    assertEquals("-1h 2m 5s",
            TimeUnitUtils.toHumanReadable(Duration.ofMillis(-3725000)));
  }

  @Test
  void testToHumanReadableDurationNull() {
    assertNull(TimeUnitUtils.toHumanReadable((Duration) null));
  }

  @Test
  void testToHumanReadableDurationSubMillis() {
    assertEquals("1ms 234\u03BCs 567ns",
            TimeUnitUtils.toHumanReadable(Duration.ofNanos(1234567)));
  }

  // ==================================================================
  //  parseDuration
  // ==================================================================

  @Test
  void testParseDurationComplex() {
    long nanos = TimeUnitUtils.parseDuration("1h 2m 5s");
    assertEquals(TimeUnit.HOURS.toNanos(1) + TimeUnit.MINUTES.toNanos(2) + TimeUnit.SECONDS.toNanos(5), nanos);
  }

  @Test
  void testParseDurationSingleUnit() {
    long nanos = TimeUnitUtils.parseDuration("5s");
    assertEquals(TimeUnit.SECONDS.toNanos(5), nanos);
  }

  @Test
  void testParseDurationAllUnits() {
    long nanos = TimeUnitUtils.parseDuration("1d 2h 3m 4s 500ms");
    assertEquals(
            TimeUnit.DAYS.toNanos(1) + TimeUnit.HOURS.toNanos(2) + TimeUnit.MINUTES.toNanos(3)
                    + TimeUnit.SECONDS.toNanos(4) + TimeUnit.MILLISECONDS.toNanos(500),
            nanos);
  }

  @Test
  void testParseDurationZero() {
    assertEquals(0, TimeUnitUtils.parseDuration("0s"));
  }

  @Test
  void testParseDurationNegativeComplex() {
    long nanos = TimeUnitUtils.parseDuration("-1h 2m 5s");
    assertEquals(-(TimeUnit.HOURS.toNanos(1) + TimeUnit.MINUTES.toNanos(2) + TimeUnit.SECONDS.toNanos(5)), nanos);
  }

  @Test
  void testParseDurationNegativeWithSpace() {
    long nanos = TimeUnitUtils.parseDuration("- 1h 2m 5s");
    assertEquals(-(TimeUnit.HOURS.toNanos(1) + TimeUnit.MINUTES.toNanos(2) + TimeUnit.SECONDS.toNanos(5)), nanos);
  }

  @Test
  void testParseDurationRoundTrip() {
    String human = TimeUnitUtils.toHumanReadable(90061000, TimeUnit.MILLISECONDS);
    long nanos = TimeUnitUtils.parseDuration(human);
    assertEquals(TimeUnit.MILLISECONDS.toNanos(90061000), nanos);
  }

  @Test
  void testParseDurationRoundTripWithNanos() {
    String human = TimeUnitUtils.toHumanReadable(1234567890, TimeUnit.NANOSECONDS);
    long nanos = TimeUnitUtils.parseDuration(human);
    assertEquals(1234567890, nanos);
  }

  @Test
  void testParseDurationNull() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration(null));
  }

  @Test
  void testParseDurationEmpty() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration(""));
  }

  @Test
  void testParseDurationBlank() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("   "));
  }

  @Test
  void testParseDurationUnknownUnit() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("5xyz"));
  }

  @Test
  void testParseDurationInvalidNumber() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("abcs"));
  }

  @Test
  void testParseDurationMissingUnit() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("5s 10"));
  }

  @Test
  void testParseDurationNoSpaceBetweenUnits() {
    long nanos = TimeUnitUtils.parseDuration("1h30m5s");
    assertEquals(TimeUnit.HOURS.toNanos(1) + TimeUnit.MINUTES.toNanos(30) + TimeUnit.SECONDS.toNanos(5), nanos);
  }

  @Test
  void testParseDurationMixedFormat() {
    long nanos = TimeUnitUtils.parseDuration("1h 30m5s 500ms");
    assertEquals(TimeUnit.HOURS.toNanos(1) + TimeUnit.MINUTES.toNanos(30)
            + TimeUnit.SECONDS.toNanos(5) + TimeUnit.MILLISECONDS.toNanos(500), nanos);
  }

  @Test
  void testParseDurationMicros() {
    long nanos = TimeUnitUtils.parseDuration("100\u03BCs");
    assertEquals(TimeUnit.MICROSECONDS.toNanos(100), nanos);
  }

  @Test
  void testParseDurationNanos() {
    long nanos = TimeUnitUtils.parseDuration("500ns");
    assertEquals(500, nanos);
  }

  @Test
  void testParseDurationMicrosAndNanos() {
    long nanos = TimeUnitUtils.parseDuration("1ms 234\u03BCs 567ns");
    assertEquals(1234567, nanos);
  }

  @Test
  void testParseDurationWithExtraWhitespace() {
    long nanos = TimeUnitUtils.parseDuration("  1h    2m  ");
    assertEquals(TimeUnit.HOURS.toNanos(1) + TimeUnit.MINUTES.toNanos(2), nanos);
  }

  @Test
  void testParseDurationSkipIntermediateUnit() {
    long nanos = TimeUnitUtils.parseDuration("1h 5s");
    assertEquals(TimeUnit.HOURS.toNanos(1) + TimeUnit.SECONDS.toNanos(5), nanos);
  }

  @Test
  void testParseDurationSkipIntermediateUnitNoSpace() {
    long nanos = TimeUnitUtils.parseDuration("1h5s");
    assertEquals(TimeUnit.HOURS.toNanos(1) + TimeUnit.SECONDS.toNanos(5), nanos);
  }

  @Test
  void testParseDurationOnlyDashThrows() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("-"));
  }

  @Test
  void testParseDurationNegativeZero() {
    assertEquals(0, TimeUnitUtils.parseDuration("-0s"));
  }

  @Test
  void testParseDurationCaseSensitiveUpperThrows() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("5H"));
  }

  @Test
  void testParseDurationRepeatedUnitThrows() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("1h 2h"));
  }

  @Test
  void testParseDurationNumberOnlyThrows() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("123"));
  }

  @Test
  void testParseDurationLettersOnlyThrows() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("abc"));
  }

  @Test
  void testParseDurationHugeNumberThrows() {
    assertThrows(IllegalArgumentException.class, () -> TimeUnitUtils.parseDuration("99999999999999999999ms"));
  }

  // ==================================================================
  //  parseDurationToMillis
  // ==================================================================

  @Test
  void testParseDurationToMillis() {
    long millis = TimeUnitUtils.parseDurationToMillis("1h 30m 5s 500ms");
    assertEquals(TimeUnit.HOURS.toMillis(1) + TimeUnit.MINUTES.toMillis(30)
            + TimeUnit.SECONDS.toMillis(5) + 500, millis);
  }

  @Test
  void testParseDurationToMillisNoSpace() {
    long millis = TimeUnitUtils.parseDurationToMillis("1h30m5s500ms");
    assertEquals(TimeUnit.HOURS.toMillis(1) + TimeUnit.MINUTES.toMillis(30)
            + TimeUnit.SECONDS.toMillis(5) + 500, millis);
  }

  @Test
  void testParseDurationToMillisRoundTrip() {
    String human = TimeUnitUtils.toHumanReadable(90061000, TimeUnit.MILLISECONDS);
    long millis = TimeUnitUtils.parseDurationToMillis(human);
    assertEquals(90061000, millis);
  }

  @Test
  void testParseDurationToMillisSubMilliTruncation() {
    long millis = TimeUnitUtils.parseDurationToMillis("1500\u03BCs");
    assertEquals(1, millis);
  }

  @Test
  void testParseDurationToMillisNegative() {
    long millis = TimeUnitUtils.parseDurationToMillis("-1h 2m 5s 500ms");
    assertEquals(-(TimeUnit.HOURS.toMillis(1) + TimeUnit.MINUTES.toMillis(2)
            + TimeUnit.SECONDS.toMillis(5) + 500), millis);
  }
}
