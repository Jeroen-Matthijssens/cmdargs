package priv.tutske.cmdargs;

import static org.tutske.cmdargs.Option.Requirement.*;

import java.util.Set;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class ParsedCommandValidator {

	private static final String NOT_VALUE_OPTION =
		"Scheme returned a non value option when looking up options with the" +
		"RequireValue requirement set. (scheme: `%s`, option: `%s`)";

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
			if ( parsed.hasOption (option) ) { continue; }
			throw new MissingOptionException (option);
		}
	}

	private void validateValuePresence () {
		Set<Option> tovalidate = scheme.getByRequirement (RequireValue);
		for ( Option option : tovalidate ) { throwOnAbsentValue (option); }
	}

	private void throwOnAbsentValue (Option option) {
		if ( ! parsed.hasOption (option) ) { return; }

		if ( ! (option instanceof ValueOption) ) {
			throw new RuntimeException (String.format (NOT_VALUE_OPTION,
				scheme.toString (), option.toString ()
			));
		}

		@SuppressWarnings ("unchecked")
		ValueOption<Object> valueOption = (ValueOption<Object>) option;

		if ( parsed.getOptionValue (valueOption) == null ) {
			throw new OptionValueException (option, null);
		}
	}

}
