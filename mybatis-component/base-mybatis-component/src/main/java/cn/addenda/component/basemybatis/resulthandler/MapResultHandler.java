package cn.addenda.component.basemybatis.resulthandler;

import cn.addenda.component.basemybatis.BaseMybatisException;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * todo 需要测试Mybatis如何返回Map数据结构以及@MapKey怎么使用
 *
 * @since addenda
 * @since 2022/2/7 16:38
 */
public class MapResultHandler<T> implements ResultHandler<Map<String, T>> {

  /**
   * 用LinkedHashMap保证有序
   */
  private final Map<T, T> resultMap = new LinkedHashMap<>();

  private final boolean repeatedValid;

  private final boolean fifo;

  private final boolean trimKeyNull;

  public static final String KEY_REP = "key";
  public static final String VALUE_REP = "value";

  /**
   * 当不允许重复时，只有参数 trimKeyNull 可以用
   */
  public MapResultHandler(boolean trimKeyNull) {
    this(false, true, trimKeyNull);
  }

  /**
   * 当允许重复时，参数 fifo 和 trimKeyNull 可以用
   */
  public MapResultHandler(boolean fifo, boolean trimKeyNull) {
    this(true, fifo, trimKeyNull);
  }

  private MapResultHandler(boolean repeatedValid, boolean fifo, boolean trimKeyNull) {
    this.repeatedValid = repeatedValid;
    this.fifo = fifo;
    this.trimKeyNull = trimKeyNull;
  }

  @Override
  public void handleResult(ResultContext<? extends Map<String, T>> resultContext) {
    Map<String, T> resultObject = resultContext.getResultObject();
    // key 和 value 都为null的时候，resultObject为null
    if (resultObject == null) {
      return;
    }
    // key 可能为 null
    T key = resultObject.get(KEY_REP);
    if (key == null && trimKeyNull) {
      return;
    }
    T value = resultObject.get(VALUE_REP);

    if (!resultMap.containsKey(key)) {
      resultMap.put(key, value);
    } else {
      if (repeatedValid) {
        if (fifo) {
          // non-op
        } else {
          resultMap.put(key, value);
        }
      } else {
        throw new BaseMybatisException("出现了重复的key, key: " + key);
      }
    }
  }

  public Map<T, T> getResult() {
    return resultMap;
  }

}
