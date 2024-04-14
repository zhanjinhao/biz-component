package cn.addenda.component.test.paraminjection;

import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;

/**
 * @author addenda
 * @since 2023/7/6 17:30
 */
public class ParamInjectedTestTest {

  static SqlSessionFactory sqlSessionFactory;

  static {
    String resource = "cn/addenda/component/test/paraminjection/mybatis-config-paraminjection.xml";
    Reader reader = null;
    try {
      reader = Resources.getResourceAsReader(resource);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
  }

  static List<Long> keyList = new ArrayList<>();

  public static void main(String[] args) {
    testDeleteByEntity();
    testInsert("addenda1");
    testInsert("addenda2");
    testUpdate();
    testBatchUpdate();
    testUpdateNonEntity();
    List<ParamInjectionTest> paramInjectionTests = testQueryByEntity();
    Assert.assertEquals(keyList.get(0), paramInjectionTests.get(0).getId());
    Assert.assertEquals(keyList.get(1), paramInjectionTests.get(1).getId());
    Assert.assertEquals("addenda", paramInjectionTests.get(0).getNickname());
    Assert.assertEquals("addenda", paramInjectionTests.get(1).getNickname());
    Assert.assertEquals(new Integer(2), paramInjectionTests.get(0).getAge());
    Assert.assertEquals(new Integer(2), paramInjectionTests.get(1).getAge());
    Assert.assertNotNull(paramInjectionTests.get(0).getBirthday());
    Assert.assertNull(paramInjectionTests.get(1).getBirthday());
  }

  private static void testDeleteByEntity() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ParamInjectionMapper mapper = sqlSession.getMapper(ParamInjectionMapper.class);
      ParamInjectionTest paramInjectionTest = new ParamInjectionTest();
      mapper.deleteByEntity(paramInjectionTest);
      sqlSession.commit();
    }
  }

  private static void testInsert(String nickname) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ParamInjectionMapper mapper = sqlSession.getMapper(ParamInjectionMapper.class);

      ParamInjectionTest paramInjectionTest = new ParamInjectionTest();
      paramInjectionTest.setAge(1);
      paramInjectionTest.setNickname(nickname);
      mapper.insert(paramInjectionTest);
      sqlSession.commit();
      keyList.add(paramInjectionTest.getId());
    }
  }

  private static void testUpdate() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ParamInjectionMapper mapper = sqlSession.getMapper(ParamInjectionMapper.class);
      ParamInjectionTest paramInjectionTest = new ParamInjectionTest();
      paramInjectionTest.setBirthday(LocalDateTime.now());
      paramInjectionTest.setId(keyList.get(0));
      mapper.updateById(paramInjectionTest);
      sqlSession.commit();
    }
  }

  private static void testUpdateNonEntity() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ParamInjectionMapper mapper = sqlSession.getMapper(ParamInjectionMapper.class);
      mapper.increment("addenda");
      sqlSession.commit();
    }
  }

  private static List<ParamInjectionTest> testQueryByEntity() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ParamInjectionMapper mapper = sqlSession.getMapper(ParamInjectionMapper.class);

      ParamInjectionTest paramInjectionTest = new ParamInjectionTest();
      paramInjectionTest.setNickname("addenda");

      return mapper.queryByEntity(paramInjectionTest);
    }
  }

  private static void testBatchUpdate() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      ParamInjectionMapper mapper = sqlSession.getMapper(ParamInjectionMapper.class);
      mapper.update(keyList.get(0), "addenda");
      sqlSession.flushStatements();
      mapper.update(keyList.get(1), "addenda");
      sqlSession.flushStatements();
      sqlSession.commit();
    }
  }

}
