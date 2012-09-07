package content.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

  public static void transfer(BufferedInputStream stream, OutputStream os) throws IOException {
    int next;
    while((next = stream.read()) != -1) {
      os.write(next);
    }
  }
}
