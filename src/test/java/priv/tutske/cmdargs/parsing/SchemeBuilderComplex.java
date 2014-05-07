package priv.tutske.cmdargs.parsing;

import org.tutske.cmdargs.*;

import priv.tutske.cmdargs.CommandImpl;
import priv.tutske.cmdargs.CommandSchemeBuilderImpl;


public class SchemeBuilderComplex {

	private Option verbase = new BooleanOption ("verbose", "v");
	private Option help = new BasicOption ("help", "h");

	public CommandScheme build () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new BooleanOption ("enabled"));
		builder.addOption (new StringOption ("path", "p"));
		builder.addOption (new StringOption ("encoding", "E"));
		builder.addOption (verbase);
		builder.addOption (help);
		builder.addCommand (buildInitCommand ());
		builder.addCommand (buildConfigCommand ());
		builder.addCommand (buildListCommand ());
		builder.addCommand (buildMapCommand ());
		builder.addCommand (buildShowCommand ());
		CommandScheme cmdscheme = builder.buildScheme ();

		return cmdscheme;
	}

	private Command buildInitCommand () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new StringOption ("repository path", "p"));
		builder.addOption (verbase);
		builder.addOption (help);
		CommandScheme initScheme = builder.buildScheme ();
		Command init = new CommandImpl ("init", initScheme);

		return init;
	}

	private Command buildConfigCommand () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new BasicOption ("global", "g"));
		builder.addOption (new BasicOption ("unset", "u"));
		builder.addArgument (new StringArgument ("item", 0));
		builder.addArgument (new StringArgument ("value", 1, false));
		CommandScheme configScheme = builder.buildScheme ();
		Command config = new CommandImpl ("config", configScheme);

		return config;
	}

	private Command buildListCommand () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (help);
		builder.addArgument (new StringArgument ("value", 0));
		CommandScheme pushScheme = builder.buildScheme ();
		Command push = new CommandImpl ("push", pushScheme);

		builder.reset ();

		builder.addOption (help);
		CommandScheme popScheme = builder.buildScheme ();
		Command pop = new CommandImpl ("pop", popScheme);

		builder.reset ();

		builder.addOption (help);
		builder.addArgument (new StringArgument ("index", 0, false));
		CommandScheme peekScheme = builder.buildScheme ();
		Command peek = new CommandImpl ("peek", peekScheme);

		builder.reset ();

		builder.addOption (new BasicOption ("human readable", "H"));
		builder.addOption (help);
		builder.addCommand (buildCreateCommand ());
		builder.addCommand (push);
		builder.addCommand (pop);
		builder.addCommand (peek);
		builder.addCommand (buildDeleteCommand ());
		CommandScheme listScheme = builder.buildScheme ();
		Command list = new CommandImpl ("list", listScheme);

		return list;
	}

	private Command buildMapCommand () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new StringOption ("key", "k", true));
		builder.addOption (new StringOption ("value", "v", true));
		builder.addOption (help);
		CommandScheme putScheme = builder.buildScheme ();
		Command put = new CommandImpl ("push", putScheme);

		builder.reset ();

		builder.addOption (new StringOption ("key", "k"));
		builder.addOption (help);
		CommandScheme unsetScheme = builder.buildScheme ();
		Command unset = new CommandImpl ("unset", unsetScheme);

		builder.reset ();

		builder.addOption (verbase);
		builder.addOption (help);
		builder.addCommand (buildCreateCommand ());
		builder.addCommand (buildDeleteCommand ());
		builder.addCommand (put);
		builder.addCommand (unset);
		CommandScheme mapScheme = builder.buildScheme ();
		Command map = new CommandImpl ("push", mapScheme);

		return map;
	}

	private Command buildShowCommand () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new BasicOption ("all", "a"));
		builder.addOption (new BooleanOption ("human readable", "H"));
		builder.addOption (new BasicOption ("paginated"));
		builder.addOption (new NumberOption ("page size"));
		builder.addOption (verbase);
		builder.addOption (help);
		CommandScheme showScheme = builder.buildScheme ();
		Command show = new CommandImpl ("show", showScheme);

		return show;
	}

	/* -- recurring things -- */

	private Command buildCreateCommand () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new StringOption ("template", "p"));
		builder.addOption (new StringOption ("layout", "l"));
		builder.addOption (new BooleanOption ("interactive", "i"));
		builder.addOption (verbase);
		builder.addOption (help);
		CommandScheme createScheme = builder.buildScheme ();
		Command create = new CommandImpl ("create", createScheme);

		return create;
	}

	private Command buildDeleteCommand () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new BasicOption ("force", "f"));
		builder.addOption (new BooleanOption ("interactive", "i"));
		builder.addOption (verbase);
		builder.addOption (help);
		CommandScheme deleteScheme = builder.buildScheme ();
		Command delete = new CommandImpl ("delete", deleteScheme);

		return delete;
	}

}
