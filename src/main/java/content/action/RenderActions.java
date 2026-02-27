package content.action;

import content.model.Result;
import io.github.ghosthack.turismo.action.Action;

/**
 * Factory for render actions.
 * Content-Type is negotiated from the Accept header.
 */
public final class RenderActions {

    private RenderActions() {}

    /** GET /render/:id -- render element using its default template via view mapping. */
    public static Action byView() {
        return new RenderAction() {
            @Override public void run() {
                var id = params("id");
                if (isBlank(id)) { badRequest(); return; }
                handleRender(manager().renderByView(id));
            }
        };
    }

    /** GET /render/:element/:template -- render element with an explicit template. */
    public static Action byPair() {
        return new RenderAction() {
            @Override public void run() {
                var element = params("element");
                var template = params("template");
                if (isBlank(element) || isBlank(template)) { badRequest(); return; }
                handleRender(manager().renderByPair(element, template));
            }
        };
    }

    /**
     * Base for render actions with Accept-header content negotiation.
     */
    private static abstract class RenderAction extends ContentAction {

        protected void handleRender(Result<String> result) {
            switch (result) {
                case Result.Ok<String> ok -> {
                    var contentType = negotiateContentType();
                    respond(200, contentType, ok.value());
                }
                case Result.NotFound<String> nf -> notFound();
                case Result.AlreadyExists<String> ae -> error();
                case Result.Error<String> err -> error();
            }
        }

        private String negotiateContentType() {
            var accept = req().getHeader("Accept");
            if (accept != null && accept.contains("text/html")) {
                return "text/html; charset=UTF-8";
            }
            return "text/plain; charset=UTF-8";
        }
    }
}
