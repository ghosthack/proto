package content;

import content.actions.InitAction;
import content.util.ActionHelper;
import org.apache.log4j.Logger;
import turismo.action.Action;
import turismo.routes.RoutesList;
import content.actions.render.*;
import content.actions.element.*;
import content.actions.template.*;
import content.actions.view.*;

import java.io.IOException;
import java.util.Enumeration;

public class ContentApi extends RoutesList {

  @Override
  protected void map() {
    get(
      "/render/:id",
      render1Action);
    get(
      "/render/:element/:template",
      render2Action);
    post(
      "/element/:id",
      elementCreateAction);
    get(
      "/element/:id",
      elementReadAction);
    put(
      "/element/:id",
      elementUpdateAction);
    delete(
      "/element/:id",
      elementDeleteAction);
    post(
      "/template/:id",
      templateCreateAction);
    get(
      "/template/:id",
      templateReadAction);
    put(
      "/template/:id",
      templateUpdateAction);
    delete(
      "/template/:id",
      templateDeleteAction);
    post(
      "/view/:element/:template",
      view1CreateAction);
    post(
      "/view/:id",
      viewCreateAction);
    get(
      "/view/:id",
      viewReadAction);
    put(
      "/view/:element/:template",
      view1UpdateAction);
    put(
      "/view/:id",
      viewUpdateAction);
    delete(
      "/view/:id",
      viewDeleteAction);
    get(
      "/init",
      initAction);
    get(
      "/check", new Action() {
      @Override
      public void run() {
        try {
          res().getWriter().print("GOOD");
          res().setStatus(200);
        } catch (IOException e) {
          res().setStatus(500);
        }
      }
    });
    route(new Action() {
      @Override
      public void run() {
        try {
          res().setStatus(404);
          res().getWriter().print("NOT FOUND");
        } catch (IOException e) {
          res().setStatus(500);
        }
      }
    });
    log.info("Mapped content api routes.");
  }

  public ContentApi(ContentManager manager) {
    super();
    this.manager = manager;
  }

  public ContentManager getManager() {
    return manager;
  }

  private final ContentManager manager;

  private final ElementCreateAction elementCreateAction = ActionHelper.createAction(ElementCreateAction.class, this);

  private final ElementReadAction elementReadAction = ActionHelper.createAction(ElementReadAction.class, this);

  private final ElementUpdateAction elementUpdateAction = ActionHelper.createAction(ElementUpdateAction.class, this);

  private final ElementDeleteAction elementDeleteAction = ActionHelper.createAction(ElementDeleteAction.class, this);

  private final TemplateCreateAction templateCreateAction = ActionHelper.createAction(TemplateCreateAction.class, this);

  private final TemplateReadAction templateReadAction = ActionHelper.createAction(TemplateReadAction.class, this);

  private final TemplateUpdateAction templateUpdateAction = ActionHelper.createAction(TemplateUpdateAction.class, this);

  private final TemplateDeleteAction templateDeleteAction = ActionHelper.createAction(TemplateDeleteAction.class, this);

  private final ViewCreateAction viewCreateAction = ActionHelper.createAction(ViewCreateAction.class, this);

  private final ViewReadAction viewReadAction = ActionHelper.createAction(ViewReadAction.class, this);

  private final ViewUpdateAction viewUpdateAction = ActionHelper.createAction(ViewUpdateAction.class, this);

  private final ViewDeleteAction viewDeleteAction = ActionHelper.createAction(ViewDeleteAction.class, this);

  private final View1CreateAction view1CreateAction = ActionHelper.createAction(View1CreateAction.class, this);

  private final View1UpdateAction view1UpdateAction = ActionHelper.createAction(View1UpdateAction.class, this);

  private final Render1Action render1Action = ActionHelper.createAction(Render1Action.class, this);

  private final Render2Action render2Action = ActionHelper.createAction(Render2Action.class, this);

  private final InitAction initAction = ActionHelper.createAction(InitAction.class, this);

  private final Logger log = Logger.getLogger(ContentApi.class);

}
