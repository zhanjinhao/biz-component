package cn.addenda.component.convention.exception.remote;

public class RemoteErrorCode implements IRemoteErrorCode {

  private final String message;

  public RemoteErrorCode(String message) {
    this.message = message;
  }

  @Override
  public String code() {
    return "remote";
  }

  @Override
  public String message() {
    return message;
  }
}
