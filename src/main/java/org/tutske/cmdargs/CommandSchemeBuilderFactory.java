package org.tutske.cmdargs;

import priv.tutske.cmdargs.CommandSchemeBuilderImpl;


public class CommandSchemeBuilderFactory {

	public static CommandSchemeBuilder newInstance () {
		return new CommandSchemeBuilderImpl ();
	}

}
