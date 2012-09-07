package content.actions.element;

import content.actions.BaseAction;
import content.handlers.element.ElementCreateHandler;
import content.util.RequestHelper;
import org.apache.log4j.Logger;
import turismo.action.ActionException;

import java.io.IOException;
import java.io.InputStream;

public class StreamCreateAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Create element action (stream)");
    InputStream stream = null;
    try {
      stream = RequestHelper.readMultipartStream(req());
      getManager().streamCreateElement(id, stream, new ElementCreateHandler() {
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
  }

  private static final Logger log = Logger.getLogger(StreamCreateAction.class);

}
