package priv.tutske.cmdargs;

import org.tutske.cmdargs.Option;


public abstract class BareOption {

	protected String longRepr;
	protected String shortRepr;
	protected boolean required;

	/* constructors */

	public BareOption (String longRepr) {
		this (longRepr, false);
	}

	public BareOption (String longRepr, boolean required){
		this (longRepr, null, required);
	}

	public BareOption (String longRepr, String shortRepr) {
		this (longRepr, shortRepr, false);
	}

	public BareOption (String longRepr, String shortRepr, boolean required) {
		longRepr = new ReprNormalizer (longRepr).normalize ();

		if ( shortRepr != null && shortRepr.length () != 1 ) {
			throw new IllegalArgumentException ("Short Options can only be one char.");
		}

		this.longRepr = longRepr;
		this.shortRepr = shortRepr;
		this.required = required;
	}

	/* partial implementation for options */

	public boolean matches (String option) {
		if (option.length () < 2 && shortRepr != null ) {
			return option.equals (shortRepr);
		}
		if ( option.length () < 2 ) { return false; }
		String repr = new ReprNormalizer (option).normalize ();
		return repr.equals (getRepresentation ());
	}

	public boolean isRequired () {
		return required;
	}

	public String getRepresentation () {
		return longRepr;
	}

	public String getDescription () {
		return "";
	}

	public boolean hasShortRepresentation () {
		return shortRepr != null;
	}

	public String getShortRepresentation () {
		return shortRepr;
	}

	@Override
	public boolean equals (Object other) {
		if ( ! (other instanceof Option) ) { return false; }
		Option otherOption = (Option) other;
		return otherOption.getRepresentation ().equals (getRepresentation ());
	}

}
