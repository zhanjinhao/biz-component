package cn.addenda.component.common.test.util.datetime;

import cn.addenda.component.common.util.datetime.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Date;

class DateUtilsTest {

  // ==================================================================
  //  Data: Date ↔ LocalDateTime conversions
  // ==================================================================

  private static final long TIMESTAMP = 1686800000000L;
  private static final Date DATE = new Date(TIMESTAMP);
  private static final ZoneId ZONE_UTC = ZoneId.of("+00:00");
  private static final ZoneId ZONE_E8 = ZoneId.of("+08:00");

  // ==================================================================
  //  Data: 2023-06-15 14:30:45.123  family
  // ==================================================================

  private static final LocalDateTime LDT_MS = LocalDateTime.of(2023, 6, 15, 14, 30, 45, 123_000_000);
  private static final LocalDateTime LDT_S = LocalDateTime.of(2023, 6, 15, 14, 30, 45);
  private static final LocalDateTime LDT_M = LocalDateTime.of(2023, 6, 15, 14, 30);
  private static final LocalDateTime LDT_H = LocalDateTime.of(2023, 6, 15, 14, 0);
  private static final LocalDate LD = LocalDate.of(2023, 6, 15);
  private static final LocalTime LT_MS = LocalTime.of(14, 30, 45, 123_000_000);
  private static final LocalTime LT_S = LocalTime.of(14, 30, 45);
  private static final LocalTime LT_M = LocalTime.of(14, 30);

  // formatted strings — Single
  private static final String S_y = "2023";
  private static final String S_M = "06";
  private static final String S_d = "15";
  private static final String S_H = "14";
  private static final String S_m = "30";
  private static final String S_s = "45";
  private static final String S_S = "123";

  // formatted strings — Compound
  private static final String S_yMdHmsS = "2023-06-15 14:30:45.123";
  private static final String S_yMdHms = "2023-06-15 14:30:45";
  private static final String S_yMdHm = "2023-06-15 14:30";
  private static final String S_yMdH = "2023-06-15 14";
  private static final String S_yMd = "2023-06-15";
  private static final String S_yM = "2023-06";
  private static final String S_MdHmsS = "06-15 14:30:45.123";
  private static final String S_MdHms = "06-15 14:30:45";
  private static final String S_MdHm = "06-15 14:30";
  private static final String S_MdH = "06-15 14";
  private static final String S_Md = "06-15";
  private static final String S_dHmsS = "15 14:30:45.123";
  private static final String S_dHms = "15 14:30:45";
  private static final String S_dHm = "15 14:30";
  private static final String S_dH = "15 14";
  private static final String S_HmsS = "14:30:45.123";
  private static final String S_Hms = "14:30:45";
  private static final String S_Hm = "14:30";
  private static final String S_msS = "30:45.123";
  private static final String S_sS = "45.123";
  private static final String S_ms = "30:45";

  // formatted strings — Compact
  private static final String S_C_yMdHmsS = "20230615143045123";
  private static final String S_C_yMdHms = "20230615143045";
  private static final String S_C_yMdHm = "202306151430";
  private static final String S_C_yMdH = "2023061514";
  private static final String S_C_yMd = "20230615";
  private static final String S_C_yM = "202306";
  private static final String S_C_MdHmsS = "0615143045123";
  private static final String S_C_MdHms = "0615143045";
  private static final String S_C_MdHm = "06151430";
  private static final String S_C_MdH = "061514";
  private static final String S_C_Md = "0615";
  private static final String S_C_dHmsS = "15143045123";
  private static final String S_C_dHms = "15143045";
  private static final String S_C_dHm = "151430";
  private static final String S_C_dH = "1514";
  private static final String S_C_HmsS = "143045123";
  private static final String S_C_Hms = "143045";
  private static final String S_C_Hm = "1430";
  private static final String S_C_msS = "3045123";
  private static final String S_C_ms = "3045";
  private static final String S_C_sS = "45123";

  // formatted strings — Slash
  private static final String S_S_yMdHmsS = "2023/06/15 14:30:45.123";
  private static final String S_S_yMdHms = "2023/06/15 14:30:45";
  private static final String S_S_yMdHm = "2023/06/15 14:30";
  private static final String S_S_yMdH = "2023/06/15 14";
  private static final String S_S_yMd = "2023/06/15";
  private static final String S_S_yM = "2023/06";
  private static final String S_S_MdHmsS = "06/15 14:30:45.123";
  private static final String S_S_MdHms = "06/15 14:30:45";
  private static final String S_S_MdHm = "06/15 14:30";
  private static final String S_S_MdH = "06/15 14";
  private static final String S_S_Md = "06/15";

  // formatted strings — ISO 8601
  private static final String S_T_yMdHmsS = "2023-06-15T14:30:45.123";
  private static final String S_T_yMdHms = "2023-06-15T14:30:45";
  private static final String S_T_yMdHm = "2023-06-15T14:30";
  private static final String S_T_yMdH = "2023-06-15T14";
  private static final String S_T_MdHmsS = "06-15T14:30:45.123";
  private static final String S_T_MdHms = "06-15T14:30:45";
  private static final String S_T_MdHm = "06-15T14:30";
  private static final String S_T_MdH = "06-15T14";
  private static final String S_T_dHmsS = "15T14:30:45.123";
  private static final String S_T_dHms = "15T14:30:45";
  private static final String S_T_dHm = "15T14:30";
  private static final String S_T_dH = "15T14";

  // ==================================================================
  //  常数
  // ==================================================================

