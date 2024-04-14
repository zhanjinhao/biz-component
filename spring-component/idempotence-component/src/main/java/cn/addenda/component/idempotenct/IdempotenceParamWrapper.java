package cn.addenda.component.idempotenct;

import lombok.*;

/**
 * @author addenda
 * @since 2023/7/29 18:07
 */
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdempotenceParamWrapper {

  private String namespace;

  private String prefix;

  private String key;

  private ConsumeMode consumeMode;

  private int ttlSecs;

  private String xId;

  public String getFullKey() {
    return namespace + ":" + prefix + ":" + key;
  }

  public String getSimpleKey() {
    return prefix + ":" + key;
  }

}
