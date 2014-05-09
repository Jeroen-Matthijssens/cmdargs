package priv.tutske.cmdargs;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class ParserOptionExtractor {

	private CommandScheme scheme;
	private ParsedCommandImpl parsed;
	private ArgsTokens tokens;

	public ParserOptionExtractor (CommandScheme scheme, ParsedCommandImpl parsed, ArgsTokens tokens) {
		this.scheme = scheme;
		this.parsed = parsed;
		this.tokens = tokens;
	}

	public void extract () {
		while (! tokens.atEnd ()) {
			switch ( tokens.typeOfNext ()) {
				case SHORT: handleShortOption (); break;
				case LONG: handleLongOption (); break;
				default: return;
			}
		}
	}

	private void handleLongOption () {
		String [] parsed = tokens.consume ().split ("=", 2);
		String repr = parsed[0];

		if ( ! scheme.hasOption (repr) ) { throw new UnknownOptionException (repr); }
		Option option = scheme.getOption (repr);

		if ( parsed.length == 2 ) {
			String [] values = parsed [1].split (",");
			for ( String value : values ) { addOption (option, repr, value); }
		} else {
			addOption (option, repr);
		}
	}

	private void handleShortOption () {
		String repr = tokens.consume ();
		if ( repr.length () > 2 ) { handleShortSequence (repr); }
		else { handleSingleShortOption (repr); }
	}

	private void handleShortSequence (String sequence) {
		for (int i = 1; i < sequence.length (); i++) {
			String s = sequence.substring (i, i+1);
			if ( ! scheme.hasOption (s) ) { throw new UnknownOptionException (s); }
			addOption (scheme.getOption (s), s);
		}
	}

	public void handleSingleShortOption (String repr) {
		if ( ! scheme.hasOption (repr) ) { throw new UnknownOptionException (repr); }
		Option option = scheme.getOption (repr);
		if ( ! canConsumeValue (repr) ) { addOption (option, repr); }
		else { addOption (option, repr, tokens.consume ()); }
	}

	private boolean canConsumeValue (String repr) {
		return ! tokens.atEnd () && (scheme.getOption (repr) instanceof ValueOption)
			&& tokens.typeOfNext () == ArgsTokens.TokenType.NONE;
	}

	private void addOption (Option option, String repr) {
		if ( option instanceof ValueOption ) { addWithDefault (option, repr); }
		parsed.addOption (option);
	}

	private void addWithDefault (Option option, String repr) {
		@SuppressWarnings("unchecked")
		ValueOption<Object> valueOption = (ValueOption<Object>) option;
		Validator<Object> validator = valueOption.getValidator (repr);

		if ( ! validator.hasDefault () ) { parsed.addOption (option); }
		else { parsed.addOption (valueOption, validator.defaultValue ()); }
	}

	private void addOption (Option option, String repr, String value) {
		if ( ! (option instanceof ValueOption) ) {
			throw new OptionValueException (option, value);
		}

		@SuppressWarnings("unchecked")
		ValueOption<Object> valueOption = (ValueOption<Object>) option;

		Validator<Object> validator = valueOption.getValidator (repr);
		boolean valid = validator.isValid (value);
		if ( ! valid ) { throw new OptionValueException (option, value); }
		parsed.addOption (valueOption, validator.parse (value));
	}

}
