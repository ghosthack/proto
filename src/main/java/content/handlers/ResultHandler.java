package content.handlers;

import java.sql.SQLException;

public interface ResultHandler<T> {
  void success(T element);

  void notFound();
}