  @Test
  void testConstants() {
    // Single
    Assertions.assertNotNull(DateUtils.y_FMT);
    Assertions.assertNotNull(DateUtils.M_FMT);
    Assertions.assertNotNull(DateUtils.d_FMT);
    Assertions.assertNotNull(DateUtils.H_FMT);
    Assertions.assertNotNull(DateUtils.m_FMT);
    Assertions.assertNotNull(DateUtils.s_FMT);
    Assertions.assertNotNull(DateUtils.S_FMT);

    // Compound
    Assertions.assertNotNull(DateUtils.yMdHmsS_FMT);
    Assertions.assertNotNull(DateUtils.yMdHms_FMT);
    Assertions.assertNotNull(DateUtils.yMdHm_FMT);
    Assertions.assertNotNull(DateUtils.yMdH_FMT);
    Assertions.assertNotNull(DateUtils.yMd_FMT);
    Assertions.assertNotNull(DateUtils.yM_FMT);
    Assertions.assertNotNull(DateUtils.MdHmsS_FMT);
    Assertions.assertNotNull(DateUtils.MdHms_FMT);
    Assertions.assertNotNull(DateUtils.MdHm_FMT);
    Assertions.assertNotNull(DateUtils.MdH_FMT);
    Assertions.assertNotNull(DateUtils.Md_FMT);
    Assertions.assertNotNull(DateUtils.dHmsS_FMT);
    Assertions.assertNotNull(DateUtils.dHms_FMT);
    Assertions.assertNotNull(DateUtils.dHm_FMT);
    Assertions.assertNotNull(DateUtils.dH_FMT);
    Assertions.assertNotNull(DateUtils.HmsS_FMT);
    Assertions.assertNotNull(DateUtils.Hms_FMT);
    Assertions.assertNotNull(DateUtils.Hm_FMT);
    Assertions.assertNotNull(DateUtils.msS_FMT);
    Assertions.assertNotNull(DateUtils.ms_FMT);
    Assertions.assertNotNull(DateUtils.sS_FMT);

    // Compact
    Assertions.assertNotNull(DateUtils.yMdHmsS_C_FMT);
    Assertions.assertNotNull(DateUtils.yMdHms_C_FMT);
    Assertions.assertNotNull(DateUtils.yMdHm_C_FMT);
    Assertions.assertNotNull(DateUtils.yMdH_C_FMT);
    Assertions.assertNotNull(DateUtils.yMd_C_FMT);
    Assertions.assertNotNull(DateUtils.yM_C_FMT);
    Assertions.assertNotNull(DateUtils.MdHmsS_C_FMT);
    Assertions.assertNotNull(DateUtils.MdHms_C_FMT);
    Assertions.assertNotNull(DateUtils.MdHm_C_FMT);
    Assertions.assertNotNull(DateUtils.MdH_C_FMT);
    Assertions.assertNotNull(DateUtils.Md_C_FMT);
    Assertions.assertNotNull(DateUtils.dHmsS_C_FMT);
    Assertions.assertNotNull(DateUtils.dHms_C_FMT);
    Assertions.assertNotNull(DateUtils.dHm_C_FMT);
    Assertions.assertNotNull(DateUtils.dH_C_FMT);
    Assertions.assertNotNull(DateUtils.HmsS_C_FMT);
    Assertions.assertNotNull(DateUtils.Hms_C_FMT);
    Assertions.assertNotNull(DateUtils.Hm_C_FMT);
    Assertions.assertNotNull(DateUtils.msS_C_FMT);
    Assertions.assertNotNull(DateUtils.ms_C_FMT);
    Assertions.assertNotNull(DateUtils.sS_C_FMT);

    // Slash
    Assertions.assertNotNull(DateUtils.yMdHmsS_S_FMT);
    Assertions.assertNotNull(DateUtils.yMdHms_S_FMT);
    Assertions.assertNotNull(DateUtils.yMdHm_S_FMT);
    Assertions.assertNotNull(DateUtils.yMdH_S_FMT);
    Assertions.assertNotNull(DateUtils.yMd_S_FMT);
    Assertions.assertNotNull(DateUtils.yM_S_FMT);
    Assertions.assertNotNull(DateUtils.MdHmsS_S_FMT);
    Assertions.assertNotNull(DateUtils.MdHms_S_FMT);
    Assertions.assertNotNull(DateUtils.MdHm_S_FMT);
    Assertions.assertNotNull(DateUtils.MdH_S_FMT);
    Assertions.assertNotNull(DateUtils.Md_S_FMT);

    // ISO 8601
    Assertions.assertNotNull(DateUtils.yMdHmsS_T_FMT);
    Assertions.assertNotNull(DateUtils.yMdHms_T_FMT);
    Assertions.assertNotNull(DateUtils.yMdHm_T_FMT);
    Assertions.assertNotNull(DateUtils.yMdH_T_FMT);
    Assertions.assertNotNull(DateUtils.MdHmsS_T_FMT);
    Assertions.assertNotNull(DateUtils.MdHms_T_FMT);
    Assertions.assertNotNull(DateUtils.MdHm_T_FMT);
    Assertions.assertNotNull(DateUtils.MdH_T_FMT);
    Assertions.assertNotNull(DateUtils.dHmsS_T_FMT);
    Assertions.assertNotNull(DateUtils.dHms_T_FMT);
    Assertions.assertNotNull(DateUtils.dHm_T_FMT);
    Assertions.assertNotNull(DateUtils.dH_T_FMT);
  }

  // ==================================================================
  //  format
  // ==================================================================

  @Test
  void format_Single() {
    Assertions.assertEquals(S_y, DateUtils.format(LDT_MS, DateUtils.y_FMT));
    Assertions.assertEquals(S_M, DateUtils.format(LDT_MS, DateUtils.M_FMT));
    Assertions.assertEquals(S_d, DateUtils.format(LDT_MS, DateUtils.d_FMT));
    Assertions.assertEquals(S_H, DateUtils.format(LDT_MS, DateUtils.H_FMT));
    Assertions.assertEquals(S_m, DateUtils.format(LDT_MS, DateUtils.m_FMT));
    Assertions.assertEquals(S_s, DateUtils.format(LDT_MS, DateUtils.s_FMT));
    Assertions.assertEquals(S_S, DateUtils.format(LDT_MS, DateUtils.S_FMT));
  }

