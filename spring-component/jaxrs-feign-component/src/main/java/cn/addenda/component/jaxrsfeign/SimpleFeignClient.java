package cn.addenda.component.jaxrsfeign;

import feign.Feign.Builder;
import feign.codec.Decoder;
import feign.codec.Encoder;

import javax.ws.rs.QueryParam;
import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/7/9 16:56
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleFeignClient {

  /**
   * 网关地址。支持从spring env中获取配置数据。
   */
  String gateWay() default "";

  /**
   * Determines the timeout in milliseconds until a connection is established.
   * A timeout value of zero is interpreted as an infinite timeout.
   * A negative value is interpreted as undefined (system default if applicable).
   * 单位ms。支持从spring env中获取配置数据。能解析为数字时覆盖{@link SimpleFeignClient#connectTimeout()}。
   */
  String connectTimeoutConfig() default "";

  /**
   * Determines the timeout in milliseconds until a connection is established.
   * A timeout value of zero is interpreted as an infinite timeout.
   * A negative value is interpreted as undefined (system default if applicable).
   * <p>
   * 单位ms。0表示超时时间为无穷大，-1表示使用系统默认超时时间。<br/>
   * 可能被{@link SimpleFeignClient#connectTimeoutConfig()}覆盖。
   */
  long connectTimeout() default 60000;

  /**
   * Defines the socket timeout (SO_TIMEOUT) in milliseconds, which is the timeout for waiting for data or,
   * put differently, a maximum period inactivity between two consecutive data packets).
   * A timeout value of zero is interpreted as an infinite timeout.
   * A negative value is interpreted as undefined (system default if applicable).
   * <p>
   * 单位ms。支持从spring env中获取配置数据。能解析为数字时覆盖{@link SimpleFeignClient#connectTimeout()}。
   */
  String readTimeoutConfig() default "";

  /**
   * Defines the socket timeout (SO_TIMEOUT) in milliseconds, which is the timeout for waiting for data or,
   * put differently, a maximum period inactivity between two consecutive data packets).
   * A timeout value of zero is interpreted as an infinite timeout.
   * A negative value is interpreted as undefined (system default if applicable).
   * <p>
   * 单位ms。0表示超时时间为无穷大，-1表示使用系统默认超时时间。<br/>。
   * 可能被{@link SimpleFeignClient#readTimeoutConfig()}覆盖。
   */
  long readTimeout() default 60000;

  /**
   * false: 异常时抛出异常。 <br/>
   * true:  异常时返回null。
   */
  boolean dismissException() default false;

  /**
   * true：将null映射为默认数据。 <br/>
   * false：直接返回 null。
   */
  boolean mapNullToDefault() default false;

  /**
   * @return whether to mark the feign proxy as a primary bean. Defaults to true.
   */
  boolean primary() default true;

  String clientConfig();

  /**
   * {@link Builder#decoder(Decoder)}。从Spring Context中获取Decoder。支持从spring env中获取配置数据。
   */
  String decoderConfig() default "jacksonDecoder";

  /**
   * {@link Builder#encoder(Encoder)}。从Spring Context中获取Encoder。支持从spring env中获取配置数据。
   */
  String encoderConfig() default "jacksonEncoder";

  /**
   * 默认的content-type。支持从spring env中获取配置数据。
   */
  String defaultContentType() default "application/json; charset=utf-8";

  /**
   * {@link QueryParam} 的数据为""时，给key增加=。 <br/>
   * 例：{@code (@QueryParam("name") String name, @QueryParam("age") int age}，调用方传入("", 29)时： <br/>
   * - false: feign在传参的时候会拼接为: xxx?name&age=29<br/>
   * - true:  feign在传参的时候会拼接为: xxx?name=&age=29
   */
  boolean queryParamAppendEqualWhenEmpty() default false;

}
