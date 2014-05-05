package priv.tutske.cmdargs;

import org.tutske.cmdargs.Option;
import org.tutske.cmdargs.exceptions.RepresentationException;


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
		longRepr = new ReprNormalizer (longRepr).getLong ();

		ReprNormalizer normalizer = new ReprNormalizer (shortRepr);
		if ( ! normalizer.isShort () ) { throw new RepresentationException (shortRepr); }

		this.longRepr = longRepr;
		this.shortRepr = normalizer.getShort ();
		this.required = required;
	}

	/* partial implementation for options */

	public boolean matches (String option) {
		ReprNormalizer normalizer = new ReprNormalizer (option);
		if ( normalizer.isShort () ) { return normalizer.getShort ().equals (shortRepr); }
		return normalizer.getLong ().equals (getRepresentation ());
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
