package content;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import content.datastore.element.ElementDataStore;
import content.datastore.template.TemplateDataStore;
import content.datastore.view.ViewDataStore;
import content.handlers.element.ElementCreate;
import content.handlers.element.ElementModify;
import content.util.CharsetConstant;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.Test;

import com.google.gson.Gson;

public class TestContentApi {

  static {
    PropertyConfigurator.configure(TestContentApi.class.getResource("/logging.properties"));
  }

  public static void main(String args[]) throws Exception {
    TestContentApi test = new TestContentApi();
    test.all();
  }

  public void all() throws Exception {
//    log.debug("********* Test content check");
//    testContentCheck();
    log.debug("********* Test create element");
    testCreateElement();
    log.debug("********* Test create element ISO");
    testCreateElementIso();
//    log.debug("********* Test render element");
//    testRender1Element();
  }

  @Test
  public void testRender1Element() throws Exception {
    ServletContext context = mock(ServletContext.class);
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    PreparedStatement viewStatement = mock(PreparedStatement.class);
    PreparedStatement templateStatement = mock(PreparedStatement.class);
    PreparedStatement elementStatement = mock(PreparedStatement.class);
    ResultSet viewSet = mock(ResultSet.class);
    ResultSet templateSet = mock(ResultSet.class);
    ResultSet elementSet = mock(ResultSet.class);
    String id = "1";
    String templateId = "2";
    String template = "This is a test string: {value}";
    String element = "{\"value\": \"Test\"}";

    when(context.getAttribute("dataSource")).thenReturn(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);

    when(connection.prepareStatement(ViewDataStore.SELECT)).thenReturn(viewStatement);
    when(viewSet.next()).thenReturn(true);
    when(viewSet.getString(1)).thenReturn(templateId);
    when(viewStatement.executeQuery()).thenReturn(viewSet);

    when(connection.prepareStatement(TemplateDataStore.SELECT)).thenReturn(templateStatement);
    when(templateSet.next()).thenReturn(true);
    when(templateSet.getString(1)).thenReturn(template);
    when(templateStatement.executeQuery()).thenReturn(templateSet);

    when(connection.prepareStatement(ElementDataStore.SELECT)).thenReturn(elementStatement);
    when(elementSet.next()).thenReturn(true);
    when(elementSet.getString(1)).thenReturn(element);
    when(elementStatement.executeQuery()).thenReturn(elementSet);

    HttpServletRequest req = mockReq("GET", "/render/" + id);
    MockHttpServletResponse res = mockRes();

    String expected = "text/html";
    when(req.getHeader("Accept")).thenReturn(expected);

    ContentHandler.create(context).service(req, res);
    assertEquals(res.getStringWriter().toString(), "This is a test string: Test");

    assertEquals(res.getContentType(), expected);
    assertEquals(res.getStatus(), 200);
  }

  @Test
  public void testRender1ElementNotFound() throws Exception {
    ServletContext context = mock(ServletContext.class);
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    PreparedStatement statement1 = mock(PreparedStatement.class);
    ResultSet set = mock(ResultSet.class);

    when(set.next()).thenReturn(false);
    when(statement1.executeQuery()).thenReturn(set);
    when(dataSource.getConnection()).thenReturn(connection);
    when(context.getAttribute("dataSource")).thenReturn(dataSource);
    when(connection.prepareStatement(ViewDataStore.SELECT)).thenReturn(statement1);

    int id = createId();

    HttpServletRequest req = mockReq("GET", "/render/" + id);
    MockHttpServletResponse res = mockRes();

//    ElementCreate element = createSampleCreateElement(id);
//    when(req.getReader()).thenReturn(new BufferedReader(new StringReader(new Gson().toJson(element))));

    PreparedStatement insertStatement = mock(PreparedStatement.class);
    when(insertStatement.executeUpdate()).thenReturn(1);
    //when(selectStatement.executeQuery()).thenReturn(set);

    ContentHandler.create(context).service(req, res);
    assertEquals(res.getStatus(), 404);
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
    element.setValue("á");
    String json = new Gson().toJson(element);
    byte[] jsonBytes = json.getBytes(CharsetConstant.UTF_8);
    when(req.getContentLength()).thenReturn(jsonBytes.length);
    when(req.getInputStream()).thenReturn(new MockServletInputStream(jsonBytes));

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
  public void testCreateElementIso() throws Exception {
    ServletContext context = mock(ServletContext.class);
    when(context.getAttribute("dataSource")).thenReturn(ContentDataSource.createDataSource());
    int id = createId();

    HttpServletRequest req = mockReq("POST", "/element/" + id);
    MockHttpServletResponse res = mockRes();

    ElementCreate element = createSampleCreateElement(id);
    element.setValue("á");
    String json = new Gson().toJson(element);
    String encoding = "ISO-8859-1";
    byte[] jsonBytes = json.getBytes(encoding);
    when(req.getCharacterEncoding()).thenReturn(encoding);
    when(req.getContentLength()).thenReturn(jsonBytes.length);
    when(req.getInputStream()).thenReturn(new MockServletInputStream(jsonBytes));

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

  private final Logger log = Logger.getLogger(TestContentApi.class);

}
