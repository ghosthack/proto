package content.actions.element;

import content.actions.BaseAction;
import content.handlers.ResultHandler;
import content.handlers.element.ElementRead;
import content.util.Preconditions;
import content.util.ResponseHelper;
import org.apache.log4j.Logger;

public class ElementReadAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Read element action");
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().readElement(id, new ResultHandler<ElementRead>() {
        @Override
        public void success(ElementRead element) {
          res().setStatus(200);
          ResponseHelper.write(res(), element.getValue());
        }

        @Override
        public void notFound() {
          log.debug("Element not found");
          res().setStatus(404);
        }
      });
    }
  }

  private static final Logger log = Logger.getLogger(ElementReadAction.class);

}
