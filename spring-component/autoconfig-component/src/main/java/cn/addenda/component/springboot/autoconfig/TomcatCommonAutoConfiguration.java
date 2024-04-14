package cn.addenda.component.springboot.autoconfig;

import cn.addenda.component.tomcat.NonFoundTomcatWebServerFactoryCustomizer;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.UpgradeProtocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author addenda
 * @since 2023/8/19 13:01
 */
@Configuration
public class TomcatCommonAutoConfiguration {

  @ConditionalOnClass({Tomcat.class, UpgradeProtocol.class})
  @Bean
  public NonFoundTomcatWebServerFactoryCustomizer nonFoundTomcatWebServerFactoryCustomizer() {
    return new NonFoundTomcatWebServerFactoryCustomizer();
  }

}
