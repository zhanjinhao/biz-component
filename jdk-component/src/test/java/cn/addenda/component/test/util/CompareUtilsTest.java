package cn.addenda.component.test.util;

import java.util.List;

import cn.addenda.component.jdk.util.my.MyArrayUtils;
import cn.addenda.component.jdk.util.CompareUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author addenda
 * @since 2024/1/9 10:39
 */
public class CompareUtilsTest {

  List<String> sequence = MyArrayUtils.asArrayList("1", "2", null, "3");

  @Test
  public void test1() {
    sequence.sort(CompareUtils::nullMaxCompare);
    Assert.assertEquals(MyArrayUtils.asArrayList("1", "2", "3", null), sequence);
    sequence.sort(CompareUtils::nullMinCompare);
    Assert.assertEquals(MyArrayUtils.asArrayList(null, "1", "2", "3"), sequence);
  }

}
