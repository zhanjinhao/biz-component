package cn.addenda.component.user;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 用户上下文
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserContext {

  private static final String ADDENDA = "addenda";

  private static final ThreadLocal<Deque<UserInfo>> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();

  /**
   * 设置用户至上下文
   *
   * @param user 用户详情信息
   */
  public static void setUser(UserInfo user) {
    Deque<UserInfo> userInfoDeque = USER_THREAD_LOCAL.get();
    if (userInfoDeque == null) {
      userInfoDeque = new ArrayDeque<>();
      USER_THREAD_LOCAL.set(userInfoDeque);
    }
    userInfoDeque.push(user);
  }

  /**
   * 获取上下文中用户 ID
   *
   * @return 用户 ID
   */
  public static String getUserId() {
    Deque<UserInfo> userInfoDeque = USER_THREAD_LOCAL.get();
    if (userInfoDeque == null) {
      return ADDENDA;
    }
    UserInfo peek = userInfoDeque.peek();
    if (peek == null) {
      return ADDENDA;
    }
    return userInfoDeque.peek().getUserId();
  }

  /**
   * 获取上下文中用户名称
   *
   * @return 用户名称
   */
  public static String getUsername() {
    Deque<UserInfo> userInfoDeque = USER_THREAD_LOCAL.get();
    if (userInfoDeque == null) {
      return ADDENDA;
    }
    UserInfo peek = userInfoDeque.peek();
    if (peek == null) {
      return ADDENDA;
    }
    return userInfoDeque.peek().getUsername();
  }

  /**
   * 获取user
   */
  public static UserInfo getUser() {
    Deque<UserInfo> userInfoDeque = USER_THREAD_LOCAL.get();
    if (userInfoDeque == null) {
      return new UserInfo(ADDENDA, ADDENDA);
    }
    return userInfoDeque.peek();
  }

  /**
   * 清理用户上下文
   */
  public static void removeUser() {
    Deque<UserInfo> userInfoDeque = USER_THREAD_LOCAL.get();
    if (userInfoDeque == null) {
      return;
    }
    userInfoDeque.pop();
    if (userInfoDeque.isEmpty()) {
      USER_THREAD_LOCAL.remove();
    }
  }

  public static <T> void acceptWithCustomUser(Consumer<T> consumer, T t, UserInfo userInfo) {
    UserContext.setUser(userInfo);
    try {
      consumer.accept(t);
    } finally {
      UserContext.removeUser();
    }
  }

  public static <T, R> R applyWithCustomUser(Function<T, R> function, T t, UserInfo userInfo) {
    UserContext.setUser(userInfo);
    try {
      return function.apply(t);
    } finally {
      UserContext.removeUser();
    }
  }

  public static void runWithCustomUser(Runnable runnable, UserInfo userInfo) {
    UserContext.setUser(userInfo);
    try {
      runnable.run();
    } finally {
      UserContext.removeUser();
    }
  }

  public static <R> R getWithCustomUser(Supplier<R> supplier, UserInfo userInfo) {
    UserContext.setUser(userInfo);
    try {
      return supplier.get();
    } finally {
      UserContext.removeUser();
    }
  }

}
