package cn.addenda.component.basaspring.util;

import cn.addenda.component.jackson.util.JacksonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2023/7/29 19:08
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpELUtils {

  public static final String MD5 = "T(cn.addenda.bc.bc.jc.util.MD5Utils).md5(#spELArgs)";
  public static final String USER_ID = "T(cn.addenda.bc.bc.uc.user.UserContext).getUserId()";

  private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();

  private static final LocalVariableTableParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

  public static Object getValue(String spEL, Method method, String spELArgsName, Object... arguments) {
    Expression exp = SPEL_PARSER.parseExpression(spEL);
    StandardEvaluationContext context = new StandardEvaluationContext();
    if (method != null) {
      String[] params = NAME_DISCOVERER.getParameterNames(method);
      if (params != null && params.length != 0) {
        for (int len = 0; len < params.length; len++) {
          context.setVariable(params[len], arguments[len]);
        }
      }
    }
    context.setVariable(spELArgsName, arguments);
    return exp.getValue(context);
  }

  public static String getKey(String spEL, Method method, String spELArgsName, Object... arguments) {
    // 默认取第一位参数
    if (!StringUtils.hasLength(spEL)) {
      spEL = "#" + spELArgsName + "[0]";
    }

    if (spEL.contains("#") || spEL.matches("T\\([\\w.]+\\)\\.\\w+\\(\\)")) {
      Object value = SpELUtils.getValue(spEL, method, spELArgsName, arguments);
      if (value == null) {
        String msg = String.format("Cannot get value from arguments: [%s], spEL: [%s].", JacksonUtils.toStr(arguments), spEL);
        throw new EvaluationException(msg);
      }
      return value.toString();
    }
    return spEL;
  }

  public static Object getObject(String spEL, Object argument) {
    Expression exp = SPEL_PARSER.parseExpression(spEL);
    StandardEvaluationContext context = new StandardEvaluationContext(argument);
    return exp.getValue(context);
  }

  public static Object getObjectIgnoreException(String spEL, Object argument) {
    try {
      Expression exp = SPEL_PARSER.parseExpression(spEL);
      StandardEvaluationContext context = new StandardEvaluationContext(argument);
      return exp.getValue(context);
    } catch (Exception e) {
      log.debug("SpringEL执行失败，spEL[{}]，argument[{}]。", spEL, argument);
      return null;
    }
  }

  public static <T> T getObject(String spEL, Object argument, Class<T> returnClass) {
    Expression exp = SPEL_PARSER.parseExpression(spEL);
    StandardEvaluationContext context = new StandardEvaluationContext(argument);
    return exp.getValue(context, returnClass);
  }

  public static <T> T getObjectIgnoreException(String spEL, Object argument, Class<T> returnClass) {
    try {
      Expression exp = SPEL_PARSER.parseExpression(spEL);
      StandardEvaluationContext context = new StandardEvaluationContext(argument);
      return exp.getValue(context, returnClass);
    } catch (Exception e) {
      log.debug("SpringEL执行失败，spEL[{}]，argument[{}]，returnClass[{}]。", spEL, argument, returnClass);
      return null;
    }
  }

}
