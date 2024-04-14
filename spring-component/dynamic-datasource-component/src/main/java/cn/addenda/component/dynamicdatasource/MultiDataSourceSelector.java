package cn.addenda.component.dynamicdatasource;

import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author addenda
 * @since 2022/3/2 23:04
 */
public class MultiDataSourceSelector implements ImportSelector {
  @Override
  public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    return new String[]{
            AutoProxyRegistrar.class.getName(),
            MultiDataSourceConfiguration.class.getName()};
  }
}
