package cn.addenda.component.common.test.util.datetime;

import cn.addenda.component.common.util.datetime.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Date;

class DateUtilsPerformanceTest {

  private static final int WARMUP = 10_000;
  private static final int ITERS = 50_000;

  private static final long TS = 1686800000000L;
  private static final Date DATE = new Date(TS);
  private static final LocalDateTime LDT = LocalDateTime.of(2023, 6, 15, 14, 30, 45, 123_000_000);
  private static final LocalDate LD = LocalDate.of(2023, 6, 15);
  private static final LocalTime LT = LocalTime.of(14, 30, 45, 123_000_000);
  private static final ZoneId ZONE = ZoneId.of("+08:00");

  static {
    // pre-cache all built-in formatters
    DateUtils.format(LDT, DateUtils.yMdHmsS_FMT);
    DateUtils.format(LD, DateUtils.yMd_FMT);
    DateUtils.format(LT, DateUtils.HmsS_FMT);
    DateUtils.parseLdt("2023-06-15 14:30:45.123", DateUtils.yMdHmsS_FMT);
    DateUtils.parseLd("2023-06-15", DateUtils.yMd_FMT);
    DateUtils.parseLt("14:30:45.123", DateUtils.HmsS_FMT);
  }

  // ==================================================================
  //  Date ↔ LocalDateTime
  // ==================================================================

  @Test
  void benchmarkDateToLocalDateTime() {
    warmup(() -> DateUtils.dateToLocalDateTime(DATE, ZONE));

    Object[][] cases = {
            {"default-zone", (Runnable) () -> DateUtils.dateToLocalDateTime(DATE)},
            {"+08:00", (Runnable) () -> DateUtils.dateToLocalDateTime(DATE, ZONE)},
            {"null→default", (Runnable) () -> DateUtils.dateToLocalDateTime(DATE, null)},
            {"null-input", (Runnable) () -> DateUtils.dateToLocalDateTime(null)},
    };

    System.out.println("========== dateToLocalDateTime (ns/call) ==========");
    for (Object[] c : cases) benchmark((String) c[0], (Runnable) c[1]);
  }

  @Test
  void benchmarkLocalDateTimeToTimestamp() {
    warmup(() -> DateUtils.localDateTimeToTimestamp(LDT, ZONE));

    Object[][] cases = {
            {"default-zone", (Runnable) () -> DateUtils.localDateTimeToTimestamp(LDT)},
            {"+08:00", (Runnable) () -> DateUtils.localDateTimeToTimestamp(LDT, ZONE)},
            {"null→default", (Runnable) () -> DateUtils.localDateTimeToTimestamp(LDT, null)},
            {"null-input", (Runnable) () -> DateUtils.localDateTimeToTimestamp(null)},
    };

    System.out.println("========== localDateTimeToTimestamp (ns/call) ==========");
    for (Object[] c : cases) benchmark((String) c[0], (Runnable) c[1]);
  }

  @Test
  void benchmarkTimestampToLocalDateTime() {
    warmup(() -> DateUtils.timestampToLocalDateTime(TS, ZONE));

    Object[][] cases = {
            {"default-zone", (Runnable) () -> DateUtils.timestampToLocalDateTime(TS)},
            {"+08:00", (Runnable) () -> DateUtils.timestampToLocalDateTime(TS, ZONE)},
            {"null→default", (Runnable) () -> DateUtils.timestampToLocalDateTime(TS, null)},
            {"null-input", (Runnable) () -> DateUtils.timestampToLocalDateTime(null)},
    };

    System.out.println("========== timestampToLocalDateTime (ns/call) ==========");
    for (Object[] c : cases) benchmark((String) c[0], (Runnable) c[1]);
  }

  @Test
  void benchmarkLocalDateTimeToDate() {
    warmup(() -> DateUtils.localDateTimeToDate(LDT, ZONE));

    Object[][] cases = {
            {"default-zone", (Runnable) () -> DateUtils.localDateTimeToDate(LDT)},
            {"+08:00", (Runnable) () -> DateUtils.localDateTimeToDate(LDT, ZONE)},
            {"null→default", (Runnable) () -> DateUtils.localDateTimeToDate(LDT, null)},
            {"null-input", (Runnable) () -> DateUtils.localDateTimeToDate(null)},
    };

    System.out.println("========== localDateTimeToDate (ns/call) ==========");
    for (Object[] c : cases) benchmark((String) c[0], (Runnable) c[1]);
  }

  @Test
  void benchmarkLocalDateToDate() {
    warmup(() -> DateUtils.localDateToDate(LD, ZONE));

    Object[][] cases = {
            {"default-zone", (Runnable) () -> DateUtils.localDateToDate(LD)},
            {"+08:00", (Runnable) () -> DateUtils.localDateToDate(LD, ZONE)},
            {"null-input", (Runnable) () -> DateUtils.localDateToDate(null)},
    };

    System.out.println("========== localDateToDate (ns/call) ==========");
    for (Object[] c : cases) benchmark((String) c[0], (Runnable) c[1]);
  }

  @Test
  void benchmarkLocalTimeToDate() {
    warmup(() -> DateUtils.localTimeToDate(LT, ZONE));

    Object[][] cases = {
            {"default-zone", (Runnable) () -> DateUtils.localTimeToDate(LT)},
            {"+08:00", (Runnable) () -> DateUtils.localTimeToDate(LT, ZONE)},
            {"null-input", (Runnable) () -> DateUtils.localTimeToDate(null)},
    };

    System.out.println("========== localTimeToDate (ns/call) ==========");
    for (Object[] c : cases) benchmark((String) c[0], (Runnable) c[1]);
  }

