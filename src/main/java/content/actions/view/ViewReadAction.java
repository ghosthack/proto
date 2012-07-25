package content.actions.view;

import content.actions.BaseAction;
import content.handlers.ResultHandler;
import content.handlers.view.ViewRead;
import content.util.Preconditions;
import content.util.ResponseHelper;
import org.apache.log4j.Logger;

public class ViewReadAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Read view action");
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().readView(id, new ResultHandler<ViewRead>() {
        @Override
        public void success(ViewRead element) {
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

  private static final Logger log = Logger.getLogger(ViewReadAction.class);

}
