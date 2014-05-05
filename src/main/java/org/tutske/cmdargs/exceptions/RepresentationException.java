package org.tutske.cmdargs.exceptions;


public class RepresentationException extends CommandLineException{

	private static final long serialVersionUID = 1L;

	public RepresentationException () { super (); }
	public RepresentationException (String msg) { super (msg); }
	public RepresentationException (Throwable cause) { super (cause); }
	public RepresentationException (String msg, Throwable cause) { super (msg, cause); }

}
