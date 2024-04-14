package cn.addenda.component.idempotence;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author addenda
 * @since 2023/7/28 17:25
 */
@Configuration
public class IdempotenceConfiguration implements ImportAware {

  protected AnnotationAttributes annotationAttributes;

  @Override
  public void setImportMetadata(AnnotationMetadata importMetadata) {
    String name = EnableIdempotenceManagement.class.getName();
    this.annotationAttributes = AnnotationAttributes.fromMap(
            importMetadata.getAnnotationAttributes(name, false));
    if (this.annotationAttributes == null) {
      throw new IllegalArgumentException(
              name + " is not present on importing class " + importMetadata.getClassName());
    }
  }

  @Bean
  public IdempotenceInterceptor idempotenceInterceptor() {
    IdempotenceInterceptor idempotenceInterceptor = new IdempotenceInterceptor();
    idempotenceInterceptor.setNamespace(annotationAttributes.getString("namespace"));
    return idempotenceInterceptor;
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public IdempotenceAdvisor idempotenceAdvisor(IdempotenceInterceptor methodInterceptor) {
    IdempotenceAdvisor idempotenceAdvisor = new IdempotenceAdvisor();
    idempotenceAdvisor.setAdvice(methodInterceptor);
    idempotenceAdvisor.setOrder(annotationAttributes.<Integer>getNumber("order"));
    return idempotenceAdvisor;
  }

}
