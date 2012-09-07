package content.util;

import org.apache.log4j.Logger;
import turismo.action.ActionException;
import turismo.multipart.MultipartRequestHelper;
import turismo.multipart.SimpleMultipartInputStream;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public class RequestHelper {

  public static InputStream readMultipartStream(HttpServletRequest req) throws IOException {
    String characterEncoding = req.getCharacterEncoding();
    if(characterEncoding == null) {
      characterEncoding = CharsetConstant.UTF_8;
      log.debug("Default character encoding " + characterEncoding);
    } else {
      log.debug("Received character encoding " + characterEncoding);
    }
    String boundary = MultipartRequestHelper.extractBoundary(req);
    SimpleMultipartInputStream stream = new SimpleMultipartInputStream(req.getInputStream(), characterEncoding, boundary);
    return stream;
  }

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
