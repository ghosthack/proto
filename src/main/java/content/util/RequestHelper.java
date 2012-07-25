package content.util;

import turismo.action.ActionException;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class RequestHelper {
  private static final String UTF_8 = "UTF-8";

  public static Object readJson(HttpServletRequest req) {
    Reader is = null;
    try {
      is = req.getReader();
      return JSONValue.parseWithException(is);
    } catch (IOException e) {
      throw new ActionException(e);
    } catch (ParseException e) {
      //TODO: should be an specific 400 error
      throw new ActionException(e);
    } finally {
      if (is != null) try {
        is.close();
      } catch (IOException e) {
        throw new ActionException(e);
      }
    }
  }

  public static String read(HttpServletRequest req) {
    String characterEncoding = req.getCharacterEncoding();
    if(characterEncoding == null)
      characterEncoding = UTF_8;
    InputStream is = null;
    try {
      is = req.getInputStream();
      return Dumper.dump(is, req.getContentLength(), characterEncoding);
    } catch (IOException e) {
      throw new ActionException(e);
    } finally {
      if (is != null) try {
        is.close();
      } catch (IOException e) {
        throw new ActionException(e);
      }
    }
  }
}
