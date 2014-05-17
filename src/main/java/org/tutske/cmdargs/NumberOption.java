package org.tutske.cmdargs;

import java.util.Set;

import priv.tutske.cmdargs.*;


public class NumberOption extends BareOption implements ValueOption<Long> {

	public NumberOption (String longRepr) {
		super (longRepr);
	}

	public NumberOption (String longRepr, Requirement requirement) {
		super (longRepr, requirement);
	}

	public NumberOption (String longRepr, Set<Requirement> requirements) {
		super (longRepr, requirements);
	}

	public NumberOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public NumberOption (String longRepr, String shortRepr, Requirement requirement) {
		super (longRepr, shortRepr, requirement);
	}

	public NumberOption (String longRepr, String shortRepr, Set<Requirement> requirements) {
		super (longRepr, shortRepr, requirements);
	}

	@Override
	public Validator<Long> getValidator (String representation) {
		return NumberValidator.newInstance ();
	}

}
