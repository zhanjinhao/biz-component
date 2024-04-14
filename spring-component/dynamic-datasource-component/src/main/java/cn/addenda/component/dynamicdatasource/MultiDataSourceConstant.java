package cn.addenda.component.dynamicdatasource;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author addenda
 * @since 2022/3/3 17:24
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiDataSourceConstant {

  public static final String MASTER = "MASTER";
  public static final String SLAVE = "SLAVE";

  public static final String DEFAULT = "default";

}
