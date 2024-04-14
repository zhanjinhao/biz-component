package cn.addenda.component.test.paraminjection;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * (EasyCodeTest)实体类
 *
 * @author addenda
 * @since 2023-07-07 09:19:53
 */
@Setter
@Getter
@ToString
public class ParamInjectionTest implements Serializable {

  private static final long serialVersionUID = 676662520236546432L;

  /**
   * 自增序列id
   * 统一使用id  到Mapper XML上做与数据库字段的对应关系
   */
  protected Long id;

  /**
   * 业务code
   */
  protected String bizCode;

  /**
   * 最后修改人的用户ID
   */
  protected String modifier;
  /**
   * 最后修改时间
   */
  protected Date modifyTm;
  /**
   * 创建人的用户ID
   */
  protected String creator;
  /**
   * 创建时间
   */
  protected Date createTm;
  /**
   * 是否删除 'Y'或'N'
   */
  protected String deleteFlag;

  private String creatorName;

  private String modifierName;

  /**
   * 昵称
   */
  private String nickname;
  /**
   * 年龄
   */
  private Integer age;
  /**
   * 生日
   */
  private LocalDateTime birthday;

}

