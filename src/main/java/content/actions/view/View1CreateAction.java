package content.actions.view;

import content.actions.View1Action;
import content.handlers.view.ViewCreate;
import content.handlers.view.ViewCreateHandler;
import content.util.Preconditions;
import org.apache.log4j.Logger;

public class View1CreateAction extends View1Action {

  @Override
  public void run(String id, String value) {
    log.debug("Create 1 view action");
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else if (Preconditions.isNullOrEmpty(value)) {
      log.debug("Value is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      ViewCreate element = new ViewCreate();
      element.setValue(value);
      getManager().createView(id, element, new ViewCreateHandler() {
        @Override
        public void success() {
          res().setStatus(201);
        }

        @Override
        public void alreadyExists() {
          log.debug("Id exists, 'Conflict'");
          res().setStatus(409);
        }

        @Override
        public void unableToUpdate() {
          log.error("No update performed");
          res().setStatus(500);
        }
      });
    }
  }

  private static final Logger log = Logger.getLogger(View1CreateAction.class);

}
