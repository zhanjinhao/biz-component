package cn.addenda.component.convention.exception;

public class ServiceErrorCode implements IErrorCode {

  private final String message;

  public ServiceErrorCode(String message) {
    this.message = message;
  }

  @Override
  public String code() {
    return "service";
  }

  @Override
  public String message() {
    return message;
  }
}
