package org.tutske.cmdargs;

import java.util.List;


public interface ParsedCommand {

	public boolean hasCommand ();
	public Command getCommand ();
	public ParsedCommand getParsedCommand ();

	public boolean hasOption (Option option);
	public <T> T getOptionValue (ValueOption<T> option);
	public <T> List<T> getOptionValues (ValueOption<T> option);

	public boolean hasArgument (Argument<?> argument);
	public <T> T getArgumentValue (Argument<T> argument);

	public String [] getArgumentValues ();

}
