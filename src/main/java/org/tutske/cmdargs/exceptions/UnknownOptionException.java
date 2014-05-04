package org.tutske.cmdargs.exceptions;

public class UnknownOptionException extends CommandLineException {

	private static final long serialVersionUID = 1L;

	public UnknownOptionException () { super (); }
	public UnknownOptionException (String msg) { super (msg); }
	public UnknownOptionException (Throwable cause) { super (cause); }
	public UnknownOptionException (String msg, Throwable cause) { super (msg, cause); }

}
