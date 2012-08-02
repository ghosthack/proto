package content.actions.render;

import content.ContentAction;
import content.ContentManager;
import content.handlers.ResultHandler;
import content.handlers.element.ElementRead;
import content.handlers.template.TemplateRead;
import content.util.CharsetConstant;
import content.util.Preconditions;
import content.util.ResponseHelper;
import content.util.TemplateHelper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RenderComponent {

  public static final String ACCEPT = "Accept";
  public static final String TEXT_HTML = "text/html";

  public void render(final HttpServletRequest req, final HttpServletResponse res, String elementId, final String templateId) {
    if (Preconditions.isNullOrEmpty(elementId)) {
      log.debug("Id is not valid, 'Bad Request'");
      res.setStatus(400);
    } else if (Preconditions.isNullOrEmpty(templateId)) {
      log.debug("Template is not valid, 'Bad Request'");
      res.setStatus(400);
    } else {
      getManager().readElement(elementId, new ResultHandler<ElementRead>() {
        @Override
        public void success(final ElementRead element) {
          log.debug("Element found: " + element.getValue());
          getManager().readTemplate(templateId, new ResultHandler<TemplateRead>() {
            @Override
            public void success(TemplateRead template) {
              log.debug("Template found: " + template.getValue());
              res.setStatus(200);
              res.setCharacterEncoding(CharsetConstant.UTF_8);
              String accept = req.getHeader(ACCEPT);
              log.debug("Accept: " + accept);
              if (!Preconditions.isNullOrEmpty(accept)) {
                if (accept.contains(TEXT_HTML)) {
                  res.setContentType(TEXT_HTML);
                } else {
                  res.setContentType(accept);
                }
              }
              String render = TemplateHelper.applyTemplate(template.getValue(), element.getValue());
              log.debug("Render: " + render);
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

  private ContentManager getManager() {
    return action.getManager();
  }

  private ContentAction action;

  private static final Logger log = Logger.getLogger(RenderComponent.class);

}
