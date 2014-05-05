package org.tutske.cmdargs;


public interface ParsedCommand {

	public boolean hasCommand ();
	public Command getCommand ();
	public ParsedCommand getParsedCommand ();

	public boolean isOptionPresent (Option option);
	public <T> T getOptionValue (ValueOption<T> option);

	public boolean isArgumentPresent (Argument<?> argument);
	public <T> T getArgumentValue (Argument<T> argument);

	public String [] getArgumentValues ();

}
