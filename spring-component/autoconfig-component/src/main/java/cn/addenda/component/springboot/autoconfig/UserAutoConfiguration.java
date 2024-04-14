package cn.addenda.component.springboot.autoconfig;

import cn.addenda.component.user.UserTransmitFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 用户配置自动装配
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class UserAutoConfiguration {

  /**
   * 用户信息传递过滤器
   */
  @Bean
  public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter() {
    FilterRegistrationBean<UserTransmitFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new UserTransmitFilter());
    registration.addUrlPatterns("/*");
    registration.setOrder(100);
    return registration;
  }

}
