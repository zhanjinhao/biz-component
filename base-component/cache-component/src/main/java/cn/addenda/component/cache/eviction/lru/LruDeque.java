package cn.addenda.component.cache.eviction.lru;

/**
 * LRU实现中用于存储新旧关系的双向链表
 *
 * @author addenda
 * @since 2023/05/30
 */
public interface LruDeque<P> {

  P getFirst();

  void addLast(P p);

  void remove(P p);

  boolean contains(P p);

  long size();

}
