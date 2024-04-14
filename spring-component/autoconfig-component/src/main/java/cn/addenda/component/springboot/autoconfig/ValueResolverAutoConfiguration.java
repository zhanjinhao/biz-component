package cn.addenda.component.springboot.autoconfig;

import cn.addenda.component.basaspring.context.ValueResolverHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author addenda
 * @since 2023/8/18 15:22
 */
@Configuration
public class ValueResolverAutoConfiguration {

  @ConditionalOnMissingBean(value = ValueResolverHelper.class)
  @Bean
  public ValueResolverHelper valueResolverHelper() {
    return new ValueResolverHelper();
  }

}