  // --- Compound ---

  @Test
  void format_Compound_LDT() {
    Assertions.assertEquals(S_yMdHmsS, DateUtils.format(LDT_MS, DateUtils.yMdHmsS_FMT));
    Assertions.assertEquals(S_yMdHms, DateUtils.format(LDT_MS, DateUtils.yMdHms_FMT));
    Assertions.assertEquals(S_yMdHm, DateUtils.format(LDT_MS, DateUtils.yMdHm_FMT));
    Assertions.assertEquals(S_yMdH, DateUtils.format(LDT_MS, DateUtils.yMdH_FMT));
    Assertions.assertEquals(S_yMd, DateUtils.format(LDT_MS, DateUtils.yMd_FMT));
    Assertions.assertEquals(S_yM, DateUtils.format(LDT_MS, DateUtils.yM_FMT));
    Assertions.assertEquals(S_y, DateUtils.format(LDT_MS, DateUtils.y_FMT));
    Assertions.assertEquals(S_MdHmsS, DateUtils.format(LDT_MS, DateUtils.MdHmsS_FMT));
    Assertions.assertEquals(S_MdHms, DateUtils.format(LDT_MS, DateUtils.MdHms_FMT));
    Assertions.assertEquals(S_MdHm, DateUtils.format(LDT_MS, DateUtils.MdHm_FMT));
    Assertions.assertEquals(S_MdH, DateUtils.format(LDT_MS, DateUtils.MdH_FMT));
    Assertions.assertEquals(S_Md, DateUtils.format(LDT_MS, DateUtils.Md_FMT));
    Assertions.assertEquals(S_dHmsS, DateUtils.format(LDT_MS, DateUtils.dHmsS_FMT));
    Assertions.assertEquals(S_dHms, DateUtils.format(LDT_MS, DateUtils.dHms_FMT));
    Assertions.assertEquals(S_dHm, DateUtils.format(LDT_MS, DateUtils.dHm_FMT));
    Assertions.assertEquals(S_dH, DateUtils.format(LDT_MS, DateUtils.dH_FMT));
    Assertions.assertEquals(S_HmsS, DateUtils.format(LDT_MS, DateUtils.HmsS_FMT));
    Assertions.assertEquals(S_Hms, DateUtils.format(LDT_MS, DateUtils.Hms_FMT));
    Assertions.assertEquals(S_Hm, DateUtils.format(LDT_MS, DateUtils.Hm_FMT));
    Assertions.assertEquals(S_H, DateUtils.format(LDT_MS, DateUtils.H_FMT));
  }

  @Test
  void format_Compound_LD() {
    Assertions.assertEquals(S_yMd, DateUtils.format(LD, DateUtils.yMd_FMT));
    Assertions.assertEquals(S_yM, DateUtils.format(LD, DateUtils.yM_FMT));
    Assertions.assertEquals(S_y, DateUtils.format(LD, DateUtils.y_FMT));
    Assertions.assertEquals(S_Md, DateUtils.format(LD, DateUtils.Md_FMT));
  }

  @Test
  void format_Compound_LT() {
    Assertions.assertEquals(S_HmsS, DateUtils.format(LT_MS, DateUtils.HmsS_FMT));
    Assertions.assertEquals(S_msS, DateUtils.format(LT_MS, DateUtils.msS_FMT));
    Assertions.assertEquals(S_sS, DateUtils.format(LT_MS, DateUtils.sS_FMT));
    Assertions.assertEquals(S_Hms, DateUtils.format(LT_MS, DateUtils.Hms_FMT));
    Assertions.assertEquals(S_ms, DateUtils.format(LT_MS, DateUtils.ms_FMT));
    Assertions.assertEquals(S_Hm, DateUtils.format(LT_MS, DateUtils.Hm_FMT));
    Assertions.assertEquals(S_m, DateUtils.format(LT_MS, DateUtils.m_FMT));
    Assertions.assertEquals(S_H, DateUtils.format(LT_MS, DateUtils.H_FMT));
  }

  // --- Compact ---

  @Test
  void format_Compact_LDT() {
    Assertions.assertEquals(S_C_yMdHmsS, DateUtils.format(LDT_MS, DateUtils.yMdHmsS_C_FMT));
    Assertions.assertEquals(S_C_yMdHms, DateUtils.format(LDT_MS, DateUtils.yMdHms_C_FMT));
    Assertions.assertEquals(S_C_yMdHm, DateUtils.format(LDT_MS, DateUtils.yMdHm_C_FMT));
    Assertions.assertEquals(S_C_yMdH, DateUtils.format(LDT_MS, DateUtils.yMdH_C_FMT));
    Assertions.assertEquals(S_C_yMd, DateUtils.format(LDT_MS, DateUtils.yMd_C_FMT));
    Assertions.assertEquals(S_C_yM, DateUtils.format(LDT_MS, DateUtils.yM_C_FMT));
    Assertions.assertEquals(S_C_MdHmsS, DateUtils.format(LDT_MS, DateUtils.MdHmsS_C_FMT));
    Assertions.assertEquals(S_C_MdHms, DateUtils.format(LDT_MS, DateUtils.MdHms_C_FMT));
    Assertions.assertEquals(S_C_MdHm, DateUtils.format(LDT_MS, DateUtils.MdHm_C_FMT));
    Assertions.assertEquals(S_C_MdH, DateUtils.format(LDT_MS, DateUtils.MdH_C_FMT));
    Assertions.assertEquals(S_C_Md, DateUtils.format(LDT_MS, DateUtils.Md_C_FMT));
    Assertions.assertEquals(S_C_dHmsS, DateUtils.format(LDT_MS, DateUtils.dHmsS_C_FMT));
    Assertions.assertEquals(S_C_dHms, DateUtils.format(LDT_MS, DateUtils.dHms_C_FMT));
    Assertions.assertEquals(S_C_dHm, DateUtils.format(LDT_MS, DateUtils.dHm_C_FMT));
    Assertions.assertEquals(S_C_dH, DateUtils.format(LDT_MS, DateUtils.dH_C_FMT));
    Assertions.assertEquals(S_C_HmsS, DateUtils.format(LDT_MS, DateUtils.HmsS_C_FMT));
    Assertions.assertEquals(S_C_Hms, DateUtils.format(LDT_MS, DateUtils.Hms_C_FMT));
    Assertions.assertEquals(S_C_Hm, DateUtils.format(LDT_MS, DateUtils.Hm_C_FMT));
  }

