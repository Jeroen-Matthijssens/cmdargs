package priv.tutske.cmdargs;

import static org.tutske.cmdargs.Option.Requirement.*;

import java.util.List;
import java.util.Set;

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

		if ( this.exception != null ) { throw exception; }
		return cmdParsed;
	}

	private void process () {
		kickOff ();
		new ParsedCommandValidator (cmdParsed, scheme).validate ();
	}

	private void kickOff () {
		boolean broken = false;

		glob: while (! tokens.atEnd ()) {
			switch ( tokens.typeOfNext ()) {
				case SHORT: handleShortOption (); break;
				case LONG: handleLongOption (); break;
				case BREAK: tokens.skip (); broken = true; break glob;
				case NONE: break glob;
				default: break glob;
			}
		}

		if ( (tokens.atEnd () || broken) && scheme.hasCommands () ) {
			throw new MissingCommandException ();
		}

		if ( tokens.atEnd () ) { return; }

		if ( broken || ! scheme.hasCommands () ) {
			handleArgs ();
			return;
		}

		Command command = handleCommand ();
		cmdParsed.setCommand (command);
		ParserImpl parser = new ParserImpl (command.getCommandScheme ());
		cmdParsed.setParsed (parser.parse (tokens));
	}

	private void handleLongOption ()
	throws CommandLineException {
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
			cmdParsed.add (scheme.getOption (s));
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
			cmdParsed.add (option);
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
		cmdParsed.add (valueOption, realval);
	}

	private Command handleCommand () {
		if ( tokens.atEnd () ) { throw new MissingCommandException (); }

		String repr = tokens.consume ();
		if ( scheme.hasCommand (repr) ) { return scheme.getCommand(repr); }

		String msg = "What was supposed to be the command is not a valid one. `" + repr + "`";
		throw new MissingCommandException (msg);
	}

	private void handleArgs () {
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
			cmdParsed.addArgument (argument, value);
		}
	}

}
