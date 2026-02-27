package content.template;

import java.util.List;
import java.util.Map;

/**
 * A compiled template that can be expanded against data.
 * <p>
 * Inspired by Google's json-template, reimplemented with modern Java:
 * sealed interfaces, records, and pattern matching.
 *
 * <pre>{@code
 * var tmpl = Template.compile("Hello, {{name}}!");
 * String out = tmpl.expand(Map.of("name", "World"));
 * // "Hello, World!"
 * }</pre>
 */
public final class Template {

    private final List<Node> nodes;
    private final Renderer renderer;

    private Template(List<Node> nodes, Renderer renderer) {
        this.nodes = nodes;
        this.renderer = renderer;
    }

    /**
     * Compile a template using the default double-curly meta characters
     * and pipe format character.
     */
    public static Template compile(String template) {
        return compile(template, "{{", "}}", '|');
    }

    /**
     * Compile a template with custom meta characters and format character.
     */
    public static Template compile(String template, String metaLeft, String metaRight, char formatChar) {
        var compiler = new Compiler(metaLeft, metaRight, formatChar);
        var nodes = compiler.compile(template);
        var renderer = new Renderer(Formatter.defaults(), "str");
        return new Template(nodes, renderer);
    }

    /**
     * Compile with additional custom formatters merged with the defaults.
     */
    public static Template compile(String template, Map<String, Formatter> extraFormatters) {
        var compiler = new Compiler("{{", "}}", '|');
        var nodes = compiler.compile(template);
        var fmts = Formatter.defaults();
        fmts.putAll(extraFormatters);
        var renderer = new Renderer(fmts, "str");
        return new Template(nodes, renderer);
    }

    /** Expand this template against the given data object (typically a Map). */
    public String expand(Object data) {
        return renderer.render(nodes, data);
    }
}
