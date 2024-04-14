package cn.addenda.component.basaspring.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * @author addenda
 * @since 2022/11/28 22:49
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssertUtils {

  public static void notNull(Object condition) {
    Assert.notNull(condition, "parameter cannot be null. ");
  }

  public static void notNull(Object condition, String filedName) {
    Assert.notNull(condition, filedName + " cannot be null. ");
  }

  public static void notModified(Object condition, String filedName) {
    Assert.isNull(condition, filedName + " cannot be modified. ");
  }

  public static void notApplied(Object condition, String filedName) {
    Assert.isNull(condition, filedName + " cannot be applied. ");
  }

}
