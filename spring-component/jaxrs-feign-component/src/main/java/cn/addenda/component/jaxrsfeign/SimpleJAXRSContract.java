/*
 * Copyright 2012-2022 The Feign Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package cn.addenda.component.jaxrsfeign;

import cn.addenda.component.basaspring.context.ValueResolverHelper;
import feign.*;
import feign.template.UriTemplate;
import feign.template.UriUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static feign.Util.*;

/**
 * Please refer to the <a href="https://github.com/Netflix/feign/tree/master/feign-jaxrs">Feign
 * JAX-RS README</a>.
 */
@Slf4j
public class SimpleJAXRSContract extends DeclarativeContract {

  static final String ACCEPT = "Accept";
  static final String CONTENT_TYPE = "Content-Type";

  private static final Pattern QUERY_STRING_PATTERN = Pattern.compile("(?<!\\{)\\?");

  private ValueResolverHelper valueResolverHelper;

  // Protected so unittest can call us
  // XXX: Should parseAndValidateMetadata(Class, Method) be public instead? The old deprecated
  // parseAndValidateMetadata(Method) was public..
  @Override
  protected MethodMetadata parseAndValidateMetadata(Class<?> targetType, Method method) {
    return super.parseAndValidateMetadata(targetType, method);
  }

  public SimpleJAXRSContract(ValueResolverHelper valueResolverHelper) {
    this.valueResolverHelper = valueResolverHelper;

    // 支持 feign 原生的Headers注解
    super.registerClassAnnotation(Headers.class, (header, data) -> {
      final String[] headersOnType = header.value();
      checkState(headersOnType.length > 0, "Headers annotation was empty on type %s.",
              data.configKey());
      final Map<String, Collection<String>> headers = toMap(headersOnType);
      headers.putAll(data.template().headers());
      data.template().headers(null); // to clear
      data.template().headers(headers);
    });

    super.registerClassAnnotation(Path.class, (path, data) -> {
      if (path != null && !path.value().isEmpty()) {
        String pathValue = path.value();
        pathValue = resolve(pathValue);
        if (!pathValue.startsWith("/")) {
          pathValue = "/" + pathValue;
        }
        if (pathValue.endsWith("/")) {
          // Strip off any trailing slashes, since the template has already had slashes
          // appropriately
          // added
          pathValue = pathValue.substring(0, pathValue.length() - 1);
        }
        // jax-rs allows whitespace around the param name, as well as an optional regex. The
        // contract
        // should
        // strip these out appropriately.
        pathValue = pathValue.replaceAll("\\{\\s*(.+?)\\s*(:.+?)?\\}", "\\{$1\\}");
        uri(pathValue, false, data.template());
      }
    });
    super.registerClassAnnotation(Consumes.class, this::handleConsumesAnnotation);
    super.registerClassAnnotation(Produces.class, this::handleProducesAnnotation);

    registerMethodAnnotation(methodAnnotation -> {
      final Class<? extends Annotation> annotationType = methodAnnotation.annotationType();
      final HttpMethod http = annotationType.getAnnotation(HttpMethod.class);
      return http != null;
    }, (methodAnnotation, data) -> {
      final Class<? extends Annotation> annotationType = methodAnnotation.annotationType();
      final HttpMethod http = annotationType.getAnnotation(HttpMethod.class);
      checkState(data.template().method() == null,
              "Method %s contains multiple HTTP methods. Found: %s and %s", data.configKey(),
              data.template().method(), http.value());
      data.template().method(Request.HttpMethod.valueOf(http.value()));
    });

    // 支持 feign 原生的Headers注解
    super.registerMethodAnnotation(Headers.class, (header, data) -> {
      final String[] headersOnMethod = header.value();
      checkState(headersOnMethod.length > 0, "Headers annotation was empty on method %s.",
              data.configKey());
      data.template().headers(toMap(headersOnMethod));
    });

    super.registerMethodAnnotation(Path.class, (path, data) -> {
      String pathValue = emptyToNull(path.value());
      if (pathValue == null) {
        return;
      }
      String methodAnnotationValue = path.value();
      methodAnnotationValue = resolve(methodAnnotationValue);
      if (!methodAnnotationValue.startsWith("/") && !data.template().url().endsWith("/")) {
        methodAnnotationValue = "/" + methodAnnotationValue;
      }
      // jax-rs allows whitespace around the param name, as well as an optional regex. The contract
      // should
      // strip these out appropriately.
      methodAnnotationValue =
              methodAnnotationValue.replaceAll("\\{\\s*(.+?)\\s*(:.+?)?\\}", "\\{$1\\}");
      uri(methodAnnotationValue, true, data.template());
    });
    super.registerMethodAnnotation(Consumes.class, this::handleConsumesAnnotation);
    super.registerMethodAnnotation(Produces.class, this::handleProducesAnnotation);

    // trying to minimize the diff
    registerParamAnnotations();
  }


