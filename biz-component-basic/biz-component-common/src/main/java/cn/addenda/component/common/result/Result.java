package cn.addenda.component.common.result;

import lombok.Getter;
import lombok.ToString;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author addenda
 * @since 2022/2/7 16:49
 */
@Getter
@ToString
public class Result<T> {

  public static final String STATUS_OK = "OK";

  public static final String STATUS_FAILED = "FAILED";

  private final T value;

  private final String status;

  private final String errorMsg;

  private Result(String status, T value, String errorMsg) {
    this.status = status;
    this.value = value;
    this.errorMsg = errorMsg;
  }

  public static <T> Result<T> fail() {
    return new Result<>(STATUS_FAILED, null, null);
  }

  public static <T> Result<T> fail(String errorMsg) {
    return new Result<>(STATUS_FAILED, null, errorMsg);
  }

  public static <T> Result<T> success() {
    return new Result<>(STATUS_OK, null, null);
  }

  public static <T> Result<T> success(T value) {
    return new Result<>(STATUS_OK, value, null);
  }

  public static <T> Result<T> success(Supplier<T> supplier) {
    return new Result<>(STATUS_OK, supplier.get(), null);
  }

  public static <R, T> Result<R> success(T value, Function<T, R> function) {
    return new Result<>(STATUS_OK, function.apply(value), null);
  }

  public static <R, T> Result<R> success(Supplier<T> supplier, Function<T, R> function) {
    return new Result<>(STATUS_OK, function.apply(supplier.get()), null);
  }

  public boolean isSuccess() {
    return STATUS_OK.equals(status);
  }

  public boolean isFail() {
    return STATUS_FAILED.equals(status);
  }

}
