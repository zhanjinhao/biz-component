package cn.addenda.component.test.jarxsfeign;

import cn.addenda.component.jaxrsfeign.SimpleStringDecoder;
import cn.addenda.component.test.jarxsfeign.remote.WithGWRemoteService;
import feign.Feign;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jaxrs.JAXRSContract;
import org.junit.Test;

/**
 * @author addenda
 * @since 2023/7/9 15:21
 */
public class FeignCoreTest {

  @Test
  public void test1() {
    WithGWRemoteService target = Feign.builder()
            .contract(new JAXRSContract())
            .client(new ApacheHttpClient())
            .decoder(new SimpleStringDecoder(new JacksonDecoder()))
            // null "" " "
            .target(WithGWRemoteService.class,
                    "https://www.baidu.com");

    String employees = target.baidu();

    System.out.println(employees);
  }

}
