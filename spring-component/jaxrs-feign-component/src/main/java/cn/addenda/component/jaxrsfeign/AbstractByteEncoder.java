package cn.addenda.component.jaxrsfeign;

import feign.RequestTemplate;
import feign.Util;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;

import java.lang.reflect.Type;

/**
 * @author addenda
 * @since 2023/8/2 16:06
 */
public abstract class AbstractByteEncoder implements Encoder {

  protected JacksonEncoder jacksonEncoder;

  protected AbstractByteEncoder(JacksonEncoder jacksonEncoder) {
    this.jacksonEncoder = jacksonEncoder;
  }

  @Override
  public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
    template.body(convert(object, bodyType), Util.UTF_8);
  }

  /**
   * @param object   http body的原始参数
   * @param bodyType http body的类型
   * @return UTF_8字符集下的字节数组
   */
  protected abstract byte[] convert(Object object, Type bodyType);

}
