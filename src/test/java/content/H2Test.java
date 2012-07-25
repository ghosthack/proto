package content;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.PropertyConfigurator;
import org.h2.Driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class H2Test {

  public static final String JDBC_URL = "jdbc:h2:~/test";

  public static void main(String[] arg) throws Exception {
    PropertyConfigurator.configure(TestContentApi.class.getResource("/logging.properties"));
    H2Test test = new H2Test();
    test.testH2();
  }

  private void testH2() throws SQLException {
    Driver driver = new Driver();
    Connection connection = driver.connect(JDBC_URL, new Properties());
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM test");
      ResultSet set = statement.executeQuery();
      if (set.next()) {
        System.out.println(set.getString(1));
      }
    } finally {
      connection.close();
    }
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setUrl(JDBC_URL);
    dataSource.setDriverClassName(Driver.class.getName());
    ContentManager manager = new ContentManager(dataSource);
    manager.readElement("0", null);
  }

}
