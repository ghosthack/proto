package content.handlers.template;

public interface TemplateCreateHandler {

  void success();

  void alreadyExists();

  void unableToUpdate();

}
