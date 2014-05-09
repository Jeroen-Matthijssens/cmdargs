package priv.tutske.cmdargs;

import org.tutske.cmdargs.Command;
import org.tutske.cmdargs.CommandScheme;
import org.tutske.cmdargs.exceptions.MissingCommandException;


public class ParserCommandExtractor {

	private static final String NOT_VALID = "Expected commad but found `%s`";

	private CommandScheme scheme;
	private ParsedCommandImpl parsed;
	private ArgsTokens tokens;

	public ParserCommandExtractor (CommandScheme scheme, ParsedCommandImpl parsed, ArgsTokens tokens) {
		this.scheme = scheme;
		this.parsed = parsed;
		this.tokens = tokens;
	}

	public void extract () {
		if ( tokens.atEnd () ) { throw new MissingCommandException (); }

		String repr = tokens.consume ();
		if ( ! scheme.hasCommand (repr) ) {
			String msg = String.format (NOT_VALID, repr);
			throw new MissingCommandException (msg);
		}

		Command command = scheme.getCommand(repr);
		parsed.setCommand (command);
	}

}
