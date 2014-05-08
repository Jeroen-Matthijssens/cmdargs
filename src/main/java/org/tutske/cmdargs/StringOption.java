package org.tutske.cmdargs;

import priv.tutske.cmdargs.BareOption;
import priv.tutske.cmdargs.NoopValidator;


public class StringOption extends BareOption implements ValueOption<String> {

	public StringOption (String longRepr) {
		super (longRepr);
	}

	public StringOption (String longRepr, Requirement requirement){
		super (longRepr, requirement);
	}

	public StringOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public StringOption (String longRepr, String shortRepr, Requirement requirement) {
		super (longRepr, shortRepr, requirement);
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
