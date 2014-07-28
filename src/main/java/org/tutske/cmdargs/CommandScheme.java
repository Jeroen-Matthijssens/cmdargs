package org.tutske.cmdargs;

import java.util.List;
import java.util.Set;

import org.tutske.cmdargs.Option.Requirement;


public interface CommandScheme {

	public boolean hasCommands ();
	public boolean hasArguments ();

	public boolean hasOption (Option option);
	public boolean hasOption (String representation);
	public Option getOption (String representation);
	public boolean hasDefault (ValueOption<?> option);
	public <T> T getDefault (ValueOption<T> option);

	public boolean hasCommand (Command command);
	public boolean hasCommand (String command);
	public Command getCommand (String command);

	public boolean hasArgument (Argument<?> argument);
	public boolean hasArgument (String argument);
	public Argument<?> getArgument (String representation);
	public List<Argument<?>> getArguments ();

	public Set<Option> getByRequirement (Requirement requirement);
	public Set<Option> getDefaultedOptions ();

}
