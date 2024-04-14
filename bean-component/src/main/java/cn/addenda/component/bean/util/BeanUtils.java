package cn.addenda.component.bean.util;

import cn.addenda.component.bean.pojo.Binary;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;

/**
 * 这个工具类只能用于Controller层向外提供数据（DTO）时做类型转换。业务对象之间的属性拷贝需要自己手动给字段赋值或使用MapStruct这种非反射的工具。
 *
 * @author addenda
 * @since 2022/2/7 12:37
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtils {

  public static <T> T copyProperties(Object source, T target) {
    org.springframework.beans.BeanUtils.copyProperties(source, target);
    return target;
  }

  public static <T> T copyPropertiesOnly(Object source, T target, Predicate<Binary<String, Object>> predicate, String... onlyProperties) {
    Set<String> ignorePropertyNames = extractPropertyName(source, true, predicate, onlyProperties);
    String[] result = new String[ignorePropertyNames.size()];
    org.springframework.beans.BeanUtils.copyProperties(source, target, ignorePropertyNames.toArray(result));
    return target;
  }

  public static <T> T copyPropertiesOnly(Object source, T target, String... onlyProperties) {
    return copyPropertiesOnly(source, target, o -> false, onlyProperties);
  }

  public static <T> T copyPropertiesIgnore(Object source, T target, Predicate<Binary<String, Object>> predicate, String... ignoreProperties) {
    Set<String> ignorePropertyNames = extractPropertyName(source, false, predicate, ignoreProperties);
    String[] result = new String[ignorePropertyNames.size()];
    org.springframework.beans.BeanUtils.copyProperties(source, target, ignorePropertyNames.toArray(result));
    return target;
  }

  public static <T> T copyPropertiesIgnore(Object source, T target, String... ignoreProperties) {
    return copyPropertiesIgnore(source, target, o -> false, ignoreProperties);
  }

  public static <T> T copyPropertiesIgnoreNull(Object source, T target, String... ignoreProperties) {
    return copyPropertiesIgnore(source, target, BeanUtils::ifNull, ignoreProperties);
  }

  public static <T> T copyPropertiesIgnoreBlank(Object source, T target, String... ignoreProperties) {
    return copyPropertiesIgnore(source, target, BeanUtils::ifBlank, ignoreProperties);
  }

  public static <T, S> T copyProperties(S source, Class<T> clazz) {
    return Optional.ofNullable(source)
            .map(s -> copyProperties(source, newInstance(clazz)))
            .orElse(null);
  }

  public static <T> T copyPropertiesOnly(Object source, Class<T> clazz, Predicate<Binary<String, Object>> predicate, String... onlyProperties) {
    return Optional.ofNullable(source)
            .map(s -> copyPropertiesOnly(source, newInstance(clazz), predicate, onlyProperties))
            .orElse(null);
  }

  public static <T> T copyPropertiesOnly(Object source, Class<T> clazz, String... onlyProperties) {
    return copyPropertiesOnly(source, clazz, a -> false, onlyProperties);
  }

  public static <T, S> T copyPropertiesIgnore(S source, Class<T> clazz, Predicate<Binary<String, Object>> predicate, String... ignoreProperties) {
    return Optional.ofNullable(source)
            .map(s -> copyPropertiesIgnore(source, newInstance(clazz), predicate, ignoreProperties))
            .orElse(null);
  }

  public static <T, S> T copyPropertiesIgnore(S source, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesIgnore(source, clazz, a -> false, ignoreProperties);
  }

  public static <T, S> T copyPropertiesIgnoreNull(S source, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesIgnore(source, clazz, BeanUtils::ifNull, ignoreProperties);
  }

  public static <T, S> T copyPropertiesIgnoreBlank(S source, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesIgnore(source, clazz, BeanUtils::ifBlank, ignoreProperties);
  }

  public static <T, S> List<T> copyProperties(Iterable<S> sources, Class<T> clazz) {
    return Optional.ofNullable(sources)
            .map(each -> {
              List<T> list = new ArrayList<>();
              each.forEach(item -> list.add(copyProperties(item, clazz)));
              return list;
            })
            .orElse(null);
  }

  public static <T, S> List<T> copyPropertiesOnly(Iterable<S> sources, Class<T> clazz, Predicate<Binary<String, Object>> predicate, String... ignoreProperties) {
    return Optional.ofNullable(sources)
            .map(each -> {
              List<T> list = new ArrayList<>();
              each.forEach(item -> list.add(copyPropertiesOnly(item, clazz, predicate, ignoreProperties)));
              return list;
            })
            .orElse(null);
  }

  public static <T, S> List<T> copyPropertiesOnly(Iterable<S> sources, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesOnly(sources, clazz, a -> false, ignoreProperties);
  }

  public static <T, S> List<T> copyPropertiesIgnore(Iterable<S> sources, Class<T> clazz, Predicate<Binary<String, Object>> predicate, String... ignoreProperties) {
    return Optional.ofNullable(sources)
            .map(each -> {
              List<T> list = new ArrayList<>();
              each.forEach(item -> list.add(copyPropertiesIgnore(item, clazz, predicate, ignoreProperties)));
              return list;
            })
            .orElse(null);
  }

  public static <T, S> List<T> copyPropertiesIgnore(Iterable<S> sources, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesIgnore(sources, clazz, a -> false, ignoreProperties);
  }

  public static <T, S> List<T> copyPropertiesIgnoreNull(Iterable<S> sources, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesIgnore(sources, clazz, BeanUtils::ifNull, ignoreProperties);
  }

  public static <T, S> List<T> copyPropertiesIgnoreBlank(Iterable<S> sources, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesIgnore(sources, clazz, BeanUtils::ifBlank, ignoreProperties);
  }

  public static <T, S> T[] copyProperties(S[] sources, Class<T> clazz) {
    return Optional.ofNullable(sources)
            .map(each -> {
              @SuppressWarnings("unchecked")
              T[] targetArray = (T[]) Array.newInstance(clazz, sources.length);
              for (int i = 0; i < targetArray.length; i++) {
                targetArray[i] = copyProperties(sources[i], clazz);
              }
              return targetArray;
            })
            .orElse(null);
  }

  public static <T, S> T[] copyPropertiesOnly(S[] sources, Class<T> clazz, Predicate<Binary<String, Object>> predicate, String... ignoreProperties) {
    return Optional.ofNullable(sources)
            .map(each -> {
              @SuppressWarnings("unchecked")
              T[] targetArray = (T[]) Array.newInstance(clazz, sources.length);
              for (int i = 0; i < targetArray.length; i++) {
                targetArray[i] = copyPropertiesOnly(sources[i], clazz, predicate, ignoreProperties);
              }
              return targetArray;
            })
            .orElse(null);
  }

  public static <T, S> T[] copyPropertiesOnly(S[] sources, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesOnly(sources, clazz, a -> false, ignoreProperties);
  }


  public static <T, S> T[] copyPropertiesIgnore(S[] sources, Class<T> clazz, Predicate<Binary<String, Object>> predicate, String... ignoreProperties) {
    return Optional.ofNullable(sources)
            .map(each -> {
              @SuppressWarnings("unchecked")
              T[] targetArray = (T[]) Array.newInstance(clazz, sources.length);
              for (int i = 0; i < targetArray.length; i++) {
                targetArray[i] = copyPropertiesIgnore(sources[i], clazz, predicate, ignoreProperties);
              }
              return targetArray;
            })
            .orElse(null);
  }

  public static <T, S> T[] copyPropertiesIgnore(S[] sources, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesIgnore(sources, clazz, a -> false, ignoreProperties);
  }

  public static <T, S> T[] copyPropertiesIgnoreNull(S[] sources, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesIgnore(sources, clazz, BeanUtils::ifNull, ignoreProperties);
  }

  public static <T, S> T[] copyPropertiesIgnoreBlank(S[] sources, Class<T> clazz, String... ignoreProperties) {
    return copyPropertiesIgnore(sources, clazz, BeanUtils::ifBlank, ignoreProperties);
  }

  /**
   * @param list vital: 集合内部元素的属性必须具有无参构造方法。
   * @return 不会影响集合中的元素
   */
  public static <T> T mergeObject(List<T> list, String... ignoreProperties) {
    if (list == null || list.isEmpty()) {
      return null;
    }
    int size = list.size();
    T stand = list.get(0);
    Class<?> aClass = stand.getClass();
    @SuppressWarnings("unchecked")
    T result = (T) newInstance(aClass);
    org.springframework.beans.BeanUtils.copyProperties(stand, result);
    for (int i = 1; i < size; i++) {
      copyPropertiesIgnoreNull(list.get(i), result, ignoreProperties);
    }
    return result;
  }

  /**
   * @param ifNon      true->predicate & properties ；false->!(predicate & properties)
   * @param predicate  提取属性名称的断言
   * @param properties 手动指定的属性
   */
  public static Set<String> extractPropertyName(Object source, boolean ifNon, Predicate<Binary<String, Object>> predicate, String... properties) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> propertiesSet = null;
    if (properties != null) {
      propertiesSet = new HashSet<>(Arrays.asList(properties));
    }

    Set<String> emptyNames = new HashSet<>();
    for (PropertyDescriptor pd : pds) {
      String name = pd.getName();
      Object srcValue = src.getPropertyValue(name);
      if (ifNon) {
        if (!(Boolean.TRUE.equals(predicate.test(new Binary<>(name, srcValue)))
                || (propertiesSet != null && propertiesSet.contains(name)))) {
          emptyNames.add(name);
        }
      } else {
        if (Boolean.TRUE.equals(predicate.test(new Binary<>(name, srcValue)))
                || (propertiesSet != null && propertiesSet.contains(name))) {
          emptyNames.add(name);
        }
      }
    }
    return emptyNames;
  }

  private static boolean ifNull(Binary<String, Object> binary) {
    return binary.getF2() == null;
  }

  public static final Character CHAR_BLANK = '\u0000';

  private static boolean ifBlank(Binary<String, Object> binary) {
    Object object = binary.getF2();
    if (object == null) {
      return true;
    }
    if (object instanceof String) {
      return !StringUtils.hasLength((CharSequence) object);
    }
    if (object instanceof CharSequence) {
      return !StringUtils.hasLength((CharSequence) object);
    }
    if (object instanceof Character) {
      return CHAR_BLANK.equals(object);
    }
    return false;
  }

  @SneakyThrows
  private static <T> T newInstance(Class<T> clazz) {
    return clazz.newInstance();
  }

}
