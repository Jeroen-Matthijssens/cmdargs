package org.tutske.cmdargs;


public interface Argument<T> extends Comparable<Argument<?>> {

	public String getRepresentation ();
	public int getPosition ();
	public boolean isRequired ();
	public boolean matches (String representation);
	public Validator<T> getValidator ();

}
