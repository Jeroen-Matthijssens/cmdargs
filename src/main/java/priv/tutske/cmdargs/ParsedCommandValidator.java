package priv.tutske.cmdargs;

import static org.tutske.cmdargs.Option.Requirement.*;

import java.util.Set;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class ParsedCommandValidator {

	private ParsedCommand parsed;
	private CommandScheme scheme;

	public ParsedCommandValidator (ParsedCommand parsed, CommandScheme scheme) {
		this.parsed = parsed;
		this.scheme = scheme;
	}

	public void validate () {
		validatePresence ();
		validateValuePresence ();
	}

	private void validatePresence () {
		Set<Option> tovalidate = scheme.getByRequirement (RequirePresence);
		for ( Option option : tovalidate ) {
			if ( parsed.hasOption (option) ) {  continue; }
			throw new MissingOptionException (option);
		}
	}

	private void validateValuePresence () {
		Set<Option> tovalidate = scheme.getByRequirement (RequireValue);
		for ( Option option : tovalidate ) {
			if ( ! parsed.hasOption (option) ) {
				throw new MissingOptionException (option);
			}
			if ( ! (option instanceof ValueOption) ) {
				throw new RuntimeException ();
			}
			ValueOption<Object> valueOption = (ValueOption<Object>) option;
			if ( parsed.getOptionValue (valueOption) == null ) {
				throw new OptionValueException (option, null);
			}
		}
	}
}
