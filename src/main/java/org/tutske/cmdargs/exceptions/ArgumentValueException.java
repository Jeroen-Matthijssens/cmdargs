package org.tutske.cmdargs.exceptions;

import org.tutske.cmdargs.*;


public class ArgumentValueException extends CommandLineException {

	private static final long serialVersionUID = 1L;
	private static final String tpl = "Error: value for `%s` for argument `%s is not valid!";

	public ArgumentValueException () { super (); }
	public ArgumentValueException (String msg) { super (msg); }
	public ArgumentValueException (Throwable cause) { super (cause); }
	public ArgumentValueException (String msg, Throwable cause) { super (msg, cause); }

	public ArgumentValueException (Argument<?> argument, String value) {
		this (constructMessage (argument, value));
	}

	private static String constructMessage (Argument<?> argument, String value) {
		return String.format (tpl, value, argument.getRepresentation ());
	}

}
