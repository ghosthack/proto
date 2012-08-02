package content.actions.template;

import content.actions.BaseAction;
import content.handlers.ResultHandler;
import content.handlers.template.TemplateRead;
import content.util.CharsetConstant;
import content.util.Preconditions;
import content.util.ResponseHelper;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;

public class TemplateReadAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Read template action");
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().readTemplate(id, new ResultHandler<TemplateRead>() {
        @Override
        public void success(TemplateRead element) {
          res().setStatus(200);
          res().setCharacterEncoding(CharsetConstant.UTF_8);
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

  private static final Logger log = Logger.getLogger(TemplateReadAction.class);

}
