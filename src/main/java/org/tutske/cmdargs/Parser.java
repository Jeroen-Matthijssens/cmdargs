package org.tutske.cmdargs;

import java.io.OutputStream;


public interface Parser {

	public ParsedCommand parse (String [] args);
	public void printError ();
	public void printError (OutputStream stream);

}
