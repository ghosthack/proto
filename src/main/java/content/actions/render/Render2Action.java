package content.actions.render;

import content.actions.View1Action;
import org.apache.log4j.Logger;

public class Render2Action extends View1Action {

  protected RenderComponent renderComponent = new RenderComponent(this);

  @Override
  public void run(String elementId, final String templateId) {
    log.debug("Render 2 element action");
    renderComponent.render(res(), elementId, templateId);
  }

  private static final Logger log = Logger.getLogger(Render2Action.class);

}