  private void handleProducesAnnotation(Produces produces, MethodMetadata data) {
    final String[] serverProduces =
            removeValues(produces.value(), mediaType -> emptyToNull(mediaType) == null, String.class);
    checkState(serverProduces.length > 0, "Produces.value() was empty on %s", data.configKey());
    data.template().header(ACCEPT, Collections.emptyList()); // remove any previous produces
    data.template().header(ACCEPT, serverProduces);
  }

  private void handleConsumesAnnotation(Consumes consumes, MethodMetadata data) {
    final String[] serverConsumes =
            removeValues(consumes.value(), mediaType -> emptyToNull(mediaType) == null, String.class);
    checkState(serverConsumes.length > 0, "Consumes.value() was empty on %s", data.configKey());
    data.template().header(CONTENT_TYPE, serverConsumes);
  }

  protected void registerParamAnnotations() {
    {
      registerParameterAnnotation(PathParam.class, (param, data, paramIndex) -> {
        final String name = param.value();
        checkState(emptyToNull(name) != null, "PathParam.value() was empty on parameter %s",
                paramIndex);
        nameParam(data, name, paramIndex);
      });
      registerParameterAnnotation(QueryParam.class, (param, data, paramIndex) -> {
        final String name = param.value();
        checkState(emptyToNull(name) != null, "QueryParam.value() was empty on parameter %s",
                paramIndex);
        final String query = addTemplatedParam(name);
        data.template().query(name, query);
        nameParam(data, name, paramIndex);
      });
      registerParameterAnnotation(HeaderParam.class, (param, data, paramIndex) -> {
        final String name = param.value();
        checkState(emptyToNull(name) != null, "HeaderParam.value() was empty on parameter %s",
                paramIndex);
        final String header = addTemplatedParam(name);
        data.template().header(name, header);
        nameParam(data, name, paramIndex);
      });
      registerParameterAnnotation(FormParam.class, (param, data, paramIndex) -> {
        final String name = param.value();
        checkState(emptyToNull(name) != null, "FormParam.value() was empty on parameter %s",
                paramIndex);
        data.formParams().add(name);
        nameParam(data, name, paramIndex);
      });
    }
  }

  // Not using override as the super-type's method is deprecated and will be removed.
  private String addTemplatedParam(String name) {
    return String.format("{%s}", name);
  }

  private Map<String, Collection<String>> toMap(String[] input) {
    final Map<String, Collection<String>> result =
            new LinkedHashMap<String, Collection<String>>(input.length);
    for (final String header : input) {
      final int colon = header.indexOf(':');
      final String name = header.substring(0, colon).trim();
      if (!result.containsKey(name)) {
        result.put(name, new ArrayList<String>(1));
      }
      String value = header.substring(colon + 1);
      value = resolve(value);
      result.get(name).add(value.trim());
    }
    return result;
  }

