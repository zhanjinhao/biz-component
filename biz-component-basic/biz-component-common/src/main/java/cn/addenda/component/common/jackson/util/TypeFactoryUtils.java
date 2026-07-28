package cn.addenda.component.common.jackson.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

  /**
   * Extract the type argument at the given index from a TypeReference.
   * <p>For {@code new TypeReference<List<User>>() {}} with index 0 returns {@code User.class}.
   */
  public static JavaType getTypeArgument(TypeReference<?> typeReference, int index) {
    List<JavaType> bindings = getTypeArguments(typeReference);
    if (index < 0 || index >= bindings.size()) {
      throw new IndexOutOfBoundsException(
          "Type has " + bindings.size() + " type parameter(s), index " + index + " is out of bounds.");
    }
    return bindings.get(index);
  }

  /**
   * Extract all type arguments from a TypeReference.
   * <p>For {@code new TypeReference<List<User>>() {}} returns {@code [User.class]}.
   * For a non-parameterized type returns an empty list.
   */
  public static List<JavaType> getTypeArguments(TypeReference<?> typeReference) {
    return new ArrayList<>(construct(typeReference).getBindings().getTypeParameters());
  }

}
