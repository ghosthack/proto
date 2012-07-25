package content.actions;

import content.ContentAction;

public abstract class View1Action extends ContentAction {

  public static final String ELEMENT = "element";

  public static final String TEMPLATE = "template";

  @Override
  public void run() {
    String element = params("element");
    String template = params("template");
    run(element, template);
  }

  public abstract void run(String element, String template);

}
