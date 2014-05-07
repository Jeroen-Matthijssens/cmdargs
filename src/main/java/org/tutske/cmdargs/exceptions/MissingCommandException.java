package org.tutske.cmdargs.exceptions;

import org.tutske.cmdargs.*;


public class MissingCommandException extends CommandLineException {
	
	public static final String tpl = "Error: missing option `%s`!";
	private static final long serialVersionUID = 1L;

	private Option option;

	public MissingCommandException () { super (); }
	public MissingCommandException (String msg) { super (msg); }
	public MissingCommandException (Throwable cause) { super (cause); }
	public MissingCommandException (String msg, Throwable cause) { super (msg, cause); }

	public MissingCommandException (Option option) {
		this (constructMessage (option));
		this.option = option;
	}

	public static String constructMessage (Option option) {
		return String.format (tpl, option.toString ());
	}

	public String displayError () {
		return constructMessage (option);
	}

}
