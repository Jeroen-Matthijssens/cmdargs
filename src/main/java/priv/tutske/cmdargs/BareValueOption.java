package priv.tutske.cmdargs;

import org.tutske.cmdargs.Option;
import org.tutske.cmdargs.Option.Requirement;


public class BareValueOption extends BareOption {

	/* constructors */

	public BareValueOption (String longRepr) {
		super (longRepr);
	}

	public BareValueOption (String longRepr, Requirement requirement){
		super (longRepr, requirement);
	}

	public BareValueOption (String longRepr, String shortRepr) {
		super (longRepr, shortRepr);
	}

	public BareValueOption (String longRepr, String shortRepr, Requirement requirement) {
		super (longRepr, shortRepr, requirement);
	}

	/* implementing options interface */

	@Override
	public boolean equals (Object other) {
		if ( ! (other instanceof Option) ) { return false; }
		Option otherOption = (Option) other;
		return otherOption.getRepresentation ().equals (getRepresentation ());
	}

}
