package priv.tutske.cmdargs;

import java.util.List;
import java.util.ArrayList;
import java.util.MissingResourceException;

import org.tutske.cmdargs.*;

public class CommandSchemeImpl implements CommandScheme {

	private List<Option> options;
	private List<Command> commands;
	private List<Argument<?>> arguments;

	/* Constructors */

	public CommandSchemeImpl () {
		options = new ArrayList<Option> ();
	}

	public CommandSchemeImpl (List<Option> options) {
		this (options, null);
	}

	public CommandSchemeImpl (List<Option> options, List<Argument<?>> arguments) {
		this.options = options;
		this.arguments = arguments;
	}

	/* implementing OptoinScheme interface */

	@Override
	public boolean hasArguments () {
		return arguments != null;
	}

	@Override
	public boolean hasCommands () {
		return commands != null;
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

}
