package cn.addenda.component.transaction;

import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 * @author addenda
 * @since 2022/3/3 17:26
 */
public class TransactionHelperAttrSource implements TransactionAttributeSource {

  private static final ThreadLocal<Stack<TransactionAttribute>> THREAD_LOCAL = new ThreadLocal<>();

  @Override
  public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
    return THREAD_LOCAL.get().peek();
  }

  public static void pushAttr(TransactionAttribute transactionAttribute) {
    Stack<TransactionAttribute> transactionAttributes = THREAD_LOCAL.get();
    if (transactionAttributes == null) {
      transactionAttributes = new Stack<>();
      THREAD_LOCAL.set(transactionAttributes);
    }
    transactionAttributes.push(transactionAttribute);
  }

  public static void popAttr() {
    Stack<TransactionAttribute> transactionAttributes = THREAD_LOCAL.get();
    transactionAttributes.pop();
    if (transactionAttributes.isEmpty()) {
      THREAD_LOCAL.remove();
    }
  }

}
