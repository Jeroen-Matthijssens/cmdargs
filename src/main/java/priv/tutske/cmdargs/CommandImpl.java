package priv.tutske.cmdargs;

import org.tutske.cmdargs.CommandScheme;
import org.tutske.cmdargs.Command;


public class CommandImpl implements Command {

	private static final String WRONG_REPR = "The command `%s` is not valid";

	private String repr;
	private CommandScheme scheme;

	/* Constructors */

	public CommandImpl (String command) {
		this (command, CommandSchemeImpl.newInstance ());
	}

	public CommandImpl (String command, CommandScheme scheme) {
		if ( command == null || ! command.matches ("[a-zA-Z]+") ) {
			String msg = String.format (WRONG_REPR, command);
			throw new IllegalArgumentException (msg);
		}

		this.repr = toStandardRepresentation (command);
		this.scheme = scheme;
	}

	/* implementing Command */

	public boolean matches (String representation) {
		String standard = toStandardRepresentation (representation);
		return getRepresentation ().equals (standard);
	}

	public String getRepresentation () {
		return repr;
	}

	public CommandScheme getCommandScheme () {
		return scheme;
	}

	@Override
	public String toString () {
		return String.format ("<CommandImpl: %s>", getRepresentation ());
	}

	/* private utility functions */

	private String toStandardRepresentation (String representation) {
		return representation.trim ().toLowerCase ();
	}

}
