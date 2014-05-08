package org.tutske.cmdargs.exceptions;


public class MissingCommandException extends CommandLineException {

	private static final long serialVersionUID = 1L;
	public static final String tpl = "Error: missing command!";

	public MissingCommandException () { this (tpl); }
	public MissingCommandException (String msg) { super (msg); }
	public MissingCommandException (Throwable cause) { super (cause); }
	public MissingCommandException (String msg, Throwable cause) { super (msg, cause); }

}
