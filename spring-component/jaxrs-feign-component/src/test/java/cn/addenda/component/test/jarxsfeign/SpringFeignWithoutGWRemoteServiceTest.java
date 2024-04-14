package cn.addenda.component.test.jarxsfeign;

import cn.addenda.component.test.jarxsfeign.remote.WithoutGWRemoteService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author addenda
 * @since 2023/7/24 9:46
 */
public class SpringFeignWithoutGWRemoteServiceTest {

  static AnnotationConfigApplicationContext context;

  static WithoutGWRemoteService withoutGWRemoteService;

  @BeforeClass
  public static void before() {
    context = new AnnotationConfigApplicationContext();
    context.register(SpringFeignCoreTestConfiguration.class);
    context.refresh();
    withoutGWRemoteService = context.getBean(WithoutGWRemoteService.class);
  }

  @Test
  public void test1() {
    for (int i = 0; i < 10; i++) {
      String baidu = withoutGWRemoteService.baidu();
      System.out.println(baidu);
    }
  }

  @AfterClass
  public static void after() {
    context.close();
  }

}
