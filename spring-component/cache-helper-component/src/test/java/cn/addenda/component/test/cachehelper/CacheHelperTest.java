package cn.addenda.component.test.cachehelper;

import cn.addenda.component.cache.ExpiredHashMapKVCache;
import cn.addenda.component.cachehelper.CacheHelper;
import cn.addenda.component.jdk.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/3/10 8:50
 */
@Slf4j
public class CacheHelperTest {

  private AnnotationConfigApplicationContext context;

  private CacheHelper cacheHelper;

  private CacheHelperTestService service;

  @Before
  public void before() {
    context = new AnnotationConfigApplicationContext();
    context.registerBean(CacheHelper.class, new ExpiredHashMapKVCache<String, String>() {
    });
    context.refresh();

    cacheHelper = context.getBean(CacheHelper.class);
    log.info(cacheHelper.toString());
    service = new CacheHelperTestService();
  }

  @After
  public void after() {
    context.close();
  }

  public static final String userCachePrefix = "user:";

  @Test
  public void test() {
    // insert 不走缓存
    service.insertUser(User.newUser("Q1"));

    User userFromDb1 = queryByPpf("Q1");
    userFromDb1 = queryByPpf("Q1");
    log.info(userFromDb1 != null ? userFromDb1.toString() : null);

    updateUserName("Q1", "我被修改了！");
    User userFromDb2 = queryByPpf("Q1");
    log.info(userFromDb2 != null ? userFromDb2.toString() : null);

    deleteUser("Q1");
    User userFromDb3 = queryByPpf("Q1");
    log.info(userFromDb3 != null ? userFromDb3.toString() : null);
  }

  private User queryByPpf(String userId) {
    return cacheHelper.queryWithPpf(userCachePrefix, userId, User.class, s -> {
//            SleepUtils.sleep(TimeUnit.SECONDS, 10);
      return service.queryBy(s);
    }, 5000L);
  }

  private void updateUserName(String userId, String userName) {
    cacheHelper.acceptWithPpf(userCachePrefix, userId, s -> service.updateUserName(s, userName));
  }

  private void deleteUser(String userId) {
    cacheHelper.acceptWithPpf(userCachePrefix, userId, s -> service.deleteUser(userId));
  }

  @Test
  public void test2() {
    // insert 不走缓存
    service.insertUser(User.newUser("Q2"));

    User userFromDb1 = queryByRdf("Q2");
    log.info(userFromDb1.toString());

    updateUserName2("Q2", "我被修改了！");
    User userFromDb2 = queryByRdf("Q2");
    log.info(userFromDb2.toString());

    deleteUser2("Q2");
    User userFromDb3 = queryByRdf("Q2");
    log.info(userFromDb3 != null ? userFromDb3.toString() : null);
  }

  private User queryByRdf(String userId) {
    return cacheHelper.queryWithRdf(userCachePrefix, userId, User.class, s -> {
      SleepUtils.sleep(TimeUnit.SECONDS, 3);
      return service.queryBy(s);
    }, 500000L);
  }

  private void updateUserName2(String userId, String userName) {
    cacheHelper.acceptWithRdf(userCachePrefix, userId, s -> service.updateUserName(s, userName));
  }

  private void deleteUser2(String userId) {
    cacheHelper.acceptWithRdf(userCachePrefix, userId, s -> service.deleteUser(userId));
  }


  @Test
  public void test3() {
    service.insertUser(User.newUser("Q2"));

    Thread[] threads = new Thread[20];
    for (int i = 0; i < 20; i++) {
      threads[i] = new Thread(() -> {
        User user = queryByRdf("Q2");
      });
    }

    Arrays.stream(threads).forEach(Thread::start);
    Arrays.stream(threads).forEach(thread -> {
      try {
        thread.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

}
