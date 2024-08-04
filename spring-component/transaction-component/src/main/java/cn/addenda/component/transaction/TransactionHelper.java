package cn.addenda.component.transaction;

import cn.addenda.component.jdk.lambda.FunctionConverter;
import cn.addenda.component.jdk.lambda.TRunnable;
import cn.addenda.component.jdk.lambda.TSupplier;
import cn.addenda.component.jdk.util.ExceptionUtils;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/3/3 17:26
 */
public class TransactionHelper extends TransactionAspectSupport {

  private String transactionManagerBeanName;

  private TransactionManager transactionManager;

  public TransactionHelper() {
    setTransactionAttributeSource(new TransactionHelperAttrSource());
  }

  public TransactionHelper(TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
    setTransactionManager(transactionManager);
  }

  public TransactionHelper(String transactionManagerBeanName) {
    this.transactionManagerBeanName = transactionManagerBeanName;
    setTransactionManagerBeanName(transactionManagerBeanName);
  }

  public TransactionHelper(String transactionManagerBeanName, TransactionManager transactionManager) {
    this.transactionManagerBeanName = transactionManagerBeanName;
    this.transactionManager = transactionManager;
    setTransactionManager(transactionManager);
    setTransactionManagerBeanName(transactionManagerBeanName);
  }

  /**
   * 最简单的事务控制场景（当发生任何异常（Exception.class）都回滚事务），
   */
  public <R> R doTransaction(TSupplier<R> supplier) {
    return doTransaction(Exception.class, supplier);
  }

  public void doTransaction(TRunnable runnable) {
    doTransaction(Exception.class, runnable);
  }

  /**
   * 较上一个场景，该场景可以指定针对特定的异常类型发生事务回滚
   */
  public <R> R doTransaction(Class<? extends Throwable> rollbackFor, TSupplier<R> supplier) {
    TransactionAttribute attribute = TransactionAttrBuilder.newBuilder().rollbackFor(rollbackFor).build();
    return doTransaction(attribute, supplier);
  }

  public void doTransaction(Class<? extends Throwable> rollbackFor, TRunnable runnable) {
    TransactionAttribute attribute = TransactionAttrBuilder.newBuilder().rollbackFor(rollbackFor).build();
    doTransaction(attribute, runnable);
  }

  /**
   * 最复杂的场景，需要手动指定所有的事务控制参数，TransactionAttribute 可以通过 TransactionAttributeBuilder构造
   * TransactionAttributeBuilder的入参跟@Transactional注解的参数保持一致
   */
  public <R> R doTransaction(TransactionAttribute txAttr, TSupplier<R> supplier) {
    return _process(txAttr, supplier);
  }

  public void doTransaction(TransactionAttribute txAttr, TRunnable runnable) {
    doTransaction(txAttr, FunctionConverter.toTSupplier(runnable));
  }

  private <R> R _process(TransactionAttribute txAttr, TSupplier<R> supplier) {
    TransactionHelperAttrSource.pushAttr(txAttr);
    try {
      return (R) invokeWithinTransaction(extractMethod(supplier), supplier.getClass(), () -> supplier.get());
    } catch (Throwable throwable) {
      throw ExceptionUtils.wrapAsRuntimeException(throwable, TransactionException.class);
    } finally {
      TransactionHelperAttrSource.popAttr();
    }
  }

  private Method extractMethod(TSupplier<?> supplier) {
    Method[] methods = supplier.getClass().getMethods();
    for (Method method : methods) {
      if ("get".equals(method.getName()) && method.getParameterCount() == 0) {
        return method;
      }
    }
    throw new TransactionException("找不到 TSupplier#get() 方法。");
  }

}