  private String resolve(String str) {
    Exception exception = null;
    // 支持从Spring中获取路径
    try {
      // spring解析不出配置时是否报错由参数 {@link org.springframework.beans.factory.config.PlaceholderConfigurerSupport.ignoreUnresolvablePlaceholders} 确定。
      // 类PlaceholderConfigurerSupport的默认实现是PropertySourcesPlaceholderConfigurer，如果在spring-xml文件中配置了context:property-placeholder标签，就会注入它。
      // disconf提供了ReloadingPropertyPlaceholderConfigurer，不是PropertySourcesPlaceholderConfigurer。
      // 如果是脚手架构建出来的项目，ReloadingPropertyPlaceholderConfigurer的配置里配置了ignoreUnresolvablePlaceholders为true。
      // 这里应该不会出现异常，但是catch一下，防止某些模块配置不一致引发问题。
      str = valueResolverHelper.resolveDollarPlaceholderFromContext(str);
    } catch (Exception e) {
      exception = e;
    }
    if (!StringUtils.hasLength(str) || str.startsWith("${")) {
      log.error("无法解析配置参数：{}。", str, exception);
    }
    return str;
  }

  /**
   * copy from {@link RequestTemplate#uri(String, boolean)}。remove validation of {@link UriUtils#isAbsolute(String)}.
   * <p>
   * Set the uri for the request.
   *
   * @param uri    to use, must be a relative uri.
   * @param append if the uri should be appended, if the uri is already set.
   * @return a RequestTemplate for chaining.
   */
  public RequestTemplate uri(String uri, boolean append, RequestTemplate requestTemplate) {
    /* validate and ensure that the url is always a relative one */
//        if (UriUtils.isAbsolute(uri)) {
//            throw new IllegalArgumentException("url values must be not be absolute.");
//        }

    if (uri == null) {
      uri = "/";
    } else if ((!uri.isEmpty() && !uri.startsWith("/") && !uri.startsWith("{")
            && !uri.startsWith("?") && !uri.startsWith(";"))) {
      /* if the start of the url is a literal, it must begin with a slash. */
      uri = "/" + uri;
    }

    /*
     * templates may provide query parameters. since we want to manage those explicity, we will need
     * to extract those out, leaving the uriTemplate with only the path to deal with.
     */
    Matcher queryMatcher = QUERY_STRING_PATTERN.matcher(uri);
    if (queryMatcher.find()) {
      String queryString = uri.substring(queryMatcher.start() + 1);

      Method extractQueryTemplates = ReflectionUtils.findMethod(RequestTemplate.class, "extractQueryTemplates", String.class, boolean.class);
      ReflectionUtils.makeAccessible(extractQueryTemplates);
      ReflectionUtils.invokeMethod(extractQueryTemplates, requestTemplate, queryString, append);

      /* reduce the uri to the path */
      uri = uri.substring(0, queryMatcher.start());
    }

    int fragmentIndex = uri.indexOf('#');
    if (fragmentIndex > -1) {
      Field fragment = ReflectionUtils.findField(RequestTemplate.class, "fragment");
      ReflectionUtils.makeAccessible(fragment);
      ReflectionUtils.setField(fragment, requestTemplate, uri.substring(fragmentIndex));
      uri = uri.substring(0, fragmentIndex);
    }

    Field uriTemplateField = ReflectionUtils.findField(RequestTemplate.class, "uriTemplate");
    ReflectionUtils.makeAccessible(uriTemplateField);
    UriTemplate uriTemplate = (UriTemplate) ReflectionUtils.getField(uriTemplateField, requestTemplate);
    /* replace the uri template */
    if (append && uriTemplate != null) {
      ReflectionUtils.setField(uriTemplateField, requestTemplate, UriTemplate.append(uriTemplate, uri));
    } else {
      Field decodeSlashField = ReflectionUtils.findField(RequestTemplate.class, "decodeSlash");
      ReflectionUtils.makeAccessible(decodeSlashField);
      boolean decodeSlash = (boolean) ReflectionUtils.getField(decodeSlashField, requestTemplate);

      Field charsetField = ReflectionUtils.findField(RequestTemplate.class, "charset");
      ReflectionUtils.makeAccessible(charsetField);
      Charset charset = (Charset) ReflectionUtils.getField(charsetField, requestTemplate);

      ReflectionUtils.setField(uriTemplateField, requestTemplate, UriTemplate.create(uri, !decodeSlash, charset));
    }
    return requestTemplate;
  }

}
