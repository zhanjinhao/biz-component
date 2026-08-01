package cn.addenda.component.common.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author addenda
 * @since 2022/8/11
 */
class HashCodeUniqueTest {

  @Test
  void main() {
    Assertions.assertEquals("AaAa".hashCode(), "BBBB".hashCode());
  }

}
