package cn.addenda.component.common.util.collection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author addenda
 * @since 2022/2/7 12:37
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayUtils {

  /**
   * 可变参转 {@link ArrayList}。
   *
   * @param objs 元素
   * @param <T>  元素类型
   * @return 包含传入元素的集合，不会返回 {@code null}
   */
  @SafeVarargs
  public static <T> ArrayList<T> asArrayList(T... objs) {
    ArrayList<T> list = new ArrayList<>();
    Collections.addAll(list, objs);
    return list;
  }

  /**
   * 可变参转 {@link LinkedList}。
   *
   * @param objs 元素
   * @param <T>  元素类型
   * @return 包含传入元素的集合，不会返回 {@code null}
   */
  @SafeVarargs
  public static <T> LinkedList<T> asLinkedList(T... objs) {
    LinkedList<T> list = new LinkedList<>();
    Collections.addAll(list, objs);
    return list;
  }

  /**
   * 可变参转 {@link HashSet}。
   *
   * @param objs 元素
   * @param <T>  元素类型
   * @return 包含传入元素的集合，不会返回 {@code null}
   */
  @SafeVarargs
  public static <T> HashSet<T> asHashSet(T... objs) {
    HashSet<T> set = new HashSet<>();
    Collections.addAll(set, objs);
    return set;
  }

  /**
   * 可变参转 {@link TreeSet}（使用指定比较器）。
   * <p>使用{@link CompareUtils#nullLastCompare(Comparable, Comparable)}或{@link CompareUtils#nullFirstCompare(Comparable, Comparable)}构造comparator可以避免NPE
   *
   * @param comparator 比较器
   * @param objs       元素
   * @param <T>        元素类型
   * @return 包含传入元素的集合，不会返回 {@code null}
   */
  @SafeVarargs
  public static <T> TreeSet<T> asTreeSet(Comparator<T> comparator, T... objs) {
    TreeSet<T> set = new TreeSet<>(comparator);
    Collections.addAll(set, objs);
    return set;
  }

  /**
   * 可变参转 {@link TreeSet}（自然顺序）。
   *
   * @param objs 元素
   * @param <T>  元素类型
   * @return 包含传入元素的集合，不会返回 {@code null}
   */
  @SafeVarargs
  public static <T extends Comparable<? super T>> TreeSet<T> asTreeSet(T... objs) {
    TreeSet<T> set = new TreeSet<>();
    Collections.addAll(set, objs);
    return set;
  }

  // ==================== ofStream (包装类型)：null 安全，返回空流 ====================

  /**
   * 数组转 Stream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @param <T>   元素类型
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static <T> Stream<T> ofStream(T[] array) {
    return array == null ? Stream.empty() : Arrays.stream(array);
  }

  /**
   * 原始类型数组转包装类型 Stream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static Stream<Byte> ofStream(byte[] array) {
    return array == null ? Stream.empty() : IntStream.range(0, array.length).mapToObj(i -> array[i]);
  }

  /**
   * 原始类型数组转包装类型 Stream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static Stream<Short> ofStream(short[] array) {
    return array == null ? Stream.empty() : IntStream.range(0, array.length).mapToObj(i -> array[i]);
  }

  /**
   * 原始类型数组转包装类型 Stream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static Stream<Integer> ofStream(int[] array) {
    return array == null ? Stream.empty() : Arrays.stream(array).boxed();
  }

  /**
   * 原始类型数组转包装类型 Stream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static Stream<Long> ofStream(long[] array) {
    return array == null ? Stream.empty() : Arrays.stream(array).boxed();
  }

  /**
   * 原始类型数组转包装类型 Stream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static Stream<Character> ofStream(char[] array) {
    return array == null ? Stream.empty() : IntStream.range(0, array.length).mapToObj(i -> array[i]);
  }

  /**
   * 原始类型数组转包装类型 Stream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static Stream<Float> ofStream(float[] array) {
    return array == null ? Stream.empty() : IntStream.range(0, array.length).mapToObj(i -> array[i]);
  }

  /**
   * 原始类型数组转包装类型 Stream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static Stream<Double> ofStream(double[] array) {
    return array == null ? Stream.empty() : Arrays.stream(array).boxed();
  }

  /**
   * 原始类型数组转包装类型 Stream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static Stream<Boolean> ofStream(boolean[] array) {
    return array == null ? Stream.empty() : IntStream.range(0, array.length).mapToObj(i -> array[i]);
  }

  // ==================== ofIntStream / ofLongStream / ofDoubleStream：null 安全，返回空流 ====================

  /**
   * 原始类型数组转 IntStream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static IntStream ofIntStream(int[] array) {
    return array == null ? IntStream.empty() : Arrays.stream(array);
  }

  /**
   * 原始类型数组转 LongStream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static LongStream ofLongStream(long[] array) {
    return array == null ? LongStream.empty() : Arrays.stream(array);
  }

  /**
   * 原始类型数组转 DoubleStream（null 安全，null 返空 Stream）。
   *
   * @param array 数组，{@code null} 视为空
   * @return 数组对应的流，数组为 {@code null} 时返回空流
   */
  public static DoubleStream ofDoubleStream(double[] array) {
    return array == null ? DoubleStream.empty() : Arrays.stream(array);
  }

  /**
   * 数组是否为空（{@code null} 或 length == 0）。
   *
   * @param array 数组
   * @return 是否为 {@code null} 或空数组
   */
  public static boolean isEmpty(final Object[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 数组是否为空（{@code null} 或 length == 0）。
   *
   * @param array 数组
   * @return 是否为 {@code null} 或空数组
   */
  public static boolean isEmpty(final byte[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 数组是否为空（{@code null} 或 length == 0）。
   *
   * @param array 数组
   * @return 是否为 {@code null} 或空数组
   */
  public static boolean isEmpty(final short[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 数组是否为空（{@code null} 或 length == 0）。
   *
   * @param array 数组
   * @return 是否为 {@code null} 或空数组
   */
  public static boolean isEmpty(final int[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 数组是否为空（{@code null} 或 length == 0）。
   *
   * @param array 数组
   * @return 是否为 {@code null} 或空数组
   */
  public static boolean isEmpty(final long[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 数组是否为空（{@code null} 或 length == 0）。
   *
   * @param array 数组
   * @return 是否为 {@code null} 或空数组
   */
  public static boolean isEmpty(final char[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 数组是否为空（{@code null} 或 length == 0）。
   *
   * @param array 数组
   * @return 是否为 {@code null} 或空数组
   */
  public static boolean isEmpty(final float[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 数组是否为空（{@code null} 或 length == 0）。
   *
   * @param array 数组
   * @return 是否为 {@code null} 或空数组
   */
  public static boolean isEmpty(final double[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 数组是否为空（{@code null} 或 length == 0）。
   *
   * @param array 数组
   * @return 是否为 {@code null} 或空数组
   */
  public static boolean isEmpty(final boolean[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 判断对象是否为数组且为空。非数组类型抛出 {@link IllegalArgumentException}。
   *
   * @param array 任意对象
   * @return 是否为 {@code null} 或空数组
   * @throws IllegalArgumentException 如果对象不是数组
   */
  public static boolean isEmpty(final Object array) {
    if (array == null) {
      return true;
    }
    if (!array.getClass().isArray()) {
      throw new IllegalArgumentException("Object is not an array: " + array.getClass().getName());
    }
    return java.lang.reflect.Array.getLength(array) == 0;
  }

  // ==================== orEmpty ====================

  /**
   * {@code null} 转为空数组，非 null 原样返回。
   *
   * @param array 数组，可为 {@code null}
   * @return 非{@code null} 数组，原数组为 {@code null} 时返回空数组
   */
  public static byte[] orEmpty(final byte[] array) {
    return array == null ? new byte[0] : array;
  }

  /**
   * {@code null} 转为空数组，非 null 原样返回。
   *
   * @param array 数组，可为 {@code null}
   * @return 非{@code null} 数组，原数组为 {@code null} 时返回空数组
   */
  public static short[] orEmpty(final short[] array) {
    return array == null ? new short[0] : array;
  }

  /**
   * {@code null} 转为空数组，非 null 原样返回。
   *
   * @param array 数组，可为 {@code null}
   * @return 非{@code null} 数组，原数组为 {@code null} 时返回空数组
   */
  public static int[] orEmpty(final int[] array) {
    return array == null ? new int[0] : array;
  }

  /**
   * {@code null} 转为空数组，非 null 原样返回。
   *
   * @param array 数组，可为 {@code null}
   * @return 非{@code null} 数组，原数组为 {@code null} 时返回空数组
   */
  public static long[] orEmpty(final long[] array) {
    return array == null ? new long[0] : array;
  }

  /**
   * {@code null} 转为空数组，非 null 原样返回。
   *
   * @param array 数组，可为 {@code null}
   * @return 非{@code null} 数组，原数组为 {@code null} 时返回空数组
   */
  public static char[] orEmpty(final char[] array) {
    return array == null ? new char[0] : array;
  }

  /**
   * {@code null} 转为空数组，非 null 原样返回。
   *
   * @param array 数组，可为 {@code null}
   * @return 非{@code null} 数组，原数组为 {@code null} 时返回空数组
   */
  public static float[] orEmpty(final float[] array) {
    return array == null ? new float[0] : array;
  }

  /**
   * {@code null} 转为空数组，非 null 原样返回。
   *
   * @param array 数组，可为 {@code null}
   * @return 非{@code null} 数组，原数组为 {@code null} 时返回空数组
   */
  public static double[] orEmpty(final double[] array) {
    return array == null ? new double[0] : array;
  }

  /**
   * {@code null} 转为空数组，非 null 原样返回。
   *
   * @param array 数组，可为 {@code null}
   * @return 非{@code null} 数组，原数组为 {@code null} 时返回空数组
   */
  public static boolean[] orEmpty(final boolean[] array) {
    return array == null ? new boolean[0] : array;
  }

  /**
   * {@code null} 转为空数组，非 null 原样返回。
   *
   * @param array 数组，可为 {@code null}
   * @param <T>   元素类型
   * @return 非{@code null} 数组，原数组为 {@code null} 时返回空数组
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] orEmpty(final T[] array) {
    return array == null ? (T[]) new Object[0] : array;
  }

  // ==================== concat ====================

  /**
   * 拼接多个数组，返回新数组，不修改原数组。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个数组，可为 {@code null}
   * @param rest  其余数组，可为 {@code null}
   * @param <T>   元素类型
   * @return 拼接后的新数组；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  @SafeVarargs
  public static <T> T[] concat(T[] first, T[]... rest) {
    int totalLen = first == null ? 0 : first.length;
    for (T[] arr : rest) {
      if (arr != null) {
        totalLen += arr.length;
      }
    }
    if (totalLen == 0) {
      return first;
    }
    T[] result = Arrays.copyOf(first != null ? first : findFirstNonNull(rest), totalLen);
    int offset = first == null ? 0 : first.length;
    for (T[] arr : rest) {
      if (arr != null) {
        System.arraycopy(arr, 0, result, offset, arr.length);
        offset += arr.length;
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private static <T> T[] findFirstNonNull(T[]... arrays) {
    for (T[] arr : arrays) {
      if (arr != null) {
        return Arrays.copyOf(arr, 0);
      }
    }
    return (T[]) new Object[0];
  }

  // ==================== concat (原始类型) ====================

  /**
   * 拼接多个数组，返回新数组，不修改原数组。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个数组，可为 {@code null}
   * @param rest  其余数组，可为 {@code null}
   * @return 拼接后的新数组；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  public static byte[] concat(byte[] first, byte[]... rest) {
    int fl = first == null ? 0 : first.length, tl = fl;
    for (byte[] arr : rest) {
      if (arr != null) tl += arr.length;
    }
    if (tl == 0) return first;
    byte[] result = first != null ? Arrays.copyOf(first, tl) : new byte[tl];
    int off = fl;
    for (byte[] arr : rest) {
      if (arr != null) {
        System.arraycopy(arr, 0, result, off, arr.length);
        off += arr.length;
      }
    }
    return result;
  }

  /**
   * 拼接多个数组，返回新数组，不修改原数组。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个数组，可为 {@code null}
   * @param rest  其余数组，可为 {@code null}
   * @return 拼接后的新数组；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  public static short[] concat(short[] first, short[]... rest) {
    int fl = first == null ? 0 : first.length, tl = fl;
    for (short[] arr : rest) {
      if (arr != null) tl += arr.length;
    }
    if (tl == 0) return first;
    short[] result = first != null ? Arrays.copyOf(first, tl) : new short[tl];
    int off = fl;
    for (short[] arr : rest) {
      if (arr != null) {
        System.arraycopy(arr, 0, result, off, arr.length);
        off += arr.length;
      }
    }
    return result;
  }

  /**
   * 拼接多个数组，返回新数组，不修改原数组。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个数组，可为 {@code null}
   * @param rest  其余数组，可为 {@code null}
   * @return 拼接后的新数组；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  public static int[] concat(int[] first, int[]... rest) {
    int fl = first == null ? 0 : first.length, tl = fl;
    for (int[] arr : rest) {
      if (arr != null) tl += arr.length;
    }
    if (tl == 0) return first;
    int[] result = first != null ? Arrays.copyOf(first, tl) : new int[tl];
    int off = fl;
    for (int[] arr : rest) {
      if (arr != null) {
        System.arraycopy(arr, 0, result, off, arr.length);
        off += arr.length;
      }
    }
    return result;
  }

  /**
   * 拼接多个数组，返回新数组，不修改原数组。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个数组，可为 {@code null}
   * @param rest  其余数组，可为 {@code null}
   * @return 拼接后的新数组；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  public static long[] concat(long[] first, long[]... rest) {
    int fl = first == null ? 0 : first.length, tl = fl;
    for (long[] arr : rest) {
      if (arr != null) tl += arr.length;
    }
    if (tl == 0) return first;
    long[] result = first != null ? Arrays.copyOf(first, tl) : new long[tl];
    int off = fl;
    for (long[] arr : rest) {
      if (arr != null) {
        System.arraycopy(arr, 0, result, off, arr.length);
        off += arr.length;
      }
    }
    return result;
  }

  /**
   * 拼接多个数组，返回新数组，不修改原数组。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个数组，可为 {@code null}
   * @param rest  其余数组，可为 {@code null}
   * @return 拼接后的新数组；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  public static char[] concat(char[] first, char[]... rest) {
    int fl = first == null ? 0 : first.length, tl = fl;
    for (char[] arr : rest) {
      if (arr != null) tl += arr.length;
    }
    if (tl == 0) return first;
    char[] result = first != null ? Arrays.copyOf(first, tl) : new char[tl];
    int off = fl;
    for (char[] arr : rest) {
      if (arr != null) {
        System.arraycopy(arr, 0, result, off, arr.length);
        off += arr.length;
      }
    }
    return result;
  }

  /**
   * 拼接多个数组，返回新数组，不修改原数组。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个数组，可为 {@code null}
   * @param rest  其余数组，可为 {@code null}
   * @return 拼接后的新数组；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  public static float[] concat(float[] first, float[]... rest) {
    int fl = first == null ? 0 : first.length, tl = fl;
    for (float[] arr : rest) {
      if (arr != null) tl += arr.length;
    }
    if (tl == 0) return first;
    float[] result = first != null ? Arrays.copyOf(first, tl) : new float[tl];
    int off = fl;
    for (float[] arr : rest) {
      if (arr != null) {
        System.arraycopy(arr, 0, result, off, arr.length);
        off += arr.length;
      }
    }
    return result;
  }

  /**
   * 拼接多个数组，返回新数组，不修改原数组。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个数组，可为 {@code null}
   * @param rest  其余数组，可为 {@code null}
   * @return 拼接后的新数组；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  public static double[] concat(double[] first, double[]... rest) {
    int fl = first == null ? 0 : first.length, tl = fl;
    for (double[] arr : rest) {
      if (arr != null) tl += arr.length;
    }
    if (tl == 0) return first;
    double[] result = first != null ? Arrays.copyOf(first, tl) : new double[tl];
    int off = fl;
    for (double[] arr : rest) {
      if (arr != null) {
        System.arraycopy(arr, 0, result, off, arr.length);
        off += arr.length;
      }
    }
    return result;
  }

  /**
   * 拼接多个数组，返回新数组，不修改原数组。
   * <ul>
   *   <li>{@code first} 为 {@code null} 且 {@code rest} 全部 {@code null} → 返回 {@code null}</li>
   *   <li>{@code rest} 中 {@code null} 段自动跳过</li>
   * </ul>
   *
   * @param first 首个数组，可为 {@code null}
   * @param rest  其余数组，可为 {@code null}
   * @return 拼接后的新数组；{@code first} 和 {@code rest} 全部为 {@code null} 时返回 {@code null}
   */
  public static boolean[] concat(boolean[] first, boolean[]... rest) {
    int fl = first == null ? 0 : first.length, tl = fl;
    for (boolean[] arr : rest) {
      if (arr != null) tl += arr.length;
    }
    if (tl == 0) return first;
    boolean[] result = first != null ? Arrays.copyOf(first, tl) : new boolean[tl];
    int off = fl;
    for (boolean[] arr : rest) {
      if (arr != null) {
        System.arraycopy(arr, 0, result, off, arr.length);
        off += arr.length;
      }
    }
    return result;
  }

  // ==================== subArray ====================

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param from  起始索引（含）
   * @param to    结束索引（不含）
   * @param <T>   元素类型
   * @return 截取后的子数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static <T> T[] subArray(T[] array, int from, int to) {
    if (array == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, array.length);
    if (from >= to) return Arrays.copyOfRange(array, 0, 0);
    return Arrays.copyOfRange(array, from, to);
  }

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param from  起始索引（含）
   * @param to    结束索引（不含）
   * @return 截取后的子数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static byte[] subArray(byte[] array, int from, int to) {
    if (array == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, array.length);
    if (from >= to) return Arrays.copyOfRange(array, 0, 0);
    return Arrays.copyOfRange(array, from, to);
  }

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param from  起始索引（含）
   * @param to    结束索引（不含）
   * @return 截取后的子数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static short[] subArray(short[] array, int from, int to) {
    if (array == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, array.length);
    if (from >= to) return Arrays.copyOfRange(array, 0, 0);
    return Arrays.copyOfRange(array, from, to);
  }

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param from  起始索引（含）
   * @param to    结束索引（不含）
   * @return 截取后的子数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static int[] subArray(int[] array, int from, int to) {
    if (array == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, array.length);
    if (from >= to) return Arrays.copyOfRange(array, 0, 0);
    return Arrays.copyOfRange(array, from, to);
  }

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param from  起始索引（含）
   * @param to    结束索引（不含）
   * @return 截取后的子数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static long[] subArray(long[] array, int from, int to) {
    if (array == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, array.length);
    if (from >= to) return Arrays.copyOfRange(array, 0, 0);
    return Arrays.copyOfRange(array, from, to);
  }

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param from  起始索引（含）
   * @param to    结束索引（不含）
   * @return 截取后的子数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static char[] subArray(char[] array, int from, int to) {
    if (array == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, array.length);
    if (from >= to) return Arrays.copyOfRange(array, 0, 0);
    return Arrays.copyOfRange(array, from, to);
  }

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param from  起始索引（含）
   * @param to    结束索引（不含）
   * @return 截取后的子数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static float[] subArray(float[] array, int from, int to) {
    if (array == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, array.length);
    if (from >= to) return Arrays.copyOfRange(array, 0, 0);
    return Arrays.copyOfRange(array, from, to);
  }

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param from  起始索引（含）
   * @param to    结束索引（不含）
   * @return 截取后的子数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static double[] subArray(double[] array, int from, int to) {
    if (array == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, array.length);
    if (from >= to) return Arrays.copyOfRange(array, 0, 0);
    return Arrays.copyOfRange(array, from, to);
  }

  /**
   * 按区间截取。from&lt;0 视为 0，to&gt;len 视为 len，from&gt;=to 返回空。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param from  起始索引（含）
   * @param to    结束索引（不含）
   * @return 截取后的子数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static boolean[] subArray(boolean[] array, int from, int to) {
    if (array == null) return null;
    from = Math.max(from, 0);
    to = Math.min(to, array.length);
    if (from >= to) return Arrays.copyOfRange(array, 0, 0);
    return Arrays.copyOfRange(array, from, to);
  }

  // ==================== withoutAt ====================

  /**
   * 返回不含指定索引元素的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @param <T>     元素类型
   * @return 不含指定索引元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static <T> T[] withoutAt(T[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length) {
        mask[idx] = true;
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    T[] result = Arrays.copyOf(array, array.length - cnt);
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定索引元素的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @return 不含指定索引元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static byte[] withoutAt(byte[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length) {
        mask[idx] = true;
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    byte[] result = new byte[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定索引元素的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @return 不含指定索引元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static short[] withoutAt(short[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length) {
        mask[idx] = true;
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    short[] result = new short[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定索引元素的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @return 不含指定索引元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static int[] withoutAt(int[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length) {
        mask[idx] = true;
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    int[] result = new int[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定索引元素的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @return 不含指定索引元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static long[] withoutAt(long[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length) {
        mask[idx] = true;
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    long[] result = new long[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定索引元素的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @return 不含指定索引元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static char[] withoutAt(char[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length) {
        mask[idx] = true;
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    char[] result = new char[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定索引元素的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @return 不含指定索引元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static float[] withoutAt(float[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length) {
        mask[idx] = true;
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    float[] result = new float[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定索引元素的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @return 不含指定索引元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static double[] withoutAt(double[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length) {
        mask[idx] = true;
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    double[] result = new double[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定索引元素的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>越界、负数索引自动忽略</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要排除的索引，可为 {@code null}
   * @return 不含指定索引元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static boolean[] withoutAt(boolean[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length) {
        mask[idx] = true;
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    boolean[] result = new boolean[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  // ==================== withoutElement ====================

  /**
   * 返回不含指定值元素的新数组（各值仅删首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>未命中不报错，返回原数组副本</li>
   *   <li>{@code null} 值通过 {@code Objects.equals} 匹配</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array    原数组，可为 {@code null}
   * @param elements 要排除的值，可为 {@code null}
   * @param <T>      元素类型
   * @return 不含指定值元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  @SafeVarargs
  public static <T> T[] withoutElement(T[] array, T... elements) {
    if (array == null) return null;
    if (elements == null || elements.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (T elem : elements) {
      for (int i = 0; i < array.length; i++) {
        if (!mask[i] && Objects.equals(elem, array[i])) {
          mask[i] = true;
          break;
        }
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    T[] result = Arrays.copyOf(array, array.length - cnt);
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定值元素的新数组（各值仅删首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>未命中不报错，返回原数组副本</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array    原数组，可为 {@code null}
   * @param elements 要排除的值，可为 {@code null}
   * @return 不含指定值元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static byte[] withoutElement(byte[] array, byte... elements) {
    if (array == null) return null;
    if (elements == null || elements.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (byte elem : elements) {
      for (int i = 0; i < array.length; i++) {
        if (!mask[i] && array[i] == elem) {
          mask[i] = true;
          break;
        }
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    byte[] result = new byte[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定值元素的新数组（各值仅删首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>未命中不报错，返回原数组副本</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array    原数组，可为 {@code null}
   * @param elements 要排除的值，可为 {@code null}
   * @return 不含指定值元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static short[] withoutElement(short[] array, short... elements) {
    if (array == null) return null;
    if (elements == null || elements.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (short elem : elements) {
      for (int i = 0; i < array.length; i++) {
        if (!mask[i] && array[i] == elem) {
          mask[i] = true;
          break;
        }
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    short[] result = new short[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定值元素的新数组（各值仅删首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>未命中不报错，返回原数组副本</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array    原数组，可为 {@code null}
   * @param elements 要排除的值，可为 {@code null}
   * @return 不含指定值元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static int[] withoutElement(int[] array, int... elements) {
    if (array == null) return null;
    if (elements == null || elements.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (int elem : elements) {
      for (int i = 0; i < array.length; i++) {
        if (!mask[i] && array[i] == elem) {
          mask[i] = true;
          break;
        }
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    int[] result = new int[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定值元素的新数组（各值仅删首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>未命中不报错，返回原数组副本</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array    原数组，可为 {@code null}
   * @param elements 要排除的值，可为 {@code null}
   * @return 不含指定值元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static long[] withoutElement(long[] array, long... elements) {
    if (array == null) return null;
    if (elements == null || elements.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (long elem : elements) {
      for (int i = 0; i < array.length; i++) {
        if (!mask[i] && array[i] == elem) {
          mask[i] = true;
          break;
        }
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    long[] result = new long[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定值元素的新数组（各值仅删首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>未命中不报错，返回原数组副本</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array    原数组，可为 {@code null}
   * @param elements 要排除的值，可为 {@code null}
   * @return 不含指定值元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static char[] withoutElement(char[] array, char... elements) {
    if (array == null) return null;
    if (elements == null || elements.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (char elem : elements) {
      for (int i = 0; i < array.length; i++) {
        if (!mask[i] && array[i] == elem) {
          mask[i] = true;
          break;
        }
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    char[] result = new char[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定值元素的新数组（各值仅删首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>未命中不报错，返回原数组副本</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array    原数组，可为 {@code null}
   * @param elements 要排除的值，可为 {@code null}
   * @return 不含指定值元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static float[] withoutElement(float[] array, float... elements) {
    if (array == null) return null;
    if (elements == null || elements.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (float elem : elements) {
      for (int i = 0; i < array.length; i++) {
        if (!mask[i] && array[i] == elem) {
          mask[i] = true;
          break;
        }
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    float[] result = new float[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定值元素的新数组（各值仅删首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>未命中不报错，返回原数组副本</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array    原数组，可为 {@code null}
   * @param elements 要排除的值，可为 {@code null}
   * @return 不含指定值元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static double[] withoutElement(double[] array, double... elements) {
    if (array == null) return null;
    if (elements == null || elements.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (double elem : elements) {
      for (int i = 0; i < array.length; i++) {
        if (!mask[i] && array[i] == elem) {
          mask[i] = true;
          break;
        }
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    double[] result = new double[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  /**
   * 返回不含指定值元素的新数组（各值仅删首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code elements} 为 {@code null} 或空 → 返回原数组副本</li>
   *   <li>未命中不报错，返回原数组副本</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array    原数组，可为 {@code null}
   * @param elements 要排除的值，可为 {@code null}
   * @return 不含指定值元素的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static boolean[] withoutElement(boolean[] array, boolean... elements) {
    if (array == null) return null;
    if (elements == null || elements.length == 0) {
      return Arrays.copyOf(array, array.length);
    }
    boolean[] mask = new boolean[array.length];
    for (boolean elem : elements) {
      for (int i = 0; i < array.length; i++) {
        if (!mask[i] && array[i] == elem) {
          mask[i] = true;
          break;
        }
      }
    }
    int cnt = 0;
    for (boolean b : mask) {
      if (b) cnt++;
    }
    boolean[] result = new boolean[array.length - cnt];
    int w = 0;
    for (int i = 0; i < array.length; i++) {
      if (!mask[i]) {
        result[w++] = array[i];
      }
    }
    return result;
  }

  // ==================== elementsAt：返回指定索引的元素 ====================

  /**
   * 返回指定索引处的元素组成的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @param <T>     元素类型
   * @return 指定索引处的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static <T> T[] elementsAt(T[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) return Arrays.copyOf(array, 0);
    boolean[] mask = new boolean[array.length];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    T[] result = Arrays.copyOf(array, count);
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      if (mask[i]) result[pos++] = array[i];
    }
    return result;
  }

  /**
   * 返回指定索引处的元素组成的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @return 指定索引处的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static byte[] elementsAt(byte[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) return new byte[0];
    boolean[] mask = new boolean[array.length];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    byte[] result = new byte[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      if (mask[i]) result[pos++] = array[i];
    }
    return result;
  }

  /**
   * 返回指定索引处的元素组成的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @return 指定索引处的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static short[] elementsAt(short[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) return new short[0];
    boolean[] mask = new boolean[array.length];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    short[] result = new short[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      if (mask[i]) result[pos++] = array[i];
    }
    return result;
  }

  /**
   * 返回指定索引处的元素组成的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @return 指定索引处的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static int[] elementsAt(int[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) return new int[0];
    boolean[] mask = new boolean[array.length];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    int[] result = new int[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      if (mask[i]) result[pos++] = array[i];
    }
    return result;
  }

  /**
   * 返回指定索引处的元素组成的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @return 指定索引处的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static long[] elementsAt(long[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) return new long[0];
    boolean[] mask = new boolean[array.length];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    long[] result = new long[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      if (mask[i]) result[pos++] = array[i];
    }
    return result;
  }

  /**
   * 返回指定索引处的元素组成的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @return 指定索引处的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static char[] elementsAt(char[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) return new char[0];
    boolean[] mask = new boolean[array.length];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    char[] result = new char[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      if (mask[i]) result[pos++] = array[i];
    }
    return result;
  }

  /**
   * 返回指定索引处的元素组成的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @return 指定索引处的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static float[] elementsAt(float[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) return new float[0];
    boolean[] mask = new boolean[array.length];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    float[] result = new float[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      if (mask[i]) result[pos++] = array[i];
    }
    return result;
  }

  /**
   * 返回指定索引处的元素组成的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @return 指定索引处的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static double[] elementsAt(double[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) return new double[0];
    boolean[] mask = new boolean[array.length];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    double[] result = new double[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      if (mask[i]) result[pos++] = array[i];
    }
    return result;
  }

  /**
   * 返回指定索引处的元素组成的新数组，不修改原数组。
   * <ul>
   *   <li>{@code indices} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>越界、负数索引忽略，重复索引去重</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array   原数组，可为 {@code null}
   * @param indices 要取的索引，可为 {@code null}
   * @return 指定索引处的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static boolean[] elementsAt(boolean[] array, int... indices) {
    if (array == null) return null;
    if (indices == null || indices.length == 0) return new boolean[0];
    boolean[] mask = new boolean[array.length];
    int count = 0;
    for (int idx : indices) {
      if (idx >= 0 && idx < array.length && !mask[idx]) {
        mask[idx] = true;
        count++;
      }
    }
    boolean[] result = new boolean[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      if (mask[i]) result[pos++] = array[i];
    }
    return result;
  }

  // ==================== elementsIn：返回匹配指定值的元素 ====================

  /**
   * 返回匹配指定值的元素组成的新数组（各值仅取首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code null} 值通过 {@code Objects.equals} 匹配</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array  原数组，可为 {@code null}
   * @param values 要匹配的值，可为 {@code null}
   * @param <T>    元素类型
   * @return 匹配的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  @SafeVarargs
  public static <T> T[] elementsIn(T[] array, T... values) {
    if (array == null) return null;
    if (values == null || values.length == 0) return Arrays.copyOf(array, 0);
    boolean[] mask = new boolean[values.length];
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && Objects.equals(values[j], array[i])) {
          mask[j] = true;
          count++;
          break;
        }
      }
    }
    T[] result = Arrays.copyOf(array, count);
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (mask[j] && Objects.equals(values[j], array[i])) {
          result[pos++] = array[i];
          mask[j] = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   * 返回匹配指定值的元素组成的新数组（各值仅取首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array  原数组，可为 {@code null}
   * @param values 要匹配的值，可为 {@code null}
   * @return 匹配的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static byte[] elementsIn(byte[] array, byte... values) {
    if (array == null) return null;
    if (values == null || values.length == 0) return new byte[0];
    boolean[] mask = new boolean[values.length];
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && array[i] == values[j]) {
          mask[j] = true;
          count++;
          break;
        }
      }
    }
    byte[] result = new byte[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (mask[j] && array[i] == values[j]) {
          result[pos++] = array[i];
          mask[j] = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   * 返回匹配指定值的元素组成的新数组（各值仅取首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array  原数组，可为 {@code null}
   * @param values 要匹配的值，可为 {@code null}
   * @return 匹配的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static short[] elementsIn(short[] array, short... values) {
    if (array == null) return null;
    if (values == null || values.length == 0) return new short[0];
    boolean[] mask = new boolean[values.length];
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && array[i] == values[j]) {
          mask[j] = true;
          count++;
          break;
        }
      }
    }
    short[] result = new short[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (mask[j] && array[i] == values[j]) {
          result[pos++] = array[i];
          mask[j] = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   * 返回匹配指定值的元素组成的新数组（各值仅取首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array  原数组，可为 {@code null}
   * @param values 要匹配的值，可为 {@code null}
   * @return 匹配的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static int[] elementsIn(int[] array, int... values) {
    if (array == null) return null;
    if (values == null || values.length == 0) return new int[0];
    boolean[] mask = new boolean[values.length];
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && array[i] == values[j]) {
          mask[j] = true;
          count++;
          break;
        }
      }
    }
    int[] result = new int[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (mask[j] && array[i] == values[j]) {
          result[pos++] = array[i];
          mask[j] = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   * 返回匹配指定值的元素组成的新数组（各值仅取首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array  原数组，可为 {@code null}
   * @param values 要匹配的值，可为 {@code null}
   * @return 匹配的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static long[] elementsIn(long[] array, long... values) {
    if (array == null) return null;
    if (values == null || values.length == 0) return new long[0];
    boolean[] mask = new boolean[values.length];
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && array[i] == values[j]) {
          mask[j] = true;
          count++;
          break;
        }
      }
    }
    long[] result = new long[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (mask[j] && array[i] == values[j]) {
          result[pos++] = array[i];
          mask[j] = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   * 返回匹配指定值的元素组成的新数组（各值仅取首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array  原数组，可为 {@code null}
   * @param values 要匹配的值，可为 {@code null}
   * @return 匹配的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static char[] elementsIn(char[] array, char... values) {
    if (array == null) return null;
    if (values == null || values.length == 0) return new char[0];
    boolean[] mask = new boolean[values.length];
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && array[i] == values[j]) {
          mask[j] = true;
          count++;
          break;
        }
      }
    }
    char[] result = new char[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (mask[j] && array[i] == values[j]) {
          result[pos++] = array[i];
          mask[j] = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   * 返回匹配指定值的元素组成的新数组（各值仅取首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array  原数组，可为 {@code null}
   * @param values 要匹配的值，可为 {@code null}
   * @return 匹配的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static float[] elementsIn(float[] array, float... values) {
    if (array == null) return null;
    if (values == null || values.length == 0) return new float[0];
    boolean[] mask = new boolean[values.length];
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && array[i] == values[j]) {
          mask[j] = true;
          count++;
          break;
        }
      }
    }
    float[] result = new float[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (mask[j] && array[i] == values[j]) {
          result[pos++] = array[i];
          mask[j] = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   * 返回匹配指定值的元素组成的新数组（各值仅取首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array  原数组，可为 {@code null}
   * @param values 要匹配的值，可为 {@code null}
   * @return 匹配的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static double[] elementsIn(double[] array, double... values) {
    if (array == null) return null;
    if (values == null || values.length == 0) return new double[0];
    boolean[] mask = new boolean[values.length];
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && array[i] == values[j]) {
          mask[j] = true;
          count++;
          break;
        }
      }
    }
    double[] result = new double[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (mask[j] && array[i] == values[j]) {
          result[pos++] = array[i];
          mask[j] = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   * 返回匹配指定值的元素组成的新数组（各值仅取首次匹配），不修改原数组。
   * <ul>
   *   <li>{@code values} 为 {@code null} 或空 → 返回空数组</li>
   *   <li>未命中不报错，对应值不参与结果</li>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   * </ul>
   *
   * @param array  原数组，可为 {@code null}
   * @param values 要匹配的值，可为 {@code null}
   * @return 匹配的元素组成的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static boolean[] elementsIn(boolean[] array, boolean... values) {
    if (array == null) return null;
    if (values == null || values.length == 0) return new boolean[0];
    boolean[] mask = new boolean[values.length];
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (!mask[j] && array[i] == values[j]) {
          mask[j] = true;
          count++;
          break;
        }
      }
    }
    boolean[] result = new boolean[count];
    int pos = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (mask[j] && array[i] == values[j]) {
          result[pos++] = array[i];
          mask[j] = false;
          break;
        }
      }
    }
    return result;
  }

  // ==================== reverse ====================

  /**
   * 反转返回新数组。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @param <T>   元素类型
   * @return 反转后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static <T> T[] reverse(T[] array) {
    if (array == null) return null;
    T[] result = Arrays.copyOf(array, array.length);
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - 1 - i];
    }
    return result;
  }

  /**
   * 反转返回新数组。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @return 反转后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static byte[] reverse(byte[] array) {
    if (array == null) return null;
    byte[] result = new byte[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - 1 - i];
    }
    return result;
  }

  /**
   * 反转返回新数组。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @return 反转后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static short[] reverse(short[] array) {
    if (array == null) return null;
    short[] result = new short[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - 1 - i];
    }
    return result;
  }

  /**
   * 反转返回新数组。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @return 反转后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static int[] reverse(int[] array) {
    if (array == null) return null;
    int[] result = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - 1 - i];
    }
    return result;
  }

  /**
   * 反转返回新数组。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @return 反转后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static long[] reverse(long[] array) {
    if (array == null) return null;
    long[] result = new long[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - 1 - i];
    }
    return result;
  }

  /**
   * 反转返回新数组。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @return 反转后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static char[] reverse(char[] array) {
    if (array == null) return null;
    char[] result = new char[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - 1 - i];
    }
    return result;
  }

  /**
   * 反转返回新数组。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @return 反转后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static float[] reverse(float[] array) {
    if (array == null) return null;
    float[] result = new float[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - 1 - i];
    }
    return result;
  }

  /**
   * 反转返回新数组。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @return 反转后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static double[] reverse(double[] array) {
    if (array == null) return null;
    double[] result = new double[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - 1 - i];
    }
    return result;
  }

  /**
   * 反转返回新数组。不修改原数组。
   *
   * @param array 原数组，{@code null} 返回 {@code null}
   * @return 反转后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static boolean[] reverse(boolean[] array) {
    if (array == null) return null;
    boolean[] result = new boolean[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - 1 - i];
    }
    return result;
  }

  // ==================== reverseSelf ====================

  /**
   * 原地反转，修改原数组。null 安全。
   *
   * @param array 要反转的数组，{@code null} 不处理
   * @param <T>   元素类型
   */
  public static <T> void reverseSelf(T[] array) {
    if (array == null) return;
    for (int i = 0; i < array.length / 2; i++) {
      T tmp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = tmp;
    }
  }

  /**
   * 原地反转，修改原数组。null 安全。
   *
   * @param array 要反转的数组，{@code null} 不处理
   */
  public static void reverseSelf(byte[] array) {
    if (array == null) return;
    for (int i = 0; i < array.length / 2; i++) {
      byte tmp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = tmp;
    }
  }

  /**
   * 原地反转，修改原数组。null 安全。
   *
   * @param array 要反转的数组，{@code null} 不处理
   */
  public static void reverseSelf(short[] array) {
    if (array == null) return;
    for (int i = 0; i < array.length / 2; i++) {
      short tmp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = tmp;
    }
  }

  /**
   * 原地反转，修改原数组。null 安全。
   *
   * @param array 要反转的数组，{@code null} 不处理
   */
  public static void reverseSelf(int[] array) {
    if (array == null) return;
    for (int i = 0; i < array.length / 2; i++) {
      int tmp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = tmp;
    }
  }

  /**
   * 原地反转，修改原数组。null 安全。
   *
   * @param array 要反转的数组，{@code null} 不处理
   */
  public static void reverseSelf(long[] array) {
    if (array == null) return;
    for (int i = 0; i < array.length / 2; i++) {
      long tmp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = tmp;
    }
  }

  /**
   * 原地反转，修改原数组。null 安全。
   *
   * @param array 要反转的数组，{@code null} 不处理
   */
  public static void reverseSelf(char[] array) {
    if (array == null) return;
    for (int i = 0; i < array.length / 2; i++) {
      char tmp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = tmp;
    }
  }

  /**
   * 原地反转，修改原数组。null 安全。
   *
   * @param array 要反转的数组，{@code null} 不处理
   */
  public static void reverseSelf(float[] array) {
    if (array == null) return;
    for (int i = 0; i < array.length / 2; i++) {
      float tmp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = tmp;
    }
  }

  /**
   * 原地反转，修改原数组。null 安全。
   *
   * @param array 要反转的数组，{@code null} 不处理
   */
  public static void reverseSelf(double[] array) {
    if (array == null) return;
    for (int i = 0; i < array.length / 2; i++) {
      double tmp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = tmp;
    }
  }

  /**
   * 原地反转，修改原数组。null 安全。
   *
   * @param array 要反转的数组，{@code null} 不处理
   */
  public static void reverseSelf(boolean[] array) {
    if (array == null) return;
    for (int i = 0; i < array.length / 2; i++) {
      boolean tmp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = tmp;
    }
  }

  // ==================== join ====================

  /**
   * 用分隔符串联数组元素。
   * <ul>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code array} 长度为 0 → 返回 {@code ""}</li>
   *   <li>{@code array} 中 {@code null} 元素 → 输出 {@code "null"}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter 分隔符，可为 {@code null}
   * @param array     原数组，可为 {@code null}
   * @param <T>       元素类型
   * @return 串联后的字符串；原数组为 {@code null} 时返回 {@code null}，空数组返回 {@code ""}
   */
  public static <T> String join(String delimiter, T[] array) {
    if (array == null) return null;
    if (array.length == 0) return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) sb.append(delimiter);
      sb.append(array[i]);
    }
    return sb.toString();
  }

  /**
   * 用分隔符串联数组元素。
   * <ul>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code array} 长度为 0 → 返回 {@code ""}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter 分隔符，可为 {@code null}
   * @param array     原数组，可为 {@code null}
   * @return 串联后的字符串；原数组为 {@code null} 时返回 {@code null}，空数组返回 {@code ""}
   */
  public static String join(String delimiter, byte[] array) {
    if (array == null) return null;
    if (array.length == 0) return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) sb.append(delimiter);
      sb.append(array[i]);
    }
    return sb.toString();
  }

  /**
   * 用分隔符串联数组元素。
   * <ul>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code array} 长度为 0 → 返回 {@code ""}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter 分隔符，可为 {@code null}
   * @param array     原数组，可为 {@code null}
   * @return 串联后的字符串；原数组为 {@code null} 时返回 {@code null}，空数组返回 {@code ""}
   */
  public static String join(String delimiter, short[] array) {
    if (array == null) return null;
    if (array.length == 0) return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) sb.append(delimiter);
      sb.append(array[i]);
    }
    return sb.toString();
  }

  /**
   * 用分隔符串联数组元素。
   * <ul>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code array} 长度为 0 → 返回 {@code ""}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter 分隔符，可为 {@code null}
   * @param array     原数组，可为 {@code null}
   * @return 串联后的字符串；原数组为 {@code null} 时返回 {@code null}，空数组返回 {@code ""}
   */
  public static String join(String delimiter, int[] array) {
    if (array == null) return null;
    if (array.length == 0) return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) sb.append(delimiter);
      sb.append(array[i]);
    }
    return sb.toString();
  }

  /**
   * 用分隔符串联数组元素。
   * <ul>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code array} 长度为 0 → 返回 {@code ""}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter 分隔符，可为 {@code null}
   * @param array     原数组，可为 {@code null}
   * @return 串联后的字符串；原数组为 {@code null} 时返回 {@code null}，空数组返回 {@code ""}
   */
  public static String join(String delimiter, long[] array) {
    if (array == null) return null;
    if (array.length == 0) return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) sb.append(delimiter);
      sb.append(array[i]);
    }
    return sb.toString();
  }

  /**
   * 用分隔符串联数组元素。
   * <ul>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code array} 长度为 0 → 返回 {@code ""}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter 分隔符，可为 {@code null}
   * @param array     原数组，可为 {@code null}
   * @return 串联后的字符串；原数组为 {@code null} 时返回 {@code null}，空数组返回 {@code ""}
   */
  public static String join(String delimiter, char[] array) {
    if (array == null) return null;
    if (array.length == 0) return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) sb.append(delimiter);
      sb.append(array[i]);
    }
    return sb.toString();
  }

  /**
   * 用分隔符串联数组元素。
   * <ul>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code array} 长度为 0 → 返回 {@code ""}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter 分隔符，可为 {@code null}
   * @param array     原数组，可为 {@code null}
   * @return 串联后的字符串；原数组为 {@code null} 时返回 {@code null}，空数组返回 {@code ""}
   */
  public static String join(String delimiter, float[] array) {
    if (array == null) return null;
    if (array.length == 0) return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) sb.append(delimiter);
      sb.append(array[i]);
    }
    return sb.toString();
  }

  /**
   * 用分隔符串联数组元素。
   * <ul>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code array} 长度为 0 → 返回 {@code ""}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter 分隔符，可为 {@code null}
   * @param array     原数组，可为 {@code null}
   * @return 串联后的字符串；原数组为 {@code null} 时返回 {@code null}，空数组返回 {@code ""}
   */
  public static String join(String delimiter, double[] array) {
    if (array == null) return null;
    if (array.length == 0) return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) sb.append(delimiter);
      sb.append(array[i]);
    }
    return sb.toString();
  }

  /**
   * 用分隔符串联数组元素。
   * <ul>
   *   <li>{@code array} 为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code array} 长度为 0 → 返回 {@code ""}</li>
   *   <li>{@code delimiter} 为 {@code null} → 输出 {@code "null"}（不抛异常）</li>
   * </ul>
   *
   * @param delimiter 分隔符，可为 {@code null}
   * @param array     原数组，可为 {@code null}
   * @return 串联后的字符串；原数组为 {@code null} 时返回 {@code null}，空数组返回 {@code ""}
   */
  public static String join(String delimiter, boolean[] array) {
    if (array == null) return null;
    if (array.length == 0) return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) sb.append(delimiter);
      sb.append(array[i]);
    }
    return sb.toString();
  }

  // ==================== contains / firstIndexOf (包装类型) ====================

  /**
   * 是否包含指定值（null 安全）。
   *
   * @param array 数组，{@code null} 返回 {@code false}
   * @param value 要查找的值
   * @param <T>   元素类型
   * @return 是否包含，数组为 {@code null} 时返回 {@code false}
   */
  public static <T> boolean contains(T[] array, T value) {
    return firstIndexOf(array, value) >= 0;
  }

  /**
   * 查找首次出现下标（null 安全）。使用 Objects.equals 匹配。
   *
   * @param array 数组，{@code null} 返回 -1
   * @param value 要查找的值
   * @param <T>   元素类型
   * @return 首次出现的下标，数组为 {@code null} 或未找到时返回 {@code -1}
   */
  public static <T> int firstIndexOf(T[] array, T value) {
    if (array == null) {
      return -1;
    }
    for (int i = 0; i < array.length; i++) {
      if (Objects.equals(value, array[i])) {
        return i;
      }
    }
    return -1;
  }

  // ==================== contains / firstIndexOf (原始类型) ====================

  /**
   * 是否包含指定值（null 安全）。
   *
   * @param array 数组，{@code null} 返回 {@code false}
   * @param value 要查找的值
   * @return 是否包含，数组为 {@code null} 时返回 {@code false}
   */
  public static boolean contains(byte[] array, byte value) {
    return firstIndexOf(array, value) >= 0;
  }

  /**
   * 查找首次出现下标（null 安全）。
   *
   * @param array 数组，{@code null} 返回 -1
   * @param value 要查找的值
   * @return 首次出现的下标，数组为 {@code null} 或未找到时返回 {@code -1}
   */
  public static int firstIndexOf(byte[] array, byte value) {
    if (array == null) return -1;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * 是否包含指定值（null 安全）。
   *
   * @param array 数组，{@code null} 返回 {@code false}
   * @param value 要查找的值
   * @return 是否包含，数组为 {@code null} 时返回 {@code false}
   */
  public static boolean contains(short[] array, short value) {
    return firstIndexOf(array, value) >= 0;
  }

  /**
   * 查找首次出现下标（null 安全）。
   *
   * @param array 数组，{@code null} 返回 -1
   * @param value 要查找的值
   * @return 首次出现的下标，数组为 {@code null} 或未找到时返回 {@code -1}
   */
  public static int firstIndexOf(short[] array, short value) {
    if (array == null) return -1;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * 是否包含指定值（null 安全）。
   *
   * @param array 数组，{@code null} 返回 {@code false}
   * @param value 要查找的值
   * @return 是否包含，数组为 {@code null} 时返回 {@code false}
   */
  public static boolean contains(int[] array, int value) {
    return firstIndexOf(array, value) >= 0;
  }

  /**
   * 查找首次出现下标（null 安全）。
   *
   * @param array 数组，{@code null} 返回 -1
   * @param value 要查找的值
   * @return 首次出现的下标，数组为 {@code null} 或未找到时返回 {@code -1}
   */
  public static int firstIndexOf(int[] array, int value) {
    if (array == null) return -1;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * 是否包含指定值（null 安全）。
   *
   * @param array 数组，{@code null} 返回 {@code false}
   * @param value 要查找的值
   * @return 是否包含，数组为 {@code null} 时返回 {@code false}
   */
  public static boolean contains(long[] array, long value) {
    return firstIndexOf(array, value) >= 0;
  }

  /**
   * 查找首次出现下标（null 安全）。
   *
   * @param array 数组，{@code null} 返回 -1
   * @param value 要查找的值
   * @return 首次出现的下标，数组为 {@code null} 或未找到时返回 {@code -1}
   */
  public static int firstIndexOf(long[] array, long value) {
    if (array == null) return -1;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * 是否包含指定值（null 安全）。
   *
   * @param array 数组，{@code null} 返回 {@code false}
   * @param value 要查找的值
   * @return 是否包含，数组为 {@code null} 时返回 {@code false}
   */
  public static boolean contains(char[] array, char value) {
    return firstIndexOf(array, value) >= 0;
  }

  /**
   * 查找首次出现下标（null 安全）。
   *
   * @param array 数组，{@code null} 返回 -1
   * @param value 要查找的值
   * @return 首次出现的下标，数组为 {@code null} 或未找到时返回 {@code -1}
   */
  public static int firstIndexOf(char[] array, char value) {
    if (array == null) return -1;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * 是否包含指定值（null 安全）。
   *
   * @param array 数组，{@code null} 返回 {@code false}
   * @param value 要查找的值
   * @return 是否包含，数组为 {@code null} 时返回 {@code false}
   */
  public static boolean contains(float[] array, float value) {
    return firstIndexOf(array, value) >= 0;
  }

  /**
   * 查找首次出现下标（null 安全）。
   *
   * @param array 数组，{@code null} 返回 -1
   * @param value 要查找的值
   * @return 首次出现的下标，数组为 {@code null} 或未找到时返回 {@code -1}
   */
  public static int firstIndexOf(float[] array, float value) {
    if (array == null) return -1;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * 是否包含指定值（null 安全）。
   *
   * @param array 数组，{@code null} 返回 {@code false}
   * @param value 要查找的值
   * @return 是否包含，数组为 {@code null} 时返回 {@code false}
   */
  public static boolean contains(double[] array, double value) {
    return firstIndexOf(array, value) >= 0;
  }

  /**
   * 查找首次出现下标（null 安全）。
   *
   * @param array 数组，{@code null} 返回 -1
   * @param value 要查找的值
   * @return 首次出现的下标，数组为 {@code null} 或未找到时返回 {@code -1}
   */
  public static int firstIndexOf(double[] array, double value) {
    if (array == null) return -1;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * 是否包含指定值（null 安全）。
   *
   * @param array 数组，{@code null} 返回 {@code false}
   * @param value 要查找的值
   * @return 是否包含，数组为 {@code null} 时返回 {@code false}
   */
  public static boolean contains(boolean[] array, boolean value) {
    return firstIndexOf(array, value) >= 0;
  }

  /**
   * 查找首次出现下标（null 安全）。
   *
   * @param array 数组，{@code null} 返回 -1
   * @param value 要查找的值
   * @return 首次出现的下标，数组为 {@code null} 或未找到时返回 {@code -1}
   */
  public static int firstIndexOf(boolean[] array, boolean value) {
    if (array == null) return -1;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * 对象是否为数组。
   *
   * @param obj 任意对象
   * @return 是否为数组，永远不返回 {@code null}
   */
  public static boolean isArray(Object obj) {
    return null != obj && obj.getClass().isArray();
  }

  /**
   * 将任意数组（含多维原始数组）转为字符串。
   * 与 {@link Arrays#deepToString(Object[])} 不同，本方法能正确处理 {@code int[][]} 等多维原始类型数组。
   *
   * @param obj 任意对象
   * @return 数组的字符串表示；参数为 {@code null} 时返回 {@code null}
   */
  public static String arrayToString(Object obj) {
    if (null == obj) {
      return null;
    }
    if (!isArray(obj)) {
      return obj.toString();
    }
    return deepArrayToString(obj);
  }

  private static String deepArrayToString(Object array) {
    int len = java.lang.reflect.Array.getLength(array);
    if (len == 0) {
      return "[]";
    }
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    for (int i = 0; i < len; i++) {
      if (i > 0) {
        sb.append(", ");
      }
      Object element = java.lang.reflect.Array.get(array, i);
      if (element != null && element.getClass().isArray()) {
        sb.append(deepArrayToString(element));
      } else {
        sb.append(element);
      }
    }
    sb.append(']');
    return sb.toString();
  }

  // ==================== lastIndexOf ====================

  public static <T> int lastIndexOf(T[] array, T value) {
    if (array == null) return -1;
    for (int i = array.length - 1; i >= 0; i--) {
      if (Objects.equals(value, array[i])) return i;
    }
    return -1;
  }

  public static int lastIndexOf(byte[] array, byte value) {
    if (array == null) return -1;
    for (int i = array.length - 1; i >= 0; i--) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  public static int lastIndexOf(short[] array, short value) {
    if (array == null) return -1;
    for (int i = array.length - 1; i >= 0; i--) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  public static int lastIndexOf(int[] array, int value) {
    if (array == null) return -1;
    for (int i = array.length - 1; i >= 0; i--) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  public static int lastIndexOf(long[] array, long value) {
    if (array == null) return -1;
    for (int i = array.length - 1; i >= 0; i--) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  public static int lastIndexOf(char[] array, char value) {
    if (array == null) return -1;
    for (int i = array.length - 1; i >= 0; i--) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  public static int lastIndexOf(float[] array, float value) {
    if (array == null) return -1;
    for (int i = array.length - 1; i >= 0; i--) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  public static int lastIndexOf(double[] array, double value) {
    if (array == null) return -1;
    for (int i = array.length - 1; i >= 0; i--) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  public static int lastIndexOf(boolean[] array, boolean value) {
    if (array == null) return -1;
    for (int i = array.length - 1; i >= 0; i--) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * 移除数组中所有 {@code null} 元素，返回新数组，不修改原数组。
   * <ul>
   *   <li>原数组为 {@code null} → 返回 {@code null}</li>
   *   <li>原数组无 null → 返回原数组副本</li>
   * </ul>
   *
   * @param array 原数组，可为 {@code null}
   * @param <T>   元素类型
   * @return 不含 {@code null} 的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static <T> T[] removeNulls(T[] array) {
    if (array == null) return null;
    int count = 0;
    for (T t : array) {
      if (t != null) count++;
    }
    if (count == array.length) return Arrays.copyOf(array, array.length);
    T[] result = Arrays.copyOf(array, count);
    int pos = 0;
    for (T t : array) {
      if (t != null) result[pos++] = t;
    }
    return result;
  }

  // ==================== distinct ====================

  /**
   * 数组去重（保留首次出现顺序），返回新数组，不修改原数组。
   * <ul>
   *   <li>原数组为 {@code null} → 返回 {@code null}</li>
   *   <li>{@code null} 元素视为有效值</li>
   * </ul>
   *
   * @param array 原数组，可为 {@code null}
   * @param <T>   元素类型
   * @return 去重后的新数组；原数组为 {@code null} 时返回 {@code null}
   */
  public static <T> T[] distinct(T[] array) {
    if (array == null) return null;
    Set<T> seen = new HashSet<>(array.length);
    int count = 0;
    for (T t : array) {
      if (seen.add(t)) count++;
    }
    T[] result = Arrays.copyOf(array, count);
    seen.clear();
    int pos = 0;
    for (T t : array) {
      if (seen.add(t)) result[pos++] = t;
    }
    return result;
  }

  public static byte[] distinct(byte[] array) {
    if (array == null) return null;
    boolean[] seen = new boolean[256];
    int count = 0;
    for (byte b : array) {
      if (!seen[b & 0xFF]) {
        seen[b & 0xFF] = true;
        count++;
      }
    }
    byte[] result = new byte[count];
    int pos = 0;
    seen = new boolean[256];
    for (byte b : array) {
      if (!seen[b & 0xFF]) {
        seen[b & 0xFF] = true;
        result[pos++] = b;
      }
    }
    return result;
  }

  public static short[] distinct(short[] array) {
    if (array == null) return null;
    boolean[] seen = new boolean[65536];
    int count = 0;
    for (short s : array) {
      if (!seen[s & 0xFFFF]) {
        seen[s & 0xFFFF] = true;
        count++;
      }
    }
    short[] result = new short[count];
    int pos = 0;
    seen = new boolean[65536];
    for (short s : array) {
      if (!seen[s & 0xFFFF]) {
        seen[s & 0xFFFF] = true;
        result[pos++] = s;
      }
    }
    return result;
  }

  public static int[] distinct(int[] array) {
    if (array == null) return null;
    Set<Integer> seen = new HashSet<>(array.length);
    int count = 0;
    for (int v : array) {
      if (seen.add(v)) count++;
    }
    int[] result = new int[count];
    seen.clear();
    int pos = 0;
    for (int v : array) {
      if (seen.add(v)) result[pos++] = v;
    }
    return result;
  }

  public static long[] distinct(long[] array) {
    if (array == null) return null;
    Set<Long> seen = new HashSet<>(array.length);
    int count = 0;
    for (long v : array) {
      if (seen.add(v)) count++;
    }
    long[] result = new long[count];
    seen.clear();
    int pos = 0;
    for (long v : array) {
      if (seen.add(v)) result[pos++] = v;
    }
    return result;
  }

  public static char[] distinct(char[] array) {
    if (array == null) return null;
    boolean[] seen = new boolean[65536];
    int count = 0;
    for (char c : array) {
      if (!seen[c]) {
        seen[c] = true;
        count++;
      }
    }
    char[] result = new char[count];
    int pos = 0;
    seen = new boolean[65536];
    for (char c : array) {
      if (!seen[c]) {
        seen[c] = true;
        result[pos++] = c;
      }
    }
    return result;
  }

  public static float[] distinct(float[] array) {
    if (array == null) return null;
    Set<Float> seen = new HashSet<>(array.length);
    int count = 0;
    for (float v : array) {
      if (seen.add(v)) count++;
    }
    float[] result = new float[count];
    seen.clear();
    int pos = 0;
    for (float v : array) {
      if (seen.add(v)) result[pos++] = v;
    }
    return result;
  }

  public static double[] distinct(double[] array) {
    if (array == null) return null;
    Set<Double> seen = new HashSet<>(array.length);
    int count = 0;
    for (double v : array) {
      if (seen.add(v)) count++;
    }
    double[] result = new double[count];
    seen.clear();
    int pos = 0;
    for (double v : array) {
      if (seen.add(v)) result[pos++] = v;
    }
    return result;
  }

  public static boolean[] distinct(boolean[] array) {
    if (array == null) return null;
    int trueIdx = -1, falseIdx = -1;
    for (int i = 0; i < array.length && (trueIdx < 0 || falseIdx < 0); i++) {
      if (array[i] && trueIdx < 0) trueIdx = i;
      else if (!array[i] && falseIdx < 0) falseIdx = i;
    }
    if (trueIdx >= 0 && falseIdx >= 0) {
      return trueIdx < falseIdx ? new boolean[]{true, false} : new boolean[]{false, true};
    }
    if (trueIdx >= 0) return new boolean[]{true};
    if (falseIdx >= 0) return new boolean[]{false};
    return new boolean[0];
  }

  // ==================== countOf ====================

  /**
   * 统计指定值在数组中出现的次数。
   *
   * @param array 原数组，{@code null} 返回 0
   * @param value 要统计的值
   * @param <T>   元素类型
   * @return 出现次数，数组为 {@code null} 时返回 0
   */
  public static <T> int countOf(T[] array, T value) {
    if (array == null) return 0;
    int count = 0;
    for (T t : array) {
      if (Objects.equals(value, t)) count++;
    }
    return count;
  }

  public static int countOf(byte[] array, byte value) {
    if (array == null) return 0;
    int c = 0;
    for (byte b : array) {
      if (b == value) c++;
    }
    return c;
  }

  public static int countOf(short[] array, short value) {
    if (array == null) return 0;
    int c = 0;
    for (short s : array) {
      if (s == value) c++;
    }
    return c;
  }

  public static int countOf(int[] array, int value) {
    if (array == null) return 0;
    int c = 0;
    for (int v : array) {
      if (v == value) c++;
    }
    return c;
  }

  public static int countOf(long[] array, long value) {
    if (array == null) return 0;
    int c = 0;
    for (long v : array) {
      if (v == value) c++;
    }
    return c;
  }

  public static int countOf(char[] array, char value) {
    if (array == null) return 0;
    int c = 0;
    for (char ch : array) {
      if (ch == value) c++;
    }
    return c;
  }

  public static int countOf(float[] array, float value) {
    if (array == null) return 0;
    int c = 0;
    for (float v : array) {
      if (v == value) c++;
    }
    return c;
  }

  public static int countOf(double[] array, double value) {
    if (array == null) return 0;
    int c = 0;
    for (double v : array) {
      if (v == value) c++;
    }
    return c;
  }

  public static int countOf(boolean[] array, boolean value) {
    if (array == null) return 0;
    int c = 0;
    for (boolean b : array) {
      if (b == value) c++;
    }
    return c;
  }

}
