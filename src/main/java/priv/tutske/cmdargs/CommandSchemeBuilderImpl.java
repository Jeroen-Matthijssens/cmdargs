package priv.tutske.cmdargs;

import java.util.List;
import java.util.ArrayList;

import org.tutske.cmdargs.*;

public class CommandSchemeBuilderImpl implements CommandSchemeBuilder {

	private static final String NOT_BOTH = "Can not have both commands and arguments";

	List<Command> commands;
	List<Option> options;
	List<Argument<?>> arguments;

	public CommandSchemeBuilderImpl () {
		reset ();
	}

	public void reset () {
		commands = new ArrayList<Command> ();
		options = new ArrayList<Option> ();
		arguments = new ArrayList<Argument<?>> ();
	}

	public CommandSchemeBuilder addOption (Option option) {
		add (option);
		return this;
	}

	public CommandSchemeBuilder add (Option option) {
		// If multiple options with the same representation are added more than once there
		// is a problem. Abuse the fact that equals is based on the long representation.
		if ( options.indexOf (option) > -1 ) {
			throw new IllegalArgumentException (option.toString ());
		}
		options.add (option);
		return this;
	}

	public CommandSchemeBuilder addCommand (Command command) {
		add (command);
		return this;
	}

	public CommandSchemeBuilder add (Command command) {
		if ( arguments.size () != 0 ) { throw new RuntimeException (NOT_BOTH); }
		commands.add (command);
		return this;
	}

	public CommandSchemeBuilder addArgument (Argument<?> argument) {
		add (argument);
		return this;
	}

	public CommandSchemeBuilder add (Argument<?> argument) {
		if ( commands.size () != 0 ) { throw new RuntimeException (NOT_BOTH); }
		arguments.add (argument);
		return this;
	}

	public CommandScheme buildScheme () {
		if ( arguments.size () == 0 ) {
			return CommandSchemeImpl.withCommands (options, commands);
		} else {
			return CommandSchemeImpl.withArguments (options, arguments);
		}
	}

}
