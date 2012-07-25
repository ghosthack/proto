package content.util;

import java.io.*;

public class Dumper {

  public static String dump(InputStream is, int size, String charSet) throws IOException {
    BufferedInputStream bis = null;
    ByteArrayOutputStream os = null;
    try {
      os = new ByteArrayOutputStream(size);
      bis = new BufferedInputStream(is);
      transfer(bis, os);
      return os.toString(charSet);
    } finally {
      try {
        if (bis != null)
          bis.close();
      } finally {
        if (os != null)
          os.close();
      }
    }
  }

  private static void transfer(BufferedInputStream in, OutputStream out) throws IOException {
    int br;
    while ((br = in.read()) != -1) {
      out.write(br);
    }
  }

  public static String dump(Reader is) throws IOException {
    BufferedReader bis = null;
    StringBuilder sb = new StringBuilder();
    try {
      bis = new BufferedReader(is);
      transfer2(bis, sb);
      return sb.toString();
    } finally {
      if (bis != null)
        bis.close();
    }

  }

  private static void transfer2(BufferedReader in, StringBuilder out) throws IOException {
    String br;
    while ((br = in.readLine()) != null) {
      out.append(br);
    }
  }
}
