package cn.addenda.component.jackson.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author addenda
 * @since 2022/2/14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeReferenceUtils {

  private static final Class<?> collectionClass = java.util.Collection.class;
  private static final Class<?> typeReferenceClass = TypeReference.class;

  @SneakyThrows
  public static <T> TypeReference<T> getCollectionItemTypeReference(TypeReference<T> typeReference) {
    Type superClass = typeReference.getClass().getGenericSuperclass();
    if (superClass instanceof Class<?>) {
      throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
    }
    ParameterizedTypeImpl actualTypeArgument = (ParameterizedTypeImpl) ((ParameterizedType) superClass).getActualTypeArguments()[0];
    if (!actualTypeArgument.getRawType().isAssignableFrom(collectionClass)) {
      return typeReference;
    }
    Class<?> itemClass = (Class<?>) actualTypeArgument.getActualTypeArguments()[0];
    Field _typeField = typeReferenceClass.getDeclaredField("_type");
    _typeField.setAccessible(true);
    _typeField.set(typeReference, itemClass);
    return typeReference;
  }

  @SneakyThrows
  public static <T> TypeReference<T> newTypeReference(Class<T> clazz) {
    TypeReference<Object> typeReference = new TypeReference<Object>() {
    };

    Field _typeField = typeReferenceClass.getDeclaredField("_type");
    _typeField.setAccessible(true);
    _typeField.set(typeReference, clazz);
    return (TypeReference<T>) typeReference;

  }

}
