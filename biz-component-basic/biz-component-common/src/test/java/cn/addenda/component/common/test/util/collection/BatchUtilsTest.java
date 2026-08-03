package cn.addenda.component.common.test.util.collection;

import cn.addenda.component.common.lambda.ExceptionBiFunction;
import cn.addenda.component.common.lambda.ExceptionFunction;
import cn.addenda.component.common.util.collection.ArrayUtils;
import cn.addenda.component.common.util.collection.BatchUtils;
import cn.addenda.component.common.util.collection.IterableUtils;
import cn.addenda.component.common.util.string.Slf4jUtils;
import cn.addenda.component.stacktrace.StackTraceUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Slf4j
class BatchUtilsTest {

  @Test
  void testList1() {
    BatchUtils.acceptListInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals("[1, 2, 3, 4, 5]", Slf4jUtils.format("{}", objects)));
  }

  @Test
  void testList2() {
    List<String> list = BatchUtils.applyListInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(objects));
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  @Test
  void testList3() {
    BatchUtils.acceptListInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals("[1, 2, 3, 4, 5]", Slf4jUtils.format("{}", objects)),
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testList4() {
    List<String> list = BatchUtils.applyListInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(objects),
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  private final List<String> testList5Result = ArrayUtils.asArrayList("[1]", "[2]", "[3]", "[4]", "[5]");

  @Test
  void testList5() {
    BatchUtils.acceptListInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> {
              Assertions.assertEquals(testList5Result.get(objects.get(0) - 1), objects.toString());
            },
            1,
            StackTraceUtils.getCallerInfo());
  }

  @Test
  void testList6() {
    List<String> list = BatchUtils.applyListInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(objects),
            1,
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  @Test
  void test1() {
    BatchUtils.acceptInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals("[1, 2, 3, 4, 5]", Slf4jUtils.format("{}", objects)));
  }

  @Test
  void test2() {
    Iterable<String> list = BatchUtils.applyInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(IterableUtils.castToList(objects)));
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  @Test
  void test3() {
    BatchUtils.acceptInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals("[1, 2, 3, 4, 5]", Slf4jUtils.format("{}", objects)),
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void test4() {
    Iterable<String> list = BatchUtils.applyInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(IterableUtils.castToList(objects)),
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  private List<String> test5Result = ArrayUtils.asArrayList("[1]", "[2]", "[3]", "[4]", "[5]");

  @Test
  void test5() {
    BatchUtils.acceptInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals(test5Result.get(IterableUtils.castToList(objects).get(0) - 1), objects.toString()),
            1,
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void test6() {
    Iterable<String> list = BatchUtils.applyInBatches(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(IterableUtils.castToList(objects)), 1, StackTraceUtils.getCallerInfo());
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  static List<String> integerToOrderedString(List<Integer> b) {
    if (b == null || b.isEmpty()) {
      return new ArrayList<>();
    }
    return b.stream().map(item -> "第" + item + "个").collect(Collectors.toList());
  }

  @Test
  void testBiList1() {
    BatchUtils.acceptListInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            new BiConsumer<List<String>, List<Integer>>() {
              @Override
              public void accept(List<String> objects1, List<Integer> objects2) {
                Assertions.assertEquals("[a, b][1, 2]", objects1.toString() + objects2.toString());
              }
            });
  }

  @Test
  void testBiList2() {
    List<String> strings = BatchUtils.applyListInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(b1, b2));

    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  @Test
  void testBiList3() {
    BatchUtils.acceptListInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            new BiConsumer<List<String>, List<Integer>>() {
              @Override
              public void accept(List<String> objects1, List<Integer> objects2) {
                Assertions.assertEquals("[a, b][1, 2]", objects1.toString() + objects2.toString());
              }
            },
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testBiList4() {
    List<String> strings = BatchUtils.applyListInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(b1, b2),
            StackTraceUtils.getDetailedCallerInfo());

    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  private List<String> testBiList5Result = ArrayUtils.asArrayList("[a][1]", "[a][2]", "[b][1]", "[b][2]");

  @Test
  void testBiList5() {
    AtomicInteger counter = new AtomicInteger(0);
    BatchUtils.acceptListInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            new BiConsumer<List<String>, List<Integer>>() {
              @Override
              public void accept(List<String> objects1, List<Integer> objects2) {
                Assertions.assertEquals(testBiList5Result.get(counter.getAndIncrement()), objects1.toString() + objects2.toString());
              }
            },
            1,
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testBiList6() {
    List<String> strings = BatchUtils.applyListInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(b1, b2),
            1,
            StackTraceUtils.getDetailedCallerInfo());

    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  @Test
  void testBi1() {
    BatchUtils.acceptInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            new BiConsumer<Iterable<String>, Iterable<Integer>>() {
              @Override
              public void accept(Iterable<String> objects1, Iterable<Integer> objects2) {
                Assertions.assertEquals("[a, b][1, 2]", objects1.toString() + objects2.toString());
              }
            });
  }

  @Test
  void testBi2() {
    Iterable<String> strings = BatchUtils.applyInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(IterableUtils.castToList(b1), IterableUtils.castToList(b2)));

    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  @Test
  void testBi3() {
    BatchUtils.acceptInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            new BiConsumer<Iterable<String>, Iterable<Integer>>() {
              @Override
              public void accept(Iterable<String> objects1, Iterable<Integer> objects2) {
                Assertions.assertEquals("[a, b][1, 2]", objects1.toString() + objects2.toString());
              }
            },
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testBi4() {
    Iterable<String> strings = BatchUtils.applyInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(IterableUtils.castToList(b1), IterableUtils.castToList(b2)),
            StackTraceUtils.getDetailedCallerInfo());

    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  private List<String> testBi5Result = ArrayUtils.asArrayList("[a][1]", "[a][2]", "[b][1]", "[b][2]");

  @Test
  void testBi5() {
    AtomicInteger counter = new AtomicInteger(0);
    BatchUtils.acceptInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            new BiConsumer<Iterable<String>, Iterable<Integer>>() {
              @Override
              public void accept(Iterable<String> objects1, Iterable<Integer> objects2) {
                Assertions.assertEquals(testBi5Result.get(counter.getAndIncrement()), objects1.toString() + objects2.toString());
              }
            },
            1,
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testBi6() {
    Iterable<String> strings = BatchUtils.applyInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(IterableUtils.castToList(b1), IterableUtils.castToList(b2)),
            1,
            StackTraceUtils.getDetailedCallerInfo());

    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  static List<String> b(List<String> b1, List<Integer> b2) {
    if (b1 == null || b1.isEmpty() || b2 == null || b2.isEmpty()) {
      return new ArrayList<>();
    }
    List<String> result = new ArrayList<>();
    for (String b1Item : b1) {
      for (Integer b2Item : b2) {
        result.add(b1Item + b2Item);
      }
    }
    return result;
  }

  // -------------------------
  //  ExceptionConsumer
  // -------------------------

  @Test
  void testList1E() throws Exception {
    BatchUtils.acceptListInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals("[1, 2, 3, 4, 5]", Slf4jUtils.format("{}", objects)));
  }

  @Test
  void testList2E() throws Exception {
    List<String> list = BatchUtils.applyListInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(objects));
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  @Test
  void testList3E() throws Exception {
    BatchUtils.acceptListInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals("[1, 2, 3, 4, 5]", Slf4jUtils.format("{}", objects)),
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testList4E() throws Exception {
    List<String> list = BatchUtils.applyListInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(objects),
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  @Test
  void testList5E() throws Exception {
    BatchUtils.acceptListInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals(testList5Result.get(objects.get(0) - 1), objects.toString()),
            1,
            StackTraceUtils.getCallerInfo());
  }

  @Test
  void testList6E() throws Exception {
    List<String> list = BatchUtils.applyListInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(objects),
            1,
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  // -------------------------
  //  ExceptionConsumer / ExceptionFunction (Iterable)
  // -------------------------

  @Test
  void test1E() throws Exception {
    BatchUtils.acceptInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals("[1, 2, 3, 4, 5]", Slf4jUtils.format("{}", objects)));
  }

  @Test
  void test2E() throws Exception {
    Iterable<String> list = BatchUtils.applyInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(IterableUtils.castToList(objects)));
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  @Test
  void test3E() throws Exception {
    BatchUtils.acceptInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> Assertions.assertEquals("[1, 2, 3, 4, 5]", Slf4jUtils.format("{}", objects)),
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void test4E() throws Exception {
    Iterable<String> list = BatchUtils.applyInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(IterableUtils.castToList(objects)),
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  @Test
  void test5E() throws Exception {
    BatchUtils.acceptInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects ->
                    Assertions.assertEquals(test5Result.get(IterableUtils.castToList(objects).get(0) - 1), objects.toString()),
            1,
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void test6E() throws Exception {
    Iterable<String> list = BatchUtils.applyInBatchesE(Arrays.asList(1, 2, 3, 4, 5),
            objects -> integerToOrderedString(IterableUtils.castToList(objects)),
            1,
            StackTraceUtils.getCallerInfo());
    Assertions.assertEquals("[第1个, 第2个, 第3个, 第4个, 第5个]", list.toString());
  }

  // -------------------------
  //  ExceptionBiConsumer / ExceptionBiFunction (List)
  // -------------------------

  @Test
  void testBiList1E() throws Exception {
    BatchUtils.acceptListInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (objects1, objects2) -> Assertions.assertEquals("[a, b][1, 2]", objects1.toString() + objects2.toString()));
  }

  @Test
  void testBiList2E() throws Exception {
    List<String> strings = BatchUtils.applyListInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(b1, b2));
    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  @Test
  void testBiList3E() throws Exception {
    BatchUtils.acceptListInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (objects1, objects2) -> Assertions.assertEquals("[a, b][1, 2]", objects1.toString() + objects2.toString()),
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testBiList4E() throws Exception {
    List<String> strings = BatchUtils.applyListInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(b1, b2),
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  @Test
  void testBiList5E() throws Exception {
    AtomicInteger counter = new AtomicInteger(0);
    BatchUtils.acceptListInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (objects1, objects2) ->
                    Assertions.assertEquals(testBiList5Result.get(counter.getAndIncrement()), objects1.toString() + objects2.toString()),
            1,
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testBiList6E() throws Exception {
    List<String> strings = BatchUtils.applyListInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(b1, b2),
            1,
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  // -------------------------
  //  ExceptionBiConsumer / ExceptionBiFunction (Iterable)
  // -------------------------

  @Test
  void testBi1E() throws Exception {
    BatchUtils.acceptInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (objects1, objects2) ->
                    Assertions.assertEquals("[a, b][1, 2]", objects1.toString() + objects2.toString()));
  }

  @Test
  void testBi2E() throws Exception {
    Iterable<String> strings = BatchUtils.applyInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(IterableUtils.castToList(b1), IterableUtils.castToList(b2)));
    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  @Test
  void testBi3E() throws Exception {
    BatchUtils.acceptInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (objects1, objects2) ->
                    Assertions.assertEquals("[a, b][1, 2]", objects1.toString() + objects2.toString()),
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testBi4E() throws Exception {
    Iterable<String> strings = BatchUtils.applyInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(IterableUtils.castToList(b1), IterableUtils.castToList(b2)),
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  @Test
  void testBi5E() throws Exception {
    AtomicInteger counter = new AtomicInteger(0);
    BatchUtils.acceptInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (objects1, objects2) ->
                    Assertions.assertEquals(testBi5Result.get(counter.getAndIncrement()), objects1.toString() + objects2.toString()),
            1,
            StackTraceUtils.getDetailedCallerInfo());
  }

  @Test
  void testBi6E() throws Exception {
    Iterable<String> strings = BatchUtils.applyInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (b1, b2) -> b(IterableUtils.castToList(b1), IterableUtils.castToList(b2)),
            1,
            StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[a1, a2, b1, b2]", strings.toString());
  }

  // -------------------------
  //  Exception propagation
  // -------------------------

  @Test
  void testExceptionConsumerThrows() {
    Assertions.assertThrows(Exception.class, () ->
            BatchUtils.acceptInBatchesE(Arrays.asList(1, 2, 3),
                    objects -> {
                      throw new Exception("test");
                    }));
  }

  @Test
  void testExceptionFunctionThrows() {
    Assertions.assertThrows(Exception.class, () ->
            BatchUtils.applyInBatchesE(Arrays.asList(1, 2, 3),
                    (ExceptionFunction<Iterable<Integer>, Iterable<Integer>>) objects -> {
                      throw new Exception("test");
                    }));
  }

  @Test
  void testExceptionBiConsumerThrows() {
    Assertions.assertThrows(Exception.class, () ->
            BatchUtils.acceptInBatchesE(
                    ArrayUtils.asArrayList("a"),
                    ArrayUtils.asArrayList(1),
                    (o1, o2) -> {
                      throw new Exception("test");
                    }));
  }

  @Test
  void testExceptionBiFunctionThrows() {
    Assertions.assertThrows(Exception.class, () ->
            BatchUtils.applyInBatchesE(
                    ArrayUtils.asArrayList("a"),
                    ArrayUtils.asArrayList(1),
                    (ExceptionBiFunction<Iterable<String>, Iterable<Integer>, Iterable<String>>) (o1, o2) -> {
                      throw new Exception("test");
                    }));
  }

  // -------------------------
  //  Function returns null
  // -------------------------

  @Test
  void testFunctionReturnsNull() {
    Iterable<String> list = BatchUtils.applyInBatches(Arrays.asList(1, 2, 3),
            objects -> null);
    Assertions.assertEquals("[]", list.toString());
  }

  @Test
  void testExceptionFunctionReturnsNull() throws Exception {
    Iterable<String> list = BatchUtils.applyInBatchesE(Arrays.asList(1, 2, 3),
            objects -> null);
    Assertions.assertEquals("[]", list.toString());
  }

  @Test
  void testBiFunctionReturnsNull() {
    Iterable<String> list = BatchUtils.applyInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (o1, o2) -> null);
    Assertions.assertEquals("[]", list.toString());
  }

  @Test
  void testExceptionBiFunctionReturnsNull() throws Exception {
    Iterable<String> list = BatchUtils.applyInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (o1, o2) -> null);
    Assertions.assertEquals("[]", list.toString());
  }

  @Test
  void testFunctionReturnsNull1() {
    Iterable<String> list = BatchUtils.applyInBatches(Arrays.asList(1, 2, 3),
            objects -> null, 1, StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[]", list.toString());
  }

  @Test
  void testExceptionFunctionReturnsNull1() throws Exception {
    Iterable<String> list = BatchUtils.applyInBatchesE(Arrays.asList(1, 2, 3),
            objects -> null, 1, StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[]", list.toString());
  }

  @Test
  void testBiFunctionReturnsNull1() {
    Iterable<String> list = BatchUtils.applyInBatches(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (o1, o2) -> null, 1, StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[]", list.toString());
  }

  @Test
  void testExceptionBiFunctionReturnsNull1() throws Exception {
    Iterable<String> list = BatchUtils.applyInBatchesE(
            ArrayUtils.asArrayList("a", "b"),
            ArrayUtils.asArrayList(1, 2),
            (o1, o2) -> null, 1, StackTraceUtils.getDetailedCallerInfo());
    Assertions.assertEquals("[]", list.toString());
  }

}
