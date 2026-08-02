package cn.addenda.component.common.lambda;

/**
 * @author addenda
 * @since 2023/10/5 23:29
 */
@FunctionalInterface
public interface ExceptionRunnable {

  void run() throws Exception;

}
