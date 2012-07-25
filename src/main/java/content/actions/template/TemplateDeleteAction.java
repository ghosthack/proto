package content.actions.template;

import content.actions.BaseAction;
import content.handlers.element.ElementDeleteHandler;
import content.handlers.template.TemplateDeleteHandler;
import org.apache.log4j.Logger;

public class TemplateDeleteAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Delete template action");
    if (id == null || id.isEmpty()) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().deleteTemplate(id, new TemplateDeleteHandler() {
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

  private static final Logger log = Logger.getLogger(TemplateDeleteAction.class);

}
