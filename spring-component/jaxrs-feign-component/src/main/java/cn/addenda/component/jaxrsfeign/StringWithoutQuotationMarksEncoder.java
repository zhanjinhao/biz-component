package cn.addenda.component.jaxrsfeign;

import feign.RequestTemplate;
import feign.Util;
import feign.codec.EncodeException;
import feign.jackson.JacksonEncoder;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class StringWithoutQuotationMarksEncoder extends AbstractByteEncoder {

  public StringWithoutQuotationMarksEncoder(JacksonEncoder jacksonEncoder) {
    super(jacksonEncoder);
  }

  @Override
  protected byte[] convert(Object object, Type bodyType) {
    if (object == null) {
      return null;
    }
    return object.toString().getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
    if (String.class.equals(bodyType)) {
      template.body(convert(object, bodyType), Util.UTF_8);
    } else {
      jacksonEncoder.encode(object, bodyType, template);
    }
  }

}
