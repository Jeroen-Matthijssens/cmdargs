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
		glob: while (! tokens.atEnd ()) {
			switch ( tokens.typeOfNext ()) {
				case SHORT: handleShortOption (); break;
				case LONG: handleLongOption (); break;
				case BREAK: break glob;
				case NONE: break glob;
				default: break glob;
			}
		}
	}

	private void handleLongOption () {
		String [] parsed = tokens.consume ().split ("=", 2);

		if ( parsed.length == 1 ) {
			addOption (parsed[0], null);
			return;
		}

		String [] values = parsed [1].split (",");
		for ( String value : values ) { addOption (parsed[0], value); }
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
			parsed.add (scheme.getOption (s));
		}
	}

	public void handleSingleShortOption (String repr) {
		repr = repr.substring (1);
		if ( ! scheme.hasOption (repr) ) { throw new UnknownOptionException (repr); }

		if ( ! canConsumeValue (repr) ) { addOption (repr, null); }
		else { addOption (repr, tokens.consume ()); }
		return;
	}

	private boolean canConsumeValue (String repr) {
		return ! tokens.atEnd () && (scheme.getOption (repr) instanceof ValueOption)
			&& tokens.typeOfNext () == ArgsTokens.TokenType.NONE;
	}

	private void addOption (String repr, String value) throws CommandLineException {
		if ( ! scheme.hasOption (repr) ) { throw new UnknownOptionException (repr); }
		Option option = scheme.getOption (repr);

		if ( ! (option instanceof ValueOption) ) {
			parsed.add (option);
			return;
		}

		@SuppressWarnings("unchecked")
		ValueOption<Object> valueOption = (ValueOption<Object>) option;

		Validator<Object> validator = valueOption.getValidator (repr);
		Object realval;
		if ( value == null && validator.hasDefault () ) {
			realval = validator.defaultValue ();
		} else if ( validator.isValid (value) ) {
			realval = validator.parse (value);
		} else {
			throw new OptionValueException (option, value);
		}
		parsed.add (valueOption, realval);
	}

}
