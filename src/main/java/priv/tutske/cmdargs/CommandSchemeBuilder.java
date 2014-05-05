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

	public void addOption (Option option) {
		add (option);
	}

	public void add (Option option) {
		// If multiple options with the same representation are added more than once there
		// is a problem. Abuse the fact that equals is based on the long representation.
		if ( options.indexOf (option) < 0 ) { options.add (option); return; }
		throw new IllegalArgumentException (option.toString ());
	}
	public void addCommand (Command command) {
		add (command);
	}

	public void add (Command command) {
		if ( arguments.size () != 0 ) { throw new RuntimeException (NOT_BOTH); }
		commands.add (command);
	}

	public void addArgument (Argument<?> argument) {
		add (argument);
	}

	public void add (Argument<?> argument) {
		if ( commands.size () != 0 ) { throw new RuntimeException (NOT_BOTH); }
		arguments.add (argument);
	}

	public CommandScheme buildScheme () {
		if ( arguments.size () == 0 ) {
			return CommandSchemeImpl.withCommands (options, commands);
		} else {
			return CommandSchemeImpl.withArguments (options, arguments);
		}
	}

}
