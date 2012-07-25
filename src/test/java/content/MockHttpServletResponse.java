package content;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

public class MockHttpServletResponse extends HttpServletResponseWrapper {

  private int status;
  StringWriter writer = new StringWriter();

  public MockHttpServletResponse(HttpServletResponse response) {
    super(response);
  }

  public StringWriter getStringWriter() {
    return writer;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    return new PrintWriter(writer);
  }

  @Override
  public void setStatus(int sc) {
    log.debug("Status set to " + sc);
    this.status = sc;
  }

  public int getStatus() {
    return status;
  }
  private static final Logger log = Logger.getLogger(MockHttpServletResponse.class);

}
