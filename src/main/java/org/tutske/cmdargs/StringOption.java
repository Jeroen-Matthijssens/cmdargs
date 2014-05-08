package org.tutske.cmdargs;

import priv.tutske.cmdargs.BareValueOption;
import priv.tutske.cmdargs.NoopValidator;


public class StringOption extends BareValueOption implements ValueOption<String> {

	/* constructors */

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

	/* implementing ValueOption interface */

	@Override
	public Validator<String> getValidator (String longRepr) {
		return NoopValidator.newInstance ();
	}

	@Override
	public String toString () {
		return String.format ("<StringValueOption: `%s`>", getRepresentation ());
	}

}
