package cn.addenda.component.springboot.autoconfig;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RedissonAutoConfiguration implements EnvironmentAware {

  private Environment environment;

  @ConditionalOnMissingBean(RedissonClient.class)
  @Bean
  public RedissonClient redissonClient() {
    // 配置
    Config config = new Config();
    config.useSingleServer()
            .setAddress("redis://"
                    + environment.resolvePlaceholders("${redis.host}")
                    + ":"
                    + environment.resolvePlaceholders("${redis.port}"))
            .setPassword(environment.resolvePlaceholders("${redis.password}"));
    // 创建RedissonClient对象
    return Redisson.create(config);
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

}
