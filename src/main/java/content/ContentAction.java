package content;

import turismo.action.Action;

public abstract class ContentAction extends Action {

  protected ContentApi api;

  public ContentManager getManager() {
    return api.getManager();
  }

  public ContentApi getApi() {
    return api;
  }

  public void setApi(ContentApi api) {
    this.api = api;
  }

}
