package content;

import content.datastore.InitDataStore;
import content.datastore.element.ElementDataStore;
import content.datastore.template.TemplateDataStore;
import content.datastore.template.TemplateUpdate;
import content.datastore.view.ViewDataStore;
import content.datastore.view.ViewUpdate;
import content.handlers.ResultHandler;
import content.handlers.ResultHandlerEx;
import content.handlers.element.*;
import content.handlers.template.*;
import content.handlers.view.*;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class ContentManager {

  public void streamCreateElement(final String id, final InputStream stream, final ElementCreateHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        elementDataStore.create(id, stream, handler);
      }
    });
  }

  public void createElement(final String id, final ElementCreate element, final ElementCreateHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        elementDataStore.create(id, element, handler);
      }
    });
  }

  public void streamUpdateElement(final String id, final InputStream stream, final ElementModifyHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        elementDataStore.update(id, stream, handler);
      }
    });
  }

  public void updateElement(final String id, final ElementModify element, final ElementModifyHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        elementDataStore.update(id, element, handler);
      }
    });
  }

  public void readElement(final String id, final ResultHandler<ElementRead> handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        elementDataStore.read(id, handler);
      }
    });
  }

  public void readElement(final String id, final ResultHandlerEx<BufferedInputStream> handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        elementDataStore.read(id, handler);
      }
    });
  }

  public void deleteElement(final String id, final ElementDeleteHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        elementDataStore.delete(id, handler);
      }
    });
  }

  public void createTemplate(final String id, final TemplateCreate element, final TemplateCreateHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        templateDataStore.create(id, element, handler);
      }
    });
  }

  public void updateTemplate(final String id, final TemplateUpdate element, final TemplateModifyHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        templateDataStore.update(id, element, handler);
      }
    });
  }

  public void readTemplate(final String id, final ResultHandler<TemplateRead> resultTemplateHandler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        templateDataStore.read(id, resultTemplateHandler);
      }
    });
  }

  public void deleteTemplate(final String id, final TemplateDeleteHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        templateDataStore.delete(id, handler);
      }
    });
  }

  public void createView(final String id, final ViewCreate element, final ViewCreateHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        viewDataStore.create(id, element, handler);
      }
    });
  }

  public void updateView(final String id, final ViewUpdate element, final ViewModifyHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        viewDataStore.update(id, element, handler);
      }
    });
  }

  public void readView(final String id, final ResultHandler<ViewRead> handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        viewDataStore.read(id, handler);
      }
    });
  }

  public void deleteView(final String id, final ViewDeleteHandler handler) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        viewDataStore.delete(id, handler);
      }
    });
  }

  public void init() {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        initDataStore.element();
        initDataStore.template();
        initDataStore.view();
      }
    });
  }

  public ContentManager(DataSource dataSource) {
    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
    transactionTemplate = new TransactionTemplate(dataSourceTransactionManager);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    elementDataStore = new ElementDataStore(jdbcTemplate);
    templateDataStore = new TemplateDataStore(jdbcTemplate);
    viewDataStore = new ViewDataStore(jdbcTemplate);
    initDataStore = new InitDataStore(jdbcTemplate);
  }

  private ElementDataStore elementDataStore;

  private TemplateDataStore templateDataStore;

  private ViewDataStore viewDataStore;

  private final InitDataStore initDataStore;

  private TransactionTemplate transactionTemplate;

  private static final Logger log = Logger.getLogger(ContentManager.class);

}
