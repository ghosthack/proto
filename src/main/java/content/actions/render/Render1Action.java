package content.actions.render;

import content.actions.BaseAction;
import content.handlers.ResultHandler;
import content.handlers.view.ViewRead;
import content.util.Preconditions;
import org.apache.log4j.Logger;

public class Render1Action extends BaseAction {

  protected RenderComponent renderComponent = new RenderComponent(this);

  @Override
  public void run(final String id) {
    log.debug("Render 1 element action");
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().readView(id, new ResultHandler<ViewRead>() {
        @Override
        public void success(ViewRead view) {
          renderComponent.render(res(), id, view.getValue());
        }

        @Override
        public void notFound() {
          log.debug("View not found");
          res().setStatus(404);
        }
      });
    }
  }

  private static final Logger log = Logger.getLogger(Render1Action.class);

}
