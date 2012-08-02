package content.actions.template;

import content.actions.BaseAction;
import content.handlers.template.TemplateCreate;
import content.handlers.template.TemplateCreateHandler;
import content.util.Preconditions;
import content.util.RequestHelper;
import org.apache.log4j.Logger;

public class TemplateCreateAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Create template action");
    TemplateCreate element = new TemplateCreate();
    element.setValue(RequestHelper.readString(req()));
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else if (Preconditions.isNullOrEmpty(element.getValue())) {
      log.debug("Value is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().createTemplate(id, element, new TemplateCreateHandler() {
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

  private static final Logger log = Logger.getLogger(TemplateCreateAction.class);

}
