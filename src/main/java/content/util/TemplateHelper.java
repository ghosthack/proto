package content.util;

import jsontemplate.Template;
import jsontemplate.TemplateCompileOptions;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import turismo.action.ActionException;

public class TemplateHelper {

  public static final String META = "{{}}";

  public static String applyTemplate(String template, String value) {
    Object parse;
    try {
      parse = JSONValue.parseWithException(value);
    } catch (ParseException e) {
      throw new ActionException(e);
    }
    TemplateCompileOptions options = new TemplateCompileOptions();
    options.setMeta(META);
    return new Template(template, options).expand(parse);
  }
}
