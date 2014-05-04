package priv.tutske.cmdargs;

import java.util.Arrays;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class CmdSchemeParser implements Parser {

	private ParsedCommandImpl cmdParsed;
	private CommandScheme scheme;
	private String [] args;
	private int pos;

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
	public void parse (String [] args) throws CommandLineException {
		if ( args.length == 0 ) {
			if ( scheme.hasCommands () ) {
				throw new MissingSubCommandException ();
			}
			return;
		}
		this.args = args;
		this.pos = 0;
		kickOff ();
	}

	/* public interface */

	@Override
	public ParsedCommand getOptions () {
		return cmdParsed;
	}

	/* private utility functions */

	private enum OptionType {
		SHORT, LONG, BREAK, NONE
	}

	private OptionType getOptionType (String repr) {
		if ( "--".equals (repr) ) { return OptionType.BREAK; }
		else if ( repr.startsWith ("--") ) { return OptionType.LONG; }
		else if (repr.startsWith ("-") ) { return OptionType.SHORT; }
		else { return OptionType.NONE; }
	}

	private void kickOff () throws CommandLineException {
		boolean broken = false;

		glob: while (! atEnd ()) {
			String arg = peek ().trim ();
			switch (getOptionType (arg)) {
				case SHORT: handleShortOption (); break;
				case LONG: handleLongOption (); break;
				case BREAK: consume (); broken = true; break glob;
				case NONE: break glob;
				default: break glob;
			}
		}

		if ( (atEnd () || broken) && scheme.hasCommands () ) {
			throw new MissingSubCommandException ();
		}

		if ( atEnd () ) { return; }

		if ( broken || ! scheme.hasCommands () ) {
			handleArgs ();
			return;
		}

		Command command = handleCommand ();
		cmdParsed.setCommand (command);
		Parser parser = new CmdSchemeParser (command.getOptionScheme ());
		cmdParsed.setParsed (parser.getOptions ());
		parser.parse (tail ());
	}

	private void handleLongOption ()
	throws CommandLineException {
		String repr = consume ();

		assert ( repr.startsWith ("--") );
		assert ( repr.length () > 2 );

		String [] parsed = repr.split ("=", 2);

		repr = parsed [0].substring (2).toLowerCase ().replace ("-", " ");

		if ( parsed.length == 2 ) {
			addOption (parsed[0], parsed[1]);
		} else {
			addOption (parsed[0], "");
		}
	}

	private void handleShortOption ()
	throws CommandLineException {
		String repr = consume ();
		assert ( repr.startsWith ("-") );
		assert ( repr.length () > 1 );

		// check for possible value after this option.
		if ( repr.length () == 2 ) {
			repr = repr.substring (1);
			if ( ! scheme.hasOption (repr) ) { throw new UnknownOptionException (repr); }

			if ( atEnd () || ! (scheme.getOption (repr) instanceof ValueOption)
					|| getOptionType (peek ()) != OptionType.NONE
			) {
				addOption (repr, "");
			} else {
				addOption (repr, consume ());
			}
			return;
		}

		// go over every char and try to add the option.
		for (int i = 1; i < repr.length (); i++) {
			String s = repr.substring (i, i+1);
			if ( ! scheme.hasOption (s) ) { throw new UnknownOptionException (s); }
			cmdParsed.add (scheme.getOption (s));
		}
	}

	private Command handleCommand () throws MissingSubCommandException {
		if ( atEnd () ) { throw new MissingSubCommandException (); }

		String repr = consume ();
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

	private boolean atEnd () {
		return pos == args.length;
	}

	private String peek () {
		return args[pos];
	}

	private String [] tail () {
		if ( atEnd () ) { return new String [] {}; }
		return Arrays.copyOfRange(args, pos, args.length);
	}

	private String consume () {
		if ( pos >= args.length ) { return ""; }
		String arg = args[pos];
		pos++;
		return arg;
	}

}
