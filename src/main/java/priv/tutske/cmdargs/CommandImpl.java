package priv.tutske.cmdargs;

import org.tutske.cmdargs.CommandScheme;
import org.tutske.cmdargs.Command;

class CommandImpl implements Command {

	private String repr;
	private CommandScheme scheme;

	/* Constructors */

	public CommandImpl (String command) {
		this (command, new CommandSchemeImpl ());
	}

	public CommandImpl (String command, CommandScheme scheme) {
		if ( command == null || command.length () == 0 ) {
			String msg = "command can not be empty or null";
			throw new IllegalArgumentException (msg);
		}

		if ( command.indexOf (" ") != -1 ) {
			String msg = "Sub commands can not contain spaces.";
			throw new IllegalArgumentException (msg);
		}

		this.repr = toStandardRepresentation (command);
		this.scheme = scheme;
	}

	/* implemetning SubCommand */

	public boolean matches (String representation) {
		String standard = toStandardRepresentation (representation);
		return getRepresentation ().equals (standard);
	}

	public String getRepresentation () {
		return repr;
	}

	public CommandScheme getOptionScheme () {
		return scheme;
	}

	/* private utility functios */

	private String toStandardRepresentation (String representation) {
		return representation.trim ().toLowerCase ();
	}

}
