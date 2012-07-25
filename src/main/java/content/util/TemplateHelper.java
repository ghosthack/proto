package content.util;

import jsontemplate.Template;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import turismo.action.ActionException;

public class TemplateHelper {
  public static String applyTemplate(String template, String value) {
    Object parse;
    try {
      parse = JSONValue.parseWithException(value);
    } catch (ParseException e) {
      throw new ActionException(e);
    }
    return new Template(template).expand(parse);
  }
}
