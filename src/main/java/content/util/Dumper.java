package content.util;

import java.io.IOException;
import java.io.InputStream;

public class Dumper {

  public static String dump(InputStream is, int size, String charSet) throws IOException {
    if(is == null)
      throw new IllegalArgumentException();
    byte[] os = new byte[size];
    try {
      is.read(os);
    } finally {
      is.close();
    }
    return new String(os, charSet);
  }

}
