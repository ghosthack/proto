package content;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import turismo.Resolver;
import turismo.action.ActionException;
import turismo.servlet.ResolverHandler;

public class ContentHandler extends ResolverHandler {

  public static ContentHandler create(ServletContext context) {
    DataSource dataSource = (DataSource) context.getAttribute(DATA_SOURCE);
    ContentManager manager = new ContentManager(dataSource);
    ContentApi api = new ContentApi(manager);
    return new ContentHandler(context, api.getResolver());
  }

  public void service(HttpServletRequest req, HttpServletResponse res)
    throws ActionException {
    try {
      super.service(req, res);
    } catch (ActionException e) {
      log.error("Request handler", e);
      res.setStatus(500);
    }
  }

  public static final String DATA_SOURCE = "dataSource";

  private ContentHandler(ServletContext context, Resolver resolver) {
    super(context, resolver);
  }

  private static final Logger log = Logger.getLogger(ContentHandler.class);

}
