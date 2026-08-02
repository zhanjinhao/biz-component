package cn.addenda.component.common.lambda;

/**
 * @author addenda
 * @since 2023/6/4 15:11
 */
@FunctionalInterface
public interface ThrowableRunnable {

  void run() throws Throwable;

}
