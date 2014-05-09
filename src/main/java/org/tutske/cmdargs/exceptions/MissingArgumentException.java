package org.tutske.cmdargs.exceptions;

import org.tutske.cmdargs.Argument;


public class MissingArgumentException extends CommandLineException {

	private static final long serialVersionUID = 1L;
	public static final String tpl = "Error: missing required argument `%s`!";

	public MissingArgumentException () { super (); }
	public MissingArgumentException (String msg) { super (msg); }
	public MissingArgumentException (Throwable cause) { super (cause); }
	public MissingArgumentException (String msg, Throwable cause) { super (msg, cause); }

	public MissingArgumentException (Argument<?> argument) {
		this (constructMessage (argument));
	}

	public static String constructMessage (Argument<?> argument) {
		return String.format (tpl, argument.getRepresentation ());
	}

}
