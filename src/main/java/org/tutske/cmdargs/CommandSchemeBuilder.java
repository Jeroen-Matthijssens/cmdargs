package org.tutske.cmdargs;


public interface CommandSchemeBuilder {

	public CommandScheme buildScheme ();
	public Command buildCommand (String command);

	public CommandSchemeBuilder addOption (Option option);
	public <T> CommandSchemeBuilder addOption (ValueOption<T> option, T value);
	public CommandSchemeBuilder addArgument (Argument<?> argument);
	public CommandSchemeBuilder addCommand (Command command);

}
