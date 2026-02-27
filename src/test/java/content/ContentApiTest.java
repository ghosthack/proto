package content;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the Content REST API.
 * Starts an embedded Jetty + in-memory H2 and exercises the full stack.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContentApiTest {

    private static Server server;
    private static int port;
    private static HttpClient client;

    @BeforeAll
    static void startServer() throws Exception {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        config.setMaximumPoolSize(2);
        var ds = new HikariDataSource(config);

        Services.init(ds);
        Services.manager().init();

        server = App.createServer(0); // random available port
        server.start();
        port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void stopServer() throws Exception {
        if (server != null) server.stop();
    }

    // -- Helpers ---------------------------------------------------------------

    private String url(String path) {
        return "http://localhost:" + port + "/content" + path;
    }

    private HttpResponse<String> get(String path) throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(URI.create(url(path))).GET().build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String body)
            throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(URI.create(url(path)))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> put(String path, String body)
            throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(URI.create(url(path)))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> delete(String path)
            throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(URI.create(url(path))).DELETE().build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    // -- Health check ----------------------------------------------------------

    @Test
    @Order(1)
    void healthCheck() throws Exception {
        var res = get("/check");
        assertEquals(200, res.statusCode());
        assertEquals("OK\n", res.body());
    }

    // -- Element CRUD ----------------------------------------------------------

    @Test
    @Order(10)
    void createElement() throws Exception {
        var res = post("/element/e1", "{\"value\":\"demo\",\"tilde\":\"a\"}");
        assertEquals(201, res.statusCode());
    }

    @Test
    @Order(11)
    void readElement() throws Exception {
        var res = get("/element/e1");
        assertEquals(200, res.statusCode());
        assertTrue(res.body().contains("demo"));
    }

    @Test
    @Order(12)
    void updateElement() throws Exception {
        var res = put("/element/e1", "{\"value\":\"updated\",\"tilde\":\"b\"}");
        assertEquals(200, res.statusCode());
    }

    @Test
    @Order(13)
    void readUpdatedElement() throws Exception {
        var res = get("/element/e1");
        assertEquals(200, res.statusCode());
        assertTrue(res.body().contains("updated"));
    }

    @Test
    @Order(14)
    void createDuplicate() throws Exception {
        var res = post("/element/e1", "{\"x\":1}");
        assertEquals(409, res.statusCode());
    }

    @Test
    @Order(15)
    void readMissing() throws Exception {
        var res = get("/element/nonexistent");
        assertEquals(404, res.statusCode());
    }

    // -- Template CRUD ---------------------------------------------------------

    @Test
    @Order(20)
    void createTemplate() throws Exception {
        var res = post("/template/t1", "{{value}} {{tilde}}");
        assertEquals(201, res.statusCode());
    }

    @Test
    @Order(21)
    void readTemplate() throws Exception {
        var res = get("/template/t1");
        assertEquals(200, res.statusCode());
        assertTrue(res.body().contains("{{value}}"));
    }

    // -- View CRUD -------------------------------------------------------------

    @Test
    @Order(30)
    void createViewMapping() throws Exception {
        var res = post("/view/e1/t1", "");
        // Dual-param mapping: element=e1, template=t1
        assertEquals(201, res.statusCode());
    }

    @Test
    @Order(31)
    void readView() throws Exception {
        var res = get("/view/e1");
        assertEquals(200, res.statusCode());
        assertEquals("t1\n", res.body());
    }

    // -- Render ----------------------------------------------------------------

    @Test
    @Order(40)
    void renderByView() throws Exception {
        var res = get("/render/e1");
        assertEquals(200, res.statusCode());
        assertEquals("updated b\n", res.body());
    }

    @Test
    @Order(41)
    void renderByPair() throws Exception {
        var res = get("/render/e1/t1");
        assertEquals(200, res.statusCode());
        assertEquals("updated b\n", res.body());
    }

    @Test
    @Order(42)
    void renderMissingElement() throws Exception {
        var res = get("/render/missing/t1");
        assertEquals(404, res.statusCode());
    }

    @Test
    @Order(43)
    void renderHtmlContentType() throws Exception {
        var req = HttpRequest.newBuilder(URI.create(url("/render/e1/t1")))
                .GET()
                .header("Accept", "text/html")
                .build();
        var res = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, res.statusCode());
        assertTrue(res.headers().firstValue("Content-Type")
                .orElse("").contains("text/html"));
    }

    // -- Delete ----------------------------------------------------------------

    @Test
    @Order(90)
    void deleteElement() throws Exception {
        var res = delete("/element/e1");
        assertEquals(200, res.statusCode());
    }

    @Test
    @Order(91)
    void deleteConfirmed() throws Exception {
        var res = get("/element/e1");
        assertEquals(404, res.statusCode());
    }

    @Test
    @Order(92)
    void deleteNonexistent() throws Exception {
        var res = delete("/element/e1");
        assertEquals(404, res.statusCode());
    }
}
