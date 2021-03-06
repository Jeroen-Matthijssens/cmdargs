package priv.tutske.cmdargs;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.tutske.cmdargs.*;


public class ParsedCommandImpl implements ParsedCommand {

	private Command command;
	private ParsedCommand parsed;
	private List<Option> options;
	private List<Argument<?>> arguments;
	private Map<Option, List<Object>> optValues;
	private Map<Argument<?>, Object> argValues;
	private List<String> remainingArgs;

	/* constructors */

	public ParsedCommandImpl () {
		options = new ArrayList<Option> ();
		arguments = new ArrayList<Argument<?>> ();
		optValues = new HashMap<Option, List<Object>> ();
		argValues = new HashMap<Argument<?>, Object> ();
	}

	/* implementing `ParsedOptions` */

	@Override
	public boolean hasCommand () {
		return command != null;
	}

	@Override
	public Command getCommand () {
		return command;
	}

	@Override
	public ParsedCommand getParsedCommand () {
		return parsed;
	}

	@Override
	public boolean hasOption (Option option) {
		return options.contains (option);
	}

	@Override
	public <T> T getOptionValue (ValueOption<T> option) {
		if ( ! optValues.containsKey (option) ) { return null; }
		return (T) (optValues.get (option).get (0));
	}

	@Override
	public <T> T getOptionValue (ValueOption<T> option, T alternative) {
		if ( ! options.contains (option) ) { return alternative; }
		return getOptionValue (option);
	}

	@Override
	public <T> List<T> getOptionValues (ValueOption<T> option) {
		if ( ! optValues.containsKey (option) ) { return new ArrayList<T> (); }
		List<? extends Object> values = optValues.get (option);
		return (List<T>) values;
	}


	@Override
	public boolean hasArgument (Argument<?> argument) {
		return options.contains (argument);
	}

	@Override
	public <T> T getArgumentValue (Argument<T> argument) {
		if ( ! argValues.containsKey (argument) ) { return null; }
		return (T) argValues.get (argument);
	}

	@Override
	public List<String> getArgumentValues () {
		return remainingArgs;
	}

	/* functions to buildup the ParsedOptions*/

	public <T> void addOption (ValueOption<T> option, T value) {
		if ( ! optValues.containsKey (option) ) {
			optValues.put (option, new ArrayList<Object> ());
		}
		options.add (option);
		optValues.get (option).add (value);
	}

	public void addOption (Option option) {
		options.add (option);
	}

	public <T> void addDefaultedOption (ValueOption<T> option, T value) {
		if ( ! optValues.containsKey (option) ) {
			optValues.put (option, new ArrayList<Object> ());
		}
		optValues.get (option).add (value);
	}

	public void addArgument (Argument<?> argument, Object value) {
		arguments.add (argument);
		argValues.put (argument, value);
	}

	public void addArgument (String value) {
		remainingArgs.add (value);
	}

	public void setCommand (Command command) {
		this.command = command;
	}

	public void setParsed (ParsedCommand parsed) {
		this.parsed = parsed;
	}

}
