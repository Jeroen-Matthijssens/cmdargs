package priv.tutske.cmdargs;

import org.tutske.cmdargs.Command;
import org.tutske.cmdargs.CommandScheme;


public class MainCommand implements Command {

	private CommandScheme scheme;

	public MainCommand () {
	}

	public MainCommand (CommandScheme scheme) {
		this.scheme = scheme;
	}

	public boolean matches (String representation) { return false; }
	public String getRepresentation () { return "MAIN"; }
	public CommandScheme getCommandScheme () { return scheme; }

}
