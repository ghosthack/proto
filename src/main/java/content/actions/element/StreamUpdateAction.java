package content.actions.element;

import content.actions.BaseAction;
import content.handlers.element.ElementModify;
import content.handlers.element.ElementModifyHandler;
import content.util.Preconditions;
import content.util.RequestHelper;
import org.apache.log4j.Logger;
import turismo.action.ActionException;

import java.io.IOException;
import java.io.InputStream;

public class StreamUpdateAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Modify element action");
    InputStream stream = null;
    try {
      stream = RequestHelper.readMultipartStream(req());
      if (Preconditions.isNullOrEmpty(id)) {
        log.debug("Id is not valid, 'Bad Request'");
        res().setStatus(400);
      } else {
        getManager().streamUpdateElement(id, stream, new ElementModifyHandler() {
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

    } catch (IOException e) {
      throw new ActionException(e);
    } finally {
      if(stream != null) {
        try {
          stream.close();
        } catch (IOException e) {
          throw new ActionException(e);
        }
      }
    }
    ElementModify element = new ElementModify();
    element.setValue(RequestHelper.readString(req()));
  }

  private static final Logger log = Logger.getLogger(StreamUpdateAction.class);

}
