package content.action;

import io.github.ghosthack.turismo.action.Action;

/**
 * Factory for template CRUD actions.
 */
public final class TemplateActions {

    private TemplateActions() {}

    public static Action create() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                var value = body();
                if (isBlank(id) || isBlank(value)) { badRequest(); return; }
                handle(manager().createTemplate(id, value), 201);
            }
        };
    }

    public static Action read() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                if (isBlank(id)) { badRequest(); return; }
                handle(manager().readTemplate(id), 200);
            }
        };
    }

    public static Action update() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                var value = body();
                if (isBlank(id) || isBlank(value)) { badRequest(); return; }
                handle(manager().updateTemplate(id, value), 200);
            }
        };
    }

    public static Action delete() {
        return new ContentAction() {
            @Override public void run() {
                var id = params("id");
                if (isBlank(id)) { badRequest(); return; }
                handle(manager().deleteTemplate(id), 200);
            }
        };
    }
}
