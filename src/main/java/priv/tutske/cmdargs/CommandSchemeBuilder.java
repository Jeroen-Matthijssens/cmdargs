package priv.tutske.cmdargs;

import java.util.List;
import java.util.ArrayList;

import org.tutske.cmdargs.*;

public class CommandSchemeBuilder {

	private static final String NOT_BOTH = "Can not have both commands and arguments";

	List<Command> commands;
	List<Option> options;
	List<Argument<?>> arguments;

	public CommandSchemeBuilder () {
		reset ();
	}

	public void reset () {
		commands = new ArrayList<Command> ();
		options = new ArrayList<Option> ();
		arguments = new ArrayList<Argument<?>> ();
	}

	public void add (Option option) {
		// If multiple options with the same representation are added more than once there
		// is a problem. Abuse the fact that equals is based on the long representation.
		if (options.indexOf (option) != -1) { throw new IllegalArgumentException (); }
		options.add (option);
	}

	public void add (Command command) {
		if ( arguments.size () != 0 ) { throw new RuntimeException (NOT_BOTH); }
		commands.add (command);
	}

	public void add (Argument<?> argument) {
		if ( commands.size () != 0 ) { throw new RuntimeException (NOT_BOTH); }
		arguments.add (argument);
	}

	public CommandScheme buildScheme () {
		return new CommandSchemeImpl (options, arguments);
	}

}
