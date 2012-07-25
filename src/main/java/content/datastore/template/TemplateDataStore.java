package content.datastore.template;

import content.handlers.ResultHandler;
import content.handlers.template.*;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class TemplateDataStore {
  public void create(final String id, final TemplateCreate element, final TemplateCreateHandler handler) {
    read(id, new ResultHandler<TemplateRead>() {
      @Override
      public void success(TemplateRead element) {
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

  public void read(String id, final ResultHandler<TemplateRead> resultHandler) {
    jdbcTemplate.query(select.newPreparedStatementCreator(new Object[]{id}), new ResultSetExtractor() {
      @Override
      public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (rs.next()) {
          TemplateRead element = new TemplateRead();
          element.setValue(rs.getString(1));
          resultHandler.success(element);
        } else {
          resultHandler.notFound();
        }
        return null;
      }
    });
  }

  public void update(final String id, final TemplateUpdate element, final TemplateModifyHandler handler) {
    read(id, new ResultHandler<TemplateRead>() {
      @Override
      public void success(TemplateRead element1) {
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

  public void delete(final String id, final TemplateDeleteHandler handler) {
    read(id, new ResultHandler<TemplateRead>() {
      @Override
      public void success(TemplateRead element) {
        int rows = jdbcTemplate.update(delete.newPreparedStatementCreator(new Object[]{id}));
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

  public TemplateDataStore(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public static final String INSERT = "INSERT INTO `TEMPLATE` (`VALUE`, `KEY`) VALUES (?, ?)";

  public static final String SELECT = "SELECT `VALUE` FROM `TEMPLATE` WHERE `KEY`=?";

  public static final String UPDATE = "UPDATE `TEMPLATE` SET `VALUE`=? WHERE `KEY`=?";

  public static final String DELETE = "DELETE FROM `TEMPLATE` WHERE `KEY`=?";

  private final PreparedStatementCreatorFactory create = new PreparedStatementCreatorFactory(
    INSERT, new int[]{Types.VARCHAR, Types.VARCHAR, });

  private final PreparedStatementCreatorFactory select = new PreparedStatementCreatorFactory(
    SELECT, new int[]{Types.VARCHAR, });

  private final PreparedStatementCreatorFactory update = new PreparedStatementCreatorFactory(
    UPDATE, new int[]{Types.VARCHAR, Types.VARCHAR, });

  private final PreparedStatementCreatorFactory delete = new PreparedStatementCreatorFactory(
    DELETE, new int[]{Types.VARCHAR, });

  private final JdbcTemplate jdbcTemplate;

  private static final Logger log = Logger.getLogger(TemplateDataStore.class);

}
