package priv.tutske.cmdargs;

import java.util.List;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class ParserImpl implements Parser {

	private ParsedCommandImpl cmdParsed;
	private CommandScheme scheme;
	private ArgsTokens tokens;

	/* Constructors */

	private ParserImpl () {
		cmdParsed = new ParsedCommandImpl ();
	}

	public ParserImpl (CommandScheme scheme) {
		this ();
		this.scheme = scheme;
	}

	/* public methods */

	@Override
	public ParsedCommand parse (String [] args) throws CommandLineException {
		return parse (new ArgsTokens (args));
	}

	public ParsedCommand parse (ArgsTokens tokens) {
		this.tokens = tokens;
		kickOff ();
		return cmdParsed;
	}

	/* private utility functions */

	private void kickOff () throws CommandLineException {
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
			throw new MissingSubCommandException ();
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
			addOption (parsed[0], "");
			return;
		}
		String [] values = parsed [1].split (",");
		for ( String value : values ) {
			addOption (parsed[0], value);
		}
	}

	private void handleShortOption ()
	throws CommandLineException {
		String repr = tokens.consume ();

		// check for possible value after this option.
		if ( repr.length () == 2 ) {
			repr = repr.substring (1);
			if ( ! scheme.hasOption (repr) ) { throw new UnknownOptionException (repr); }

			if ( ! canConsumeValue (repr) ) { addOption (repr, ""); }
			else { addOption (repr, tokens.consume ()); }
			return;
		}

		// go over every char and try to add the option.
		for (int i = 1; i < repr.length (); i++) {
			String s = repr.substring (i, i+1);
			if ( ! scheme.hasOption (s) ) { throw new UnknownOptionException (s); }
			cmdParsed.add (scheme.getOption (s));
		}
	}

	private boolean canConsumeValue (String repr) {
		return ! tokens.atEnd () && (scheme.getOption (repr) instanceof ValueOption)
			&& tokens.typeOfNext () == ArgsTokens.TokenType.NONE;
	}

	private Command handleCommand () throws MissingSubCommandException {
		if ( tokens.atEnd () ) { throw new MissingSubCommandException (); }

		String repr = tokens.consume ();
		if ( scheme.hasCommand (repr) ) { return scheme.getCommand(repr); }

		String msg = "What was supposed to be the command is not a valid one. `" + repr + "`";
		throw new MissingSubCommandException (msg);
	}

	private void handleArgs () {
		List<Argument<?>> arguments = scheme.getArguments ();
		for ( Argument<?> argument : arguments ) {
			if ( tokens.atEnd () && argument.isRequired () ) {
				String tpl = "Could not find required argument: %s";
				String msg = String.format (tpl, argument.toString ());
				throw new WrongValueException (msg);
			}

			if ( tokens.atEnd () ) { break; }

			Validator<?> validator = argument.getValidator ();
			String valuestring = tokens.peek ();

			if ( ! validator.isValid (valuestring) && argument.isRequired () ) {
				String tpl = "Could not parse required argument: %s `%s`";
				String msg = String.format (tpl, argument.toString (), valuestring);
				throw new WrongValueException (msg);
			}

			if ( ! validator.isValid (valuestring) ) { continue; }

			if ( ! tokens.atEnd () ) { tokens.consume (); }
			Object value = validator.parse (valuestring);
			cmdParsed.addArgument (argument, value);
		}
	}

	private void addOption (String repr, String value) throws CommandLineException {

		if ( ! scheme.hasOption (repr) ) { throw new UnknownOptionException ("`" + repr + "`"); }
		Option option = scheme.getOption (repr);

		if ( ! (option instanceof ValueOption) ) {
			cmdParsed.add (option);
			return;
		}
		@SuppressWarnings("unchecked")
		ValueOption<Object> valueOption = (ValueOption<Object>) option;

		Validator<Object> validator = valueOption.getValidator (repr);
		if ( ! validator.isValid (value) ) { throw new WrongValueException (); }
		cmdParsed.add (valueOption, validator.parse (value));
	}

}