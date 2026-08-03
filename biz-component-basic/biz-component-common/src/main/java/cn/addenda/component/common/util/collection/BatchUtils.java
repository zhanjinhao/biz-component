package cn.addenda.component.common.util.collection;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import cn.addenda.component.common.lambda.ExceptionBiConsumer;
import cn.addenda.component.common.lambda.ExceptionBiFunction;
import cn.addenda.component.common.lambda.ExceptionConsumer;
import cn.addenda.component.common.lambda.ExceptionFunction;
import cn.addenda.component.common.lambda.FunctionConverter;
import cn.addenda.component.common.util.AssertUtils;
import cn.addenda.component.stacktrace.StackTraceUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchUtils {

  private static final int BATCH_SIZE = 100;
  private static final String BATCH_SIZE_MSG = "`batchSize` should be greater than `0";
  private static final String LOG_SEG_PARAM = "applyInBatches [{}] batchId:[{}] SegParam-{}: {}";
  private static final String LOG_SEG_RESULT = "applyInBatches [{}] batchId:[{}] SegResult-{}: {}";
  private static final String LOG_SEG_PARAM_BI = "applyInBatches [{}] batchId:[{}] SegParam-{}-{}: [{}], [{}]";
  private static final String LOG_SEG_RESULT_BI = "applyInBatches [{}] batchId:[{}] SegResult-{}-{}: [{}]";
  private static final String LOG_EXEC = "applyInBatches [{}] batchId:[{}] operation execute [{}] ms. ";

  // -------------------------
  //  Consumer
  // -------------------------

  public static <T> void acceptListInBatches(
          List<T> params, Consumer<List<T>> consumer) {
    acceptInBatches(params, iterable -> consumer.accept(IterableUtils.castToList(iterable)));
  }

  public static <T> void acceptListInBatches(
          List<T> params, Consumer<List<T>> consumer, String name) {
    acceptInBatches(params, iterable -> consumer.accept(IterableUtils.castToList(iterable)), name);
  }

  public static <T> void acceptListInBatches(
          List<T> params, Consumer<List<T>> consumer, int batchSize, String name) {
    acceptInBatches(params, iterable -> consumer.accept(IterableUtils.castToList(iterable)), batchSize, name);
  }

  public static <T> void acceptInBatches(
          Iterable<T> params, Consumer<Iterable<T>> consumer) {
    applyInBatches(params, FunctionConverter.toNullFunction(consumer));
  }

  public static <T> void acceptInBatches(
          Iterable<T> params, Consumer<Iterable<T>> consumer, String name) {
    applyInBatches(params, FunctionConverter.toNullFunction(consumer), name);
  }

  public static <T> void acceptInBatches(
          Iterable<T> params, Consumer<Iterable<T>> consumer, int batchSize, String name) {
    applyInBatches(params, FunctionConverter.toNullFunction(consumer), batchSize, name);
  }

  // -------------------------
  //  Function
  // -------------------------

  public static <R, T> List<R> applyListInBatches(
          List<T> params, Function<List<T>, List<R>> function) {
    Function<Iterable<T>, Iterable<R>> iterableFunction = ts -> function.apply(IterableUtils.castToList(ts));
    return IterableUtils.castToList(applyInBatches(params, iterableFunction));
  }

  public static <R, T> List<R> applyListInBatches(
          List<T> params, Function<List<T>, List<R>> function, String name) {
    Function<Iterable<T>, Iterable<R>> iterableFunction = ts -> function.apply(IterableUtils.castToList(ts));
    return IterableUtils.castToList(applyInBatches(params, iterableFunction, name));
  }

  public static <R, T> List<R> applyListInBatches(
          List<T> params, Function<List<T>, List<R>> function, int batchSize, String name) {
    Function<Iterable<T>, Iterable<R>> iterableFunction = ts -> function.apply(IterableUtils.castToList(ts));
    return IterableUtils.castToList(applyInBatches(params, iterableFunction, batchSize, name));
  }

  public static <R, T> Iterable<R> applyInBatches(
          Iterable<T> params, Function<Iterable<T>, Iterable<R>> function) {
    return applyInBatches(params, function, BATCH_SIZE, null);
  }

  public static <R, T> Iterable<R> applyInBatches(
          Iterable<T> params, Function<Iterable<T>, Iterable<R>> function, String name) {
    return applyInBatches(params, function, BATCH_SIZE, name);
  }

  public static <R, T> Iterable<R> applyInBatches(
          Iterable<T> params, Function<Iterable<T>, Iterable<R>> function, int batchSize, String name) {
    AssertUtils.isTrue(batchSize > 0, BATCH_SIZE_MSG);
    if (params == null) {
      return new ArrayList<>();
    }
    if (name == null) {
      name = StackTraceUtils.getCallerInfo();
    }
    String batchId = UUID.randomUUID().toString().replace("-", "");
    long start = System.currentTimeMillis();
    List<R> result = new ArrayList<>();
    List<List<T>> paramsList = IterableUtils.splitToListList(params, batchSize);
    boolean isNullFunction = function instanceof FunctionConverter.NullFunction;
    for (int i = 0; i < paramsList.size(); i++) {
      List<T> paramSeg = paramsList.get(i);
      if (log.isDebugEnabled()) {
        log.debug(LOG_SEG_PARAM, name, batchId, i, JacksonUtils.toStr(paramSeg));
      }
      Iterable<R> resultSeg = function.apply(paramSeg);
      if (!isNullFunction && log.isDebugEnabled()) {
        log.debug(LOG_SEG_RESULT, name, batchId, i, JacksonUtils.toStr(resultSeg));
      }
      if (resultSeg != null) {
        resultSeg.forEach(result::add);
      }
    }
    log.info(LOG_EXEC, name, batchId, System.currentTimeMillis() - start);
    return result;
  }

  // -------------------------
  //  BiConsumer
  // -------------------------

  public static <T1, T2> void acceptListInBatches(
          List<T1> param1s, List<T2> param2s, BiConsumer<List<T1>, List<T2>> biConsumer) {
    acceptInBatches(param1s, param2s, (ts1, ts2) -> biConsumer.accept(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2)));
  }

  public static <T1, T2> void acceptListInBatches(
          List<T1> param1s, List<T2> param2s, BiConsumer<List<T1>, List<T2>> biConsumer, String name) {
    acceptInBatches(param1s, param2s, (ts1, ts2) -> biConsumer.accept(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2)), name);
  }

  public static <T1, T2> void acceptListInBatches(
          List<T1> param1s, List<T2> param2s, BiConsumer<List<T1>, List<T2>> biConsumer, int batchSize, String name) {
    acceptInBatches(param1s, param2s, (ts1, ts2) -> biConsumer.accept(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2)), batchSize, name);
  }

  public static <T1, T2> void acceptInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiConsumer<Iterable<T1>, Iterable<T2>> biConsumer) {
    applyInBatches(param1s, param2s, FunctionConverter.toNullBiFunction(biConsumer));
  }

  public static <T1, T2> void acceptInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiConsumer<Iterable<T1>, Iterable<T2>> biConsumer, String name) {
    applyInBatches(param1s, param2s, FunctionConverter.toNullBiFunction(biConsumer), name);
  }

  public static <T1, T2> void acceptInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiConsumer<Iterable<T1>, Iterable<T2>> biConsumer, int batchSize, String name) {
    applyInBatches(param1s, param2s, FunctionConverter.toNullBiFunction(biConsumer), batchSize, name);
  }

  // -------------------------
  //  BiFunction
  // -------------------------

  public static <R, T1, T2> List<R> applyListInBatches(
          List<T1> param1s, List<T2> param2s, BiFunction<List<T1>, List<T2>, List<R>> biFunction) {
    BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> iterableBiFunction = (ts1, ts2) -> biFunction.apply(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2));
    return IterableUtils.castToList(applyInBatches(param1s, param2s, iterableBiFunction));
  }

  public static <R, T1, T2> List<R> applyListInBatches(
          List<T1> param1s, List<T2> param2s, BiFunction<List<T1>, List<T2>, List<R>> biFunction, String name) {
    BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> iterableBiFunction = (ts1, ts2) -> biFunction.apply(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2));
    return IterableUtils.castToList(applyInBatches(param1s, param2s, iterableBiFunction, name));
  }

  public static <R, T1, T2> List<R> applyListInBatches(
          List<T1> param1s, List<T2> param2s, BiFunction<List<T1>, List<T2>, List<R>> biFunction, int batchSize, String name) {
    BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> iterableBiFunction = (ts1, ts2) -> biFunction.apply(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2));
    return IterableUtils.castToList(applyInBatches(param1s, param2s, iterableBiFunction, batchSize, name));
  }

  public static <R, T1, T2> Iterable<R> applyInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> biFunction) {
    return applyInBatches(param1s, param2s, biFunction, BATCH_SIZE, null);
  }

  public static <R, T1, T2> Iterable<R> applyInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> biFunction, String name) {
    return applyInBatches(param1s, param2s, biFunction, BATCH_SIZE, name);
  }

  public static <R, T1, T2> Iterable<R> applyInBatches(
          Iterable<T1> param1s, Iterable<T2> param2s, BiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> biFunction, int batchSize, String name) {
    AssertUtils.isTrue(batchSize > 0, BATCH_SIZE_MSG);
    if (param1s == null || param2s == null) {
      return new ArrayList<>();
    }
    if (name == null) {
      name = StackTraceUtils.getCallerInfo();
    }
    String batchId = UUID.randomUUID().toString().replace("-", "");
    long start = System.currentTimeMillis();
    List<R> result = new ArrayList<>();
    List<List<T1>> param1sList = IterableUtils.splitToListList(param1s, batchSize);
    List<List<T2>> param2sList = IterableUtils.splitToListList(param2s, batchSize);
    boolean isNullBiFunction = biFunction instanceof FunctionConverter.NullBiFunction;
    for (int i = 0; i < param1sList.size(); i++) {
      List<T1> param1Seg = param1sList.get(i);
      for (int j = 0; j < param2sList.size(); j++) {
        List<T2> param2Seg = param2sList.get(j);
        if (log.isDebugEnabled()) {
          log.debug(LOG_SEG_PARAM_BI, name, batchId, i, j, JacksonUtils.toStr(param1Seg), JacksonUtils.toStr(param2Seg));
        }
        Iterable<R> resultSeg = biFunction.apply(param1Seg, param2Seg);
        if (!isNullBiFunction && log.isDebugEnabled()) {
          log.debug(LOG_SEG_RESULT_BI, name, batchId, i, j, JacksonUtils.toStr(resultSeg));
        }
        if (resultSeg != null) {
          resultSeg.forEach(result::add);
        }
      }
    }
    log.info(LOG_EXEC, name, batchId, System.currentTimeMillis() - start);
    return result;
  }

  // -------------------------
  //  ExceptionConsumer (E-suffix)
  // -------------------------

  public static <T> void acceptListInBatchesE(
          List<T> params, ExceptionConsumer<List<T>> consumer)
          throws Exception {
    acceptInBatchesE(params, iterable -> consumer.accept(IterableUtils.castToList(iterable)));
  }

  public static <T> void acceptListInBatchesE(
          List<T> params, ExceptionConsumer<List<T>> consumer, String name)
          throws Exception {
    acceptInBatchesE(params, iterable -> consumer.accept(IterableUtils.castToList(iterable)), name);
  }

  public static <T> void acceptListInBatchesE(
          List<T> params, ExceptionConsumer<List<T>> consumer, int batchSize, String name)
          throws Exception {
    acceptInBatchesE(params, iterable -> consumer.accept(IterableUtils.castToList(iterable)), batchSize, name);
  }

  public static <T> void acceptInBatchesE(
          Iterable<T> params, ExceptionConsumer<Iterable<T>> consumer)
          throws Exception {
    applyInBatchesE(params, FunctionConverter.toNullExceptionFunction(consumer), BATCH_SIZE, null);
  }

  public static <T> void acceptInBatchesE(
          Iterable<T> params, ExceptionConsumer<Iterable<T>> consumer, String name)
          throws Exception {
    applyInBatchesE(params, FunctionConverter.toNullExceptionFunction(consumer), BATCH_SIZE, name);
  }

  public static <T> void acceptInBatchesE(
          Iterable<T> params, ExceptionConsumer<Iterable<T>> consumer, int batchSize, String name)
          throws Exception {
    applyInBatchesE(params, FunctionConverter.toNullExceptionFunction(consumer), batchSize, name);
  }

  // -------------------------
  //  ExceptionFunction (E-suffix)
  // -------------------------

  public static <R, T> List<R> applyListInBatchesE(
          List<T> params, ExceptionFunction<List<T>, List<R>> function)
          throws Exception {
    ExceptionFunction<Iterable<T>, Iterable<R>> iterableFunction = ts -> function.apply(IterableUtils.castToList(ts));
    return IterableUtils.castToList(applyInBatchesE(params, iterableFunction));
  }

  public static <R, T> List<R> applyListInBatchesE(
          List<T> params, ExceptionFunction<List<T>, List<R>> function, String name)
          throws Exception {
    ExceptionFunction<Iterable<T>, Iterable<R>> iterableFunction = ts -> function.apply(IterableUtils.castToList(ts));
    return IterableUtils.castToList(applyInBatchesE(params, iterableFunction, name));
  }

  public static <R, T> List<R> applyListInBatchesE(
          List<T> params, ExceptionFunction<List<T>, List<R>> function, int batchSize, String name)
          throws Exception {
    ExceptionFunction<Iterable<T>, Iterable<R>> iterableFunction = ts -> function.apply(IterableUtils.castToList(ts));
    return IterableUtils.castToList(applyInBatchesE(params, iterableFunction, batchSize, name));
  }

  public static <R, T> Iterable<R> applyInBatchesE(
          Iterable<T> params, ExceptionFunction<Iterable<T>, Iterable<R>> function)
          throws Exception {
    return applyInBatchesE(params, function, BATCH_SIZE, null);
  }

  public static <R, T> Iterable<R> applyInBatchesE(
          Iterable<T> params, ExceptionFunction<Iterable<T>, Iterable<R>> function, String name)
          throws Exception {
    return applyInBatchesE(params, function, BATCH_SIZE, name);
  }

  public static <R, T> Iterable<R> applyInBatchesE(
          Iterable<T> params, ExceptionFunction<Iterable<T>, Iterable<R>> function, int batchSize, String name)
          throws Exception {
    AssertUtils.isTrue(batchSize > 0, BATCH_SIZE_MSG);
    if (params == null) {
      return new ArrayList<>();
    }
    if (name == null) {
      name = StackTraceUtils.getCallerInfo();
    }
    String batchId = UUID.randomUUID().toString().replace("-", "");
    long start = System.currentTimeMillis();
    List<R> result = new ArrayList<>();
    List<List<T>> paramsList = IterableUtils.splitToListList(params, batchSize);
    boolean isNullFunction = function instanceof FunctionConverter.NullExceptionFunction;
    for (int i = 0; i < paramsList.size(); i++) {
      List<T> paramSeg = paramsList.get(i);
      if (log.isDebugEnabled()) {
        log.debug(LOG_SEG_PARAM, name, batchId, i, JacksonUtils.toStr(paramSeg));
      }
      Iterable<R> resultSeg = function.apply(paramSeg);
      if (!isNullFunction && log.isDebugEnabled()) {
        log.debug(LOG_SEG_RESULT, name, batchId, i, JacksonUtils.toStr(resultSeg));
      }
      if (resultSeg != null) {
        resultSeg.forEach(result::add);
      }
    }
    log.info(LOG_EXEC, name, batchId, System.currentTimeMillis() - start);
    return result;
  }

  // -------------------------
  //  ExceptionBiConsumer (E-suffix)
  // -------------------------

  public static <T1, T2> void acceptListInBatchesE(
          List<T1> param1s, List<T2> param2s, ExceptionBiConsumer<List<T1>, List<T2>> biConsumer)
          throws Exception {
    acceptInBatchesE(param1s, param2s, (ts1, ts2) -> biConsumer.accept(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2)));
  }

  public static <T1, T2> void acceptListInBatchesE(
          List<T1> param1s, List<T2> param2s, ExceptionBiConsumer<List<T1>, List<T2>> biConsumer, String name)
          throws Exception {
    acceptInBatchesE(param1s, param2s, (ts1, ts2) -> biConsumer.accept(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2)), name);
  }

  public static <T1, T2> void acceptListInBatchesE(
          List<T1> param1s, List<T2> param2s, ExceptionBiConsumer<List<T1>, List<T2>> biConsumer, int batchSize, String name)
          throws Exception {
    acceptInBatchesE(param1s, param2s, (ts1, ts2) -> biConsumer.accept(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2)), batchSize, name);
  }

  public static <T1, T2> void acceptInBatchesE(
          Iterable<T1> param1s, Iterable<T2> param2s, ExceptionBiConsumer<Iterable<T1>, Iterable<T2>> biConsumer)
          throws Exception {
    applyInBatchesE(param1s, param2s, FunctionConverter.toNullExceptionBiFunction(biConsumer), BATCH_SIZE, null);
  }

  public static <T1, T2> void acceptInBatchesE(
          Iterable<T1> param1s, Iterable<T2> param2s, ExceptionBiConsumer<Iterable<T1>, Iterable<T2>> biConsumer, String name)
          throws Exception {
    applyInBatchesE(param1s, param2s, FunctionConverter.toNullExceptionBiFunction(biConsumer), BATCH_SIZE, name);
  }

  public static <T1, T2> void acceptInBatchesE(
          Iterable<T1> param1s, Iterable<T2> param2s, ExceptionBiConsumer<Iterable<T1>, Iterable<T2>> biConsumer, int batchSize, String name)
          throws Exception {
    applyInBatchesE(param1s, param2s, FunctionConverter.toNullExceptionBiFunction(biConsumer), batchSize, name);
  }

  // -------------------------
  //  ExceptionBiFunction (E-suffix)
  // -------------------------

  public static <R, T1, T2> List<R> applyListInBatchesE(
          List<T1> param1s, List<T2> param2s, ExceptionBiFunction<List<T1>, List<T2>, List<R>> biFunction)
          throws Exception {
    ExceptionBiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> iterableBiFunction = (ts1, ts2) -> biFunction.apply(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2));
    return IterableUtils.castToList(applyInBatchesE(param1s, param2s, iterableBiFunction));
  }

  public static <R, T1, T2> List<R> applyListInBatchesE(
          List<T1> param1s, List<T2> param2s, ExceptionBiFunction<List<T1>, List<T2>, List<R>> biFunction, String name)
          throws Exception {
    ExceptionBiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> iterableBiFunction = (ts1, ts2) -> biFunction.apply(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2));
    return IterableUtils.castToList(applyInBatchesE(param1s, param2s, iterableBiFunction, name));
  }

  public static <R, T1, T2> List<R> applyListInBatchesE(
          List<T1> param1s, List<T2> param2s, ExceptionBiFunction<List<T1>, List<T2>, List<R>> biFunction, int batchSize, String name)
          throws Exception {
    ExceptionBiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> iterableBiFunction = (ts1, ts2) -> biFunction.apply(IterableUtils.castToList(ts1), IterableUtils.castToList(ts2));
    return IterableUtils.castToList(applyInBatchesE(param1s, param2s, iterableBiFunction, batchSize, name));
  }

  public static <R, T1, T2> Iterable<R> applyInBatchesE(
          Iterable<T1> param1s, Iterable<T2> param2s, ExceptionBiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> biFunction)
          throws Exception {
    return applyInBatchesE(param1s, param2s, biFunction, BATCH_SIZE, null);
  }

  public static <R, T1, T2> Iterable<R> applyInBatchesE(
          Iterable<T1> param1s, Iterable<T2> param2s, ExceptionBiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> biFunction, String name)
          throws Exception {
    return applyInBatchesE(param1s, param2s, biFunction, BATCH_SIZE, name);
  }

  public static <R, T1, T2> Iterable<R> applyInBatchesE(
          Iterable<T1> param1s, Iterable<T2> param2s, ExceptionBiFunction<Iterable<T1>, Iterable<T2>, Iterable<R>> biFunction, int batchSize, String name)
          throws Exception {
    AssertUtils.isTrue(batchSize > 0, BATCH_SIZE_MSG);
    if (param1s == null || param2s == null) {
      return new ArrayList<>();
    }
    if (name == null) {
      name = StackTraceUtils.getCallerInfo();
    }
    String batchId = UUID.randomUUID().toString().replace("-", "");
    long start = System.currentTimeMillis();
    List<R> result = new ArrayList<>();
    List<List<T1>> param1sList = IterableUtils.splitToListList(param1s, batchSize);
    List<List<T2>> param2sList = IterableUtils.splitToListList(param2s, batchSize);
    boolean isNullBiFunction = biFunction instanceof FunctionConverter.NullExceptionBiFunction;
    for (int i = 0; i < param1sList.size(); i++) {
      List<T1> param1Seg = param1sList.get(i);
      for (int j = 0; j < param2sList.size(); j++) {
        List<T2> param2Seg = param2sList.get(j);
        if (log.isDebugEnabled()) {
          log.debug(LOG_SEG_PARAM_BI, name, batchId, i, j, JacksonUtils.toStr(param1Seg), JacksonUtils.toStr(param2Seg));
        }
        Iterable<R> resultSeg = biFunction.apply(param1Seg, param2Seg);
        if (!isNullBiFunction && log.isDebugEnabled()) {
          log.debug(LOG_SEG_RESULT_BI, name, batchId, i, j, JacksonUtils.toStr(resultSeg));
        }
        if (resultSeg != null) {
          resultSeg.forEach(result::add);
        }
      }
    }
    log.info(LOG_EXEC, name, batchId, System.currentTimeMillis() - start);
    return result;
  }

}
