package org.tutske.cmdargs;

import priv.tutske.cmdargs.*;


public class NumberOption extends BareValueOption implements ValueOption<Long> {

	/* constructors */

	public NumberOption (String longRepr) {
		super (longRepr);
	}

	public NumberOption (String longRepr, boolean required){
		super (longRepr, required);
	}

	public NumberOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public NumberOption (String longRepr, String shortRepr, boolean required) {
		super (longRepr, shortRepr, required);
	}

	@Override
	public Validator<Long> getValidator (String representation) {
		// TODO Auto-generated method stub
		return NumberValidator.newInstance ();
	}

}
