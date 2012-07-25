package content;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.mysql.jdbc.Driver;

public class ContentDataSource {

  public static DataSource createDataSource() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName(Driver.class.getName());
    ds.setUrl("jdbc:mysql://127.0.0.1/content?userServerPrepStmts=true");
    ds.setUsername("root");
    ds.setPassword("");
    return ds;
  }

}
