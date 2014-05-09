package priv.tutske.cmdargs;

import static priv.tutske.cmdargs.ArgsTokens.TokenType.*;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class ParserCommandExtractor {

	private CommandScheme scheme;
	private ParsedCommandImpl parsed;
	private ArgsTokens tokens;

	public ParserCommandExtractor (CommandScheme scheme, ParsedCommandImpl parsed, ArgsTokens tokens) {
		this.scheme = scheme;
		this.parsed = parsed;
		this.tokens = tokens;
	}

	public void extract () {
		if ( cannotFindPossibleCandidate () ) { throw new MissingCommandException (); }

		String repr = tokens.consume ();
		if ( ! scheme.hasCommand (repr) ) { throw new CommandMismatchException (repr); }

		Command command = scheme.getCommand(repr);
		parsed.setCommand (command);
	}

	public boolean cannotFindPossibleCandidate () {
		return tokens.atEnd () || tokens.typeOfNext () != NONE;
	}

}
