package cn.addenda.component.test.cachehelper;

import cn.addenda.component.cache.ExpiredHashMapKVCache;
import cn.addenda.component.cachehelper.CacheHelper;
import cn.addenda.component.jdk.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author addenda
 * @since 2023/3/10 8:50
 */
@Slf4j
public class CacheHelperDelayDeleteTest {

  AnnotationConfigApplicationContext context;

  CacheHelper cacheHelper;

  CacheHelperTestService service;

  @Before
  public void before() {
    String xId = UUID.randomUUID().toString();
    context = new AnnotationConfigApplicationContext();
    context.registerBean(CacheHelper.class, new ExpiredHashMapKVCache<String, String>() {
      final AtomicInteger atomicInteger = new AtomicInteger(-1);

      @Override
      public void delete(String key) {
        atomicInteger.compareAndSet(atomicInteger.get(), atomicInteger.get() + 2);
        SleepUtils.sleep(TimeUnit.SECONDS, atomicInteger.get());
        super.delete(key);
      }
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
    log.info(userFromDb1 != null ? userFromDb1.toString() : null);

    updateUserName("Q1", "我被修改了！");
    User userFromDb2 = queryByPpf("Q1");
    log.info(userFromDb2 != null ? userFromDb2.toString() : null);

    deleteUser("Q1");
    User userFromDb3 = queryByPpf("Q1");
    log.info(userFromDb3 != null ? userFromDb3.toString() : null);

    SleepUtils.sleep(TimeUnit.SECONDS, 100);
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

}
