package org.tutske.cmdargs;


public interface Command {

	public boolean matches (String representation);
	public String getRepresentation ();
	public CommandScheme getCommandScheme ();

}
