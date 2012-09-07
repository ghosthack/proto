package content;

import turismo.multipart.SimpleMultipartInputStream;

import java.io.ByteArrayInputStream;

public class TestMultipartInputStream {
  public static void main(String[] args) throws Exception {
    String content =
      "------------------------------8c3e91c7ea8f\r\n" +
      "Content-Disposition: form-data; name=\"image1\"; filename=\"1\"\r\n" +
      "Content-Type: image/jpeg\r\n" +
      "\r\n" +
        "\r\n" +
      "http://a/1?ws=4fa87759-826f-e011-971f-0030487d8897&vsro=8\n" +
      "-----------87759-826f-e011-971f-0030487d8897&vsro=8\n" +
      "-----------------87759-826f-e011-971f-0030487d8897&vsro=8\n" +
      "\r\n----------------------------------8c3e91c7ea8--------------------------------8c3e91c7ea8f-\n\n" +
        "\r\r\r" +
        "\r\r\r" +
        "\r\n" +
        "\r\n" +
      "------------------------------8c3e91c7ea8f--\r\n";

    String characterEncoding = "UTF-8";
    ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes(characterEncoding));
    String boundary = "------------------------------8c3e91c7ea8f";
    SimpleMultipartInputStream multipartStream = new SimpleMultipartInputStream(is, characterEncoding, boundary);
    int next;
    while((next = multipartStream.read()) != -1) {
      debug(next);
    }
    System.out.println();
  }

  public static void debug(int n) {
    byte next = (byte) n;
    if(Character.isLetterOrDigit(next) || Character.isWhitespace(next) || !Character.isUnicodeIdentifierPart(next)) {
      if(next == 0x0A) {
        System.out.println("(0x0A)");
      } else if(next == 0x0D) {
        System.out.print("(0x0D)");
      } else {
        System.out.print(Character.toChars(next));
      }
    } else {
      System.out.print("*");
    }
  }

}