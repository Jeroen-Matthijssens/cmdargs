package priv.tutske.cmdargs.parsing;

import org.tutske.cmdargs.*;
import priv.tutske.cmdargs.CommandSchemeBuilder;


public class SchemeBuilderSimple {

	public CommandScheme build () {
		CommandSchemeBuilder builder = new CommandSchemeBuilder ();

		builder.add (new BasicOption ("verbose", "v"));
		builder.add (new BasicOption ("human readable", "H"));
		builder.add (new BasicOption ("two words", "t"));
		builder.add (new StringValueOption ("path", "p"));
		builder.add (new StringValueOption ("layout", "l"));
		builder.add (new BooleanOption ("enabled"));
		builder.add (new BooleanOption ("recursive"));
		builder.add (new BasicOption ("help", "h"));

		return builder.buildScheme ();
	}

	public CommandScheme buildWitArguments () {
		CommandSchemeBuilder builder = new CommandSchemeBuilder ();

		builder.add (new BasicOption ("verbose", "v"));
		builder.add (new BasicOption ("human readable", "H"));
		builder.add (new BasicOption ("two words", "t"));
		builder.add (new StringValueOption ("path", "p"));
		builder.add (new StringValueOption ("layout", "l"));
		builder.add (new BooleanOption ("enabled"));
		builder.add (new BooleanOption ("recursive"));
		builder.add (new BasicOption ("help", "h"));

		builder.add (new StringArgument ("first", 0));
		builder.add (new StringArgument ("first", 1));
		builder.add (new StringArgument ("first", 2, false));

		return builder.buildScheme ();
	}
}
