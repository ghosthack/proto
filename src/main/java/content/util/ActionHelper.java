package content.util;

import content.ContentAction;
import content.ContentApi;
import turismo.action.ActionException;

public class ActionHelper {
  public static <T extends ContentAction> T createAction(Class<T> actionClass, ContentApi api) {
    try {
      T t = actionClass.newInstance();
      t.setApi(api);
      return t;
    } catch (InstantiationException e) {
      throw new ActionException(e);
    } catch (IllegalAccessException e) {
      throw new ActionException(e);
    }
  }
}
