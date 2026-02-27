package content;

import content.model.Result;
import content.store.KeyValueStore;
import content.store.Schema;
import content.template.Template;
import content.util.Json;

/**
 * Service layer that orchestrates store operations and template rendering.
 */
public final class ContentManager {

    private final KeyValueStore elements;
    private final KeyValueStore templates;
    private final KeyValueStore views;
    private final Schema schema;

    public ContentManager(KeyValueStore elements, KeyValueStore templates,
                          KeyValueStore views, Schema schema) {
        this.elements = elements;
        this.templates = templates;
        this.views = views;
        this.schema = schema;
    }

    // -- Schema ----------------------------------------------------------------

    public void init() {
        schema.initialize();
    }

    // -- Element CRUD ----------------------------------------------------------

    public Result<String> createElement(String key, String value) {
        return elements.create(key, value);
    }

    public Result<String> readElement(String key) {
        return elements.read(key);
    }

    public Result<String> updateElement(String key, String value) {
        return elements.update(key, value);
    }

    public Result<Void> deleteElement(String key) {
        return elements.delete(key);
    }

    // -- Template CRUD ---------------------------------------------------------

    public Result<String> createTemplate(String key, String value) {
        return templates.create(key, value);
    }

    public Result<String> readTemplate(String key) {
        return templates.read(key);
    }

    public Result<String> updateTemplate(String key, String value) {
        return templates.update(key, value);
    }

    public Result<Void> deleteTemplate(String key) {
        return templates.delete(key);
    }

    // -- View CRUD -------------------------------------------------------------

    public Result<String> createView(String key, String value) {
        return views.create(key, value);
    }

    public Result<String> readView(String key) {
        return views.read(key);
    }

    public Result<String> updateView(String key, String value) {
        return views.update(key, value);
    }

    public Result<Void> deleteView(String key) {
        return views.delete(key);
    }

    // -- Rendering -------------------------------------------------------------

    /**
     * Render an element using the template specified by its view mapping.
     */
    public Result<String> renderByView(String elementId) {
        var viewResult = views.read(elementId);
        if (viewResult instanceof Result.Ok<String> view) {
            return renderByPair(elementId, view.value());
        }
        return castResult(viewResult);
    }

    /**
     * Render an element with an explicit template.
     */
    public Result<String> renderByPair(String elementId, String templateId) {
        var elemResult = elements.read(elementId);
        if (!(elemResult instanceof Result.Ok<String> elem)) {
            return castResult(elemResult);
        }

        var tmplResult = templates.read(templateId);
        if (!(tmplResult instanceof Result.Ok<String> tmpl)) {
            return castResult(tmplResult);
        }

        var data = Json.parse(elem.value());
        var rendered = Template.compile(tmpl.value()).expand(data);
        return Result.ok(rendered);
    }

    /** Type-safe cast for non-Ok results (NotFound, AlreadyExists, Error). */
    @SuppressWarnings("unchecked")
    private static <T, U> Result<U> castResult(Result<T> result) {
        return switch (result) {
            case Result.NotFound<?> nf -> Result.notFound();
            case Result.AlreadyExists<?> ae -> Result.alreadyExists();
            case Result.Error<?> err -> Result.error(err.message(), err.cause());
            case Result.Ok<?> ok -> throw new IllegalStateException("Cannot cast Ok result");
        };
    }
}
