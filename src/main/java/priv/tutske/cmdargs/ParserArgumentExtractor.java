package priv.tutske.cmdargs;

import java.util.List;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class ParserArgumentExtractor {

	private CommandScheme scheme;
	private ParsedCommandImpl parsed;
	private ArgsTokens tokens;

	public ParserArgumentExtractor (CommandScheme scheme, ParsedCommandImpl parsed, ArgsTokens tokens) {
		this.scheme = scheme;
		this.parsed = parsed;
		this.tokens = tokens;
	}

	public void extract () {
		List<Argument<?>> arguments = scheme.getArguments ();
		for ( Argument<?> argument : arguments ) {
			if ( tokens.atEnd () && argument.isRequired () ) {
				String tpl = "Could not find required argument: %s";
				String msg = String.format (tpl, argument.toString ());
				throw new ArgumentValueException (msg);
			}

			if ( tokens.atEnd () ) { break; }

			Validator<?> validator = argument.getValidator ();
			String valuestring = tokens.peek ();

			if ( ! validator.isValid (valuestring) && argument.isRequired () ) {
				String tpl = "Could not parse required argument: %s `%s`";
				String msg = String.format (tpl, argument.toString (), valuestring);
				throw new ArgumentValueException (msg);
			}

			if ( ! validator.isValid (valuestring) ) { continue; }

			if ( ! tokens.atEnd () ) { tokens.consume (); }
			Object value = validator.parse (valuestring);
			parsed.addArgument (argument, value);
		}
	}

}
