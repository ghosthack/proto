package content.actions.view;

import content.actions.View1Action;
import content.datastore.view.ViewUpdate;
import content.handlers.view.ViewModifyHandler;
import content.util.Preconditions;
import org.apache.log4j.Logger;

public class View1UpdateAction extends View1Action {

  @Override
  public void run(String id, String value) {
    log.debug("Modify 1 view action");
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else if (Preconditions.isNullOrEmpty(value)) {
      log.debug("Value is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      ViewUpdate element = new ViewUpdate();
      element.setValue(value);
      getManager().updateView(id, element, new ViewModifyHandler() {
        @Override
        public void success() {
          res().setStatus(200);
        }

        @Override
        public void unableToUpdate() {
          log.error("No update performed");
          res().setStatus(500);
        }

        @Override
        public void notFound() {
          log.debug("Element not found");
          res().setStatus(404);
        }
      });
    }
  }

  private static final Logger log = Logger.getLogger(View1UpdateAction.class);

}
