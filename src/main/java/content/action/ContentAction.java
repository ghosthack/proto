package content.action;

import content.ContentManager;
import content.Services;
import content.model.Result;
import content.util.IO;
import io.github.ghosthack.turismo.action.Action;
import io.github.ghosthack.turismo.action.ActionException;

import java.io.IOException;

/**
 * Base action providing access to the {@link ContentManager} and
 * common HTTP response helpers.
 * <p>
 * Reduces every action to its essential logic by factoring out
 * body reading, status codes, and result-to-response mapping.
 */
public abstract class ContentAction extends Action {

    protected ContentManager manager() {
        return Services.manager();
    }

    // -- Request helpers -------------------------------------------------------

    /** Read the full request body as a UTF-8 string. */
    protected String body() {
        try {
            return IO.readBody(req());
        } catch (IOException e) {
            throw new ActionException(e);
        }
    }

    protected boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    // -- Response helpers ------------------------------------------------------

    /** Override turismo's print to always append a newline. */
    @Override
    protected void print(String s) {
        super.print(s + "\n");
    }

    protected void respond(int status, String contentType, String body) {
        res().setStatus(status);
        res().setContentType(contentType);
        print(body);
    }

    protected void json(int status, String body) {
        respond(status, "application/json; charset=UTF-8", body);
    }

    protected void text(int status, String body) {
        respond(status, "text/plain; charset=UTF-8", body);
    }

    protected void ok(String body)      { json(200, body); }
    protected void created(String body) { json(201, body); }
    protected void badRequest()         { res().setStatus(400); }
    protected void notFound()           { res().setStatus(404); }
    protected void conflict()           { res().setStatus(409); }
    protected void error()              { res().setStatus(500); }

    // -- Result mapping --------------------------------------------------------

    /**
     * Map a {@link Result} to the appropriate HTTP response.
     * Uses pattern matching over the sealed hierarchy.
     */
    protected void handle(Result<?> result, int successStatus) {
        switch (result) {
            case Result.Ok<?> ok -> {
                var v = ok.value();
                json(successStatus, v != null ? v.toString() : "");
            }
            case Result.NotFound<?> nf    -> notFound();
            case Result.AlreadyExists<?> ae -> conflict();
            case Result.Error<?> err      -> error();
        }
    }
}
