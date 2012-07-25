package jsontemplate;


class TemplateExecutor {

	static void execute(StatementList statements,
			ScopedContext context,
			ITemplateRenderCallback callback) {
		for (IStatement statement : statements) {
			statement.execute(context, callback);
		}
	}

}
