package content.template;

import java.util.List;

/**
 * AST node types for the template engine.
 * Sealed hierarchy enables exhaustive pattern matching in the renderer.
 */
public sealed interface Node {

    /** Raw text passed through verbatim. */
    record Literal(String text) implements Node {}

    /** Variable lookup with an optional formatter chain. */
    record Substitution(String name, List<String> formatters) implements Node {}

    /** Conditional section: renders body if the named value is truthy, or-clause otherwise. */
    record Section(String name, List<Node> body, List<Node> orClause) implements Node {}

    /** Iteration over a collection, with optional separator and or-clause for empty. */
    record RepeatedSection(
            String name,
            List<Node> body,
            List<Node> separator,
            List<Node> orClause
    ) implements Node {}
}
