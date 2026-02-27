package content;

import content.store.KeyValueStore;
import content.store.Schema;

import javax.sql.DataSource;

/**
 * Static service locator for application-scoped singletons.
 * <p>
 * Initialized once at startup in {@link App#main}, before the Servlet
 * container instantiates the {@link ContentRoutes} class via reflection.
 */
public final class Services {

    private static volatile ContentManager manager;

    private Services() {}

    /** Wire up all services from the given DataSource. */
    public static void init(DataSource dataSource) {
        var elements  = new KeyValueStore(dataSource, "ELEMENT");
        var templates = new KeyValueStore(dataSource, "TEMPLATE");
        var views     = new KeyValueStore(dataSource, "VIEW");
        var schema    = new Schema(dataSource);
        manager = new ContentManager(elements, templates, views, schema);
    }

    public static ContentManager manager() {
        if (manager == null) throw new IllegalStateException("Services not initialized");
        return manager;
    }
}
