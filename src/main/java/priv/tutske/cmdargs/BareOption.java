package priv.tutske.cmdargs;

import java.util.EnumSet;
import java.util.Set;

import org.tutske.cmdargs.Option;
import org.tutske.cmdargs.Option.Requirement;
import org.tutske.cmdargs.exceptions.RepresentationException;


public abstract class BareOption {

	protected String longRepr;
	protected String shortRepr;
	protected Set<Requirement> requirements;

	/* constructors */

	public BareOption (String longRepr) {
		this (longRepr, Requirement.RequireNone);
	}

	public BareOption (String longRepr, Requirement requirement){
		this (longRepr, null, requirement);
	}

	public BareOption (String longRepr, String shortRepr) {
		this (longRepr, shortRepr, Requirement.RequireNone);
	}

	public BareOption (String longRepr, String shortRepr, Requirement requirement) {
		longRepr = new ReprNormalizer (longRepr).getLong ();

		ReprNormalizer normalizer = new ReprNormalizer (shortRepr);
		if ( ! normalizer.isShort () ) { throw new RepresentationException (shortRepr); }

		this.longRepr = longRepr;
		this.shortRepr = normalizer.getShort ();
		this.requirements = EnumSet.of (requirement);
	}

	/* partial implementation for options */

	public boolean matches (String option) {
		ReprNormalizer normalizer = new ReprNormalizer (option);
		if ( normalizer.isShort () ) { return normalizer.getShort ().equals (shortRepr); }
		return normalizer.getLong ().equals (getRepresentation ());
	}

	public boolean hasRequirement (Requirement requirement) {
		return requirements.contains (requirement);
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
