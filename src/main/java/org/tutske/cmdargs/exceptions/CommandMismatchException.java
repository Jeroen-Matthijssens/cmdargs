package org.tutske.cmdargs.exceptions;


public class CommandMismatchException extends CommandLineException {

	private static final long serialVersionUID = 1L;
	private static final String tpl = "Expected commad but found `%s`";

	public CommandMismatchException () { super (); }
	public CommandMismatchException (String msg) { super (constructMsg (msg)); }
	public CommandMismatchException (Throwable cause) { super (cause); }
	public CommandMismatchException (String msg, Throwable cause) { super (msg, cause); }

	private static String constructMsg (String commandstring) {
		return String.format (tpl, commandstring);
	}

}
