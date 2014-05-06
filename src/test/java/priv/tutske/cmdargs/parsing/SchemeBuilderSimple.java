package priv.tutske.cmdargs.parsing;

import org.tutske.cmdargs.*;
import priv.tutske.cmdargs.CommandSchemeBuilderImpl;


public class SchemeBuilderSimple {

	public CommandScheme build () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.add (new BasicOption ("verbose", "v"));
		builder.add (new BasicOption ("human readable", "H"));
		builder.add (new BasicOption ("two words", "t"));
		builder.add (new StringOption ("path", "p"));
		builder.add (new StringOption ("layout", "l"));
		builder.add (new NumberOption ("number", "n"));
		builder.add (new BooleanOption ("enabled"));
		builder.add (new BooleanOption ("recursive"));
		builder.add (new BasicOption ("help", "h"));

		return builder.buildScheme ();
	}

	public CommandScheme buildWitArguments () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.add (new BasicOption ("verbose", "v"));
		builder.add (new BasicOption ("human readable", "H"));
		builder.add (new BasicOption ("two words", "t"));
		builder.add (new StringOption ("path", "p"));
		builder.add (new StringOption ("layout", "l"));
		builder.add (new BooleanOption ("enabled"));
		builder.add (new BooleanOption ("recursive"));
		builder.add (new BasicOption ("help", "h"));

		builder.add (new StringArgument ("first", 0));
		builder.add (new StringArgument ("second", 1));
		builder.add (new StringArgument ("third", 2, false));

		return builder.buildScheme ();
	}

	public CommandScheme buildWithRequired () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.add (new BasicOption ("verbose", "v"));
		builder.add (new BasicOption ("human readable", "H"));
		builder.add (new BasicOption ("two words", "t"));
		builder.add (new StringOption ("path", "p", true));
		builder.add (new StringOption ("layout", "l"));
		builder.add (new BooleanOption ("enabled"));
		builder.add (new BooleanOption ("recursive"));
		builder.add (new BasicOption ("help", "h"));

		return builder.buildScheme ();
	}

}
