package content.template;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * Stack-based scoped context for template variable resolution.
 * <p>
 * Supports Map-based lookup with dot notation ({@code user.name.first}),
 * the {@code @} cursor reference, and walking up the scope stack when a
 * name is not found in the current scope.
 */
public final class Context {

    private final Deque<Object> stack = new ArrayDeque<>();

    public Context(Object root) {
        stack.push(root);
    }

    /** Return the value at the top of the scope stack. */
    public Object cursor() {
        return stack.peek();
    }

    /** Push a new scope. */
    public void push(Object value) {
        stack.push(value);
    }

    /** Pop and return the top scope. */
    public Object pop() {
        return stack.pop();
    }

    /**
     * Look up a name, walking up the scope stack until found.
     * The special name {@code @} always returns the cursor value.
     */
    public Object lookup(String name) {
        if ("@".equals(name)) return cursor();

        for (var scope : stack) {
            var result = resolve(scope, name);
            if (result != null) return result;
        }
        return null;
    }

    /**
     * Resolve a (possibly dotted) name against a single scope object.
     */
    private static Object resolve(Object scope, String name) {
        if (name.indexOf('.') == -1) {
            return lookupSingle(scope, name);
        }

        var current = scope;
        for (var segment : name.split("\\.")) {
            current = lookupSingle(current, segment);
            if (current == null) return null;
        }
        return current;
    }

    private static Object lookupSingle(Object obj, String key) {
        if (obj instanceof Map<?, ?> map) {
            return map.get(key);
        }
        return null;
    }
}
