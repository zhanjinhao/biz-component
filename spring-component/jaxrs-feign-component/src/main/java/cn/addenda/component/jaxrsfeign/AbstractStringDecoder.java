package cn.addenda.component.jaxrsfeign;

import com.fasterxml.jackson.core.type.TypeReference;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @author addenda
 * @since 2023/7/27 15:52
 */
@Slf4j
public abstract class AbstractStringDecoder implements Decoder {

  protected JacksonDecoder jacksonDecoder;

  protected AbstractStringDecoder(JacksonDecoder jacksonDecoder) {
    this.jacksonDecoder = jacksonDecoder;
  }

  @Override
  public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
    if (response.status() == 404 || response.status() == 204) {
      return Util.emptyValueOf(type);
    }
    if (response.body() == null) {
      return null;
    }

    return convert(toString(response), type);
  }

  private String toString(Response response) throws IOException {
    Response.Body body = response.body();
    if (response.status() == 404 || response.status() == 204 || body == null) {
      return null;
    }
    return Util.toString(body.asReader(Util.UTF_8));
  }

  /**
   * @param str  提供方返回的字符串
   * @param type 返回值的类型
   */
  protected abstract Object convert(String str, Type type);

  public TypeReference<?> createTypeReference(Type type) {
    TypeReference<Object> reference = new TypeReference<Object>() {
    };
    Field typeField = ReflectionUtils.findField(TypeReference.class, "_type");
    ReflectionUtils.makeAccessible(typeField);
    ReflectionUtils.setField(typeField, reference, type);
    return reference;
  }

}
