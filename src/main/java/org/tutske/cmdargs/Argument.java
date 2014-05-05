package org.tutske.cmdargs;


public interface Argument<T> {

	public String getRepresentation ();
	public int getPosition ();
	public boolean isRequired ();
	public boolean matches (String represenation);
	public Validator<T> getValidator ();

}
