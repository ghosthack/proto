package jsontemplate;

interface IStatement {

	void execute(ScopedContext context, ITemplateRenderCallback callback);

}
