package cn.addenda.component.user;

import lombok.*;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(userId, userInfo.userId) && Objects.equals(username, userInfo.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, username);
  }
}
