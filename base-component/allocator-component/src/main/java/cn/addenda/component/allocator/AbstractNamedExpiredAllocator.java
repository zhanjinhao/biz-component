package cn.addenda.component.allocator;

import lombok.Setter;

/**
 * @author addenda
 * @since 2023/9/27 18:49
 */
public abstract class AbstractNamedExpiredAllocator<T> implements NamedExpiredAllocator<T> {

  @Setter
  private String name;

  protected AbstractNamedExpiredAllocator(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

}
