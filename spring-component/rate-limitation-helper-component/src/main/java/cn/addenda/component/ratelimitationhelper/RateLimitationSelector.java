package cn.addenda.component.ratelimitationhelper;

import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author addenda
 * @since 2023/8/26 22:57
 */
public class RateLimitationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
            AutoProxyRegistrar.class.getName(),
            RateLimitationConfiguration.class.getName()};
    }

}
