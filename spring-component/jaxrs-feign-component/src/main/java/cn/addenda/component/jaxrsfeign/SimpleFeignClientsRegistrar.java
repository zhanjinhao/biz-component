/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.addenda.component.jaxrsfeign;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Spencer Gibb
 * @author Jakub Narloch
 * @author Venil Noronha
 * @author Gang Li
 */
class SimpleFeignClientsRegistrar
        implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

  // patterned after Spring Integration IntegrationComponentScanRegistrar
  // and RibbonClientsConfigurationRegistgrar

  private ResourceLoader resourceLoader;

  private Environment environment;

  public SimpleFeignClientsRegistrar() {
  }

  static String getUrl(String url) {
    if (StringUtils.hasText(url) && !(url.startsWith("#{") && url.contains("}"))) {
      if (!url.contains("://")) {
        url = "http://" + url;
      }
      try {
        new URL(url);
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException(url + " is malformed", e);
      }
    }
    return url;
  }

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Override
  public void registerBeanDefinitions(AnnotationMetadata metadata,
                                      BeanDefinitionRegistry registry) {
    registerFeignClients(metadata, registry);
  }

  public void registerFeignClients(AnnotationMetadata metadata,
                                   BeanDefinitionRegistry registry) {
    ClassPathScanningCandidateComponentProvider scanner = getScanner();
    scanner.setResourceLoader(this.resourceLoader);

    AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
            SimpleFeignClient.class);

    scanner.addIncludeFilter(annotationTypeFilter);
    Set<String> basePackages = getBasePackages(metadata);

    for (String basePackage : basePackages) {
      Set<BeanDefinition> candidateComponents = scanner
              .findCandidateComponents(basePackage);
      for (BeanDefinition candidateComponent : candidateComponents) {
        if (candidateComponent instanceof AnnotatedBeanDefinition) {
          // verify annotated class is an interface
          AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
          AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
          Assert.isTrue(annotationMetadata.isInterface(),
                  "@FeignClient can only be specified on an interface");

          Map<String, Object> attributes = annotationMetadata
                  .getAnnotationAttributes(
                          SimpleFeignClient.class.getCanonicalName());

          registerFeignClient(registry, annotationMetadata, attributes);
        }
      }
    }
  }

  private void registerFeignClient(BeanDefinitionRegistry registry,
                                   AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
    String className = annotationMetadata.getClassName();
    BeanDefinitionBuilder definition = BeanDefinitionBuilder
            .genericBeanDefinition(SimpleFeignClientFactoryBean.class);
    definition.addPropertyValue("gateWay", getUrl(attributes));
    definition.addPropertyValue("connectTimeout", getConnectTimeout(attributes));
    definition.addPropertyValue("readTimeout", getReadTimeout(attributes));
    definition.addPropertyValue("dismissException", getDismissException(attributes));
    definition.addPropertyValue("mapNullToDefault", getMapNullToDefault(attributes));
    definition.addPropertyValue("encoderConfig", getEncoderConfig(attributes));
    definition.addPropertyValue("decoderConfig", getDecoderConfig(attributes));
    definition.addPropertyValue("defaultContentType", getDefaultContentType(attributes));
    definition.addPropertyValue("queryParamAppendEqualWhenEmpty", getQueryParamAppendEqualWhenEmpty(attributes));
    definition.addPropertyValue("clientConfig", getClientConfig(attributes));
    definition.addPropertyValue("type", className);

    definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

    AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

    // has a default, won't be null
    boolean primary = (Boolean) attributes.get("primary");

    beanDefinition.setPrimary(primary);

    BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
    BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
  }

  private Object getClientConfig(Map<String, Object> attributes) {
    return attributes.get("clientConfig");
  }

  private Object getQueryParamAppendEqualWhenEmpty(Map<String, Object> attributes) {
    return attributes.get("queryParamAppendEqualWhenEmpty");
  }

  private Object getDefaultContentType(Map<String, Object> attributes) {
    return resolve((String) attributes.get("defaultContentType"));
  }

  private Object getDecoderConfig(Map<String, Object> attributes) {
    return resolve((String) attributes.get("decoderConfig"));
  }

  private Object getEncoderConfig(Map<String, Object> attributes) {
    return resolve((String) attributes.get("encoderConfig"));
  }

  private Object getMapNullToDefault(Map<String, Object> attributes) {
    return attributes.get("mapNullToDefault");
  }

  private Object getDismissException(Map<String, Object> attributes) {
    return attributes.get("dismissException");
  }

  private String resolve(String value) {
    if (StringUtils.hasText(value)) {
      return this.environment.resolvePlaceholders(value);
    }
    return value;
  }

  private String getUrl(Map<String, Object> attributes) {
    String url = resolve((String) attributes.get("gateWay"));
    return getUrl(url);
  }

  private Long getConnectTimeout(Map<String, Object> attributes) {
    String connectTimeoutConfig = resolve((String) attributes.get("connectTimeoutConfig"));
    Long connectTimeout = null;
    if (StringUtils.hasText(connectTimeoutConfig)) {
      try {
        connectTimeout = Long.valueOf(connectTimeoutConfig);
      } catch (Exception e) {
      }
    }
    if (connectTimeout == null) {
      connectTimeout = (Long) attributes.get("connectTimeout");
    }
    return connectTimeout;
  }

  private Long getReadTimeout(Map<String, Object> attributes) {
    String readTimeoutConfig = resolve((String) attributes.get("readTimeoutConfig"));
    Long readTimeout = null;
    if (StringUtils.hasText(readTimeoutConfig)) {
      try {
        readTimeout = Long.valueOf(readTimeoutConfig);
      } catch (Exception e) {
      }
    }
    if (readTimeout == null) {
      readTimeout = (Long) attributes.get("readTimeout");
    }
    return readTimeout;
  }

  protected ClassPathScanningCandidateComponentProvider getScanner() {
    return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
      @Override
      protected boolean isCandidateComponent(
              AnnotatedBeanDefinition beanDefinition) {
        boolean isCandidate = false;
        if (beanDefinition.getMetadata().isIndependent()) {
          if (!beanDefinition.getMetadata().isAnnotation()) {
            isCandidate = true;
          }
        }
        return isCandidate;
      }
    };
  }

  protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
    Map<String, Object> attributes = importingClassMetadata
            .getAnnotationAttributes(EnableSimpleFeignClients.class.getCanonicalName());

    Set<String> basePackages = new HashSet<>();

    for (String pkg : (String[]) attributes.get("basePackages")) {
      if (StringUtils.hasText(pkg)) {
        basePackages.add(pkg);
      }
    }
    if (basePackages.isEmpty()) {
      basePackages.add(
              ClassUtils.getPackageName(importingClassMetadata.getClassName()));
    }
    return basePackages;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

}
