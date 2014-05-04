package priv.tutske.cmdargs;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.tutske.cmdargs.*;


public class ParsedOptionsImpl implements ParsedOptions {

	private Command command;
	private List<Option> options;
	private Map<Option, Object> values;

	/* constructors */

	public ParsedOptionsImpl (Command command) {
		this.command = command;
		options = new ArrayList<Option> ();
		values = new HashMap<Option, Object> ();
	}

	/* implementing `ParsedOptions` */

	@Override
	public boolean isPresent (String representation) {
		try { return findOption (representation) != null; }
		catch (RuntimeException e) { return false; }
	}

	@Override
	public boolean isPresent (Option option) {
		return options.contains (option);
	}

	@Override
	public Command getCommand () {
		return command;
	}

	@Override
	public <T> T getValue (String representation) {
		Option option = findOption (representation);
		if ( ! (option instanceof ValueOption) ) { throw new RuntimeException (); }
		return getValue ((ValueOption<T>) option);
	}

	@Override
	public <T> T getValue (ValueOption<T> option){
		return (T) values.get (option);
	}

	/* functions to buildup the ParsedOptions*/

	public <T> void add (ValueOption<T> option, T value) {
		options.add (option);
		values.put (option, value);
	}

	public void add (Option option) {
		options.add (option);
	}

	private Option findOption (String representation) {
		representation = new ReprNormalizer (representation).normalize ();
		for ( Option option : options ) {
			if ( option.matches(representation) ) { return option; }
		}
		throw new RuntimeException ("option not found `" + representation + "`");
	}

}
