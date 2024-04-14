package cn.addenda.component.test.cache.lru;

import org.junit.Test;

public class LRUTest {

  @Test
  public void test1() {
    LRUCache_1 lRUCache = new LRUCache_1(2);
    lRUCache.put(1, 1);                     // 缓存是 {1=1}
    lRUCache.put(2, 2);                     // 缓存是 {1=1, 2=2}
    System.out.println(lRUCache.get(1));    // 返回 1
    lRUCache.put(3, 3);                     // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
    System.out.println(lRUCache.get(2));    // 返回 -1 (未找到)
    lRUCache.put(4, 4);                     // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
    System.out.println(lRUCache.get(1));    // 返回 -1 (未找到)
    System.out.println(lRUCache.get(3));    // 返回 3
    System.out.println(lRUCache.get(4));    // 返回 4
  }

  @Test
  public void test2() {
    LRUCache_2 lRUCache = new LRUCache_2(2);
    lRUCache.put(1, 1);                     // 缓存是 {1=1}
    lRUCache.put(2, 2);                     // 缓存是 {1=1, 2=2}
    System.out.println(lRUCache.get(1));    // 返回 1
    lRUCache.put(3, 3);                     // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
    System.out.println(lRUCache.get(2));    // 返回 -1 (未找到)
    lRUCache.put(4, 4);                     // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
    System.out.println(lRUCache.get(1));    // 返回 -1 (未找到)
    System.out.println(lRUCache.get(3));    // 返回 3
    System.out.println(lRUCache.get(4));    // 返回 4
  }

}
