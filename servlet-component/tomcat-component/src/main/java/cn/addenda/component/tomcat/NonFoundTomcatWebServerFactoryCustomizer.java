package cn.addenda.component.tomcat;

import cn.addenda.component.jackson.util.JacksonUtils;
import cn.addenda.component.jdk.result.Result;
import org.apache.catalina.Container;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.Constants;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.catalina.valves.JsonErrorReportValve;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.res.StringManager;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.io.Writer;

/**
 * @author addenda
 * @since 2023/8/19 12:14
 */
public class NonFoundTomcatWebServerFactoryCustomizer
        implements WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory>, Ordered {

  @Override
  public void customize(ConfigurableTomcatWebServerFactory factory) {
    factory.addContextCustomizers((context) -> {
      ErrorReportValve valve = new JsonErrorReportValve() {
        @Override
        protected void report(Request request, Response response, Throwable throwable) {
          int statusCode = response.getStatus();
          if (statusCode == 404) {
            Result<Object> result = new Result<>();
            result.setReqCode("404");
            result.setReqMessage(request.getRequestURI() + ".");

            doWrite(response, container, JacksonUtils.toStr(result));
          } else {

            StringManager smClient = StringManager.getManager(Constants.Package, request.getLocales());
            response.setLocale(smClient.getLocale());
            String type = null;
            if (throwable != null) {
              type = smClient.getString("errorReportValve.exceptionReport");
            } else {
              type = smClient.getString("errorReportValve.statusReport");
            }
            String message = response.getMessage();
            if (message == null && throwable != null) {
              message = throwable.getMessage();
            }
            String description = null;
            description = smClient.getString("http." + statusCode + ".desc");
            if (description == null) {
              if (message == null || message.isEmpty()) {
                return;
              } else {
                description = smClient.getString("errorReportValve.noDescription");
              }
            }

            String jsonReport = "{\n" +
                    "  \"type\": \"" + type + "\",\n" +
                    "  \"message\": \"" + message + "\",\n" +
                    "  \"description\": \"" + description + "\"\n" +
                    "}";

            doWrite(response, container, jsonReport);
          }
        }
      };
      valve.setShowServerInfo(false);
      valve.setShowReport(false);
      context.getParent().getPipeline().addValve(valve);
    });
  }

  private void doWrite(Response response, Container container, String jsonReport) {
    try {
      try {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
      } catch (Throwable t) {
        ExceptionUtils.handleThrowable(t);
        if (container.getLogger().isDebugEnabled()) {
          container.getLogger().debug("Failure to set the content-type of response", t);
        }
      }
      Writer writer = response.getReporter();
      if (writer != null) {
        writer.write(jsonReport);
        response.finishResponse();
        return;
      }
    } catch (IOException | IllegalStateException e) {
      // Ignore
    }

  }

  @Override
  public int getOrder() {
    return 1;
  }

}
