package content;

import content.template.Template;
import content.template.TemplateException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for the jsontemplate-inspired template engine.
 */
class TemplateEngineTest {

    // -- Simple substitution ---------------------------------------------------

    @Test
    void plainText() {
        assertEquals("hello", Template.compile("hello").expand(Map.of()));
    }

    @Test
    void simpleVariable() {
        var tmpl = Template.compile("Hello, {{name}}!");
        assertEquals("Hello, World!", tmpl.expand(Map.of("name", "World")));
    }

    @Test
    void missingVariable() {
        var tmpl = Template.compile("Hello, {{name}}!");
        assertEquals("Hello, !", tmpl.expand(Map.of()));
    }

    @Test
    void cursorReference() {
        var tmpl = Template.compile("{{@}}");
        assertEquals("test", tmpl.expand("test"));
    }

    // -- Dot notation ----------------------------------------------------------

    @Test
    void nestedLookup() {
        var tmpl = Template.compile("{{user.name}}");
        var data = Map.of("user", Map.of("name", "Alice"));
        assertEquals("Alice", tmpl.expand(data));
    }

    @Test
    void deepNestedLookup() {
        var tmpl = Template.compile("{{a.b.c}}");
        var data = Map.of("a", Map.of("b", Map.of("c", "deep")));
        assertEquals("deep", tmpl.expand(data));
    }

    // -- Formatters ------------------------------------------------------------

    @Test
    void htmlFormatter() {
        var tmpl = Template.compile("{{content|html}}");
        assertEquals("&lt;b&gt;bold&lt;/b&gt;",
                tmpl.expand(Map.of("content", "<b>bold</b>")));
    }

    @Test
    void htmlAttrFormatter() {
        var tmpl = Template.compile("{{val|html-attr-value}}");
        assertEquals("a&quot;b&#39;c",
                tmpl.expand(Map.of("val", "a\"b'c")));
    }

    @Test
    void rawFormatter() {
        var tmpl = Template.compile("{{content|raw}}");
        assertEquals("<b>bold</b>",
                tmpl.expand(Map.of("content", "<b>bold</b>")));
    }

    @Test
    void formatterChain() {
        var tmpl = Template.compile("{{x|str|html}}");
        assertEquals("42", tmpl.expand(Map.of("x", 42)));
    }

    // -- Sections --------------------------------------------------------------

    @Test
    void sectionTruthy() {
        var tmpl = Template.compile("{{.section user}}{{name}}{{.end}}");
        assertEquals("Bob", tmpl.expand(Map.of("user", Map.of("name", "Bob"))));
    }

    @Test
    void sectionFalsy() {
        var tmpl = Template.compile("{{.section user}}{{name}}{{.or}}anonymous{{.end}}");
        assertEquals("anonymous", tmpl.expand(Map.of()));
    }

    @Test
    void sectionEmptyString() {
        var tmpl = Template.compile("{{.section name}}present{{.or}}absent{{.end}}");
        assertEquals("absent", tmpl.expand(Map.of("name", "")));
    }

    @Test
    void nestedSections() {
        var tmpl = Template.compile(
                "{{.section a}}{{.section b}}{{val}}{{.end}}{{.end}}");
        var data = Map.of("a", Map.of("b", Map.of("val", "OK")));
        assertEquals("OK", tmpl.expand(data));
    }

    // -- Repeated sections -----------------------------------------------------

    @Test
    void repeatedSection() {
        var tmpl = Template.compile(
                "{{.repeated section items}}{{@}}{{.end}}");
        assertEquals("abc", tmpl.expand(Map.of("items", List.of("a", "b", "c"))));
    }

    @Test
    void repeatedSectionWithSeparator() {
        var tmpl = Template.compile(
                "{{.repeated section items}}{{@}}{{.alternates with}}, {{.end}}");
        assertEquals("a, b, c", tmpl.expand(Map.of("items", List.of("a", "b", "c"))));
    }

    @Test
    void repeatedSectionOrClause() {
        var tmpl = Template.compile(
                "{{.repeated section items}}{{@}}{{.or}}empty{{.end}}");
        assertEquals("empty", tmpl.expand(Map.of("items", List.of())));
    }

    @Test
    void repeatedSectionMissing() {
        var tmpl = Template.compile(
                "{{.repeated section items}}{{@}}{{.or}}empty{{.end}}");
        assertEquals("empty", tmpl.expand(Map.of()));
    }

    @Test
    void repeatedSectionWithObjects() {
        var tmpl = Template.compile(
                "{{.repeated section people}}{{name}}{{.alternates with}} & {{.end}}");
        var data = Map.of("people", List.of(
                Map.of("name", "Alice"),
                Map.of("name", "Bob")));
        assertEquals("Alice & Bob", tmpl.expand(data));
    }

    // -- Keywords --------------------------------------------------------------

    @Test
    void metaLeftRight() {
        var tmpl = Template.compile("{{.meta-left}}x{{.meta-right}}");
        assertEquals("{{x}}", tmpl.expand(Map.of()));
    }

    @Test
    void spaceTabNewline() {
        var tmpl = Template.compile("a{{.space}}b{{.tab}}c{{.newline}}d");
        assertEquals("a b\tc\nd", tmpl.expand(Map.of()));
    }

    // -- Comments --------------------------------------------------------------

    @Test
    void commentIgnored() {
        var tmpl = Template.compile("before{{# this is a comment}}after");
        assertEquals("beforeafter", tmpl.expand(Map.of()));
    }

    // -- Error handling --------------------------------------------------------

    @Test
    void unclosedMeta() {
        assertThrows(TemplateException.class, () -> Template.compile("{{open"));
    }

    @Test
    void unclosedSection() {
        assertThrows(TemplateException.class,
                () -> Template.compile("{{.section x}}body"));
    }

    @Test
    void endWithoutSection() {
        assertThrows(TemplateException.class, () -> Template.compile("{{.end}}"));
    }

    // -- Integration: matches original template.txt ----------------------------

    @Test
    void demoTemplateExpansion() {
        // Mirrors the project's template.txt / element.txt demo
        var tmpl = Template.compile("{{value}} {{tilde}}");
        var data = Map.of("value", "demo", "tilde", "a");
        assertEquals("demo a", tmpl.expand(data));
    }
}