  // ==================================================================
  //  format（已预热缓存） / parse
  // ==================================================================

  @Test
  void benchmarkFormatCached() {
    warmup(() -> DateUtils.format(LDT, DateUtils.yMdHmsS_FMT));

    System.out.println("========== format cached (ns/call) ==========");
    benchmark("LDT yMdHmsS", () -> DateUtils.format(LDT, DateUtils.yMdHmsS_FMT));
    benchmark("LDT yMdHms", () -> DateUtils.format(LDT, DateUtils.yMdHms_FMT));
    benchmark("LDT yMdHm", () -> DateUtils.format(LDT, DateUtils.yMdHm_FMT));
    benchmark("LDT yMd", () -> DateUtils.format(LDT, DateUtils.yMd_FMT));
    benchmark("LD  yMd", () -> DateUtils.format(LD, DateUtils.yMd_FMT));
    benchmark("LT  HmsS", () -> DateUtils.format(LT, DateUtils.HmsS_FMT));
    benchmark("LT  Hms", () -> DateUtils.format(LT, DateUtils.Hms_FMT));
    benchmark("LT  Hm", () -> DateUtils.format(LT, DateUtils.Hm_FMT));
    benchmark("LDT compact", () -> DateUtils.format(LDT, DateUtils.yMdHms_C_FMT));
    benchmark("LDT slash", () -> DateUtils.format(LDT, DateUtils.yMdHms_S_FMT));
    benchmark("LDT iso", () -> DateUtils.format(LDT, DateUtils.yMdHms_T_FMT));
  }

  @Test
  void benchmarkFormatCustom() {
    String pattern = "yyyy/MM/dd HH:mm:ss.SSS";
    warmup(() -> DateUtils.format(LDT, pattern));

    System.out.println("========== format custom (ns/call) ==========");
    benchmark("custom cached", () -> DateUtils.format(LDT, pattern));

    // cold: each call a new unique pattern, always computeIfAbsent miss
    String base = "yyyy-MM-";
    System.out.println("  --- cold path (new pattern every call) ---");
    long t = System.nanoTime();
    for (int i = 0; i < ITERS; i++) {
      DateUtils.format(LDT, base + (i % 12 + 1) + "-dd HH:mm");
    }
    long elapsed = System.nanoTime() - t;
    print("cold-unique", elapsed, ITERS);
  }

  @Test
  void benchmarkParseCached() {
    String strLdt = "2023-06-15 14:30:45.123";
    String strLd = "2023-06-15";
    String strLt = "14:30:45.123";

    warmup(() -> DateUtils.parseLdt(strLdt, DateUtils.yMdHmsS_FMT));

    System.out.println("========== parse cached (ns/call) ==========");
    benchmark("LDT yMdHmsS", () -> DateUtils.parseLdt(strLdt, DateUtils.yMdHmsS_FMT));
    benchmark("LDT yMdHms", () -> DateUtils.parseLdt("2023-06-15 14:30:45", DateUtils.yMdHms_FMT));
    benchmark("LD  yMd", () -> DateUtils.parseLd(strLd, DateUtils.yMd_FMT));
    benchmark("LT  HmsS", () -> DateUtils.parseLt(strLt, DateUtils.HmsS_FMT));
    benchmark("LDT compact", () -> DateUtils.parseLdt("20230615143045", DateUtils.yMdHms_C_FMT));
    benchmark("LDT slash", () -> DateUtils.parseLdt("2023/06/15 14:30:45", DateUtils.yMdHms_S_FMT));
    benchmark("LDT iso", () -> DateUtils.parseLdt("2023-06-15T14:30:45", DateUtils.yMdHms_T_FMT));
  }

  // ==================================================================
  //  roundtrip
  // ==================================================================

  @Test
  void benchmarkRoundtrip() {
    warmup(() -> {
      DateUtils.format(LDT, DateUtils.yMdHmsS_FMT);
      DateUtils.parseLdt("2023-06-15 14:30:45.123", DateUtils.yMdHmsS_FMT);
    });

    System.out.println("========== roundtrip format+parse (ns/call) ==========");
    benchmark("compound yMdHmsS", () -> {
      String s = DateUtils.format(LDT, DateUtils.yMdHmsS_FMT);
      DateUtils.parseLdt(s, DateUtils.yMdHmsS_FMT);
    });
    benchmark("compact yMdHms", () -> {
      String s = DateUtils.format(LDT, DateUtils.yMdHms_C_FMT);
      DateUtils.parseLdt(s, DateUtils.yMdHms_C_FMT);
    });
    benchmark("iso yMdHmsS", () -> {
      String s = DateUtils.format(LDT, DateUtils.yMdHmsS_T_FMT);
      DateUtils.parseLdt(s, DateUtils.yMdHmsS_T_FMT);
    });
  }

  // ==================================================================
  //  helpers
  // ==================================================================

  private static void warmup(Runnable task) {
    for (int i = 0; i < WARMUP; i++) {
      task.run();
    }
  }

  private static void benchmark(String label, Runnable task) {
    long t = System.nanoTime();
    for (int i = 0; i < ITERS; i++) {
      task.run();
    }
    long elapsed = System.nanoTime() - t;
    print(label, elapsed, ITERS);
  }

  private static void print(String label, long nanos, int iters) {
    System.out.printf("  %-22s %8.0f ns/op  (%6.2f ms total)%n",
            label, (double) nanos / iters, nanos / 1_000_000.0);
  }
}
