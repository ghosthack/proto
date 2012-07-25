package content.actions.render;

import content.ContentAction;
import content.handlers.ResultHandler;
import content.handlers.element.ElementRead;
import content.handlers.template.TemplateRead;
import content.util.Preconditions;
import content.util.ResponseHelper;
import content.util.TemplateHelper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;

public class RenderComponent {

  public void render(final HttpServletResponse res, String elementId, final String templateId) {
    if (Preconditions.isNullOrEmpty(elementId)) {
      log.debug("Id is not valid, 'Bad Request'");
      res.setStatus(400);
    } else if (Preconditions.isNullOrEmpty(templateId)) {
      log.debug("Template is not valid, 'Bad Request'");
      res.setStatus(400);
    } else {
      action.getManager().readElement(elementId, new ResultHandler<ElementRead>() {
        @Override
        public void success(final ElementRead element) {
          action.getManager().readTemplate(templateId, new ResultHandler<TemplateRead>() {
            @Override
            public void success(TemplateRead template) {
              res.setStatus(200);
              String render = TemplateHelper.applyTemplate(template.getValue(), element.getValue());
              ResponseHelper.write(res, render);
            }

            @Override
            public void notFound() {
              log.debug("Template not found");
              res.setStatus(404);
            }
          });
        }

        @Override
        public void notFound() {
          log.debug("Element not found");
          res.setStatus(404);
        }
      });
    }
  }

  public RenderComponent(ContentAction action) {
    this.action = action;
  }

  private ContentAction action;

  private static final Logger log = Logger.getLogger(RenderComponent.class);

}
