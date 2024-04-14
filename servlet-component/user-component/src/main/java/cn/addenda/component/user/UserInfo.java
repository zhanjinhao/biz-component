package cn.addenda.component.user;

import lombok.*;

/**
 * 用户信息实体
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

  /**
   * 用户 ID
   */
  private String userId;

  /**
   * 用户名
   */
  private String username;
}
