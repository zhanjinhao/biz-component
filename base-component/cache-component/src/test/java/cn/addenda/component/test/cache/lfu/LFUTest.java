package cn.addenda.component.test.cache.lfu;

import org.junit.Test;

public class LFUTest {

  @Test
  public void test1() {
    // cnt(x) = 键 x 的使用计数
    // cache=[] 将显示最后一次使用的顺序（最左边的元素是最近的）
    LFUCache_1 lfu = new LFUCache_1(2);
    lfu.put(1, 1);                        // cache=[1,_], cnt(1)=1
    lfu.put(2, 2);                        // cache=[2,1], cnt(2)=1, cnt(1)=1
    System.out.println(lfu.get(1));       // 返回 1
    // cache=[1,2], cnt(2)=1, cnt(1)=2
    lfu.put(3, 3);   // 去除键 2 ，因为 cnt(2)=1 ，使用计数最小
    // cache=[3,1], cnt(3)=1, cnt(1)=2
    System.out.println(lfu.get(2));      // 返回 -1（未找到）
    System.out.println(lfu.get(3));      // 返回 3
    // cache=[3,1], cnt(3)=2, cnt(1)=2
    lfu.put(4, 4);   // 去除键 1 ，1 和 3 的 cnt 相同，但 1 最久未使用
    // cache=[4,3], cnt(4)=1, cnt(3)=2
    System.out.println(lfu.get(1));      // 返回 -1（未找到）
    System.out.println(lfu.get(3));      // 返回 3
    // cache=[3,4], cnt(4)=1, cnt(3)=3
    System.out.println(lfu.get(4));      // 返回 4
    // cache=[3,4], cnt(4)=2, cnt(3)=3
  }

  @Test
  public void test2() {
    // cnt(x) = 键 x 的使用计数
    // cache=[] 将显示最后一次使用的顺序（最左边的元素是最近的）
    LFUCache_2 lfu = new LFUCache_2(2);
    lfu.put(1, 1);                        // cache=[1,_], cnt(1)=1
    lfu.put(2, 2);                        // cache=[2,1], cnt(2)=1, cnt(1)=1
    System.out.println(lfu.get(1));       // 返回 1
    // cache=[1,2], cnt(2)=1, cnt(1)=2
    lfu.put(3, 3);   // 去除键 2 ，因为 cnt(2)=1 ，使用计数最小
    // cache=[3,1], cnt(3)=1, cnt(1)=2
    System.out.println(lfu.get(2));      // 返回 -1（未找到）
    System.out.println(lfu.get(3));      // 返回 3
    // cache=[3,1], cnt(3)=2, cnt(1)=2
    lfu.put(4, 4);   // 去除键 1 ，1 和 3 的 cnt 相同，但 1 最久未使用
    // cache=[4,3], cnt(4)=1, cnt(3)=2
    System.out.println(lfu.get(1));      // 返回 -1（未找到）
    System.out.println(lfu.get(3));      // 返回 3
    // cache=[3,4], cnt(4)=1, cnt(3)=3
    System.out.println(lfu.get(4));      // 返回 4
    // cache=[3,4], cnt(4)=2, cnt(3)=3
  }

}
