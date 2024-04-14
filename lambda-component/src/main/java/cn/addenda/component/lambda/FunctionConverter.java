package cn.addenda.component.lambda;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.function.*;

/**
 * @author addenda
 * @since 2023/6/4 15:01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionConverter {

  public static <T, R> Function<T, R> toFunction(Consumer<T> consumer) {
    return new NullFunction<>(consumer);
  }

  public static <T, U, R> BiFunction<T, U, R> toBiFunction(BiConsumer<T, U> biConsumer) {
    return new NullBiFunction<>(biConsumer);
  }

  public static <T> Supplier<T> toSupplier(Runnable runnable) {
    return new NullSupplier<>(runnable);
  }

  public static <T, R> TFunction<T, R> toTFunction(TConsumer<T> tConsumer) {
    return new NullTFunction<>(tConsumer);
  }

  public static <T, U, R> TBiFunction<T, U, R> toTBiFunction(TBiConsumer<T, U> tBiConsumer) {
    return new NullTBiFunction<>(tBiConsumer);
  }

  public static <T> TSupplier<T> toTSupplier(TRunnable tRunnable) {
    return new NullTSupplier<>(tRunnable);
  }

  public static Runnable toRunnable(TRunnable tRunnable) {
    return new Runnable() {
      @Override
      @SneakyThrows
      public void run() {
        tRunnable.run();
      }
    };
  }

  public static <T> Supplier<T> toSupplier(TSupplier<T> tSupplier) {
    return new Supplier<T>() {
      @Override
      @SneakyThrows
      public T get() {
        return tSupplier.get();
      }
    };
  }

  public static <T> Consumer<T> toConsumer(TConsumer<T> tConsumer) {
    return new Consumer<T>() {
      @Override
      @SneakyThrows
      public void accept(T t) {
        tConsumer.accept(t);
      }
    };
  }

  public static <T, R> Function<T, R> toFunction(TFunction<T, R> tFunction) {
    return new Function<T, R>() {
      @Override
      @SneakyThrows
      public R apply(T t) {
        return tFunction.apply(t);
      }
    };
  }

}
