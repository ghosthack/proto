package content.action;

import io.github.ghosthack.turismo.action.Action;

/**
 * Factory for view CRUD actions.
 * <p>
 * A "view" maps an element ID to its default template ID.
 * Supports both single-param ({@code /view/:id}) and dual-param
 * ({@code /view/:element/:template}) URL patterns.
 */
public final class ViewActions {

    private ViewActions() {}

    public static Action create() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                var value = body();
                if (isBlank(id) || isBlank(value)) { badRequest(); return; }
                handle(manager().createView(id, value), 201);
            }
        };
    }

    public static Action read() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                if (isBlank(id)) { badRequest(); return; }
                handle(manager().readView(id), 200);
            }
        };
    }

    public static Action update() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                var value = body();
                if (isBlank(id) || isBlank(value)) { badRequest(); return; }
                handle(manager().updateView(id, value), 200);
            }
        };
    }

    public static Action delete() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                if (isBlank(id)) { badRequest(); return; }
                handle(manager().deleteView(id), 200);
            }
        };
    }

    /** POST /view/:element/:template -- create mapping from URL path. */
    public static Action createMapping() {
        return new ContentAction() {
            @Override public void run() {
                var element = params("element");
                var template = params("template");
                if (isBlank(element) || isBlank(template)) { badRequest(); return; }
                handle(manager().createView(element, template), 201);
            }
        };
    }

    /** PUT /view/:element/:template -- update mapping from URL path. */
    public static Action updateMapping() {
        return new ContentAction() {
            @Override public void run() {
                var element = params("element");
                var template = params("template");
                if (isBlank(element) || isBlank(template)) { badRequest(); return; }
                handle(manager().updateView(element, template), 200);
            }
        };
    }
}
