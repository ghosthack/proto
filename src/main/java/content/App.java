package content;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.ghosthack.turismo.servlet.Servlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Application entry point.
 * <p>
 * Starts an embedded Jetty server with the Content API mounted at {@code /content/*}.
 * <pre>
 *   mvn compile exec:java
 *   mvn compile exec:java -Dexec.args="--port 9090"
 * </pre>
 */
public final class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        int port = resolvePort(args);
        var dataSource = createDataSource();

        Services.init(dataSource);
        Services.manager().init();

        var server = createServer(port);
        server.start();
        log.info("Content API listening on http://localhost:{}/content/", port);
        server.join();
    }

    static Server createServer(int port) {
        var server = new Server(port);

        var ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        ctx.setContextPath("/");

        var holder = new ServletHolder("content", new Servlet());
        holder.setInitParameter("routes", ContentRoutes.class.getName());
        ctx.addServlet(holder, "/content/*");

        server.setHandler(ctx);
        return server;
    }

    static DataSource createDataSource() {
        var dbPath = System.getProperty("db.path",
                System.getenv().getOrDefault("DB_PATH", "~/content"));
        var resolvedPath = dbPath.replace("~", System.getProperty("user.home"));

        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:" + resolvedPath);
        config.setMaximumPoolSize(5);
        config.setAutoCommit(true);

        log.info("H2 database: {}", resolvedPath);
        return new HikariDataSource(config);
    }

    private static int resolvePort(String[] args) {
        // CLI arg takes precedence: --port 9090
        for (int i = 0; i < args.length - 1; i++) {
            if ("--port".equals(args[i])) {
                return Integer.parseInt(args[i + 1]);
            }
        }
        // Then system property / env var
        var portStr = System.getProperty("port",
                System.getenv().getOrDefault("PORT", "8080"));
        return Integer.parseInt(portStr);
    }
}
