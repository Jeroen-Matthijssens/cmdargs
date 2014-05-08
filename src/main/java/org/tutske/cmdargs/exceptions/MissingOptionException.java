package org.tutske.cmdargs.exceptions;

import org.tutske.cmdargs.*;


public class MissingOptionException extends CommandLineException {

	private static final long serialVersionUID = 1L;
	public static final String tpl = "Error: missing option `%s`!";

	public MissingOptionException () { super (); }
	public MissingOptionException (String msg) { super (msg); }
	public MissingOptionException (Throwable cause) { super (cause); }
	public MissingOptionException (String msg, Throwable cause) { super (msg, cause); }

	public MissingOptionException (Option option) {
		this (constructMessage (option));
	}

	public static String constructMessage (Option option) {
		return String.format (tpl, option.toString ());
	}

}
