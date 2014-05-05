package org.tutske.cmdargs;

import priv.tutske.cmdargs.NoopValidator;
import priv.tutske.cmdargs.BareArgument;


public class StringArgument extends BareArgument implements Argument<String> {

	public StringArgument (String representation, int position) {
		super (representation, position);
	}

	public StringArgument (String representation, int position, boolean required) {
		super (representation, position, required);
	}

	@Override
	public Validator<String> getValidator () {
		return NoopValidator.newInstance ();
	}

	@Override
	public String toString () {
		return String.format ("<StringArgument: %s>", getRepresentation ());
	}

}
