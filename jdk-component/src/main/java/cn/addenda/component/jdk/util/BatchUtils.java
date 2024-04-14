package cn.addenda.component.jdk.util;

import cn.addenda.component.lambda.FunctionConverter;
import cn.addenda.component.lambda.NullBiFunction;
import cn.addenda.component.lambda.NullFunction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchUtils {
  private static final int BATCH_SIZE = 100;

  public static <T> void acceptInBatches(Iterable<T> params, Consumer<Iterable<T>> consumer) {
    applyInBatches(params, FunctionConverter.toFunction(consumer), BATCH_SIZE, null);
  }

  public static <R, T> List<R> applyInBatches(Iterable<T> params, Function<Iterable<T>, Iterable<R>> function) {
    return applyInBatches(params, function, BATCH_SIZE, null);
  }

  public static <R, T> List<R> applyInBatches(List<T> params, Function<Iterable<T>, Iterable<R>> function) {
    return applyInBatches(params, function, BATCH_SIZE, null);
  }

  public static <T> void acceptInBatches(Iterable<T> params, Consumer<Iterable<T>> consumer, int batchSize) {
    applyInBatches(params, FunctionConverter.toFunction(consumer), batchSize, null);
  }

  public static <R, T> List<R> applyInBatches(Iterable<T> params, Function<Iterable<T>, Iterable<R>> function, int batchSize) {
    return applyInBatches(params, function, batchSize, null);
  }

  public static <T> void acceptInBatches(Iterable<T> params, Consumer<Iterable<T>> consumer, String name) {
    applyInBatches(params, FunctionConverter.toFunction(consumer), BATCH_SIZE, name);
  }

  public static <R, T> List<R> applyInBatches(Iterable<T> params, Function<Iterable<T>, Iterable<R>> function, String name) {
    return applyInBatches(params, function, BATCH_SIZE, name);
  }

  public static <T> void acceptInBatches(Iterable<T> params, Consumer<Iterable<T>> consumer, int batchSize, String name) {
    applyInBatches(params, FunctionConverter.toFunction(consumer), batchSize, name);
  }

  /**
   * @param params    参数集合
   * @param function  参数集合映射到结果集合的函数
   * @param batchSize 一次处理多少参数
   * @param name      给当前操作起个名字，方便排查问题
   * @return 结果
   */
  public static <R, T> List<R> applyInBatches(Iterable<T> params, Function<Iterable<T>, Iterable<R>> function, int batchSize, String name) {
    Assert.isTrue(batchSize > 0, "`batchSize` should be greater than !`0");
    if (params == null) {
      return new ArrayList<>();
    }
    long start = System.currentTimeMillis();
    List<R> result = new ArrayList<>();
    List<List<T>> paramsList = IterableUtils.splitToList(params, batchSize);
    for (int i = 0; i < paramsList.size(); i++) {
      List<T> paramSeg = paramsList.get(i);
      log.debug("Seg-{}-param: {}", i, Arrays.deepToString(paramSeg.toArray()));
      Iterable<R> resultSeg = function.apply(paramSeg);
      if (!(function instanceof NullFunction)) {
        String a = Optional.ofNullable(resultSeg)
                .map(k -> Arrays.deepToString(IterableUtils.toArrayList(k).toArray()))
                .orElse(null);
        log.debug("Seg-{}-result: {}", i, a);
      }
      if (resultSeg != null) {
        resultSeg.forEach(result::add);
      }
    }
    if (name != null) {
      log.info("applyInBatches [{}] operation execute [{}] ms. ", name, System.currentTimeMillis() - start);
    } else {
      log.info("applyInBatches operation execute [{}] ms. ", System.currentTimeMillis() - start);
    }
    return result;
  }


  public static <T1, T2> void acceptInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiConsumer<Iterable<T1>, Iterable<T2>> consumer) {
    applyInBatches(param1s, param2s, FunctionConverter.toBiFunction(consumer), BATCH_SIZE, null);
  }


  public static <R, T1, T2> List<R> applyInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> function) {
    return applyInBatches(param1s, param2s, function, BATCH_SIZE, null);
  }

  public static <T1, T2> void acceptInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiConsumer<Iterable<T1>, Iterable<T2>> consumer, int batchSize) {
    applyInBatches(param1s, param2s, FunctionConverter.toBiFunction(consumer), batchSize, null);
  }

  public static <R, T1, T2> List<R> applyInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> function, int batchSize) {
    return applyInBatches(param1s, param2s, function, batchSize, null);
  }

  public static <T1, T2> void acceptInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiConsumer<Iterable<T1>, Iterable<T2>> consumer, String name) {
    applyInBatches(param1s, param2s, FunctionConverter.toBiFunction(consumer), BATCH_SIZE, name);
  }

  public static <R, T1, T2> List<R> applyInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> function, String name) {
    return applyInBatches(param1s, param2s, function, BATCH_SIZE, name);
  }

  public static <T1, T2> void acceptInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiConsumer<Iterable<T1>, Iterable<T2>> consumer, int batchSize, String name) {
    applyInBatches(param1s, param2s, FunctionConverter.toBiFunction(consumer), batchSize, name);
  }

  public static <R, T1, T2> List<R> applyInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> function, int batchSize, String name) {
    Assert.isTrue(batchSize > 0, "`batchSize` should be greater than !`0");
    if (param1s == null || param2s == null) {
      return new ArrayList<>();
    }
    long start = System.currentTimeMillis();
    List<R> result = new ArrayList<>();
    List<List<T1>> param1sList = IterableUtils.splitToList(param1s, batchSize);
    for (int i = 0; i < param1sList.size(); i++) {
      List<T1> param1Seg = param1sList.get(i);
      List<List<T2>> param2sList = IterableUtils.splitToList(param2s, batchSize);
      for (int j = 0; j < param2sList.size(); j++) {
        List<T2> param2Seg = param2sList.get(j);
        log.debug("Seg-{}-{}-param: {}, {}", i, j, Arrays.deepToString(param1Seg.toArray()), Arrays.deepToString(param2Seg.toArray()));
        Iterable<R> resultSeg = function.apply(param1Seg, param2Seg);
        if (!(function instanceof NullBiFunction)) {
          String a = Optional.ofNullable(resultSeg)
                  .map(k -> Arrays.deepToString(IterableUtils.toArrayList(k).toArray()))
                  .orElse(null);
          log.debug("Seg-{}-result: {}", i, a);
        }
        if (resultSeg != null) {
          resultSeg.forEach(result::add);
        }
      }
    }
    if (name != null) {
      log.info("applyInBatches {} operation execute [{}] ms. ", name, System.currentTimeMillis() - start);
    } else {
      log.info("applyInBatches operation execute [{}] ms. ", System.currentTimeMillis() - start);
    }
    return result;
  }

}
