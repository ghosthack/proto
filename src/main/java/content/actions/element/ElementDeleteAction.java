package content.actions.element;

import content.actions.BaseAction;
import content.handlers.element.ElementDeleteHandler;
import org.apache.log4j.Logger;

public class ElementDeleteAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Delete element action");
    if (id == null || id.isEmpty()) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().deleteElement(id, new ElementDeleteHandler() {
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

  private static final Logger log = Logger.getLogger(ElementDeleteAction.class);

}
