package content;

import content.action.*;
import io.github.ghosthack.turismo.routes.RoutesList;

/**
 * Route table for the Content API.
 * <p>
 * Instantiated reflectively by the turismo {@code Servlet} during init.
 * All services must be wired in {@link Services} before this class is loaded.
 */
public class ContentRoutes extends RoutesList {

    @Override
    protected void map() {
        var mgr = Services.manager();

        // -- Health / Admin ----------------------------------------------------

        get("/check", new ContentAction() {
            @Override public void run() { print("OK"); }
        });

        get("/init", new ContentAction() {
            @Override public void run() {
                manager().init();
                ok("initialized");
            }
        });

        // -- Render ------------------------------------------------------------

        get("/render/:id",                  RenderActions.byView());
        get("/render/:element/:template",   RenderActions.byPair());

        // -- Element CRUD ------------------------------------------------------

        post  ("/element/:id",          ElementActions.create());
        get   ("/element/:id",          ElementActions.read());
        put   ("/element/:id",          ElementActions.update());
        delete("/element/:id",          ElementActions.delete());

        // -- Element stream (multipart) ----------------------------------------

        post  ("/element/:id/stream",   ElementActions.streamCreate());
        get   ("/element/:id/stream",   ElementActions.streamRead());
        put   ("/element/:id/stream",   ElementActions.streamUpdate());

        // -- Template CRUD -----------------------------------------------------

        post  ("/template/:id",         TemplateActions.create());
        get   ("/template/:id",         TemplateActions.read());
        put   ("/template/:id",         TemplateActions.update());
        delete("/template/:id",         TemplateActions.delete());

        // -- View CRUD ---------------------------------------------------------

        post  ("/view/:element/:template",  ViewActions.createMapping());
        put   ("/view/:element/:template",  ViewActions.updateMapping());
        post  ("/view/:id",                 ViewActions.create());
        get   ("/view/:id",                 ViewActions.read());
        put   ("/view/:id",                 ViewActions.update());
        delete("/view/:id",                 ViewActions.delete());
    }
}
