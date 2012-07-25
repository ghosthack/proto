package content.datastore.view;

import content.handlers.ResultHandler;
import content.handlers.view.*;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ViewDataStore {
  public void create(final String id, final ViewCreate element, final ViewCreateHandler handler) {
    read(id, new ResultHandler<ViewRead>() {
      @Override
      public void success(ViewRead element) {
        handler.alreadyExists();
      }

      @Override
      public void notFound() {
        int rows = jdbcTemplate.update(create.newPreparedStatementCreator(new Object[]{element.getValue(), id}));
        if (rows <= 0) {
          handler.unableToUpdate();
        } else {
          handler.success();
        }
      }
    });
  }

  public void read(String id, final ResultHandler<ViewRead> handler) {
    jdbcTemplate.query(select.newPreparedStatementCreator(new Object[]{id}), new ResultSetExtractor() {
      @Override
      public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (rs.next()) {
          ViewRead element = new ViewRead();
          element.setValue(rs.getString(1));
          handler.success(element);
        } else {
          handler.notFound();
        }
        return null;
      }
    });
  }

  public void update(final String id, final ViewUpdate element, final ViewModifyHandler handler) {
    read(id, new ResultHandler<ViewRead>() {
      @Override
      public void success(ViewRead element1) {
        int rows = jdbcTemplate.update(update.newPreparedStatementCreator(new Object[]{element.getValue(), id}));
        if (rows <= 0) {
          handler.unableToUpdate();
        } else {
          handler.success();
        }
      }

      @Override
      public void notFound() {
        handler.notFound();
      }
    });
  }

  public void delete(final String id, final ViewDeleteHandler elementDeleteHandler) {
    read(id, new ResultHandler<ViewRead>() {
      @Override
      public void success(ViewRead element) {
        int rows = jdbcTemplate.update(delete.newPreparedStatementCreator(new Object[]{id}));
        if (rows <= 0) {
          elementDeleteHandler.unableToUpdate();
        } else {
          elementDeleteHandler.success();
        }
      }

      @Override
      public void notFound() {
        elementDeleteHandler.notFound();
      }
    });
  }

  public ViewDataStore(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public static final String INSERT = "INSERT INTO `VIEW` (`VALUE`, `KEY`) VALUES (?, ?)";

  public static final String SELECT = "SELECT `VALUE` FROM `VIEW` WHERE `KEY`=?";

  public static final String UPDATE = "UPDATE `VIEW` SET `VALUE`=? WHERE `KEY`=?";

  public static final String DELETE = "DELETE FROM `VIEW` WHERE `KEY`=?";

  private final PreparedStatementCreatorFactory create = new PreparedStatementCreatorFactory(
    INSERT, new int[]{Types.VARCHAR, Types.VARCHAR, });

  private final PreparedStatementCreatorFactory select = new PreparedStatementCreatorFactory(
    SELECT, new int[]{Types.VARCHAR, });

  private final PreparedStatementCreatorFactory update = new PreparedStatementCreatorFactory(
    UPDATE, new int[]{Types.VARCHAR, Types.VARCHAR, });

  private final PreparedStatementCreatorFactory delete = new PreparedStatementCreatorFactory(
    DELETE, new int[]{Types.VARCHAR, });

  private final JdbcTemplate jdbcTemplate;

  private static final Logger log = Logger.getLogger(ViewDataStore.class);

}