  @Test
  void format_Compact_LD() {
    Assertions.assertEquals(S_C_yMd, DateUtils.format(LD, DateUtils.yMd_C_FMT));
    Assertions.assertEquals(S_C_yM, DateUtils.format(LD, DateUtils.yM_C_FMT));
    Assertions.assertEquals(S_C_Md, DateUtils.format(LD, DateUtils.Md_C_FMT));
  }

  @Test
  void format_Compact_LT() {
    Assertions.assertEquals(S_C_HmsS, DateUtils.format(LT_MS, DateUtils.HmsS_C_FMT));
    Assertions.assertEquals(S_C_Hms, DateUtils.format(LT_MS, DateUtils.Hms_C_FMT));
    Assertions.assertEquals(S_C_Hm, DateUtils.format(LT_MS, DateUtils.Hm_C_FMT));
    Assertions.assertEquals(S_C_msS, DateUtils.format(LT_MS, DateUtils.msS_C_FMT));
    Assertions.assertEquals(S_C_ms, DateUtils.format(LT_MS, DateUtils.ms_C_FMT));
    Assertions.assertEquals(S_C_sS, DateUtils.format(LT_MS, DateUtils.sS_C_FMT));
  }

  // --- Slash ---

  @Test
  void format_Slash_LDT() {
    Assertions.assertEquals(S_S_yMdHmsS, DateUtils.format(LDT_MS, DateUtils.yMdHmsS_S_FMT));
    Assertions.assertEquals(S_S_yMdHms, DateUtils.format(LDT_MS, DateUtils.yMdHms_S_FMT));
    Assertions.assertEquals(S_S_yMdHm, DateUtils.format(LDT_MS, DateUtils.yMdHm_S_FMT));
    Assertions.assertEquals(S_S_yMdH, DateUtils.format(LDT_MS, DateUtils.yMdH_S_FMT));
    Assertions.assertEquals(S_S_yMd, DateUtils.format(LDT_MS, DateUtils.yMd_S_FMT));
    Assertions.assertEquals(S_S_yM, DateUtils.format(LDT_MS, DateUtils.yM_S_FMT));
    Assertions.assertEquals(S_S_MdHmsS, DateUtils.format(LDT_MS, DateUtils.MdHmsS_S_FMT));
    Assertions.assertEquals(S_S_MdHms, DateUtils.format(LDT_MS, DateUtils.MdHms_S_FMT));
    Assertions.assertEquals(S_S_MdHm, DateUtils.format(LDT_MS, DateUtils.MdHm_S_FMT));
    Assertions.assertEquals(S_S_MdH, DateUtils.format(LDT_MS, DateUtils.MdH_S_FMT));
    Assertions.assertEquals(S_S_Md, DateUtils.format(LDT_MS, DateUtils.Md_S_FMT));
  }

  @Test
  void format_Slash_LD() {
    Assertions.assertEquals(S_S_yMd, DateUtils.format(LD, DateUtils.yMd_S_FMT));
    Assertions.assertEquals(S_S_yM, DateUtils.format(LD, DateUtils.yM_S_FMT));
    Assertions.assertEquals(S_S_Md, DateUtils.format(LD, DateUtils.Md_S_FMT));
  }

  // --- ISO 8601 ---

  @Test
  void format_ISO_LDT() {
    Assertions.assertEquals(S_T_yMdHmsS, DateUtils.format(LDT_MS, DateUtils.yMdHmsS_T_FMT));
    Assertions.assertEquals(S_T_yMdHms, DateUtils.format(LDT_MS, DateUtils.yMdHms_T_FMT));
    Assertions.assertEquals(S_T_yMdHm, DateUtils.format(LDT_MS, DateUtils.yMdHm_T_FMT));
    Assertions.assertEquals(S_T_yMdH, DateUtils.format(LDT_MS, DateUtils.yMdH_T_FMT));
    Assertions.assertEquals(S_T_MdHmsS, DateUtils.format(LDT_MS, DateUtils.MdHmsS_T_FMT));
    Assertions.assertEquals(S_T_MdHms, DateUtils.format(LDT_MS, DateUtils.MdHms_T_FMT));
    Assertions.assertEquals(S_T_MdHm, DateUtils.format(LDT_MS, DateUtils.MdHm_T_FMT));
    Assertions.assertEquals(S_T_MdH, DateUtils.format(LDT_MS, DateUtils.MdH_T_FMT));
    Assertions.assertEquals(S_T_dHmsS, DateUtils.format(LDT_MS, DateUtils.dHmsS_T_FMT));
    Assertions.assertEquals(S_T_dHms, DateUtils.format(LDT_MS, DateUtils.dHms_T_FMT));
    Assertions.assertEquals(S_T_dHm, DateUtils.format(LDT_MS, DateUtils.dHm_T_FMT));
    Assertions.assertEquals(S_T_dH, DateUtils.format(LDT_MS, DateUtils.dH_T_FMT));
  }

