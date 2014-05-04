package org.tutske.cmdargs;


public interface Parser {

	public void parse (String [] args);
	public ParsedOptions getOptions ();

}
