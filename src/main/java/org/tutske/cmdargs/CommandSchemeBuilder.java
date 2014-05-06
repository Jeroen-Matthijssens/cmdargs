package org.tutske.cmdargs;


public interface CommandSchemeBuilder {

	public CommandScheme buildScheme ();

	public CommandSchemeBuilder add (Option option);
	public CommandSchemeBuilder add (Argument<?> argument);
	public CommandSchemeBuilder add (Command command);

	public CommandSchemeBuilder addOption (Option option);
	public CommandSchemeBuilder addArgument (Argument<?> argument);
	public CommandSchemeBuilder addCommand (Command command);

}
