package cn.addenda.component.ratelimitationhelper;

import cn.addenda.component.allocator.NamedExpiredAllocator;
import cn.addenda.component.ratelimiter.RateLimiter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * @author addenda
 * @since 2023/8/26 22:58
 */
@Configuration
public class RateLimitationConfiguration implements ImportAware {

  protected AnnotationAttributes annotationAttributes;

  @Override
  public void setImportMetadata(AnnotationMetadata importMetadata) {
    String name = EnableRateLimitation.class.getName();
    this.annotationAttributes = AnnotationAttributes.fromMap(
            importMetadata.getAnnotationAttributes(name, false));
    if (this.annotationAttributes == null) {
      throw new IllegalArgumentException(
              name + " is not present on importing class " + importMetadata.getClassName());
    }
  }

  @Bean
  public RateLimitationInterceptor rateLimitationInterceptor(List<NamedExpiredAllocator<? extends RateLimiter>> allocatorList) {
    RateLimitationInterceptor rateLimitationInterceptor = new RateLimitationInterceptor(allocatorList);
    rateLimitationInterceptor.setNamespace(annotationAttributes.getString("namespace"));
    return rateLimitationInterceptor;
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public RateLimitationAdvisor rateLimitationAdvisor(RateLimitationInterceptor methodInterceptor) {
    RateLimitationAdvisor rateLimitationAdvisor = new RateLimitationAdvisor();
    rateLimitationAdvisor.setAdvice(methodInterceptor);
    rateLimitationAdvisor.setOrder(annotationAttributes.<Integer>getNumber("order"));
    return rateLimitationAdvisor;
  }

}
