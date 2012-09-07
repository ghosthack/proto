package content.actions.element;

import content.actions.BaseAction;
import content.handlers.ResultHandlerEx;
import content.util.Dumper;
import content.util.Preconditions;
import org.apache.log4j.Logger;

import javax.servlet.ServletOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

public class StreamReadAction extends BaseAction {

  @Override
  public void run(String id) {
    log.debug("Read element action");
    if (Preconditions.isNullOrEmpty(id)) {
      log.debug("Id is not valid, 'Bad Request'");
      res().setStatus(400);
    } else {
      getManager().readElement(id, new ResultHandlerEx<BufferedInputStream>() {
        @Override
        public void success(BufferedInputStream is) {
          ServletOutputStream os = null;
          res().setStatus(200);
          try {
            os = res().getOutputStream();
            Dumper.transfer(is, os);

          } catch (IOException e) {
            exception(e);
          } finally {
            if(os != null) {
              try {
                os.close();
              } catch (IOException e) {
                exception(e);
              }
            }
          }
          log.debug("Value: stream");
        }

        @Override
        public void exception(Exception e){
          log.error("Unable to read element", e);
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

  private static final Logger log = Logger.getLogger(StreamReadAction.class);

}
