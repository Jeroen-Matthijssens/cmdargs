package priv.tutske.cmdargs;

import static priv.tutske.cmdargs.ArgsTokens.TokenType.*;

import java.io.OutputStream;
import java.io.PrintStream;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class ParserImpl implements Parser {

	private CommandLineException exception;
	private ParsedCommandImpl cmdParsed;
	private CommandScheme scheme;
	private ArgsTokens tokens;

	/* Constructors */

	public ParserImpl (CommandScheme scheme) {
		cmdParsed = new ParsedCommandImpl ();
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
		new ParsedCommandValidator (cmdParsed, scheme).validate ();

		if ( this.exception != null ) { throw exception; }
		return cmdParsed;
	}

	private void process () {
		new ParserOptionExtractor (scheme, cmdParsed, tokens).extract ();

		boolean broken = ! tokens.atEnd () && tokens.typeOfNext ().equals (BREAK);
		if ( broken ) { tokens.skip (); }

		if ( (tokens.atEnd () || broken) && scheme.hasCommands () ) {
			throw new MissingCommandException ();
		}

		if ( tokens.atEnd () ) { return; }

		if ( broken || ! scheme.hasCommands () ) {
			new ParserArgumentExtractor (scheme, cmdParsed, tokens).extract ();
			return;
		}

		new ParserCommandExtractor (scheme, cmdParsed, tokens).extract ();
		Command command = cmdParsed.getCommand ();
		ParserImpl parser = new ParserImpl (command.getCommandScheme ());
		cmdParsed.setParsed (parser.parse (tokens));
	}

}
