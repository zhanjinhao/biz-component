package cn.addenda.component.ratelimiter;

import cn.addenda.component.bean.pojo.Binary;

import java.util.ArrayList;
import java.util.List;

/**
 * todo
 *
 * @author addenda
 * @since 2023/1/21 15:57
 */
public class Ipv4BlacklistTrafficLimiter {

  /**
   * 每个Binary存储一个区间（开始和结束），各个区间之间不相交。
   */
  private List<Binary<Integer, Integer>> blacklist = new ArrayList<>();

  /**
   * 添加一个区间
   */
  private void addSection(Binary<Integer, Integer> section) {


  }

  private void addSection(String section) {
    StringBuilder netPrefix = new StringBuilder();
    StringBuilder maskLength = new StringBuilder();

    boolean flag = false;
    for (int i = 0; i < section.length(); i++) {
      char c = section.charAt(i);
      if (!flag) {
        if (c == '/') {
          flag = true;
        } else {
          netPrefix.append(c);
        }
      } else {
        maskLength.append(c);
      }
    }
  }

}
