package content;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import content.handlers.element.ElementCreate;
import content.handlers.element.ElementModify;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.Test;

import com.google.gson.Gson;

public class TestApi {

  public static void main(String args[]) throws Exception {
    PropertyConfigurator.configure(TestApi.class.getResource("/logging.properties"));
    TestApi test = new TestApi();
    test.all();
  }

  public void all() throws Exception {
    log.debug("********* Test content check");
    testContentCheck();
    log.debug("********* Test create element");
    testCreateElement();
  }

  @Test
  public void testContentCheck() throws Exception {
    ServletContext context = mock(ServletContext.class);
    when(context.getAttribute("dataSource")).thenReturn(ContentDataSource.createDataSource());
    HttpServletRequest req = mockReq("GET", "/check");
    MockHttpServletResponse res = mockRes();

    ContentHandler.create(context).service(req, res);
    assertEquals(res.getStatus(), 200);
    assertEquals(res.getStringWriter().toString(), "GOOD");
  }

@Test
  public void testCreateElement() throws Exception {
    ServletContext context = mock(ServletContext.class);
    when(context.getAttribute("dataSource")).thenReturn(ContentDataSource.createDataSource());
    int id = createId();

    HttpServletRequest req = mockReq("POST", "/element/" + id);
    MockHttpServletResponse res = mockRes();

    ElementCreate element = createSampleCreateElement(id);
    when(req.getReader()).thenReturn(new BufferedReader(new StringReader(new Gson().toJson(element))));

    PreparedStatement insertStatement = mock(PreparedStatement.class);
    when(insertStatement.executeUpdate()).thenReturn(1);
    PreparedStatement selectStatement = mock(PreparedStatement.class);
    ResultSet set = mock(ResultSet.class);
    when(set.next()).thenReturn(false);
    when(selectStatement.executeQuery()).thenReturn(set);

    ContentHandler.create(context).service(req, res);
    assertEquals(res.getStatus(), 201);
  }

  @Test
  public void testReadElementNotFound() throws Exception {
    ServletContext context = mock(ServletContext.class);
    int id = createId();

    HttpServletRequest req = mockReq("GET", "/element/" + id);
    MockHttpServletResponse res = mockRes();

    PreparedStatement selectStatement = mockEmptyResultSelect();

    ContentHandler.create(context).service(req, res);
    assertEquals(res.getStatus(), 404);
  }

  private int createId() {
    return (int) (System.currentTimeMillis() / 1000);
  }

  private PreparedStatement mockEmptyResultSelect() throws Exception {
    PreparedStatement selectStatement = mock(PreparedStatement.class);
    ResultSet set = mock(ResultSet.class);
    when(set.next()).thenReturn(false);
    when(selectStatement.executeQuery()).thenReturn(set);
    return selectStatement;
  }

  private HttpServletRequest mockReq(String post, String resource) throws Exception {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getMethod()).thenReturn(post);
    when(req.getPathInfo()).thenReturn(resource);
    return req;
  }

  private MockHttpServletResponse mockRes() {
    return new MockHttpServletResponse(mock(HttpServletResponse.class));
  }

  private ElementCreate createSampleCreateElement(int id) {
    ElementCreate element = new ElementCreate();
    return element;
  }

  private ElementModify createSampleModify(int id) {
    ElementModify element = new ElementModify();
    return element;
  }

  private final Logger log = Logger.getLogger(TestApi.class);

}
