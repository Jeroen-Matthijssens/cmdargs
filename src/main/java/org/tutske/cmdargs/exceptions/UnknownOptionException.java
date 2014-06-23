package org.tutske.cmdargs.exceptions;


public class UnknownOptionException extends CommandLineException {

	private static final long serialVersionUID = 1L;
	private static final String tpl = "Error: unknown option `%s`!";

	public UnknownOptionException () { super (); }
	public UnknownOptionException (String msg) { super (constructMessage (msg)); }
	public UnknownOptionException (Throwable cause) { super (cause); }
	public UnknownOptionException (String msg, Throwable cause) { super (msg, cause); }

	private static String constructMessage (String repr) {
		return String.format (tpl,  repr);
	}

}
