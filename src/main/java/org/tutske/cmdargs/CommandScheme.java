package org.tutske.cmdargs;


public interface CommandScheme {

	public boolean hasCommands ();
	public boolean hasArguments ();

	public boolean hasOption (String representation);
	public boolean hasOption (Option option);
	public boolean hasCommand (String command);
	public boolean hasCommand (Command command);

	public Option getOption (String representation);
	public Command getCommand (String command);

	public int nrOfArguments ();
	public Argument<?> getArgument (int position);

}
