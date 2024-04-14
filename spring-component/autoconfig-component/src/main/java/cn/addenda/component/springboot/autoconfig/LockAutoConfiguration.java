package cn.addenda.component.springboot.autoconfig;

import cn.addenda.component.lockhelper.LockConfigurer;
import cn.addenda.component.lockhelper.LockHelper;
import cn.addenda.component.redis.allocator.RedissonLockAllocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author addenda
 * @since 2022/11/30 19:43
 */
@Configuration
public class LockAutoConfiguration implements EnvironmentAware {

  private Environment environment;

  @ConditionalOnMissingBean(LockConfigurer.class)
  @Bean
  public LockConfigurer lockConfigurer(RedissonLockAllocator redissonLockAllocator) {
    return new LockConfigurer(redissonLockAllocator);
  }

  @ConditionalOnMissingBean(LockHelper.class)
  @Bean
  public LockHelper lockHelper(RedissonLockAllocator redissonLockHelper) {
    return new LockHelper(environment.resolvePlaceholders("${spring.application.name}"), redissonLockHelper);
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

}
