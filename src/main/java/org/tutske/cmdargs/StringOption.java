package org.tutske.cmdargs;

import java.util.Set;

import priv.tutske.cmdargs.BareOption;
import priv.tutske.cmdargs.NoopValidator;


public class StringOption extends BareOption implements ValueOption<String> {

	public StringOption (String longRepr) {
		super (longRepr);
	}

	public StringOption (String longRepr, Requirement requirement) {
		super (longRepr, requirement);
	}

	public StringOption (String longRepr, Set<Requirement> requirements) {
		super (longRepr, requirements);
	}

	public StringOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public StringOption (String longRepr, String shortRepr, Requirement requirement) {
		super (longRepr, shortRepr, requirement);
	}

	public StringOption (String longRepr, String shortRepr, Set<Requirement> requirements) {
		super (longRepr, shortRepr, requirements);
	}

	@Override
	public Validator<String> getValidator (String longRepr) {
		return NoopValidator.newInstance ();
	}

	@Override
	public String toString () {
		return String.format ("<StringValueOption: `%s`>", getRepresentation ());
	}

}
