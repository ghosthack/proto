package content.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Initializes the database schema.
 * Uses quoted identifiers for H2 2.x compatibility.
 */
public final class Schema {

    private static final Logger log = LoggerFactory.getLogger(Schema.class);

    private final DataSource dataSource;

    public Schema(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialize() {
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS "ELEMENT" (
                        "KEY"   VARCHAR(250) PRIMARY KEY,
                        "VALUE" VARCHAR(2500)
                    )""");

            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS "TEMPLATE" (
                        "KEY"   VARCHAR(250) PRIMARY KEY,
                        "VALUE" VARCHAR(2500)
                    )""");

            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS "VIEW" (
                        "KEY"   VARCHAR(250) PRIMARY KEY,
                        "VALUE" VARCHAR(250)
                    )""");

            log.info("Database schema initialized");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
}
