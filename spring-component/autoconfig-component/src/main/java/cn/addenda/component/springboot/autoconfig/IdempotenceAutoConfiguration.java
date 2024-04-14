package cn.addenda.component.springboot.autoconfig;

import cn.addenda.component.idempotenct.DbStorageCenter;
import cn.addenda.component.idempotenct.IdempotenceHelper;
import cn.addenda.component.idempotenct.RedisStorageCenter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;

/**
 * @author addenda
 * @since 2023/9/30 10:28
 */
@Configuration
public class IdempotenceAutoConfiguration implements EnvironmentAware {

  private Environment environment;

  @ConditionalOnMissingBean(RedisStorageCenter.class)
  @Bean
  public RedisStorageCenter redisStorageCenter(StringRedisTemplate stringRedisTemplate) {
    return new RedisStorageCenter(stringRedisTemplate);
  }

  @ConditionalOnMissingBean(DbStorageCenter.class)
  @ConditionalOnProperty(prefix = "biz-collection", name = "idempotence.db.enable", havingValue = "true")
  @Bean
  public DbStorageCenter dbStorageCenter(DataSource dataSource) {
    return new DbStorageCenter(dataSource);
  }

  @ConditionalOnMissingBean(IdempotenceHelper.class)
  @Bean
  public IdempotenceHelper idempotenceHelper() {
    IdempotenceHelper idempotenceHelper = new IdempotenceHelper();
    idempotenceHelper.setNamespace(environment.resolvePlaceholders("${spring.application.name}") + ":idempotence");
    return idempotenceHelper;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

}
