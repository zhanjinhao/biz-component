package cn.addenda.component.jaxrsfeign;

import cn.addenda.component.basaspring.context.ValueResolverHelper;
import cn.addenda.component.convention.result.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import feign.*;
import feign.Feign.Builder;
import feign.Request.Options;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.slf4j.Slf4jLogger;
import feign.template.Literal;
import feign.template.TemplateChunk;
import feign.template.UriTemplate;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/7/9 16:56
 */
class SimpleFeignClientFactoryBean implements FactoryBean<Object>, InitializingBean, BeanFactoryAware {

  /**
   * {@link SimpleFeignClient#gateWay()}
   */
  @Setter
  private String gateWay;

  /**
   * {@link SimpleFeignClient#connectTimeout()}
   */
  @Setter
  private long connectTimeout;

  /**
   * {@link SimpleFeignClient#readTimeout()}
   */
  @Setter
  private long readTimeout;

  /**
   * {@link SimpleFeignClient#dismissException()}
   */
  @Setter
  private boolean dismissException;

  /**
   * {@link SimpleFeignClient#mapNullToDefault()}
   */
  @Setter
  private boolean mapNullToDefault;

  /**
   * {@link SimpleFeignClient#encoderConfig()}
   */
  @Setter
  private String encoderConfig;

  private Encoder encoder;

  /**
   * {@link SimpleFeignClient#decoderConfig()}
   */
  @Setter
  private String decoderConfig;

  private Decoder decoder;

  /**
   * {@link SimpleFeignClient#defaultContentType()}
   */
  @Setter
  private String defaultContentType;

  /**
   * {@link SimpleFeignClient#queryParamAppendEqualWhenEmpty()}
   */
  @Setter
  private boolean queryParamAppendEqualWhenEmpty;

  @Setter
  private Class<?> type;

  @Setter
  private String clientConfig;

  private Client client;

  private BeanFactory beanFactory;

  private ValueResolverHelper valueResolverHelper;

  private static final List<String> EMPTY_STR_LIST = new ArrayList<>();

  static {
    EMPTY_STR_LIST.add("");
  }

