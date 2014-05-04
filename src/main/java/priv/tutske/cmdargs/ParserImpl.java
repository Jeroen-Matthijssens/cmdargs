package priv.tutske.cmdargs;

import java.util.List;

import org.tutske.cmdargs.*;
import org.tutske.cmdargs.exceptions.*;


public class ParserImpl implements Parser {

	private CommandScheme globalScheme;
	private List<Command> commands;

	private ParsedOptionsImpl globalParsed;
	private ParsedOptionsImpl cmdParsed;
	private String command;

	/* for parsing the args */
	private String [] args;
	private int pos;
	private CommandScheme scheme;

	/* Constructors */

	private ParserImpl () {
		globalParsed = new ParsedOptionsImpl (new MainCommand ());
	}

	public ParserImpl (CommandScheme scheme) {
		this ();
		this.globalScheme = scheme;
	}

	public ParserImpl (CommandScheme globalScheme, List<Command> subCommands) {
		this ();
		this.globalScheme = globalScheme;
		this.commands = subCommands;
	}

	/* public methods */

	@Override
	public void parse (String [] args) throws CommandLineException {
		if ( args.length == 0 ) { return; }
		this.args = args;
		this.pos = 0;
		kickOff ();
	}

	/* public interface */

	@Override
	public ParsedOptions getOptions () {
		if (commands == null || commands.size () == 0) { return globalParsed; }
		else { return cmdParsed; }
	}

	public ParsedOptions getGlobalOptions () {
		return globalParsed;
	}

	public boolean hasSubCommand () {
		return command != null;
	}

	public String getSubCommand () {
		return command;
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
				case SHORT: handleShortOption (globalScheme, globalParsed); break;
				case LONG: handleLongOption (globalScheme, globalParsed); break;
				case BREAK: consume (); broken = true; break glob;
				case NONE: break glob;
				default: break glob;
			}
		}

		if ( atEnd () && commands != null ) {
			throw new MissingSubCommandException ();
		}

		if ( atEnd () ) { return; }

		if ( broken && commands != null ) { throw new MissingSubCommandException (); }

		if ( broken || commands == null ) {
			handleArgs ();
			return;
		}

		handleCommand ();
		cmd: while (! atEnd ()) {
			String arg = peek ().trim ();
			switch (getOptionType (arg)) {
				case SHORT: handleShortOption (scheme, cmdParsed); break;
				case LONG: handleLongOption (scheme, cmdParsed); break;
				case BREAK: consume (); break cmd;
				case NONE: break cmd;
				default: break cmd;
			}
		}

		if ( atEnd () ) { return; }

		handleArgs ();
	}

	private void handleLongOption (CommandScheme scheme, ParsedOptionsImpl options)
	throws CommandLineException {
		String repr = consume ();

		assert ( repr.startsWith ("--") );
		assert ( repr.length () > 2 );

		String [] parsed = repr.split ("=", 2);

		repr = parsed [0].substring (2).toLowerCase ().replace ("-", " ");

		if ( parsed.length == 2 ) {
			addOption (parsed[0], parsed[1], scheme, options);
		} else {
			addOption (parsed[0], "", scheme, options);
		}
	}

	private void handleShortOption (CommandScheme scheme, ParsedOptionsImpl options)
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
				addOption (repr, "", scheme, options);
			} else {
				addOption (repr, consume (), scheme, options);
			}
			return;
		}

		// go over every char and try to add the option.
		for (int i = 1; i < repr.length (); i++) {
			String s = repr.substring (i, i+1);
			if ( ! scheme.hasOption (s) ) { throw new UnknownOptionException (s); }
			options.add (scheme.getOption (s));
		}
	}

	private void handleCommand () throws MissingSubCommandException {
		if ( atEnd () ) { throw new MissingSubCommandException (); }

		String repr = consume ();
		for (Command cmd : commands ) {
			if ( ! cmd.matches (repr) ) { continue; }
			cmdParsed = new ParsedOptionsImpl (cmd);
			scheme = cmd.getOptionScheme ();
			command = cmd.getRepresentation ();
			return;
		}

		String msg = "What was supposed to be the command is not a valid one.";
		throw new MissingSubCommandException (msg);
	}

	private void handleArgs () {
	}

	private void addOption (String repr, String value, CommandScheme scheme
		, ParsedOptionsImpl options
	) throws CommandLineException {
		if ( ! scheme.hasOption (repr) ) { throw new UnknownOptionException ("`" + repr + "`"); }
		Option option = scheme.getOption (repr);

		if ( ! (option instanceof ValueOption) ) {
			options.add (option);
			return;
		}
		@SuppressWarnings("unchecked")
		ValueOption<Object> valueOption = (ValueOption<Object>) option;

		Validator<Object> validator = valueOption.getValidator (repr);
		if ( ! validator.isValid (value) ) { throw new WrongValueException (); }
		options.add (valueOption, validator.parse (value));
	}

	private boolean atEnd () {
		return pos == args.length;
	}

	private String peek () {
		return args[pos];
	}

	private String consume () {
		if ( pos >= args.length ) { return ""; }
		String arg = args[pos];
		pos++;
		return arg;
	}

}
