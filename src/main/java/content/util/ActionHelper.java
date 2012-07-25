package content.util;

import content.ContentAction;
import content.ContentApi;
import turismo.action.ActionException;

/**
 * Created by IntelliJ IDEA.
 * User: adrian
 * Date: 7/25/12
 * Time: 2:38 AM
 * To change this template use File | Settings | File Templates.
 */
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
