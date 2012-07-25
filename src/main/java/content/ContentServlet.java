package content;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

import turismo.servlet.Handler;

public class ContentServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private Handler handler;

  @Override
  public void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    handler.service(req, res);
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    Properties properties = new Properties();
    properties.put("log4j.rootLogger", "DEBUG, C");
    properties.put(".level", "DEBUG");
    properties.put("log4j.appender.C", "org.apache.log4j.ConsoleAppender");
    properties.put("log4j.appender.C.layout", "org.apache.log4j.PatternLayout");
    properties.put("log4j.appender.C.layout.ConversionPattern", "%-5p %c %x - %m%n");
    PropertyConfigurator.configure(properties);
    super.init();
    ServletContext context = config.getServletContext();
    context.setAttribute(ContentHandler.DATA_SOURCE, ContentDataSource.createDataSource());
    handler = ContentHandler.create(context);
  }

}
