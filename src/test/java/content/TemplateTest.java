package content;

import jsontemplate.Template;
import org.json.simple.JSONValue;

import java.util.HashMap;

public class TemplateTest {
  public static void main(String [] args) {
    Template t;
    Object parse = JSONValue.parse("{\"value\":[{\"e\":1},{\"e\":2},{\"e\":3}]}");
    t = new Template("Hello {.repeated section value}{e}{.space}{.end}");
    String expand = t.expand(parse);
    System.out.println(expand);
  }
}
