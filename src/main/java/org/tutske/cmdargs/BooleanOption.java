package org.tutske.cmdargs;

import java.util.Set;

import priv.tutske.cmdargs.*;


public class BooleanOption extends BareOption implements ValueOption<Boolean> {

	/* -- Constructors -- */

	public BooleanOption (String longRepr) {
		super (longRepr);
	}

	public BooleanOption (String longRepr, Requirement requirement) {
		super (longRepr, requirement);
	}

	public BooleanOption (String longRepr, Set<Requirement> requirements) {
		super (longRepr, requirements);
	}

	public BooleanOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public BooleanOption (String longRepr, String shortRepr, Requirement requirement) {
		super (longRepr, shortRepr, requirement);
	}

	public BooleanOption (String longRepr, String shortRepr, Set<Requirement> requirements) {
		super (longRepr, shortRepr, requirements);
	}

	/* implement remaining parts of the Option interface */

	@Override
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
