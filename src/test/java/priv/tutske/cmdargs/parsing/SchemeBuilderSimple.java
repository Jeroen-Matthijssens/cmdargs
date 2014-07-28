package priv.tutske.cmdargs.parsing;

import static org.tutske.cmdargs.Option.Requirement.*;

import java.util.EnumSet;
import org.tutske.cmdargs.*;
import org.tutske.cmdargs.Option.Requirement;
import priv.tutske.cmdargs.CommandSchemeBuilderImpl;


public class SchemeBuilderSimple {

	public CommandScheme build () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new BasicOption ("verbose", "v"));
		builder.addOption (new BasicOption ("human readable", "H"));
		builder.addOption (new BasicOption ("two words", "t"));
		builder.addOption (new StringOption ("path", "p"));
		builder.addOption (new StringOption ("layout", "l"));
		builder.addOption (new NumberOption ("number", "n"));
		builder.addOption (new BooleanOption ("enabled"));
		builder.addOption (new BooleanOption ("recursive"));
		builder.addOption (new BasicOption ("help", "h"));

		return builder.buildScheme ();
	}

	public CommandScheme buildWitArguments () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new BasicOption ("verbose", "v"));
		builder.addOption (new BasicOption ("human readable", "H"));
		builder.addOption (new BasicOption ("two words", "t"));
		builder.addOption (new StringOption ("path", "p"));
		builder.addOption (new StringOption ("layout", "l"));
		builder.addOption (new BooleanOption ("enabled"));
		builder.addOption (new BooleanOption ("recursive"));
		builder.addOption (new BasicOption ("help", "h"));

		builder.addArgument (new StringArgument ("first", 0));
		builder.addArgument (new StringArgument ("second", 1));
		builder.addArgument (new StringArgument ("third", 2, false));

		return builder.buildScheme ();
	}

	public CommandScheme buildWithRequired () {
		CommandSchemeBuilderImpl builder = new CommandSchemeBuilderImpl ();

		builder.addOption (new BasicOption ("verbose", "v"));
		builder.addOption (new BasicOption ("human readable", "H"));
		builder.addOption (new BasicOption ("two words", "t"));
		builder.addOption (new StringOption ("layout", "l"));
		builder.addOption (new BooleanOption ("enabled"));
		builder.addOption (new BooleanOption ("recursive"));
		builder.addOption (new BasicOption ("help", "h"));
		builder.addOption (new StringOption ("path", "p",
			EnumSet.of (RequireValue, RequirePresence)
		));

		return builder.buildScheme ();
	}

}