  // --- null ---

  @Test
  void format_null() {
    Assertions.assertNull(DateUtils.format((LocalDateTime) null, DateUtils.yMdHms_FMT));
    Assertions.assertNull(DateUtils.format((LocalDate) null, DateUtils.yMd_FMT));
    Assertions.assertNull(DateUtils.format((LocalTime) null, DateUtils.Hms_FMT));
  }

  // --- custom ---

  @Test
  void format_custom() {
    Assertions.assertEquals("15/06/2023", DateUtils.format(LDT_MS, "dd/MM/yyyy"));
    Assertions.assertEquals(S_C_yMd, DateUtils.format(LDT_MS, "yyyyMMdd"));
  }

  // ==================================================================
  //  parse
  // ==================================================================

  // --- Compound ---

  @Test
  void parse_Compound_LDT() {
    Assertions.assertEquals(LDT_MS, DateUtils.parseLdt(S_yMdHmsS, DateUtils.yMdHmsS_FMT));
    Assertions.assertEquals(LDT_S, DateUtils.parseLdt(S_yMdHms, DateUtils.yMdHms_FMT));
    Assertions.assertEquals(LDT_M, DateUtils.parseLdt(S_yMdHm, DateUtils.yMdHm_FMT));
  }

  @Test
  void parse_Compound_LD() {
    Assertions.assertEquals(LD, DateUtils.parseLd(S_yMd, DateUtils.yMd_FMT));
  }

  @Test
  void parse_Compound_LT() {
    Assertions.assertEquals(LT_MS, DateUtils.parseLt(S_HmsS, DateUtils.HmsS_FMT));
    Assertions.assertEquals(LT_S, DateUtils.parseLt(S_Hms, DateUtils.Hms_FMT));
    Assertions.assertEquals(LT_M, DateUtils.parseLt(S_Hm, DateUtils.Hm_FMT));
  }

  // --- Compact ---

  @Test
  void parse_Compact_LDT() {
    Assertions.assertEquals(LDT_S, DateUtils.parseLdt(S_C_yMdHms, DateUtils.yMdHms_C_FMT));
    Assertions.assertEquals(LDT_M, DateUtils.parseLdt(S_C_yMdHm, DateUtils.yMdHm_C_FMT));
  }

  @Test
  void parse_Compact_LD() {
    Assertions.assertEquals(LD, DateUtils.parseLd(S_C_yMd, DateUtils.yMd_C_FMT));
  }

  @Test
  void parse_Compact_LT() {
    Assertions.assertEquals(LT_S, DateUtils.parseLt(S_C_Hms, DateUtils.Hms_C_FMT));
    Assertions.assertEquals(LT_M, DateUtils.parseLt(S_C_Hm, DateUtils.Hm_C_FMT));
    Assertions.assertEquals(LT_MS, DateUtils.parseLt(S_C_HmsS, DateUtils.HmsS_C_FMT));
  }

  // --- Slash ---

  @Test
  void parse_Slash_LDT() {
    Assertions.assertEquals(LDT_MS, DateUtils.parseLdt(S_S_yMdHmsS, DateUtils.yMdHmsS_S_FMT));
    Assertions.assertEquals(LDT_S, DateUtils.parseLdt(S_S_yMdHms, DateUtils.yMdHms_S_FMT));
    Assertions.assertEquals(LDT_M, DateUtils.parseLdt(S_S_yMdHm, DateUtils.yMdHm_S_FMT));
  }

  @Test
  void parse_Slash_LD() {
    Assertions.assertEquals(LD, DateUtils.parseLd(S_S_yMd, DateUtils.yMd_S_FMT));
  }

  // --- ISO 8601 ---

  @Test
  void parse_ISO_LDT() {
    Assertions.assertEquals(LDT_MS, DateUtils.parseLdt(S_T_yMdHmsS, DateUtils.yMdHmsS_T_FMT));
    Assertions.assertEquals(LDT_S, DateUtils.parseLdt(S_T_yMdHms, DateUtils.yMdHms_T_FMT));
    Assertions.assertEquals(LDT_M, DateUtils.parseLdt(S_T_yMdHm, DateUtils.yMdHm_T_FMT));
  }

  // --- null ---

  @Test
  void parse_null() {
    Assertions.assertNull(DateUtils.parseLdt(null, DateUtils.yMdHms_FMT));
    Assertions.assertNull(DateUtils.parseLd(null, DateUtils.yMd_FMT));
    Assertions.assertNull(DateUtils.parseLt(null, DateUtils.Hms_FMT));
  }

  // --- invalid / edge ---

