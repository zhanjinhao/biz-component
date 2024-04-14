package cn.addenda.component.test.paraminjection;

import cn.addenda.component.paraminjection.ParamInjected;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (EasyCodeTest)表数据库访问层
 *
 * @author addenda
 * @since 2023-07-07 09:19:52
 */
public interface ParamInjectionMapper {
  /**
   * 新增数据
   */
  int insert(ParamInjectionTest paramInjectionTest);

  /**
   * 按ID更新数据
   */
  int updateById(ParamInjectionTest paramInjectionTest);

  /**
   * 按ID删除数据
   */
  int deleteById(@Param("id") Long id);

  /**
   * 按实体类删除数据
   */
  @ParamInjected(name = "a", el = "1")
  @ParamInjected(name = "b", el = "2")
  int deleteByEntity(ParamInjectionTest paramInjectionTest);

  /**
   * 按实体类删除数据
   */
  @ParamInjected(name = "a", el = "1")
  @ParamInjected(name = "b", el = "2")
  int deleteByEntity2(ParamInjectionTest paramInjectionTest);

  /**
   * 按实体类查询数据
   */
  List<ParamInjectionTest> queryByEntity(ParamInjectionTest paramInjectionTest);

  /**
   * 按ID查询数据
   */
  ParamInjectionTest queryById(@Param("id") Long id);

  /**
   * 按ID集合查询数据
   */
  List<ParamInjectionTest> queryByIdList(@Param("idList") List<Long> idList);

  /**
   * 按实体类计数数据
   */
  List<Long> countByEntity(ParamInjectionTest paramInjectionTest);

  @ParamInjected(name = "modifierName", el = "testName")
  @ParamInjected(name = "modifier", el = "testId")
  int update(@Param("id") Long id, @Param("nickName") String nickName);

  @ParamInjected(name = "modifierName", el = "testName")
  @ParamInjected(name = "modifier", el = "testId")
  int increment(@Param("nickName") String nickName);

}

