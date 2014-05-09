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
			if ( tokens.atEnd () && ! argument.isRequired () ) { return; }
			if ( tokens.atEnd () ) { throw new MissingArgumentException (argument); }

			Validator<?> validator = argument.getValidator ();
			String valuestring = tokens.peek ();
			boolean valid = validator.isValid (valuestring);

			if ( ! valid && ! argument.isRequired () ) { continue; }
			if ( ! valid ) { throw new ArgumentValueException (argument, valuestring); }

			Object value = validator.parse (tokens.consume ());
			parsed.addArgument (argument, value);
		}
	}

}
