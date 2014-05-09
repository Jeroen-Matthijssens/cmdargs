package priv.tutske.cmdargs;

import java.io.OutputStream;
import java.io.PrintStream;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class ParserImpl implements Parser {

	private CommandLineException exception;
	private ParsedCommandImpl parsed;
	private CommandScheme scheme;
	private ArgsTokens tokens;

	/* Constructors */

	public ParserImpl (CommandScheme scheme) {
		parsed = new ParsedCommandImpl ();
		this.scheme = scheme;
	}

	/* public methods */

	@Override
	public void printError () {
		printError (System.out);
	}

	@Override
	public void printError (OutputStream stream) {
		printError (new PrintStream (stream));
	}

	@Override
	public ParsedCommand parse (String [] args) {
		return parse (new ArgsTokens (args));
	}

	/* private utility functions */

	private void printError (PrintStream writer) {
		writer.println (exception.getMessage ());
	}

	private ParsedCommand parse (ArgsTokens tokens) {
		this.tokens = tokens;

		try { process (); }
		catch (CommandLineException e) { this.exception = e; }

		if ( this.exception != null ) { throw exception; }
		return parsed;
	}

	private void process () {
		new ParserOptionExtractor (scheme, parsed, tokens).extract ();

		if ( scheme.hasCommands () ) {
			new ParserCommandExtractor (scheme, parsed, tokens).extract ();
			Command command = parsed.getCommand ();
			ParserImpl parser = new ParserImpl (command.getCommandScheme ());
			parsed.setParsed (parser.parse (tokens));
		} else if ( ! tokens.atEnd () ) {
			new ParserArgumentExtractor (scheme, parsed, tokens).extract ();
		}

		new ParsedCommandValidator (parsed, scheme).validate ();
	}

}
