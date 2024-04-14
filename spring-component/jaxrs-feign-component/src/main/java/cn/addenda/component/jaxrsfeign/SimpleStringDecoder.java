package cn.addenda.component.jaxrsfeign;

import feign.jackson.JacksonDecoder;

import java.lang.reflect.Type;

public class SimpleStringDecoder extends AbstractStringDecoder {

  public SimpleStringDecoder(JacksonDecoder jacksonDecoder) {
    super(jacksonDecoder);
  }

  @Override
  protected String convert(String str, Type type) {
    return str;
  }

}
