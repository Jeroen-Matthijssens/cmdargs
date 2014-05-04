package priv.tutske.cmdargs;

import java.util.List;
import java.util.ArrayList;
import java.util.MissingResourceException;

import org.tutske.cmdargs.*;

public class CommandSchemeImpl implements CommandScheme {

	private List<Option> options;
	private List<Command> commands;
	private List<Argument<?>> arguments;

	/* -- static constructors -- */

	public static CommandScheme newInstance () {
		return new CommandSchemeImpl ();
	}

	public static CommandScheme newInstance (List<Option> options) {
		return new CommandSchemeImpl ().setOptions (options);
	}

	public static CommandScheme withCommands (List<Command> commands) {
		return new CommandSchemeImpl ().setCommands (commands);
	}

	public static CommandScheme withCommands (List<Option> options, List<Command> commands) {
		return new CommandSchemeImpl ().setOptions (options).setCommands (commands);
	}

	public static CommandScheme withArguments (List<Argument<?>> arguments) {
		return new CommandSchemeImpl ().setArguments (arguments);
	}

	public static CommandScheme withArguments (List<Option> options, List<Argument<?>> arguments) {
		return new CommandSchemeImpl ().setOptions (options).setArguments (arguments);
	}

	/* Constructors */

	public CommandSchemeImpl () {
		options = new ArrayList<Option> ();
	}

	/* implementing OptoinScheme interface */

	@Override
	public boolean hasArguments () {
		return arguments != null && arguments.size () != 0;
	}

	@Override
	public boolean hasCommands () {
		return commands != null && commands.size () != 0;
	}

	@Override
	public boolean hasOption (String representation) {
		try { return getOption (representation) != null; }
		catch (MissingResourceException e) { return false; }
	}

	@Override
	public boolean hasOption (Option option) {
		return options.contains (option);
	}

	@Override
	public boolean hasCommand (String command) {
		try { return getCommand (command) != null; }
		catch (MissingResourceException e) { return false; }
	}

	@Override
	public boolean hasCommand (Command command) {
		return false;
	}

	@Override
	public Option getOption (String representation) {
		if ( options == null ) { throw new RuntimeException (); }

		for (Option option : options ) {
			if ( option.matches (representation) ) { return option; }
		}

		String msg = "Option could not be found `" + representation + "`";
		throw new MissingResourceException (msg, "Option", representation);
	}

	@Override
	public Command getCommand (String cmdstring) {
		if ( commands == null ) { throw new RuntimeException (); }

		for (Command command : commands ) {
			if ( command.matches (cmdstring) ) { return command; }
		}

		String msg = "command could not be found `" + cmdstring + "`";
		throw new MissingResourceException (msg, "Command", cmdstring);
	}

	@Override
	public int nrOfArguments () {
		return arguments.size ();
	}

	@Override
	public Argument<?> getArgument (int position) {
		return arguments.get (position);
	}

	/* -- privates -- */

	private CommandSchemeImpl setOptions (List<Option> options) {
		this.options = options;
		return this;
	}

	private CommandSchemeImpl setArguments (List<Argument<?>> arguments) {
		this.arguments = arguments;
		return this;
	}

	private CommandSchemeImpl setCommands (List<Command> commands) {
		this.commands = commands;
		return this;
	}

}
