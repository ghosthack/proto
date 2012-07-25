package content.actions.element;

import content.actions.BaseAction;
import content.handlers.element.ElementCreate;
import content.handlers.element.ElementCreateHandler;
import content.util.RequestHelper;
import content.util.Preconditions;
import org.apache.log4j.Logger;

public class ElementCreateAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Create element action");
    ElementCreate element = new ElementCreate();
    element.setValue(RequestHelper.readJson(req()).toString());
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else if (Preconditions.isNullOrEmpty(element.getValue())) {
      log.debug("Value is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().createElement(id, element, new ElementCreateHandler() {
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

  private static final Logger log = Logger.getLogger(ElementCreateAction.class);

}
