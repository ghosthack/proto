package content.actions.element;

import content.actions.BaseAction;
import content.handlers.element.ElementModify;
import content.handlers.element.ElementModifyHandler;
import content.util.CharsetConstant;
import content.util.Dumper;
import content.util.RequestHelper;
import content.util.Preconditions;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;

public class ElementUpdateAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Modify element action");
    ElementModify element = new ElementModify();
    element.setValue(RequestHelper.readString(req()));
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else if (Preconditions.isNullOrEmpty(element.getValue())) {
      log.debug("Value is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().updateElement(id, element, new ElementModifyHandler() {
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

  private static final Logger log = Logger.getLogger(ElementUpdateAction.class);

}
