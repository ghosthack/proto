package content.template;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Renders a compiled template AST against a data context.
 * Uses exhaustive pattern matching over the sealed {@link Node} hierarchy.
 */
public final class Renderer {

    private final Map<String, Formatter> formatters;
    private final String defaultFormatter;

    public Renderer(Map<String, Formatter> formatters, String defaultFormatter) {
        this.formatters = formatters;
        this.defaultFormatter = defaultFormatter;
    }

    /** Render the node list against the given data and return the result. */
    public String render(List<Node> nodes, Object data) {
        var ctx = new Context(data);
        var out = new StringBuilder();
        renderAll(nodes, ctx, out);
        return out.toString();
    }

    private void renderAll(List<Node> nodes, Context ctx, StringBuilder out) {
        for (var node : nodes) {
            renderNode(node, ctx, out);
        }
    }

    private void renderNode(Node node, Context ctx, StringBuilder out) {
        switch (node) {
            case Node.Literal lit ->
                out.append(lit.text());

            case Node.Substitution sub -> {
                var value = ctx.lookup(sub.name());
                var fmts = sub.formatters().isEmpty()
                        ? List.of(defaultFormatter)
                        : sub.formatters();
                for (var fmtName : fmts) {
                    var formatter = formatters.get(fmtName);
                    if (formatter != null) {
                        value = formatter.format(value);
                    }
                }
                if (value != null) out.append(value);
            }

            case Node.Section sec -> {
                var value = ctx.lookup(sec.name());
                if (isTruthy(value)) {
                    ctx.push(value);
                    renderAll(sec.body(), ctx, out);
                    ctx.pop();
                } else {
                    renderAll(sec.orClause(), ctx, out);
                }
            }

            case Node.RepeatedSection rep -> {
                var value = ctx.lookup(rep.name());
                if (value instanceof Iterable<?> items) {
                    var iter = items.iterator();
                    boolean first = true;
                    boolean any = false;
                    while (iter.hasNext()) {
                        any = true;
                        if (!first) renderAll(rep.separator(), ctx, out);
                        first = false;
                        ctx.push(iter.next());
                        renderAll(rep.body(), ctx, out);
                        ctx.pop();
                    }
                    if (!any) renderAll(rep.orClause(), ctx, out);
                } else {
                    renderAll(rep.orClause(), ctx, out);
                }
            }
        }
    }

    private static boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean b) return b;
        if (value instanceof String s) return !s.isEmpty();
        if (value instanceof Collection<?> c) return !c.isEmpty();
        if (value instanceof Number n) return n.doubleValue() != 0;
        return true;
    }
}
