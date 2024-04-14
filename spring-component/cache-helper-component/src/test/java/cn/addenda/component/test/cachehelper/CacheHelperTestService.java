package cn.addenda.component.test.cachehelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/3/10 10:03
 */
public class CacheHelperTestService {

    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    public CacheHelperTestService() {
        userMap.put("F1", User.newUser("F1"));
        userMap.put("F2", User.newUser("F2"));
        userMap.put("F3", User.newUser("F3"));
        userMap.put("F4", User.newUser("F4"));
        userMap.put("F5", User.newUser("F5"));
        userMap.put("F6", User.newUser("F6"));
    }

    public User queryBy(String userId) {
        return userMap.get(userId);
    }

    public void updateUserName(String userId, String userName) {
        userMap.computeIfPresent(userId, new BiFunction<String, User, User>() {
            @Override
            public User apply(String s, User user) {
                user.setUsername(userName);
                return user;
            }
        });
    }

    public void insertUser(User user) {
        userMap.computeIfAbsent(user.getUserId(), new Function<String, User>() {
            @Override
            public User apply(String s) {
                return user;
            }
        });
    }

    public void deleteUser(String userId) {
        userMap.remove(userId);
    }

}
