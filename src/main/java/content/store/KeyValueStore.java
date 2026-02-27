package content.store;

import content.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Generic key-value CRUD store backed by JDBC.
 * <p>
 * One instance per logical table (ELEMENT, TEMPLATE, "VIEW").
 * All identifiers are quoted to avoid H2 2.x reserved-word conflicts.
 */
public final class KeyValueStore {

    private static final Logger log = LoggerFactory.getLogger(KeyValueStore.class);

    private final DataSource dataSource;
    private final String table;

    // Pre-built SQL with quoted identifiers
    private final String insertSql;
    private final String selectSql;
    private final String updateSql;
    private final String deleteSql;

    public KeyValueStore(DataSource dataSource, String table) {
        this.dataSource = dataSource;
        this.table = table;
        this.insertSql = "INSERT INTO \"" + table + "\" (\"KEY\", \"VALUE\") VALUES (?, ?)";
        this.selectSql = "SELECT \"VALUE\" FROM \"" + table + "\" WHERE \"KEY\" = ?";
        this.updateSql = "UPDATE \"" + table + "\" SET \"VALUE\" = ? WHERE \"KEY\" = ?";
        this.deleteSql = "DELETE FROM \"" + table + "\" WHERE \"KEY\" = ?";
    }

    public Result<String> create(String key, String value) {
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(insertSql)) {
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.executeUpdate();
            return Result.ok(value);
        } catch (SQLIntegrityConstraintViolationException e) {
            return Result.alreadyExists();
        } catch (SQLException e) {
            log.error("Failed to create {} in {}", key, table, e);
            return Result.error("Failed to create entry", e);
        }
    }

    public Result<String> read(String key) {
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, key);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Result.ok(rs.getString(1));
                }
                return Result.notFound();
            }
        } catch (SQLException e) {
            log.error("Failed to read {} from {}", key, table, e);
            return Result.error("Failed to read entry", e);
        }
    }

    public Result<String> update(String key, String value) {
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(updateSql)) {
            stmt.setString(1, value);
            stmt.setString(2, key);
            int rows = stmt.executeUpdate();
            if (rows == 0) return Result.notFound();
            return Result.ok(value);
        } catch (SQLException e) {
            log.error("Failed to update {} in {}", key, table, e);
            return Result.error("Failed to update entry", e);
        }
    }

    public Result<Void> delete(String key) {
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(deleteSql)) {
            stmt.setString(1, key);
            int rows = stmt.executeUpdate();
            if (rows == 0) return Result.notFound();
            return Result.ok(null);
        } catch (SQLException e) {
            log.error("Failed to delete {} from {}", key, table, e);
            return Result.error("Failed to delete entry", e);
        }
    }
}
