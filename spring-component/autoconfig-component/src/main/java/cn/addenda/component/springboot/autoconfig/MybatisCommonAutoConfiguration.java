package cn.addenda.component.springboot.autoconfig;

import cn.addenda.component.basemybatis.helper.MybatisBatchDmlHelper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author addenda
 * @since 2023/8/18 15:30
 */
@AutoConfigureAfter({MybatisAutoConfiguration.class})
@Configuration
public class MybatisCommonAutoConfiguration {

  @ConditionalOnBean(value = {SqlSessionFactory.class})
  @ConditionalOnMissingBean(value = {MybatisBatchDmlHelper.class})
  @Bean
  public MybatisBatchDmlHelper mybatisBatchOperationHelper(SqlSessionFactory sqlSessionFactory) {
    return new MybatisBatchDmlHelper(sqlSessionFactory);
  }

}
