package content.actions;

import content.ContentAction;

/**
 * Created with IntelliJ IDEA.
 * User: adrian
 * Date: 7/25/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class InitAction extends ContentAction {
  @Override
  public void run() {
    getManager().init();
  }
}
