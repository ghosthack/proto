package content.util;

import turismo.action.ActionException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseHelper {
  public static void write(HttpServletResponse res, String s) {
    PrintWriter writer = null;
    try {
      writer = res.getWriter();
      writer.write(s);
    } catch (IOException e) {
      throw new ActionException(e);
    } finally {
      if (writer != null) writer.close();
    }
  }
}
