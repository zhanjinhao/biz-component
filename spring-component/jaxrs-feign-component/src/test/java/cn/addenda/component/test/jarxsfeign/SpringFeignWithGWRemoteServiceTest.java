package cn.addenda.component.test.jarxsfeign;

import cn.addenda.component.test.jarxsfeign.remote.WithGWRemoteService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author addenda
 * @since 2023/7/24 9:46
 */
public class SpringFeignWithGWRemoteServiceTest {

  static AnnotationConfigApplicationContext context;

  static WithGWRemoteService withGWRemoteService;

  @BeforeClass
  public static void before() {
    context = new AnnotationConfigApplicationContext();
    context.register(SpringFeignCoreTestConfiguration.class);
    context.refresh();
    withGWRemoteService = context.getBean(WithGWRemoteService.class);
  }


  @Test
  public void test1() {
    for (int i = 0; i < 10; i++) {
      String baidu = withGWRemoteService.baidu();
      System.out.println(baidu);
    }
  }

  @AfterClass
  public static void after() {
    context.close();
  }

}
