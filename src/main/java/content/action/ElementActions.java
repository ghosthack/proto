package content.action;

import content.model.Result;
import io.github.ghosthack.turismo.action.Action;
import io.github.ghosthack.turismo.action.ActionException;
import io.github.ghosthack.turismo.multipart.MultipartRequest;

import java.nio.charset.StandardCharsets;

/**
 * Factory for element CRUD and stream actions.
 * Each method returns a fresh {@link Action} instance for route registration.
 */
public final class ElementActions {

    private ElementActions() {}

    public static Action create() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                var value = body();
                if (isBlank(id) || isBlank(value)) { badRequest(); return; }
                handle(manager().createElement(id, value), 201);
            }
        };
    }

    public static Action read() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                if (isBlank(id)) { badRequest(); return; }
                handle(manager().readElement(id), 200);
            }
        };
    }

    public static Action update() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                var value = body();
                if (isBlank(id) || isBlank(value)) { badRequest(); return; }
                handle(manager().updateElement(id, value), 200);
            }
        };
    }

    public static Action delete() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                if (isBlank(id)) { badRequest(); return; }
                handle(manager().deleteElement(id), 200);
            }
        };
    }

    // -- Stream (multipart) endpoints ------------------------------------------

    public static Action streamCreate() {
        return new ContentAction() {
            @Override public void run() {
                try {
                    var id = params("id");
                    if (isBlank(id)) { badRequest(); return; }
                    var multipart = MultipartRequest.wrapAndParse(req());
                    var bytes = (byte[]) multipart.getAttribute(id);
                    if (bytes == null) { badRequest(); return; }
                    var value = new String(bytes, StandardCharsets.UTF_8);
                    handle(manager().createElement(id, value), 201);
                } catch (Exception e) {
                    throw new ActionException(e);
                }
            }
        };
    }

    public static Action streamRead() {
        return new ContentAction() {
            @Override public void run() {
                try {
                    var id = params("id");
                    if (isBlank(id)) { badRequest(); return; }
                    var result = manager().readElement(id);
                    switch (result) {
                        case Result.Ok<String> ok -> {
                            var bytes = ok.value().getBytes(StandardCharsets.UTF_8);
                            res().setContentType("application/octet-stream");
                            res().setContentLength(bytes.length);
                            res().getOutputStream().write(bytes);
                        }
                        case Result.NotFound<String> nf -> notFound();
                        case Result.AlreadyExists<String> ae -> error();
                        case Result.Error<String> err -> error();
                    }
                } catch (Exception e) {
                    throw new ActionException(e);
                }
            }
        };
    }

    public static Action streamUpdate() {
        return new ContentAction() {
            @Override public void run() {
                try {
                    var id = params("id");
                    if (isBlank(id)) { badRequest(); return; }
                    var multipart = MultipartRequest.wrapAndParse(req());
                    var bytes = (byte[]) multipart.getAttribute(id);
                    if (bytes == null) { badRequest(); return; }
                    var value = new String(bytes, StandardCharsets.UTF_8);
                    handle(manager().updateElement(id, value), 200);
                } catch (Exception e) {
                    throw new ActionException(e);
                }
            }
        };
    }
}
