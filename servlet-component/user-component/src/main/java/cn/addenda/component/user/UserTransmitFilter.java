package cn.addenda.component.user;

import cn.addenda.component.jdk.util.StringUtils;
import cn.addenda.component.jdk.util.UrlUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用户信息传输过滤器
 */
public class UserTransmitFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String userId = httpServletRequest.getHeader(UserConstant.USER_ID_KEY);
    if (StringUtils.hasText(userId)) {
      String userName = httpServletRequest.getHeader(UserConstant.USER_NAME_KEY);
      if (StringUtils.hasText(userName)) {
        userName = UrlUtils.decode(userName);
      }
      UserInfo userInfo = UserInfo.builder()
              .userId(userId)
              .username(userName)
              .build();
      UserContext.setUser(userInfo);
    }
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      UserContext.removeUser();
    }
  }
}
