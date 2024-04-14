package cn.addenda.component.idgenerator.idgenerator;

/**
 * @author addenda
 * @since 2022/2/4 14:29
 */
public interface IdGenerator {

  Object nextId(String scopeName);

}
