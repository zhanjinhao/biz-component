package cn.addenda.component.convention.exception.client;

import cn.addenda.component.convention.exception.IErrorCode;

public class ClientErrorCode implements IErrorCode {

  private final String message;

  public ClientErrorCode(String message) {
    this.message = message;
  }

  @Override
  public String code() {
    return "client";
  }

  @Override
  public String message() {
    return message;
  }
}
