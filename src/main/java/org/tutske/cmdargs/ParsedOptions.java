package org.tutske.cmdargs;


public interface ParsedOptions {

	public boolean isPresent (String representation);
	public boolean isPresent (Option option);
	public Command getCommand ();
	public <T> T getValue (String representation);
	public <T> T getValue (ValueOption<T> option);

}
