package org.tutske.cmdargs.exceptions;

public class WrongValueException extends CommandLineException {

	private static final long serialVersionUID = 1L;

	public WrongValueException () { super (); }
	public WrongValueException (String msg) { super (msg); }
	public WrongValueException (Throwable cause) { super (cause); }
	public WrongValueException (String msg, Throwable cause) { super (msg, cause); }

}
