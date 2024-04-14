package cn.addenda.component.test.cachehelper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author addenda
 * @since 2023/3/8 15:21
 */
@Setter
@Getter
@ToString
public class User {

  private String userId;

  private String username;

  private String status;

  private Integer isDel;

  private LocalDateTime activeTm;

  private LocalDateTime deactiveTm;

  private String modifierUser;

  private LocalDateTime modifyTm;

  private String creatorUser;

  private LocalDateTime createTm;

  private String autograph;

  private Integer autoDelSign;

  public static User newUser(String userId) {
    User user = new User();
    user.setUserId(userId);
    user.setUsername(userId + "姓名");
    user.setIsDel(1);
    user.setStatus("1");
    user.setActiveTm(LocalDateTime.now());
    user.setDeactiveTm(LocalDateTime.now());
    user.setModifierUser("addenda");
    user.setModifyTm(LocalDateTime.now());
    user.setCreatorUser("addenda");
    user.setCreateTm(LocalDateTime.now());
    user.setAutograph("addenda");
    user.setAutoDelSign(0);
    return user;
  }

}
