package priv.tutske.cmdargs;

import java.util.*;

import org.tutske.cmdargs.*;


public class CommandSchemeBuilderImpl implements CommandSchemeBuilder {

	private static final String NOT_BOTH = "Can not have both commands and arguments";
	private static final String EXISTS = "An option with the name as `%s` already exists";

	List<Command> commands;
	List<Option> options;
	Map<Option, Object> defaults;
	List<Argument<?>> arguments;

	public CommandSchemeBuilderImpl () {
		reset ();
	}

	public void reset () {
		commands = new ArrayList<Command> ();
		options = new ArrayList<Option> ();
		arguments = new ArrayList<Argument<?>> ();
		defaults = new HashMap<Option, Object> ();
	}

	@Override
	public CommandSchemeBuilder addOption (Option option) {
		// If multiple options with the same representation are added more than once there
		// is a problem. Abuse the fact that equals is based on the long representation.
		if ( options.contains (option) ) {
			String msg = String.format (EXISTS, option.toString ());
			throw new IllegalArgumentException (msg);
		}
		options.add (option);
		return this;
	}

	@Override
	public <T> CommandSchemeBuilder addOption (ValueOption<T> option, T value) {
		addOption (option);
		defaults.put (option, value);
		return this;
	}

	@Override
	public CommandSchemeBuilder addCommand (Command command) {
		if ( arguments.size () != 0 ) { throw new RuntimeException (NOT_BOTH); }
		commands.add (command);
		return this;
	}

	@Override
	public CommandSchemeBuilder addArgument (Argument<?> argument) {
		if ( commands.size () != 0 ) { throw new RuntimeException (NOT_BOTH); }
		arguments.add (argument);
		return this;
	}

	@Override
	public CommandScheme buildScheme () {
		if ( arguments.size () == 0 ) {
			return CommandSchemeImpl.withCommands (options, defaults, commands);
		} else {
			return CommandSchemeImpl.withArguments (options, defaults, arguments);
		}
	}

	@Override
	public Command buildCommand (String command) {
		return new CommandImpl (command, buildScheme ());
	}

}