  @Test
  void parse_invalidFormat() {
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLdt("15-06-2023", DateUtils.yMd_FMT));
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLd("15/06/2023", DateUtils.yMd_FMT));
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLt("14-30-45", DateUtils.Hms_FMT));
  }

  @Test
  void parse_incompleteComponent() {
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLd(S_yM, DateUtils.yM_FMT));
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLd(S_y, DateUtils.y_FMT));
  }

  @Test
  void parse_Md_noYear() {
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLdt(S_MdHmsS, DateUtils.MdHmsS_FMT));
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLdt(S_MdHms, DateUtils.MdHms_FMT));
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLdt(S_MdHm, DateUtils.MdHm_FMT));
  }

  @Test
  void parse_noHour() {
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLt(S_msS, DateUtils.msS_FMT));
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLt(S_ms, DateUtils.ms_FMT));
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLt(S_sS, DateUtils.sS_FMT));
  }

  @Test
  void parse_yMdHmsS_C_notParsable() {
    Assertions.assertEquals(S_C_yMdHmsS, DateUtils.format(LDT_MS, DateUtils.yMdHmsS_C_FMT));
    Assertions.assertThrows(DateTimeException.class,
            () -> DateUtils.parseLdt(S_C_yMdHmsS, DateUtils.yMdHmsS_C_FMT));
  }

  @Test
  void parse_leapYear() {
    Assertions.assertEquals(LocalDateTime.of(2020, 2, 29, 12, 0, 0),
            DateUtils.parseLdt("2020-02-29 12:00:00", DateUtils.yMdHms_FMT));
  }

  @Test
  void parse_custom() {
    Assertions.assertEquals(LDT_M,
            DateUtils.parseLdt("15/06/2023 14:30", "dd/MM/yyyy HH:mm"));
  }

  // ==================================================================
  //  roundtrip (format → parse)
  // ==================================================================

  @Test
  void roundtrip_Compound() {
    Assertions.assertEquals(LDT_MS, DateUtils.parseLdt(DateUtils.format(LDT_MS, DateUtils.yMdHmsS_FMT), DateUtils.yMdHmsS_FMT));
    Assertions.assertEquals(LDT_S, DateUtils.parseLdt(DateUtils.format(LDT_S, DateUtils.yMdHms_FMT), DateUtils.yMdHms_FMT));
    Assertions.assertEquals(LDT_M, DateUtils.parseLdt(DateUtils.format(LDT_M, DateUtils.yMdHm_FMT), DateUtils.yMdHm_FMT));
    Assertions.assertEquals(LD, DateUtils.parseLd(DateUtils.format(LD, DateUtils.yMd_FMT), DateUtils.yMd_FMT));
    Assertions.assertEquals(LT_MS, DateUtils.parseLt(DateUtils.format(LT_MS, DateUtils.HmsS_FMT), DateUtils.HmsS_FMT));
    Assertions.assertEquals(LT_S, DateUtils.parseLt(DateUtils.format(LT_S, DateUtils.Hms_FMT), DateUtils.Hms_FMT));
    Assertions.assertEquals(LT_M, DateUtils.parseLt(DateUtils.format(LT_M, DateUtils.Hm_FMT), DateUtils.Hm_FMT));
  }

  @Test
  void roundtrip_Compact() {
    Assertions.assertEquals(LDT_S, DateUtils.parseLdt(DateUtils.format(LDT_S, DateUtils.yMdHms_C_FMT), DateUtils.yMdHms_C_FMT));
    Assertions.assertEquals(LDT_M, DateUtils.parseLdt(DateUtils.format(LDT_M, DateUtils.yMdHm_C_FMT), DateUtils.yMdHm_C_FMT));
    Assertions.assertEquals(LDT_H, DateUtils.parseLdt(DateUtils.format(LDT_H, DateUtils.yMdH_C_FMT), DateUtils.yMdH_C_FMT));
    Assertions.assertEquals(LD, DateUtils.parseLd(DateUtils.format(LD, DateUtils.yMd_C_FMT), DateUtils.yMd_C_FMT));
    Assertions.assertEquals(LT_S, DateUtils.parseLt(DateUtils.format(LT_S, DateUtils.Hms_C_FMT), DateUtils.Hms_C_FMT));
    Assertions.assertEquals(LT_M, DateUtils.parseLt(DateUtils.format(LT_M, DateUtils.Hm_C_FMT), DateUtils.Hm_C_FMT));
  }

  @Test
  void roundtrip_Slash() {
    Assertions.assertEquals(LDT_MS, DateUtils.parseLdt(DateUtils.format(LDT_MS, DateUtils.yMdHmsS_S_FMT), DateUtils.yMdHmsS_S_FMT));
    Assertions.assertEquals(LDT_S, DateUtils.parseLdt(DateUtils.format(LDT_S, DateUtils.yMdHms_S_FMT), DateUtils.yMdHms_S_FMT));
    Assertions.assertEquals(LDT_M, DateUtils.parseLdt(DateUtils.format(LDT_M, DateUtils.yMdHm_S_FMT), DateUtils.yMdHm_S_FMT));
    Assertions.assertEquals(LDT_H, DateUtils.parseLdt(DateUtils.format(LDT_H, DateUtils.yMdH_S_FMT), DateUtils.yMdH_S_FMT));
    Assertions.assertEquals(LD, DateUtils.parseLd(DateUtils.format(LD, DateUtils.yMd_S_FMT), DateUtils.yMd_S_FMT));
  }

  @Test
  void roundtrip_ISO() {
    Assertions.assertEquals(LDT_MS, DateUtils.parseLdt(DateUtils.format(LDT_MS, DateUtils.yMdHmsS_T_FMT), DateUtils.yMdHmsS_T_FMT));
    Assertions.assertEquals(LDT_S, DateUtils.parseLdt(DateUtils.format(LDT_S, DateUtils.yMdHms_T_FMT), DateUtils.yMdHms_T_FMT));
    Assertions.assertEquals(LDT_M, DateUtils.parseLdt(DateUtils.format(LDT_M, DateUtils.yMdHm_T_FMT), DateUtils.yMdHm_T_FMT));
    Assertions.assertEquals(LDT_H, DateUtils.parseLdt(DateUtils.format(LDT_H, DateUtils.yMdH_T_FMT), DateUtils.yMdH_T_FMT));
  }

  // ==================================================================
  //  conversion
  // ==================================================================

  @Test
  void conversion_dateToLDT() {
    Assertions.assertNull(DateUtils.dateToLocalDateTime(null));
    Assertions.assertNull(DateUtils.dateToLocalDateTime(null, ZONE_E8));
    Assertions.assertNotNull(DateUtils.dateToLocalDateTime(DATE));
    Assertions.assertNotNull(DateUtils.dateToLocalDateTime(DATE, ZONE_E8));

    LocalDateTime utc = DateUtils.dateToLocalDateTime(DATE, ZONE_UTC);
    LocalDateTime e8 = DateUtils.dateToLocalDateTime(DATE, ZONE_E8);
    Assertions.assertEquals(8, Duration.between(utc, e8).toHours());

    Assertions.assertEquals(LocalDateTime.of(1970, 1, 1, 0, 0, 0),
            DateUtils.dateToLocalDateTime(new Date(0), ZONE_UTC));
    Assertions.assertEquals(LocalDateTime.of(1969, 12, 31, 0, 0, 0),
            DateUtils.dateToLocalDateTime(new Date(-86400000L), ZONE_UTC));
  }

  @Test
  void conversion_LDTToTimestamp() {
    Assertions.assertNull(DateUtils.localDateTimeToTimestamp(null));
    Assertions.assertNull(DateUtils.localDateTimeToTimestamp(null, ZONE_E8));

    Long ts = DateUtils.localDateTimeToTimestamp(LDT_M, ZONE_E8);
    Assertions.assertNotNull(ts);
    Assertions.assertTrue(ts > 0);

    Long tsE8 = DateUtils.localDateTimeToTimestamp(LDT_H, ZONE_E8);
    Long tsUtc = DateUtils.localDateTimeToTimestamp(LDT_H, ZONE_UTC);
    Assertions.assertEquals(8 * 3600 * 1000L, tsUtc - tsE8);

    Assertions.assertEquals(0L,
            DateUtils.localDateTimeToTimestamp(LocalDateTime.of(1970, 1, 1, 0, 0, 0), ZONE_UTC).longValue());
  }

  @Test
  void conversion_timestampToLDT() {
    Assertions.assertNull(DateUtils.timestampToLocalDateTime(null));
    Assertions.assertNull(DateUtils.timestampToLocalDateTime(null, ZONE_E8));
    Assertions.assertNotNull(DateUtils.timestampToLocalDateTime(TIMESTAMP));
    Assertions.assertNotNull(DateUtils.timestampToLocalDateTime(TIMESTAMP, ZONE_E8));

    LocalDateTime utc = DateUtils.timestampToLocalDateTime(TIMESTAMP, ZONE_UTC);
    LocalDateTime e8 = DateUtils.timestampToLocalDateTime(TIMESTAMP, ZONE_E8);
    Assertions.assertEquals(8, Duration.between(utc, e8).toHours());

    Assertions.assertEquals(LocalDateTime.of(1970, 1, 1, 0, 0, 0),
            DateUtils.timestampToLocalDateTime(0L, ZONE_UTC));
    Assertions.assertEquals(LocalDateTime.of(1969, 12, 31, 0, 0, 0),
            DateUtils.timestampToLocalDateTime(-86400000L, ZONE_UTC));
    Assertions.assertDoesNotThrow(() -> DateUtils.timestampToLocalDateTime(Long.MAX_VALUE));
  }

  @Test
  void conversion_LDTToDate() {
    Assertions.assertNull(DateUtils.localDateTimeToDate(null));
    Assertions.assertNull(DateUtils.localDateTimeToDate(null, ZONE_E8));
    Assertions.assertNotNull(DateUtils.localDateTimeToDate(LDT_M, ZONE_E8));

    Date dE8 = DateUtils.localDateTimeToDate(LDT_H, ZONE_E8);
    Date dUtc = DateUtils.localDateTimeToDate(LDT_H, ZONE_UTC);
    Assertions.assertEquals(8 * 3600 * 1000L, dUtc.getTime() - dE8.getTime());
  }

  @Test
  void conversion_LDToDate() {
    Assertions.assertNull(DateUtils.localDateToDate(null));
    Assertions.assertNull(DateUtils.localDateToDate(null, ZONE_E8));
    Assertions.assertNotNull(DateUtils.localDateToDate(LD));

    Date utc = DateUtils.localDateToDate(LD, ZONE_UTC);
    Date e8 = DateUtils.localDateToDate(LD, ZONE_E8);
    Assertions.assertEquals(8 * 3600 * 1000L, utc.getTime() - e8.getTime());
    LocalDateTime ldtUtc = DateUtils.dateToLocalDateTime(utc, ZONE_UTC);
    Assertions.assertEquals(0, ldtUtc.getHour());
    Assertions.assertEquals(0, ldtUtc.getMinute());
    Assertions.assertEquals(0, ldtUtc.getSecond());
  }

  @Test
  void conversion_LTToDate() {
    Assertions.assertNull(DateUtils.localTimeToDate(null));
    Assertions.assertNull(DateUtils.localTimeToDate(null, ZONE_E8));
    Assertions.assertNotNull(DateUtils.localTimeToDate(LT_M));

    Assertions.assertEquals(0L,
            DateUtils.localTimeToDate(LocalTime.of(0, 0, 0), ZONE_UTC).getTime());

    Assertions.assertEquals(-28800000L,
            DateUtils.localTimeToDate(LocalTime.of(0, 0, 0), ZONE_E8).getTime());

    Date e8 = DateUtils.localTimeToDate(LocalTime.of(14, 0, 0), ZONE_E8);
    Date utc = DateUtils.localTimeToDate(LocalTime.of(14, 0, 0), ZONE_UTC);
    Assertions.assertEquals(8 * 3600 * 1000L, utc.getTime() - e8.getTime());
  }

  @Test
  void conversion_nullZone() {
    Assertions.assertEquals(
            DateUtils.dateToLocalDateTime(DATE),
            DateUtils.dateToLocalDateTime(DATE, null));

    LocalDateTime ldt = LocalDateTime.now();
    Assertions.assertEquals(
            DateUtils.localDateTimeToTimestamp(ldt),
            DateUtils.localDateTimeToTimestamp(ldt, null));
    Assertions.assertEquals(
            DateUtils.localDateTimeToDate(ldt),
            DateUtils.localDateTimeToDate(ldt, null));

    LocalDate ld = LocalDate.now();
    Assertions.assertEquals(
            DateUtils.localDateToDate(ld),
            DateUtils.localDateToDate(ld, null));

    LocalTime lt = LocalTime.now();
    Assertions.assertEquals(
            DateUtils.localTimeToDate(lt),
            DateUtils.localTimeToDate(lt, null));
  }

  @Test
  void conversion_roundtrip() {
    Date d = new Date(TIMESTAMP);
    Assertions.assertEquals(d, DateUtils.localDateTimeToDate(DateUtils.dateToLocalDateTime(d)));

    LocalDateTime ldtE8 = DateUtils.dateToLocalDateTime(DATE, ZONE_E8);
    Assertions.assertEquals(DATE, DateUtils.localDateTimeToDate(ldtE8, ZONE_E8));

    LocalDateTime ldtFromTs = DateUtils.timestampToLocalDateTime(TIMESTAMP);
    Assertions.assertEquals(TIMESTAMP, DateUtils.localDateTimeToTimestamp(ldtFromTs).longValue());

    Assertions.assertEquals(LDT_M,
            DateUtils.timestampToLocalDateTime(
                    DateUtils.localDateTimeToTimestamp(LDT_M, ZONE_E8), ZONE_E8));
  }

  @Test
  void conversion_multiZone() {
    LocalDateTime e8 = DateUtils.timestampToLocalDateTime(TIMESTAMP, ZONE_E8);
    LocalDateTime utc = DateUtils.timestampToLocalDateTime(TIMESTAMP, ZONE_UTC);
    LocalDateTime e9 = DateUtils.timestampToLocalDateTime(TIMESTAMP, ZoneId.of("+09:00"));
    LocalDateTime n5 = DateUtils.timestampToLocalDateTime(TIMESTAMP, ZoneId.of("-05:00"));

    Assertions.assertEquals(8, Duration.between(utc, e8).toHours());
    Assertions.assertEquals(9, Duration.between(utc, e9).toHours());
    Assertions.assertEquals(-5, Duration.between(utc, n5).toHours());
  }

  @Test
  void conversion_edge() {
    Assertions.assertEquals("0001-01-01 00:00:00",
            DateUtils.format(LocalDateTime.of(1, 1, 1, 0, 0, 0), DateUtils.yMdHms_FMT));
  }

  // ==================================================================
  //  defaultZoneId — biz.component.timezone
  // ==================================================================

  @Test
  void defaultZoneId_valid() throws Exception {
    java.lang.reflect.Field f = DateUtils.class.getDeclaredField("defaultZoneId");
    f.setAccessible(true);
    ZoneId dz = (ZoneId) f.get(null);
    Assertions.assertNotNull(dz);
    Assertions.assertNotNull(dz.getRules());
  }

  @Test
  void defaultZoneId_nullZoneFallback() {
    Assertions.assertEquals(
            DateUtils.dateToLocalDateTime(DATE),
            DateUtils.dateToLocalDateTime(DATE, null));
    Assertions.assertEquals(
            DateUtils.timestampToLocalDateTime(TIMESTAMP),
            DateUtils.timestampToLocalDateTime(TIMESTAMP, null));
    Assertions.assertEquals(
            DateUtils.localDateTimeToTimestamp(LDT_M),
            DateUtils.localDateTimeToTimestamp(LDT_M, null));
    Assertions.assertEquals(
            DateUtils.localDateTimeToDate(LDT_M),
            DateUtils.localDateTimeToDate(LDT_M, null));
    Assertions.assertEquals(
            DateUtils.localDateToDate(LD),
            DateUtils.localDateToDate(LD, null));
    Assertions.assertEquals(
            DateUtils.localTimeToDate(LT_M),
            DateUtils.localTimeToDate(LT_M, null));
  }

  @Test
  void defaultZoneId_explicitVsNull() {
    long tsE8 = DateUtils.localDateTimeToTimestamp(LDT_H, ZONE_E8);
    long tsUtc = DateUtils.localDateTimeToTimestamp(LDT_H, ZONE_UTC);
    long tsNull = DateUtils.localDateTimeToTimestamp(LDT_H, null);
    long tsNone = DateUtils.localDateTimeToTimestamp(LDT_H);

    Assertions.assertEquals(tsNone, tsNull);
    Assertions.assertEquals(8 * 3600 * 1000L, tsUtc - tsE8);

    LocalDateTime fromE8 = DateUtils.timestampToLocalDateTime(TIMESTAMP, ZONE_E8);
    LocalDateTime fromUtc = DateUtils.timestampToLocalDateTime(TIMESTAMP, ZONE_UTC);
    LocalDateTime fromNull = DateUtils.timestampToLocalDateTime(TIMESTAMP, null);
    LocalDateTime fromNone = DateUtils.timestampToLocalDateTime(TIMESTAMP);

    Assertions.assertEquals(fromNone, fromNull);
    Assertions.assertEquals(8, Duration.between(fromUtc, fromE8).toHours());

    Date dE8 = DateUtils.localDateTimeToDate(LDT_H, ZONE_E8);
    Date dUtc = DateUtils.localDateTimeToDate(LDT_H, ZONE_UTC);
    Date dNone = DateUtils.localDateTimeToDate(LDT_H);
    Date dNull = DateUtils.localDateTimeToDate(LDT_H, null);
    Assertions.assertEquals(dNone, dNull);
    Assertions.assertEquals(8 * 3600 * 1000L, dUtc.getTime() - dE8.getTime());
  }

}
