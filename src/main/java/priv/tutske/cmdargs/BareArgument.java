package priv.tutske.cmdargs;

import org.tutske.cmdargs.*;


public abstract class BareArgument {

	private boolean required;
	private String representation;
	private int position;

	public BareArgument (String representation, int position) {
		this (representation, position, true);
	}

	public BareArgument (String representation, int position, boolean required) {
		this.representation = representation;
		this.position = position;
		this.required = required;
	}

	public String getRepresentation () { return representation; }
	public int getPosition () { return position; }
	public boolean isRequired () { return required; }
	public boolean matches (String repr) { return representation.equals (repr); }

	public int compareTo (Argument<?> other) {
		return getPosition () - other.getPosition ();
	}

}
