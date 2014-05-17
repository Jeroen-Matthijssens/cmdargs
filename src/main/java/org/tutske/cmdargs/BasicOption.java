package org.tutske.cmdargs;

import java.util.Set;

import priv.tutske.cmdargs.BareOption;


public class BasicOption extends BareOption implements Option {

	public BasicOption (String longRepr) {
		super (longRepr);
	}

	public BasicOption (String longRepr, Requirement requirement) {
		super (longRepr, requirement);
	}

	public BasicOption (String longRepr, Set<Requirement> requirements) {
		super (longRepr, requirements);
	}

	public BasicOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public BasicOption (String longRepr, String shortRepr, Requirement requirement) {
		super (longRepr, shortRepr, requirement);
	}

	public BasicOption (String longRepr, String shortRepr, Set<Requirement> requirements) {
		super (longRepr, shortRepr, requirements);
	}

	@Override
	public String toString () {
		return String.format ("<BasicOption: `%s`, `%s`>"
			, getRepresentation (), getShortRepresentation ()
		);
	}

}
