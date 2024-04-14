package cn.addenda.component.dynamicdatasource;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

/**
 * @author addenda
 * @since 2022/3/2 23:02
 */
@Configuration
public class MultiDataSourceConfiguration implements ImportAware {

  @Nullable
  protected AnnotationAttributes annotationAttributes;

  @Override
  public void setImportMetadata(AnnotationMetadata importMetadata) {
    String name = EnableMultiDataSource.class.getName();
    this.annotationAttributes = AnnotationAttributes.fromMap(
            importMetadata.getAnnotationAttributes(name, false));
    if (this.annotationAttributes == null) {
      throw new IllegalArgumentException(
              name + " is not present on importing class " + importMetadata.getClassName());
    }
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public MultiDataSourceAdvisor multiDataSourceAdvisor() {
    MultiDataSourceAdvisor multiDataSourceAdvisor = new MultiDataSourceAdvisor();
    multiDataSourceAdvisor.setAdvice(new MultiDataSourceMethodInterceptor());
    if (this.annotationAttributes != null) {
      multiDataSourceAdvisor.setOrder(annotationAttributes.<Integer>getNumber("order"));
    }
    return multiDataSourceAdvisor;
  }

}
