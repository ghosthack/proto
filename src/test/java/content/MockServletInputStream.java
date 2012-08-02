package content;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 8/2/12
 * Time: 3:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class MockServletInputStream extends ServletInputStream {

  protected final ByteArrayInputStream bais;

  public MockServletInputStream(byte[] bs) {
    this.bais = new ByteArrayInputStream(bs);
  }

  @Override
  public int read() throws IOException {
    return bais.read();
  }
}
