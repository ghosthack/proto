package content.template;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Compiles a template string into a list of {@link Node} AST nodes.
 * <p>
 * Supports configurable meta characters (default {@code {{ }}}), sections,
 * repeated sections, alternates-with separators, or-clauses, comments,
 * and built-in keywords (meta-left, meta-right, space, tab, newline).
 */
public final class Compiler {

    private final String metaLeft;
    private final String metaRight;
    private final char formatChar;

    public Compiler(String metaLeft, String metaRight, char formatChar) {
        this.metaLeft = metaLeft;
        this.metaRight = metaRight;
        this.formatChar = formatChar;
    }

    public List<Node> compile(String template) {
        var tokens = tokenize(template);
        return buildAst(tokens);
    }

    // -- Tokenization ----------------------------------------------------------

    private enum TokenType { TEXT, DIRECTIVE }

    private record Token(TokenType type, String value) {}

    private List<Token> tokenize(String template) {
        var tokens = new ArrayList<Token>();
        int pos = 0;

        while (pos < template.length()) {
            int open = template.indexOf(metaLeft, pos);
            if (open == -1) {
                tokens.add(new Token(TokenType.TEXT, template.substring(pos)));
                break;
            }
            if (open > pos) {
                tokens.add(new Token(TokenType.TEXT, template.substring(pos, open)));
            }
            int close = template.indexOf(metaRight, open + metaLeft.length());
            if (close == -1) {
                throw new TemplateException("Unclosed " + metaLeft + " at position " + open);
            }
            var directive = template.substring(open + metaLeft.length(), close).trim();
            tokens.add(new Token(TokenType.DIRECTIVE, directive));
            pos = close + metaRight.length();
        }

        return tokens;
    }

    // -- AST Construction ------------------------------------------------------

    /**
     * Tracks state while building a section / repeated-section node.
     *
     * @param name           the variable name bound by this section
     * @param repeated       true for {@code .repeated section}
     * @param body           nodes inside the main body
     * @param orClause       nodes inside the {@code .or} branch (may be null)
     * @param separator      nodes inside the {@code .alternates with} branch (may be null)
     * @param previousTarget the node list that was active before this section opened
     */
    private static final class Frame {
        final String name;
        final boolean repeated;
        final List<Node> body = new ArrayList<>();
        List<Node> orClause;
        List<Node> separator;
        final List<Node> previousTarget;

        Frame(String name, boolean repeated, List<Node> previousTarget) {
            this.name = name;
            this.repeated = repeated;
            this.previousTarget = previousTarget;
        }

        Node toNode() {
            if (repeated) {
                return new Node.RepeatedSection(
                        name,
                        List.copyOf(body),
                        separator != null ? List.copyOf(separator) : List.of(),
                        orClause != null ? List.copyOf(orClause) : List.of());
            }
            return new Node.Section(
                    name,
                    List.copyOf(body),
                    orClause != null ? List.copyOf(orClause) : List.of());
        }
    }

    private List<Node> buildAst(List<Token> tokens) {
        List<Node> root = new ArrayList<>();
        var stack = new ArrayDeque<Frame>();
        List<Node> current = root;

        for (var token : tokens) {
            if (token.type() == TokenType.TEXT) {
                if (!token.value().isEmpty()) {
                    current.add(new Node.Literal(token.value()));
                }
                continue;
            }

            // DIRECTIVE
            var d = token.value();

            if (d.startsWith("#")) {
                // Comment -- discard
                continue;
            }

            if (d.startsWith(".section ")) {
                var name = d.substring(".section ".length()).trim();
                var frame = new Frame(name, false, current);
                stack.push(frame);
                current = frame.body;

            } else if (d.startsWith(".repeated section ")) {
                var name = d.substring(".repeated section ".length()).trim();
                var frame = new Frame(name, true, current);
                stack.push(frame);
                current = frame.body;

            } else if (d.equals(".or")) {
                if (stack.isEmpty()) throw new TemplateException(".or outside of a section");
                var frame = stack.peek();
                frame.orClause = new ArrayList<>();
                current = frame.orClause;

            } else if (d.equals(".alternates with")) {
                if (stack.isEmpty()) throw new TemplateException(".alternates with outside of a repeated section");
                var frame = stack.peek();
                if (!frame.repeated) throw new TemplateException(".alternates with in a non-repeated section");
                frame.separator = new ArrayList<>();
                current = frame.separator;

            } else if (d.equals(".end")) {
                if (stack.isEmpty()) throw new TemplateException(".end without matching section");
                var frame = stack.pop();
                current = frame.previousTarget;
                current.add(frame.toNode());

            } else {
                // Keyword literals or variable substitution
                current.add(parseSubstitutionOrKeyword(d));
            }
        }

        if (!stack.isEmpty()) {
            throw new TemplateException("Unclosed section: " + stack.peek().name);
        }

        return List.copyOf(root);
    }

    private Node parseSubstitutionOrKeyword(String directive) {
        return switch (directive) {
            case ".meta-left"  -> new Node.Literal(metaLeft);
            case ".meta-right" -> new Node.Literal(metaRight);
            case ".space"      -> new Node.Literal(" ");
            case ".tab"        -> new Node.Literal("\t");
            case ".newline"    -> new Node.Literal("\n");
            default            -> parseSubstitution(directive);
        };
    }

    private Node parseSubstitution(String directive) {
        var parts = directive.split("\\" + formatChar);
        var name = parts[0].trim();
        var formatters = new ArrayList<String>(parts.length - 1);
        for (int i = 1; i < parts.length; i++) {
            var fmt = parts[i].trim();
            if (!fmt.isEmpty()) formatters.add(fmt);
        }
        return new Node.Substitution(name, List.copyOf(formatters));
    }
}
