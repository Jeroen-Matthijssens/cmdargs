package org.tutske.cmdargs.exceptions;

import org.tutske.cmdargs.*;


public class OptionValueException extends CommandLineException {

	private static final long serialVersionUID = 1L;
	private static final String tpl = "Error: value `%s` for option `%s` is not valid!";
	private static final String missing = "Error: missing value for `%s`!";

	public OptionValueException () { super (); }
	public OptionValueException (String msg) { super (msg); }
	public OptionValueException (Throwable cause) { super (cause); }
	public OptionValueException (String msg, Throwable cause) { super (msg, cause); }

	public OptionValueException (Option option, String value) {
		this (constructMessage (option, value));
	}

	private static String constructMessage (Option option, String value) {
		String repr = option.getRepresentation ();
		if ( value == null ) { return String.format (missing, repr); }
		else { return String.format (tpl, value, repr); }
	}

}
