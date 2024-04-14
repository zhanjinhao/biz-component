package cn.addenda.component.test.jarxsfeign;

import cn.addenda.component.basaspring.context.ValueResolverHelper;
import cn.addenda.component.jaxrsfeign.EnableSimpleFeignClients;
import cn.addenda.component.jaxrsfeign.SimpleStringDecoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author addenda
 * @since 2023/7/24 9:49
 */
@Configuration
@PropertySource("classpath:cn/addenda/component/test/jaxrsfeign/system.properties")
@EnableSimpleFeignClients(basePackages = {"cn.addenda.component.test.jarxsfeign.remote"})
public class SpringFeignCoreTestConfiguration {

  @Bean
  public JacksonEncoder jacksonEncoder() {
    return new JacksonEncoder();
  }

  @Bean
  public JacksonDecoder jacksonDecoder() {
    return new JacksonDecoder();
  }

  @Bean
  public SimpleStringDecoder simpleStringDecoder(JacksonDecoder jacksonDecoder) {
    return new SimpleStringDecoder(jacksonDecoder);
  }

  @Bean
  public ApacheHttpClient apacheHttpClient() {
    return new ApacheHttpClient();
  }

  @Bean
  public ValueResolverHelper valueResolverHelper() {
    return new ValueResolverHelper();
  }

}
