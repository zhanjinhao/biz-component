package cn.addenda.component.idempotenct;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author addenda
 * @since 2023/9/15 9:08
 */
@Setter
@Getter
@ToString
public class StorageCenterEntity {

  private Long id;

  private String namespace;

  private String prefix;

  private String key;

  private ConsumeMode consumeMode;

  private String xId;

  private ConsumeStatus consumeStatus;

  private LocalDateTime expireTime;

  public String getFullKey() {
    return namespace + ":" + prefix + ":" + key;
  }

}
