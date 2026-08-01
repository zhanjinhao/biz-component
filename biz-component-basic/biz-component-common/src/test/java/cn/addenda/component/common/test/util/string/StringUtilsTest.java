package cn.addenda.component.common.test.util.string;

import cn.addenda.component.common.util.string.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

  // ==================== biTrimSpecifiedChar ====================

  @Test
  void testBiTrim_BothSides() {
    Assertions.assertEquals("example", StringUtils.biTrimSpecifiedChar(":example:", ':'));
  }

  @Test
  void testBiTrim_RightSide() {
    Assertions.assertEquals("example", StringUtils.biTrimSpecifiedChar("example:", ':'));
  }

  @Test
  void testBiTrim_LeftSide() {
    Assertions.assertEquals("example", StringUtils.biTrimSpecifiedChar(":example", ':'));
  }

  @Test
  void testBiTrim_NoTrim() {
    Assertions.assertEquals("example", StringUtils.biTrimSpecifiedChar("example", ':'));
  }

  @Test
  void testBiTrim_MultipleChar() {
    Assertions.assertEquals("example", StringUtils.biTrimSpecifiedChar("::example::", ':'));
  }

  @Test
  void testBiTrim_Null() {
    Assertions.assertNull(StringUtils.biTrimSpecifiedChar(null, ':'));
  }

  @Test
  void testBiTrim_AllMatching() {
    Assertions.assertEquals("", StringUtils.biTrimSpecifiedChar("::::", ':'));
  }

  // ==================== expandWithSpecifiedChar / expandWithZero ====================

  @Test
  void testExpandWithSpecifiedChar() {
    Assertions.assertEquals("00123", StringUtils.expandWithSpecifiedChar("123", '0', 5));
  }

  @Test
  void testExpandWithSpecifiedChar_AlreadyLongEnough() {
    Assertions.assertEquals("123", StringUtils.expandWithSpecifiedChar("123", '0', 3));
  }

  @Test
  void testExpandWithSpecifiedChar_LongerThanExpected() {
    Assertions.assertEquals("12345", StringUtils.expandWithSpecifiedChar("12345", '0', 3));
  }

  @Test
  void testExpandWithZero() {
    Assertions.assertEquals("00042", StringUtils.expandWithZero("42", 5));
  }

  // ==================== join ====================

  @Test
  void testJoin_Normal() {
    Assertions.assertEquals("a,b,c", StringUtils.join(",", "a", "b", "c"));
  }

  @Test
  void testJoin_EmptyArray() {
    Assertions.assertEquals("", StringUtils.join(","));
  }

  @Test
  void testJoin_NullValue() {
    Assertions.assertEquals("a,c", StringUtils.join(",", "a", null, "c"));
  }

  @Test
  void testJoin_BlankValue() {
    Assertions.assertEquals("a,,c", StringUtils.join(",", "a", "", "c"));
  }

  @Test
  void testJoin_AllNull() {
    Assertions.assertEquals("", StringUtils.join(",", null, null, null));
  }

  @Test
  void testJoin_SingleValue() {
    Assertions.assertEquals("a", StringUtils.join(",", "a"));
  }

  @Test
  void testJoin_WhitespaceValue() {
    Assertions.assertEquals("a,  ,c", StringUtils.join(",", "a", "  ", "c"));
  }

  @Test
  void testJoin_FirstNull() {
    Assertions.assertEquals("b", StringUtils.join(",", null, "b"));
  }

  @Test
  void testJoin_NullSeparator() {
    Assertions.assertEquals("anullb", StringUtils.join(null, "a", "b"));
  }

  // ==================== joinArrayToString(String[], int, int) ====================

  @Test
  void testJoinArrayToString_Array_Normal() {
    String result = StringUtils.joinArrayToString(new String[]{"a", "b", "c", "d"}, 1, 3);
    Assertions.assertEquals("b c", result);
  }

  @Test
  void testJoinArrayToString_Array_Null() {
    Assertions.assertNull(StringUtils.joinArrayToString((String[]) null, 0, 1));
  }

  @Test
  void testJoinArrayToString_Array_FullRange() {
    String result = StringUtils.joinArrayToString(new String[]{"a", "b", "c"}, 0, 3);
    Assertions.assertEquals("a b c", result);
  }

  @Test
  void testJoinArrayToString_Array_SingleElement() {
    String result = StringUtils.joinArrayToString(new String[]{"a"}, 0, 1);
    Assertions.assertEquals("a", result);
  }

  @Test
  void testJoinArrayToString_Array_EndIndexOutOfBounds() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> StringUtils.joinArrayToString(new String[]{"a"}, 0, 2));
  }

  @Test
  void testJoinArrayToString_Array_FromIndexNegative() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> StringUtils.joinArrayToString(new String[]{"a"}, -1, 1));
  }

  // ==================== joinArrayToString(String, int, int) ====================

  @Test
  void testJoinArrayToString_String_Normal() {
    String result = StringUtils.joinArrayToString("a b c d", 1, 3);
    Assertions.assertEquals("b c", result);
  }

  @Test
  void testJoinArrayToString_String_Null() {
    Assertions.assertNull(StringUtils.joinArrayToString((String) null, 0, 1));
  }

  @Test
  void testJoinArrayToString_String_Blank() {
    Assertions.assertEquals("", StringUtils.joinArrayToString("", 0, 1));
  }

  @Test
  void testJoinArrayToString_String_WhitespaceOnly() {
    Assertions.assertEquals("   ", StringUtils.joinArrayToString("   ", 0, 1));
  }

  @Test
  void testJoinArrayToString_String_MultipleSpaces() {
    String result = StringUtils.joinArrayToString("a  b  c", 0, 3);
    Assertions.assertEquals("a b c", result);
  }

  // ==================== joinArrayToString with delimiter ====================

  @Test
  void testJoinArrayToString_Array_WithDelimiter() {
    String result = StringUtils.joinArrayToString(new String[]{"a", "b", "c", "d"}, ",", 1, 3);
    Assertions.assertEquals("b,c", result);
  }

  @Test
  void testJoinArrayToString_Array_DelimiterFullRange() {
    String result = StringUtils.joinArrayToString(new String[]{"1", "2", "3"}, " - ", 0, 3);
    Assertions.assertEquals("1 - 2 - 3", result);
  }

  @Test
  void testJoinArrayToString_Array_DelimiterSingleElement() {
    String result = StringUtils.joinArrayToString(new String[]{"a"}, ":", 0, 1);
    Assertions.assertEquals("a", result);
  }

  @Test
  void testJoinArrayToString_String_CommaDelimiter() {
    String result = StringUtils.joinArrayToString("a,b,c,d,e", ",", ",", 1, 4);
    Assertions.assertEquals("b,c,d", result);
  }

  @Test
  void testJoinArrayToString_String_PipeDelimiter() {
    String result = StringUtils.joinArrayToString("x|y|z|w", "\\|", ":", 1, 3);
    Assertions.assertEquals("y:z", result);
  }

  @Test
  void testJoinArrayToString_String_DelimiterNull() {
    Assertions.assertNull(StringUtils.joinArrayToString((String) null, ",", ",", 0, 1));
  }

  @Test
  void testJoinArrayToString_String_DelimiterBlank() {
    Assertions.assertEquals("", StringUtils.joinArrayToString("", ",", ",", 0, 1));
  }

  // ==================== replaceCharAtIndex ====================

  @Test
  void testReplaceCharAtIndex() {
    Assertions.assertEquals("abXde", StringUtils.replaceCharAtIndex("abcde", 2, 'X'));
  }

  @Test
  void testReplaceCharAtIndex_First() {
    Assertions.assertEquals("Xbc", StringUtils.replaceCharAtIndex("abc", 0, 'X'));
  }

  @Test
  void testReplaceCharAtIndex_Last() {
    Assertions.assertEquals("abX", StringUtils.replaceCharAtIndex("abc", 2, 'X'));
  }

  @Test
  void testReplaceCharAtIndex_Null() {
    Assertions.assertNull(StringUtils.replaceCharAtIndex(null, 0, 'X'));
  }

  @Test
  void testReplaceCharAtIndex_SingleChar() {
    Assertions.assertEquals("X", StringUtils.replaceCharAtIndex("a", 0, 'X'));
  }

  @Test
  void testReplaceCharAtIndex_IndexNegative() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> StringUtils.replaceCharAtIndex("abc", -1, 'X'));
  }

  @Test
  void testReplaceCharAtIndex_IndexOutOfBounds() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> StringUtils.replaceCharAtIndex("abc", 3, 'X'));
  }

  // ==================== discardNull ====================

  @Test
  void testDiscardNull_Null() {
    Assertions.assertEquals("", StringUtils.discardNull(null));
  }

  @Test
  void testDiscardNull_NotNull() {
    Assertions.assertEquals("hello", StringUtils.discardNull("hello"));
  }

  // ==================== hasText ====================

  @Test
  void testHasText_NonNull() {
    Assertions.assertTrue(StringUtils.hasText("hello"));
  }

  @Test
  void testHasText_Null() {
    Assertions.assertFalse(StringUtils.hasText((String) null));
  }

  @Test
  void testHasText_Empty() {
    Assertions.assertFalse(StringUtils.hasText(""));
  }

  @Test
  void testHasText_WhitespaceOnly() {
    Assertions.assertFalse(StringUtils.hasText("   "));
  }

  @Test
  void testHasText_CharSequence() {
    StringBuilder sb = new StringBuilder("text");
    Assertions.assertTrue(StringUtils.hasText(sb));
  }

  @Test
  void testHasText_CharSequence_Null() {
    Assertions.assertFalse(StringUtils.hasText((CharSequence) null));
  }

  @Test
  void testHasText_CharSequence_Empty() {
    Assertions.assertFalse(StringUtils.hasText(new StringBuilder("")));
  }

  @Test
  void testHasText_CharSequence_WhitespaceOnly() {
    Assertions.assertFalse(StringUtils.hasText(new StringBuilder("   ")));
  }

  // ==================== containsText ====================

  @Test
  void testContainsText_True() {
    Assertions.assertTrue(StringUtils.containsText(" a "));
  }

  @Test
  void testContainsText_False_Whitespace() {
    Assertions.assertFalse(StringUtils.containsText("   "));
  }

  @Test
  void testContainsText_Empty() {
    Assertions.assertFalse(StringUtils.containsText(""));
  }

  // ==================== hasLength ====================

  @Test
  void testHasLength_True() {
    Assertions.assertTrue(StringUtils.hasLength("abc"));
  }

  @Test
  void testHasLength_False_Null() {
    Assertions.assertFalse(StringUtils.hasLength(null));
  }

  @Test
  void testHasLength_False_Empty() {
    Assertions.assertFalse(StringUtils.hasLength(""));
  }

  @Test
  void testHasLength_WhitespaceOnly() {
    Assertions.assertTrue(StringUtils.hasLength("   "));
  }

  // ==================== isStrictlyNumeric ====================

  @Test
  void testIsStrictlyNumeric_True() {
    Assertions.assertTrue(StringUtils.isStrictlyNumeric("12345"));
  }

  @Test
  void testIsStrictlyNumeric_False_Alpha() {
    Assertions.assertFalse(StringUtils.isStrictlyNumeric("12a45"));
  }

  @Test
  void testIsStrictlyNumeric_False_Null() {
    Assertions.assertFalse(StringUtils.isStrictlyNumeric(null));
  }

  @Test
  void testIsStrictlyNumeric_False_Empty() {
    Assertions.assertFalse(StringUtils.isStrictlyNumeric(""));
  }

  @Test
  void testIsStrictlyNumeric_False_Negative() {
    Assertions.assertFalse(StringUtils.isStrictlyNumeric("-123"));
  }

  @Test
  void testIsStrictlyNumeric_False_Decimal() {
    Assertions.assertFalse(StringUtils.isStrictlyNumeric("1.23"));
  }

  @Test
  void testIsStrictlyNumeric_SingleDigit() {
    Assertions.assertTrue(StringUtils.isStrictlyNumeric("0"));
  }

  @Test
  void testIsStrictlyNumeric_False_Whitespace() {
    Assertions.assertFalse(StringUtils.isStrictlyNumeric("  "));
  }

  // ==================== isInteger ====================

  @Test
  void testIsInteger_True_Positive() {
    Assertions.assertTrue(StringUtils.isInteger("12345"));
  }

  @Test
  void testIsInteger_True_LeadingPlus() {
    Assertions.assertTrue(StringUtils.isInteger("+42"));
  }

  @Test
  void testIsInteger_True_Negative() {
    Assertions.assertTrue(StringUtils.isInteger("-99"));
  }

  @Test
  void testIsInteger_True_Zero() {
    Assertions.assertTrue(StringUtils.isInteger("0"));
  }

  @Test
  void testIsInteger_False_MinusOnly() {
    Assertions.assertFalse(StringUtils.isInteger("-"));
  }

  @Test
  void testIsInteger_False_PlusOnly() {
    Assertions.assertFalse(StringUtils.isInteger("+"));
  }

  @Test
  void testIsInteger_False_Alpha() {
    Assertions.assertFalse(StringUtils.isInteger("12a45"));
  }

  @Test
  void testIsInteger_False_Decimal() {
    Assertions.assertFalse(StringUtils.isInteger("1.23"));
  }

  @Test
  void testIsInteger_False_OperatorMiddle() {
    Assertions.assertFalse(StringUtils.isInteger("4+2"));
  }

  @Test
  void testIsInteger_False_OperatorMiddle2() {
    Assertions.assertFalse(StringUtils.isInteger("4-2"));
  }

  @Test
  void testIsInteger_False_Null() {
    Assertions.assertFalse(StringUtils.isInteger(null));
  }

  @Test
  void testIsInteger_False_Empty() {
    Assertions.assertFalse(StringUtils.isInteger(""));
  }

  @Test
  void testIsInteger_False_Whitespace() {
    Assertions.assertFalse(StringUtils.isInteger("  "));
  }

  @Test
  void testIsInteger_False_DoubleNegative() {
    Assertions.assertFalse(StringUtils.isInteger("--1"));
  }

  // ==================== substringMatch ====================

  @Test
  void testSubstringMatch_True() {
    Assertions.assertTrue(StringUtils.substringMatch("hello world", 6, "world"));
  }

  @Test
  void testSubstringMatch_False() {
    Assertions.assertFalse(StringUtils.substringMatch("hello world", 6, "xxxxx"));
  }

  @Test
  void testSubstringMatch_BeyondLength() {
    Assertions.assertFalse(StringUtils.substringMatch("hi", 0, "hello"));
  }

  @Test
  void testSubstringMatch_ExactMatch() {
    Assertions.assertTrue(StringUtils.substringMatch("hello", 0, "hello"));
  }

  @Test
  void testSubstringMatch_EmptySubstring() {
    Assertions.assertFalse(StringUtils.substringMatch("hello", 0, ""));
  }

  @Test
  void testSubstringMatch_NullStr() {
    Assertions.assertFalse(StringUtils.substringMatch(null, 0, "hello"));
  }

  @Test
  void testSubstringMatch_NullSubstring() {
    Assertions.assertFalse(StringUtils.substringMatch("hello", 0, null));
  }

  // ==================== startsWithIgnoreCase ====================

  @Test
  void testStartsWithIgnoreCase_True() {
    Assertions.assertTrue(StringUtils.startsWithIgnoreCase("HelloWorld", "hello"));
  }

  @Test
  void testStartsWithIgnoreCase_False() {
    Assertions.assertFalse(StringUtils.startsWithIgnoreCase("HelloWorld", "world"));
  }

  @Test
  void testStartsWithIgnoreCase_Null() {
    Assertions.assertFalse(StringUtils.startsWithIgnoreCase(null, "hello"));
  }

  @Test
  void testStartsWithIgnoreCase_NullPrefix() {
    Assertions.assertFalse(StringUtils.startsWithIgnoreCase("hello", null));
  }

  @Test
  void testStartsWithIgnoreCase_Shorter() {
    Assertions.assertFalse(StringUtils.startsWithIgnoreCase("hi", "hello"));
  }

  @Test
  void testStartsWithIgnoreCase_ExactCase() {
    Assertions.assertTrue(StringUtils.startsWithIgnoreCase("hello", "hello"));
  }

  // ==================== startsWithIgnoreBlankAndCase ====================

  @Test
  void testStartsWithIgnoreBlankAndCase_True() {
    Assertions.assertTrue(StringUtils.startsWithIgnoreBlankAndCase("  HelloWorld", "hello"));
  }

  @Test
  void testStartsWithIgnoreBlankAndCase_InternalBlank() {
    Assertions.assertTrue(StringUtils.startsWithIgnoreBlankAndCase("H e l l o World", "hello"));
  }

  @Test
  void testStartsWithIgnoreBlankAndCase_False() {
    Assertions.assertFalse(StringUtils.startsWithIgnoreBlankAndCase("HelloWorld", "world"));
  }

  @Test
  void testStartsWithIgnoreBlankAndCase_Null() {
    Assertions.assertFalse(StringUtils.startsWithIgnoreBlankAndCase(null, "hello"));
  }

  @Test
  void testStartsWithIgnoreBlankAndCase_NullPrefix() {
    Assertions.assertFalse(StringUtils.startsWithIgnoreBlankAndCase("hello", null));
  }

  @Test
  void testStartsWithIgnoreBlankAndCase_AllBlank() {
    Assertions.assertFalse(StringUtils.startsWithIgnoreBlankAndCase("   ", "hello"));
  }

  @Test
  void testStartsWithIgnoreBlankAndCase_ExactMatch() {
    Assertions.assertTrue(StringUtils.startsWithIgnoreBlankAndCase("hello", "hello"));
  }

  @Test
  void testStartsWithIgnoreBlankAndCase_EmptyPrefix() {
    Assertions.assertTrue(StringUtils.startsWithIgnoreBlankAndCase("hello", ""));
  }

  @Test
  void testStartsWithIgnoreBlankAndCase_StrShorterThanPrefix() {
    Assertions.assertFalse(StringUtils.startsWithIgnoreBlankAndCase("  hi", "hello"));
  }

  @Test
  void testStartsWithIgnoreBlankAndCase_StrLongerThanPrefix() {
    Assertions.assertTrue(StringUtils.startsWithIgnoreBlankAndCase("  hello world", "hello"));
  }

  // ==================== endsWithIgnoreBlankAndCase ====================

  @Test
  void testEndsWithIgnoreBlankAndCase_True() {
    Assertions.assertTrue(StringUtils.endsWithIgnoreBlankAndCase("HelloWorld  ", "world"));
  }

  @Test
  void testEndsWithIgnoreBlankAndCase_InternalBlank() {
    Assertions.assertTrue(StringUtils.endsWithIgnoreBlankAndCase("HelloW o  r  l d", "world"));
  }

  @Test
  void testEndsWithIgnoreBlankAndCase_False() {
    Assertions.assertFalse(StringUtils.endsWithIgnoreBlankAndCase("HelloWorld", "hello"));
  }

  @Test
  void testEndsWithIgnoreBlankAndCase_Null() {
    Assertions.assertFalse(StringUtils.endsWithIgnoreBlankAndCase(null, "world"));
  }

  @Test
  void testEndsWithIgnoreBlankAndCase_NullSuffix() {
    Assertions.assertFalse(StringUtils.endsWithIgnoreBlankAndCase("hello", null));
  }

  @Test
  void testEndsWithIgnoreBlankAndCase_AllBlank() {
    Assertions.assertFalse(StringUtils.endsWithIgnoreBlankAndCase("   ", "world"));
  }

  @Test
  void testEndsWithIgnoreBlankAndCase_ExactMatch() {
    Assertions.assertTrue(StringUtils.endsWithIgnoreBlankAndCase("world", "world"));
  }

  @Test
  void testEndsWithIgnoreBlankAndCase_EmptySuffix() {
    Assertions.assertTrue(StringUtils.endsWithIgnoreBlankAndCase("hello", ""));
  }

  @Test
  void testEndsWithIgnoreBlankAndCase_StrShorterThanSuffix() {
    Assertions.assertFalse(StringUtils.endsWithIgnoreBlankAndCase("ld  ", "world"));
  }

  @Test
  void testEndsWithIgnoreBlankAndCase_StrLongerThanSuffix() {
    Assertions.assertTrue(StringUtils.endsWithIgnoreBlankAndCase("hello world  ", "world"));
  }

  // ==================== atMost ====================

  @Test
  void testAtMost_Null() {
    Assertions.assertNull(StringUtils.atMost(null, 5));
  }

  @Test
  void testAtMost_Truncate() {
    Assertions.assertEquals("Hello", StringUtils.atMost("Hello World", 5));
  }

  @Test
  void testAtMost_ShorterThanMax() {
    Assertions.assertEquals("Hi", StringUtils.atMost("Hi", 5));
  }

  @Test
  void testAtMost_EqualToMax() {
    Assertions.assertEquals("Hello", StringUtils.atMost("Hello", 5));
  }

  @Test
  void testAtMost_ZeroMaxLength() {
    Assertions.assertEquals("", StringUtils.atMost("Hello", 0));
  }

  @Test
  void testAtMost_NegativeMaxLength() {
    Assertions.assertEquals("Hello", StringUtils.atMost("Hello", -1));
  }

  @Test
  void testAtMost_EmptyString() {
    Assertions.assertEquals("", StringUtils.atMost("", 5));
  }

  // ==================== joinArrayToString (String[] pieces) ====================

  @Test
  void testJoinArrayToString_Normal() {
    Assertions.assertEquals("a b c", StringUtils.joinArrayToString(new String[]{"a", "b", "c"}, 0, 3));
  }

  @Test
  void testJoinArrayToString_SubRange() {
    Assertions.assertEquals("b c", StringUtils.joinArrayToString(new String[]{"a", "b", "c", "d"}, 1, 3));
  }

  @Test
  void testJoinArrayToString_NullPieces() {
    Assertions.assertNull(StringUtils.joinArrayToString((String[]) null, 0, 1));
  }

  @Test
  void testJoinArrayToString_EmptyRange() {
    Assertions.assertEquals("", StringUtils.joinArrayToString(new String[]{"a", "b"}, 1, 1));
  }

  // ==================== joinArrayToString (String[] pieces, delimiter) ====================

  @Test
  void testJoinArrayToStringWithDelimiter_Comma() {
    Assertions.assertEquals("a,b,c", StringUtils.joinArrayToString(new String[]{"a", "b", "c"}, ",", 0, 3));
  }

  @Test
  void testJoinArrayToStringWithDelimiter_SubRange() {
    Assertions.assertEquals("b-c-d", StringUtils.joinArrayToString(new String[]{"a", "b", "c", "d"}, "-", 1, 4));
  }

  @Test
  void testJoinArrayToStringWithDelimiter_NullPieces() {
    Assertions.assertNull(StringUtils.joinArrayToString(null, ",", 0, 1));
  }

  // ==================== joinArrayToString (String content, fromIndex, endIndex) ====================

  @Test
  void testJoinArrayToStringFromContent_Normal() {
    Assertions.assertEquals("a b c", StringUtils.joinArrayToString("a b c", 0, 3));
  }

  @Test
  void testJoinArrayToStringFromContent_SubRange() {
    Assertions.assertEquals("b", StringUtils.joinArrayToString("a  b  c", 1, 2));
  }

  @Test
  void testJoinArrayToStringFromContent_NullContent() {
    Assertions.assertNull(StringUtils.joinArrayToString((String) null, 0, 1));
  }

  @Test
  void testJoinArrayToStringFromContent_EmptyContent() {
    Assertions.assertEquals("", StringUtils.joinArrayToString("", 0, 1));
  }

  // ==================== joinArrayToString (String content, splitRegex, delimiter) ====================

  @Test
  void testJoinArrayToStringWithRegex_Normal() {
    Assertions.assertEquals("a,b,c", StringUtils.joinArrayToString("a b c", "\\s+", ",", 0, 3));
  }

  @Test
  void testJoinArrayToStringWithRegex_SubRange() {
    Assertions.assertEquals("c", StringUtils.joinArrayToString("a  b  c  d", "\\s+", ",", 2, 3));
  }

  @Test
  void testJoinArrayToStringWithRegex_NullContent() {
    Assertions.assertNull(StringUtils.joinArrayToString(null, "\\s+", ",", 0, 1));
  }

  @Test
  void testJoinArrayToStringWithRegex_BlankContent() {
    Assertions.assertEquals("  ", StringUtils.joinArrayToString("  ", "\\s+", ",", 0, 1));
  }

}
