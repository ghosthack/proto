package content.template;

import java.util.HashMap;
import java.util.Map;

/**
 * Template output formatter.
 * Functional interface with built-in defaults for HTML escaping, raw pass-through, etc.
 */
@FunctionalInterface
public interface Formatter {

    Object format(Object value);

    /** Returns the default formatter registry. */
    static Map<String, Formatter> defaults() {
        var map = new HashMap<String, Formatter>();
        map.put("str", v -> v == null ? "" : v.toString());
        map.put("raw", v -> v);
        map.put("html", Formatter::escapeHtml);
        map.put("html-attr-value", Formatter::escapeHtmlAttr);
        map.put("htmltag", Formatter::escapeHtml);
        return map;
    }

    private static Object escapeHtml(Object value) {
        if (value == null) return "";
        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private static Object escapeHtmlAttr(Object value) {
        if (value == null) return "";
        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
