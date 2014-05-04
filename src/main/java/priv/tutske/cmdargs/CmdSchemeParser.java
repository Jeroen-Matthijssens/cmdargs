package priv.tutske.cmdargs;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class CmdSchemeParser implements Parser {

	private ParsedCommandImpl cmdParsed;
	private CommandScheme scheme;
	private ArgsTokens tokens;

	/* Constructors */

	private CmdSchemeParser () {
		cmdParsed = new ParsedCommandImpl ();
	}

	public CmdSchemeParser (CommandScheme scheme) {
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
		CmdSchemeParser parser = new CmdSchemeParser (command.getOptionScheme ());
		cmdParsed.setParsed (parser.parse (tokens));
	}

	private void handleLongOption ()
	throws CommandLineException {
		String [] parsed = tokens.consume ().split ("=", 2);

		if ( parsed.length == 2 ) { addOption (parsed[0], parsed[1]); }
		else { addOption (parsed[0], ""); }
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

		String msg = "What was supposed to be the command is not a valid one.";
		throw new MissingSubCommandException (msg);
	}

	private void handleArgs () {
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
