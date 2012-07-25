package content.actions.template;

import content.handlers.template.TemplateModifyHandler;
import org.apache.log4j.Logger;

import content.actions.BaseAction;
import content.datastore.template.TemplateUpdate;
import content.util.Preconditions;
import content.util.RequestHelper;

public class TemplateUpdateAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Modify template action");
    TemplateUpdate element = new TemplateUpdate();
    element.setValue(RequestHelper.read(req()));
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else if (Preconditions.isNullOrEmpty(element.getValue())) {
      log.debug("Value is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().updateTemplate(id, element, new TemplateModifyHandler() {
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

  private static final Logger log = Logger.getLogger(TemplateUpdateAction.class);

}
