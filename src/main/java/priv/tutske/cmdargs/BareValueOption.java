package priv.tutske.cmdargs;

import org.tutske.cmdargs.exceptions.WrongValueException;


public class BareValueOption extends BareOption {

	/* constructors */

	public BareValueOption (String longRepr) {
		super (longRepr);
	}

	public BareValueOption (String longRepr, boolean required){
		super (longRepr, required);
	}

	public BareValueOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public BareValueOption (String longRepr, String shortRepr, boolean required) {
		super (longRepr, shortRepr, required);
	}

	/* implementing options interface */

	public String validateValue (String representation, String value)
	throws WrongValueException {
		return value;
	}

}
