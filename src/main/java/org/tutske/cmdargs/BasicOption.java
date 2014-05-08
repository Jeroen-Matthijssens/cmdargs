package org.tutske.cmdargs;

import priv.tutske.cmdargs.BareOption;


public class BasicOption extends BareOption implements Option {

	/* constructors */

	public BasicOption (String longRepr) {
		super (longRepr);
	}

	public BasicOption (String longRepr, Requirement requirement){
		super (longRepr, requirement);
	}

	public BasicOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public BasicOption (String longRepr, String shortRepr, Requirement requirement) {
		super (longRepr, shortRepr, requirement);
	}

	@Override
	public String toString () {
		return String.format ("<BasicOption: `%s`, `%s`>"
			, getRepresentation (), getShortRepresentation ()
		);
	}

}