  @Override
  public Object getObject() throws Exception {
    Logger logger = LoggerFactory.getLogger(type);
    Object target = feign().build()
            .newInstance(new SimpleHardCodedTarget<>(type, gateWay));
    logger.info("feign client create instance of [{}], config: [gateWay,{}], [connectTimeout,{}], [readTimeout,{}], [dismissException,{}].",
            type, !StringUtils.hasLength(gateWay) ? "no fixed gateway" : gateWay, connectTimeout, readTimeout, dismissException);
    return Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            new InvocationHandler() {
              @Override
              public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                  if (Object.class.equals(method.getDeclaringClass())) {
                    return method.invoke(this, args);
                  }
                  Object invoke = invokeWithRetry(method, target, args, logger);
                  if (mapNullToDefault) {
                    if (invoke == null) {
                      logger.info("Info and MapNullToDefault, feign client http request success and retrieve null, return default result instead of null due to mapNullToDefault is true.");
                      return defaultResult(method.getReturnType(), method.getGenericReturnType(), true, method, args);
                    }
                    if (invoke instanceof Result) {
                      Result invokeResult = (Result) invoke;
                      if (invokeResult.getResult() == null) {
                        logger.info(
                                "Info and MapNullToDefault, feign client http request success and retrieve Result(null), return default result instead of null due to mapNullToDefault is true.");
                        Result result = (Result) defaultResult(method.getReturnType(), method.getGenericReturnType(), Result.OK.equals(invokeResult.getReqCode()), method, args);
                        if (result != null) {
                          BeanUtils.copyProperties(invokeResult, result, "success", "obj");
                        }
                        return result;
                      }
                    }
                  }

                  return invoke;
                } catch (Exception e) {
                  if (dismissException) {
                    logger.error("Error and DismissException, feign client http request failed, return null instead of throwing exception due to dismissException is true. Exception Detail: ",
                            e);
                    if (mapNullToDefault) {
                      logger.error("Error and MapNullToDefault, feign client http request exception and dismissException, return default result instead of null due to mapNullToDefault is true.");
                      return defaultResult(method.getReturnType(), method.getGenericReturnType(), false, method, args);
                    }
                    return null;
                  }
                  throw e;
                }
              }
            });
  }

  private Object invokeWithRetry(Method method, Object target, Object[] args, Logger logger) throws Throwable {
    Retry retry = method.getAnnotation(Retry.class);
    if (retry == null) {
      return method.invoke(target, args);
    }

    int maxAttempts = retry.maxAttempts();
    if (maxAttempts < 1) {
      throw new IllegalArgumentException("@Retry的maxAttempts属性必须大于0！");
    }

    Throwable throwable = null;
    Object invoke = null;
    for (int i = 0; i < maxAttempts - 1; i++) {
      try {
        invoke = method.invoke(target, args);
      } catch (Throwable t) {
        long millis = retry.timeunit().toMillis(retry.interval());
        logger.error("Error and Retry, feign client http request failed, try again in {}ms. Current is the {}th request, max attempts are {}.", millis, i + 1, maxAttempts, t);
        Thread.sleep(millis);
        throwable = t;
      }
    }
    if (throwable != null) {
      throw throwable;
    }

    return invoke;
  }

  private Object defaultResult(Class<?> returnType, Type genericReturnType, boolean success, Method method, Object[] args) {
    if (returnType.isAssignableFrom(Result.class)) {
      ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
      Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];

      Result<Object> result = new Result<>();
      result.setReqCode(success ? Result.OK : Result.FAILED);
      if (actualTypeArgument instanceof Class) {
        result.setResult(defaultResult((Class<?>) actualTypeArgument, actualTypeArgument, success, method, args));
      } else if (actualTypeArgument instanceof ParameterizedType) {
        ParameterizedType parameterizedType1 = (ParameterizedType) actualTypeArgument;
        Type rawType = parameterizedType1.getRawType();
        if (rawType instanceof Class) {
          result.setResult(defaultResult((Class<?>) rawType, actualTypeArgument, success, method, args));
        }
      }
      return result;
    }
    // List
    else if (returnType.isAssignableFrom(PageInfo.class)) {
      Annotation[][] parameterAnnotationss = method.getParameterAnnotations();
      int pageNumIndex = -1;
      int pageSizeIndex = -1;
      int orderByIndex = -1;

      for (int i = 0; i < parameterAnnotationss.length; i++) {
        Annotation[] parameterAnnotations = parameterAnnotationss[i];
        for (Annotation annotation : parameterAnnotations) {
          if (QueryParam.class.equals(annotation.annotationType())) {
            QueryParam queryParam = (QueryParam) annotation;
            String value = queryParam.value();
            if ("pageNum".equalsIgnoreCase(value)) {
              pageNumIndex = i;
            } else if ("pageSize".equalsIgnoreCase(value)) {
              pageSizeIndex = i;
            } else if ("orderBy".equalsIgnoreCase(value)) {
              orderByIndex = i;
            }
          }
        }
      }
      int pageNum = 0;
      if (pageNumIndex != -1) {
        try {
          Object arg = args[pageNumIndex];
          if (arg != null) {
            pageNum = (int) arg;
          }
        } catch (Exception e) {
        }
      }
      int pageSize = 0;
      if (pageSizeIndex != -1) {
        try {
          Object arg = args[pageSizeIndex];
          if (arg != null) {
            pageSize = (int) arg;
          }
        } catch (Exception e) {
        }
      }
      String orderBy = null;
      if (orderByIndex != -1) {
        try {
          Object arg = args[orderByIndex];
          if (arg != null) {
            orderBy = (String) arg;
          }
        } catch (Exception e) {
        }
      }

      Page<Object> page = new Page<>(pageNum, pageSize);
      page.setTotal(0);
      page.setOrderBy(orderBy);
      return new PageInfo<>(page);
    } else if (returnType.isAssignableFrom(ArrayList.class)) {
      return new ArrayList<>();
    } else if (returnType.isAssignableFrom(LinkedList.class)) {
      return new LinkedList<>();
    } else if (returnType.isAssignableFrom(Stack.class)) {
      return new Stack<>();
    } else if (returnType.isAssignableFrom(Vector.class)) {
      return new Vector<>();
    } else if (returnType.isAssignableFrom(ArrayDeque.class)) {
      return new ArrayDeque<>();
    } else if (returnType.isAssignableFrom(List.class)) {
      return new ArrayList<>();
    }
    // Set
    else if (returnType.isAssignableFrom(HashSet.class)) {
      return new HashSet<>();
    } else if (returnType.isAssignableFrom(LinkedHashSet.class)) {
      return new LinkedHashSet<>();
    } else if (returnType.isAssignableFrom(TreeSet.class)) {
      return new TreeSet<>();
    } else if (returnType.isAssignableFrom(Set.class)) {
      return new HashSet<>();
    }
    // Map
    else if (returnType.isAssignableFrom(LinkedHashMap.class)) {
      return new LinkedHashMap<>();
    } else if (returnType.isAssignableFrom(HashMap.class)) {
      return new HashMap<>();
    } else if (returnType.isAssignableFrom(TreeMap.class)) {
      return new TreeMap<>();
    } else if (returnType.isAssignableFrom(Map.class)) {
      return new HashMap<>();
    }
    return null;
  }

  private static final String ERROR_PREFIX = "/http";

  protected Feign.Builder feign() {
    Options options = new Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, false);
    Logger logger = LoggerFactory.getLogger(type);

    Builder builder = Feign.builder()
            // required values
            .logger(new Slf4jLogger(type))
            .client(client)
            .contract(new SimpleJAXRSContract(valueResolverHelper))
            .retryer(Retryer.NEVER_RETRY)
            .requestInterceptor(new RequestInterceptor() {
              @Override
              public void apply(RequestTemplate requestTemplate) {
                String url = requestTemplate.url();
                if (url.startsWith(ERROR_PREFIX)) {
                  logger.debug("feign client url [{}] start with '/http', remove first '/'.", url);
                  Field uriTemplateField = ReflectionUtils.findField(RequestTemplate.class, "uriTemplate");
                  ReflectionUtils.makeAccessible(uriTemplateField);
                  UriTemplate uriTemplate = (UriTemplate) ReflectionUtils.getField(uriTemplateField, requestTemplate);

                  // 如果template以/http开头，移除/
                  Field templateField = ReflectionUtils.findField(UriTemplate.class, "template");
                  ReflectionUtils.makeAccessible(templateField);
                  String template = (String) ReflectionUtils.getField(templateField, uriTemplate);
                  if (template != null && template.startsWith(ERROR_PREFIX)) {
                    ReflectionUtils.setField(templateField, uriTemplate, template.substring(1));
                  }

                  // templateChunks[0]如果以/http开头，移除/
                  Field templateChunksField = ReflectionUtils.findField(UriTemplate.class, "templateChunks");
                  ReflectionUtils.makeAccessible(templateChunksField);
                  List<TemplateChunk> templateChunkList = (List<TemplateChunk>) ReflectionUtils.getField(templateChunksField, uriTemplate);
                  if (templateChunkList != null && !templateChunkList.isEmpty()) {
                    TemplateChunk templateChunk = templateChunkList.get(0);
                    if (templateChunk instanceof Literal) {
                      String value = templateChunk.getValue();
                      if (value.startsWith(ERROR_PREFIX)) {
                        templateChunkList.set(0, Literal.create(value.substring(1)));
                      }
                    }
                  }
                }
              }
            })

            .encoder(new FormEncoder(encoder))
            .decoder(decoder)
            .options(options);

    if (!StringUtils.isEmpty(defaultContentType)) {
      builder.requestInterceptor(new RequestInterceptor() {
        @Override
        public void apply(RequestTemplate requestTemplate) {
          Map<String, Collection<String>> headers = requestTemplate.headers();
          for (Entry<String, Collection<String>> stringCollectionEntry : headers.entrySet()) {
            if ("content-type".equalsIgnoreCase(stringCollectionEntry.getKey())) {
              return;
            }
          }
          requestTemplate.header("content-type", defaultContentType);
        }
      });
    }

    if (queryParamAppendEqualWhenEmpty) {
      builder.requestInterceptor(new RequestInterceptor() {
        @Override
        public void apply(RequestTemplate template) {
          Map<String, Collection<String>> newQueries = new LinkedHashMap<>();
          Map<String, Collection<String>> queries = template.queries();
          Iterator<Entry<String, Collection<String>>> iterator = queries.entrySet().iterator();
          while (iterator.hasNext()) {
            Entry<String, Collection<String>> next = iterator.next();
            String key = next.getKey();
            Collection<String> value = next.getValue();
            if (CollectionUtils.isEmpty(value)) {
              newQueries.put(key + "=", EMPTY_STR_LIST);
            } else {
              newQueries.put(key, value);
            }
          }
          template.queries(null);
          template.queries(newQueries);
        }
      });
    }

    return builder;
  }

  @Override
  public Class<?> getObjectType() {
    return type;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    encoder = beanFactory.getBean(encoderConfig, Encoder.class);
    decoder = beanFactory.getBean(decoderConfig, Decoder.class);
    client = beanFactory.getBean(clientConfig, Client.class);
    valueResolverHelper = beanFactory.getBean(ValueResolverHelper.class);
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

}