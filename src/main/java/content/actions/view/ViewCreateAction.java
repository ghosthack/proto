package content.actions.view;

import org.apache.log4j.Logger;

import content.actions.BaseAction;
import content.handlers.view.ViewCreate;
import content.handlers.view.ViewCreateHandler;
import content.util.Preconditions;
import content.util.RequestHelper;

public class ViewCreateAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Create view action");
    ViewCreate element = new ViewCreate();
    element.setValue(RequestHelper.read(req()));
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else if (Preconditions.isNullOrEmpty(element.getValue())) {
      log.debug("Value is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().createView(id, element, new ViewCreateHandler() {
        @Override
        public void success() {
          res().setStatus(201);
        }

        @Override
        public void alreadyExists() {
          log.debug("Element id exists, 'Conflict'");
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

  private static final Logger log = Logger.getLogger(ViewCreateAction.class);

}
