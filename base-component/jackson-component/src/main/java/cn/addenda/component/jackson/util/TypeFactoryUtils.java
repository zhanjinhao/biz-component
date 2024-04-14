package cn.addenda.component.jackson.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author addenda
 * @since 2022/12/21 14:31
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeFactoryUtils {

  private static final TypeFactory typeFactory = TypeFactory.defaultInstance();

  public static JavaType construct(Class<?> clazz) {
    return typeFactory.constructType(clazz);
  }

  public static JavaType construct(TypeReference<?> typeReference) {
    return typeFactory.constructType(typeReference);
  }

  public static JavaType constructList(Class<?> clazz) {
    return typeFactory.constructParametricType(List.class, clazz);
  }

  public static JavaType constructList(JavaType javaType) {
    return typeFactory.constructParametricType(List.class, javaType);
  }

  public static JavaType constructSet(Class<?> clazz) {
    return typeFactory.constructParametricType(Set.class, clazz);
  }

  public static JavaType constructSet(JavaType javaType) {
    return typeFactory.constructParametricType(Set.class, javaType);
  }

  public static JavaType constructMap(Class<?> keyClazz, Class<?> valueClazz) {
    return typeFactory.constructParametricType(Map.class, keyClazz, valueClazz);
  }

  public static JavaType constructMap(JavaType keyType, JavaType valueType) {
    return typeFactory.constructParametricType(Map.class, keyType, valueType);
  }

  public static JavaType constructArray(Class<?> clazz) {
    return typeFactory.constructArrayType(clazz);
  }

  public static JavaType constructArray(JavaType javaType) {
    return typeFactory.constructArrayType(javaType);
  }

  public static JavaType constructParametricType(Class<?> clazz, JavaType... innerTypes) {
    if (innerTypes == null || innerTypes.length == 0) {
      return construct(clazz);
    }
    return typeFactory.constructParametricType(clazz, innerTypes);
  }

  public static JavaType constructParametricType(Class<?> clazz, Class<?>... innerClazzes) {
    if (innerClazzes == null || innerClazzes.length == 0) {
      return construct(clazz);
    }
    return typeFactory.constructParametricType(clazz, innerClazzes);
  }

}
