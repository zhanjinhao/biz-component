package cn.addenda.component.idgenerator.idgenerator;

import java.util.UUID;

/**
 * @author addenda
 * @since 2022/2/4 14:38
 */
public class UUIDIdGenerator implements IdGenerator {

  @Override
  public Object nextId(String scopeName) {
    return scopeName + ":" + UUID.randomUUID();
  }

}
