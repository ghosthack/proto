package content;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.mysql.jdbc.Driver;

public class ContentDataSource {

  public static DataSource createDataSource() {
    //return mysqlDataSource();
    return h2DataSource();
  }

  private static DataSource mysqlDataSource() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName(Driver.class.getName());
    ds.setUrl("jdbc:mysql://127.0.0.1/content?userServerPrepStmts=true");
    ds.setUsername("root");
    ds.setPassword("");
    return ds;
  }

  private static DataSource h2DataSource() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName(org.h2.Driver.class.getName());
    ds.setUrl("jdbc:h2:~/content");
    ds.setUsername("");
    ds.setPassword("");
    return ds;
  }

}
