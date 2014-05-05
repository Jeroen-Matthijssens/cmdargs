package org.tutske.cmdargs;

import priv.tutske.cmdargs.*;


public class NumberValueOption extends BareValueOption implements ValueOption<Long> {

	/* constructors */

	public NumberValueOption (String longRepr) {
		super (longRepr);
	}

	public NumberValueOption (String longRepr, boolean required){
		super (longRepr, required);
	}

	public NumberValueOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public NumberValueOption (String longRepr, String shortRepr, boolean required) {
		super (longRepr, shortRepr, required);
	}

	@Override
	public Validator<Long> getValidator (String representation) {
		// TODO Auto-generated method stub
		return NumberValidator.newInstance ();
	}

}
