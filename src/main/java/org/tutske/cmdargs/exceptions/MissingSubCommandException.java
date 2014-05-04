package org.tutske.cmdargs.exceptions;

public class MissingSubCommandException extends CommandLineException {

	private static final long serialVersionUID = 1L;

	public MissingSubCommandException () { super (); }
	public MissingSubCommandException (String msg) { super (msg); }
	public MissingSubCommandException (Throwable cause) { super (cause); }
	public MissingSubCommandException (String msg, Throwable cause) { super (msg, cause); }

}
