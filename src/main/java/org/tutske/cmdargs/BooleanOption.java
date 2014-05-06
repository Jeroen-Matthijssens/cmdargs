package org.tutske.cmdargs;

import priv.tutske.cmdargs.*;


public class BooleanOption extends BareOption implements ValueOption<Boolean> {

	/* -- Constructors -- */

	public BooleanOption (String longRepr) {
		super (longRepr);
	}

	public BooleanOption (String longRepr, boolean required){
		super (longRepr, required);
	}

	public BooleanOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public BooleanOption (String longRepr, String shortRepr, boolean required) {
		super (longRepr, shortRepr, required);
	}

	/* implement remaining parts of the Option interface */

	public boolean acceptsValue () {
		return true;
	}

	public boolean matches (String option) {
		boolean b = super.matches (option);
		if ( b ) { return b; }
		if ( option.length () < 2 ) { return false; }

		String repr = new ReprNormalizer (option).getLong ();
		String not = new ReprNormalizer ("not", getRepresentation ()).getLong ();
		String no = new ReprNormalizer ("no", getRepresentation ()).getLong ();
		return repr.equals (not) || repr.equals (no);
	}

	@Override
	public Validator<Boolean> getValidator (String longRepr) {
		longRepr = longRepr.trim ().toLowerCase ();
		if ( longRepr.startsWith ("--not-") || longRepr.startsWith ("--no-") ) {
			return new BooleanValidator (BooleanValidator.Type.Reversed);
		} else { 
			return new BooleanValidator ();
		}
	}

	@Override
	public String toString () {
		return String.format ("<BooleanOption: `%s`>", getRepresentation ());
	}

}
