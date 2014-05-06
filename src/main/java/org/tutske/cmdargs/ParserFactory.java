package org.tutske.cmdargs;

import priv.tutske.cmdargs.ParserImpl;


public class ParserFactory {

	public static Parser newInstance (CommandScheme scheme) {
		return new ParserImpl (scheme);
	}

}
