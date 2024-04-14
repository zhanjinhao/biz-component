package cn.addenda.component.convention.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author addenda
 * @since 2022/2/7 16:49
 */
@Setter
@Getter
@ToString
public class Result<T> implements Serializable {

  public static final String OK = "OK";

  public static final String FAILED = "FAILED";

  private String reqId;
  private String version;
  private long ts = System.currentTimeMillis();
  private T result;

  /**
   * 请求状态
   */
  private String reqCode;

  /**
   * 请求错误原因
   */
  private String reqMessage;

  public Result() {
  }

  private Result(T result) {
    this.reqCode = OK;
    this.result = result;
  }

  /**
   * 用于简便构建 请求成功&业务成功 时的结果对象
   */
  public static <T> Result<T> success(T result) {
    return new Result<>(result);
  }

  public static <T> Result<T> success(Supplier<T> supplier) {
    return new Result<>(supplier.get());
  }

  public static <R, T> Result<R> success(T result, Function<T, R> function) {
    return new Result<>(function.apply(result));
  }

  public static <R, T> Result<R> success(Supplier<T> supplier, Function<T, R> function) {
    return new Result<>(function.apply(supplier.get()));
  }

}
