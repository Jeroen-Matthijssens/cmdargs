package org.tutske.cmdargs.exceptions;


public class CommandLineException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CommandLineException () { super (); }
	public CommandLineException (String msg) { super (msg); }
	public CommandLineException (Throwable cause) { super (cause); }
	public CommandLineException (String msg, Throwable cause) { super (msg, cause); }

}
