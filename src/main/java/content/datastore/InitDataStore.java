package content.datastore;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class InitDataStore {

  public void element() {
    jdbcTemplate.update(ELEMENT);
  }

  public void template() {
    jdbcTemplate.update(TEMPLATE);
  }

  public void view() {
    jdbcTemplate.update(VIEW);
  }

  public InitDataStore(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public static final String ELEMENT = "CREATE TABLE `ELEMENT`(`KEY` VARCHAR(250) PRIMARY KEY, `VALUE` VARCHAR(2500));";

  public static final String TEMPLATE = "CREATE TABLE `TEMPLATE`(`KEY` VARCHAR(250) PRIMARY KEY, `VALUE` VARCHAR(2500));";

  public static final String VIEW = "CREATE TABLE `VIEW`(`KEY` VARCHAR(250) PRIMARY KEY, `VALUE` VARCHAR(250));";

  private final JdbcTemplate jdbcTemplate;

  private static final Logger log = Logger.getLogger(InitDataStore.class);

}
