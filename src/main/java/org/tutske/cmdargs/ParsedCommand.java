package org.tutske.cmdargs;


public interface ParsedCommand {

	public boolean hasCommand ();
	public boolean isPresent (String representation);
	public boolean isPresent (Option option);

	public Command getCommand ();
	public ParsedCommand getParsed ();

	public <T> T getValue (String representation);
	public <T> T getValue (ValueOption<T> option);

}
