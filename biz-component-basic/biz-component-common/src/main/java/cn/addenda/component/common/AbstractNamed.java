package cn.addenda.component.common;

import lombok.Setter;

public abstract class AbstractNamed implements Named {

  @Setter
  private String name;

  protected AbstractNamed() {
  }

  protected AbstractNamed(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }
}
