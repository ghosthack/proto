package content.actions;

import content.ContentAction;

public abstract class BaseAction extends ContentAction {

  public static final String ID = "id";

  @Override
  public void run() {
    String id = params(ID);
    run(id);
  }

  public abstract void run(String id);
}
