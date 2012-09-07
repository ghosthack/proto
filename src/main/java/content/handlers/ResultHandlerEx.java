package content.handlers;

public interface ResultHandlerEx<T> {
  void success(T element);

  void exception(Exception e);

  void notFound();
}
