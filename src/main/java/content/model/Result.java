package content.model;

/**
 * Algebraic result type for store operations.
 * Replaces the old callback-handler pattern with exhaustive pattern matching.
 */
public sealed interface Result<T> {

    record Ok<T>(T value) implements Result<T> {}
    record NotFound<T>() implements Result<T> {}
    record AlreadyExists<T>() implements Result<T> {}
    record Error<T>(String message, Throwable cause) implements Result<T> {
        public Error(String message) {
            this(message, null);
        }
    }

    static <T> Result<T> ok(T value) {
        return new Ok<>(value);
    }

    static <T> Result<T> notFound() {
        return new NotFound<>();
    }

    static <T> Result<T> alreadyExists() {
        return new AlreadyExists<>();
    }

    static <T> Result<T> error(String message) {
        return new Error<>(message);
    }

    static <T> Result<T> error(String message, Throwable cause) {
        return new Error<>(message, cause);
    }
}
