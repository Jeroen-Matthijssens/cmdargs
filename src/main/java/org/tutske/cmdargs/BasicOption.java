package org.tutske.cmdargs;

import priv.tutske.cmdargs.BareOption;


public class BasicOption extends BareOption implements Option {

	/* constructors */

	public BasicOption (String longRepr) {
		super (longRepr);
	}

	public BasicOption (String longRepr, boolean required){
		super (longRepr, required);
	}

	public BasicOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public BasicOption (String longRepr, String shortRepr, boolean required) {
		super (longRepr, shortRepr, required);
	}

	@Override
	public String toString () {
		return String.format ("<BasicOption: `%s`, `%s`>"
			, getRepresentation (), getShortRepresentation ()
		);
	}

}
