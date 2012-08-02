package content.util;

import org.apache.log4j.Logger;
import turismo.action.ActionException;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class RequestHelper {

  public static String readString(HttpServletRequest req) {
    String characterEncoding = req.getCharacterEncoding();
    if(characterEncoding == null) {
      characterEncoding = CharsetConstant.UTF_8;
      log.debug("Default character encoding " + characterEncoding);
    } else {
      log.debug("Received character encoding " + characterEncoding);
    }
    try {
      int contentLength = req.getContentLength();
      log.debug("Content: " + contentLength);
      InputStream is = req.getInputStream();
      String data = Dumper.dump(is, contentLength, characterEncoding);
      log.debug("Stream: " + data);
      return data;
    } catch (IOException e) {
      throw new ActionException(e);
    }
  }

  private static final Logger log = Logger.getLogger(RequestHelper.class);

}
