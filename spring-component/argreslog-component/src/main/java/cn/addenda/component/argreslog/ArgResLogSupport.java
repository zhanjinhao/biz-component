package cn.addenda.component.argreslog;

import cn.addenda.component.jackson.util.JacksonUtils;
import cn.addenda.component.jdk.util.StackTraceUtils;
import cn.addenda.component.lambda.TSupplier;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArgResLogSupport {

  private static final String NULL_STR = "NIL";

  private static final Map<String, AtomicLong> SEQUENCE_GENERATOR_MAP = new ConcurrentHashMap<>();

  private static final AtomicLong GLOBAL_SEQUENCE = new AtomicLong(0L);

  private static final ThreadLocal<Deque<ArgResBo>> ARG_RES_DEQUE_TL = ThreadLocal.withInitial(ArrayDeque::new);

  protected static <R> R invoke(Object[] arguments, TSupplier<R> supplier, String callerInfo) throws Throwable {
    if (callerInfo == null) {
      callerInfo = StackTraceUtils.getDetailedCallerInfo(true,
//                ReflectiveMethodInvocation.class.getName(),
//                "org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor",
              ArgResLogSupport.class.getName(),
              ArgResLogMethodInterceptor.class.getName(),
              ArgResLogUtils.class.getName());
    }
    return doInvoke(arguments, supplier, callerInfo);
  }

  private static <R> R doInvoke(Object[] arguments, TSupplier<R> supplier, String callerInfo) throws Throwable {
    long globalSequence = GLOBAL_SEQUENCE.getAndIncrement();
    long sequence = SEQUENCE_GENERATOR_MAP.computeIfAbsent(callerInfo, s -> new AtomicLong(0L)).getAndIncrement();

    ArgResBo cur = new ArgResBo();
    Deque<ArgResBo> argResBoDeque = ARG_RES_DEQUE_TL.get();
    if (argResBoDeque.isEmpty()) {
      argResBoDeque.push(cur);
    } else {
      ArgResBo parent = argResBoDeque.peek();
      parent.getChildren().add(cur);
      argResBoDeque.push(cur);
    }

    cur.setCallerInfo(callerInfo);
    cur.setSequence(globalSequence + "-" + sequence);
    cur.setArgument(arguments == null || arguments.length == 0 ?
            "No arguments." : toJsonStr(arguments));

    long start = System.currentTimeMillis();

    try {
      try {
        R result = supplier.get();
        cur.setResult(toJsonStr(result));
        return result;
      } catch (Throwable throwable) {
        cur.setError(toJsonStr(throwable));
        throw throwable;
      } finally {
        cur.setCost(System.currentTimeMillis() - start);
      }
    } finally {
      ArgResBo pop = argResBoDeque.pop();
      if (argResBoDeque.isEmpty()) {
        log.info(toJsonStr(pop));
        ARG_RES_DEQUE_TL.remove();
      }
    }
  }

  @Setter
  @Getter
  @ToString
  @NoArgsConstructor
  public static class ArgResBo {

    private String sequence;
    private String callerInfo;

    private String argument;
    private String result;
    private String error;
    private Long cost;

    private List<ArgResBo> children = new ArrayList<>();

  }

  private static String toJsonStr(Object o) {
    if (o == null) {
      return NULL_STR;
    }

    if (o instanceof Throwable) {
      Throwable throwable = (Throwable) o;
      StringWriter sw = new StringWriter();
      throwable.printStackTrace(new PrintWriter(sw));
      return sw.toString();
    }

    return JacksonUtils.toStr(o);
  }

  /**
   * 这种方法输出的字符串json短，但是不方便反序列化为对象。<p/>
   * 先保留着，后续看看有没有用武之地。
   */
  private static String toStr(Object o) {
    if (o == null) {
      return NULL_STR;
    } else if (o instanceof Collection) {
      Collection<?> collection = (Collection<?>) o;
      return collection.stream().map(ArgResLogSupport::toStr).collect(Collectors.joining(",", "[", "]"));
    } else if (o.getClass().isArray()) {
      // A 是 B 的子类，则 A[] 是 B[] 的子类；
      // 所以 o 可以转换为 Object[]
      Object[] array = (Object[]) o;
      return Arrays.stream(array).map(ArgResLogSupport::toStr).collect(Collectors.joining(",", "[", "]"));
    } else if (o instanceof Map.Entry) {
      Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
      return toStr(entry.getKey()) + "=" + toStr(entry.getValue());
    } else if (o instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) o;
      return "{" + map.entrySet().stream().map(ArgResLogSupport::toStr).collect(Collectors.joining(",")) + "}";
    } else if (o instanceof Throwable) {
      Throwable throwable = (Throwable) o;
      StringWriter sw = new StringWriter();
      throwable.printStackTrace(new PrintWriter(sw));
      return sw.toString();
    }
    return o.toString();
  }

}
